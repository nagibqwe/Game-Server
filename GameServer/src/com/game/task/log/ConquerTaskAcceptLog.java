/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.task.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 讨伐任务接受日志
 *
 */
public class ConquerTaskAcceptLog extends TaskLogBean {

    private static final Logger logger = LogManager.getLogger("ConquerTaskAcceptLog");

    private int taskCount;	//当前是第几环

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }
    
     @Log(logField = "taskCount", fieldType = "int", index = "0")
    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }
}
