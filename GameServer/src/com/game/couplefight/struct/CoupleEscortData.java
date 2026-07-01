package com.game.couplefight.struct;

/**
 * 仙女护送数据
 */
public class CoupleEscortData {


    private long roleId;

    private long startTime;

    private int type;//护送类型


    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
