package com.wifi.response;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//为了减少重复代码，创建一个辅助类来生成ApiResponse对象
public class ResponseHelper {

    public static <T> ResponseEntity<ApiResponse<T>> createResponse(HttpStatus status, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>(status.value(), message, data);
        return new ResponseEntity<>(response, status);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return createResponse(HttpStatus.OK, message, data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message) {
        return createResponse(status, message, null);
    }
}
