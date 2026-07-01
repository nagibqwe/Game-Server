package com.game.boss.struct;

import com.game.backpack.structs.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * boss死亡信息
 * Created by admin on 2017/7/18.
 */
public class BossDieRecord {

    private long playerId;          //玩家名字
    private String mapName;         //地图名字
    private int xPos;		        //X坐标
    private int yPos;	        	//y坐标
    private long killedTime;		//击杀时间
    private String bossName;	    //boss名字
    private List<Item> reward = new ArrayList<>();          //获得的奖励

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public long getKilledTime() {
        return killedTime;
    }

    public void setKilledTime(long killedTime) {
        this.killedTime = killedTime;
    }

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public List<Item> getReward() {
        return reward;
    }

    public void setReward(List<Item> reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        return "BossDieRecord{" +
                "playerId=" + playerId +
                ", mapName='" + mapName + '\'' +
                ", xPos=" + xPos +
                ", yPos=" + yPos +
                ", killedTime=" + killedTime +
                ", bossName='" + bossName + '\'' +
                ", reward=" + reward +
                '}';
    }
}
