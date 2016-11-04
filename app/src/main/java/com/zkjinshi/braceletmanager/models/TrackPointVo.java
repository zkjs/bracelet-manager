package com.zkjinshi.braceletmanager.models;

import java.io.Serializable;

/**
 * Created by yejun on 11/4/16.
 * Copyright (C) 2016 qinyejun
 */

public class TrackPointVo implements Serializable {
    private String ap;
    private int floor;
    private Gps gps;
    private long timestamp;

    public String getAp() {
        return ap;
    }

    public void setAp(String ap) {
        this.ap = ap;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Gps getGps() {
        return gps;
    }

    public void setGps(Gps gps) {
        this.gps = gps;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
