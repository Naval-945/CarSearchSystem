package com.dingwei.wifi.api.model;

import java.util.Map;

public class WifiData {
    private Map<String, Integer> wifiData;

    public WifiData(Map<String, Integer> wifiData) {
        this.wifiData = wifiData;
    }

    public Map<String, Integer> getWifiData() {
        return wifiData;
    }

    public void setWifiData(Map<String, Integer> wifiData) {
        this.wifiData = wifiData;
    }
}
