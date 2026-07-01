package com.game.welfare.struct;

import java.util.ArrayList;
import java.util.List;

public class DayCheckIn {
    // 总共签到了几天，含补签
    private int day;
    // Sign_rewardCumulative累积了几天
    private int signCumulativeDay;
    // 第几轮
    private int round;
    // 本轮第一次签到时间
    private long firstTime;
    // 本轮正常签到的列表
    private List<Integer> checkIns;
    // 本轮补签的列表
    private List<Integer> checkIn2s;
    // 本轮已经领取的配置ID
    private List<Integer> rewardCfgID;
    // 最后一次签到时间
    private long lastTime;

    /**
     * new
     *
     * @return
     */
    public static DayCheckIn newDayCheckIn() {
        DayCheckIn dayCheckIn = new DayCheckIn();
        dayCheckIn.day = 0;
        dayCheckIn.signCumulativeDay = 0;
        dayCheckIn.round = 1;
        dayCheckIn.checkIns = new ArrayList<>();
        dayCheckIn.checkIn2s = new ArrayList<>();
        dayCheckIn.rewardCfgID = new ArrayList<>();
        dayCheckIn.lastTime = 0;
        dayCheckIn.firstTime = 0;
        return dayCheckIn;
    }

    private DayCheckIn() {}

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getSignCumulativeDay() {
        return signCumulativeDay;
    }

    public void setSignCumulativeDay(int signCumulativeDay) {
        this.signCumulativeDay = signCumulativeDay;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public List<Integer> getCheckIns() {
        return checkIns;
    }

    public void setCheckIns(List<Integer> checkIns) {
        this.checkIns = checkIns;
    }

    public List<Integer> getCheckIn2s() {
        return checkIn2s;
    }

    public void setCheckIn2s(List<Integer> checkIn2s) {
        this.checkIn2s = checkIn2s;
    }

    public List<Integer> getRewardCfgID() {
        return rewardCfgID;
    }

    public void setRewardCfgID(List<Integer> rewardCfgID) {
        this.rewardCfgID = rewardCfgID;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public long getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(long firstTime) {
        this.firstTime = firstTime;
    }
}
