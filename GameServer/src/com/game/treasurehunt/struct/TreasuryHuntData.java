package com.game.treasurehunt.struct;


import com.game.backpack.structs.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 瞿冰冰
 * 2019/7/12
 */
public class TreasuryHuntData {

    //玩家寻宝记录
    private Map<Integer,List<TreasureHuntRecord>>  records = new HashMap<>();

    //玩家的仓库
    private Map<Integer,Map<Long,Item>> storeHouse = new HashMap<>();

    private Map<Integer,Long> lastHuntTime = new HashMap<>();

    public Map<Integer, List<TreasureHuntRecord>> getRecords() {
        return records;
    }

    public void setRecords(Map<Integer, List<TreasureHuntRecord>> records) {
        this.records = records;
    }

    public Map<Integer, Map<Long, Item>> getStoreHouse() {
        return storeHouse;
    }

    public void setStoreHouse(Map<Integer, Map<Long, Item>> storeHouse) {
        this.storeHouse = storeHouse;
    }

    public Map<Integer, Long> getLastHuntTime() {
        return lastHuntTime;
    }

    public void setLastHuntTime(Map<Integer, Long> lastHuntTime) {
        this.lastHuntTime = lastHuntTime;
    }
}
