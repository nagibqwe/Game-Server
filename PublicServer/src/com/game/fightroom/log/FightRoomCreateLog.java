/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.fightroom.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 房间创建的日志
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class FightRoomCreateLog extends BaseLogBean {

    private static final Logger log = LogManager.getLogger(FightRoomCreateLog.class);

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

    //角色ID
    long roleId;
    //角色名字
    String roleName;
    //平台
    String plat;

    //房间战
    long fid;
    //跨服战的副本ID
    int modelId;
    //创建服的来源服务器Id
    int sid;

    int power;//战斗力

    int isAuto;//是否自动开始

    @Log(logField = "roleId", fieldType = "bigint", index = "0")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(logField = "roleName", fieldType = "varchar(100)", index = "0")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Log(logField = "plat", fieldType = "varchar(100)", index = "0")
    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    @Log(logField = "fid", fieldType = "bigint", index = "0")
    public long getFid() {
        return fid;
    }

    public void setFid(long fid) {
        this.fid = fid;
    }

    @Log(logField = "modelId", fieldType = "int", index = "0")
    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    @Log(logField = "sid", fieldType = "int", index = "0")
    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    @Log(logField = "power", fieldType = "int", index = "0")
    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Log(logField = "isAuto", fieldType = "tinyint", index = "0")
    public int isIsAuto() {
        return isAuto;
    }

    public void setIsAuto(int isAuto) {
        this.isAuto = isAuto;
    }

}
