/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.nature.log;

import com.game.nature.log.NatureBaseUpLog;
import game.core.dblog.base.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 翅膀幻化日志
 * @author Administrator
 */
public class NatureUpLog extends NatureBaseUpLog {
    private static final Logger logger = LogManager.getLogger("NatureUpLog");
    /**
     * 当前经验
     * */
    private int exp;

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Log(logField = "exp", fieldType = "int", index = "0")
    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
    
}
