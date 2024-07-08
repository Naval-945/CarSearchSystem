package com.wifi.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FingerMac {
    private List<List<Integer>> macAddressData;

    public FingerMac() {
        // 初始化指纹库，这里使用List来存储每个指纹的MAC地址列表
        macAddressData = new ArrayList<>();
        initFingeData(); // 调用初始化指纹数据的方法
    }

    private void initFingeData() {
//        addFingerData("f1", Arrays.asList(-10, -20, -15, -255, -255, -255, -255, -255, -255, -255, -255, -255));
        addFingerData("f1", Arrays.asList(-255, -73, -43));
        addFingerData("f2", Arrays.asList(-255, -72, -49));
        addFingerData("f3", Arrays.asList(-87, -65, -46));
        addFingerData("f4", Arrays.asList(-255, -51, -45));
        addFingerData("f5", Arrays.asList(-255, -72, -45));
        addFingerData("f6", Arrays.asList(-77, -63, -56));
        addFingerData("f7", Arrays.asList(-66, -255, -65));
        addFingerData("f8", Arrays.asList(-66, -255, -74));
        addFingerData("f9", Arrays.asList(-63, -255, -79));
        addFingerData("f10", Arrays.asList(-64, -255, -76));
        addFingerData("f11", Arrays.asList(-66, -255, -78));
        addFingerData("f12", Arrays.asList(-55, -255, -255));
        addFingerData("f13", Arrays.asList(-39, -255, -255));
        addFingerData("f14", Arrays.asList(-53, -255, -255));
        addFingerData("f15", Arrays.asList(-50, -255, -255));
        addFingerData("f16", Arrays.asList(-54, -255, -255));
    }

    public List<List<Integer>> getMacAddressData() {
        return macAddressData;
    }

    public void addFingerData(String fingerprintId, List<Integer> macValues) {
        macAddressData.add(macValues);
    }


}