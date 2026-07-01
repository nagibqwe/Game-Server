package com.game.soulArmor.struct;

import com.game.backpack.structs.Item;
import com.game.skill.structs.Skill;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO 魂甲
 * @Date 2020/12/23 18:15
 * @Auth ZUncle
 */
public class SoulArmor {
    boolean open;           //功能开关
    int level;              //淬炼等级
    int qualityLevel = 1;   //突破等级
    int skillLevel;         //觉醒等级
    HashMap<Integer, Skill> skills = new HashMap<>();
    int lotteryLevel;   //抽奖等级
    int lotteryExp;     //抽奖经验

    int wearId;         //穿戴的魂甲ID

    ConcurrentHashMap<Integer, SoulArmorSlot> slots = new ConcurrentHashMap<>();     //魂珠镶嵌
    ConcurrentHashMap<Long, Item> bag = new ConcurrentHashMap<>();                  //魂甲背包

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int gainQualityLevel() {
        return open ? qualityLevel : 0;
    }

    public int getQualityLevel() {
        return qualityLevel;
    }

    public void setQualityLevel(int qualityLevel) {
        this.qualityLevel = qualityLevel;
    }

    public int getWearId() {
        return wearId;
    }

    public void setWearId(int wearId) {
        this.wearId = wearId;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public HashMap<Integer, Skill> getSkills() {
        return skills;
    }

    public void setSkills(HashMap<Integer, Skill> skills) {
        this.skills = skills;
    }

    public int getLotteryLevel() {
        return lotteryLevel;
    }

    public void setLotteryLevel(int lotteryLevel) {
        this.lotteryLevel = lotteryLevel;
    }

    public int getLotteryExp() {
        return lotteryExp;
    }

    public void setLotteryExp(int lotteryExp) {
        this.lotteryExp = lotteryExp;
    }

    public ConcurrentHashMap<Integer, SoulArmorSlot> getSlots() {
        return slots;
    }

    public void setSlots(ConcurrentHashMap<Integer, SoulArmorSlot> slots) {
        this.slots = slots;
    }

    public ConcurrentHashMap<Long, Item> getBag() {
        return bag;
    }

    public void setBag(ConcurrentHashMap<Long, Item> bag) {
        this.bag = bag;
    }

}
