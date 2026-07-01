package com.game.copymap.structs;

import com.game.backpack.structs.Item;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO 副本基本信息
 * @Date 2021/5/17 16:24
 * @Auth ZUncle
 */
public class ZoneCache {

    public ZoneCache() {
    }

    public ZoneCache(int zoneId, int level) {
        this.zoneId = zoneId;
        this.level = level;
    }

    int zoneId; //副本模板Id
    int level;  //副本难度等级
    ConcurrentHashMap<Long, Integer> memberRate = new ConcurrentHashMap<>();     //副本合并次数
    //副本掉落记录<PlayerID, List<Item>>
    HashMap<Long, HashMap<Integer, Item>> dropItemsHistory = new HashMap<>();


    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ConcurrentHashMap<Long, Integer> getMerge() {
        return memberRate;
    }

    public HashMap<Long, HashMap<Integer, Item>> getDropItemsHistory() {
        return dropItemsHistory;
    }

    public HashMap<Integer, Item> getDropItemsHistory(long roleId) {
        if (!dropItemsHistory.containsKey(roleId)) {
            dropItemsHistory.put(roleId, new HashMap<>());
        }
        return dropItemsHistory.get(roleId);
    }

    @Override
    public String toString() {
        return "Zone{" +
                "zoneId=" + zoneId +
                ", level=" + level +
                '}';
    }
}
