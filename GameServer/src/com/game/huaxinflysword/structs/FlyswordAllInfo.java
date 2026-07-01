package com.game.huaxinflysword.structs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 542 on 2020/7/4.
 */
public class FlyswordAllInfo {

    //当前使用的外观剑灵
    private int curUseFlySwordId;

    //当前系列剑灵激活的最大ID，不管怎么切换飞剑，都使用当前激活最大ID的技能
    private int curFlySwordSkillId;

    //key = type;
    private ConcurrentHashMap<Integer,FlyswordData> flyswordDataMap = new ConcurrentHashMap<>();

    //剑冢通关状态信息
    ConcurrentHashMap<Integer, Integer> swaordTombInfo = new ConcurrentHashMap<>();

    //剑灵阁当前层数(默认1层)
    private int swordSoulLayer = 1;

    //挂机开始时间
    private int hookStartTime;

    //当前层挂机开始时间
    private int curHookStartTime;

    //剑灵阁挂机奖励
    private ConcurrentHashMap<Integer, Integer> hookTimeInfo = new ConcurrentHashMap<>();


    //剑灵激活的技能
    private List<Integer> flySwordSkill = new ArrayList<>();


    private boolean frist_reward = false;

    public int getCurFlySwordSkillId() {
        return curFlySwordSkillId;
    }

    public void setCurFlySwordSkillId(int curFlySwordSkillId) {
        this.curFlySwordSkillId = curFlySwordSkillId;
    }

    public int getCurUseFlySwordId() {
        return curUseFlySwordId;
    }

    public void setCurUseFlySwordId(int curUseFlySwordId) {
        this.curUseFlySwordId = curUseFlySwordId;
    }

    public ConcurrentHashMap<Integer, FlyswordData> getFlyswordDataMap() {
        return flyswordDataMap;
    }

    public void setFlyswordDataMap(ConcurrentHashMap<Integer, FlyswordData> flyswordDataMap) {
        this.flyswordDataMap = flyswordDataMap;
    }

    public ConcurrentHashMap<Integer, Integer> getSwaordTombInfo() {
        return swaordTombInfo;
    }

    public void setSwaordTombInfo(ConcurrentHashMap<Integer, Integer> swaordTombInfo) {
        this.swaordTombInfo = swaordTombInfo;
    }

    public int getSwordSoulLayer() {
        return swordSoulLayer;
    }

    public void setSwordSoulLayer(int swordSoulLayer) {
        this.swordSoulLayer = swordSoulLayer;
    }

    public int getHookStartTime() {
        return hookStartTime;
    }

    public void setHookStartTime(int hookStartTime) {
        this.hookStartTime = hookStartTime;
    }

    public int getCurHookStartTime() {
        return curHookStartTime;
    }

    public void setCurHookStartTime(int curHookStartTime) {
        this.curHookStartTime = curHookStartTime;
    }

    public ConcurrentHashMap<Integer, Integer> getHookTimeInfo() {
        return hookTimeInfo;
    }

    public void setHookTimeInfo(ConcurrentHashMap<Integer, Integer> hookTimeInfo) {
        this.hookTimeInfo = hookTimeInfo;
    }

    public List<Integer> getFlySwordSkill() {
        return flySwordSkill;
    }

    public void setFlySwordSkill(List<Integer> flySwordSkill) {
        this.flySwordSkill = flySwordSkill;
    }

    public boolean isFrist_reward() {
        return frist_reward;
    }

    public void setFrist_reward(boolean frist_reward) {
        this.frist_reward = frist_reward;
    }
}
