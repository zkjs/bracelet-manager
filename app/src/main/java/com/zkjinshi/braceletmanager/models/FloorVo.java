package com.zkjinshi.braceletmanager.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yejun on 11/25/16.
 * Copyright (C) 2016 qinyejun
 */

public class FloorVo {
    private List<DrawableVo> drawables = new ArrayList<>();
    private int floors;
    private String id;

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
}
