/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.task.log;

import game.core.dblog.TableCheckStepEnum;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author Administrator
 */
public class BranchTaskFinishLog extends TaskLogBean {

    private static final Logger logger = LogManager.getLogger(BranchTaskFinishLog.class);
    
    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }
}
