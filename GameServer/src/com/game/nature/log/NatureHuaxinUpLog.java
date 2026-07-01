/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.nature.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 翅膀升星日志
 * @author Administrator
 */
public class NatureHuaxinUpLog extends CommonLogBean {
    
    private static final Logger logger = LogManager.getLogger("NatureHuaxinUpLog");
    /**
     * 玩家id
     * */
    private long playerId;
    /**
     * action id
     * */
    private long actionId;
    /**
     * 升级消耗的物品
     * */
    private String itemStr;
    /**
     * 化形模型的id
     * */
    private int huanxiId;
    /**
     * 模型当前的星数
     * */
    private int star;
    /**
     * 操作类型，激活还是升级
     * 0 激活 ， 1 升级 2 表示赠送
     * */
    private int type;
    
    @Override
    public TableCheckStepEnum getRollingStep() {
	return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
	logger.error(buildSql());
    }

    @Log(logField = "playerId", fieldType = "bigint", index = "0")
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Log(logField = "actionId", fieldType = "bigint", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }
   
    @Log(logField = "huanxiId", fieldType = "int", index = "2")
    public int getHuanxiId() {
	    return huanxiId;
    }

    public void setHuanxiId(int huanxiId) {
	    this.huanxiId = huanxiId;
    }

    @Log(logField = "star", fieldType = "int", index = "0")
    public int getStar() {
	    return star;
    }

    public void setStar(int star) {
	    this.star = star;
    }
    
    @Log(logField = "type", fieldType = "int", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(logField = "itemStr", fieldType = "varchar(255)", index = "0")
    public String getItemStr() {
        return itemStr;
    }

    public void setItemStr(String itemStr) {
        this.itemStr = itemStr;
    }
}
