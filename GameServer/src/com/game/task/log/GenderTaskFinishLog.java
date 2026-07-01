/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.task.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author admin
 */
public class GenderTaskFinishLog extends BaseLogBean {

    private static final Logger logger = LogManager.getLogger("GenderTaskFinishLog");

    private long roleId;
    private long userId;
    private String roleName;
    private int serverId;
    private int level;  //玩家接取任务时的等级

    private int taskId; //转职任务Id
    private int count;  // 当前是第几环
    private String reward; // 奖励

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.YEAR;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Log(logField = "roleid", fieldType = "bigint", index = "0")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(logField = "userid", fieldType = "bigint", index = "0")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Log(logField = "rolename", fieldType = "varchar(100)", index = "0")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Log(logField = "serverid", fieldType = "int", index = "0")
    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    @Log(logField = "level", fieldType = "int", index = "0")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Log(logField = "taskId", fieldType = "int", index = "0")
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Log(logField = "taskCount", fieldType = "int", index = "0")
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Log(logField = "reward", fieldType = "varchar(100)", index = "0")
    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

}
