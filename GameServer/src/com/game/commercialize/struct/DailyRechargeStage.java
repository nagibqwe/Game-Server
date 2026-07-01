package com.game.commercialize.struct;

import java.util.List;

public class DailyRechargeStage {
    // 该项需要的元宝数量
    private int gold;

    private long lastModifyTime;

    // 详细奖励列表，单项奖励在第0个
    private List<DailyRechargeReward> rewardList;

    private DailyRechargeStage(){}

    public DailyRechargeStage(int gold, List<DailyRechargeReward> rewardList) {
        this.gold = gold;
        this.rewardList = rewardList;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public List<DailyRechargeReward> getRewardList() {
        return rewardList;
    }

    public void setRewardList(List<DailyRechargeReward> rewardList) {
        this.rewardList = rewardList;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}
