package com.wifi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class WifiDataDTO {
    private Map<String, Integer> wifiData;

    public WifiDataDTO(Map<String, Integer> wifiData) {
        this.wifiData = wifiData;
    }
}
