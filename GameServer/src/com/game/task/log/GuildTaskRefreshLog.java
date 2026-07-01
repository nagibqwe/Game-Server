/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.task.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 仙盟任务池刷新日志
 */
public class GuildTaskRefreshLog extends CommonLogBean {

    private static final Logger logger = LogManager.getLogger("GuildTaskRefreshLog");

    private int freshCount;	//当日刷新次数

    private int rTaskId;	//刷新掉的任务ID

    private String taskPool; //刷新后的任务池信息

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }
    
    @Log(logField = "freshCount", fieldType = "int", index = "0")
    public int getFreshCount() {
        return freshCount;
    }

    public void setFreshCount(int freshCount) {
        this.freshCount = freshCount;
    }

    @Log(logField = "rTaskId", fieldType = "int", index = "0")
    public int getrTaskId() {
        return rTaskId;
    }

    public void setrTaskId(int rTaskId) {
        this.rTaskId = rTaskId;
    }

    @Log(logField = "taskPool", fieldType = "varchar(128)", index = "0")
    public String getTaskPool() {
        return taskPool;
    }

    public void setTaskPool(String taskPool) {
        this.taskPool = taskPool;
    }
}
