package com.game.command.structs;

import java.util.ArrayList;
import java.util.List;

public class CommandData {
    /**
     * 指挥官角色ID
     */
    private long commanderId;
    /**
     * 集火目标ID
     */
    private long targetId;
    /**
     * 参与跟随的角色ID
     */
    private List<Long> roleList = new ArrayList<>();

    private int buffId;

    /**
     * 最后弹幕发送时间
     */
    private long bulletScreenTime;

    public long getCommanderId() {
        return commanderId;
    }

    public void setCommanderId(long commanderId) {
        this.commanderId = commanderId;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public List<Long> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Long> roleList) {
        this.roleList = roleList;
    }

    public int getBuffId() {
        return buffId;
    }

    public void setBuffId(int buffId) {
        this.buffId = buffId;
    }

    public long getBulletScreenTime() {
        return bulletScreenTime;
    }

    public void setBulletScreenTime(long bulletScreenTime) {
        this.bulletScreenTime = bulletScreenTime;
    }
}
