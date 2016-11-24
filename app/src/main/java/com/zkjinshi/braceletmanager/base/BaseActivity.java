package com.zkjinshi.braceletmanager.base;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.zkjinshi.braceletmanager.Pages.SigninActivity;
import com.zkjinshi.braceletmanager.common.utils.CacheUtil;

/**
 * Created by yejun on 11/23/16.
 * Copyright (C) 2016 qinyejun
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.activityResumed();
        if (MyApplication.isAuthRequired() && !MyApplication.isAuthActivityShowing()) {
            Intent intent = new Intent(this, SigninActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }
}
