package com.dingwei.wifi.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WifiScanReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiScanService.scanWifiNetworks(context);
    }
}

