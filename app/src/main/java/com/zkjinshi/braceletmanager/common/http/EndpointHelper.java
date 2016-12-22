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
        return  ConfigUtil.getInstance().getRemoteApiDomain()+"track/" + bracelet;
    }

    /**
     * 手环轨迹(相对坐标)
     * @return
     */
    public static String braceletTrackCoords(String bracelet){
        return  ConfigUtil.getInstance().getRemoteApiDomain()+"track/rel/" + bracelet;
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
     * 建筑物地图
     * @return
     */
    public static String buildingMap() {
        return  ConfigUtil.getInstance().getApiDomain()+"map";
    }

    /**
     * 楼层地图
     * @return
     */
    public static String floorMap(String buildingID) {
        return  ConfigUtil.getInstance().getApiDomain()+"map/" + buildingID;
    }

    /**
     * 手环照片
     * @param bracelet
     * @return
     */
    public static String braceletPhotos(String bracelet) {
        return  ConfigUtil.getInstance().getApiDomain()+"bracelet/photos/" + bracelet;
    }

}