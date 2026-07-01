/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.redpacket.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 红包的删除日志
 */
public class RedPacketDeleteLog extends BaseLogBean {

    private static final Logger log = LogManager.getLogger(RedPacketDeleteLog.class);

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }
    private long roleId;
    private String roleName;
    private long rpId;
    private int maxvalue;
    private int maxNum;
    private int type;
    private int goldType;
    private int curNum;
    private long createtime;

    @Log(fieldType = "bigint", index = "", logField = "rpId")
    public long getRpId() {
        return rpId;
    }

    public void setRpId(long rpId) {
        this.rpId = rpId;
    }

    @Log(fieldType = "int", index = "", logField = "maxval")
    public int getMaxvalue() {
        return maxvalue;
    }

    public void setMaxvalue(int maxvalue) {
        this.maxvalue = maxvalue;
    }

    @Log(fieldType = "int", index = "", logField = "maxNum")
    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    @Log(fieldType = "int", index = "", logField = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(fieldType = "int", index = "", logField = "goldType")
    public int getGoldType() {
        return goldType;
    }

    public void setGoldType(int goldType) {
        this.goldType = goldType;
    }

    @Log(fieldType = "bigint", index = "", logField = "roleId")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(fieldType = "varchar(100)", index = "", logField = "roleName")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Log(fieldType = "int", index = "", logField = "curNum")
    public int getCurNum() {
        return curNum;
    }

    public void setCurNum(int curNum) {
        this.curNum = curNum;
    }

    @Log(fieldType = "bigint", index = "", logField = "createtime")
    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

}
