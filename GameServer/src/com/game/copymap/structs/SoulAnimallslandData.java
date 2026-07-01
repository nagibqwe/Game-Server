package com.game.copymap.structs;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 瞿冰冰
 * 2019/8/17
 */
public class SoulAnimallslandData {

    //地图上除掉boss,以外的所有东西坐标
    private Map<Long,String> idPosMap = new ConcurrentHashMap<>();

    //地图上坐标有东西
    private Map<String,Integer> posHasValueMap = new ConcurrentHashMap<>();

    //所有事物的刷新时间 除了j boss
    private Map<Integer,Integer> allThingBornTimeMap = new ConcurrentHashMap<>();

    //所有boss对应的配置表id
    private Map<Integer,Integer> bossConfigIds = new ConcurrentHashMap<>();

    private Set<String> refshTypeSet = new HashSet<>();

    private int crossServerGroupId = 0;

    public Map<Long, String> getIdPosMap() {
        return idPosMap;
    }

    public void setIdPosMap(Map<Long, String> idPosMap) {
        this.idPosMap = idPosMap;
    }

    public Map<String, Integer> getPosHasValueMap() {
        return posHasValueMap;
    }

    public void setPosHasValueMap(Map<String, Integer> posHasValueMap) {
        this.posHasValueMap = posHasValueMap;
    }

    public Map<Integer, Integer> getAllThingBornTimeMap() {
        return allThingBornTimeMap;
    }

    public void setAllThingBornTimeMap(Map<Integer, Integer> allThingBornTimeMap) {
        this.allThingBornTimeMap = allThingBornTimeMap;
    }

    public Set<String> getRefshTypeSet() {
        return refshTypeSet;
    }

    public void setRefshTypeSet(Set<String> refshTypeSet) {
        this.refshTypeSet = refshTypeSet;
    }

    public Map<Integer, Integer> getBossConfigIds() {
        return bossConfigIds;
    }

    public void setBossConfigIds(Map<Integer, Integer> bossConfigIds) {
        this.bossConfigIds = bossConfigIds;
    }

    public int getCrossServerGroupId() {
        return crossServerGroupId;
    }

    public void setCrossServerGroupId(int crossServerGroupId) {
        this.crossServerGroupId = crossServerGroupId;
    }
}
