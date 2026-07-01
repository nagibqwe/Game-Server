package com.game.fallingsky.struct;

import java.util.HashMap;

/**
 * 天禁令
 * Created by cxl on 2020/11/6.
 */
public class FallingSkyData {


    private long loginTime = 0l; //登录时间

    private int round = 0;//当前轮

    private boolean isPay = false;//是否付费

    private HashMap<Integer,FallingSkyTask> fallingSkyTaskMap = new HashMap<>();//任务进度

    private HashMap<Integer,FallingSkyLevel> fallingSkyLevelMap = new HashMap<>();//等级进度

    public HashMap<Integer, FallingSkyTask> getFallingSkyTaskMap() {
        return fallingSkyTaskMap;
    }

    public void setFallingSkyTaskMap(HashMap<Integer, FallingSkyTask> fallingSkyTaskMap) {
        this.fallingSkyTaskMap = fallingSkyTaskMap;
    }

    public HashMap<Integer, FallingSkyLevel> getFallingSkyLevelMap() {
        return fallingSkyLevelMap;
    }

    public void setFallingSkyLevelMap(HashMap<Integer, FallingSkyLevel> fallingSkyLevelMap) {
        this.fallingSkyLevelMap = fallingSkyLevelMap;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
}
