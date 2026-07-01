package com.game.worldbonfire.structs;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Description
 * @auther lw
 * @create 2019-10-21 15:54
 */
public class WorldBonfireMm {
    private long roleId;

    private int joinNum;

    private int winNum;

    private boolean isGet;

    private ChannelHandlerContext context;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public int getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(int joinNum) {
        this.joinNum = joinNum;
    }

    public int getWinNum() {
        return winNum;
    }

    public void setWinNum(int winNum) {
        this.winNum = winNum;
    }

    public boolean isGet() {
        return isGet;
    }

    public void setGet(boolean get) {
        isGet = get;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    public void setContext(ChannelHandlerContext context) {
        this.context = context;
    }

}
