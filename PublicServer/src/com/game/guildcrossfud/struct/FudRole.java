package com.game.guildcrossfud.struct;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/2/2 17:41
 * @Auth ZUncle
 */
public class FudRole {

    @JsonIgnore
    transient long roleId;        //玩家ID
    @JsonIgnore
    transient String name;        //玩家名字
    @JsonIgnore
    transient String platform;    //平台
    @JsonIgnore
    transient int serverId;       //服务器ID
    @JsonIgnore
    transient int tValue;         //天禁值
    @JsonIgnore
    transient int score;          //个人积分
    @JsonIgnore
    transient int kill;           //击杀
    @JsonIgnore
    transient int lock;           //是否解锁
    @JsonIgnore
    transient long scoreReward;   //积分奖励领取状态
    @JsonIgnore
    transient long time;          //更新时间

    HashMap<Integer, HashSet<Integer>> follow = new HashMap<>();   //自动关注列表

    @JsonIgnore
    transient String data;

    public boolean checkRewardState(int boxId) {
        return ((1L << boxId) & scoreReward) > 0;
    }

    public void signRewardState(int boxId, boolean sign) {
        if (sign) {
            scoreReward |= (1L << boxId);
        } else {
            scoreReward &= ~(1L << boxId);
        }
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int gettValue() {
        return tValue;
    }

    public void settValue(int tValue) {
        this.tValue = tValue;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getKill() {
        return kill;
    }

    public void setKill(int kill) {
        this.kill = kill;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public long getScoreReward() {
        return scoreReward;
    }

    public void setScoreReward(long scoreReward) {
        this.scoreReward = scoreReward;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public HashMap<Integer, HashSet<Integer>> getFollow() {
        return follow;
    }

    public void setFollow(HashMap<Integer, HashSet<Integer>> follow) {
        this.follow = follow;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FudRole{" +
                "roleId=" + roleId +
                ", name='" + name + '\'' +
                '}';
    }
}
