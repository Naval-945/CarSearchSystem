package com.wifi.controller;

import com.wifi.entity.User;
import com.wifi.request.LoginRequest;
import com.wifi.request.RegisterRequest;
import com.wifi.response.ApiResponse;
import com.wifi.response.ResponseHelper;
import com.wifi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> registerUser(@RequestBody RegisterRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        try {
            User user = userService.registerUser(username, password);
            return ResponseHelper.success("Registration successful", user);
        } catch (RuntimeException e) {
            return ResponseHelper.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<User>> loginUser(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        //for test
        System.out.println("username: " + username + ", password: " + password);

        try{
            User user = userService.loginUser(username, password);
            return ResponseHelper.success("Login successful", user);
        }catch(RuntimeException e){
            return ResponseHelper.error(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
