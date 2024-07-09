package com.dingwei.wifi.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.dingwei.wifi.service.WifiScanService;

import java.util.List;

public class WifiScanReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            @SuppressLint("MissingPermission") List<ScanResult> scanResults = wifiManager.getScanResults();

            if (scanResults != null && !scanResults.isEmpty()) {
                StringBuilder wifiInfo = new StringBuilder();
                for (ScanResult scanResult : scanResults) {
                    wifiInfo.append("SSID: ").append(scanResult.SSID)
                            .append(", BSSID: ").append(scanResult.BSSID)
                            .append(", RSSI: ").append(scanResult.level)
                            .append("\n");
                }

                // 将WiFi信息发送到后台服务
                Intent serviceIntent = new Intent(context, WifiScanService.class);
                serviceIntent.putExtra("wifiInfo", wifiInfo.toString());
                context.startService(serviceIntent);
            }
        }
    }
}
