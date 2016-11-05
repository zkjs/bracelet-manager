package com.zkjinshi.braceletmanager.response.data;

import com.zkjinshi.braceletmanager.models.AccessPointVo;
import com.zkjinshi.braceletmanager.models.TrackPointVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yejun on 11/4/16.
 * Copyright (C) 2016 qinyejun
 */

public class TrackListData {
    private List<TrackPointVo> list = new ArrayList<>();
    private List<AccessPointVo> aps = new ArrayList<>();

    public List<TrackPointVo> getList() {
        return list;
    }

    public void setList(List<TrackPointVo> list) {
        this.list = list;
    }

    public List<AccessPointVo> getAps() {
        return aps;
    }

    public void setAps(List<AccessPointVo> aps) {
        this.aps = aps;
    }
}
