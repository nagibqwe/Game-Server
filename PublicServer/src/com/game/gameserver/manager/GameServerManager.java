package com.game.gameserver.manager;

import com.game.gameserver.structs.ServerInfo;
import com.game.manager.Manager;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.structs.ServerType;
import com.game.structs.SessionKey;
import com.game.utils.MessageUtils;
import game.core.net.Config.ServerConfig;
import game.core.util.TimeUtils;
import io.netty.channel.ChannelHandlerContext;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameServerManager {

    private final static Logger log = LogManager.getLogger("GameServerManager");

    private static GameServerManager manager;

    private static final Object obj = new Object();

    private GameServerManager() {
    }

    public static GameServerManager getInstance() {
        synchronized (obj) {
            if (manager == null) {
                manager = new GameServerManager();
            }
        }
        return manager;
    }

    //总的数据，对应区号=具体服务器信息
    private final ConcurrentHashMap<String, ServerInfo> serverCache = new ConcurrentHashMap<>();

    //连接数据， 服务器类型 = 服务器信息类集合
    private final ConcurrentHashMap<String, ChannelHandlerContext> gameSessions = new ConcurrentHashMap<>();

    //分数据， 服务器类型 = 服务器信息类集合
    private final ConcurrentHashMap<Integer, List<ServerInfo>> typeCache = new ConcurrentHashMap<>();

    //当前月分组号对应的服务器(包含默认分组),服务器维护。默认分组0,-1,-2开始，运营分组 1，2，3开始
    private ConcurrentHashMap<Integer, List<String>> groupToServers = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, ServerInfo> getServerCache() {
        return serverCache;
    }

    public ConcurrentHashMap<String, ChannelHandlerContext> getGameSessions() {
        return gameSessions;
    }

    public ServerInfo socialServer;

    //获得分列表
    public List<ServerInfo> GetType(int type) {
        if (typeCache.containsKey(type)) {
            return typeCache.get(type);
        }
        return new ArrayList<>();
    }

    public String makeKey(String plat, int sid) {
        return plat + "_" + sid;
    }

    //添加服务器
    public synchronized boolean addServer(ServerInfo info) {

        String key = makeKey(info.getPlatName(), info.getServerId());

        if (gameSessions.containsKey(key)) {
            ChannelHandlerContext ss = gameSessions.get(key);
            ss.channel().attr(SessionKey.SERVERPLATID).set("");
        }

        gameSessions.put(key, info.getSession());

        for (int id : info.getSids()) {
            serverCache.put(makeKey(info.getPlatName(), id), info);
            log.info(info.getPlatName() + " _ " + id + " 注册的游戏服ID： " + info.getServerId() + " ip:" + info.getServerIp());
        }

        List<ServerInfo> list;
        if (typeCache.containsKey(info.getServerType())) {
            list = typeCache.get(info.getServerType());
            if (!list.contains(info)) {
                list.add(info);
            }
        } else {
            list = new ArrayList<>();
            list.add(info);
            typeCache.put(info.getServerType(), list);
        }
        if(info.getServerType() == ServerType.GAMESERVER_TEST || info.getServerType() == ServerType.GAMESERVER){
            ServerMatchManager.addGameServer(info);
            //游戏服连接公共服时候计算跨服世界等级
            Manager.crossRankManager.deal().calcSingleServerWorldLv(info);
        }
        return true;
    }

    public synchronized boolean removeServer(ChannelHandlerContext ss, String key) {
        ServerInfo info = serverCache.get(key);
        if (info == null) {
            log.error(key + " 已经没有服务器信息了！");
            return false;
        }
        ChannelHandlerContext session = gameSessions.get(key);
        if (session == null) {
            log.error(key + " 老的连接地址已经没有了！");
            return false;
        }

        if (ss == null) {
            log.error(key + "  新连接地址已经没有了！");
            return false;
        }

        if (!ss.equals(session)) {
            log.error(key + " 老连接与新连接不相等！");
            return false;
        }

        gameSessions.remove(key);
        log.info("服务器数量删除前：" + serverCache.size());
        try {
            for (int id : info.getSids()) {
                String kk = makeKey(info.getPlatName(), id);
                serverCache.remove(kk);
                log.info(info.getPlatName() + " _ " + id + " 注册的游戏服ID： " + info.getServerId() + "清除");
            }
        } catch (Exception e) {
            log.error(e, e);
        }
        log.info("服务器数量删除后：" + serverCache.size());
        List<ServerInfo> list = typeCache.get(info.getServerType());
        log.info("服务器分类列表删除前：" + list.size());
        list.remove(info);
        log.info("服务器分类列表删除后：" + list.size());
        return true;
    }

    //获得连接的服务器连接
    public ChannelHandlerContext GetSession(String plat, int serverId) {
        String key = plat + "_" + serverId;
        if (gameSessions.containsKey(key)) {
            return gameSessions.get(plat + "_" + serverId);
        } else {
            log.error(" 向（" + plat + "）服务器id:" + serverId + " 发送消息时，连接已经不存在了！");
            return null;
        }
    }

    //获得连接的服务器连接
    public ChannelHandlerContext GetSession(String key) {
        if (gameSessions.containsKey(key)) {
            return gameSessions.get(key);
        } else {
            //获得实际的服务器连接
            ServerInfo info = serverCache.get(key);
            if (info != null) {
                return GetSession(info.getPlatName(), info.getServerId());
            }
            log.error(" 向（" + key + ") 发送消息时，连接已经不存在了！");

            return null;
        }
    }

    /**
     * 向所有游戏服发送消息
     *
     * @param msgId
     * @param msg
     */
    public void send_all_game(int msgId, byte[] msg) {
        List<ServerInfo> list = typeCache.get(ServerType.GAMESERVER_TEST);
        if (list == null) {
            list = new ArrayList<>();
        }
        if (typeCache.get(ServerType.GAMESERVER) != null) {
            list.addAll(typeCache.get(ServerType.GAMESERVER));
        }

        if (list.size() < 1) {
            return;
        }

        for (ServerInfo si : list) {
            MessageUtils.send_to_game(si.getSession(), msgId, msg);
        }
    }

    /**
     * 向所有战斗服发送消息
     *
     * @param msgId
     * @param msg
     */
    public void send_all_FightGame(int msgId, byte[] msg) {
        List<ServerInfo> list = typeCache.get(ServerType.FIGHTSERVER);
        if (list == null) {
            log.error("还没有战斗服连接进哦=========================");
            return;
        }

        if (list.size() < 1) {
            log.error("还没有战斗服连接进哦========================2=");
            return;
        }

        for (ServerInfo si : list) {
            MessageUtils.send_to_game(si.getSession(), msgId, msg);
        }
    }

    /**
     * 获取所有游戏服
     * @return
     */
    public List<ServerInfo> getAllGameServer(){
        List<ServerInfo> list = typeCache.get(ServerType.GAMESERVER_TEST);
        if (list == null) {
            list = new ArrayList<>();
        }
        if (typeCache.get(ServerType.GAMESERVER) != null) {
            list.addAll(typeCache.get(ServerType.GAMESERVER));
        }
        return list;
    }



    public ConcurrentHashMap<Integer, List<String>> getGroupToServers() {
        return groupToServers;
    }


    public static long getOpenServerTime(String openTime) {
        try {
            Calendar instance = Calendar.getInstance();
            instance.setTime(TimeUtils.getDateByString(openTime));
            instance.set(Calendar.MILLISECOND, 0);
            instance.set(instance.get(Calendar.YEAR)
                    , instance.get(Calendar.MONTH)
                    , instance.get(Calendar.DATE)
                    , instance.get(Calendar.HOUR_OF_DAY)
                    , instance.get(Calendar.MINUTE)
                    , instance.get(Calendar.SECOND));

            return instance.getTimeInMillis();
        } catch (ParseException ex) {
            log.error("开服时间解析错误,开服时间：" +openTime);
            return 0;
        }
    }

    public static int getOpenServerDay(String openTime) {
        try {
            Date open = TimeUtils.getDateByString(openTime);
            long zday = TimeUtils.GetCurTimeInMin(4,  open.getTime());
            long sday = TimeUtils.GetCurTimeInMin(4, TimeUtils.Time());
            return (int) (sday - zday) + 1;
        } catch (ParseException ex) {
            log.error(ex, ex);
        }
        return 0;
    }
}
