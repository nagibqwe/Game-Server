package com.backend.manager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.backend.struct.ServerType;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
     * 所有平台的信息
     */
    private Map<String, String> allGroupListMap = new ConcurrentHashMap<>();

    /**
     * 所有(测试和正式)游戏服务器连接信息(0:测试服1:正式服)包括合服的服务器
     */
    private Map<Integer, Server> serverMap = new ConcurrentHashMap<>();

    /**
     * 游戏服务器连接信息(0:测试服1:正式服)不包括合服的服务器
     */
    private Map<Integer, Server> serverNoHeFuMap = new ConcurrentHashMap<>();

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
        log.info("Инициализация информации о сервере ————————————————————————————————");
        this.dao = dao;
        load();
    }

    public void load() {
        allGroupListMap.clear();
        allServerMap.clear();
        officialServerListMap.clear();
        testServerListMap.clear();
        noHeFuServerListMap.clear();

        List<Server> list = dao.query(Server.class, Cnd.where("serverType", "<", "2")
                .and("isDeleted", "=", 0));
        if (list.isEmpty()) {
            log.error("Ошибка загрузки информации об игровых серверах! Проверьте таблицу t_server в базе данных GM-бэкенда!");
            return;
        }
        List<Server> noHefuServerList;
        for (Server server : list) {
            allGroupListMap.put(server.getGroupName(),server.getGroupName());
            serverMap.put(server.getServerId(), server);
            allServerMap.put(server.getServerId(), server);
            if (server.getIsHeFu() == 0) {
                serverNoHeFuMap.put(server.getServerId(), server);
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
        log.info("Загрузка информации по всем платформам завершена, всего записей: " + allGroupListMap.size());
        log.info("Загрузка информации об игровых серверах (тестовые и боевые, включая объединённые) завершена, всего записей: " + serverMap.size());
        log.info("Загрузка информации об игровых серверах (тестовые и боевые, без учёта объединённых) завершена, всего записей: " + serverNoHeFuMap.size());
        log.info("Загрузка информации о подключениях всех игровых серверов завершена, всего записей: " + allServerMap.size());
        log.info("Загрузка информации о подключениях тестовых игровых серверов (платформа) завершена, всего записей: " + testServerListMap.size());
        log.info("Загрузка информации о подключениях боевых игровых серверов (платформа) завершена, всего записей: " + officialServerListMap.size());

        List<Server> fightList = dao.query(Server.class, Cnd.where("serverType", "=", 4)
                .and("isDeleted", "=", 0));
        if (fightList.isEmpty()) {
            log.error("(TzjBackEnd) Ошибка загрузки информации о боевых серверах! Проверьте таблицу t_server в базе данных GM-бэкенда!");
            return;
        }
        for (Server server : fightList) {
            allServerMap.put(server.getServerId(), server);
        }
        log.info("(TzjBackEnd) Загрузка информации о подключениях всех игровых серверов (включая боевые) завершена, всего записей: " + allServerMap.size());
    }
    /**
     * 获取平台标识map
     *
     * @return
     */
    public List<String> getGroupList() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : allGroupListMap.entrySet()) {
            String groupKey = entry.getKey();
            list.add(groupKey);
        }
        return list;
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
    /**
     * 获取平台标识与服务器的关联(0：测试服 1：正式服)包括合服的服务器
     * @param GroupList
     * @return
     */
    public JSON getGroupServer(List<String> GroupList,int isContainHeFu) {
        if (null == GroupList)
            return null;
        List<HashMap<String, Object>> list;
        if (isContainHeFu == 1){
            list=getList(allGroupListMap,serverMap);
        }else {
            list=getList(allGroupListMap,serverNoHeFuMap);
        }
        JSONArray json = JSONArray.fromObject(list);
        return (JSON)json;
    }
    private List<HashMap<String, Object>> getList(Map<String, String> allGroupListMap,Map<Integer, Server> servers){
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (Map.Entry<String, String> entry : allGroupListMap.entrySet()) {
            String groupValue = entry.getValue();
            String groupKey = entry.getKey();

            List<HashMap<String, Object>> integerList = new ArrayList<HashMap<String, Object>>();
            int count = 0;
            for (Map.Entry<Integer, Server> serverEntry : servers.entrySet()) {
                Server server = serverEntry.getValue();
                if (!server.getGroupName().equals(groupKey))
                    continue;

                count++;
                HashMap<String, Object> rs  =  new HashMap<String, Object>();
                rs.put("value", serverEntry.getKey());
                rs.put("text", server.getServerName() + "("+server.getServerId()+")");
                integerList.add(rs);

                Collections.sort(integerList,new Comparator<HashMap<String, Object>>() {
                    @Override
                    public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                        if(o1 == null || o2 == null){
                            return 0;
                        }
                        if(Integer.parseInt(o1.get("value").toString()) > Integer.parseInt(o2.get("value").toString())){
                            return 1;
                        }
                        if(Integer.parseInt(o1.get("value").toString()) < Integer.parseInt(o2.get("value").toString())){
                            return -1;
                        }
                        return 0;
                    }
                });
            }
            // 该平台下还没有对应的服务器
            if (count == 0)
                continue;

            HashMap<String, Object> rs  =  new HashMap<String, Object>();
            rs.put("groupKey", groupKey);

            // 直接拼装平台标识组
            HashMap<String, Object> ch_rs  =  new HashMap<String, Object>();
            ch_rs.put("value", groupKey);
            ch_rs.put("text", groupValue);
            rs.put("GroupName", ch_rs);

            rs.put("serverIDList", integerList);
            list.add(rs);
        }

        return list;

    }
}
