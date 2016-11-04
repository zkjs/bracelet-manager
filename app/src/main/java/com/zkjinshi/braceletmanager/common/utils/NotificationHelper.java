package com.zkjinshi.braceletmanager.common.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.zkjinshi.braceletmanager.Pages.MainActivity;
import com.zkjinshi.braceletmanager.Pages.PatientTrackActivity;
import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.models.SOSMessage;

import java.util.List;


/**
 * Created by yejun on 11/3/16.
 * Copyright (C) 2016 qinyejun
 */

public class NotificationHelper {

    public static int NOTIFY_ID = 1;

    private NotificationHelper() {
    }

    private static NotificationHelper instance;

    public synchronized static NotificationHelper getInstance() {
        if (null == instance) {
            instance = new NotificationHelper();
        }
        return instance;
    }

    public void showNotification(Context context, SOSMessage msg){
        // 1.设置显示信息
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setContentTitle(msg.getAlert());
        notificationBuilder.setContentText(msg.getAddress());
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);

        ++NOTIFY_ID;
        // 2.设置点击跳转事件
        Intent intent = new Intent(context, PatientTrackActivity.class);
        intent.putExtra("sos", msg);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFY_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);
        // 3.设置通知栏其他属性
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFY_ID, notificationBuilder.build());

    }

    /**
     * 是否在后台
     *
     * @param context
     * @return
     */
    public boolean isRunningBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return false;
            }
        }
        return true;
    }

}