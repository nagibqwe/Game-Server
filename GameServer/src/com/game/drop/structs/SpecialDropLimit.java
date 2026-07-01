package com.game.drop.structs;

import game.core.util.TimeUtils;

/**
 * 特殊掉落限制(针对活动)
 */
public class SpecialDropLimit {
    /**
     * 上次记录时间
     */
    private long time;
    /**
     * 排名奖励次数
     */
    private int rankDropCount;
    /**
     * 购买的排名次数
     */
    private int buyCount;

    private void updateData() {
        long now = TimeUtils.Time();
        if (!TimeUtils.isSameDay(time, now)) {
            time = now;
            rankDropCount = 0;
            buyCount = 0;
        }
    }

    /////////////////////////////getter and setter////////////////////////////////////////////////////////

    /**
     * 获取 上次记录时间
     *
     * @return time 上次记录时间
     */
    public long getTime() {
        return this.time;
    }

    /**
     * 设置 上次记录时间
     *
     * @param time 上次记录时间
     */
    public void setTime(long time) {
        this.time = time;
    }


    /**
     * 获取 排名奖励次数
     *
     * @return rankDropCount 排名奖励次数
     */
    public int getRankDropCount() {
        updateData();
        return this.rankDropCount;
    }

    /**
     * 设置 排名奖励次数
     *
     * @param rankDropCount 排名奖励次数
     */
    public void setRankDropCount(int rankDropCount) {
        this.rankDropCount = rankDropCount;
    }

    /**
     * 获取 购买的排名次数
     *
     * @return buyCount 购买的排名次数
     */
    public int getBuyCount() {
        updateData();
        return this.buyCount;
    }

    /**
     * 设置 购买的排名次数
     *
     * @param buyCount 购买的排名次数
     */
    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }
}
