package com.example.identity_service.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity // Entity đại diện cho một bảng trong cơ sở dữ liệu
@Data // Lombok annotation to generate getters, setters, toString, equals, and
      // hashCode methods
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob; // date of birth
}
