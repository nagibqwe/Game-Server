package com.game.treasurehunt.struct;

/**
 * 寻宝记录
 * Created by 瞿冰冰
 * 2019/7/12
 */
public class TreasureHuntRecord {

    private int itemId;

    private int itemNum;

    private String playerName;

    private int bind;

    private int type;//1 表示道具 2 表示仙破

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
