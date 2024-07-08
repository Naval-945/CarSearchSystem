package com.wifi.controller;

import com.wifi.entity.FingerInfo;
import com.wifi.entity.FingerMac;
import com.wifi.entity.FingerPrint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/collection")
public class FingerPrintController {
    private FingerMac fingerMac = new FingerMac();
    private FingerInfo fingerInfo = new FingerInfo();
    private FingerPrint fingerPrint = new FingerPrint(fingerMac, fingerInfo);

    @PostMapping("/matchFingerprint")
    public Map<String, String> matchFingerprint(@RequestBody int[] inputRSSI) {
        String locationInfo = fingerPrint.getLocation(inputRSSI);
        if (!locationInfo.equals("error")) {
            Map<String, String> response = fingerInfo.getFingerprintData(fingerPrint.matchFinger(inputRSSI));
            return Map.of(
                    "xCoordinate", response.get("XCoordinate"),
                    "yCoordinate", response.get("YCoordinate")
            );
        } else {
            return Map.of("error", "No matching fingerprint found");
        }
    }
}
