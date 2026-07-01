package com.game.guildactivity.struct;

/**
 * @Desc TODO
 * @Date 2021/3/15 20:38
 * @Auth ZUncle
 */
public class GuildFudBoss {
    //福地bossID
    int bossId;
    //对应怪物ID
    int monsterId;
    //刷新时间
    long birthTime;
    //是否广播刷新
    boolean notify;

    public int getBossId() {
        return bossId;
    }

    public void setBossId(int bossId) {
        this.bossId = bossId;
    }

    public int getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(int monsterId) {
        this.monsterId = monsterId;
    }

    public long getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(long birthTime) {
        this.birthTime = birthTime;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    @Override
    public String toString() {
        return "GuildFudBoss{" +
                "bossId=" + bossId +
                ", monsterId=" + monsterId +
                '}';
    }
}
