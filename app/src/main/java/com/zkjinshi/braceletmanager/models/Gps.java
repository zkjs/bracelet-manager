package com.zkjinshi.braceletmanager.models;

import java.io.Serializable;

/**
 * Created by yejun on 11/4/16.
 * Copyright (C) 2016 qinyejun
 */

public class Gps implements Serializable {
    private double lat;
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
