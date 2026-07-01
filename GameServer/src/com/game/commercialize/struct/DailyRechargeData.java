package com.game.commercialize.struct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cxl on 2020/9/4.
 */
public class DailyRechargeData {


    private long lastLoginTime = 0;//上次登录时间

    private long rechargeTotal = 0;

    private int consumeTotal = 0;

    private List<Integer> alreadyGetRechargeList = new ArrayList<>();

    private List<Integer> alreadyGetConsumeList  = new ArrayList<>();

    private int boxRewardCount = 0;//宝箱奖励领取次数

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
    public long getRechargeTotal() {
        return rechargeTotal;
    }

    public void setRechargeTotal(long rechargeTotal) {
        this.rechargeTotal = rechargeTotal;
    }

    public int getConsumeTotal() {
        return consumeTotal;
    }

    public void setConsumeTotal(int consumeTotal) {
        this.consumeTotal = consumeTotal;
    }

    public List<Integer> getAlreadyGetRechargeList() {
        return alreadyGetRechargeList;
    }

    public void setAlreadyGetRechargeList(List<Integer> alreadyGetRechargeList) {
        this.alreadyGetRechargeList = alreadyGetRechargeList;
    }

    public List<Integer> getAlreadyGetConsumeList() {
        return alreadyGetConsumeList;
    }

    public void setAlreadyGetConsumeList(List<Integer> alreadyGetConsumeList) {
        this.alreadyGetConsumeList = alreadyGetConsumeList;
    }

    public int getBoxRewardCount() {
        return boxRewardCount;
    }

    public void setBoxRewardCount(int boxRewardCount) {
        this.boxRewardCount = boxRewardCount;
    }
}
