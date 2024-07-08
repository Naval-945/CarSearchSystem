package com.dingwei.wificollector;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;


public class WifiScanReceiver extends BroadcastReceiver {

    private final WifiManager wifiManager;
    private final List<String> wifiList;
    private final ArrayAdapter<String> wifiListAdapter;

    public WifiScanReceiver(WifiManager wifiManager, List<String> wifiList, ArrayAdapter<String> wifiListAdapter) {
        this.wifiManager = wifiManager;
        this.wifiList = wifiList;
        this.wifiListAdapter = wifiListAdapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
            @SuppressLint("MissingPermission") List<ScanResult> scanResults = wifiManager.getScanResults();

            // 对 scanResults 进行排序，按 SSID 值从小到大排列
            scanResults.sort((result1, result2) -> {
                return result1.SSID.compareToIgnoreCase(result2.SSID); // 按字母顺序排序
            });

            wifiList.clear();
            for (ScanResult scanResult : scanResults) {
                String ssid = scanResult.SSID; // 获取SSID
                String bssid = scanResult.BSSID; // 获取BSSID
                int rssi = scanResult.level; // 获取RSSI
                wifiList.add("SSID: " + ssid + "\nBSSID: " + bssid + "\nRSSI: " + rssi);
            }
            wifiListAdapter.notifyDataSetChanged();
            Toast.makeText(context, "WiFi扫描完成", Toast.LENGTH_SHORT).show();
        }
    }
}
