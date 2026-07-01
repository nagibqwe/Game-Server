/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.bean;

import game.core.db.BaseBean;

/**
 * @author hewei
 */
public class UserActivityBean extends BaseBean {

    private long userId; // 账号ID值\
    private int serverId;//服务器id
    private String vipRewardGet;//vip等级奖励领取结果
    private String rewardContext; //充值消费领奖记录
    private String rewardVesionIdList;//已领取的资源更新奖励对应资源版本号列表
    private int isEvaluated;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getVipRewardGet() {
        return vipRewardGet;
    }

    public void setVipRewardGet(String vipRewardGet) {
        this.vipRewardGet = vipRewardGet;
    }

    public String getRewardContext() {
        return rewardContext;
    }

    public void setRewardContext(String rewardContext) {
        this.rewardContext = rewardContext;
    }

    public String getRewardVesionIdList() {
        return rewardVesionIdList;
    }

    public void setRewardVesionIdList(String rewardVesionIdList) {
        this.rewardVesionIdList = rewardVesionIdList;
    }

    public int getIsEvaluated() {
        return isEvaluated;
    }

    public void setIsEvaluated(int isEvaluated) {
        this.isEvaluated = isEvaluated;
    }

}
