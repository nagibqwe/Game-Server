package com.game.bravepeak.struct;

import com.game.map.structs.MapObject;
import game.core.map.Position;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 勇者巅峰地图数据
 */
public class BravePeakMapInfo {
    private ConcurrentHashMap<Long, Long> playerScore = new ConcurrentHashMap<>();//积分
    private ConcurrentHashMap<Long, Position> playerNewPos = new ConcurrentHashMap<>();//位置
    private ConcurrentHashMap<Long, List<Integer>> playerGetReward = new ConcurrentHashMap<>();//获取过的奖励
    private ConcurrentHashMap<Long, Integer> playerFloor = new ConcurrentHashMap<>();//玩家通关层数
    private int posIndex = 0;
    private int floor;//层数
    private boolean over;//是否完成所有层数

    private long activeEndTime = 0;

    //*****************************构造函数****************************************

    public BravePeakMapInfo() {
        this.floor = BravePeakDefine.BRAVE_PEAK_COPY_ID_FIRST;
    }

    //********************************方法*********************************************

    public void clean() {
        this.playerScore.clear();
        this.floor = BravePeakDefine.BRAVE_PEAK_COPY_ID_FIRST;
        this.over = false;
    }

    //***************************getter and setter**********************************************

    /**
     * 获取 积分
     *
     * @return playerScore 积分
     */
    public ConcurrentHashMap<Long, Long> getPlayerScore() {
        return this.playerScore;
    }

    public ConcurrentHashMap<Long, Position> getPlayerNewPos(){
        return this.playerNewPos;
    }

    public ConcurrentHashMap<Long, List<Integer>> getPlayerGetReward(){
        return this.playerGetReward;
    }

    public ConcurrentHashMap<Long, Integer> getPlayerFloor() {
        return playerFloor;
    }

    public Position getPosIndex(MapObject map){

        posIndex  = posIndex >=map.getRelives().size()?0:posIndex;
        Position position =  map.getRelives().get(posIndex);
        posIndex++;
        return position;
    }

    /**
     * 设置 积分
     *
     * @param playerScore 积分
     */
    public void setPlayerScore(ConcurrentHashMap<Long, Long> playerScore) {
        this.playerScore = playerScore;
    }

    /**
     * 获取 层数
     *
     * @return floor 层数
     */
    public int getFloor() {
        return this.floor;
    }

    /**
     * 设置 层数
     *
     * @param floor 层数
     */
    public void setFloor(int floor) {
        this.floor = floor;
    }

    /**
     * 获取 是否完成所有层数
     *
     * @return over 是否完成所有层数
     */
    public boolean isOver() {
        return this.over;
    }

    /**
     * 设置 是否完成所有层数
     *
     * @param over 是否完成所有层数
     */
    public void setOver(boolean over) {
        this.over = over;
    }


    public void setActiveEndTime(long activeEndTime){this.activeEndTime = activeEndTime;}

    public long getActiveEndTime(){return activeEndTime;}

    public void clear(){
        playerScore.clear();
        playerNewPos.clear();
        playerGetReward.clear();
        activeEndTime = 0;
        playerFloor.clear();
        posIndex = 0;
    }
}
