package com.zkjinshi.braceletmanager.Pages;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.base.BaseActivity;
import com.zkjinshi.braceletmanager.common.mqtt.MqttManager;
import com.zkjinshi.braceletmanager.common.utils.CacheUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 配置服务器地址等基础信息
 * Created by yejun on 11/22/16.
 * Copyright (C) 2016 qinyejun
 */

public class SettingServerActivity extends BaseActivity {

    @BindView(R.id.et_local_mqtt_server)
    EditText mEtMqttServer;
    @BindView(R.id.et_local_api_server)
    EditText mEtApiServer;
    @BindView(R.id.et_local_mqtt_port)
    EditText mEtMqttPort;
    @BindView(R.id.et_local_api_port)
    EditText mEtApiPort;
    @BindView(R.id.et_device_no)
    EditText mEtDeviceNo;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


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
        if (!TextUtils.isEmpty(CacheUtil.getInstance().getLocalMqttServer())) {
            mEtMqttServer.setText(CacheUtil.getInstance().getLocalMqttServer());
        }
        if (!TextUtils.isEmpty(CacheUtil.getInstance().getLocalMqttPort())) {
            mEtMqttPort.setText(CacheUtil.getInstance().getLocalMqttPort());
        }
        if (!TextUtils.isEmpty(CacheUtil.getInstance().getLocalApiServer())) {
            mEtApiServer.setText(CacheUtil.getInstance().getLocalApiServer());
        }
        if (!TextUtils.isEmpty(CacheUtil.getInstance().getLocalApiPort())) {
            mEtApiPort.setText(CacheUtil.getInstance().getLocalApiPort());
        }
        if (!TextUtils.isEmpty(CacheUtil.getInstance().getDeviceNo())) {
            mEtDeviceNo.setText(CacheUtil.getInstance().getDeviceNo());
        }
    }

    @OnClick(R.id.btn_confirm)
    public void onClick() {
        String mqttServer = mEtMqttServer.getText().toString().trim();
        String mqttPort = mEtMqttPort.getText().toString().trim();
        String apiServer = mEtApiServer.getText().toString().trim();
        String apiPort = mEtApiPort.getText().toString().trim();
        String deviceNo = mEtDeviceNo.getText().toString().trim();
        if (TextUtils.isEmpty(mqttServer) || TextUtils.isEmpty(mqttPort)
                || TextUtils.isEmpty(apiServer) || TextUtils.isEmpty(apiPort)
                || TextUtils.isEmpty(deviceNo)) {
            Toast.makeText(this, "请将所有字段填写完整", Toast.LENGTH_SHORT).show();
            return;
        }

        CacheUtil.getInstance().setLocalMqttServer(mqttServer);
        CacheUtil.getInstance().setLocalMqttPort(mqttPort);
        CacheUtil.getInstance().setLocalApiServer(apiServer);
        CacheUtil.getInstance().setLocalApiPort(apiPort);
        CacheUtil.getInstance().setDeviceNo(deviceNo);

        if (CacheUtil.getInstance().getLocalMqttServer() != null) {
            MqttManager mqttManager = MqttManager.getInstance(getApplicationContext());
            mqttManager.connect();
        }

        EventBus.getDefault().post("configChanged");

        finish();
    }
}
