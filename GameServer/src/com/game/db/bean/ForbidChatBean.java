/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.bean;

import game.core.db.BaseBean;

/**
 * @author hewei
 */
public class ForbidChatBean extends BaseBean {

    private long userId;
    private int forbidType;//禁言类型1:工作室禁言2:全文替换禁言3:关键字替换禁言4:常规禁言5:隐形禁言6:隔离禁言
    private String createTime;
    private long endTime;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getForbidType() {
        return forbidType;
    }

    public void setForbidType(int forbidType) {
        this.forbidType = forbidType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

}
