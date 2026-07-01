package com.game.statevip.structs;

/**
 * @author admin
 */
public class StateVip {
    private int lv;

    private int giftLv;

    private long startTime;//经验池开始运行的时间

    private long exp;//经验池当前经验

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public int getGiftLv() {
        return giftLv;
    }

    public void setGiftLv(int giftLv) {
        this.giftLv = giftLv;
    }
}
