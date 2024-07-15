package com.wifi.utils;

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
        addFingerInfo("f1", "0", "13", "A1");
        addFingerInfo("f2", "0", "8", "A1");
        addFingerInfo("f3", "2", "10", "A1");
        addFingerInfo("f4", "3", "13", "A1");
        addFingerInfo("f5", "3", "8", "A1");
        addFingerInfo("f6", "4", "13", "R");
        addFingerInfo("f7", "4", "9", "R");
        addFingerInfo("f8", "4", "6", "R");
        addFingerInfo("f9", "4", "5", "R");
        addFingerInfo("f10", "4", "2", "R");
        addFingerInfo("f11", "4", "1", "R");
        addFingerInfo("f12", "3", "0", "A2");
        addFingerInfo("f13", "3", "5", "A2");
        addFingerInfo("f14", "1", "5", "A2");
        addFingerInfo("f15", "1", "0", "A2");
        addFingerInfo("f16", "2", "2", "A2");
        addFingerInfo("f17", "5", "0", "A3");
        addFingerInfo("f18", "8", "0", "A3");
        addFingerInfo("f19", "7", "2", "A3");
        addFingerInfo("f20", "8", "4", "A3");
        addFingerInfo("f21", "5", "4", "A3");
        addFingerInfo("f22", "5", "6", "GATE");
        addFingerInfo("f23", "5", "7", "GATE");
        addFingerInfo("f24", "5", "8", "GATE");
        addFingerInfo("f25", "5", "9", "GATE");
        addFingerInfo("f26", "5", "13", "A4");
        addFingerInfo("f27", "8", "13", "A4");
        addFingerInfo("f28", "8", "10", "A4");
        addFingerInfo("f29", "5", "10", "A4");
        addFingerInfo("f30", "7", "11", "A4");
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
