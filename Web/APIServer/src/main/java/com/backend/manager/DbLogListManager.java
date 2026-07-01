package com.backend.manager;

import com.backend.bean.Server;
import com.backend.struct.ServerType;
import org.apache.log4j.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DbLogListManager {

    private final static Logger log = Logger.getLogger(DbLogListManager.class);

    private enum Singleton {

        INSTANCE;
        DbLogListManager manager;

        Singleton() {
            this.manager = new DbLogListManager();
        }

        DbLogListManager getProcessor() {
            return manager;
        }
    }

    public static DbLogListManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private Map<Integer, Server> gameDbList = new ConcurrentHashMap<>();

    private Map<Integer, Server> dblogList = new ConcurrentHashMap<>();

    private Map<String, List<Server>> serverDBMap = new ConcurrentHashMap<>();

    private Dao dao;

    public void init(Dao dao) {
        log.info("初始化服务器日志库信息...");
        this.dao = dao;
        reloadAll();
    }

    public void reloadAll() {
        loadDBServer();
    }

    private void loadDBServer() {
        gameDbList.clear();
        dblogList.clear();
        serverDBMap.clear();

        List<Server> gameDbs = dao.query(Server.class, Cnd.where("isDeleted", "=", 0)
                .and("serverType", "<", 2));
        gameDbs.forEach(n -> gameDbList.put(n.getServerId(), n));

        List<Server> dblogs = dao.query(Server.class, Cnd.where("isDeleted", "=", 0)
                .and("serverType", "<", 2));
        for (Server dblog : dblogs) {
            if (!serverDBMap.containsKey(dblog.getGroupName())) {
                serverDBMap.put(dblog.getGroupName(), new ArrayList<>());
            }
            dblogList.put(dblog.getServerId(), dblog);
            serverDBMap.get(dblog.getGroupName()).add(dblog);
        }
        log.info("游戏服dblog信息加载完成,共" + serverDBMap.size() + "条数据");
    }

    public Set<String> getPlatformDBNames() {
        return serverDBMap.keySet();
    }

    public List<Server> getServerDBs(String groupName) {
        List<Server> dblogList = serverDBMap.getOrDefault(groupName, null);
        Collections.sort(dblogList);
        return dblogList;
    }

    public List<Server> getNoHeFuServerDBs(String groupName) {
        return serverDBMap.getOrDefault(groupName, new ArrayList<>())
                .stream().filter(n -> n.getIsHeFu() == 0).sorted().collect(Collectors.toList());
    }

    public Server getDBServer(String groupName, int serverId) {
        List<Server> dbLogs = serverDBMap.get(groupName);
        if (dbLogs != null) {
            for (Server dblog : dbLogs) {
                if (dblog.getServerId() == serverId) {
                    return dblog;
                }
            }
        }
        return null;
    }

    public Server getDblog(int serverId) {
        return dblogList.get(serverId);
    }

    public Server getDblog(String serverId) {
        Integer id = Integer.parseInt(serverId);
        return dblogList.get(id);
    }

    public Server getGameDb(int serverId) {
        return gameDbList.get(serverId);
    }

    public Server getGameDb(String serverId) {
        Integer id = Integer.parseInt(serverId);
        return gameDbList.get(id);
    }

    public List<Server> getGameDbList() {
        List<Server> dblogList = new ArrayList<>(gameDbList.values());
        Collections.sort(dblogList);
        return dblogList;
    }

    public List<Server> getDblogList() {
        List<Server> list = new ArrayList<>(dblogList.values());
        Collections.sort(list);
        return list;
    }

    public void updateDBlog(Server dblog) {
        switch (dblog.getServerType()) {
            case ServerType.Test:
            case ServerType.Official:
                reloadAll();
                break;
            case ServerType.Login:
                LoginServerManager.getInstance().loadAll();
            case ServerType.Public:
            case ServerType.Fight:
                CrossManager.getInstance().loadDBs();
        }
    }

}
