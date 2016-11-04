package com.zkjinshi.braceletmanager.response;

import java.io.Serializable;

/**
 * Created by yejun on 11/3/16.
 * Copyright (C) 2016 qinyejun
 */

public class BaseResponse implements Serializable {

    private String status;
    private String error;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}