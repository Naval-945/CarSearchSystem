package com.wifi.entity;

import java.util.List;
import java.util.Map;

public class FingerPrint {
    private FingerMac fingerMac;
    private FingerInfo fingerInfo;

    public FingerPrint(FingerMac fingerMac, FingerInfo fingerInfo) {
        this.fingerMac = fingerMac;
        this.fingerInfo = fingerInfo;
    }

    // 根据传入的MAC地址数组匹配指纹的方法
    public String matchFinger(int[] inputRSSI) {

        String matchedFinger = null; // 匹配度最高的指纹
        int minDifferenceSum = Integer.MAX_VALUE; // 记录到目前为止找到的最小的差异总和
        int fingerIndex = 0; // 用于记录匹配到的指纹的索引


        List<List<Integer>> macAddressData = fingerMac.getMacAddressData();
        for (List<Integer> macValues : macAddressData) {
            int differenceSum = 0;
            for (int i = 0; i < inputRSSI.length; i++) {
                differenceSum += Math.abs(inputRSSI[i] - macValues.get(i)); // 使用绝对值
            }
            if (differenceSum < minDifferenceSum) {
                minDifferenceSum = differenceSum;
                matchedFinger = "f" + (fingerIndex + 1); // 构造指纹ID
            }
            fingerIndex++;
        }

        // 返回与输入数组差异最小的指纹ID
        return matchedFinger;
    }

    // 用于获取匹配指纹的位置信息
    public String getLocation(int[] inputRSSI) {
        String fingerprintId = matchFinger(inputRSSI);
        if (fingerprintId != null) {
            Map<String, String> locationInfo = fingerInfo.getFingerprintData(fingerprintId);
            return " Location: (" + locationInfo.get("XCoordinate") + ", " + locationInfo.get("YCoordinate") + "), "
                    + "Parking Spot: " + locationInfo.get("ParkingSpot");
        } else {
            return "error";
        }
    }


}
