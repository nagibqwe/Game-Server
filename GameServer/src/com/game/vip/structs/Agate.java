package com.game.vip.structs;

import game.core.util.TimeUtils;

/**
 * @author gsj
 * @create 2020/5/12 11:09
 */
public class Agate {

    /**
     * 今日可领取值
     */
    private int todayPoint;

    /**
     * 周卡奖池累计值
     */
    private int weekPoolPoint;

    /**
     * 月卡奖池累计值
     */
    private int monthPoolPoint;

    /**
     * 记录时间，用于跨天邮件补偿未领取值
     */
    private long recordTime;

    public int getTodayPoint() {
        return todayPoint;
    }

    public void setTodayPoint(int todayPoint) {
        this.todayPoint = todayPoint;
        this.recordTime = TimeUtils.Time();
    }

    public int getWeekPoolPoint() {
        return weekPoolPoint;
    }

    public void setWeekPoolPoint(int weekPoolPoint) {
        this.weekPoolPoint = weekPoolPoint;
    }

    public int getMonthPoolPoint() {
        return monthPoolPoint;
    }

    public void setMonthPoolPoint(int monthPoolPoint) {
        this.monthPoolPoint = monthPoolPoint;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }
}
