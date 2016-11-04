package com.zkjinshi.braceletmanager.response.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yejun on 11/4/16.
 * Copyright (C) 2016 qinyejun
 */

public class NormalListData<T> {
    private List<T> list = new ArrayList<>();

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
