package com.game.copymap.structs;

/**
 * @Desc TODO
 * @Date 2021/11/2 15:21
 * @Auth ZUncle
 */
public class StateBossCopyData extends ZoneCache{

    //是否刷新boss
    boolean refreshMonster;
    //副本结束时间
    long endTime;

    public boolean isRefreshMonster() {
        return refreshMonster;
    }

    public void setRefreshMonster(boolean refreshMonster) {
        this.refreshMonster = refreshMonster;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
