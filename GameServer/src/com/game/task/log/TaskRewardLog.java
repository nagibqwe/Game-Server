/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.task.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 任务奖励记录
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class TaskRewardLog extends BaseLogBean {

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }
    private static final Logger LOG = LogManager.getLogger("TaskRewardLog");

    @Override
    public void logToFile() {
        LOG.error(buildSql());
    }

    private long roleId;
    private long userId;
    private String platformname;
    private String rolename;
    private int type;
    private int modelId;
    private String reward;
    private int rewardType;//领奖的倍数

    @Log(logField = "roleId", fieldType = "bigint", index = "0")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(logField = "userId", fieldType = "bigint", index = "0")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Log(logField = "platfromname", fieldType = "varchar(100)", index = "0")
    public String getPlatformname() {
        return platformname;
    }

    public void setPlatformname(String platformname) {
        this.platformname = platformname;
    }

    @Log(logField = "rolename", fieldType = "varchar(100)", index = "0")
    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    @Log(logField = "type", fieldType = "int", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(logField = "modelId", fieldType = "int", index = "0")
    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    @Log(logField = "reward", fieldType = "varchar(300)", index = "0")
    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    @Log(logField = "rewardType", fieldType = "int", index = "0")
    public int getRewardType() {
        return rewardType;
    }

    public void setRewardType(int rewardType) {
        this.rewardType = rewardType;
    }

}
