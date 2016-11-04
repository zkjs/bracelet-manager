package com.zkjinshi.braceletmanager.common.http;

import android.content.Context;

import com.zkjinshi.braceletmanager.common.utils.DialogUtil;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yejun on 11/3/16.
 * Copyright (C) 2016 qinyejun
 */

public abstract class HttpLoadingCallback<T> extends HttpBaseCallback<T>{
    private Context context;
    private String msg = "";

    public HttpLoadingCallback(Context context) {
        this.context = context;
    }

    @Override
    public void onRequestBefore(Request request) {
        showLoading();
    }

    @Override
    public void onFailure(Request request, IOException e) {
        DialogUtil.getInstance().cancelProgressDialog();
        DialogUtil.getInstance().showToast(context,"网络请求失败:"+e.getMessage());
    }

    @Override
    public void onError(Response response, int code, Exception e) {
        DialogUtil.getInstance().showToast(context,"网络请求失败:"+code);
    }

    //token过期
    @Override
    public void onTokenExpired(Response response) {
        DialogUtil.getInstance().showToast(context,"网络请求失败:Token过期");
        //取消订阅别名
        /*YunBaSubscribeManager.getInstance().cancelAlias(context);
        CacheUtil.getInstance().setLogin(false);
        BaseApplication.getInst().clear();
        Intent intent = new Intent(context,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ((Activity)context).startActivity(intent);*/
    }

    @Override
    public void onFinish(Boolean success) {
        DialogUtil.getInstance().cancelProgressDialog();
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private void showLoading() {
        DialogUtil.getInstance().showAvatarProgressDialog(context,msg);
    }
}
