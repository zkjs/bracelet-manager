package com.zkjinshi.braceletmanager.base;

import android.content.Context;

/**
 * Created by yejun on 11/3/16.
 * Copyright (C) 2016 qinyejun
 */

public class BaseContext {
    private Context context;

    private BaseContext(){}

    private static BaseContext instance;

    public static synchronized BaseContext getInstance(){
        if(null == instance){
            instance = new BaseContext();
        }
        return instance;
    }

    public void init(android.content.Context context){
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
