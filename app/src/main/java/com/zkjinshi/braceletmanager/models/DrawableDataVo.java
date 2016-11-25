package com.zkjinshi.braceletmanager.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yejun on 11/24/16.
 * Copyright (C) 2016 qinyejun
 */

public class DrawableDataVo {
    private List<List<Float>> path = new ArrayList<>();

    public List<List<Float>> getPath() {
        return path;
    }

    public void setPath(List<List<Float>> path) {
        this.path = path;
    }
}
