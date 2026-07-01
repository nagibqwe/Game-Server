package com.game.nature.structs;

import java.util.concurrent.ConcurrentHashMap;

public class Nature extends NatureBase {
    /**
     * 当前经验
     * */
    private int currentExp;
    /**
     * drug映射表
     * key是物品配置表id(丹药模板id)，value是drug实例
     * */
    private ConcurrentHashMap<Integer, Drug> drugs = new ConcurrentHashMap<>();

    public int getCurrentExp() {
        return currentExp;
    }

    public void setCurrentExp(int currentExp) {
        this.currentExp = currentExp;
    }

    public void setDrugs(ConcurrentHashMap<Integer, Drug> drugs) {
        this.drugs = drugs;
    }

    public ConcurrentHashMap<Integer, Drug> getDrugs() {
        return drugs;
    }

    public void addCurrentExp(int value) {
        this.currentExp += value;
    }
}
