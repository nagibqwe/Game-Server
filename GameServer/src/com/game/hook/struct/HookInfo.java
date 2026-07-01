package com.game.hook.struct;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class HookInfo {

    /**
     * 是否在挂机中
     */
    private boolean onHook;

    /**
     * 挂机时长,单位秒(剩余时长，可补充)
     */
    private int hookTime;

    /**
     * 打坐开始时间,单位秒
     */
    private int sittingStartTime;

    /**打坐增加的经验*/
    private transient long sittingExe;
    /**打坐前等级*/
    private transient int sittingLevel;
    /**打坐前经验值*/
    private transient BigInteger sittingBeforeExe;

    /**
     * 单次挂机找回时间
     */
    private int hookFindTime;
    /**
     * 总挂机找回时间
     */
    private int totalHookFindTime;
    /**
     * 挂机找回经验
     */
    private long hookFindExp;


    /**
     * 经验药使用情况：<倍率（百分比）,剩余时间（秒）>
     */
    private ConcurrentHashMap<Integer, Integer> itemExpAddRateTime = new ConcurrentHashMap<>();

    public boolean isOnHook() {
        return onHook;
    }

    public void setOnHook(boolean onHook) {
        this.onHook = onHook;
    }

    public int getHookTime() {
        return hookTime;
    }

    public void setHookTime(int hookTime) {
        this.hookTime = hookTime;
    }

    public int getSittingStartTime() {
        return sittingStartTime;
    }

    public void setSittingStartTime(int sittingStartTime) {
        this.sittingStartTime = sittingStartTime;
    }

    public ConcurrentHashMap<Integer, Integer> getItemExpAddRateTime() {
        return itemExpAddRateTime;
    }

    public void setItemExpAddRateTime(ConcurrentHashMap<Integer, Integer> itemExpAddRateTime) {
        this.itemExpAddRateTime = itemExpAddRateTime;
    }

    public long getSittingExe() {
        return sittingExe;
    }

    public void setSittingExe(long sittingExe) {
        this.sittingExe = sittingExe;
    }

    public int getSittingLevel() {
        return sittingLevel;
    }

    public void setSittingLevel(int sittingLevel) {
        this.sittingLevel = sittingLevel;
    }

    public BigInteger getSittingBeforeExe() {
        return sittingBeforeExe;
    }

    public void setSittingBeforeExe(BigInteger sittingBeforeExe) {
        this.sittingBeforeExe = sittingBeforeExe;
    }


    public int getHookFindTime() {
        return hookFindTime;
    }

    public void setHookFindTime(int hookFindTime) {
        this.hookFindTime = hookFindTime;
    }

    public long getHookFindExp() {
        return hookFindExp;
    }

    public void setHookFindExp(long hookFindExp) {
        this.hookFindExp = hookFindExp;
    }

    public int getTotalHookFindTime() {
        return totalHookFindTime;
    }

    public void setTotalHookFindTime(int totalHookFindTime) {
        this.totalHookFindTime = totalHookFindTime;
    }
}
