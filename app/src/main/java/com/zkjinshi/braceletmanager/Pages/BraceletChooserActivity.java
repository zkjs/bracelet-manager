package com.zkjinshi.braceletmanager.Pages;

import android.app.Fragment;
import android.os.Bundle;

import com.zkjinshi.braceletmanager.base.SingleFragmentActivity;
import com.zkjinshi.braceletmanager.models.BraceletVo;
import com.zkjinshi.braceletmanager.models.SOSMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by yejun on 11/4/16.
 * Copyright (C) 2016 qinyejun
 */

public class BraceletChooserActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BraceletsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe( threadMode = ThreadMode.MAIN)
    public void onBus(BraceletVo data){
        finish();
    }
}
