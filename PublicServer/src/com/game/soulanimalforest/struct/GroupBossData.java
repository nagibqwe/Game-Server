/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.soulanimalforest.struct;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class GroupBossData {
    int groupId = 0;
        /**
     * 兽灵水晶的最后一次刷新的时间 BeastSpiritCrystal
     */
    private final ConcurrentHashMap<Integer, BossData> crystalTime = new ConcurrentHashMap<>();//<副本ID，兽灵与兽神的重生时间管理>
    private final ConcurrentHashMap<Integer, BossData> beastlyBloodCrystalBirthTime = new ConcurrentHashMap<>();//<副本ID， 兽血水晶的重生时间管理>
    private final ConcurrentHashMap<Integer, BossData> soulAnimalForestMonsterTime = new ConcurrentHashMap<>();//<副本ID， 精英怪的重生时间管理>
    private final ConcurrentHashMap<Integer, BossData> soulAnimalDataMap = new ConcurrentHashMap<>(); //boss需要存库的数据信息
    private final ConcurrentHashMap<Integer, List<BossData>> soulAnimalDataListMap = new ConcurrentHashMap<>(); //副本ID， boss需要存库的数据信息

    public ConcurrentHashMap<Integer, List<BossData>> getSoulAnimalDataListMap() {
        return soulAnimalDataListMap;
    }

    public ConcurrentHashMap<Integer, BossData> getCrystalTime() {
        return crystalTime;
    }

    public ConcurrentHashMap<Integer, BossData> getBeastlyBloodCrystalBirthTime() {
        return beastlyBloodCrystalBirthTime;
    }

    public ConcurrentHashMap<Integer, BossData> getSoulAnimalForestMonsterTime() {
        return soulAnimalForestMonsterTime;
    }

    public ConcurrentHashMap<Integer, BossData> getSoulAnimalDataMap() {
        return soulAnimalDataMap;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
