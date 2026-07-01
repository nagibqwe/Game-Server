package com.game.count.structs;

import game.core.util.TimeUtils;

import java.util.Calendar;
import java.util.HashMap;

/**
 * @Desc TODO
 * @Date 2020/11/17 9:49
 * @Auth ZUncle
 */
public class Count {

    /**
     * 计时类型
     */
    private CountReset type = CountReset.Day;
    /**
     * key值
     */
    private String key;
    /**
     * 计数数量
     */
    private long count;
    /**
     * 计数集合
     */
    private HashMap<Long, Long> note = new HashMap<>();

    /**
     * 上次计数时间
     */
    private long lastTime;

    /**
     * 每日刷新时间 小时
     */
    private int hour = 0;
    int minute;
    int second;

    public HashMap<Long, Long> getNote() {
        return note;
    }

    public void setNote(HashMap<Long, Long> note) {
        this.note = note;
    }

    public void setType(CountReset type) {
        this.type = type;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getKey() {
        return key;
    }

    public long getCount() {
        return count;
    }

    public CountReset getType() {
        return type;
    }

    public long getLastTime() {
        return lastTime;
    }

    public int getHour() {
        return hour;
    }

    /**
     * 计数器重置（通过时间判断）
     */
    public void reset() {
        if (type == CountReset.Forever) {
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
        if (lastTime > now) {
            lastTime = now;
        }
        if (lastTime < refresh && now > refresh) {
            count = 0;
            note.clear();
            lastTime = now;
        }
    }

    //默认检测
    private void defaultChecks(Calendar cal, int year, int month, int date, int curHour, int week) {

        switch (type) {
            //小时
            case Hour:
                cal.set(year, month, date, curHour, minute, second);
                break;
            //天
            case Day:
                cal.set(year, month, date, hour, minute, second);
                break;
            //周
            case Week:
                cal.set(year, month, date, hour, minute, second);
                cal.add(Calendar.DATE, week > 0 ? week - 7 : week);
                break;
            //月
            case Month:
                cal.set(year, month, 1, hour, minute, second);
                break;
            //年
            case Year:
                cal.set(year, 0, 1, hour, minute, second);
                break;
            //永久
            case Forever:
                cal.setTimeInMillis(0);
                break;
            default:
                break;
        }
    }
}
