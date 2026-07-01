/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.player.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class LevelChangeLog extends BaseLogBean {

    private int nowLevel;
    private int oldLevel;
    private long roleId;
    private long changeExp;
    private int reason;
    private long beforeExp;
    private long afterExp;
    private long actionId;
    private int sid;

    @Log(logField = "NowLevel", fieldType = "int", index = "0")
    public int getnowLevel() {
        return nowLevel;
    }

    public void setNowLevel(int nowLevel) {
        this.nowLevel = nowLevel;
    }

    @Log(logField = "oldLevel", fieldType = "int", index = "0")
    public int getOldLevel() {
        return oldLevel;
    }

    public void setOldLevel(int oldLevel) {
        this.oldLevel = oldLevel;
    }

    @Log(logField = "sid", fieldType = "int", index = "0")
    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Log(logField = "changeExp", fieldType = "bigint", index = "0")
    public long getChangeExp() {
        return changeExp;
    }

    public void setChangeExp(long changeExp) {
        this.changeExp = changeExp;
    }

    @Log(logField = "reason", fieldType = "int", index = "0")
    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    @Log(logField = "beforeExp", fieldType = "bigint", index = "0")
    public long getBeforeExp() {
        return beforeExp;
    }

    public void setBeforeExp(long beforeExp) {
        this.beforeExp = beforeExp;
    }

    @Log(logField = "afterExp", fieldType = "bigint", index = "0")
    public long getAfterExp() {
        return afterExp;
    }

    public void setAfterExp(long afterExp) {
        this.afterExp = afterExp;
    }

    @Log(logField = "roleId", fieldType = "bigint", index = "0")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long setActionId(long action) {
        return actionId = action;
    }

    @Log(logField = "actionId", fieldType = "bigint", index = "0")
    public long getActionId() {
        return actionId;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }
    private static final Logger logger = LogManager.getLogger("LevelChangeLog");
}
