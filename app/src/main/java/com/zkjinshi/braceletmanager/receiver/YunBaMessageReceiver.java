package com.zkjinshi.braceletmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Cache;
import com.google.gson.Gson;
import com.zkjinshi.braceletmanager.common.utils.CacheUtil;
import com.zkjinshi.braceletmanager.common.utils.NotificationHelper;
import com.zkjinshi.braceletmanager.models.SOSMessage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

//import io.yunba.android.manager.YunBaManager;

/**
 * Created by yejun on 11/3/16.
 * Copyright (C) 2016 qinyejun
 */

public class YunBaMessageReceiver extends BroadcastReceiver {

    public static final String TAG = YunBaMessageReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {
/*
        //云巴推送处理
        if (YunBaManager.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {

//            try {
                String topic = intent.getStringExtra(YunBaManager.MQTT_TOPIC);
                String msg   = intent.getStringExtra(YunBaManager.MQTT_MSG);

                // {"apid": "ap110", "payload": "126683000000", "rssi": -32, "alert": "y", "bracelet": "82"}
                Log.i(TAG, "YunBaMessageReceiver-msg:"+msg);
                Log.i(TAG, "YunBaMessageReceiver-topic:"+topic);
                //JSONObject jsonObject = new JSONObject(msg);

                //String message = jsonObject.getString("message");

                SOSMessage sosMessage = new Gson().fromJson(msg, SOSMessage.class);

                if(null != sosMessage) {
                    if (new Date().getTime() - CacheUtil.getInstance().getBraceletTime(sosMessage.getBracelet()) > 5000) {
                        NotificationHelper.getInstance().showNotification(context, sosMessage);
                        EventBus.getDefault().post(sosMessage);
                        CacheUtil.getInstance().setBraceletTime(sosMessage.getBracelet(), new Date().getTime());
                    }
                }

//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        } else if(YunBaManager.PRESENCE_RECEIVED_ACTION.equals(intent.getAction())) {
            String topic   = intent.getStringExtra(YunBaManager.MQTT_TOPIC);
            String payload = intent.getStringExtra(YunBaManager.MQTT_MSG);
            StringBuilder showMsg = new StringBuilder();
            showMsg.append("Received message presence: ").append(YunBaManager.MQTT_TOPIC)
                    .append(" = ").append(topic).append(" ")
                    .append(YunBaManager.MQTT_MSG).append(" = ").append(payload);
            Log.d(TAG, showMsg.toString());
        }
        */
    }

}