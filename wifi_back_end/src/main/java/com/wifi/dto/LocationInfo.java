package com.wifi.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationInfo {
    private float x;
    private float y;
    private String parkingSpot;

    // 后端返回值的定义
    public LocationInfo(float x, float y, String parkingSpot) {
        this.x = x/8.0f;
        this.y = y/13.0f;
        this.parkingSpot = parkingSpot;
    }

    @Override
    public String toString() {
        // 这里仍然返回字符串，但提供数值的原始float类型通过get方法访问
        return String.format("LocationInfo{x=%.2f, y=%.2f, parkingSpot='%s'}", x, y, parkingSpot);
    }
}
