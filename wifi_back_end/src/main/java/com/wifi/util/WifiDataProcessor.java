package com.wifi.util;

import com.wifi.dto.WifiData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WifiDataProcessor {

    private final Set<String> allowedBssids;
    private final AlgorithmA algorithmA;

    public WifiDataProcessor(Set<String> allowedBssids, AlgorithmA algorithmA) {
        this.allowedBssids = allowedBssids;
        this.algorithmA = algorithmA;
    }

    //TODO: 修改以适配需求
    public int processWifiData(WifiData wifiData) {
        Map<String, Integer> filteredData = new HashMap<>();
        for (Map.Entry<String, Integer> entry : wifiData.getWifiData().entrySet()) {
            if (allowedBssids.contains(entry.getKey())) {
                filteredData.put(entry.getKey(), entry.getValue());
            }
        }

        // 调用算法A处理过滤后的数据
        return algorithmA.process(filteredData);
    }
}

