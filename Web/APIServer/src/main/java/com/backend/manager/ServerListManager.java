package com.backend.manager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.backend.struct.ServerType;
import org.apache.log4j.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import com.backend.bean.Server;

/**
 * 服务器列表管理器
 */
public class ServerListManager {

    private final static Logger log = Logger.getLogger(ServerListManager.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        ServerListManager manager;

        Singleton() {
            this.manager = new ServerListManager();
        }

        ServerListManager getProcessor() {
            return manager;
        }
    }

    public static ServerListManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 所有游戏服务器连接信息
     */
    private Map<Integer, Server> allServerMap = new ConcurrentHashMap<>();
    /**
     * 平台相关所有未被合服的游戏服务器连接信息
     */
    private Map<String, List<Server>> noHeFuServerListMap = new ConcurrentHashMap<>();
    /**
     * 平台相关未合服正式游戏服务器连接信息
     */
    private Map<String, List<Server>> officialServerListMap = new ConcurrentHashMap<>();
    /**
     * 平台相关未合服测试游戏服务器连接信息
     */
    private Map<String, List<Server>> testServerListMap = new ConcurrentHashMap<>();

    private Dao dao;

    public void init(Dao dao) {
        log.info("初始化服务器相关信息————————————————————————————————");
        this.dao = dao;
        load();
    }

    public void load() {
        allServerMap.clear();
        officialServerListMap.clear();
        testServerListMap.clear();
        noHeFuServerListMap.clear();

        List<Server> list = dao.query(Server.class, Cnd.where("serverType", "<", "2")
                .and("isDeleted", "=", 0));
        if (list.isEmpty()) {
            log.error("游戏服务器相关信息加载失败！请检查GM后台数据库中的t_server表!");
            return;
        }
        List<Server> noHefuServerList;
        for (Server server : list) {
            allServerMap.put(server.getServerId(), server);
            if (server.getIsHeFu() == 0) {
                if (noHeFuServerListMap.containsKey(server.getGroupName())) {
                    noHefuServerList = noHeFuServerListMap.get(server.getGroupName());
                } else {
                    noHefuServerList = new ArrayList<>();
                    noHeFuServerListMap.put(server.getGroupName(), noHefuServerList);
                }
                noHefuServerList.add(server);


                Map<String, List<Server>> serverMap;
                if (server.getServerType() == 1) {
                    serverMap = officialServerListMap;
                } else {
                    serverMap = testServerListMap;
                }
                List<Server> servers;
                if (serverMap.containsKey(server.getGroupName())) {
                    servers = serverMap.get(server.getGroupName());
                } else {
                    servers = new ArrayList<>();
                    serverMap.put(server.getGroupName(), servers);
                }
                servers.add(server);
            }

        }
        log.info("所有游戏服务器连接信息加载完成,共" + allServerMap.size() + "条数据");
        log.info("平台相关测试游戏服务器连接信息加载完成,共" + testServerListMap.size() + "条数据");
        log.info("平台相关正式游戏服务器连接信息加载完成,共" + officialServerListMap.size() + "条数据");

        List<Server> fightList = dao.query(Server.class, Cnd.where("serverType", "=", 4)
                .and("isDeleted", "=", 0));
        if (fightList.isEmpty()) {
            log.error("战斗服务器相关信息加载失败！请检查GM后台数据库中的t_server表!");
            return;
        }
        for (Server server : fightList) {
            allServerMap.put(server.getServerId(), server);
        }
        log.info("所有游戏服务器（包括战斗服）连接信息加载完成,共" + allServerMap.size() + "条数据");
    }

    public Server getServer(int key) {
        if (allServerMap.containsKey(key)) {
            return allServerMap.get(key);
        }
        return dao.fetch(Server.class, Cnd.where("serverId", "=", key)
                .and("isDeleted", "=", 0));
    }

    public Server getServer(String key) {
        int sid = Integer.parseInt(key);
        return getServer(sid);
    }

    public Set<String> getPlatformNames() {
        return noHeFuServerListMap.keySet();
    }

    public Map<String, List<Server>> getOfficialServerMap() {
        return officialServerListMap;
    }

    public Map<String, List<Server>> getNoHefuServerListMap() {
        return noHeFuServerListMap;
    }

    public Map<String, List<Server>> getTestServerListMap() {
        return testServerListMap;
    }

    public List<Server> getNoHeFuServerListByGroup(String groupName) {
        if (noHeFuServerListMap.containsKey(groupName)) {
            return noHeFuServerListMap.get(groupName);
        }
        return new ArrayList<>();
    }

    public List<Server> getServers(String groupName) {
        return allServerMap.values().stream()
                .filter(n -> n.getGroupName().equals(groupName)
                        && n.getServerType() < 2
                ).collect(Collectors.toList());
    }

    public void updateServer(Server server) {
        switch (server.getServerType()) {
            case ServerType.Test:
            case ServerType.Official:
                load();
                break;
            case ServerType.Login:
                LoginServerManager.getInstance().loadAll();
                break;
            case ServerType.Public:
            case ServerType.Fight:
                CrossManager.getInstance().loadServer();
                break;
        }
    }

}
