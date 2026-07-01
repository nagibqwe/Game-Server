package com.game.statevip.structs;

/**
 * @author admin
 */
public class StateVipGift {
    /**
     * 推送包ID
     */
    private int id;

    /**
     * 下次推送时间
     */
    private long nextPushTime;

    /**
     * 是否完成
     */
    private boolean isComplete;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getNextPushTime() {
        return nextPushTime;
    }

    public void setNextPushTime(long nextPushTime) {
        this.nextPushTime = nextPushTime;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
