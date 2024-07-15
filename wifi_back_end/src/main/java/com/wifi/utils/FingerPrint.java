package com.wifi.utils;

import com.wifi.dto.LocationInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 根据传入的WiFi数据匹配指纹，返回匹配的指纹ID和位置信息
 */

public class FingerPrint {
    private final FingerMac fingerMac;
    private final FingerInfo fingerInfo;


    public FingerPrint(){
        this.fingerMac = new FingerMac();
        this.fingerInfo = new FingerInfo();
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

    //根据传入的键值导出需要的一维强度数组
    public int[] processWifiData(Map<String, Integer> wifiData) {
        // 初始化一个长度为6的数组，所有值默认为-255
        int[] dataArray = new int[6];
        Arrays.fill(dataArray, -255);

        // 定义MAC地址的顺序
        String[] bssids = {
                "0e:69:6c:bd:e1:2b", // 索引0
                "0e:69:6c:d4:3c:56", // 索引1
                "0e:69:6c:d6:a4:c8", // 索引2
                "0e:69:6c:d6:8c:03", // 索引3
                "0e:69:6c:d6:9d:7c", // 索引4
                "0e:69:6c:d6:98:ab"  // 索引5
        };

        // 遍历传入的wifiData，将强度值放到正确的数组位置上
        for (Map.Entry<String, Integer> entry : wifiData.entrySet()) {
            for (int j = 0; j < bssids.length; j++) {
                if (entry.getKey().equalsIgnoreCase(bssids[j])) {
                    dataArray[j] = entry.getValue();
                    break; // 找到匹配项后退出循环
                }
            }
        }

        return dataArray;
    }

    //定位算法的最终实现
    public LocationInfo getLocation(Map<String, Integer> wifiData) {
        int[] rssiArray = processWifiData(wifiData);
        String fingerprintId = matchFinger(rssiArray);

        if (fingerprintId != null) {
            // 使用指纹ID获取位置信息
            Map<String, String> locationData = fingerInfo.getFingerprintData(fingerprintId);
            return new LocationInfo(
                    Float.parseFloat(locationData.get("XCoordinate")),
                    Float.parseFloat(locationData.get("YCoordinate")),
                    locationData.get("ParkingSpot")
            );
        } else {
            return null;
        }
    }

}
