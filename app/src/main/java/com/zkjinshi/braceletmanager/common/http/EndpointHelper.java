package com.zkjinshi.braceletmanager.common.http;

import com.zkjinshi.braceletmanager.common.utils.ConfigUtil;

/**
 * Created by yejun on 11/3/16.
 * Copyright (C) 2016 qinyejun
 */

public class EndpointHelper {

    /**
     * SOS呼救应答
     * @return
     */
    public static String answerSOS(){
        return ConfigUtil.getInstance().getApiDomain()+"rescue";
    }

    /**
     * 手环轨迹
     * @return
     */
    public static String braceletTrack(String bracelet){
        return  ConfigUtil.getInstance().getApiDomain()+"track/" + bracelet;
    }

    /**
     * 手环轨迹(相对指标)
     * @return
     */
    public static String braceletTrackCoords(String bracelet){
        return  ConfigUtil.getInstance().getApiDomain()+"track/rel/" + bracelet;
    }

    /**
     * 手环列表
     * @return
     */
    public static String bracelets(){
        return  ConfigUtil.getInstance().getApiDomain()+"bracelet/";
    }

    /**
     * 病人列表
     * @return
     */
    public static String patients(){
        return  ConfigUtil.getInstance().getApiDomain()+"bracelet/?binded=1";
    }

    /**
     * 绑定手环
     * @return
     */
    public static String bindBracelet(){
        return  ConfigUtil.getInstance().getApiDomain()+"bracelet";
    }

    /**
     * 解除绑定手环
     * @return
     */
    public static String unbindBracelet(String bracelet){
        return  ConfigUtil.getInstance().getApiDomain()+"bracelet/binded/" + bracelet;
    }

    /**
     * 病人轨迹
     * @return
     */
    public static String patientTrack(String bracelet) {
        return  ConfigUtil.getInstance().getApiDomain()+"track/" + bracelet;
    }

}