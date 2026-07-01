/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.bean;

/**
 *  服务器改名信息
 */
public class ServerNameBean {
    private int serverId;   //服务器编号
    private String changeName; //更改后的名字
    private int changeTime; //更改时间
    private long roleId;     //更改玩家

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getChangeName() {
        return changeName;
    }

    public void setChangeName(String changeName) {
        this.changeName = changeName;
    }

    public int getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(int changeTime) {
        this.changeTime = changeTime;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "ServerNameBean{" + "ServerId=" + serverId + ", changeName=" + changeName +
                ", changeTime=" + changeTime + ", roleId=" + roleId + '}';
    }

}
