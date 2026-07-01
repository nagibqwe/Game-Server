/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.task.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

public class DailyTaskAcceptLog extends TaskLogBean {

    private static final Logger logger = LogManager.getLogger("DailyTaskAcceptLog");
    
    private int taskCount;	//当前是第几环
    private int star;         //日常任务星级
    
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
    
    @Log(logField = "star", fieldType = "int", index = "0")
    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}
