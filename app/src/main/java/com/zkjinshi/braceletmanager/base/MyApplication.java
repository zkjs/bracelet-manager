package com.zkjinshi.braceletmanager.base;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.zkjinshi.braceletmanager.common.mqtt.MqttManager;
import com.zkjinshi.braceletmanager.common.utils.CacheUtil;
import com.zkjinshi.braceletmanager.common.utils.ConfigUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;


/**
 * Created by yejun on 11/3/16.
 * Copyright (C) 2016 qinyejun
 */

public class MyApplication  extends BaseApplication {

    public static final String TAG = MyApplication.class.getSimpleName();

    public int currentNetConfigVersion = 1;//网络配置项
    private static Context mContext;
    private static boolean activityVisible;
    private static boolean authRequired;

    private static boolean authActivityShowing;

    @Override
    public void onCreate() {
        super.onCreate();

        initContext();
        initCache();
        saveConfig();
        initMqtt();
    }

    private void initMqtt() {
        if (CacheUtil.getInstance().getLocalServer() != null) {
            MqttManager mqttManager = MqttManager.getInstance(getApplicationContext());
            mqttManager.connect();
        }
    }

    public static Context getContext(){
        return mContext;
    }

    public static boolean isAuthRequired() {
        return authRequired;
    }

    public static void setAuthRequired(boolean authRequired) {
        MyApplication.authRequired = authRequired;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static boolean isAuthActivityShowing() {
        return authActivityShowing;
    }

    public static void setAuthActivityShowing(boolean authActivityShowing) {
        MyApplication.authActivityShowing = authActivityShowing;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!MyApplication.isActivityVisible() && !TextUtils.isEmpty(CacheUtil.getInstance().getPassword())) {
                    authRequired = true;
                }
            }
        }, 500);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processName;
    }

    public void initContext(){
        BaseContext.getInstance().init(this);
    }

    /**
     * 初始化配置
     */
    private void saveConfig(){
        try {
            File f = new File(this.getFilesDir(), "config.xml");
            InputStream is;
            int netConfigVersion = CacheUtil.getInstance().getNetConfigVersion();
            if (f.exists()) {
                if(netConfigVersion != currentNetConfigVersion){
                    f.delete();
                    is = this.getResources().getAssets()
                            .open("config.xml");
                    ConfigUtil.getInstance(is);
                    ConfigUtil.getInstance().save(this);
                    CacheUtil.getInstance().setNetConfigVersion(currentNetConfigVersion);
                }else {
                    is = new FileInputStream(f);
                    ConfigUtil.getInstance(is);
                }
            } else {
                is = this.getResources().getAssets()
                        .open("config.xml");
                ConfigUtil.getInstance(is);
                ConfigUtil.getInstance().save(this);
            }
        } catch (IOException e) {
            Log.e(TAG, "找不到assets/目录下的config.xml配置文件", e);
            e.printStackTrace();
        }
    }

    /**
     * 初始化缓存
     */
    private void initCache(){
        CacheUtil.getInstance().init(this);
    }

}
