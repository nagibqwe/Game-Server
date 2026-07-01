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
 * @author soko <xuchangming@haowan123.com>
 */
public class ActivityBossDieLog extends BaseLogBean {

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.YEAR;
    }

    private static final Logger log = LogManager.getLogger("ActivityBossDieLog");

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

    private int monsterModel;
    private long activityId;
    private long monsterId;

    private long roleId;
    private String rolename;
    private int hitIndex;
    private long hitvalue;
    private int mapId;

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

    @Log(fieldType = "bigint", logField = "activityId", index = "0")
    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    @Log(fieldType = "bigint", logField = "roleId", index = "0")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(fieldType = "varchar(100)", logField = "rolename", index = "0")
    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    @Log(fieldType = "int", logField = "hitIndex", index = "0")
    public int getHitIndex() {
        return hitIndex;
    }

    public void setHitIndex(int hitIndex) {
        this.hitIndex = hitIndex;
    }

    @Log(fieldType = "bigint", logField = "hitvalue", index = "0")
    public long getHitvalue() {
        return hitvalue;
    }

    public void setHitvalue(long hitvalue) {
        this.hitvalue = hitvalue;
    }

    @Log(fieldType = "int", logField = "mapId", index = "0")
    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

}
