package com.game.guildcrossfud.struct;

/**
 * @Desc TODO
 * @Date 2021/2/2 15:10
 * @Auth ZUncle
 */
public class FudBoss  {
    int bossId;             //bossID
    long hp;                //血量
    long refreshTime;       //刷新时间
    int kill;               //击杀

    public int getKill() {
        return kill;
    }

    public void setKill(int kill) {
        this.kill = kill;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }

    public int getBossId() {
        return bossId;
    }

    public void setBossId(int bossId) {
        this.bossId = bossId;
    }

    public long getHp() {
        return hp;
    }

    public void setHp(long hp) {
        this.hp = hp;
    }
}
