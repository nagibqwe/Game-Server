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
 * @author lanxiang@haowan123.com
 */
public class ChangeRoleNameLog extends BaseLogBean {

    private static final Logger logger = LogManager.getLogger("ChangeRoleNameLog");

    private long playerId;                  //玩家Id

    private long userId;                    //账号Id

    private int sid;                        //区服

    private String oldName;                 //改名前的角色名

    private String newName;                 //改名后的角色名

    private int modelId;//                  道具ID

    private String platformName;//平台名字

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.UNROLL;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Log(logField = "playerId", fieldType = "bigint", index = "2")
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Log(logField = "userId", fieldType = "bigint", index = "3")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Log(logField = "sid", fieldType = "int", index = "0")
    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    @Log(logField = "oldName", fieldType = "varchar(200)", index = "0")
    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    @Log(logField = "newName", fieldType = "varchar(200)", index = "0")
    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    @Log(logField = "modelId", fieldType = "int", index = "0")
    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    @Log(logField = "platformName", fieldType = "varchar(100)", index = "0")
    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

}
