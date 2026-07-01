package com.game.ranklist.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 崇拜日志
 */
public class WorshipLog extends CommonLogBean {

    private static final Logger logger = LogManager.getLogger("WorshipLog");

    private long worshipPlayerId;           //被崇拜玩家Id
    private int worshipDay;                 //崇拜时间（1970至今的天数）
    private int todayWorshipNum;            //今日已崇拜次数

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

    @Log(logField = "worshipPlayerId", fieldType = "bigint", index = "3")
    public long getWorshipPlayerId() {
        return worshipPlayerId;
    }

    public void setWorshipPlayerId(long worshipPlayerId) {
        this.worshipPlayerId = worshipPlayerId;
    }

    @Log(logField = "worshipDay", fieldType = "int", index = "0")
    public int getWorshipDay() {
        return worshipDay;
    }

    public void setWorshipDay(int worshipDay) {
        this.worshipDay = worshipDay;
    }

    @Log(logField = "todayWorshipNum", fieldType = "int", index = "0")
    public int getTodayWorshipNum() {
        return todayWorshipNum;
    }

    public void setTodayWorshipNum(int todayWorshipNum) {
        this.todayWorshipNum = todayWorshipNum;
    }

}
