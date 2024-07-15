package com.wifi.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String token;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password, String email, String token) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.token = token;
    }
}

