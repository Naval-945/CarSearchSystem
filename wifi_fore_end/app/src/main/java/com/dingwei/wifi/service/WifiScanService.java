package com.dingwei.wifi.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.dingwei.wifi.api.ApiService;
import com.dingwei.wifi.api.RetrofitClient;
import com.dingwei.wifi.api.model.ApiResponse;
import com.dingwei.wifi.api.model.FingerPrint;
import com.dingwei.wifi.api.model.LocationInfo;
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
    private StringBuffer mScanResultStr;    // 暂存WiFi扫描结果的字符串
    private FingerPrint fingerPrint;
    private final String BASE0 = "0e:69:6c:bd:e1:2b";//Redmi_0FA2
    private final String BASE1 = "0e:69:6c:d4:3c:56";//CMCC-SSNS
    private final String BASE2 = "0e:69:6c:d6:a4:c8";//computer1 wifi
    private final String BASE3 = "0e:69:6c:d6:8c:03";//MERCURY_96CA
    private final String BASE4 = "0e:69:6c:d6:9d:7c";
    private final String BASE5 = "0e:69:6c:d6:98:ab";//G8 ThinQ

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

        System.out.println("111");
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE); // 获取WifiManager实例
        Retrofit retrofit = RetrofitClient.getClient("http://123.249.15.162:8848/"); // 初始化Retrofit客户端
        apiService = retrofit.create(ApiService.class); // 创建ApiService实例

        handler = new Handler();
        scanRunnable = new Runnable() {
            @Override
            public void run() {
                scanWifiNetworks(); // 扫描WiFi网络
                mScanResultStr = new StringBuffer();
                fingerPrint = new FingerPrint(0, 0, -100, -100, -100, -100, -100);
                if (ActivityCompat.checkSelfPermission(WifiScanService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                List<ScanResult> scanResults = wifiManager.getScanResults();
                for (ScanResult sr : scanResults) {
                    switch (sr.BSSID) {
                        case BASE0:
                            fingerPrint.setSs1(sr.level);
                            break;
                        case BASE1:
                            fingerPrint.setSs2(sr.level);
                            break;
                        case BASE2:
                            fingerPrint.setSs3(sr.level);
                            break;
                        case BASE3:
                            fingerPrint.setSs4(sr.level);
                            break;
                        case BASE4:
                            fingerPrint.setSs5(sr.level);
                            break;
                        case BASE5:
                            fingerPrint.setSs6(sr.level);
                            break;


                    }
                    mScanResultStr.append("SSID: ").append(sr.SSID).append("\n");
                    mScanResultStr.append("MAC Address: ").append(sr.BSSID).append("\n");
                    mScanResultStr.append("Signal Strength(dBm): ").append(sr.level).append("\n\n");
                    Log.d(TAG, "SSID:" + sr.SSID + "  MAC Address: " + sr.BSSID + "  Signal Strength:" + sr.level);
                }
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
        System.out.println("222");
        Call<ApiResponse<LocationInfo>> call = apiService.sendWifiData(wifiData);
        call.enqueue(new Callback<ApiResponse<LocationInfo>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<LocationInfo>> call, @NonNull Response<ApiResponse<LocationInfo>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "WiFi data sent successfully.");
                    LocationInfo location = response.body().getData();
//                    SharedPreferences sharedPreferences = getSharedPreferences("xy", Context.MODE_PRIVATE);
//                    float x = (float) location.getX();
//                    float y = (float) location.getY();
//
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putFloat("x", x);
//                    editor.putFloat("y", y);
//                    editor.apply();

                    Log.d(TAG, "Received location: " + location);
                    //处理位置信息
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
