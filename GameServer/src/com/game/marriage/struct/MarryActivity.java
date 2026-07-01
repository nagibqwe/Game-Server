package com.game.marriage.struct;


import java.util.HashMap;

/**
 * @Desc TODO
 * @Date 2021/6/30 16:05
 * @Auth ZUncle
 */
public class MarryActivity {

    //完美情缘排名奖励领取状态
    long rankRewardState;


    public long getRankRewardState() {
        return rankRewardState;
    }

    public void setRankRewardState(long rankRewardState) {
        this.rankRewardState = rankRewardState;
    }

    public boolean checkRankRewardState(int id) {
        return ((1L << id) & rankRewardState) > 0;
    }

    public void signRankRewardState(int id, boolean sign) {
        if (sign) {
            rankRewardState |= (1L << id);
        } else {
            rankRewardState &= ~(1L << id);
        }
    }

    /**
     * 情缘任务集合
     */
    private HashMap<Integer, MarryActivityTask> marryActivityTaskMap = new HashMap<>();//任务进度

    public HashMap<Integer, MarryActivityTask> getMarryActivityTaskMap() {
        return marryActivityTaskMap;
    }

    public void setMarryActivityTaskMap(HashMap<Integer, MarryActivityTask> marryActivityTaskMap) {
        this.marryActivityTaskMap = marryActivityTaskMap;
    }
}
