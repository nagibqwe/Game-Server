package com.game.openserverac.structs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * V4返利
 * by:cxl
 */
public class VIP4Rebate {


    private int curState = 1;//当前阶段

    private List<Integer> rewardStates = new ArrayList<>();//当前领奖到那个阶段

    private HashMap<Integer,VIP4GrouUpData> vip4GrouUpDatas = new HashMap();//V4返利列表

    public int getCurState() {
        return curState;
    }

    public void setCurState(int curState) {
        this.curState = curState;
    }


    public List<Integer> getRewardStates() {
        return rewardStates;
    }

    public void setRewardStates(List<Integer> rewardStates) {
        this.rewardStates = rewardStates;
    }

    public HashMap<Integer, VIP4GrouUpData> getVip4GrouUpDatas() {
        return vip4GrouUpDatas;
    }

    public void setVip4GrouUpDatas(HashMap<Integer, VIP4GrouUpData> vip4GrouUpDatas) {
        this.vip4GrouUpDatas = vip4GrouUpDatas;
    }
}
