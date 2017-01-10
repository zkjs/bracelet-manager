package com.zkjinshi.braceletmanager.Pages;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.base.BaseActivity;
import com.zkjinshi.braceletmanager.common.utils.CacheUtil;
import com.zkjinshi.braceletmanager.common.utils.NotificationHelper;
import com.zkjinshi.braceletmanager.models.SOSMessage;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yejun on 12/28/16.
 * Copyright (C) 2016 qinyejun
 */

public class MqttPubActivity extends BaseActivity {

    @BindView(R.id.et_topic)
    EditText mEtTopic;
    @BindView(R.id.et_payload)
    EditText mEtPayload;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    private MqttAndroidClient mqttClient;
    private boolean isConnected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt_pub);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MqttPubActivity.this.finish();
            }
        });

        ButterKnife.bind(this);

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        String server = CacheUtil.getInstance().getLocalMqttServer();
        String port = CacheUtil.getInstance().getLocalMqttPort();
        String clientId = CacheUtil.getInstance().getDeviceNo();

        if (TextUtils.isEmpty(server) || TextUtils.isEmpty(port)) {
            Toast.makeText(this, "mqtt server 还未配置", Toast.LENGTH_SHORT).show();
            return;
        }
        String broker = "tcp://" + server + ":" + port;
        mqttClient = new MqttAndroidClient(this, broker, clientId);

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setKeepAliveInterval(3600);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        try {
            Log.i("mqtt","Connecting to " + broker);
            mqttClient.connect(mqttConnectOptions, this, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttClient.setBufferOpts(disconnectedBufferOptions);
                    isConnected = true;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MqttPubActivity.this, "Mqtt Server连接失败", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }

    @OnClick(R.id.btn_confirm)
    public void onClick() {
        String server = CacheUtil.getInstance().getLocalMqttServer();
        String port = CacheUtil.getInstance().getLocalMqttPort();
        String clientId = CacheUtil.getInstance().getDeviceNo();
        String topic = mEtTopic.getText().toString().trim();
        String payload = mEtPayload.getText().toString().trim();

        if (TextUtils.isEmpty(server) || TextUtils.isEmpty(port)) {
            Toast.makeText(this, "mqtt server 还未配置", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(clientId)) {
            clientId = "android";
        }
        if (TextUtils.isEmpty(topic) || TextUtils.isEmpty(payload)) {
            Toast.makeText(this, "请将字段填写完整", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isConnected) {
            Toast.makeText(this, "mqtt server 未连接", Toast.LENGTH_SHORT).show();
            return;
        }

        MqttMessage msg = new MqttMessage();
        msg.setPayload(payload.getBytes());
        msg.setQos(1);

        try {
            mqttClient.publish(topic, msg);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "消息已经发送", Toast.LENGTH_SHORT).show();
    }
}
