package com.game.guildcrossfud.struct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;

/**
 * @Desc TODO 混沌虚空
 * @Date 2021/11/16 15:00
 * @Auth ZUncle
 */
public class AlienCity {

    @JsonIgnore
    int cityId;
    @JsonIgnore
    transient long roomId;                                             //房间ID
    @JsonIgnore
    transient HashMap<Integer, FudBoss> bossHashMap = new HashMap<>(); //boss数据
    @JsonIgnore
    transient HashMap<Integer, Integer> canEnterServer = new HashMap<>();  //能进入得服务器列表

    int totalScore = 0;       //幻境总积分

    int extraServerId;      //虚空幻境进入服务器

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public HashMap<Integer, FudBoss> getBossHashMap() {
        return bossHashMap;
    }

    public void setBossHashMap(HashMap<Integer, FudBoss> bossHashMap) {
        this.bossHashMap = bossHashMap;
    }

    public HashMap<Integer, Integer> getCanEnterServer() {
        return canEnterServer;
    }

    public void setCanEnterServer(HashMap<Integer, Integer> canEnterServer) {
        this.canEnterServer = canEnterServer;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getExtraServerId() {
        return extraServerId;
    }

    public void setExtraServerId(int extraServerId) {
        this.extraServerId = extraServerId;
    }
}
