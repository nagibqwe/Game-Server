package com.game.count.structs;

import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;

/**
 * @author admin
 */
public class Count {

    final static Logger logger = LogManager.getLogger(Count.class);

    public Count() {

    }

    public Count(RefreshType type, int count) {
        this.type = type;
        this.count = 0;
    }

    enum TypeCycle {
        Week(7 * 24 * 3600 * 1000L),
        Day(24 * 3600 * 1000L),
        ;
        final long time;

        TypeCycle(long time) {
            this.time = time;
        }

        public long getTime() {
            return time;
        }
    }

    /**
     * 刷新类型枚举
     */
    public enum RefreshType {
        CountType_Forever(4), //永久
        CountType_Year(3), //每年刷新
        CountType_Month(2), //每月刷新
        CountType_Week(1, 24), //每周刷新
        CountType_Day(0),  //每日凌晨
        CountType_Day_Five(0, 5),  //每日5点
        CountType_Hour(5),  //每小时刷新
        ;
        final int value;
        int hour;

        RefreshType(int value, int hour) {
            this.value = value;
            this.hour = hour;
        }

        RefreshType(int value) {
            this.value = value;
        }

        public int getHour() {
            return hour;
        }

        public int getValue() {
            return value;
        }

        public static RefreshType convert(int v) {
            if (v == CountType_Week.getValue()) {
                return CountType_Week;
            }
            for (RefreshType type : RefreshType.values()) {
                if (type.getValue() == v && type.getHour() == 0) {
                    return type;
                }
            }
            return null;
        }

        public static RefreshType convert(int v, int hour) {
            for (RefreshType type : RefreshType.values()) {
                if (type.getValue() == v && type.getHour() == hour) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * 计时类型
     */
    private RefreshType type = RefreshType.CountType_Day;
    /**
     * key值
     */
    private String key;
    /**
     * 计数数量
     */
    private long count;
    /**
     * 上次计数时间
     */
    private long lastTime;

    /**
     * 每日刷新时间 小时
     */
    private int hour;

    /**
     * 计时类型
     *
     * @return
     */
    public RefreshType getType() {
        return type;
    }

    /**
     * 计时类型
     *
     * @param type
     */
    public void setType(RefreshType type) {
        this.type = type;
    }

    /**
     * key值
     *
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * key值
     *
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 计数数量
     *
     * @return
     */
    public long getCount() {
        return count;
    }

    /**
     * 计数数量
     *
     * @param count
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * 上次计数时间
     *
     * @return
     */
    public long getLastTime() {
        return lastTime;
    }

    /**
     * 上次计数时间
     *
     * @param lastTime
     */
    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    /**
     * 计数器重置（通过时间判断）
     */
    public void reset() {
        if (type == RefreshType.CountType_Forever) {
            return;
        }

        long now = TimeUtils.Time();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(now);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        int curHour = cal.get(Calendar.HOUR_OF_DAY);
        int week = cal.getFirstDayOfWeek() - cal.get(Calendar.DAY_OF_WEEK);

        defaultChecks(cal, year, month, date, curHour, week);

        long refresh = cal.getTimeInMillis();

        //修正时间
        if (lastTime / 1000 > now / 1000) {
            lastTime = now;
        }
        //修正超过一轮周期的计数器
        if ((type == RefreshType.CountType_Day || type == RefreshType.CountType_Day_Five) && now - lastTime > TypeCycle.Day.getTime()) {
            count = 0;
            lastTime = now;
        }
        if (lastTime / 1000 < refresh / 1000 && refresh / 1000 < now / 1000) {
            count = 0;
            lastTime = now;
        }
    }

    //默认检测
    private void defaultChecks(Calendar cal, int year, int month, int date, int curHour, int week) {

        switch (type) {
            //小时
            case CountType_Hour:
                cal.set(year, month, date, curHour, 0, 0);
                break;
            //天
            case CountType_Day:
            case CountType_Day_Five:
                cal.set(year, month, date, hour == 0 ? type.getHour() : hour, 0, 0);
                break;
            //周
            case CountType_Week:
                cal.set(year, month, date, hour == 0 ? type.getHour() : hour, 0, 0);
                cal.add(Calendar.DATE, week > 0 ? week - 7 : week);
                break;
            //月
            case CountType_Month:
                cal.set(year, month, 1, 0, 0, 0);
                break;
            //年
            case CountType_Year:
                cal.set(year, 0, 1, 0, 0, 0);
                break;
            //永久
            case CountType_Forever:
                cal.setTimeInMillis(0);
                break;
            default:
                break;
        }
    }
}
