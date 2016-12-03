package com.zkjinshi.braceletmanager.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by yejun on 11/3/16.
 * Copyright (C) 2016 qinyejun
 */

public class CacheUtil {

    private static final String PARKING_CACHE = "bracelet_manager_cache";

    private CacheUtil() {
    }

    private static CacheUtil instance;

    public synchronized static CacheUtil getInstance() {
        if (null == instance) {
            instance = new CacheUtil();
        }
        return instance;
    }

    private Context context;

    public void init(Context context) {
        this.context = context;
    }

    /**
     * 设置用户登录状态
     * @param isLogin
     */
    public void setLogin(boolean isLogin) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putBoolean("is_login", isLogin).commit();
    }

    /**
     * 获取用户登录状态
     * @return
     */
    public boolean isLogin() {
        if (null == context) {
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getBoolean("is_login", false);
    }

    /**
     * 保存登录token
     * @param token
     */
    public void setToken(String token) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putString("token", token).commit();
    }

    /**
     * 获取登录token
     * @return
     */
    public String getToken() {
        if (null == context) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getString("token", null);
    }

    /**
     * 保存用户id
     * @param userId
     */
    public void setUserId(String userId) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putString("userId", userId).commit();
    }

    /**
     * 获取用户id
     * @return
     */
    public String getUserId() {
        if (null == context) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getString("userId", null);
    }

    /**
     * 保存用户password
     * @param password
     */
    public void setPassword(String password) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putString("password", password).commit();
    }

    /**
     * 获取用户password
     * @return
     */
    public String getPassword() {
        if (null == context) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getString("password", null);
    }

    /**
     * mqtt topic
     * @param topic
     */
    public void setMqttTopic(String topic) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putString("mqtt_topic", topic).commit();
    }

    /**
     * mqtt topic
     * @return
     */
    public String getMqttTopic() {
        if (null == context) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getString("mqtt_topic", null);
    }

    /**
     * 保存LocalServer
     * @param host
     */
    public void setLocalMqttServer(String host) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putString("local_mqtt_server", host).commit();
    }

    /**
     * 获取LocalServer
     * @return
     */
    public String getLocalMqttServer() {
        if (null == context) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getString("local_mqtt_server", null);
    }

    /**
     * 保存本地mqtt server port
     * @param port
     */
    public void setLocalMqttPort(String port) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putString("local_mqtt_port", port).commit();
    }

    /**
     * 获取本地mqtt server port
     * @return
     */
    public String getLocalMqttPort() {
        if (null == context) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getString("local_mqtt_port", "1883");
    }

    /**
     * 保存ApiServer
     * @param host
     */
    public void setLocalApiServer(String host) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putString("local_api_host", host).commit();
    }

    /**
     * 获取ApiServer
     * @return
     */
    public String getLocalApiServer() {
        if (null == context) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getString("local_api_host", null);
    }

    /**
     * 保存本地 api server port
     * @param port
     */
    public void setLocalApiPort(String port) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putString("local_api_port", port).commit();
    }

    /**
     * 获取本地 api server port
     * @return
     */
    public String getLocalApiPort() {
        if (null == context) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getString("local_api_port", "80");
    }

    /**
     * 保存DeviceNo
     * @param deviceNo
     */
    public void setDeviceNo(String deviceNo) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putString("device_no", deviceNo).commit();
    }

    /**
     * 获取DeviceNo
     * @return
     */
    public String getDeviceNo() {
        if (null == context) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getString("device_no", null);
    }

    /**
     * 保存bracelet通知时间
     * @param
     */
    public void setBraceletTime(String bracelet, long timestamp) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putLong(bracelet, timestamp).commit();
    }



    /**
     * 获取Bracelet通知时间
     * @return
     */
    public long getBraceletTime(String bracelet) {
        if (null == context) {
            return 0;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getLong(bracelet,0);
    }


    /**
     * 设置网络配置版本号
     * @param netConfigVersion
     */
    public void setNetConfigVersion(int netConfigVersion) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putInt("net_config_version", netConfigVersion).commit();
    }

    /**
     * 获取网络配置版本号
     * @return
     */
    public int getNetConfigVersion() {
        if (null == context) {
            return 0;
        }
        SharedPreferences sp = context.getSharedPreferences(PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getInt("net_config_version", 0);
    }

    /**
     * 加密存入缓存
     *
     * @param cacheObj
     */
    public void saveObjCache(Object cacheObj) {
        if (null != cacheObj) {
            Gson gson = new Gson();
            String json = gson.toJson(cacheObj);
            String key = cacheObj.getClass().getSimpleName();
            try {
                String encryptedData = Base64Encoder.encode(json);// base 64加密
                SharedPreferences sp = context.getSharedPreferences(PARKING_CACHE,
                        Context.MODE_PRIVATE);
                sp.edit().putString(key, encryptedData).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解密取出缓存对象
     *
     * @param cacheObj
     * @return
     */
    public Object getObjCache(Object cacheObj) {
        if (null == cacheObj) {
            return null;
        }
        if (null != cacheObj) {
            SharedPreferences sp = context.getSharedPreferences(PARKING_CACHE,
                    Context.MODE_PRIVATE);
            String key = cacheObj.getClass().getSimpleName();
            String value = "";
            String encryptedData = sp.getString(key, "");
            if (!TextUtils.isEmpty(encryptedData)) {
                try {
                    value = Base64Decoder.decode(encryptedData);
                    Gson gson = new Gson();
                    cacheObj = gson.fromJson(value, cacheObj.getClass());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return cacheObj;
    }

    /**
     *  存入集合缓存的通用方法
     * @param key
     * @param cacheList
     * @param <T>
     */
    public <T> void saveListCache(String key,
                                  ArrayList<T> cacheList) {
        if (null != cacheList && cacheList.size() >= 0) {
            Gson gson = new Gson();
            String json = gson.toJson(cacheList);
            try {
                String encryptedData = Base64Encoder.encode(json);// base
                // 64加密
                SharedPreferences sp = context.getSharedPreferences(PARKING_CACHE, Context.MODE_PRIVATE);
                sp.edit().putString(key, encryptedData).commit();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("info", "saveListCache Exception:" + e);
            }
        }
    }

    /**
     * 取集合缓存的通用方法
     * @param key
     * @return
     */
    public String getListStrCache(String key) {
        SharedPreferences sp = context.getSharedPreferences(PARKING_CACHE,
                Context.MODE_PRIVATE);
        String value = "";
        String encryptedData = sp.getString(key, "");
        if (!TextUtils.isEmpty(encryptedData)) {
            try {
                value = Base64Decoder.decode(encryptedData);// base 64解密
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("info", "getListCache Exception:" + e);
            }
        }
        return value;
    }

}
