package com.game.guildbattle.structs;

/**
 * @Description
 * @auther lw
 * @create 2020-03-03 15:02
 */
public class GuildBattleBuild {

    private int buildId;

    private int hp;

    private long beginTime;

    private long startRepair;   //开始修复时间

    public int getBuildId() {
        return buildId;
    }

    public void setBuildId(int buildId) {
        this.buildId = buildId;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getStartRepair() {
        return startRepair;
    }

    public void setStartRepair(long startRepair) {
        this.startRepair = startRepair;
    }
}
