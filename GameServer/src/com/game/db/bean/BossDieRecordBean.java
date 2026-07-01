package com.game.db.bean;

import game.core.db.BaseBean;

/**
 * Created by zcd on 2018/4/19.
 */
public class BossDieRecordBean extends BaseBean {

    private long playerId;          //玩家名字
    private String mapName;         //地图名字
    private int xPos;		        //X坐标
    private int yPos;	        	//y坐标
    private long killedTime;		//击杀时间
    private String bossName;	    //boss名字
    private String reward;          //奖励

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

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
