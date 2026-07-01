/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.bean;

import game.core.db.BaseBean;

/**
 * @author zhaibiao
 */
public class RoleLoginInfoBean extends BaseBean {

    private String platUserId;//平台的账号名（有可能是渠道的登录ID）
    private long userId; // 账号ＩＤ
    private String platformName; // 平台名
    private String os;//玩家的接口系统
    private String maCode;//机器设备码
    private String uuid; // funcell生成的uuid
    private long createTime;//创建时间
    private long lastLoginTime;//最后登陆时间

    public String getPlatUserId() {
        return platUserId;
    }

    public void setPlatUserId(String platUserId) {
        this.platUserId = platUserId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getMaCode() {
        return maCode;
    }

    public void setMaCode(String maCode) {
        this.maCode = maCode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

}
