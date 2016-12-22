package com.zkjinshi.braceletmanager.models;

import com.zkjinshi.braceletmanager.common.utils.ConfigUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yejun on 12/21/16.
 * Copyright (C) 2016 qinyejun
 */

public class PhotoVo {
    String id;
    String path;
    int timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return ConfigUtil.getInstance().getApiDomain()+path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormatedTime() {
        Date date = new Date();
        date.setTime(timestamp);
        return new SimpleDateFormat("MM-dd HH:mm").format(date);
    }
}
