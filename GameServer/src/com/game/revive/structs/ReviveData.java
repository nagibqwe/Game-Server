package com.game.revive.structs;

/**
 * Created by cxl on 2020/3/17.
 */
public class ReviveData {



    /**
     *上一次的复活类型
     */
    private  int lastReviveType = 0;

    /**
     * 上一次死亡时间
     * @return
     */
    private  long lastDeadTime = 0;

    /**
     * 复活次数
     */
    private int reviveCount = 0;

    /**
     * 死亡复活后等待时间
     */
    public int waitTimeCD = 0;


    public int getLastReviveType() {
        return lastReviveType;
    }

    public void reset(){
        lastReviveType = 0;
        lastDeadTime   = 0;
        reviveCount    = 0;
        waitTimeCD = 0;
    }

    public void setLastReviveType(int lastReviveType) {
        this.lastReviveType = lastReviveType;
    }

    public long getLastDeadTime() {
        return lastDeadTime;
    }

    public void setLastDeadTime(long lastReveietTime) {
        this.lastDeadTime = lastDeadTime;
    }

    public int getReviveCount() {
        return reviveCount;
    }

    public void setReviveCount(int reviveCount) {
        this.reviveCount = reviveCount;
    }

    public int getWaitTimeCD() {
        return waitTimeCD;
    }

    public void setWaitTimeCD(int waitTimeCD) {
        this.waitTimeCD = waitTimeCD;
    }
}
