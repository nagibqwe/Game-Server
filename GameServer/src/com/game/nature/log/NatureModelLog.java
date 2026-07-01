/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.nature.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;

/**
 * 造化系统中保存模型变更的日志
 */
public class NatureModelLog extends CommonLogBean{
    /**
     * NatureType
     * */
    private int type;
    /**
     * 旧模型id
     * */
    private int oldId;
    /**
     * 新模型id
     * */
    private int newId;
    /**
     * 操作类型 0：激活，1：切换
     * */
    private int actType;

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

    @Log(logField = "oldId", fieldType = "int", index = "0")
    public int getOldId() {
        return oldId;
    }

    public void setOldId(int oldId) {
        this.oldId = oldId;
    }

    @Log(logField = "newId", fieldType = "int", index = "0")
    public int getNewId() {
        return newId;
    }

    public void setNewId(int wingId) {
        this.newId = wingId;
    }

    @Log(logField = "actType", fieldType = "int", index = "0")
    public int getActType() {
        return actType;
    }

    public void setActType(int actType) {
        this.actType = actType;
    }

    @Log(logField = "type", fieldType = "int", index = "0")
    public int getType() {return type;}

    public void setType(int type) {this.type = type;}
}
