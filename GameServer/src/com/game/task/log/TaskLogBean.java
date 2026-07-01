/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.task.log;

import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 *
 * @author admin
 */
public abstract class TaskLogBean extends BaseLogBean {

    private long userId;                        //用户Id
    private long roleId;                        //角色Id
    private int modelId;                        //任务ModelId
    private String taskInfo;                    //任务信息
    private String roleName;                    //角色名字
    private String platform;                    //平台名字
    private int sid;                            //服务器ID

    @Log(logField = "userId", fieldType = "bigint", index = "2")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Log(logField = "roleId", fieldType = "bigint", index = "3")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(logField = "modelId", fieldType = "int", index = "0")
    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    @Log(logField = "taskInfo", fieldType = "varchar(1000)", index = "0")
    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    @Log(logField = "rolename", fieldType = "varchar(100)", index = "0")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Log(logField = "platformName", fieldType = "varchar(100)", index = "0")
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Log(logField = "sid", fieldType = "int", index = "0")
    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }
}
