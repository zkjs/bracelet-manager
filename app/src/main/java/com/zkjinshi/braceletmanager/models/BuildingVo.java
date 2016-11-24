package com.zkjinshi.braceletmanager.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yejun on 11/24/16.
 * Copyright (C) 2016 qinyejun
 */

public class BuildingVo {
    private List<DrawableVo> drawables = new ArrayList<>();
    private int floors;
    private String id;
    private double lat;
    private double lng;
    private String title;

    public List<DrawableVo> getDrawables() {
        return drawables;
    }

    public void setDrawables(List<DrawableVo> drawables) {
        this.drawables = drawables;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
