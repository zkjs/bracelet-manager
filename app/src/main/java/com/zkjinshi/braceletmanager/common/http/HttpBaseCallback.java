package com.zkjinshi.braceletmanager.common.http;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yejun on 11/3/16.
 * Copyright (C) 2016 qinyejun
 */

public abstract class HttpBaseCallback<T> {
    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass)
    {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public HttpBaseCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }

    public abstract void onRequestBefore(Request request);

    public abstract void onFailure(Request request, IOException e);

    public abstract void onSuccess(Response response, T t);

    public abstract void onError(Response response, int code, Exception e);

    public abstract void onFinish(Boolean success);

    public abstract void onTokenExpired(Response response);
}
