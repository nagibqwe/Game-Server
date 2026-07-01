package com.game.server.impl;

import game.core.thread.TimerThread;

/**
 * 地图服务器管理组
 */
public class GameMapServerGroup extends MapServerGroup {


    private static final int minServers = 2;
    private static final int MaxServers = 24;


    public GameMapServerGroup(ThreadGroup group, String name, TimerThread timerThread, int mapMinNum) {
        super(group, name, timerThread, minServers, MaxServers, mapMinNum);
    }

    public GameMapServerGroup(ThreadGroup group, String name, TimerThread timerThread, int min, int max, int mapMin) {
        super(group, name, timerThread, min, max, mapMin);
    }

    @Override
    protected MapServer createMapServer(ThreadGroup group, String name, int index) {
        return new LogicServer(group, name, timerThread, index);
    }


    @Override
    public void stop() {
        super.stop();
    }

}
