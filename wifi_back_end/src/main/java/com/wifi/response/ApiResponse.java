package com.wifi.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    public ApiResponse() {}

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}