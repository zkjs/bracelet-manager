package com.zkjinshi.braceletmanager.common.mqtt;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.zkjinshi.braceletmanager.common.utils.CacheUtil;
import com.zkjinshi.braceletmanager.common.utils.NotificationHelper;
import com.zkjinshi.braceletmanager.models.SOSMessage;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

import java.util.Date;

/**
 * Created by yejun on 11/24/16.
 * Copyright (C) 2016 qinyejun
 */

public class MqttManager {
    private static MqttManager instance = null;
    private Context context;
    private MqttAndroidClient mqttClient;
    private String broker = "tcp://192.168.1.101";  // Broker URL or IP Address
    private String port = "1883";
    private String clientId = "android";
    private String topic = "sos";                  // Subscribe topic
    int qos = 1;

    private MqttManager(Context context) {
        this.context = context;
        readLocalConfig();
        initMqtt();
    }

    public synchronized static MqttManager getInstance(Context context) {
        if (instance == null) {
            instance = new MqttManager(context);
        }
        return instance;
    }

    private void readLocalConfig() {

        if (null != CacheUtil.getInstance().getLocalMqttPort())
            port = CacheUtil.getInstance().getLocalMqttPort();

        if (null != CacheUtil.getInstance().getDeviceNo())
            clientId = CacheUtil.getInstance().getDeviceNo();

        if (null != CacheUtil.getInstance().getLocalMqttServer()) {
            broker = CacheUtil.getInstance().getLocalMqttServer();
            broker = "tcp://" + broker + ":" + port;
        }
    }

    private void initMqtt() {
        //MemoryPersistence persistence = new MemoryPersistence();
        mqttClient = new MqttAndroidClient(this.context, broker, clientId);
    }

    public void connect() {
        readLocalConfig();
        mqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    Log.i("mqtt", "reconnect");
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic(topic);
                } else {
                    Log.i("mqtt", "connected");
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.i("mqtt", "connection lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.i("mqtt", "incoming msg ["+ topic +"]: " + new String(message.getPayload()));

                String msg = new String(message.getPayload());
                SOSMessage sosMessage = new Gson().fromJson(msg, SOSMessage.class);

                if(null != sosMessage) {
                    if (new Date().getTime() - CacheUtil.getInstance().getBraceletTime(sosMessage.getBracelet()) > 5000) {
                        NotificationHelper.getInstance().showNotification(context, sosMessage);
                        EventBus.getDefault().post(sosMessage);
                        CacheUtil.getInstance().setBraceletTime(sosMessage.getBracelet(), new Date().getTime());
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setKeepAliveInterval(3600);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        try {
            Log.i("mqtt","Connecting to " + broker);
            mqttClient.connect(mqttConnectOptions, this.context, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic(topic);
                    //publish("hello mqtt");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("mqtt", "connect fail");
                }
            });


        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribeToTopic(String topic){
        try {
            mqttClient.subscribe(topic, qos, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("mqtt","Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("mqtt","Failed to subscribe");
                }
            });

        } catch (MqttException ex){
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }

    }

    public void publish(String content) {
        //content      = content + " : " + new Date().getTime();
        try {
            MqttMessage msg = new MqttMessage();

            msg.setPayload(content.getBytes());
            //msg.setId(1);
            msg.setQos(qos);
            mqttClient.publish(topic, msg);
            //mqttClient.getBufferedMessage(1);
        } catch (Exception me) {
            Log.w(this.getClass().getSimpleName(), "-----------publich");
        }
    }

}
