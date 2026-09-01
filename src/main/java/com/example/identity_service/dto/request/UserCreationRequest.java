package com.example.identity_service.dto.request;

import java.time.LocalDate;

import com.example.identity_service.validator.DobConstraint;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // Giúp tạo ra một constructor với tất cả các tham số, cần thiết cho việc
                    // khởi tạo đối tượng từ dữ liệu JSON
@NoArgsConstructor // Giúp tạo ra một constructor không có tham số, cần thiết cho việc
                   // deserialization từ JSON
@Builder
public class UserCreationRequest {
    @Size(min = 4, message = "USERNAME_INVALID")
    private String username;

    @Size(min = 6, message = "INVALID_PASSWORD")
    private String password;

    private String firstName;
    private String lastName;

    @DobConstraint(min = 16, message = "INVALID_DOB")
    private LocalDate dob;

}
