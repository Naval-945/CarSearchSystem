package com.dingwei.wifi.api;

import com.dingwei.wifi.api.model.ApiResponse;
import com.dingwei.wifi.api.model.EmailRequest;
import com.dingwei.wifi.api.model.LocationInfo;
import com.dingwei.wifi.api.model.LoginRequest;
import com.dingwei.wifi.api.model.RegisterRequest;
import com.dingwei.wifi.api.model.User;
import com.dingwei.wifi.api.model.WifiData;
//import com.dingwei.wifi.api.model.WifiData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/login")
    Call<ApiResponse<User>> loginUser(@Body LoginRequest loginRequest);

    @POST("/api/register")
    Call<ApiResponse<User>> registerUser(@Body RegisterRequest registerRequest);

    @POST("/api/wifi")
    Call<ApiResponse<LocationInfo>> sendWifiData(@Body WifiData wifiData);

    @POST("/api/send-token")
    Call<ApiResponse<String>> sendCaptcha(@Body EmailRequest emailRequest);
}

