package com.dingwei.wifi.api.model;

public class RegisterRequest {

    private String username;
    private String password;
    private String email;
    private String token;

    public RegisterRequest(String username, String password, String email, String captcha){
        this.username = username;
        this.password = password;
        this.email = email;
        this.token = captcha;
    }

    public RegisterRequest(){};

}
