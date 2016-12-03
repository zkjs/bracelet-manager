package com.zkjinshi.braceletmanager.models;

import java.io.Serializable;

/**
 * 呼救信息推送
 * Created by yejun on 11/3/16.
 * Copyright (C) 2016 qinyejun
 */

public class SOSMessage implements Serializable {
    private String apid;
    private int floor;
    private String payload;
    private int rssi;
    private String message;
    private String address;
    private String bracelet;

    public String getApid() {
        return apid;
    }

    public void setApid(String apid) {
        this.apid = apid;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBracelet() {
        return bracelet;
    }

    public void setBracelet(String bracelet) {
        this.bracelet = bracelet;
    }
}
