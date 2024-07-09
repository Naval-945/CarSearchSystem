package com.wifi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class WifiData {

    private Map<String, Integer> wifiData;

    public WifiData() {}

    public WifiData(Map<String, Integer> wifiData) {
        this.wifiData = wifiData;
    }

}
