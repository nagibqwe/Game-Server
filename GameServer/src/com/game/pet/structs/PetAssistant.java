package com.game.pet.structs;

import game.message.PetMessage;

import java.util.concurrent.ConcurrentHashMap;

/**宠物助战位*/
public class PetAssistant {

    /**
     * 放置的宠物id
     * */
    private int petId;

    /**
     * 几个装备位置
     * */
    private ConcurrentHashMap<Integer, PetEquipPart> parts = new ConcurrentHashMap<>();

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

    /**评分*/
    private int score;

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

    public ConcurrentHashMap<Integer, PetEquipPart> getParts() {
        return parts;
    }

    public void setParts(ConcurrentHashMap<Integer, PetEquipPart> parts) {
        this.parts = parts;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
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

    public PetMessage.PetAssistantInfo bytesWriteToPb(int assistantId) {
        PetMessage.PetAssistantInfo.Builder pb = PetMessage.PetAssistantInfo.newBuilder();
        pb.setAssistantId(assistantId);
        pb.setPetId(petId);
        pb.setSoulActiveId(soulActiveId);
        pb.setStrengthActiveId(strengthActiveId);
        pb.setOpen(active);
        pb.setScore(score);
        parts.entrySet().forEach(entry -> pb.addCellList(entry.getValue().bytesWriteToPb(entry.getKey())));
        return pb.build();
    }
}
