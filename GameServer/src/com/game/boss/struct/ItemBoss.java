package com.game.boss.struct;

import game.core.map.Position;

/**
 * 使用物品刷新出来的boss
 */
public class ItemBoss {

    private int boosId;

    private Position position;

    public ItemBoss(){}

    public ItemBoss(int bossId, Position position) {
        this.boosId = bossId;
        this.position = position;
    }

    public int getBoosId() {
        return boosId;
    }

    public void setBoosId(int boosId) {
        this.boosId = boosId;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "ItemBoss{" +
                "boosId=" + boosId +
                ", position=" + position +
                '}';
    }
}
