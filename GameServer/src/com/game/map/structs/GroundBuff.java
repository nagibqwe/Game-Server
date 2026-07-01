/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.map.structs;

import com.game.structs.GameObject;
import game.core.map.IMapObject;
import game.core.map.Position;
import game.core.net.Config.ServerConfig;

import java.util.HashMap;
import java.util.List;

/**
 *地面BUFF处理
 * @author xuchangming <xysoko@qq.com>
 */
public class GroundBuff extends GameObject implements IMapObject  {
    private long createTime;
    private int modelId;
    private int logicSize;
    private Position pos;//坐标
    private int type = 0 ;// 0 永久， 1 为临时的
    private int line;
    private long mapId;
    //所属阵营， 为0时两个阵营都加
    private int groupNo;
    //生效次数
    private int maxNum;
    //上一次生效时间
    private long lastUpdateTime;
    
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getLogicSize() {
        return logicSize;
    }

    public void setLogicSize(int logicSize) {
        this.logicSize = logicSize;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }  

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    @Override
    public int gainLine() {
        return line;
    }

    @Override
    public int gainMapModelId() {
        return modelId;
    }

    @Override
    public long gainMapId() {
        return mapId;
    }

    @Override
    public Position gainCurPos() {
        return pos;
    }

    @Override
    public boolean canSee(IMapObject player) {
        return true;
    }

    /**
     * 获取任务隐藏id集合
     *
     * @return
     */
    @Override
    public HashMap<Integer, List<Integer>> gainHideTaskIds() {
        return null;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }   

    public int getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public void release() {

    }
}
