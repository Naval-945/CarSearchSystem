package com.wifi.controller;

import com.wifi.dto.LocationInfo;
import com.wifi.dto.WifiData;
import com.wifi.response.ApiResponse;
import com.wifi.response.ResponseHelper;
import com.wifi.utils.FingerPrint;
import com.wifi.utils.WifiDataProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class WifiDataController {

    private static final Logger logger = LoggerFactory.getLogger(WifiDataController.class);
    WifiDataProcessor wifiDataProcessor;

    public WifiDataController() {
        Set<String> allowedBssids = new HashSet<>(Arrays.asList(
                // 允许的BSSID
                "0e:69:6c:bd:e1:2b",
                "0e:69:6c:d4:3c:56",
                "0e:69:6c:d6:a4:c8",
                "0e:69:6c:d6:8c:03",
                "0e:69:6c:d6:9d:7c",
                "0e:69:6c:d6:98:ab"
        ));
        this.wifiDataProcessor = new WifiDataProcessor(allowedBssids, new FingerPrint());
    }

    @PostMapping("/wifi")
    public ResponseEntity<ApiResponse<LocationInfo>> receiveWifiData(@RequestBody WifiData wifiData) {
        // 将WiFi数据写入日志
        logger.info("Received WiFi data:");
        wifiData.getWifiData().forEach((bssid, rssi) -> logger.info("BSSID: {}, RSSI: {}", bssid, rssi));

        // 调用算法处理WiFi数据
        LocationInfo location = wifiDataProcessor.processWifiData(wifiData);
        return ResponseHelper.success("WiFi data received and processed", location);
    }
}
