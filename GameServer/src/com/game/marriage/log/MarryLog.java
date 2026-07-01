package com.game.marriage.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 * @Description
 * @auther admin
 * @create 2020-06-04 20:29
 */
public class MarryLog extends BaseLogBean {

    private long marryId;       //婚姻关系

    private long playerId;

    private long partnerId;

    private int opt;  //1:结婚  2：预定婚宴 3:离婚

    @Log(fieldType = "bigint", logField = "marryId", index = "1")
    public long getMarryId() {
        return marryId;
    }

    public void setMarryId(long marryId) {
        this.marryId = marryId;
    }

    @Log(fieldType = "bigint", logField = "playerId", index = "2")
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Log(fieldType = "bigint", logField = "partnerId", index = "3")
    public long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }

    @Log(fieldType = "int", logField = "opt", index = "0")
    public int getOpt() {
        return opt;
    }

    public void setOpt(int opt) {
        this.opt = opt;
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
