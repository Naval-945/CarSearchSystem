package com.dingwei.wifi.service;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dingwei.wifi.api.ApiService;
import com.dingwei.wifi.api.RetrofitClient;
import com.dingwei.wifi.api.model.ApiResponse;
import com.dingwei.wifi.api.model.WifiData;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WifiScanService extends Service {

    private static final String TAG = "WifiScanService";
    private WifiManager wifiManager;
    private Handler handler;
    private Runnable scanRunnable;
    private ApiService apiService;

    // 指定的BSSID列表
    private static final String[] TARGET_BSSIDS = {
            "62:9e:92:e6:23:c4", "2e:3a:a3:f2:88:e2", "0e:69:6c:d6:8c:04"
            // 添加更多的BSSID
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        handler = new Handler();
        Retrofit retrofit = RetrofitClient.getClient("http://123.249.15.162:8848/");
        apiService = retrofit.create(ApiService.class);

        scanRunnable = new Runnable() {
            @Override
            public void run() {
                if (wifiManager != null) {
                    boolean success = wifiManager.startScan();
                    if (!success) {
                        Log.e(TAG, "WiFi scan failed to start.");
                    }
                }
                handler.postDelayed(this, 3000); // 3秒扫描一次
            }
        };
        handler.post(scanRunnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("wifiInfo")) {
            String wifiInfo = intent.getStringExtra("wifiInfo");
            Log.d(TAG, "Received wifiInfo: " + wifiInfo);
            sendWifiInfoToServer(wifiInfo);
        }
        return START_STICKY;
    }

    private void sendWifiInfoToServer(String wifiInfo) {
        Map<String, Integer> wifiData = new HashMap<>();
        String[] lines = wifiInfo.split("\n");
        for (String line : lines) {
            String[] parts = line.split(", ");
            String bssid = parts[1].split(": ")[1];
            int rssi = Integer.parseInt(parts[2].split(": ")[1]);

            // 只发送指定的BSSID数据
            for (String targetBssid : TARGET_BSSIDS) {
                if (targetBssid.equals(bssid)) {
                    wifiData.put(bssid, rssi);
                    break;
                }
            }
        }

        Call<ApiResponse<Void>> call = apiService.sendWifiData(new WifiData(wifiData));
        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Void>> call, @NonNull Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "WiFi data sent successfully. Response: " + response.body());
                } else {
                    Log.e(TAG, "Failed to send WiFi data. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                Log.e(TAG, "Error sending WiFi info to server", t);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(scanRunnable);
    }
}
