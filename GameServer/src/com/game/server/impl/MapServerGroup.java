package com.game.server.impl;

import game.core.thread.TimerThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 地图服务器管理组
 */
public abstract class MapServerGroup {
    private static final Logger log = LogManager.getLogger(MapServerGroup.class);

    //最小地图线程数量
    protected int minMapThreads;
    //最大地图线程数量
    protected int maxMapThreads;
    //最小地图承载量
    protected int midPerMapThreads;

    //地图所在的地图服务器
    protected ConcurrentHashMap<Long, MapServer> mapServers = new ConcurrentHashMap<>();

    //地图服务器组
    protected Set<MapServer> serverCache = new HashSet<>();

    //线程组
    protected ThreadGroup group;
    //计时器
    protected TimerThread timerThread;
    //线程名字
    protected String name;

    protected MapServerGroup(ThreadGroup group, String name, TimerThread timerThread, int minservers, int maxServes, int minPerServer) {
        this.group = group;
        this.maxMapThreads = maxServes;
        this.midPerMapThreads = minPerServer;
        this.minMapThreads = minservers;
        this.name = name;
        this.timerThread = timerThread;
    }

    /**
     * 往线程中添加地图
     *
     * @param id
     * @return
     */
    public MapServer addMapServer(long id) {
        MapServer mapServer = mapServers.get(id);
        if (mapServer != null) {
            log.error(id + "已经存在,开始加入地图线程" + mapServer.getName() + ", 地图数量：" + mapServer.getNum());
            return mapServer;
        }

        boolean isFull = true;
        int minMap = Integer.MAX_VALUE;

        Iterator<MapServer> iter = serverCache.iterator();
        int maxNo = 0;
        while (iter.hasNext()) {
            MapServer ss = iter.next();
            if (ss.getServerIndex() > maxNo) {
                maxNo = ss.getServerIndex();
            }

            if (ss.getNum() < midPerMapThreads) {
                mapServer = ss;
                isFull = false;
                break;
            }

            if (ss.getNum() < minMap) {
                mapServer = ss;
                minMap = ss.getNum();
            }
        }

        if (!isFull) {
            log.info(id + "开始加入地图线程" + mapServer.getName() + ", 地图数量：" + mapServer.getNum());
            mapServers.put(id, mapServer);
        } else if (serverCache.size() < maxMapThreads) {
            mapServer = createMapServer(group, name, maxNo + 1);
            mapServer.start();
            mapServers.put(id, mapServer);
            serverCache.add(mapServer);
            log.info(id + "开始加入新增加地图线程" + mapServer.getName() + ", 地图数量：" + mapServer.getNum());
        } else {
            mapServers.put(id, mapServer);
            log.info(id + "开始加入地图线程" + mapServer.getName() + ", 地图数量：" + mapServer.getNum());
        }

        return mapServer;
    }

    /**
     * 移除地图
     *
     * @param mapId
     * @return
     */
    public MapServer removeMap(long mapId) {
        MapServer ms = mapServers.get(mapId);
        if (ms == null)
            return null;

        log.info(mapId + "地图消毁开始！");

        mapServers.remove(mapId);
        return ms;
    }

    /**
     * 获取地图服务线程
     *
     * @param mapId
     * @return
     */
    public MapServer getMapServer(long mapId) {
        return mapServers.get(mapId);
    }

    /**
     * 创建地图服务器
     *
     * @param group
     * @param name
     * @return
     */
    protected abstract MapServer createMapServer(ThreadGroup group, String name, int index);


    //线程的数量
    public int size() {
        return serverCache.size();
    }

    //地图的数量
    public int mapSize() {
        int size = 0;
        for (MapServer ms : serverCache) {
            size += ms.getNum();
        }
        return size;
    }

    public void stop() {
        for (MapServer ms : serverCache) {
            ms.stop(true);
        }
    }

    public ConcurrentHashMap<Long, MapServer> getMapServers() {
        return mapServers;
    }

    public void removeMapServer(MapServer ms) {
        log.info(ms.getName() + "地图数量=" + ms.getNum() + "！");
        if (ms.getNum() <= 0) {
            if (serverCache.size() > minMapThreads) {
                serverCache.remove(ms);
                ms.stop(true);
            }
        }
    }

}
