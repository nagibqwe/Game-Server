package com.game.openserverac.structs;

import java.util.HashMap;

public class OpenServerGrowUp {
    /**
     * 总积分(大师点)
     */
    private int point;

    /**
     * 已领取状态
     */
    private int hasGet;

    /**
     * 直购价格
     */
    private int purPrice;

    //TODO这个废弃
    /**
     * 成长之路领取状态
     */
    private HashMap<Integer, Boolean> growups = new HashMap<>();


    /**
     * 新成长之路数据结构装填
     */
    private HashMap<Integer,GrowUpData> newGroupDatas = new HashMap<>();


    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getHasGet() {
        return hasGet;
    }

    public void setHasGet(int hasGet) {
        this.hasGet = hasGet;
    }

    public int getPurPrice() {
        return purPrice;
    }

    public void setPurPrice(int purPrice) {
        this.purPrice = purPrice;
    }

    public HashMap<Integer, Boolean> getGrowups() {
        return growups;
    }

    public void setGrowups(HashMap<Integer, Boolean> growups) {
        this.growups = growups;
    }


    public HashMap<Integer, GrowUpData> getNewGroupDatas() {
        return newGroupDatas;
    }

    public void setNewGroupDatas(HashMap<Integer, GrowUpData> newGroupDatas) {
        this.newGroupDatas = newGroupDatas;
    }
}
