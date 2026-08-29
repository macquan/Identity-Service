package com.example.identity_service.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // Giúp tạo ra một constructor với tất cả các tham số, cần thiết cho việc
                    // khởi tạo đối tượng từ dữ liệu JSON
@NoArgsConstructor // Giúp tạo ra một constructor không có tham số, cần thiết cho việc
                   // deserialization từ JSON
@Builder
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    private String username;

    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;

}
