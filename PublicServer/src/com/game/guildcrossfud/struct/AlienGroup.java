package com.game.guildcrossfud.struct;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO
 * @Date 2021/11/16 15:12
 * @Auth ZUncle
 */
public class AlienGroup {

    /**
     * 虚空列表
     */
    ConcurrentHashMap<Integer, AlienCity> alien = new ConcurrentHashMap<>();

    /**
     * 玩家进入记录
     * @return
     */
    ConcurrentHashMap<Long, Integer> alienSelect = new ConcurrentHashMap<>();


    public ConcurrentHashMap<Long, Integer> getAlienSelect() {
        return alienSelect;
    }

    public void setAlienSelect(ConcurrentHashMap<Long, Integer> alienSelect) {
        this.alienSelect = alienSelect;
    }

    public ConcurrentHashMap<Integer, AlienCity> getAlien() {
        return alien;
    }

    public void setAlien(ConcurrentHashMap<Integer, AlienCity> alien) {
        this.alien = alien;
    }
}
