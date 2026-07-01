package com.game.shop.structs;

/**
 * Created by cxl on 2020/6/1.
 */
public class FreeShop {

    private int      id;

    private boolean  isGet = false;

    private long     buyTime  = 0L;

    private long     lastGetRewardTime = 0L;

    private boolean  isOverdue = false; //是否过期

    private int      rewardTimes; //领奖次数


    public int getRewardTimes() {
        return rewardTimes;
    }

    public void setRewardTimes(int rewardTimes) {
        this.rewardTimes = rewardTimes;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }

    public long getLastGetRewardTime() {
        return lastGetRewardTime;
    }

    public void setLastGetRewardTime(long lastGetRewardTime) {
        this.lastGetRewardTime = lastGetRewardTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isGet() {
        return isGet;
    }

    public void setGet(boolean get) {
        isGet = get;
    }

    public long getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(long buyTime) {
        this.buyTime = buyTime;
    }
}
