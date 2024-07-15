package com.wifi.utils;

import com.wifi.dto.LocationInfo;
import com.wifi.dto.WifiData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WifiDataProcessor {

    private final Set<String> allowedBssids;
    private final FingerPrint fingerPrint;

    public WifiDataProcessor(Set<String> allowedBssids, FingerPrint fingerPrint) {
        this.allowedBssids = allowedBssids;
        this.fingerPrint = fingerPrint;
    }

    public LocationInfo processWifiData(WifiData wifiData) {
        Map<String, Integer> filteredData = new HashMap<>();
        for (Map.Entry<String, Integer> entry : wifiData.getWifiData().entrySet()) {
            if (allowedBssids.contains(entry.getKey())) {
                filteredData.put(entry.getKey(), entry.getValue());
            }
        }

        return fingerPrint.getLocation(filteredData);
    }
}

