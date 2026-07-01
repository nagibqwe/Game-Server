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
public class GameMaster extends BaseBean {

    private long userId;
    private int sid;
    private int gmLevel;
    private long addTime;
    private int isDeleted;

    public GameMaster() {
        super();
    }

    public GameMaster(long userId, int sid, int gmLevel, long addTime, int isDeleted) {
        super();
        this.userId = userId;
        this.sid = sid;
        this.gmLevel = gmLevel;
        this.addTime = addTime;
        this.isDeleted = isDeleted;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getGmLevel() {
        return gmLevel;
    }

    public void setGmLevel(int gmLevel) {
        this.gmLevel = gmLevel;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

}
