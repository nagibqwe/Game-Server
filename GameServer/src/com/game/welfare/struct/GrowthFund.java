package com.game.welfare.struct;

import java.util.ArrayList;
import java.util.List;

public class GrowthFund {
    private boolean isBuy;
    private int gear;
    private List<Integer> rewardCfgID;
    private List<Integer> rewardCfgIDServer;

    /**
     * 分配一个成长基金结构
     * @return
     */
    public static GrowthFund newGrowthFund() {
        GrowthFund fund = new GrowthFund();
        fund.setBuy(false);
        fund.setGear(0);
        fund.setRewardCfgID(new ArrayList<>());
        fund.setRewardCfgIDServer(new ArrayList<>());
        return fund;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public int getGear() {
        return gear;
    }

    public void setGear(int gear) {
        this.gear = gear;
    }

    public List<Integer> getRewardCfgID() {
        return rewardCfgID;
    }

    public void setRewardCfgID(List<Integer> rewardCfgID) {
        this.rewardCfgID = rewardCfgID;
    }

    public List<Integer> getRewardCfgIDServer() {
        return rewardCfgIDServer;
    }

    public void setRewardCfgIDServer(List<Integer> rewardCfgIDServer) {
        this.rewardCfgIDServer = rewardCfgIDServer;
    }

    @Override
    public String toString() {
        return "GrowthFund{" +
                "isBuy=" + isBuy +
                ", gear=" + gear +
                ", rewardCfgID=" + rewardCfgID +
                ", rewardCfgIDServer=" + rewardCfgIDServer +
                '}';
    }
}
