package com.dingwei.wifi.ui;

import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dingwei.wifi.R;
import com.dingwei.wifi.api.ApiService;
import com.dingwei.wifi.api.RetrofitClient;
import com.dingwei.wifi.model.RegisterRequest;
import com.dingwei.wifi.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.registerUsername);
        passwordEditText = findViewById(R.id.registerPassword);
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            registerUser(username, password);
        });
    }

    private void registerUser(String username, String password) {
        Retrofit retrofit = RetrofitClient.getClient("http://10.61.158.248:8080/");
        ApiService apiService = retrofit.create(ApiService.class);

        RegisterRequest registerRequest = new RegisterRequest(username, password);
        Call<User> call = apiService.registerUser(registerRequest);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    // 处理注册成功逻辑
                } else {
                    // 处理注册失败逻辑
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                // 处理网络请求失败逻辑
            }
        });
    }
}

