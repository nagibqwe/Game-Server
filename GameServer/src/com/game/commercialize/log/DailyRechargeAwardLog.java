package com.game.commercialize.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;

/**
 * 每日累充日志
 */
public class DailyRechargeAwardLog extends CommonLogBean {

    private int awardId; //奖励id

    private String award; //奖励

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

    @Log(logField = "awardId", fieldType = "int", index = "0")
    public int getAwardId() {
        return awardId;
    }

    public void setAwardId(int awardId) {
        this.awardId = awardId;
    }

    @Log(logField = "award", fieldType = "text", index = "0")
    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

}
