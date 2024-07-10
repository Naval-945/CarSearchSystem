package com.dingwei.wifi.api.model;

public class FingerPrint {
    private Integer positionX;
    private Integer positionY;
    private Integer ss1;
    private Integer ss2;
    private Integer ss3;
    private Integer ss4;
    private Integer ss5;
    private Integer ss6;

    public FingerPrint(Integer positionX, Integer positionY, Integer ss1, Integer ss2, Integer ss3, Integer ss4, Integer ss5) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.ss1 = ss1;
        this.ss2 = ss2;
        this.ss3 = ss3;
        this.ss4 = ss4;
        this.ss5 = ss5;
        this.ss6 = ss6;
    }

    public Integer getSs4() {
        return ss4;
    }

    public void setSs4(Integer ss4) {
        this.ss4 = ss4;
    }

    public Integer getSs5() {
        return ss5;
    }

    public void setSs5(Integer ss5) {
        this.ss5 = ss5;
    }

    public void setSs6(Integer ss5) {
        this.ss5 = ss6;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public void setPositionX(Integer positionX) {
        this.positionX = positionX;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public void setPositionY(Integer positionY) {
        this.positionY = positionY;
    }


    public Integer getSs1() {
        return ss1;
    }

    public void setSs1(Integer ss1) {
        this.ss1 = ss1;
    }

    public Integer getSs2() {
        return ss2;
    }

    public void setSs2(Integer ss2) {
        this.ss2 = ss2;
    }

    public Integer getSs3() {
        return ss3;
    }

    public void setSs3(Integer ss3) {
        this.ss3 = ss3;
    }
}
