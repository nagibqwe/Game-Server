package com.backend.struct;

import java.util.List;

public class BaoDiBean {

    private int index;
    private int baoDi_min_num;
    private int baoDi_max_num;
    private int baodiReward_num_show;
    List<ItemBean> baodiRewardlist;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getBaoDi_min_num() {
        return baoDi_min_num;
    }

    public void setBaoDi_min_num(int baoDi_min_num) {
        this.baoDi_min_num = baoDi_min_num;
    }

    public int getBaoDi_max_num() {
        return baoDi_max_num;
    }

    public void setBaoDi_max_num(int baoDi_max_num) {
        this.baoDi_max_num = baoDi_max_num;
    }

    public int getBaodiReward_num_show() {
        return baodiReward_num_show;
    }

    public void setBaodiReward_num_show(int baodiReward_num_show) {
        this.baodiReward_num_show = baodiReward_num_show;
    }

    public List<ItemBean> getBaodiRewardlist() {
        return baodiRewardlist;
    }

    public void setBaodiRewardlist(List<ItemBean> baodiRewardlist) {
        this.baodiRewardlist = baodiRewardlist;
    }
}
