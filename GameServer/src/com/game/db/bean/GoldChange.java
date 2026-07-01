/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.bean;

import game.core.db.BaseBean;

/**
 * @author hewei@haowan123.com
 */
public class GoldChange extends BaseBean {

    private int serverId;//服务器id
    private long userId;//账号id
    private long roleId;//角色id
    private String platformName;//渠道名
    private int beforeNum;//变化前元宝数量
    private int changeNum;//改变数量
    private int afterNum;//改变后数量
    private int reason;//改变原因
    private int time;//改变时间

    /**
     * 服务器id
     *
     * @return
     */
    public int getServerId() {
        return serverId;
    }

    /**
     * 服务器id
     *
     * @param roleId
     */
    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    /**
     * 角色id
     *
     * @return
     */
    public long getRoleId() {
        return roleId;
    }

    /**
     * 角色id
     *
     * @param roleId
     */
    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    /**
     * 账号id
     *
     * @return
     */
    public long getUserId() {
        return userId;
    }

    /**
     * 账号id
     *
     * @param userId
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * 渠道名
     *
     * @return
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * 渠道名
     *
     * @param platformName
     */
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    /**
     * 变化前元宝数量
     *
     * @return
     */
    public int getBeforeNum() {
        return beforeNum;
    }

    /**
     * 变化前元宝数量
     *
     * @param beforeNum
     */
    public void setBeforeNum(int beforeNum) {
        this.beforeNum = beforeNum;
    }

    /**
     * 改变数量
     *
     * @return
     */
    public int getChangeNum() {
        return changeNum;
    }

    /**
     * 改变数量
     *
     * @param changeNum
     */
    public void setChangeNum(int changeNum) {
        this.changeNum = changeNum;
    }

    /**
     * 改变后数量
     *
     * @return
     */
    public int getAfterNum() {
        return afterNum;
    }

    /**
     * 改变后数量
     *
     * @param afterNum
     */
    public void setAfterNum(int afterNum) {
        this.afterNum = afterNum;
    }

    /**
     * 改变原因
     *
     * @return
     */
    public int getReason() {
        return reason;
    }

    /**
     * 改变原因
     *
     * @param afterNum
     */
    public void setReason(int reason) {
        this.reason = reason;
    }

    /**
     * 改变时间
     *
     * @return
     */
    public int getTime() {
        return time;
    }

    /**
     * 改变时间
     *
     * @param time
     */
    public void setTime(int time) {
        this.time = time;
    }

}
