package com.game.boss.struct;

/**
 * 世界Boss存库数据信息
 */
public class BossData {
    
    private int bossId;           //bossId
    
    private long bornTime;        //boss出生刷新时间点
    
    private int dieNum;           //首次出生刷新后当前被击杀死亡次数
    
    private int reBornBaseTime;   //重生基数时间，单位：秒
    
    private long rebornTime;      //重生时间点(记录下来主要用于服务器重启加载boss后设置其下次刷新时间)

    public long getMapUid() {
        return mapUid;
    }

    public void setMapUid(long mapUid) {
        this.mapUid = mapUid;
    }

    private long mapUid = 0;  //所在地图唯一ID

    public BossData() {}

    public BossData(int bossId, long bornTime, int dieNum, int reBornBaseTime, long rebornTime) {
        this.bossId = bossId;
        this.bornTime = bornTime;
        this.dieNum = dieNum;
        this.reBornBaseTime = reBornBaseTime;
        this.rebornTime = rebornTime;
    }

    public int getBossId() {
        return bossId;
    }

    public void setBossId(int bossId) {
        this.bossId = bossId;
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
 
}
