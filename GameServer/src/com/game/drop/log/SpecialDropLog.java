package com.game.drop.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 特殊掉落日志
 */
public class SpecialDropLog extends CommonLogBean {
    private static final Logger logger = LogManager.getLogger("SpecialDropLog");

    private int rankDropCount;  //排名掉落次数
    private int bossId;         //击杀boss的配置表id
    private int randomCount;    //玩家随机排名奖励获得额外奖励次数
    private int hasRankCount;   //已经获得该boss排名奖励的次数

    @Log(logField = "rankDropCount", fieldType = "int", index = "0")
    public int getRankDropCount() {
        return rankDropCount;
    }

    public void setRankDropCount(int rankDropCount) {
        this.rankDropCount = rankDropCount;
    }

    @Log(logField = "randomCount", fieldType = "int", index = "0")
    public int getRandomCount() {
        return randomCount;
    }

    public void setRandomCount(int randomCount) {
        this.randomCount = randomCount;
    }

    @Log(logField = "bossId", fieldType = "int", index = "0")
    public int getBossId() {
        return bossId;
    }

    public void setBossId(int bossId) {
        this.bossId = bossId;
    }

    @Log(logField = "hasRankCount", fieldType = "int", index = "0")
    public int getHasRankCount() {
        return hasRankCount;
    }

    public void setHasRankCount(int hasRankCount) {
        this.hasRankCount = hasRankCount;
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
