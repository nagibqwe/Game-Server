/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.soulanimalforest.structs;

/**
 * 跨服的BOSSDATA数据
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class CrossBossData {
    private int modelConfigId;//栏目模板
    private long nextTime;//下一次刷新的时间
    private int dieTime;//死亡的次数
    private int rebornTime;//下一次需要的时间， 用来计算再下一次发生的时间
    private long bornTime;//本次应该出生的时间
    private int dieNum;//死亡的数量
    private int maxNum;//存在总数量， 主要是用于通知游戏中界面的变更

    private long mapUid =0;//所在地图唯一ID

    public int getModelConfigId() {
        return modelConfigId;
    }

    public void setModelConfigId(int modelConfigId) {
        this.modelConfigId = modelConfigId;
    }

    public long getNextTime() {
        return nextTime;
    }

    public void setNextTime(long nextTime) {
        this.nextTime = nextTime;
    }

    public int getDieTime() {
        return dieTime;
    }

    public void setDieTime(int dieTime) {
        this.dieTime = dieTime;
    }

    public int getRebornTime() {
        return rebornTime;
    }

    public void setRebornTime(int rebornTime) {
        this.rebornTime = rebornTime;
    }

    public long getBornTime() {
        return bornTime;
    }

    public void setBornTime(long bornTime) {
        this.bornTime = bornTime;
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

    public long getMapUid() {
        return mapUid;
    }

    public void setMapUid(long mapUid) {
        this.mapUid = mapUid;
    }
}
