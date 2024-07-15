package com.wifi.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 存储指纹
 * 每个指纹对应一个MAC地址列表
 */


public class FingerMac {
    private final List<List<Integer>> macAddressData;

    public FingerMac() {
        // 初始化指纹库，这里使用List来存储每个指纹的MAC地址列表
        macAddressData = new ArrayList<>();
        initFingerData(); // 调用初始化指纹数据的方法
    }

    private void initFingerData() {

        addFingerData("f1", Arrays.asList(-255, -73, -43, -255, -77, -255));
        addFingerData("f2", Arrays.asList(-255, -72, -49, -255, -255, -255));
        addFingerData("f3", Arrays.asList(-87, -65, -46, -255, -77, -82));
        addFingerData("f4", Arrays.asList(-255, -51, -45, -255, -71, -255));
        addFingerData("f5", Arrays.asList(-255, -72, -45, -255, -67, -255));
        addFingerData("f6", Arrays.asList(-77, -63, -56, -255, -58, -77));
        addFingerData("f7", Arrays.asList(-66, -255, -65, -255, -68, -68));
        addFingerData("f8", Arrays.asList(-66, -255, -74, -70, -255, -61));
        addFingerData("f9", Arrays.asList(-63, -255, -79, -66, -255, -60));
        addFingerData("f10", Arrays.asList(-64, -255, -76, -65, -255, -49));
        addFingerData("f11", Arrays.asList(-66, -255, -78, -69, -255, -67));
        addFingerData("f12", Arrays.asList(-55, -255, -255, -81, -255, -77));
        addFingerData("f13", Arrays.asList(-39, -255, -255, -78, -255, -63));
        addFingerData("f14", Arrays.asList(-53, -255, -255, -80, -255, -76));
        addFingerData("f15", Arrays.asList(-50, -255, -255, -83, -255, -76));
        addFingerData("f16", Arrays.asList(-54, -255, -255, -78, -255, -68));
        addFingerData("f17", Arrays.asList(-73, -255, -255, -54, -255, -59));
        addFingerData("f18", Arrays.asList(-75, -255, -255, -47, -255, -59));
        addFingerData("f19", Arrays.asList(-71, -255, -85, -51, -255, -50));
        addFingerData("f20", Arrays.asList(-72, -255, -255, -57, -255, -52));
        addFingerData("f21", Arrays.asList(-71, -255, -85, -63, -255, -38));
        addFingerData("f22", Arrays.asList(-63, -78, -63, -79, -75, -54));
        addFingerData("f23", Arrays.asList(-67, -255, -73, -79, -77, -67));
        addFingerData("f24", Arrays.asList(-73, -255, -67, -82, -74, -69));
        addFingerData("f25", Arrays.asList(-68, -255, -68, -80, -77, -70));
        addFingerData("f26", Arrays.asList(-255, -74, -63, -38, -255, -255));
        addFingerData("f27", Arrays.asList(-255, -84, -80, -255, -255, -255));
        addFingerData("f28", Arrays.asList(-255, -255, -79, -255, -255, -255));
        addFingerData("f29", Arrays.asList(-255, -84, -78, -48, -255, -255));
        addFingerData("f30", Arrays.asList(-255, -80, -79, -255, -255, -255));
    }


    public List<List<Integer>> getMacAddressData() {
        return macAddressData;
    }

    public void addFingerData(String fingerprintId, List<Integer> macValues) {
        macAddressData.add(macValues);
    }


}