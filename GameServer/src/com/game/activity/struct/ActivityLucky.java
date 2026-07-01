package com.game.activity.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/4/9 15:37
 * @Auth ZUncle
 */
public class ActivityLucky {

    int luckyValue;                                         //幸运值
    List<RewardData> luckyAwardList = new ArrayList<>() ;   //幸运值奖励

    public int getLuckyValue() {
        return luckyValue;
    }

    public void setLuckyValue(int luckyValue) {
        this.luckyValue = luckyValue;
    }

    public List<RewardData> getLuckyAwardList() {
        return luckyAwardList;
    }

    public void setLuckyAwardList(List<RewardData> luckyAwardList) {
        this.luckyAwardList = luckyAwardList;
    }
}
