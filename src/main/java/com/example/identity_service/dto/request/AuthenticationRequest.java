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
@FieldDefaults(level = lombok.AccessLevel.PRIVATE) // Giúp đặt tất cả các trường trong lớp này có mức truy cập là
                                                   // private, đảm bảo tính đóng gói và bảo vệ dữ liệu
public class AuthenticationRequest {
    String username;
    String password;
}
