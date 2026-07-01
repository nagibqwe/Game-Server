package com.game.cangbaoge.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * 大奖数据
 * Created by cxl on 2020/9/2.
 */
public class SuperrewardData {

    private int round = 1;//轮数

    private int lotteryTimes = 0 ;//抽奖次数

    private List<Integer> alreadyGetID = new ArrayList();//已经领取的ID


    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getLotteryTimes() {
        return lotteryTimes;
    }

    public void setLotteryTimes(int lotteryTimes) {
        this.lotteryTimes = lotteryTimes;
    }

    public List<Integer> getAlreadyGetID() {
        return alreadyGetID;
    }

    public void setAlreadyGetID(List<Integer> alreadyGetID) {
        this.alreadyGetID = alreadyGetID;
    }
}
