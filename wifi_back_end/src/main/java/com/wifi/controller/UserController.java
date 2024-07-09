package com.wifi.controller;

import com.wifi.entity.User;
import com.wifi.request.LoginRequest;
import com.wifi.request.RegisterRequest;
import com.wifi.response.ApiResponse;
import com.wifi.response.ResponseHelper;
import com.wifi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处理注册和登录请求
 * 使用UserService进行用户注册和登录操作，返回统一的ApiResponse响应。
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> registerUser(@RequestBody RegisterRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        try {
            User user = userService.registerUser(username, password);
            logger.info("User registration successful: {}", user.toString());
            return ResponseHelper.success("Registration successful", user);
        } catch (RuntimeException e) {
            logger.info("User registration failed: {}", e.getMessage());
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
            logger.info("User login request: username={}, password={}", username, password);
            User user = userService.loginUser(username, password);
            return ResponseHelper.success("Login successful", user);
        }catch(RuntimeException e){
            logger.info("User login failed: {}", e.getMessage());
            return ResponseHelper.error(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
