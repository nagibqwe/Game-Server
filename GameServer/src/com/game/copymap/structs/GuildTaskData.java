package com.game.copymap.structs;

import java.util.HashSet;
import java.util.Set;

public class GuildTaskData {

    /**
     * 队长角色ID
     */
    private long leaderId;

    /**
     * 队长角色名
     */
    private String leaderName = "";

    /**
     * 要完成的任务ID
     */
    private int taskId;

    /**
     * 是否有协助
     */
    private boolean isHelp;

    /**
     * 求助信息唯一ID
     */
    private int helpId;

    /**
     * 怪物波数
     */
    private int stage;

    /**
     * 剩余boss数量
     */
    private int remainBossNum;

    /**
     * 副本开始时间（秒）
     */
    private int startTime;

    /**
     * 副本结束时间（秒）
     */
    private int endTime;

    /**
     * 副本参与角色ID
     */
    private Set<Long> playerIds = new HashSet<>();

    public long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(long leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public boolean isHelp() {
        return isHelp;
    }

    public void setHelp(boolean help) {
        isHelp = help;
    }

    public int getHelpId() {
        return helpId;
    }

    public void setHelpId(int helpId) {
        this.helpId = helpId;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getRemainBossNum() {
        return remainBossNum;
    }

    public void setRemainBossNum(int remainBossNum) {
        this.remainBossNum = remainBossNum;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public Set<Long> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(Set<Long> playerIds) {
        this.playerIds = playerIds;
    }
}
