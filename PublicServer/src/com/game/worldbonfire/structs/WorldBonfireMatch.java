package com.game.worldbonfire.structs;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Description
 * @auther lw
 * @create 2019-10-21 15:54
 */
public class WorldBonfireMatch {

    private long roleId;

    private String name = "";

    private int stateLv;

    private int fashionBodyId;

    private int fashionWeaponId;

    private int fashionHalo;

    private int fashionMatrix;

    private int wingId;

    private int remainWine;

    private long time;

    private int carrer;

    private int curTotal = -1;

    private int curFinger = -1;

    private int lastRes;

    private int spiritId;

    private ChannelHandlerContext context;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStateLv() {
        return stateLv;
    }

    public void setStateLv(int stateLv) {
        this.stateLv = stateLv;
    }

    public int getFashionBodyId() {
        return fashionBodyId;
    }

    public void setFashionBodyId(int fashionBodyId) {
        this.fashionBodyId = fashionBodyId;
    }

    public int getFashionWeaponId() {
        return fashionWeaponId;
    }

    public void setFashionWeaponId(int fashionWeaponId) {
        this.fashionWeaponId = fashionWeaponId;
    }

    public int getWingId() {
        return wingId;
    }

    public void setWingId(int wingId) {
        this.wingId = wingId;
    }

    public int getFashionHalo() {
        return fashionHalo;
    }

    public void setFashionHalo(int fashionHalo) {
        this.fashionHalo = fashionHalo;
    }

    public int getFashionMatrix() {
        return fashionMatrix;
    }

    public void setFashionMatrix(int fashionMatrix) {
        this.fashionMatrix = fashionMatrix;
    }

    public int getRemainWine() {
        return remainWine;
    }

    public void setRemainWine(int remainWine) {
        this.remainWine = remainWine;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getCarrer() {
        return carrer;
    }

    public void setCarrer(int carrer) {
        this.carrer = carrer;
    }

    public int getCurTotal() {
        return curTotal;
    }

    public void setCurTotal(int curTotal) {
        this.curTotal = curTotal;
    }

    public int getCurFinger() {
        return curFinger;
    }

    public void setCurFinger(int curFinger) {
        this.curFinger = curFinger;
    }

    public int getLastRes() {
        return lastRes;
    }

    public void setLastRes(int lastRes) {
        this.lastRes = lastRes;
    }

    public int getSpiritId() {
        return spiritId;
    }

    public void setSpiritId(int spiritId) {
        this.spiritId = spiritId;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    public void setContext(ChannelHandlerContext context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "WorldBonfireMatch{" +
                "roleId=" + roleId +
                ", name='" + name + '\'' +
                ", stateLv=" + stateLv +
                ", fashionBodyId=" + fashionBodyId +
                ", fashionWeaponId=" + fashionWeaponId +
                ", fashionHalo=" + fashionHalo +
                ", fashionMatrix=" + fashionMatrix +
                ", wingId=" + wingId +
                ", remainWine=" + remainWine +
                ", time=" + time +
                ", carrer=" + carrer +
                ", curTotal=" + curTotal +
                ", curFinger=" + curFinger +
                ", lastRes=" + lastRes +
                ", context=" + context +
                '}';
    }
}
