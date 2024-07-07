package com.wifi.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String password;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

