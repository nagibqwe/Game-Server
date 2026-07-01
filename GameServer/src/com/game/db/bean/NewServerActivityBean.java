/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.bean;

import game.core.db.BaseBean;

/**
 * @author Administrator
 */
public class NewServerActivityBean extends BaseBean {

    private long roleId;//角色ID

    private String activityData;//七天开服活动数据

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getActivityData() {
        return activityData;
    }

    public void setActivityData(String activityData) {
        this.activityData = activityData;
    }
}
