package com.game.home.structs;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO
 * @Date 2021/9/27 17:40
 * @Auth ZUncle
 */
public class Home {

    /**
     * 房屋规模
     */
    int level = 1;
    /**
     * 聚宝盆
     */
    int tupLevel = 1;
    /**
     * 聚宝盆经验
     */
    long tupExp;
    /**
     * 装饰度
     */
    int decorate;

    public int getDecorate() {
        return decorate;
    }

    public void setDecorate(int decorate) {
        this.decorate = decorate;
    }

    ConcurrentHashMap<Integer, HomeTask> homeTask = new ConcurrentHashMap<>();

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTupLevel() {
        return tupLevel;
    }

    public void setTupLevel(int tupLevel) {
        this.tupLevel = tupLevel;
    }

    public long getTupExp() {
        return tupExp;
    }

    public void setTupExp(long tupExp) {
        this.tupExp = tupExp;
    }

    public ConcurrentHashMap<Integer, HomeTask> getHomeTask() {
        return homeTask;
    }

    public void setHomeTask(ConcurrentHashMap<Integer, HomeTask> homeTask) {
        this.homeTask = homeTask;
    }
}
