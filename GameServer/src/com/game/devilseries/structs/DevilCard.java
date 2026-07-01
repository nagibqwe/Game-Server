package com.game.devilseries.structs;

import game.message.DevilSeriesMessage;

import java.util.HashMap;
import java.util.Map;

public class DevilCard {
    //魔魂id
    private int id;
    //装备信息
    private Map<Integer,DevilEquipPart> parts = new HashMap<>();
    //是否激活
    private boolean active = false;
    //强化阶数
    private int rank;
    //强化等级
    private int level;
    //突破等级
    private int break_level;
    //战斗力
    private int fightPoint;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, DevilEquipPart> getParts() {
        return parts;
    }

    public void setParts(Map<Integer, DevilEquipPart> parts) {
        this.parts = parts;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getBreak_level() {
        return break_level;
    }

    public void setBreak_level(int break_level) {
        this.break_level = break_level;
    }

    public int getFightPoint() {
        return fightPoint;
    }

    public void setFightPoint(int fightPoint) {
        this.fightPoint = fightPoint;
    }

    public DevilSeriesMessage.DevilCard.Builder toProto() {
        DevilSeriesMessage.DevilCard.Builder obj = DevilSeriesMessage.DevilCard.newBuilder();
        obj.setId(id);
        obj.setActive(active);
        obj.setLevel(level);
        obj.setRank(rank);
        obj.setBreakLv(break_level);
        obj.setFightPoint(fightPoint);
        for(DevilEquipPart part : parts.values()){
            obj.addPart(part.toProto());
        }
        return obj;
    }
}
