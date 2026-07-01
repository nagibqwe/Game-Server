package com.game.immortalequip.structs;

import com.game.backpack.structs.Equip;

/**
 * Created by 542 on 2020/2/12.
 */
public class ImmortalEquipPart {

    /**
     * 每个部位的id，该id只代表玩家部位
     * 取值范围 30-43
     * */
    private int part;

    /**
     * 穿戴的装备
     * */
    private Equip equip ;


    public Equip getEquip() {
        return equip;
    }

    public void setEquip(Equip equip) {
        this.equip = equip;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }
}
