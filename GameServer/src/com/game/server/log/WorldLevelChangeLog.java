package com.game.server.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 世界等级变化日志
 */
public class WorldLevelChangeLog extends BaseLogBean {

    private static final Logger log = LogManager.getLogger("WorldLevelChangeLog");

    private int oldLevel;
    private int newLevel;
    private int totalLevel;
    private int peoples;

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.UNROLL;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

    @Log(fieldType = "int", index = "0", logField = "oldLevel")
    public int getOldLevel() {
        return oldLevel;
    }

    public void setOldLevel(int oldLevel) {
        this.oldLevel = oldLevel;
    }

    @Log(fieldType = "int", index = "0", logField = "newLevel")
    public int getNewLevel() {
        return newLevel;
    }

    public void setNewLevel(int newLevel) {
        this.newLevel = newLevel;
    }

    @Log(fieldType = "int", index = "0", logField = "totalLevel")
    public int getTotalLevel() {
        return totalLevel;
    }

    public void setTotalLevel(int totalLevel) {
        this.totalLevel = totalLevel;
    }

    @Log(fieldType = "int", index = "0", logField = "peoples")
    public int getPeoples() {
        return peoples;
    }

    public void setPeoples(int peoples) {
        this.peoples = peoples;
    }

}
