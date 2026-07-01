/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.gm.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author Administrator
 */
public class GmCommandLog extends BaseLogBean{
    
    private static final Logger logger = LogManager.getLogger("GmCommandLog");

    @Override
    public TableCheckStepEnum getRollingStep() {
	return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
	logger.error(buildSql());
    }
    
    private long userId;       //账号id
    private String roleName;   //角色名
    private long roleId;       //角色id
    private int sid;           //角色区服
    private int gmLevel;       //gm等级
    private String command;    //GM命令
    //private int type;          //0：游戏内GM命令 1：后台GM命令 (只记录游戏内部GM命令的log，后台GM命令后台去进行记录)
    
    @Log(logField="userId",fieldType="BIGINT(20)",index="2")
    public long getUserId() {
	return userId;
    }

    public void setUserId(long userId) {
	this.userId = userId;
    }

    @Log(logField="roleName",fieldType="varchar(200)",index="3")
    public String getRoleName() {
	return roleName;
    }

    public void setRoleName(String roleName) {
	this.roleName = roleName;
    }

    @Log(logField="roleId",fieldType="BIGINT(20)",index="4")
    public long getRoleId() {
	return roleId;
    }

    public void setRoleId(long roleId) {
	this.roleId = roleId;
    }

    @Log(logField="sid",fieldType="INT(4)",index="0")
    public int getSid() {
	return sid;
    }

    public void setSid(int sid) {
	this.sid = sid;
    }

    @Log(logField="gmLevel",fieldType="INT(4)",index="0")
    public int getGmLevel() {
	return gmLevel;
    }

    public void setGmLevel(int gmLevel) {
	this.gmLevel = gmLevel;
    }

    @Log(logField="command",fieldType="varchar(512)",index="0")
    public String getCommand() {
	return command;
    }

    public void setCommand(String command) {
	this.command = command;
    }
    
}
