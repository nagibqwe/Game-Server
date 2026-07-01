package com.game.buff.structs;

import java.io.Serializable;

public class Buff implements Serializable {

    private int buffId;
    private long sourceId; //添加者
    private int overlap = 1; //重叠次数
    private long passTime;  //buff使用时间
    private long start; //开始计时时间
    private int trigger; //触发次数
    private boolean delete; //可删除标志

    //额外参数
    private int par1;
    //额外参数
    private int par2;
    //额外参数
    private int par3;
    //额外参数
    private int par4;

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public long getPassTime() {
        return passTime;
    }

    public void setPassTime(long passTime) {
        this.passTime = passTime;
    }

    public int getBuffId() {
        return buffId;
    }

    public void setBuffId(int buffId) {
        this.buffId = buffId;
    }

    public int getPar1() {
        return par1;
    }

    public void setPar1(int par1) {
        this.par1 = par1;
    }

    public int getPar2() {
        return par2;
    }

    public void setPar2(int par2) {
        this.par2 = par2;
    }

    public int getPar3() {
        return par3;
    }

    public void setPar3(int par3) {
        this.par3 = par3;
    }

    public int getPar4() {
        return par4;
    }

    public void setPar4(int par4) {
        this.par4 = par4;
    }

    public int getOverlap() {
        return overlap;
    }

    public void setOverlap(int overlap) {
        this.overlap = overlap;
    }

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public int getTrigger() {
        return trigger;
    }

    public void setTrigger(int trigger) {
        this.trigger = trigger;
    }

}
