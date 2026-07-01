package com.game.commercialize.struct;

/**
 * 开服活动奖励类
 *
 * @author Administrator
 */
public class DailyRechargeReward {
    /**
     * 奖励ID，配置ID
     */
    private int rewardId;

    /**
     * 是否已经领奖
     */
    private boolean isGetReward;

    /**
     * 类型（1每日，2累积）
     */
    private int type;

    /**
     * 当类型为累积时，已经累积的天数
     */
    private int day;

    /**
     * 达成该项，需要的天数
     */
    private int needDay;

    private DailyRechargeReward(){}

    public DailyRechargeReward(int rewardId, int type, int needDay) {
        this.rewardId = rewardId;
        this.type = type;
        this.needDay = needDay;
        this.day = 0;
        this.isGetReward = false;
    }

    public int getRewardId() {
        return rewardId;
    }

    public void setRewardId(int rewardId) {
        this.rewardId = rewardId;
    }

    /**
     * 获取状态 1:未达成 2:可领取 3:已领完
     * @return
     */
    public int getState() {
        if (isGetReward())
            return DailyRechargeDefine.END_RECEIVE;
        return day < needDay ? DailyRechargeDefine.DISSATISFY : DailyRechargeDefine.CAN_RECEIVE;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void addDay() {
        if (day >= needDay)
            return;

        this.day += 1;
    }

    public int getNeedDay() {
        return needDay;
    }

    public void setNeedDay(int needDay) {
        this.needDay = needDay;
    }

    public boolean isGetReward() {
        return isGetReward;
    }

    public void setGetReward(boolean getReward) {
        isGetReward = getReward;
    }
}
