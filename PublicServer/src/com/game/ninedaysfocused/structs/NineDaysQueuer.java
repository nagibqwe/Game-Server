package com.game.ninedaysfocused.structs;

/**
 * Created by 542 on 2019/7/23.
 */
public class NineDaysQueuer {

    private long roleID; //玩家ID
    private int degree;  //阶
    private long queuetime;   //加入时间


    public long getRoleID() {
        return roleID;
    }

    public void setRoleID(long roleID) {
        this.roleID = roleID;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public long getQueuetime() {
        return queuetime;
    }

    public void setQueuetime(long queuetime) {
        this.queuetime = queuetime;
    }
}
