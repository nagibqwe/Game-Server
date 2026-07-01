package com.game.kaoshangling.struct;

import java.util.ArrayList;
import java.util.List;

public class KaoShangLingBean {
    public int getRank() {
        return rank;
    }

    public int getIsBuySpecail() {
        return isBuySpecail;
    }

    public void setIsBuySpecail(int isBuySpecail) {
        this.isBuySpecail = isBuySpecail;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }


    public List<Integer> getCommonRewardList() {
        return commonRewardList;
    }

    public void setCommonRewardList(List<Integer> commonRewardList) {
        this.commonRewardList = commonRewardList;
    }

    public List<Integer> getSpecailRewardist() {
        return specailRewardist;
    }

    public void setSpecailRewardist(List<Integer> specailRewardist) {
        this.specailRewardist = specailRewardist;
    }

    private int rank = 1;//轮数

    private int isBuySpecail; //高级犒赏令 1为是


    private List<Integer> commonRewardList = new ArrayList();//已经领取普通列表
    private List<Integer> specailRewardist = new ArrayList();//已经领取高级列表

}