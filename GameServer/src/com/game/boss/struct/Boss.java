package com.game.boss.struct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.manager.Manager;
import game.core.map.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Boss {
    //怪物唯一Id
    private long id;
    //怪物Id
    private int modelId;
    //刷新地图id
    private int mapID;
    //是否已经刷新
    private boolean haveFlush;
    //刷新坐标
    private Position pos;
    //出生时间
    private long bornTime;
    //下一次刷新时间
    private long nextTime;
    //对应配置表的id
    private int configId;
    //所在地图唯一ID
    @JsonIgnore
    transient long mapUid = 0;

    //************************方法*********************************

    public String name() {
        return Manager.monsterManager.GetMonsterName(modelId);
    }

    @Override
    public String toString() {
        return "Boss{" +
                ", modelId=" + modelId +
                ", mapID=" + mapID +
                ", haveFlush=" + haveFlush +
                ", pos=" + pos +
                ", nextTime=" + nextTime +
                ", configId=" + configId +
                '}';
    }

    //**************************getter and setter***********************************

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getMapID() {
        return mapID;
    }

    public void setMapID(int mapID) {
        this.mapID = mapID;
    }

    public boolean isHaveFlush() {
        return haveFlush;
    }

    public void setHaveFlush(boolean haveFlush) {
        this.haveFlush = haveFlush;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public long getBornTime() {
        return bornTime;
    }

    public void setBornTime(long bornTime) {
        this.bornTime = bornTime;
    }

    public long getNextTime() {
        return nextTime;
    }

    public void setNextTime(long nextTime) {
        this.nextTime = nextTime;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public long getMapUid() {return mapUid;}

    public void setMapUid(long mapUid) {this.mapUid = mapUid;}
}
