package com.zkjinshi.braceletmanager.models;

import java.io.Serializable;

/**
 * Created by yejun on 11/4/16.
 * Copyright (C) 2016 qinyejun
 */

public class BraceletVo implements Serializable {
    private String bracelet;
    private String name;

    public String getBracelet() {
        return bracelet;
    }

    public void setBracelet(String bracelet) {
        this.bracelet = bracelet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
