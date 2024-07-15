package com.dingwei.wifi.api.model;

public class LoginRequest {

    private String identifier;
    private String password;

    public LoginRequest(String username, String password) {
        this.identifier = username;
        this.password = password;
    }

    public LoginRequest(){}

    // Getters and Setters
}

