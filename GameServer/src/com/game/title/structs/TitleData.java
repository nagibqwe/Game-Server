package com.game.title.structs;

import java.util.concurrent.ConcurrentHashMap;

public class TitleData {

    /**
     * 当前穿戴称号id
     */
    private int wearId;

    /**
     * 拥有的称号
     * key：  称号id
     * value：限时时长，0表示永久
     */
    private ConcurrentHashMap<Integer, Integer> titleList = new ConcurrentHashMap<>();

    public int getWearId() {
        return wearId;
    }

    public void setWearId(int wearId) {
        this.wearId = wearId;
    }

    public ConcurrentHashMap<Integer, Integer> getTitleList() {
        return titleList;
    }

    public void setTitleList(ConcurrentHashMap<Integer, Integer> titleList) {
        this.titleList = titleList;
    }
}
