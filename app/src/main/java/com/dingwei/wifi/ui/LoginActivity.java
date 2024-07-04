package com.dingwei.wifi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dingwei.wifi.R;
import com.dingwei.wifi.api.ApiService;
import com.dingwei.wifi.api.RetrofitClient;
import com.dingwei.wifi.model.LoginRequest;
import com.dingwei.wifi.model.User;

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
        Retrofit retrofit = RetrofitClient.getClient("http://192.168.176.139:8080/");
        ApiService apiService = retrofit.create(ApiService.class);

        LoginRequest loginRequest = new LoginRequest(username, password);
        Call<User> call = apiService.loginUser(loginRequest);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    // 处理登录成功逻辑
                    Toast.makeText(LoginActivity.this, "登录成功！欢迎 " + user.getUsername(), Toast.LENGTH_LONG).show();
                } else {
                    // 处理登录失败逻辑
                    Toast.makeText(LoginActivity.this, "登录失败：用户名或密码错误", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                // 处理网络请求失败逻辑
                Toast.makeText(LoginActivity.this, "网络请求失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}


