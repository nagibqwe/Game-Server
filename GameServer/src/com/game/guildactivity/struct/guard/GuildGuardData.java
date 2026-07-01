package com.game.guildactivity.struct.guard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 守护宗派活动数据
 */
public class GuildGuardData {
    /**
     * 当前怪物刷新波数
     */
    private int progress;
    /**
     * 怪物刷新总波数
     */
    private int general;
    /**
     * 玩家伤害数据，用以排名
     */
    private ConcurrentHashMap<Long, GuildGuardRank> harms = new ConcurrentHashMap<>();
    /**
     * 排名数据
     */
    private List<GuildGuardRank> ranks = new ArrayList<>();
    /**
     * 怪物是否刷完
     */
    private boolean end;
    /**
     * 杀死上一波怪物的时间
     */
    private long killLastMonster;

    //**************************************方法****************************************************

    /**
     * 清理数据
     */
    public void clear() {
        progress = 0;
        general = 0;
        harms.clear();
        ranks.clear();
        end = false;
    }

    //*******************************getter and setter********************************************************

    /**
     * 获取 当前怪物刷新波数
     *
     * @return progress 当前怪物刷新波数
     */
    public int getProgress() {
        return this.progress;
    }

    /**
     * 设置 当前怪物刷新波数
     *
     * @param progress 当前怪物刷新波数
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * 获取 怪物刷新总波数
     *
     * @return general 怪物刷新总波数
     */
    public int getGeneral() {
        return this.general;
    }

    /**
     * 设置 怪物刷新总波数
     *
     * @param general 怪物刷新总波数
     */
    public void setGeneral(int general) {
        this.general = general;
    }

    /**
     * 获取 玩家伤害数据，用以排名
     *
     * @return harms 玩家伤害数据，用以排名
     */
    public ConcurrentHashMap<Long, GuildGuardRank> getHarms() {
        return this.harms;
    }

    /**
     * 设置 玩家伤害数据，用以排名
     *
     * @param harms 玩家伤害数据，用以排名
     */
    public void setHarms(ConcurrentHashMap<Long, GuildGuardRank> harms) {
        this.harms = harms;
    }

    /**
     * 获取 排名数据
     *
     * @return ranks 排名数据
     */
    public List<GuildGuardRank> getRanks() {
        return this.ranks;
    }

    /**
     * 设置 排名数据
     *
     * @param ranks 排名数据
     */
    public void setRanks(List<GuildGuardRank> ranks) {
        this.ranks = ranks;
    }

    /**
     * 获取 怪物是否刷完
     *
     * @return end 怪物是否刷完
     */
    public boolean isEnd() {
        return this.end;
    }

    /**
     * 设置 怪物是否刷完
     *
     * @param end 怪物是否刷完
     */
    public void setEnd(boolean end) {
        this.end = end;
    }

    /**
     * 获取 杀死上一波怪物的时间
     *
     * @return killLastMonster 杀死上一波怪物的时间
     */
    public long getKillLastMonster() {
        return this.killLastMonster;
    }

    /**
     * 设置 杀死上一波怪物的时间
     *
     * @param killLastMonster 杀死上一波怪物的时间
     */
    public void setKillLastMonster(long killLastMonster) {
        this.killLastMonster = killLastMonster;
    }
}
