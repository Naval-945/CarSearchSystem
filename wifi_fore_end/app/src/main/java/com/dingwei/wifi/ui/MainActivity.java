package com.dingwei.wifi.ui;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import com.dingwei.wifi.MySurfaceView;
import com.dingwei.wifi.R;
import com.dingwei.wifi.service.WifiScanService;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private WifiScanService wifiScanService;
    private MySurfaceView sv_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        startWifiScanService();
        requestPermissions();

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private void startWifiScanService() {
        Intent serviceIntent = new Intent(this, WifiScanService.class);
        startService(serviceIntent);
    }

    private void stopWifiScanService() {
        Intent serviceIntent = new Intent(this, WifiScanService.class);
        stopService(serviceIntent);
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
    protected void onDestroy() {
        super.onDestroy();
        // 在活动销毁时停止 WifiScanService
        stopWifiScanService();
    }

}