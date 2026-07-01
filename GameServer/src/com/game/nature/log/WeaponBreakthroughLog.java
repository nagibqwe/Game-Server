package com.game.nature.log;

import com.game.nature.log.NatureBaseUpLog;
import game.core.dblog.base.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeaponBreakthroughLog extends NatureBaseUpLog {
    private static final Logger logger = LogManager.getLogger("WeaponBreakthroughLog");
    /**
     * 突破或者附灵后的全部属性信息
     * */
    private String attribute;

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Log(logField = "attribute", fieldType = "varchar(255)", index = "0")
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
