package com.zkjinshi.braceletmanager.common.mqtt;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.maps2d.model.Text;
import com.zkjinshi.braceletmanager.Constants;
import com.zkjinshi.braceletmanager.common.utils.CacheUtil;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Date;

/**
 * Created by yejun on 11/24/16.
 * Copyright (C) 2016 qinyejun
 */

public class MqttManager {
    private static MqttManager instance = null;
    private Context context;
    private MqttAndroidClient client;
    String broker = "tcp://192.168.1.101:1883";  // Broker URL or IP Address
    String clientId = "sample";
    private String topic = "topic";              // Subscribe topic
    int qos = 0;

    private MqttManager(Context context) {
        this.context = context;
        initMqtt();
    }

    public synchronized static MqttManager getInstance(Context context) {
        if (instance == null) {
            instance = new MqttManager(context);
        }
        return instance;
    }

    private void initMqtt() {

        MemoryPersistence persistence = new MemoryPersistence();

        client = new MqttAndroidClient(this.context, broker, clientId, persistence);
    }

    public void connect() {
        try {
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: ");
            //client.connect(connOpts);
            client.connect(connOpts, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Log.e(this.getClass().getCanonicalName(), "Mqtt Connected Success");
                    MqttManager.this.publish("pub from android");
                    MqttManager.this.subscribe();
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

                }
            });
            System.out.println("Connected");
        } catch (MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }

    public void subscribe() {
        String topic = Constants.MQTT_TOPIC;

        if (!TextUtils.isEmpty(CacheUtil.getInstance().getMqttTopic())) {
            topic = CacheUtil.getInstance().getMqttTopic();
        }

        try {
            client.subscribe(topic, qos, context, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Log.e("Mqtt", "mqtt subscribe topic success");
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Log.e("Mqtt", "mqtt subscribe topic fail");
                }
            });
        } catch (MqttSecurityException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to subscribe to" + topic , e);
        } catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to subscribe to" + topic , e);
        }

    }

    public void publish(String content) {
        content      = content + " : " + new Date().getTime();

        try {
            System.out.println("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);

            client.publish(topic, content.getBytes(), qos, false, content, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Log.e("Mqtt", "publish success");
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Log.e("Mqtt", "publish failure");
                }
            });
            System.out.println("Message published");
        } catch (MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }

}
