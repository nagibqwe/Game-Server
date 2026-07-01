package com.game.guildactivity.struct;

import com.data.Global;
import com.game.structs.GlobalType;
import game.core.util.TimeUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 宗派活动数据
 */
public class GuildActivityInfo {
    /**
     * 纪录时间，用作数据刷新
     */
    private long time;

    /**
     * 当日历史最高积分
     */
    private int record;

    /**
     * 当日积分
     */
    private int nowScore;

    /**
     * 获取 当日历史最高积分
     *
     * @return record 当日历史最高积分
     */
    public int getRecord() {
        checkResetData();
        return this.record;
    }

    /**
     * 设置 当日历史最高积分
     *
     * @param record 当日历史最高积分
     */
    public void setRecord(int record) {
        this.record = record;
    }

    /**
     * 获取 当日积分
     *
     * @return nowScore 当日积分
     */
    public int getNowScore() {
        checkResetData();
        return this.nowScore;
    }

    /**
     * 设置 当日积分
     *
     * @param nowScore 当日积分
     */
    public void setNowScore(int nowScore) {
        this.nowScore = nowScore;
    }

    /**
     * 获取 纪录时间，用作数据刷新
     *
     * @return time 纪录时间，用作数据刷新
     */
    public long getTime() {
        return this.time;
    }

    /**
     * 设置 纪录时间，用作数据刷新
     *
     * @param time 纪录时间，用作数据刷新
     */
    public void setTime(long time) {
        this.time = time;
    }

    private void checkResetData() {
        long zeroOffset = Global.Daily_times_reset_time * GlobalType.MILLIS_PER_HOUR;
        if (!TimeUtils.isSameDay(time - zeroOffset, TimeUtils.Time() - zeroOffset)) {
            this.record = 0;
            this.nowScore = 0;
            this.time = TimeUtils.Time();
        }
    }
}
