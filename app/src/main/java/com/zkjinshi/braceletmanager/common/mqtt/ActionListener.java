package com.zkjinshi.braceletmanager.common.mqtt;

import android.content.Context;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

/**
 * Created by yejun on 11/24/16.
 * Copyright (C) 2016 qinyejun
 */

public class ActionListener implements IMqttActionListener {

    enum Action {
        /** Connect Action **/
        CONNECT,
        /** Disconnect Action **/
        DISCONNECT,
        /** Subscribe Action **/
        SUBSCRIBE,
        /** Publish Action **/
        PUBLISH
    }

    private Action action;
    private Context context;

    public ActionListener(Context context, Action action) {
        this.context = context;
        this.action = action;
    }



    @Override
    public void onSuccess(IMqttToken iMqttToken) {

    }

    @Override
    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

    }
}
