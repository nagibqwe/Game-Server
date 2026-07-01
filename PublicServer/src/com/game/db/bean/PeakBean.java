package com.game.db.bean;

/**
 * @Desc TODO
 * @Date 2020/11/16 17:14
 * @Auth ZUncle
 */
public class PeakBean {

    long roleId; //'玩家ID'
    String name; //'玩家名字',
    String platform; //平台
    int serverId; // '服务器ID',
    int score; // '积分',
    long time;  // '更新时间',
    long power;  //战力
    int rankId; //段位ID
    int times;  //本赛季参赛场次
    int dayTimes;   //当天参赛场次
    long timesReward;   //场次奖励领取状态
    long stageReward;   //段位奖励领取状态

    //////////////////////////////
    transient boolean fight;    //挑战状态
    transient int winLian;      //连胜
    transient int loserLian;    //连负
    transient long lastMatchId; //上一场匹配的玩家
    transient long delay;       //匹配延时


    public boolean checkStageRewardState(int stage) {
        return ((1L << stage) & stageReward) > 0;
    }

    public void signStageRewardState(int stage, boolean sign) {
        if (sign) {
            stageReward |= (1L << stage);
        } else {
            stageReward &= ~(1L << stage);
        }
    }

    public boolean checkTimesRewardState(int times) {
        return ((1L << times) & timesReward) > 0;
    }

    public void signTimesRewardState(int times, boolean sign) {
        if (sign) {
            timesReward |= (1L << times);
        } else {
            timesReward &= ~(1L << times);
        }
    }

    public int getDayTimes() {
        return dayTimes;
    }

    public void setDayTimes(int dayTimes) {
        this.dayTimes = dayTimes;
    }

    public long getTimesReward() {
        return timesReward;
    }

    public void setTimesReward(long timesReward) {
        this.timesReward = timesReward;
    }

    public long getStageReward() {
        return stageReward;
    }

    public void setStageReward(long stageReward) {
        this.stageReward = stageReward;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getPower() {
        return power;
    }

    public void setPower(long power) {
        this.power = power;
    }

    public int getRankId() {
        return rankId;
    }

    public void setRankId(int rankId) {
        this.rankId = rankId;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public boolean isFight() {
        return fight;
    }

    public void setFight(boolean fight) {
        this.fight = fight;
    }

    public int getWinLian() {
        return winLian;
    }

    public void setWinLian(int winLian) {
        this.winLian = winLian;
    }

    public int getLoserLian() {
        return loserLian;
    }

    public void setLoserLian(int loserLian) {
        this.loserLian = loserLian;
    }

    public long getLastMatchId() {
        return lastMatchId;
    }

    public void setLastMatchId(long lastMatchId) {
        this.lastMatchId = lastMatchId;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "PeakBean{" +
                "roleId=" + roleId +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", rankId=" + rankId +
                '}';
    }
}
