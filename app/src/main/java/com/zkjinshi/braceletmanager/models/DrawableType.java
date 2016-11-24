package com.zkjinshi.braceletmanager.models;

/**
 * Created by yejun on 11/24/16.
 * Copyright (C) 2016 qinyejun
 */

public enum DrawableType {
    Polygon;

    @Override
    public String toString() {
        switch (this) {
            case Polygon:
                return "AMap.Polygon";
        }
        return super.toString();
    }
}
