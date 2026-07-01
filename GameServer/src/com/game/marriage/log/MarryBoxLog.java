package com.game.marriage.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 * @Desc TODO
 * @Date 2020/8/14 18:25
 * @Auth ZUncle
 */
public class MarryBoxLog extends BaseLogBean {

    long playerId;      //玩家ID
    long buyId;         //购买者ID
    long timeout;          //过期时间

    @Log(fieldType = "bigint", logField = "playerId", index = "1")
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Log(fieldType = "bigint", logField = "buyId", index = "2")
    public long getBuyId() {
        return buyId;
    }

    public void setBuyId(long buyId) {
        this.buyId = buyId;
    }

    @Log(fieldType = "bigint", logField = "timeout", index = "0")
    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * 日志多长时间建一次表
     *
     * @return
     */
    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.UNROLL;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }
}
