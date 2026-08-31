package com.example.identity_service.dto.request;

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
@FieldDefaults(level = lombok.AccessLevel.PRIVATE) // Giúp đặt tất cả các trường trong lớp này có mức truy cập là
                                                   // private, đảm bảo tính đóng gói và bảo vệ dữ liệu
public class AuthenticationRequest {
    String username;
    String password;
}
