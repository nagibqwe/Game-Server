package com.game.guildactivity.struct;

import com.game.guild.structs.Guild;

import java.util.HashMap;

/**
 * @Desc TODO
 * @Date 2021/3/15 20:58
 * @Auth ZUncle
 */
public class GuildFud {
    int rank;     //福地排名
    int cloneId;  //福地ID
    long mapId;   //福地副本
    Guild guild;  //占领仙盟
    HashMap<Integer, GuildFudBoss> boss = new HashMap<>();        //福地boss信息
    HashMap<Integer, Integer> mb = new HashMap<>();               //福地monsterId -> bossId
    HashMap<Integer, GuildFudBoss> waitBoss = new HashMap<>();    //等待刷新boss
    /**
     * 怪物剩余数量，<类型，数量>
     */
    HashMap<Integer, Integer> monsterNum = new HashMap<>();

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getCloneId() {
        return cloneId;
    }

    public void setCloneId(int cloneId) {
        this.cloneId = cloneId;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public HashMap<Integer, GuildFudBoss> getBoss() {
        return boss;
    }

    public void setBoss(HashMap<Integer, GuildFudBoss> boss) {
        this.boss = boss;
    }

    public HashMap<Integer, GuildFudBoss> getWaitBoss() {
        return waitBoss;
    }

    public void setWaitBoss(HashMap<Integer, GuildFudBoss> waitBoss) {
        this.waitBoss = waitBoss;
    }

    public HashMap<Integer, Integer> getMb() {
        return mb;
    }

    public void setMb(HashMap<Integer, Integer> mb) {
        this.mb = mb;
    }

    public HashMap<Integer, Integer> getMonsterNum() {
        return monsterNum;
    }

    public void setMonsterNum(HashMap<Integer, Integer> monsterNum) {
        this.monsterNum = monsterNum;
    }

    public GuildFudBoss findBoss(int monsterId) {
        if (!mb.containsKey(monsterId)) {
            return null;
        }
        return boss.get(mb.get(monsterId));
    }
}
