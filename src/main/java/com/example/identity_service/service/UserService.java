package com.example.identity_service.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.Role;
import com.example.identity_service.entity.User;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.mapper.UserMapper;
import com.example.identity_service.repository.RoleRepository;
import com.example.identity_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service // Service đại diện cho một lớp dịch vụ, cung cấp các phương thức để xử lý logic
         // nghiệp vụ
@RequiredArgsConstructor // Tự động tạo ra một constructor với tất cả các trường được đánh dấu là final
                         // hoặc @NonNull, giúp khởi tạo các dependency injection một cách dễ dàng, thay
                         // cho @Autowired hoặc constructor thủ công
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTS);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)); // hoặc orElseGet(...)

        HashSet<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getMyInfo() {
        // Khi đăng nhập thành công, Spring Security sẽ lưu thông tin người dùng vào
        // SecurityContextHolder.
        var context = SecurityContextHolder.getContext();
        context.getAuthentication().getName();

        User user = userRepository.findByUsername(context.getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')") // Kiểm tra quyền trước khi thực hiện method bên dưới, chỉ cho phép người dùng
                                      // có quyền "ROLE_ADMIN" truy cập
    public List<UserResponse> getUsers() {
        return userMapper.toUserResponseList(
                userRepository.findAll());
    }

    @PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')") // Thực hiện xong method bên dưới
                                                                                       // thì mới kiểm tra quyền có phải
                                                                                       // là ADMIN hay không,...
    public UserResponse getUserById(String userId) {
        return userMapper.toUserResponse(
                userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    // @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

}
