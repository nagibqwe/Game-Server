package com.game.guildbattle.structs;

import com.game.copymap.structs.ExpNoteData;
import com.game.copymap.structs.ZoneCache;
import com.game.monster.structs.Monster;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Description
 * @auther lw
 * @create 2020-02-13 15:48
 */
public class GuildBattleMapData extends ExpNoteData {

    /**
     * 上古怪物
     */
    private Monster monster;

    /**
     * 当前轮数
     */
    private int curRound;

    /**
     * 世界等级
     */
    private int globalLv;

    /**
     * 仙盟评级等级
     */
    private int lv;

    /**
     * 不同建筑血量
     */
    private HashMap<Integer, GuildBattleBuild> buildInfos = new HashMap<>();

    /**
     * 仙盟数据
     */
    ConcurrentHashMap<Long, GuildBattleData> guild = new ConcurrentHashMap<>();

    /**
     * 巅峰竞技经验统计
     */
    ConcurrentHashMap<Long, Long> expNote = new ConcurrentHashMap<>();

    /**
     * 上一次上古意志归属公告
     */
    long lastNotifyAuth;

    /**
     * 当前
     */
    private int curType;

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public int getCurRound() {
        return curRound;
    }

    public void setCurRound(int curRound) {
        this.curRound = curRound;
    }

    public int getGlobalLv() {
        return globalLv;
    }

    public void setGlobalLv(int globalLv) {
        this.globalLv = globalLv;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public HashMap<Integer, GuildBattleBuild> getBuildInfos() {
        return buildInfos;
    }

    public void setBuildInfos(HashMap<Integer, GuildBattleBuild> buildInfos) {
        this.buildInfos = buildInfos;
    }

    public int getCurType() {
        return curType;
    }

    public void setCurType(int curType) {
        this.curType = curType;
    }

    public ConcurrentHashMap<Long, GuildBattleData> getGuild() {
        return guild;
    }

    public void setGuild(ConcurrentHashMap<Long, GuildBattleData> guild) {
        this.guild = guild;
    }

    public ConcurrentHashMap<Long, Long> getExpNote() {
        return expNote;
    }

    public void setExpNote(ConcurrentHashMap<Long, Long> expNote) {
        this.expNote = expNote;
    }

    public long getLastNotifyAuth() {
        return lastNotifyAuth;
    }

    public void setLastNotifyAuth(long lastNotifyAuth) {
        this.lastNotifyAuth = lastNotifyAuth;
    }
}
