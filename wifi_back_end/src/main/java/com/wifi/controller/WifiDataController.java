package com.wifi.controller;

import com.wifi.dto.WifiDataDTO;
import com.wifi.response.ApiResponse;
import com.wifi.response.ResponseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/wifi")
public class WifiDataController {

    private static final Logger logger = LoggerFactory.getLogger(WifiDataController.class);

    // 指定的BSSID列表
    private static final String[] TARGET_BSSIDS = {
            "62:9e:92:e6:23:c4", "2e:3a:a3:f2:88:e2", "0e:69:6c:d6:8c:04"
            // 添加更多的BSSID
    };

    @PostMapping
    public ResponseEntity<ApiResponse<WifiDataDTO>> receiveWifiData(@RequestBody WifiDataDTO wifiDataDTO) {
        // 将WiFi数据写入日志
        logger.info("Received WiFi data:");
        Map<String, Integer> filteredData = new HashMap<>();

        wifiDataDTO.getWifiData().forEach((bssid, rssi) -> {
            // 只处理指定的BSSID数据
            for (String targetBssid : TARGET_BSSIDS) {
                if (targetBssid.equals(bssid)) {
                    filteredData.put(bssid, rssi);
                    logger.info("BSSID: {}, RSSI: {}", bssid, rssi);
                    break;
                }
            }
        });

        // TODO: 调用算法A
        algoA(filteredData);

        return ResponseHelper.success("WiFi data received and processed", new WifiDataDTO(filteredData));
    }

    // 假设的算法A实现
    private void algoA(Map<String, Integer> wifiData) {
        // 算法A的处理逻辑
        logger.info("Running algoA with data: {}", wifiData);
    }
}
