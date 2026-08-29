package com.example.identity_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception"),
    INVALID_KEY(1001, "Invalid message key"),
    USER_EXISTS(1002, "Username already exists"),
    USERNAME_INVALID(1003, "Username must be at least 3 characters long"),
    INVALID_PASSWORD(1004, "Password must be at least 8 characters long"),
    USER_NOT_EXISTED(1005, "User not existed"),
    UNAUTHENTICATED(1006, "Unauthenticated"),
    INVALID_TOKEN(1007, "Invalid token format"),
    INVALID_SIGNATURE(1008, "Invalid token signature"),;

    private int code;
    private String message;
}
