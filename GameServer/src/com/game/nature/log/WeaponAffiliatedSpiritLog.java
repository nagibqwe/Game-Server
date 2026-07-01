package com.game.nature.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeaponAffiliatedSpiritLog extends CommonLogBean {
    private static final Logger logger = LogManager.getLogger("WeaponAffiliatedSpiritLog");
    /**
     * 玩家id
     * */
    private long playerId;
    /**
     * action id
     * */
    private long actionId;
    /**
     * 附灵改变的属性，只记录改变的
     * */
    private String attribute;
    /**
     *
     * */

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

    @Log(logField = "attribute", fieldType = "varchar(255)", index = "0")
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

}
