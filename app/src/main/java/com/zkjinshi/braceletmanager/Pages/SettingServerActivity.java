package com.zkjinshi.braceletmanager.Pages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.base.BaseActivity;
import com.zkjinshi.braceletmanager.common.utils.CacheUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 配置服务器地址等基础信息
 * Created by yejun on 11/22/16.
 * Copyright (C) 2016 qinyejun
 */

public class SettingServerActivity extends BaseActivity {

    @BindView(R.id.et_local_server)
    EditText mEtLocalServer;
    @BindView(R.id.et_api_server)
    EditText mEtApiServer;
    @BindView(R.id.et_device_no)
    EditText mEtDeviceNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_server);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingServerActivity.this.finish();
            }
        });

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        if (!TextUtils.isEmpty(CacheUtil.getInstance().getLocalServer())) {
            mEtLocalServer.setText(CacheUtil.getInstance().getLocalServer());
        }
        if (!TextUtils.isEmpty(CacheUtil.getInstance().getApiServer())) {
            mEtApiServer.setText(CacheUtil.getInstance().getApiServer());
        }
        if (!TextUtils.isEmpty(CacheUtil.getInstance().getDeviceNo())) {
            mEtDeviceNo.setText(CacheUtil.getInstance().getDeviceNo());
        }
    }

    @OnClick(R.id.btn_confirm)
    public void onClick() {
        String local = mEtLocalServer.getText().toString().trim();
        String api = mEtApiServer.getText().toString().trim();
        String deviceNo = mEtDeviceNo.getText().toString().trim();
        if (TextUtils.isEmpty(local) || TextUtils.isEmpty(api) || TextUtils.isEmpty(deviceNo)) {
            Toast.makeText(this, "请将所有字段填写完整", Toast.LENGTH_SHORT).show();
            return;
        }

        CacheUtil.getInstance().setLocalServer(local);
        CacheUtil.getInstance().setApiServer(api);
        CacheUtil.getInstance().setDeviceNo(deviceNo);

        finish();
    }
}
