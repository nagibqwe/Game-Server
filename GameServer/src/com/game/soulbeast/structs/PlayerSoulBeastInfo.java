package com.game.soulbeast.structs;

import com.game.backpack.structs.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家战兽信息总管类
 * Created by zcd on 2018/5/4.
 */
public class PlayerSoulBeastInfo {

    /**
     * 玩家兽魂装备背包
     */
    private Map<Long, Item> soulBeastEquipMap = new ConcurrentHashMap<>();

    /**
     * 魂兽信息
     */
    private Map<Integer, SoulBeast> soulBeasts = new HashMap<>();

    /**
     * 神兽格子输
     */
    private int soulGridNum;


    public Map<Long, Item> getSoulBeastEquipMap() {
        return soulBeastEquipMap;
    }

    public void setSoulBeastEquipMap(Map<Long, Item> soulBeastEquipMap) {
        this.soulBeastEquipMap = soulBeastEquipMap;
    }

    public Map<Integer, SoulBeast> getSoulBeasts() {
        return soulBeasts;
    }

    public void setSoulBeasts(Map<Integer, SoulBeast> soulBeasts) {
        this.soulBeasts = soulBeasts;
    }

    public int getSoulGridNum() {
        return soulGridNum;
    }

    public void setSoulGridNum(int soulGridNum) {
        this.soulGridNum = soulGridNum;
    }

}
