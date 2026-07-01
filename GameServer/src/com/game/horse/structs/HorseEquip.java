package com.game.horse.structs;

import com.game.manager.Manager;
import com.game.structs.GameObject;
import game.message.HorseMessage;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 坐骑脉轮信息
 */
public class HorseEquip {
    /**
     * 脉轮id
     */
    private int equipId;
    /**
     * 脉轮部位信息
     */
    private Map<Integer, HorseEquipPart> parts = new ConcurrentHashMap<>(4);

    /**
     * 全身强化目标中激活的配置表id
     * */
    private int strengthActiveId;

    /**
     * 全身附魂目标中激活的配置表id
     * */
    private int soulActiveId;

    /**
     * 是否激活
     */
    private boolean active;

    /**
     * 脉轮的评分，进入副本需要
     * 装备评分相加
     */
    private int score;

    /**
     * 显示的buff
     */
    private int buff;

    public HorseEquip(){}

    public HorseEquip(int equipId){
        this.equipId = equipId;
    }

    public int getEquipId() {
        return equipId;
    }

    public void setEquipId(int equipId) {
        this.equipId = equipId;
    }

    public Map<Integer, HorseEquipPart> getParts() {
        return parts;
    }

    public void setParts(Map<Integer, HorseEquipPart> parts) {
        this.parts = parts;
    }

    public int getStrengthActiveId() {
        return strengthActiveId;
    }

    public void setStrengthActiveId(int strengthActiveId) {
        this.strengthActiveId = strengthActiveId;
    }

    public int getSoulActiveId() {
        return soulActiveId;
    }

    public void setSoulActiveId(int soulActiveId) {
        this.soulActiveId = soulActiveId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBuff() {
        return buff;
    }

    public void setBuff(int buff) {
        this.buff = buff;
    }

    public HorseMessage.HorseEquipInfo bytesWriteToPb() {
        HorseMessage.HorseEquipInfo.Builder pb = HorseMessage.HorseEquipInfo.newBuilder();
        pb.setAssistantId(equipId);
        pb.setPetId(0);
        pb.setSoulActiveId(soulActiveId > 0 ? soulActiveId : equipId * 10000);
        pb.setStrengthActiveId(strengthActiveId > 0 ? strengthActiveId : equipId * 10000);
        pb.setOpen(active);
        this.parts.entrySet().forEach(entry->pb.addCellList(entry.getValue().bytesWriteToPb(entry.getKey())));
        return pb.build();
    }

}
