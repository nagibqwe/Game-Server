/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.activity.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * @author hewei@haowan123.com
 */
public class ActivityGetLog extends BaseLogBean {

    private long roleId;
    private String parms;
    private long activityId;
    private long actionId;
    private int sid;
    private int monsterModel;
    private long monsterId;
    private String platformName;

    @Log(logField = "sid", fieldType = "int", index = "0")
    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    @Log(logField = "activityId", fieldType = "bigint", index = "2")
    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    @Log(fieldType = "bigint", logField = "actionId", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    @Log(fieldType = "bigint", logField = "roleid", index = "0")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(logField = "parms", fieldType = "varchar(128)", index = "0")
    public String getParms() {
        return parms;
    }

    public void setParms(String parms) {
        this.parms = parms;
    }

    @Log(fieldType = "int", logField = "monsterModel", index = "0")
    public int getMonsterModel() {
        return monsterModel;
    }

    public void setMonsterModel(int monsterModel) {
        this.monsterModel = monsterModel;
    }

    @Log(fieldType = "bigint", logField = "monsterId", index = "0")
    public long getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(long monsterId) {
        this.monsterId = monsterId;
    }
    
    @Log(logField = "platformName", fieldType = "varchar(128)", index = "0")
    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    private static final Logger logger = LogManager.getLogger("ActivityGetLog");
}
