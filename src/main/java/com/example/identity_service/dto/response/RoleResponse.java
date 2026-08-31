package com.example.identity_service.dto.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
// Dùng để verify token, xem token có hợp lệ hay không, có hết hạn hay chưa, và
// lấy thông tin người dùng từ token.
public class RoleResponse {
    String name;
    String description;
    Set<PermissionResponse> permissions;
}
