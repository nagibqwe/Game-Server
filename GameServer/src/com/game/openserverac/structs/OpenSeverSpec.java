package com.game.openserverac.structs;

import java.util.*;

public class OpenSeverSpec {

    /**
     * 前面4个活动状态
     */
    private HashMap<Integer, Boolean> isGetReward = new HashMap<>();

    /**
     * 红包列表
     */
    private List<Integer> redList = new ArrayList<>();

    /**
     * 是否领取红包
     */
    private boolean isGetRed;


    //====================下面是开服狂欢之幸运翻牌的数据
    //已获得奖励列表 格子id-奖励索引
    private HashMap<Integer, Integer> luckyRewardHis = new HashMap<>();

    //任务id-任务完成度分子
    private HashMap<Integer, Integer> luckyTask = new HashMap<>();

    //幸运值
    private int luckyValue = 0;

    //任务奖励领没领的位运算
    private int luckyTaskSta = 0;

    public HashMap<Integer, Boolean> getIsGetReward() {
        return isGetReward;
    }

    public void setIsGetReward(HashMap<Integer, Boolean> isGetReward) {
        this.isGetReward = isGetReward;
    }

    public List<Integer> getRedList() {
        return redList;
    }

    public void setRedList(List<Integer> redList) {
        this.redList = redList;
    }

    public boolean isGetRed() {
        return isGetRed;
    }

    public void setGetRed(boolean getRed) {
        isGetRed = getRed;
    }

    public HashMap<Integer, Integer> getLuckyRewardHis() {
        return luckyRewardHis;
    }

    public void setLuckyRewardHis(HashMap<Integer, Integer> luckyRewardHis) {
        this.luckyRewardHis = luckyRewardHis;
    }

    public HashMap<Integer, Integer> getLuckyTask() {
        return luckyTask;
    }

    public void setLuckyTask(HashMap<Integer, Integer> luckyTask) {
        this.luckyTask = luckyTask;
    }

    public int getLuckyValue() {
        return luckyValue;
    }

    public void setLuckyValue(int luckyValue) {
        this.luckyValue = luckyValue;
    }

    public int getLuckyTaskSta() {
        return luckyTaskSta;
    }

    public void setLuckyTaskSta(int luckyTaskSta) {
        this.luckyTaskSta = luckyTaskSta;
    }

    public void addLuckyValue(int add) {
        this.luckyValue += add;
    }

    @Override
    public String toString() {
        return "OpenSeverSpec{" +
                "luckyRewardHis=" + luckyRewardHis.toString() +
                ", luckyTask=" + luckyTask.toString() +
                ", luckyValue=" + luckyValue +
                ", luckyTaskSta=" + luckyTaskSta +
                '}';
    }
}
