package com.game.godbook.struct;

import java.util.concurrent.ConcurrentHashMap;

public class Amulet {

    /**
     * 符咒id
     */
    private int id;

    /**
     * 激活符咒的条件
     */
    private ConcurrentHashMap<Integer, ConditionInfo> amuletInfo = new ConcurrentHashMap<>();

    /**
     * 是否激活
     */
    private boolean activated;

    public Amulet() {}

    public Amulet(int amuletId) {
        this.id = amuletId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ConcurrentHashMap<Integer, ConditionInfo> getAmuletInfo() {
        return amuletInfo;
    }

    public void setAmuletInfo(ConcurrentHashMap<Integer, ConditionInfo> amuletInfo) {
        this.amuletInfo = amuletInfo;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public String toString() {
        return "Amulet{" +
                "id=" + id +
                ", amuletInfo=" + amuletInfo +
                ", activated=" + activated +
                '}';
    }
}
