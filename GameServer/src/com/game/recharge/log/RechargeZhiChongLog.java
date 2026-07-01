/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.recharge.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 直充日志记录的接口
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class RechargeZhiChongLog extends BaseLogBean {

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    private static final Logger LOG = LogManager.getLogger("RechargeZhiChongLog");

    @Override
    public void logToFile() {
        LOG.error(buildSql());
    }

    private String orderId;
    private String token;
    private int serverId;
    private long roleId;
    private long userId;
    private String roleName;
    private long dealTime;
    private String error;
    private int level;
    private int isOk;

    @Log(fieldType = "varchar(50)", index = "0", logField = "orderId")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Log(fieldType = "text", index = "0", logField = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Log(fieldType = "int", index = "0", logField = "serverId")
    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    @Log(fieldType = "bigint", index = "0", logField = "roleId")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(fieldType = "bigint", index = "0", logField = "userId")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Log(fieldType = "varchar(50)", index = "0", logField = "roleName")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Log(fieldType = "bigint", index = "0", logField = "dealTime")
    public long getDealTime() {
        return dealTime;
    }

    public void setDealTime(long dealTime) {
        this.dealTime = dealTime;
    }

    @Log(fieldType = "text", index = "0", logField = "error")
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Log(fieldType = "int", index = "0", logField = "level")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Log(fieldType = "int", index = "0", logField = "isOk")
    public int getIsOk() {
        return isOk;
    }

    public void setIsOk(int isOk) {
        this.isOk = isOk;
    }

}
