package com.zkjinshi.braceletmanager.models;

import java.io.Serializable;

/**
 * Created by yejun on 11/4/16.
 * Copyright (C) 2016 qinyejun
 */

public class PatientVo implements Serializable {
    private String bracelet;
    private String name;
    private String patientGender;
    private String patientName;
    private String patientRemark;
    private String patientRoom;
    private String patientAge;
    private int attached;

    public String getBracelet() {
        return bracelet;
    }

    public void setBracelet(String bracelet) {
        this.bracelet = bracelet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientRemark() {
        return patientRemark;
    }

    public void setPatientRemark(String patientRemark) {
        this.patientRemark = patientRemark;
    }

    public String getPatientRoom() {
        return patientRoom;
    }

    public void setPatientRoom(String patientRoom) {
        this.patientRoom = patientRoom;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public int getAttached() {
        return attached;
    }

    public void setAttached(int attached) {
        this.attached = attached;
    }
}
