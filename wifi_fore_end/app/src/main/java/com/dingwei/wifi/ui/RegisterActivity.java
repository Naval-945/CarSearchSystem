package com.dingwei.wifi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dingwei.wifi.R;
import com.dingwei.wifi.api.ApiService;
import com.dingwei.wifi.api.RetrofitClient;
import com.dingwei.wifi.api.model.ApiResponse;
import com.dingwei.wifi.api.model.EmailRequest;
import com.dingwei.wifi.api.model.RegisterRequest;
import com.dingwei.wifi.api.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
<<<<<<< HEAD
    private EditText emailEditText;
    private EditText captchaEditText;

=======
    private EditText confirm_pwdEditText;
>>>>>>> 2d836c32e69b07e19f0c76ba882e267013101b35

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.registerUsername);
        passwordEditText = findViewById(R.id.registerPassword);
<<<<<<< HEAD
        emailEditText = findViewById(R.id.emailEditText);
        captchaEditText = findViewById(R.id.verificationCodeEditText);
=======
        confirm_pwdEditText=findViewById(R.id.password_ck);

>>>>>>> 2d836c32e69b07e19f0c76ba882e267013101b35
        Button registerButton = findViewById(R.id.registerButton);
        Button captchaButton = findViewById(R.id.captchaButton);

        registerButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
<<<<<<< HEAD
            String email = emailEditText.getText().toString();
            String captcha = captchaEditText.getText().toString();
            registerUser(username, password, email, captcha);

            Log.i("registerActivity", " " + username + " " + password + " " + email + " " + captcha);
        });


        captchaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEditText.getText().toString();
                if(!"".equals(email)){
                    sendCaptcha(email);
                    // 禁用按钮
                    captchaButton.setEnabled(false);

                    new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            // 更新按钮文本，显示剩余时间
                            captchaButton.setText("剩余时间：" + millisUntilFinished / 1000 + "秒");
                        }

                        @Override
                        public void onFinish() {
                            // 重新启用按钮，恢复原始文本
                            captchaButton.setEnabled(true);
                            captchaButton.setText("获取验证码");
                        }
                    }.start();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "验证码发送失败，请输入正确的邮箱！",Toast.LENGTH_LONG).show();

                }
            }
=======
            String confirm_pwd=confirm_pwdEditText.getText().toString();

            if(password.equals(confirm_pwd)){
                registerUser(username, password);
            }else{
                Toast.makeText(this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
            }


>>>>>>> 2d836c32e69b07e19f0c76ba882e267013101b35
        });
    }

    private void registerUser(String username, String password, String email, String captcha) {
        Retrofit retrofit = RetrofitClient.getClient("http://123.249.15.162:8848/");
        ApiService apiService = retrofit.create(ApiService.class);

        RegisterRequest registerRequest = new RegisterRequest(username, password, email, captcha);
        Call<ApiResponse<User>> call = apiService.registerUser(registerRequest);

        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<User>> call, @NonNull Response<ApiResponse<User>> response) {

                Log.d("TAG", " "+response);

                if(response.body() != null && response.isSuccessful()){
                    ApiResponse<User> apiResponse = response.body();

                    Toast.makeText(RegisterActivity.this, "注册成功, 欢迎您 ：" + apiResponse.getData().getUsername(),Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    // Handle the error response
                    if (response.errorBody() != null) {
                        try {
                            // Parse the error response body
                            String errorBody = response.errorBody().string();
                            ApiResponse<User> errorResponse = new Gson().fromJson(errorBody, new TypeToken<ApiResponse<User>>(){}.getType());
                            String errorMessage = errorResponse.getMessage();
                            // Display the error message
                            Toast.makeText(RegisterActivity.this, "嘿嘿失败： "+errorMessage, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<User>> call, @NonNull Throwable t) {
                // 处理网络请求失败逻辑
                Toast.makeText(RegisterActivity.this, "网络请求失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendCaptcha(String email) {
        Retrofit retrofit = RetrofitClient.getClient("http://123.249.15.162:8848/");
        ApiService apiService = retrofit.create(ApiService.class);

        EmailRequest emailRequest = new EmailRequest(email);
        Call<ApiResponse<String>> call = apiService.sendCaptcha(emailRequest);

        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<String>> call, @NonNull Response<ApiResponse<String>> response) {
                System.out.println("Response Code: " + response.code());
                System.out.println("Response Message: " + response.message());
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        String capcha = apiResponse.getData();
                        // 处理注册成功逻辑
                        Toast.makeText(RegisterActivity.this, "验证码已发送至 " + email, Toast.LENGTH_LONG).show();
                    } else {
                        // 处理注册失败逻辑
                        Toast.makeText(RegisterActivity.this, "验证码发送失败：" + apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    try {
                        if (response.code() == 400) {
                            // 处理特定的错误代码
                            Toast.makeText(RegisterActivity.this, "注册失败: " + response.message(), Toast.LENGTH_LONG).show();
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
            public void onFailure(@NonNull Call<ApiResponse<String>> call, @NonNull Throwable t) {
                // 处理网络请求失败逻辑
                Toast.makeText(RegisterActivity.this, "网络请求失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}


