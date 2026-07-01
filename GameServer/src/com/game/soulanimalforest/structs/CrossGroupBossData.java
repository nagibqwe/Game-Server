/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.soulanimalforest.structs;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class CrossGroupBossData {
    private int groupId= 0;
     private final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> beastSpiritCrystalMap = new ConcurrentHashMap<>(); //层，怪的配置ID,怪物数量值

    /**
     * 魂兽森林的守卫的最后更新时间
     */
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> soulAnimalForestMonsterMap = new ConcurrentHashMap<>(); //层，怪的配置ID
    /**
     *
     */
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> beastlyBloodCrystalMap = new ConcurrentHashMap<>(); //层，怪的配置ID

    public ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> getBeastSpiritCrystalMap() {
        return beastSpiritCrystalMap;
    }

    public ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> getSoulAnimalForestMonsterMap() {
        return soulAnimalForestMonsterMap;
    }

    public ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> getBeastlyBloodCrystalMap() {
        return beastlyBloodCrystalMap;
    }

    private final ConcurrentHashMap<Integer, CrossBossData> crystalTime = new ConcurrentHashMap<>();//<副本ID，兽灵与兽神的重生时间管理>
    private final ConcurrentHashMap<Integer, CrossBossData> beastlyBloodCrystalBirthTime = new ConcurrentHashMap<>();//<副本ID， 兽血水晶的重生时间管理>
    private final ConcurrentHashMap<Integer, CrossBossData> soulAnimalForestMonsterTime = new ConcurrentHashMap<>();//<副本ID， 精英怪的重生时间管理>
    private final ConcurrentHashMap<Integer, CrossBossData> soulAnimalDataMap = new ConcurrentHashMap<>(); //boss需要存库的数据信息

    public ConcurrentHashMap<Integer, CrossBossData> getCrystalTime() {
        return crystalTime;
    }

    public ConcurrentHashMap<Integer, CrossBossData> getBeastlyBloodCrystalBirthTime() {
        return beastlyBloodCrystalBirthTime;
    }

    public ConcurrentHashMap<Integer, CrossBossData> getSoulAnimalForestMonsterTime() {
        return soulAnimalForestMonsterTime;
    }

    public ConcurrentHashMap<Integer, CrossBossData> getSoulAnimalDataMap() {
        return soulAnimalDataMap;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
