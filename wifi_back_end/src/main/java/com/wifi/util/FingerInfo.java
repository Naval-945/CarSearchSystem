package com.wifi.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储指纹信息
 * 每个指纹对应的一系列位置信息（X, Y 坐标）和停车位信息。
 */

public class FingerInfo {
    private final Map<String, Map<String, String>> fingerprints;

    public FingerInfo() {
        fingerprints = new HashMap<>();
        initFingerInfo(); // 在构造函数中调用初始化方法
    }

    private void initFingerInfo() {
        // 在这里添加初始的指纹数据
        addFingerInfo("f1", "0", "0", "1");
        addFingerInfo("f2", "0", "5", "1");
        addFingerInfo("f3", "2", "3", "1");
        addFingerInfo("f4", "3", "0", "1");
        addFingerInfo("f5", "3", "5", "1");
        addFingerInfo("f6", "4", "0", "2");
        addFingerInfo("f7", "4", "4", "2");
        addFingerInfo("f8", "4", "7", "2");
        addFingerInfo("f9", "4", "8", "2");
        addFingerInfo("f10", "4", "11", "2");
        addFingerInfo("f11", "4", "12", "2");
        addFingerInfo("f12", "3", "13", "3");
        addFingerInfo("f13", "3", "8", "3");
        addFingerInfo("f14", "1", "8", "3");
        addFingerInfo("f15", "1", "13", "3");
        addFingerInfo("f16", "2", "11", "3");
        addFingerInfo("f17", "5", "13", "4");
        addFingerInfo("f18", "8", "13", "4");
        addFingerInfo("f19", "7", "11", "4");
        addFingerInfo("f20", "8", "9", "4");
        addFingerInfo("f21", "5", "9", "4");
        addFingerInfo("f22", "5", "7", "5");
        addFingerInfo("f23", "5", "6", "5");
        addFingerInfo("f24", "5", "5", "5");
        addFingerInfo("f25", "5", "4", "5");
    }

    public void addFingerInfo(String fingerprintId, String xCoordinate, String yCoordinate, String parkingSpot) {
        Map<String, String> locationData = new HashMap<>();
        locationData.put("XCoordinate", xCoordinate);
        locationData.put("YCoordinate", yCoordinate);
        locationData.put("ParkingSpot", parkingSpot);
        fingerprints.put(fingerprintId, locationData);
    }

    //获取指定指纹ID的位置信息
    public Map<String, String> getFingerprintData(String fingerprintId) {
        return fingerprints.get(fingerprintId);
    }


}