package com.game.universe.struct;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 太虚战场数据
 */
public class UniverseWarData {

    /**
     * 公共服分组ID
     */
    private int crossServerGroupId = 0;

    /**
     * 公共服分组游戏服的平均世界等级
     */
    private int worldLevel = 0;

    /**
     * 准备阻挡是否开启
     */
    private boolean isOpen = false;

    /**
     * 阵营信息
     * Map<服务器标识, 阵营ID>
     */
    Map<String,Integer> campMap = new LinkedHashMap<>();

    /**
     * 守关BOSS死亡后加的阵营buff
     * List<buffID>
     */
    private List<Integer> buffList = new ArrayList<>();

    /**
     * 守关BOSS死亡记录
     * List<阵营ID>
     */
    private List<Integer> openCamps = new ArrayList<>();

    /**
     * 阵营传送点
     * Map<阵营ID, 传送点Map>
     */
    private Map<Integer,Map<Integer,Integer>> transportMap = new ConcurrentHashMap<>();

    /**
     * 怪物死亡信息
     * Map<怪物模型ID, 怪物唯一ID>
     */
    private Map<Integer,Long> dieMap = new ConcurrentHashMap<>();

    /**
     * 怪物刷新时间
     * Map<怪物模型ID, 怪物下次刷新时间>
     */
    private Map<Integer,Long> refreshMap = new ConcurrentHashMap<>();

    public int getCrossServerGroupId() {
        return crossServerGroupId;
    }

    public void setCrossServerGroupId(int crossServerGroupId) {
        this.crossServerGroupId = crossServerGroupId;
    }

    public int getWorldLevel() {
        return worldLevel;
    }

    public void setWorldLevel(int worldLevel) {
        this.worldLevel = worldLevel;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Map<String, Integer> getCampMap() {
        return campMap;
    }

    public void setCampMap(Map<String, Integer> campMap) {
        this.campMap = campMap;
    }

    public List<Integer> getBuffList() {
        return buffList;
    }

    public void setBuffList(List<Integer> buffList) {
        this.buffList = buffList;
    }

    public List<Integer> getOpenCamps() {
        return openCamps;
    }

    public void setOpenCamps(List<Integer> openCamps) {
        this.openCamps = openCamps;
    }

    public Map<Integer, Map<Integer, Integer>> getTransportMap() {
        return transportMap;
    }

    public void setTransportMap(Map<Integer, Map<Integer, Integer>> transportMap) {
        this.transportMap = transportMap;
    }

    public Map<Integer, Long> getDieMap() {
        return dieMap;
    }

    public void setDieMap(Map<Integer, Long> dieMap) {
        this.dieMap = dieMap;
    }

    public Map<Integer, Long> getRefreshMap() {
        return refreshMap;
    }

    public void setRefreshMap(Map<Integer, Long> refreshMap) {
        this.refreshMap = refreshMap;
    }
}
