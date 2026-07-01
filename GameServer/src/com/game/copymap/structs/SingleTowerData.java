package com.game.copymap.structs;

/**
 * @author gsj
 * @create 2020/5/14 17:59
 */
public class SingleTowerData {

    /**
     * 单人塔挑战层数
     */
    private int curLayer = 1;
    /**
     * 单人塔通关状态
     */
    private int singleTowerState = 0;

    /**
     * 是否已领取普通奖励
     */
    private boolean hasReward = false;

    public int getCurLayer() {
        return curLayer;
    }

    public void setCurLayer(int curLayer) {
        this.curLayer = curLayer;
    }

    public int getSingleTowerState() {
        return singleTowerState;
    }

    public void setSingleTowerState(int singleTowerState) {
        this.singleTowerState = singleTowerState;
    }

    public boolean isHasReward() {
        return hasReward;
    }

    public void setHasReward(boolean hasReward) {
        this.hasReward = hasReward;
    }
}
