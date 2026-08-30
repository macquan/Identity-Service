package com.example.identity_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true) // Ignore
    @Mapping(target = "roles", ignore = true) // Ignore
    User toUser(UserCreationRequest request); // Chuyển đổi từ UserCreationRequest sang User

    UserResponse toUserResponse(User user); // Chuyển đổi từ User sang UserResponse

    List<UserResponse> toUserResponseList(List<User> users); // Chuyển đổi từ danh sách User sang danh sách UserResponse

    @Mapping(target = "id", ignore = true) // Ignore
    @Mapping(target = "username", ignore = true) // Ignore
    @Mapping(target = "roles", ignore = true) // Ignore
    void updateUser(@MappingTarget User user, UserUpdateRequest request); // @MappingTarget chỉ định đối tượng đích cần
                                                                          // cập nhật, trong trường hợp này là User.
                                                                          // Phương thức này sẽ cập nhật các trường của
                                                                          // User dựa trên dữ liệu từ UserUpdateRequest.
}
