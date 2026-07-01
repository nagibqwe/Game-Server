package com.game.welfare.struct;

import game.core.util.TimeUtils;

public class ExclusiveCard {
    // 卡片ID
    private int id;
    // 到期时间
    private long endTime;
    // 最后一次领取奖励时间
    private long lastRewardTime;

    private ExclusiveCard() {}

    public ExclusiveCard(int id) {
        this.id = id;
        this.endTime = 0;
        this.lastRewardTime = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getLastRewardTime() {
        return lastRewardTime;
    }

    public void setLastRewardTime(long lastRewardTime) {
        this.lastRewardTime = lastRewardTime;
    }

    public boolean isValid() {
        return endTime == -1 || TimeUtils.Time() < endTime;
    }

    @Override
    public String toString() {
        return "ExclusiveCard{" +
                "id=" + id +
                ", endTime=" + endTime +
                ", lastRewardTime=" + lastRewardTime +
                '}';
    }
}
