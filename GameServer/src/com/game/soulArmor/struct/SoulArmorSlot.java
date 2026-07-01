package com.game.soulArmor.struct;

import com.game.backpack.structs.Item;

/**
 * @Desc TODO
 * @Date 2020/12/28 15:12
 * @Auth ZUncle
 */
public class SoulArmorSlot {
    int slotId;     //位置ID
    int level;      //强化等级
    Item ball;      //魂印

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Item getBall() {
        return ball;
    }

    public void setBall(Item ball) {
        this.ball = ball;
    }

    @Override
    public String toString() {
        return "SoulArmorSlot{" +
                "slotId=" + slotId +
                ", level=" + level +
                ", ball=" + ball +
                '}';
    }
}
