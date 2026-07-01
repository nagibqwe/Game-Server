package com.game.treasurehuntwuyou.struct;

import com.game.backpack.structs.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * 无忧宝库数据
 * @Auther: gouzhongliang
 * @Date: 2021/8/9 11:25
 */
public class TreasureHuntWuyouData {
    /**最近领取日期*/
    private String awardDate;
    /**已抽奖次数*/
    private int awardCount;
    /**最近一次中大奖的序号*/
    private int alreadyIndex;

    public String getAwardDate() {
        return awardDate;
    }

    public void setAwardDate(String awardDate) {
        this.awardDate = awardDate;
    }

    public int getAwardCount() {
        return awardCount;
    }

    public void setAwardCount(int awardCount) {
        this.awardCount = awardCount;
    }

    public int getAlreadyIndex() {
        return alreadyIndex;
    }

    public void setAlreadyIndex(int alreadyIndex) {
        this.alreadyIndex = alreadyIndex;
    }
}
