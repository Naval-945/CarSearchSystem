package com.dingwei.wifi.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dingwei.wifi.R;
import com.dingwei.wifi.api.ApiService;
import com.dingwei.wifi.api.RetrofitClient;
import com.dingwei.wifi.model.ApiResponse;
import com.dingwei.wifi.model.LoginRequest;
import com.dingwei.wifi.model.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            loginUser(username, password);
        });

        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser(String username, String password) {
        Retrofit retrofit = RetrofitClient.getClient("http://123.249.15.162:8848/");
        ApiService apiService = retrofit.create(ApiService.class);

        LoginRequest loginRequest = new LoginRequest(username, password);
        Call<ApiResponse<User>> call = apiService.loginUser(loginRequest);

        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<User>> call, @NonNull Response<ApiResponse<User>> response) {
                System.out.println("1" + response.isSuccessful());
                System.out.println("2" + response.code());
                System.out.println("3" + response.message());

                //如果相应体返回成功
                if (response.isSuccessful() && response.body() != null) {

                    ApiResponse<User> apiResponse = response.body();
                    System.out.println("4" + apiResponse);
                    System.out.println("5" + apiResponse.getStatus());
                    System.out.println("6" + apiResponse.getMessage());
                    System.out.println("7" + apiResponse.getData());

                    //根据具体状态码处理对应逻辑
                    if (apiResponse.getStatus() == 200) {
                        User user = apiResponse.getData();
                        // 处理登录成功逻辑
                        Toast.makeText(LoginActivity.this, "登录成功！欢迎 " + user.getUsername(), Toast.LENGTH_LONG).show();
                    } else {
                        // 处理登录失败逻辑
                        Toast.makeText(LoginActivity.this, "登录失败：" + apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                //如果返回响应体为错误
                else {
                    //根据错误响应体的状态码进行对应操作
                    try {
                        if (response.code() == 401) {
                            Toast.makeText(LoginActivity.this, "登录失败：错误的用户名或密码", Toast.LENGTH_LONG).show();
                        } else {
                            String errorBody = response.errorBody().string();
                            System.out.println("Error Body: " + errorBody);
                            Toast.makeText(LoginActivity.this, "登录失败：" + errorBody, Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "登录失败：无法解析错误信息", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<User>> call, @NonNull Throwable t) {
                // 处理网络请求失败逻辑
                System.out.println("Network request failed: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "网络请求失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}


