package com.example.identity_service.dto.request;

import java.time.LocalDate;
import java.util.Set;

import com.example.identity_service.validator.DobConstraint;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;
    private String firstName;

    private String lastName;

    @DobConstraint(min = 50, message = "INVALID_DOB")
    private LocalDate dob;

    private Set<String> roles;
}
