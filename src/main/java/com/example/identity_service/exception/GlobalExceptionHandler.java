package com.example.identity_service.exception;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.identity_service.dto.response.ApiResponse;
import com.nimbusds.jose.JOSEException;

@ControllerAdvice // ControllerAdvice đại diện cho một lớp xử lý ngoại lệ toàn cục, giúp xử lý các
                  // ngoại lệ phát sinh trong ứng dụng và cung cấp phản hồi thích hợp cho người
                  // dùng
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handlingException(Exception ex) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handlingAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String enumKey = ex.getBindingResult().getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY; // Default error code in case the enumKey is not found
        try {
            errorCode = ErrorCode.valueOf(enumKey); // enumKey là tên của enum trong ErrorCode
        } catch (IllegalArgumentException e) {
        }
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<ApiResponse> handlingParseException(ParseException ex) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.INVALID_TOKEN.getCode());
        apiResponse.setMessage(ErrorCode.INVALID_TOKEN.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(JOSEException.class)
    public ResponseEntity<ApiResponse> handlingJOSEException(JOSEException ex) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.INVALID_SIGNATURE.getCode());
        apiResponse.setMessage(ErrorCode.INVALID_SIGNATURE.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
