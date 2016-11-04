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

    private static final String PARKING_CACHE = "smart_parking_cache";

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
     * 设置指引状态
     * @param isGuide
     */
    public void setGuide(boolean isGuide) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putBoolean("is_guide", isGuide).commit();
    }

    /**
     * 获取指引状态
     * @return
     */
    public boolean isGuide() {
        if (null == context) {
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getBoolean("is_guide", false);
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
     * 设置权限控制列表
     * @param features
     */
    public void setFeatures(Set<String> features) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putStringSet("features",features).commit();
    }

    /**
     * 获取用户id
     * @return
     */
    public Set<String> getFeatures() {
        if (null == context) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getStringSet("features", null);
    }


    /**
     * 保存用户姓名
     * @param userName
     */
    public void setUserName(String userName) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putString("userName", userName).commit();
    }



    /**
     * 获取用户姓名
     * @return
     */
    public String getUserName() {
        if (null == context) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getString("userName","");
    }

    /**
     * 保存用户姓别
     * @param sex
     */
    public void setSex(String sex) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putString("sex", sex).commit();
    }

    /**
     * 获取用户姓别
     * @return
     */
    public String getSex() {
        if (null == context) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getString("sex","0");
    }

    /**
     * 保存用户手机号
     * @param mobilePhone
     */
    public void setUserPhone(String mobilePhone) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putString("mobilePhone", mobilePhone).commit();
    }

    /**
     * 获取用户手机号
     * @return
     */
    public String getUserPhone() {
        if (null == context) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(
                PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getString("mobilePhone","");
    }

    public void setCurrentItem(int currentItem) {
        if (null == context) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(PARKING_CACHE, Context.MODE_PRIVATE);
        sp.edit().putInt("currentItem", currentItem).commit();
    }

    public int getCurrentItem() {
        if (null == context) {
            return 0;
        }
        SharedPreferences sp = context.getSharedPreferences(PARKING_CACHE, Context.MODE_PRIVATE);
        return sp.getInt("currentItem", 0);
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
