package com.dingwei.wifi.api.model;

public class LocationInfo {
    private float x;
    private float y;
    private String parkingSpot;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getParkingSpot(){return parkingSpot;}

    // 后端返回值的定义
    public LocationInfo(float x, float y, String parkingSpot) {
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

