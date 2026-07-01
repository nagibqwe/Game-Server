/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.dailyactive.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 领取活跃度奖励
 */
public class ReceiveActiveRewardLog extends CommonLogBean {

    private static final Logger logger = LogManager.getLogger("ReceiveActiveRewardLog");

    private int cfgId;                  //领取的奖励id
    private int dailyActiveValue;       //玩家领取时当前的活跃值
    private String activeReward;        //获得的奖励
    private long actionId;              //日志关联ID

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Log(logField = "cfgId", fieldType = "int", index = "0")
    public int getCfgId() {
        return cfgId;
    }

    public void setCfgId(int cfgId) {
        this.cfgId = cfgId;
    }

    @Log(logField = "dailyActiveValue", fieldType = "int", index = "0")
    public int getDailyActiveValue() {
        return dailyActiveValue;
    }

    public void setDailyActiveValue(int dailyActiveValue) {
        this.dailyActiveValue = dailyActiveValue;
    }

    @Log(logField = "activeReward", fieldType = "varchar(100)", index = "0")
    public String getActiveReward() {
        return activeReward;
    }

    public void setActiveReward(String activeReward) {
        this.activeReward = activeReward;
    }

    @Log(fieldType = "bigint", logField = "actionId", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

}
