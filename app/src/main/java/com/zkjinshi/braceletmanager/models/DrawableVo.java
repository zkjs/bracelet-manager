package com.zkjinshi.braceletmanager.models;

/**
 * Created by yejun on 11/24/16.
 * Copyright (C) 2016 qinyejun
 */

public class DrawableVo {
    private DrawableDataVo data;
    private float lat;
    private float lng;
    private String title;
    private String type;

    public DrawableDataVo getData() {
        return data;
    }

    public void setData(DrawableDataVo data) {
        this.data = data;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
