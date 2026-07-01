package com.game.openserverac.structs;

import game.message.OpenServerAcMessage;

/**幸运翻牌中奖记录*/
public class LuckyCardRewardLog {
    private long time;
    private int id;
    private int num;
    private String playerName;//玩家名字

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public OpenServerAcMessage.luckyCardRecord bytesWriteToClient() {
        OpenServerAcMessage.luckyCardRecord.Builder pb = OpenServerAcMessage.luckyCardRecord.newBuilder();
        pb.setTime(time);
        pb.setItemId(id);
        pb.setNum(num);
        pb.setPlayerName(playerName);
        return pb.build();
    }
}
