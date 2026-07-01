/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.bean;

/**
 * 活动的排名
 * @author zhaibiao
 */
public class ActivityRankBean {
    private long id;//活动Id
    private int type ;//排行类型
    private long roleId;//玩家角色
    private int funtionV ;//对应条件限制
    private int rankDate;//排行数值
    private String name;
    private int serverId;
    private String plat;
    private int serial;//和跨服对应的编号
    private boolean send;//是否发送了

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    
    
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public int getFuntionV() {
        return funtionV;
    }

    public void setFuntionV(int funtionV) {
        this.funtionV = funtionV;
    }

    public int getRankDate() {
        return rankDate;
    }

    public void setRankDate(int rankDate) {
        this.rankDate = rankDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }


    
    
    
    
}
