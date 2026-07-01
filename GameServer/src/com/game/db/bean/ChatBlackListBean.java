package com.game.db.bean;

import game.core.db.BaseBean;

/**
 * 标记用户黑名单
 */
public class ChatBlackListBean extends BaseBean {

    private int serverId;
    private long userId;
//    private String createTime;
//    private long endTime;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

//    public String getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(String createTime) {
//        this.createTime = createTime;
//    }
//
//    public long getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(long endTime) {
//        this.endTime = endTime;
//    }
}
