package com.example.identity_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.identity_service.entity.User;

@Repository // Repository đại diện cho một lớp truy cập dữ liệu, cung cấp các phương thức để
            // tương tác với cơ sở dữ liệu
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username); // Kiểm tra xem có tồn tại người dùng với username đã cho hay không

    Optional<User> findByUsername(String username); // Tìm kiếm người dùng theo username
}
