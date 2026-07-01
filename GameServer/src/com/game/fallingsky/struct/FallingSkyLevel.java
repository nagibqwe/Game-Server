package com.game.fallingsky.struct;

/**
 * 天禁令等级数据结构
 * Created by cxl on 2020/11/6.
 */
public class FallingSkyLevel {

    private int levelID;

    private boolean isGetFree = false;//是否领取免费奖励

    private boolean isGetPay = false;//是否领取付费奖励


    public int getLevelID() {
        return levelID;
    }

    public void setLevelID(int levelID) {
        this.levelID = levelID;
    }

    public boolean isGetFree() {
        return isGetFree;
    }

    public void setGetFree(boolean getFree) {
        isGetFree = getFree;
    }

    public boolean isGetPay() {
        return isGetPay;
    }

    public void setGetPay(boolean getPay) {
        isGetPay = getPay;
    }
}
