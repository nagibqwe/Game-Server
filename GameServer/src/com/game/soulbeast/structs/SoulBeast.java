package com.game.soulbeast.structs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zcd on 2018/5/4.
 */
public class SoulBeast {

    /** 魂兽Id */
    private int beastId;
    /**
     * 是否出战
     */
    private boolean work;

    /** 已经穿戴的装备 */
    private Map<Integer, SoulBeastEquip> equip = new HashMap<>();

    public int getBeastId() {
        return beastId;
    }

    public void setBeastId(int beastId) {
        this.beastId = beastId;
    }

    public boolean isWork() {
        return work;
    }

    public void setWork(boolean work) {
        this.work = work;
    }

    public Map<Integer, SoulBeastEquip> getEquip() {
        return equip;
    }

    public void setEquip(Map<Integer, SoulBeastEquip> equip) {
        this.equip = equip;
    }
}
