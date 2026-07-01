package com.game.cangbaoge.struct;

/**
 * Created by cxl on 2020/9/2.
 */
public class CangbaogeRecord {

    private long time;//抽奖时间

    private String playerName;//玩家名字

    private int itemId;//道具ID

    private int num;//数量

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
