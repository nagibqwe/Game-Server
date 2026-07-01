package com.game.player.structs;

public class CommunityLeaveMessageInfo {
    private long leaveMessageId;
    private long roleId;
    private String condition;
    private long time;


    public long getLeaveMessageId() {
        return leaveMessageId;
    }

    public void setLeaveMessageId(long leaveMessageId) {
        this.leaveMessageId = leaveMessageId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
