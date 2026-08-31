package com.example.identity_service.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.IntrospectRequest;
import com.example.identity_service.dto.response.AuthenticationResponse;
import com.example.identity_service.dto.response.IntrospectResponse;
import com.example.identity_service.entity.User;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);
        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // var là một từ khóa trong Java 10 trở lên, cho phép trình biên dịch suy luận
        // kiểu dữ liệu của biến dựa trên giá trị được gán cho nó. Trong trường hợp này,
        // var user sẽ được suy luận là kiểu Optional<User> dựa trên kết quả trả về của
        // phương thức findByUsername.
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(request.getPassword(),
                user.getPassword()); // So sánh mật khẩu đã mã hóa với mật khẩu người dùng nhập vào

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private String generateToken(User user) {
        // Một jwt token bao gồm 3 phần: header, payload và signature. Header chứa thông
        // tin về thuật toán mã hóa và loại token, payload chứa thông tin về người dùng
        // hoặc các dữ liệu khác liên quan đến phiên làm việc, và signature được tạo ra
        // bằng cách mã hóa header và payload bằng một khóa bí mật.
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // subject là thông tin định danh của người dùng, thường là username hoặc
                                             // userId
                .issuer("macquan8.com") // người phát hành token, có thể là tên miền hoặc tên ứng dụng
                .issueTime(new Date()) // thời gian phát hành token
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())) // thời gian hết hạn token, ở đây là 1
                                                                                 // giờ kể từ thời điểm phát hành
                .claim("scope", buildScope(user))
                .build(); // claim là từng cặp key-value trong payload của JWT, chứa thông tin về người
                          // dùng hoặc các dữ liệu khác liên quan đến phiên làm việc.

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        // Tạo chữ ký cho token bằng cách sử dụng khóa bí mật. Chữ ký này sẽ được sử
        // dụng để xác thực tính toàn vẹn của token khi người dùng gửi yêu cầu đến
        // server trong tương lai.
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize(); // serialize() sẽ trả về chuỗi JWT hoàn chỉnh, bao gồm header, payload và
                                          // signature
        } catch (Exception e) {
            log.error("Cannot create token", e);
            throw new RuntimeException("Error signing the JWT token", e);
        }
    }

    private String buildScope(User user) {
        // Xây dựng chuỗi scope từ danh sách các vai trò của người dùng. Scope là một
        // khái niệm trong OAuth 2.0, đại diện cho quyền truy cập mà người dùng được
        // cấp cho ứng dụng.
        StringJoiner stringJoiner = new StringJoiner(" "); // Các vai trò của người dùng sẽ được nối với nhau bằng dấu
                                                           // cách.
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
                }
            }); // Thêm từng vai trò vào StringJoiner
        }
        return stringJoiner.toString();
    }
}
