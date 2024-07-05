package com.dingwei.wifi.api;

import com.dingwei.wifi.model.ApiResponse;
import com.dingwei.wifi.model.LoginRequest;
import com.dingwei.wifi.model.RegisterRequest;
import com.dingwei.wifi.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/login")
    Call<ApiResponse<User>> loginUser(@Body LoginRequest loginRequest);

    @POST("/api/register")
    Call<ApiResponse<User>> registerUser(@Body RegisterRequest registerRequest);
}

