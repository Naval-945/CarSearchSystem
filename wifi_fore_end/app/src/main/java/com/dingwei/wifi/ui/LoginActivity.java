package com.dingwei.wifi.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dingwei.wifi.R;
import com.dingwei.wifi.api.ApiService;
import com.dingwei.wifi.api.RetrofitClient;
import com.dingwei.wifi.api.model.ApiResponse;
import com.dingwei.wifi.api.model.LoginRequest;
import com.dingwei.wifi.api.model.User;
import com.dingwei.wifi.service.WifiScanService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "WifiScanService created");
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

        requestPermissions();
    }

    private void requestPermissions() {
        String[] permissions = {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.CHANGE_WIFI_STATE,
                android.Manifest.permission.FOREGROUND_SERVICE,
                android.Manifest.permission.FOREGROUND_SERVICE_LOCATION
        };

        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && allPermissionsGranted(grantResults)) {
                Toast.makeText(this, "权限已授予", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean allPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void loginUser(String username, String password) {
        Log.d(TAG, "in loginUser(): " + username + password);
        Retrofit retrofit = RetrofitClient.getClient("http://123.249.15.162:8848/");
        ApiService apiService = retrofit.create(ApiService.class);

        LoginRequest loginRequest = new LoginRequest(username, password);
        Call<ApiResponse<User>> call = apiService.loginUser(loginRequest);

        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<User>> call, @NonNull Response<ApiResponse<User>> response) {
                System.out.println("onResponse()");
                Log.d(TAG, "Response isSuccessful: " + response.isSuccessful());
                Log.d(TAG, "Response Code: " + response.code());
                Log.d(TAG, "Response Message: " + response.message());

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();
                    Log.d(TAG, "API Response: " + apiResponse);

                    if (apiResponse.getStatus() == 200) {
                        User user = apiResponse.getData();
                        Log.i(TAG, "Login successful: " + user.getUsername());
                        Toast.makeText(LoginActivity.this, "登录成功！欢迎 " + user.getUsername(), Toast.LENGTH_LONG).show();

                        Intent serviceIntent = new Intent(LoginActivity.this, WifiScanService.class);
                        startForegroundService(serviceIntent);

                    } else {
                        Log.w(TAG, "Login failed: " + apiResponse.getMessage());
                        Toast.makeText(LoginActivity.this, "登录失败：" + apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        if (response.code() == 401) {
                            Log.e(TAG, "Unauthorized: Invalid username or password");
                            Toast.makeText(LoginActivity.this, "登录失败：错误的用户名或密码", Toast.LENGTH_LONG).show();
                        } else {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error Body: " + errorBody);
                            Toast.makeText(LoginActivity.this, "登录失败：" + errorBody, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing error response", e);
                        Toast.makeText(LoginActivity.this, "登录失败：无法解析错误信息", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<User>> call, @NonNull Throwable t) {
                Log.e(TAG, "Network request failed: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "网络请求失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
