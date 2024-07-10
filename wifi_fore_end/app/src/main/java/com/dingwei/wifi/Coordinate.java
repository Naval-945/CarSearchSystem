package com.dingwei.wifi;

public class Coordinate {
    private Double positionX;
    private Double positionY;

    public Coordinate(Double positionX, Double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public Double getPositionX() {
        return positionX;
    }

    public void setPositionX(Double positionX) {
        this.positionX = positionX;
    }

    public Double getPositionY() {
        return positionY;
    }

    public void setPositionY(Double positionY) {
        this.positionY = positionY;
    }
}
