package com.game.treasurehunt.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TreasureHuntLog extends CommonLogBean {
    private int treasureHutTimes;//寻宝次数
    private long actionId;//唯一id
    private String rewards;//寻宝获得的奖励
    private int type;//寻宝类型
    @Log(logField = "type", fieldType = "int", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(fieldType = "bigint", logField = "actionId", index = "0")
    public long getActionId() {
        return actionId;
    }
    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }
    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    @Log(logField = "treasurehuttimes", fieldType = "int", index = "0")
    public int getTreasureHutTimes() {
        return treasureHutTimes;
    }

    public void setTreasureHutTimes(int treasureHutTimes) {
        this.treasureHutTimes = treasureHutTimes;
    }

    @Log(fieldType = "TEXT", logField = "rewards", index = "0")
    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }
}
