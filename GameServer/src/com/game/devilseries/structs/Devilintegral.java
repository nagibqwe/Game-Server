package com.game.devilseries.structs;

/**
 * 除魔团积分
 * Created by cxl on 2021/5/20.
 */
public class Devilintegral {

    private long roleId;

    private int integral;

    private int rank;

    private String name;

    private long integralTime;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIntegralTime() {
        return integralTime;
    }

    public void setIntegralTime(long integralTime) {
        this.integralTime = integralTime;
    }
}
