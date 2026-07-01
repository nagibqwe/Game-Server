package com.game.drop.structs;

/**
 * 次数特殊掉落（针对boss）
 */
public class BossRelationDropLimit {
    /**
     * 随机排名奖励次数，用来发特殊奖励
     */
    private int randomKillCount;

    /**
     * 已经获得该boss的排名奖励次数
     */
    private int rankDropCount;

    ///////////////////////getter and setter///////////////////////////////////////////////

    /**
     * 获取 随机排名奖励次数，用来发特殊奖励
     *
     * @return randomKillCount 随机排名奖励次数，用来发特殊奖励
     */
    public int getRandomKillCount() {
        return this.randomKillCount;
    }

    /**
     * 设置 随机排名奖励次数，用来发特殊奖励
     *
     * @param randomKillCount 随机排名奖励次数，用来发特殊奖励
     */
    public void setRandomKillCount(int randomKillCount) {
        this.randomKillCount = randomKillCount;
    }

    /**
     * 获取 已经获得该boss的排名奖励次数
     *
     * @return rankDropCount 已经获得该boss的排名奖励次数
     */
    public int getRankDropCount() {
        return this.rankDropCount;
    }

    /**
     * 设置 已经获得该boss的排名奖励次数
     *
     * @param rankDropCount 已经获得该boss的排名奖励次数
     */
    public void setRankDropCount(int rankDropCount) {
        this.rankDropCount = rankDropCount;
    }
}
