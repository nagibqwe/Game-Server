/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.nature.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *  翅膀技能升级日志
 * @author Administrator
 */
public class NatrueDrugLog extends BaseLogBean {
    
    private static final Logger logger = LogManager.getLogger("NatrueDrugLog");
    /**
     * 玩家id
     * */
    private long playerId;
    /**
     * action id
     * */
    private long actionId;
    /**
     * drug信息，由drug.toString生成
     * */
    private String info;

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

    @Log(logField = "info", fieldType = "varchar(255)", index = "0")
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    
}
