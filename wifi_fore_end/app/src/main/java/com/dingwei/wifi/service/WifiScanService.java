package com.dingwei.wifi.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import com.dingwei.wifi.api.model.LocationInfo;
import com.dingwei.wifi.api.model.LocationViewModel;
import com.dingwei.wifi.api.model.WifiData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

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

    private static LocationViewModel locationViewModel;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;


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

        locationViewModel = LocationViewModel.getInstance();

        createNotificationChannel(); // 创建通知通道
        startForeground(1, buildNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION); // 启动前台服务，指定类型为位置服务

//        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE); // 获取WifiManager实例

        Intent intent = new Intent(this, WifiScanReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        startRepeatingAlarm();
    }

    public static void scanWifiNetworks(Context context) {
        Log.d(TAG, "理论上是每5秒通过定时广播启动 scanWifiNetworks()");

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        WifiManager.ScanResultsCallback callback = new WifiManager.ScanResultsCallback() {
            @Override
            public void onScanResultsAvailable() {
                Log.d(TAG, "这是拿到扫描结果了 待会送过去处理 onScanResultsAvailable()");
                @SuppressLint("MissingPermission") List<ScanResult> results = wifiManager.getScanResults();
                processScanResults(context, results); // 处理扫描结果
            }
        };
        wifiManager.registerScanResultsCallback(context.getMainExecutor(), callback); // 注册扫描结果回调 TODO：有什么用?
        Log.d(TAG, "Callback registered");

        // 手动触发WiFi扫描
        boolean scanStarted = wifiManager.startScan();
        if (scanStarted) {
            Log.d(TAG, "WiFi scan started successfully.");
        } else {
            Log.e(TAG, "WiFi scan failed to start.");
        }
    }

    public static void processScanResults(Context context, List<ScanResult> results) {
        Log.d(TAG, "拿到扫描结果了嗷 processScanResults()");
        if (results != null && !results.isEmpty()) {
            Map<String, Integer> wifiDataMap = new HashMap<>();
            for (ScanResult result : results) {
                wifiDataMap.put(result.BSSID, result.level); // 获取BSSID和RSSI
            }

            Log.i(TAG, "发送的扫描结果为: " + wifiDataMap);

            // 发送数据到服务器
            sendWifiInfoToServer(context, new WifiData(wifiDataMap));
        } else {
            Log.d(TAG, "No WiFi scan results available.");
        }
    }

    private static void sendWifiInfoToServer(Context context, WifiData wifiData) {
//        // 模拟服务器返回随机位置
//        Random random = new Random();
//        float x = random.nextFloat(); // 假设 x 坐标在 0 到 1 之间
//        float y = random.nextFloat(); // 假设 y 坐标在 0 到 1 之间
//        Log.d(TAG, "random x: "+x+" y: "+y);
//        String parkingSpot = "A"+(int) (Math.random()*4+1);
//
//
//        LocationInfo randomLocation = new LocationInfo(x, y, parkingSpot);
//        Log.i(TAG, "生成的randomLocation : "+randomLocation);
//        handleLocationUpdate(randomLocation);

        Log.d(TAG,"扫描结果被送到这里了 sendWifiInfoToServer()");

        Retrofit retrofit = RetrofitClient.getClient("http://123.249.15.162:8848/");
        ApiService apiService = retrofit.create(ApiService.class);
        Call<ApiResponse<LocationInfo>> call = apiService.sendWifiData(wifiData);
        call.enqueue(new Callback<ApiResponse<LocationInfo>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<LocationInfo>> call, @NonNull Response<ApiResponse<LocationInfo>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, " 成功送到服务器 WiFi data sent successfully.");
                    LocationInfo location = response.body().getData();
                    Log.d(TAG, "哟这不响应体老妹吗 让我瞅瞅怎么个事 Received location: " + location);
                    //处理位置信息
                    handleLocationUpdate(context, location);
                } else {
                    Log.e(TAG, "Failed to send WiFi data. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<LocationInfo>> call, @NonNull Throwable t) {
                Log.e(TAG, "Error sending WiFi info to server: " + t.getMessage());
            }
        });
    }

    // This method should be called when you receive the location info from the server
    private static void handleLocationUpdate(Context context, LocationInfo location) {
        locationViewModel.setLocationInfo(location);
    }

    private void startRepeatingAlarm() {
        long interval = 5000; // 5秒
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, interval, pendingIntent);
    }

    private void stopRepeatingAlarm() {
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
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
        stopRepeatingAlarm(); // 停止定期扫描任务
    }
}