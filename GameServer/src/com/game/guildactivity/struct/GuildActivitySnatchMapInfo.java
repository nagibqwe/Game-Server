package com.game.guildactivity.struct;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GuildActivitySnatchMapInfo {
    /**
     * 当前环数
     */
    private int loop = 0;

    /**
     * 最大环数
     */
    private int maxLoop;

    /**
     * 怪物全部死完的时间
     */
    private long killAllTime;

    /**
     * 是否结束刷怪
     */
    private boolean end;

    /**
     * 宗派的奖励倍数
     */
    private ConcurrentHashMap<Long, Integer> rewType = new ConcurrentHashMap<>();

    /**
     * 玩家伤害
     */
    private ConcurrentHashMap<Long, GuildActivitySnatchRank> playerHarm = new ConcurrentHashMap<>();

    /**
     * 伤害排行
     */
    private List<GuildActivitySnatchRank> ranks = new ArrayList<>();

    /**
     * 面板已经初始化过的玩家
     */
    private ConcurrentLinkedQueue<Long> panelReady = new ConcurrentLinkedQueue<>();

    /**
     * 获取 玩家伤害
     *
     * @return playerHarm 玩家伤害
     */
    public ConcurrentHashMap<Long, GuildActivitySnatchRank> getPlayerHarm() {
        return this.playerHarm;
    }

    /**
     * 设置 玩家伤害
     *
     * @param playerHarm 玩家伤害
     */
    public void setPlayerHarm(ConcurrentHashMap<Long, GuildActivitySnatchRank> playerHarm) {
        this.playerHarm = playerHarm;
    }

    /**
     * 获取 伤害排行
     *
     * @return ranks 伤害排行
     */
    public List<GuildActivitySnatchRank> getRanks() {
        return this.ranks;
    }

    /**
     * 设置 伤害排行
     *
     * @param ranks 伤害排行
     */
    public void setRanks(List<GuildActivitySnatchRank> ranks) {
        this.ranks = ranks;
    }

    /**
     * 获取 当前环数
     *
     * @return loop 当前环数
     */
    public int getLoop() {
        return this.loop;
    }

    /**
     * 设置 当前环数
     *
     * @param loop 当前环数
     */
    public void setLoop(int loop) {
        this.loop = loop;
    }

    /**
     * 获取 怪物全部死完的时间
     *
     * @return killAllTime 怪物全部死完的时间
     */
    public long getKillAllTime() {
        return this.killAllTime;
    }

    /**
     * 设置 怪物全部死完的时间
     *
     * @param killAllTime 怪物全部死完的时间
     */
    public void setKillAllTime(long killAllTime) {
        this.killAllTime = killAllTime;
    }

    /**
     * 获取 是否结束刷怪
     *
     * @return end 是否结束刷怪
     */
    public boolean isEnd() {
        return this.end;
    }

    /**
     * 设置 是否结束刷怪
     *
     * @param end 是否结束刷怪
     */
    public void setEnd(boolean end) {
        this.end = end;
    }

    /**
     * 获取 宗派的奖励倍数
     *
     * @return rewType 宗派的奖励倍数
     */
    public ConcurrentHashMap<Long, Integer> getRewType() {
        return this.rewType;
    }

    /**
     * 设置 宗派的奖励倍数
     *
     * @param rewType 宗派的奖励倍数
     */
    public void setRewType(ConcurrentHashMap<Long, Integer> rewType) {
        this.rewType = rewType;
    }

    /**
     * 获取 面板已经初始化过的玩家
     *
     * @return panelReady 面板已经初始化过的玩家
     */
    public ConcurrentLinkedQueue<Long> getPanelReady() {
        return this.panelReady;
    }

    /**
     * 设置 面板已经初始化过的玩家
     *
     * @param panelReady 面板已经初始化过的玩家
     */
    public void setPanelReady(ConcurrentLinkedQueue<Long> panelReady) {
        this.panelReady = panelReady;
    }

    /**
     * 获取 最大环数
     *
     * @return maxLoop 最大环数
     */
    public int getMaxLoop() {
        return this.maxLoop;
    }

    /**
     * 设置 最大环数
     *
     * @param maxLoop 最大环数
     */
    public void setMaxLoop(int maxLoop) {
        this.maxLoop = maxLoop;
    }
}
