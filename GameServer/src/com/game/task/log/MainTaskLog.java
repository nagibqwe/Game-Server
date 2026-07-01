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

public class MainTaskLog extends BaseLogBean {

    private long roleId;
    private long userId;
    private String platformName;
    private String rolename;

    private long finishmodelId;
    private String finishtaskInfo;
    private long finishonlinetime;
    private int finishlevel;

    private long acceptmodelId;
    private long acceptonlinetime;
    private int acceptlevel;
    private int sid;

    private int accmoney; //接任务时身上的铜币
    private int fshmoney; //完成任务时身上的铜币

    @Log(logField = "accmoney", fieldType = "int", index = "0")
    public int getAccmoney() {
        return accmoney;
    }

    public void setAccmoney(int accmoney) {
        this.accmoney = accmoney;
    }

    @Log(logField = "fshmoney", fieldType = "int", index = "0")
    public int getFshmoney() {
        return fshmoney;
    }

    public void setFshmoney(int fshmoney) {
        this.fshmoney = fshmoney;
    }

    @Log(logField = "platformName", fieldType = "varchar(100)", index = "0")
    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @Log(logField = "rolename", fieldType = "varchar(100)", index = "0")
    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    @Log(logField = "sid", fieldType = "int", index = "0")
    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }
    private static final Logger logger = LogManager.getLogger("MainTaskLog");

    @Log(logField = "userid", fieldType = "bigint", index = "2")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Log(logField = "roleId", fieldType = "bigint", index = "3")
    public long getRoleId() {
        return roleId;
    }

    @Log(logField = "fshmodelId", fieldType = "int", index = "0")
    public long getFinishmodelId() {
        return finishmodelId;
    }

    @Log(logField = "fshtaskInfo", fieldType = "longtext", index = "0")
    public String getFinishtaskInfo() {
        return finishtaskInfo;
    }


    @Log(logField = "fshonlinetime", fieldType = "bigint", index = "0")
    public long getFinishonlinetime() {
        return finishonlinetime;
    }

    @Log(logField = "fshlevel", fieldType = "int", index = "0")
    public int getFinishlevel() {
        return finishlevel;
    }

    @Log(logField = "accmodelId", fieldType = "int", index = "0")
    public long getAcceptmodelId() {
        return acceptmodelId;
    }

    @Log(logField = "acconlinetime", fieldType = "bigint", index = "0")
    public long getAcceptonlinetime() {
        return acceptonlinetime;
    }

    @Log(logField = "acclevel", fieldType = "int", index = "0")
    public int getAcceptlevel() {
        return acceptlevel;
    }

    public void setFinishmodelId(long finishmodelId) {
        this.finishmodelId = finishmodelId;
    }

    public void setFinishtaskInfo(String finishtaskInfo) {
        this.finishtaskInfo = finishtaskInfo;
    }

    public void setAcceptmodelId(long acceptmodelId) {
        this.acceptmodelId = acceptmodelId;
    }
    
    public void setFinishonlinetime(long finishonlinetime) {
        this.finishonlinetime = finishonlinetime;
    }

    public void setFinishlevel(int finishlevel) {
        this.finishlevel = finishlevel;
    }

    public void setAcceptonlinetime(long acceptonlinetime) {
        this.acceptonlinetime = acceptonlinetime;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public void setAcceptlevel(int acceptlevel) {
        this.acceptlevel = acceptlevel;
    }

}
