package com.game.copymap.structs;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO
 * @Date 2021/8/5 14:56
 * @Auth ZUncle
 */
public class ExpCopyData extends ZoneCache {
    /**
     * 获得星数
     */
    private int star;

    /**
     * 开始时间
     */
    private int startTime;

    /**
     * 结束时间
     */
    private int endTime;

    /**
     * 是否副本结束
     */
    private boolean isEnd;

    /**
     * 怪物等级
     */
    private int monsterLv;

    /**
     * 上次检查击杀数量
     */
    private int lastKillNum;

    /**
     * 击杀怪物数量
     */
    private int killNum;

    /**
     * 玩家进入时等级
     */
    private ConcurrentHashMap<Long, Integer> memberLevel = new ConcurrentHashMap<>();

    /**
     * 累计经验
     */
    private ConcurrentHashMap<Long, Long> totalExp = new ConcurrentHashMap<>();

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public int getMonsterLv() {
        return monsterLv;
    }

    public void setMonsterLv(int monsterLv) {
        this.monsterLv = monsterLv;
    }

    public int getLastKillNum() {
        return lastKillNum;
    }

    public void setLastKillNum(int lastKillNum) {
        this.lastKillNum = lastKillNum;
    }

    public int getKillNum() {
        return killNum;
    }

    public void setKillNum(int killNum) {
        this.killNum = killNum;
    }

    public ConcurrentHashMap<Long, Integer> getMemberLevel() {
        return memberLevel;
    }

    public ConcurrentHashMap<Long, Long> getTotalExp() {
        return totalExp;
    }
}
