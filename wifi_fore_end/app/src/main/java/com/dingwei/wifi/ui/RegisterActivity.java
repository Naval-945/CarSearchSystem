package com.dingwei.wifi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dingwei.wifi.R;
import com.dingwei.wifi.api.ApiService;
import com.dingwei.wifi.api.RetrofitClient;
import com.dingwei.wifi.api.model.ApiResponse;
import com.dingwei.wifi.api.model.RegisterRequest;
import com.dingwei.wifi.api.model.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirm_pwdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.registerUsername);
        passwordEditText = findViewById(R.id.registerPassword);
        confirm_pwdEditText=findViewById(R.id.password_ck);

        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirm_pwd=confirm_pwdEditText.getText().toString();

            if(password.equals(confirm_pwd)){
                registerUser(username, password);
            }else{
                Toast.makeText(this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void registerUser(String username, String password) {
        Retrofit retrofit = RetrofitClient.getClient("http://123.249.15.162:8848/");
        ApiService apiService = retrofit.create(ApiService.class);

        RegisterRequest registerRequest = new RegisterRequest(username, password);
        Call<ApiResponse<User>> call = apiService.registerUser(registerRequest);

        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<User>> call, @NonNull Response<ApiResponse<User>> response) {
                System.out.println("Response Code: " + response.code());
                System.out.println("Response Message: " + response.message());
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        User user = apiResponse.getData();
                        // 处理注册成功逻辑
                        Toast.makeText(RegisterActivity.this, "注册成功！欢迎 " + user.getUsername(), Toast.LENGTH_LONG).show();

                        // 启动主界面Activity
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // 结束当前Activity,防止用户返回到登录页面

                    } else {
                        // 处理注册失败逻辑
                        Toast.makeText(RegisterActivity.this, "注册失败：" + apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    try {
                        if (response.code() == 400) {
                            // 处理特定的错误代码
                            Toast.makeText(RegisterActivity.this, "注册失败：已存在该用户", Toast.LENGTH_LONG).show();
                        } else {
                            String errorBody = response.errorBody().string();
                            System.out.println("Error Body: " + errorBody);
                            Toast.makeText(RegisterActivity.this, "注册失败：" + errorBody, Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "注册失败：无法解析错误信息", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<User>> call, @NonNull Throwable t) {
                // 处理网络请求失败逻辑
                Toast.makeText(RegisterActivity.this, "网络请求失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}

