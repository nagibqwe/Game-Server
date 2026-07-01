/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.soulanimalforest.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * 魂兽森林的内存数据结构
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class BossData {

    private int configId;

    private long bornTime;        //boss出生刷新时间点

    private int dieTimes;           //首次出生刷新后当前被击杀死亡次数

    private int reBornBaseTime;   //重生基数时间，单位：秒

    private long rebornTime;      //重生时间点(记录下来主要用于服务器重启加载boss后设置其下次刷新时间)
    //上一轮击杀者
    private List<KilledRecord> killer = new ArrayList<>();
    
    private int dieNum; //死亡的数量值
    
    private int maxNum; //总的数量个数
    
    private int cloneId;//副本ID值
    
    private boolean flushFollow;//是否已经发过通知了
    
    private long fightId;//战斗服房间ID值
    

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public long getBornTime() {
        return bornTime;
    }

    public void setBornTime(long bornTime) {
        this.bornTime = bornTime;
    }

    public int getDieTimes() {
        return dieTimes;
    }

    public void setDieTimes(int dieTimes) {
        this.dieTimes = dieTimes;
    }

    public int getReBornBaseTime() {
        return reBornBaseTime;
    }

    public void setReBornBaseTime(int reBornBaseTime) {
        this.reBornBaseTime = reBornBaseTime;
    }

    public long getRebornTime() {
        return rebornTime;
    }

    public void setRebornTime(long rebornTime) {
        this.rebornTime = rebornTime;
    }

    public List<KilledRecord> getKiller() {
        return killer;
    }

    public void setKiller(List<KilledRecord> killer) {
        this.killer = killer;
    }

    public int getDieNum() {
        return dieNum;
    }

    public void setDieNum(int dieNum) {
        this.dieNum = dieNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getCloneId() {
        return cloneId;
    }

    public void setCloneId(int cloneId) {
        this.cloneId = cloneId;
    }

    public boolean isFlushFollow() {
        return flushFollow;
    }

    public void setFlushFollow(boolean flushFollow) {
        this.flushFollow = flushFollow;
    }

    public long getFightId() {
        return fightId;
    }

    public void setFightId(long fightId) {
        this.fightId = fightId;
    }
    
    
    @Override
    public String toString() {
        return "BossData{" + "configId=" + configId + ", bornTime=" + bornTime + ", dieTimes=" + dieTimes + ", reBornBaseTime=" + reBornBaseTime + ", rebornTime=" + rebornTime + ", killer=" + killer + ", dieNum=" + dieNum + ", maxNum=" + maxNum + '}';
    }
}
