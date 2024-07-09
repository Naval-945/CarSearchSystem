package com.wifi.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationInfo {
    private int x;
    private int y;
    private String parkingSpot;

    // 后端返回值的定义
    public LocationInfo(int x, int y, String parkingSpot) {
        this.x = x;
        this.y = y;
        this.parkingSpot = parkingSpot;
    }

    @Override
    public String toString() {
        return "LocationInfo{" +
                "x=" + x +
                ", y=" + y +
                ", parkingSpot='" + parkingSpot + '\'' +
                '}';
    }
}
