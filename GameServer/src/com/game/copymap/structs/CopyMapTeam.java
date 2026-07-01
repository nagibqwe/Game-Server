package com.game.copymap.structs;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 副本队伍
 *
 * @author lw
 */

public class CopyMapTeam {

    /**
     * 组队ID值
     */
    private long teamId;

    /**
     * 准备的状态
     */
    private final ConcurrentHashMap<Long, Boolean> roleReady = new ConcurrentHashMap<>();
    /**
     * 准备的状态
     */
    private final ConcurrentHashMap<Long, Integer> roleState = new ConcurrentHashMap<>();
    /**
     * 战场副本ID
     */
    private long fightId;
    /**
     * 开始计时的时间
     */
    private long startTime;
    /**
     * 要进入的副本类型， 是进跨服 3，还是进本服 2, 其它为1
     */
    private int type = 1;
    /**
     * 副本配置ID
     */
    private int modelId = 0;
    /**
     * 创建者的ID
     */
    private long createRoleId;

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public ConcurrentHashMap<Long, Integer> getRoleState() {
        return roleState;
    }

    public ConcurrentHashMap<Long, Boolean> getRoleReady() {
        return roleReady;
    }

    public long getFightId() {
        return fightId;
    }

    public void setFightId(long fightId) {
        this.fightId = fightId;
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

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public boolean getReady(long roleId) {
        if (roleReady.containsKey(roleId)) {
            return roleReady.get(roleId);
        }
        return false;
    }

    public long getCreateRoleId() {
        return createRoleId;
    }

    public void setCreateRoleId(long createRoleId) {
        this.createRoleId = createRoleId;
    }

    @Override
    public String toString() {
        return "CopyMapTeam{" + "teamId=" + teamId + ", roleReady=" + roleReady + ", roleState=" + roleState + ", fightId=" + fightId + ", startTime=" + startTime + ", type=" + type + ", modelId=" + modelId + ", createRoleId=" + createRoleId + '}';
    }
    
}
