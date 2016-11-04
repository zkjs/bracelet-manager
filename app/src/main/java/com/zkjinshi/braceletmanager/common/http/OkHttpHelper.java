package com.zkjinshi.braceletmanager.common.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.zkjinshi.braceletmanager.common.utils.CacheUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yejun on 11/3/16.
 * Copyright (C) 2016 qinyejun
 */

public class OkHttpHelper {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/jpeg");
    private  static OkHttpClient okHttpClient;
    private Gson mGson;
    private Handler handler;

    private  OkHttpHelper() {
        okHttpClient = new OkHttpClient();
        mGson = new Gson();
        handler = new Handler(Looper.getMainLooper()) ;
    }

    public static OkHttpHelper getInstance() {
        return new OkHttpHelper();
    }

    public void get(String url, HttpBaseCallback callback) {
        Request request = buildRequest(url,null,HttpMethodType.GET);
        doRequest(request, callback);
    }

    public void post(String url, Map<String,String> params, HttpBaseCallback callback) {
        Request request = buildRequest(url,params,HttpMethodType.POST);
        doRequest(request, callback);
    }

    public void put(String url, Map<String,String> params, HttpBaseCallback callback) {
        Request request = buildRequest(url,params,HttpMethodType.PUT);
        doRequest(request, callback);
    }

    public void doRequest(final Request request, final HttpBaseCallback callback) {

        callback.onRequestBefore(request);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(request, e);
                    }
                });
                callbackFinish(callback, false);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if(response.isSuccessful()) {
                    String resultStr = response.body().string();
                    if(callback.mType == String.class){
                        callbackSuccess(callback, response, resultStr);
                        callbackFinish(callback, true);
                    } else {
                        try {
                            Object object = mGson.fromJson(resultStr,callback.mType);
                            callbackSuccess(callback, response, object);
                            callbackFinish(callback, true);
                        } catch (JsonParseException e) {
                            callbackError(callback,response);
                            callbackFinish(callback, false);
                        }
                    }
                } else if (response.code() == 401) {
                    tokenExpired(callback, response);
                    callbackFinish(callback, false);
                } else {
                    callbackError(callback,response);
                    callbackFinish(callback, false);
                }
            }
        });
    }

    public void uploadImage(String url, String filePath, Map<String,String> params, final HttpBaseCallback callback) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MEDIA_TYPE_IMAGE, file);

        // 初始化请求体对象，设置Content-Type以及文件数据流
        /*RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)			// multipart/form-data
                .addFormDataPart("image", file.getName(), requestFile)
                .build();*/

        // 封装OkHttp请求对象，初始化请求参数
        final Request request = new Request.Builder()
                .header("authorization", CacheUtil.getInstance().getToken())
                .url(url)
                .post(requestFile)
                .build();

        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient  = httpBuilder
                .connectTimeout(100, TimeUnit.SECONDS)			// 设置请求超时时间
                .writeTimeout(150, TimeUnit.SECONDS)
                .build();

        callback.onRequestBefore(request);
        // 发起异步网络请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if(response.isSuccessful()) {
                    String resultStr = response.body().string();
                    if(callback.mType == String.class){
                        callbackSuccess(callback, response, resultStr);
                        callbackFinish(callback, true);
                    } else {
                        try {
                            Object object = mGson.fromJson(resultStr,callback.mType);
                            callbackSuccess(callback, response, object);
                            callbackFinish(callback, true);
                        } catch (JsonParseException e) {
                            callbackError(callback,response);
                            callbackFinish(callback, false);
                        }
                    }
                } else if (response.code() == 401) {
                    tokenExpired(callback, response);
                    callbackFinish(callback, false);
                } else {
                    callbackError(callback,response);
                    callbackFinish(callback, false);
                }
            }
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(request, e);
                    }
                });
                callbackFinish(callback, false);
            }
        });
    }

    private Request buildRequest(String url, Map<String, String> params, HttpMethodType type) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);

        if( type == HttpMethodType.GET ) {

        } else if( type == HttpMethodType.POST ) {
            builder.post(buildFormData(params));
        } else if( type == HttpMethodType.PUT ) {
            builder.put(buildFormData(params));
        }

        String token = CacheUtil.getInstance().getToken();
        if (token != null && !token.isEmpty()) {
            Log.i("token",token);
            builder.header("authorization", token);
        }
        return builder.build();
    }

    private RequestBody buildFormData(Map<String,String> params) {
        JSONObject parameter = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, parameter.toString());
        return body;
    }

    private void callbackSuccess(final HttpBaseCallback callback, final Response response, final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, object);
            }
        });
    }

    private void callbackError(final HttpBaseCallback callback, final Response response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), null);
            }
        });
    }

    private void callbackFinish(final HttpBaseCallback callback, final Boolean success) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFinish(success);
            }
        });
    }

    private void tokenExpired(final HttpBaseCallback callback, final Response response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onTokenExpired(response);
            }
        });
    }



    enum HttpMethodType {
        GET,
        POST,
        PUT
    }
}