package com.backend.manager;

import com.backend.bean.Server;
import com.backend.struct.ServerType;
import org.apache.log4j.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 跨服公共服务器相关信息
 */
public class CrossManager {

    private final static Logger log = Logger.getLogger(CrossManager.class);

    private enum Singleton {

        INSTANCE;
        CrossManager manager;

        Singleton() {
            this.manager = new CrossManager();
        }

        CrossManager getProcessor() {
            return manager;
        }
    }

    public static CrossManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    //世界服、跨服DBLOG
    private Map<Integer, Server> dblogMap = new ConcurrentHashMap<>();

    //世界服server
    private Map<String, List<Server>> psMap = new ConcurrentHashMap<>();
    //世界服db
    private Map<String, List<Server>> psDBMap = new ConcurrentHashMap<>();
    //跨服server
    private Map<String, List<Server>> fsMap = new ConcurrentHashMap<>();
    //跨服db
    private Map<String, List<Server>> fsDBMap = new ConcurrentHashMap<>();

    private Dao dao;

    public void init(Dao dao) {
        log.info("初始化跨服相关信息————————————————————————————————");
        this.dao = dao;
        reloadAll();
    }

    public void reloadAll(){
        loadDBs();
        loadServer();
    }

    public void loadDBs() {
        loadDB(ServerType.Public, psDBMap);
        loadDB(ServerType.Fight, fsDBMap);
    }

    public void loadServer() {
        loadServer(ServerType.Public, psMap);
        loadServer(ServerType.Fight, fsMap);
    }

    private void loadServer(int serverType, Map<String, List<Server>> map) {
        map.clear();
        List<String> groupList = getGroupInfo("t_server", serverType);
        List<Server> serverList;
        for (String groupName : groupList) {
            serverList = getSeverByGroupName(groupName, serverType);
            if (serverList == null) {
                serverList = new ArrayList<>();
            }
            map.put(groupName, serverList);
        }
        log.info("t_server" + "表中serverType=" + serverType + "的信息加载完成,共" + map.size() + "条数据");
}

    private void loadDB(int serverType, Map<String, List<Server>> map) {
        map.clear();
        List<String> groupList = getGroupInfo("t_server", serverType);
        List<Server> serverDBList;
        for (String groupName : groupList) {
            serverDBList = getSeverDBByGroupName(groupName, serverType);
            if (serverDBList == null) {
                serverDBList = new ArrayList<>();
            }
            map.put(groupName, serverDBList);
            for (Server dblog : serverDBList) {
                dblogMap.put(dblog.getServerId(), dblog);
            }
        }
        log.info("t_dblog" + "表中serverType=" + serverType + "的信息加载完成,共" + map.size() + "条数据");
    }

    private List<String> getGroupInfo(String table, int serverType) {
        Sql sql = Sqls.create("SELECT groupName FROM $table where serverType=@serverType and isDeleted=0 group by groupName");
        sql.vars().set("table", table);
        sql.params().set("serverType", serverType);
        sql.setCallback((Connection conn, ResultSet rs, Sql sql1) -> {
            List<String> list = new LinkedList<>();
            while (rs.next()) {
                list.add(rs.getString("groupName"));
            }
            return list;
        });
        dao.execute(sql);
        return sql.getList(String.class);
    }

    private List<Server> getSeverByGroupName(String groupName, int serverType) {
        return dao.query(Server.class, Cnd.where("groupName", "=", groupName)
                .and("serverType", "=", serverType)
                .and("isDeleted", "=", 0)
                .asc("serverId"));
    }

    private List<Server> getSeverDBByGroupName(String groupName, int serverType) {
        List<Server> dblogList = dao.query(Server.class, Cnd.where("groupName", "=", groupName)
                .and("serverType", "=", serverType)
                .and("isDeleted", "=", 0)
                .asc("serverId"));
        Collections.sort(dblogList);
        return dblogList;
    }

    public Set<String> getServerGroupNames(int serverType) {
        switch (serverType) {
            case ServerType.Public:
                return psMap.keySet();
            case ServerType.Fight:
                return fsMap.keySet();
            default:
                return new HashSet<>();
        }
    }

    public Set<String> getDbGroupNames(int serverType) {
        switch (serverType) {
            case ServerType.Public:
                return psDBMap.keySet();
            case ServerType.Fight:
                return fsDBMap.keySet();
            default:
                return new HashSet<>();
        }
    }

    public List<Server> getServers(String groupName, int serverType) {
        switch (serverType) {
            case ServerType.Public:
                return psMap.get(groupName);
            case ServerType.Fight:
                return fsMap.get(groupName);
        }
        return new ArrayList<>();

    }

    public List<Server> getDBs(String groupName, int serverType) {
        switch (serverType) {
            case ServerType.Public:
                return psDBMap.get(groupName);
            case ServerType.Fight:
                return fsDBMap.get(groupName);
        }
        return new ArrayList<>();
    }

    public Server getDB(int serverId) {
        return dblogMap.get(serverId);
    }
}
