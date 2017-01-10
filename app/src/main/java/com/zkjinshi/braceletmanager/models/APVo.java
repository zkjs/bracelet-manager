package com.zkjinshi.braceletmanager.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yejun on 12/25/16.
 * Copyright (C) 2016 qinyejun
 */

public class APVo implements Serializable {
    private String apid;
    private String alias;
    private String address;
    private int floor;
    private int camera;
    private String triggerLogic;
    private List<String> triggers;
    private String update;
    private int working;

    public String getApid() {
        return apid;
    }

    public void setApid(String apid) {
        this.apid = apid;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getCamera() {
        return camera;
    }

    public void setCamera(int camera) {
        this.camera = camera;
    }

    public String getTriggerLogic() {
        return triggerLogic;
    }

    public void setTriggerLogic(String triggerLogic) {
        this.triggerLogic = triggerLogic;
    }

    public List<String> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<String> triggers) {
        this.triggers = triggers;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public int getWorking() {
        return working;
    }

    public void setWorking(int working) {
        this.working = working;
    }
}
