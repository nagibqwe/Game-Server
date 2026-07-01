package com.game.boss.struct;


public class SuitGemBossInfo {

    /**
     * 玩家id
     */
    private long playerId;

    /**
     * 进入时间
     */
    private int enterTime;

    /**
     * 打怪累计的天罚值
     */
    private int scourge;

    /**
     * 在地图中时长累计的天罚值
     */
    private int stayScourge;

    /**
     * 是否已达最大值，等待退出
     */
    private boolean isEnd;

    public SuitGemBossInfo() {}

    public SuitGemBossInfo(long playerId, int enterTime) {
        this.playerId = playerId;
        this.enterTime = enterTime;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public int getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(int enterTime) {
        this.enterTime = enterTime;
    }

    public int getScourge() {
        return scourge;
    }

    public void setScourge(int scourge) {
        this.scourge = scourge;
    }

    public int getStayScourge() {
        return stayScourge;
    }

    public void setStayScourge(int stayScourge) {
        this.stayScourge = stayScourge;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

}
