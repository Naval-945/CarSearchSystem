package com.dingwei.wifi.model;

public class RegisterRequest {
    private String username;
    private String password;

    public RegisterRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    public RegisterRequest(){};

    // Getters and Setters
}
