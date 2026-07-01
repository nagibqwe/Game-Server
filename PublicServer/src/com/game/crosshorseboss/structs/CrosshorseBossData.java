package com.game.crosshorseboss.structs;

/**
 * Created by cxl on 2021/4/14.
 */
public class CrosshorseBossData {

    //bossnew_HorseBoss 配置表ID
    private int configId;

    private int cloneId;//副本ID值

    private int monsterId;

    private boolean isDie;

    private int level;//层数

    private int reBornBaseTime;   //重生基数时间，单位：秒

    private long rebornTime;      //重生时间点(记录下来主要用于服务器重启加载boss后设置其下次刷新时间)

    private long roomId;//房间ID

    private int fightServerId;//战斗服 服务器ID

    private boolean flushFollow; //是否刷新



    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public int getCloneId() {
        return cloneId;
    }

    public void setCloneId(int cloneId) {
        this.cloneId = cloneId;
    }

    public int getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(int monsterId) {
        this.monsterId = monsterId;
    }

    public boolean isDie() {
        return isDie;
    }

    public void setDie(boolean die) {
        isDie = die;
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

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public int getFightServerId() {
        return fightServerId;
    }

    public void setFightServerId(int fightServerId) {
        this.fightServerId = fightServerId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isFlushFollow() {
        return flushFollow;
    }

    public void setFlushFollow(boolean flushFollow) {
        this.flushFollow = flushFollow;
    }
}
