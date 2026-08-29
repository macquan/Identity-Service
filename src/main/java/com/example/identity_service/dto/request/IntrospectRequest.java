package com.example.identity_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
// Dùng để verify token, xem token có hợp lệ hay không, có hết hạn hay chưa, và
// lấy thông tin người dùng từ token.
public class IntrospectRequest {
    String token;
}
