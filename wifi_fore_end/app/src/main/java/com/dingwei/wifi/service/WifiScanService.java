package com.dingwei.wifi.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.dingwei.wifi.api.ApiService;
import com.dingwei.wifi.api.RetrofitClient;
import com.dingwei.wifi.api.model.ApiResponse;
import com.dingwei.wifi.api.model.WifiData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 调用顺序和逻辑
 * 应用启动后，用户登录成功。
 * 登录成功后，启动 WifiScanService 前台服务。
 * WifiScanService 在 onCreate() 方法中初始化定期执行的扫描任务。
 * 定期执行 scanWifiNetworks() 方法，手动触发 WiFi 扫描，并注册回调方法处理扫描结果。
 * 当 WiFi 扫描结果可用时，调用 onScanResultsAvailable() 方法。
 * 在 processScanResults() 方法中处理扫描结果，并将数据发送到服务器。
 * 当服务被销毁时，在 onDestroy() 方法中移除定期执行的扫描任务。
 */

public class WifiScanService extends Service {
    private static final String TAG = "WifiScanService";
    private static final String CHANNEL_ID = "WifiScanServiceChannel";
    private WifiManager wifiManager;
    private ApiService apiService;
    private Handler handler;
    private Runnable scanRunnable;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // 不提供绑定服务
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "WifiScanService on Create");
        createNotificationChannel(); // 创建通知通道
        startForeground(1, buildNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION); // 启动前台服务，指定类型为位置服务

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE); // 获取WifiManager实例
        Retrofit retrofit = RetrofitClient.getClient("http://123.249.15.162:8848/"); // 初始化Retrofit客户端
        apiService = retrofit.create(ApiService.class); // 创建ApiService实例

        handler = new Handler();
        scanRunnable = new Runnable() {
            @Override
            public void run() {
                scanWifiNetworks(); // 扫描WiFi网络
                handler.postDelayed(this, 10000); // 每10秒执行一次扫描任务
            }
        };
        handler.post(scanRunnable); // 开始定期扫描任务
    }

    private void scanWifiNetworks() {
        Log.d(TAG, "scanWifiNetworks()");
        // Android 11 及以上，使用新API
        WifiManager.ScanResultsCallback callback = new WifiManager.ScanResultsCallback() {
            @Override
            public void onScanResultsAvailable() {
                Log.d(TAG, "onScanResultsAvailable()");
                @SuppressLint("MissingPermission") List<ScanResult> results = wifiManager.getScanResults();
                processScanResults(results); // 处理扫描结果
            }
        };
        wifiManager.registerScanResultsCallback(getMainExecutor(), callback); // 注册扫描结果回调
        Log.d(TAG, "Callback registered");

        // 手动触发WiFi扫描
        boolean scanStarted = wifiManager.startScan();
        if (scanStarted) {
            Log.d(TAG, "WiFi scan started successfully.");
        } else {
            Log.e(TAG, "WiFi scan failed to start.");
        }
    }

    private void processScanResults(List<ScanResult> results) {
        Log.d(TAG, "processScanResults()");
        if (results != null && !results.isEmpty()) {
            Map<String, Integer> wifiDataMap = new HashMap<>();
            for (ScanResult result : results) {
                wifiDataMap.put(result.BSSID, result.level); // 获取BSSID和RSSI
            }

            //TODO: 此处用于开发调试、可删
            Log.d(TAG, wifiDataMap.toString());

            // 发送数据到服务器
            sendWifiInfoToServer(new WifiData(wifiDataMap));
        } else {
            Log.d(TAG, "No WiFi scan results available.");
        }
    }

    private void sendWifiInfoToServer(WifiData wifiData) {
        Call<ApiResponse<Integer>> call = apiService.sendWifiData(wifiData);
        call.enqueue(new Callback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Integer>> call, @NonNull Response<ApiResponse<Integer>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "WiFi data sent successfully.");
                    int location = response.body().getData();
                    Log.d(TAG, "Received location: " + location);
                    //处理位置信息
                } else {
                    Log.e(TAG, "Failed to send WiFi data. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Integer>> call, @NonNull Throwable t) {
                Log.e(TAG, "Error sending WiFi info to server: " + t.getMessage());
            }
        });
    }

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("WiFi Scanning Service")
                .setContentText("Scanning for WiFi networks...")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "WiFi Scanning Service",
                NotificationManager.IMPORTANCE_DEFAULT
        );

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel); // 创建通知通道
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(scanRunnable); // 停止定期扫描任务
    }
}
