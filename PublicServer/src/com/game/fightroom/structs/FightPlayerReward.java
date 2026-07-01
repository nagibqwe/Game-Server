/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.fightroom.structs;

/**
 *战场结束没有领奖的玩家的收集结构
 * @author soko <xuchangming@haowan123.com>
 */
public class FightPlayerReward {
    private long roleId;
    private String platsid;
    private long fightId;
    private int cloneModelid;
    private boolean success;
    private long score;
    private int time;
    private int sortIndex;//排名
    private int rewardTime;//发奖时间

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getPlatsid() {
        return platsid;
    }

    public void setPlatsid(String platsid) {
        this.platsid = platsid;
    }


    public long getFightId() {
        return fightId;
    }

    public void setFightId(long fightId) {
        this.fightId = fightId;
    }

    public int getCloneModelid() {
        return cloneModelid;
    }

    public void setCloneModelid(int cloneModelid) {
        this.cloneModelid = cloneModelid;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public int getRewardTime() {
        return rewardTime;
    }

    public void setRewardTime(int rewardTime) {
        this.rewardTime = rewardTime;
    }   
}
