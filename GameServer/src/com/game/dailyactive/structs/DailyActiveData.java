package com.game.dailyactive.structs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DailyActiveData {

    /**
     * 日常活动项,完成次数
     */
    private ConcurrentHashMap<Integer, Integer> dailyProgress = new ConcurrentHashMap<>();

    /**
     * 日常活动项，购买次数
     */
    private ConcurrentHashMap<Integer, Integer> dailyBuyCount = new ConcurrentHashMap<>();

    /**
     * 使用道具增加的次数
     */
    private ConcurrentHashMap<Integer, Integer> itemAddCount = new ConcurrentHashMap<>();

    /**
     * 领取的活跃度奖励id列表
     */
    private List<Integer> acRewardList = new ArrayList<>();

    /**
     * 关注列表
     */
    private List<Integer> focusList = new ArrayList<>();

    /**
     * 增加活跃值上限次数
     */
    private int addMaxValueCount;

    /**
     * 每日活跃值
     */
    private int activeNum;

    public ConcurrentHashMap<Integer, Integer> getDailyProgress() {
        return dailyProgress;
    }

    public void setDailyProgress(ConcurrentHashMap<Integer, Integer> dailyProgress) {
        this.dailyProgress = dailyProgress;
    }

    public ConcurrentHashMap<Integer, Integer> getDailyBuyCount() {
        return dailyBuyCount;
    }

    public void setDailyBuyCount(ConcurrentHashMap<Integer, Integer> dailyBuyCount) {
        this.dailyBuyCount = dailyBuyCount;
    }

    public ConcurrentHashMap<Integer, Integer> getItemAddCount() {
        return itemAddCount;
    }

    public void setItemAddCount(ConcurrentHashMap<Integer, Integer> itemAddCount) {
        this.itemAddCount = itemAddCount;
    }

    public List<Integer> getAcRewardList() {
        return acRewardList;
    }

    public void setAcRewardList(List<Integer> acRewardList) {
        this.acRewardList = acRewardList;
    }

    public List<Integer> getFocusList() {
        return focusList;
    }

    public void setFocusList(List<Integer> focusList) {
        this.focusList = focusList;
    }

    public int getAddMaxValueCount() {
        return addMaxValueCount;
    }

    public void setAddMaxValueCount(int addMaxValueCount) {
        this.addMaxValueCount = addMaxValueCount;
    }

    public int getActiveNum() {
        return activeNum;
    }

    public void setActiveNum(int activeNum) {
        this.activeNum = activeNum;
    }

}
