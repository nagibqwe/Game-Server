package com.game.player.manager;

import com.data.Global;
import com.game.db.dao.PlayerWorldInfoDao;
import com.game.manager.Manager;
import com.game.player.script.*;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.player.structs.SavePlayerLevel;
import com.game.register.script.ILoadScript;
import com.game.register.script.IQuitGame;
import com.game.script.structs.ScriptEnum;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tc
 */
public class PlayerManager {
    private final Logger log = LogManager.getLogger(PlayerManager.class);

    private final int BornMapID = Global.NewPlayerFirstMap;//新手村地图ID值

    private final PlayerWorldInfoDao dao = new PlayerWorldInfoDao();

    // 全部玩家列表
    private final ConcurrentHashMap<Long, PlayerWorldInfo> allPlayers = new ConcurrentHashMap<>();
    // 玩家数据缓存(玩家可能不在线)
    private final ConcurrentHashMap<Long, Player> playersCache = new ConcurrentHashMap<>();

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例
        PlayerManager manager;

        Singleton() {
            this.manager = new PlayerManager();
        }

        PlayerManager getProcessor() {
            return manager;
        }
    }

    public static PlayerManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public void loadAllPlayer() {
        long time = TimeUtils.Time();
        log.info("加载全部玩家 开始···");
        List<PlayerWorldInfo> list = dao.selectAll();
        for (int i = 0; i < list.size(); i++) {
            allPlayers.put(list.get(i).getRoleid(), list.get(i));
        }
        log.info("加载全部玩家结束，所用时间：" + (TimeUtils.Time() - time));
    }

    /**
     * get new born map id
     * @return
     */
    public int getBornMapID() {
        return BornMapID;
    }

    /**
     * 获取玩家所有缓存数据
     *
     * @return
     */
    public ConcurrentHashMap<Long, Player> getPlayersCache() {
        return playersCache;
    }

    /**
     * 获取所有玩家
     *
     * @return players
     */
    public ConcurrentHashMap<Long, PlayerWorldInfo> getAllPlayerWorldInfo() {
        return allPlayers;
    }


    /**
     * get online players
     * @return
     */
    public List<Player> getOnLines() {
        List<Player> list = new ArrayList<>();
        for (Map.Entry<Long, Player> entry : playersCache.entrySet()) {
            if (entry.getValue().isOnline())
                list.add(entry.getValue());
        }
        return list;
    }

    /**
     * 获取在线人数
     *
     * @return
     */
    public int getOnLinePlayerNum() {
        return getOnLines().size();
    }

    /**
     * cache player
     * @param player
     */
    public void cachePlayer(Player player) {
        playersCache.put(player.getId(), player);
    }

    /**
     * query player from cache
     * @param roleId
     * @return
     */
    public Player getPlayerCache(long roleId) {
        return playersCache.get(roleId);
    }

    /**
     * 通过角色id获取角色对象
     * if no cache, query db
     *
     * @param roleId 角色id
     * @return player 玩家
     */
    public Player getPlayer(long roleId) {
        Player player = getPlayerCache(roleId);
        if (player != null) {
            return player;
        }

        player = manager().LoadPlayerFromDB(roleId);
        if (player == null) {
            log.error("加载玩家失败：" + roleId);
            return null;
        }

        playersCache.put(player.getId(), player);
        return player;
    }

    /**
     * get player online
     *
     * @param roleId
     * @return
     */
    public Player getPlayerOnline(long roleId) {
        Player player = playersCache.get(roleId);
        if (player == null) {
            return null;
        }
        if (!player.isOnline()) {
            return null;
        }
        return player;
    }

    /**
     * player is online
     * @param roleId
     * @return
     */
    public boolean isOnline(long roleId) {
        Player pp = playersCache.get(roleId);
        if (pp == null) {
            return false;
        }
        return pp.isOnline();
    }

    /**
     * 获取玩家,包含不在线玩家
     *
     * @param roleId
     * @return PlayerWorldInfo
     */
    public PlayerWorldInfo getPlayerWorldInfo(long roleId) {
        return allPlayers.get(roleId);
    }

    /**
     * 保存玩家,不同步数据到世界服
     * @param player
     * @param level
     */
    public void savePlayer(Player player, SavePlayerLevel level) {
        manager().SavePlayer(player, level);
    }

    public int getCreateServeId(long roleId) {
        Player player = getPlayerCache(roleId);
        if (player != null) {
            return player.getCreateServerId();
        }

        PlayerWorldInfo pwi = getPlayerWorldInfo(roleId);
        if (pwi != null) {
            return pwi.getCsid();
        }
        return ServerConfig.getServerId();
    }

    public String getPlatformName(long roleId) {
        Player player = getPlayerCache(roleId);
        if (player != null) {
            return player.getPlatformName();
        }

        PlayerWorldInfo pwi = getPlayerWorldInfo(roleId);
        if (pwi != null) {
            return pwi.getPlat();
        }
        return ServerConfig.getServerPlatform();
    }

    public IPlayerScript deal(int scriptId) {
        IScript is = Manager.scriptManager.GetScriptClass(scriptId);
        if (is instanceof IPlayerScript) {
            return (IPlayerScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public IPlayerManagerScript manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.PlayerManagerBaseScript);
        if (is instanceof IPlayerManagerScript) {
            return (IPlayerManagerScript) is;
        }
        throw new NullPointerException("没有找到playerManager脚本的实现！");
    }

    public IPlayerManagerExtScript managerExt() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.PlayerManagerExtScript);
        if (is instanceof IPlayerManagerExtScript) {
            return (IPlayerManagerExtScript) is;
        }
        throw new NullPointerException("没有找到playerManagerExt脚本的实现！");
    }

    public IQuitGame iQuitGame() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.QuitGameBaseScript);
        if (is instanceof IQuitGame) {
            return (IQuitGame) is;
        }
        throw new NullPointerException("没有找到IQuitGame脚本的实现！");
    }

    public IXiSuiScript xiSuiScript() {
        IScript script = ScriptManager.getInstance().GetScriptClass(ScriptEnum.XiSuiScript);
        if (script == null) {
            log.error("没有找到洗髓的脚本，ID：" + ScriptEnum.XiSuiScript);
            return null;
        } else {
            return (IXiSuiScript) script;
        }
    }

    public ICertifyScript certifyScript() {
        IScript script = ScriptManager.getInstance().GetScriptClass(ScriptEnum.CertifyScript);
        if (script == null) {
            log.error("没有找到实名认证的脚本，ID：" + ScriptEnum.CertifyScript);
            return null;
        } else {
            return (ICertifyScript) script;
        }
    }
    public ILoadScript loadScript() {
        IScript script = ScriptManager.getInstance().GetScriptClass(ScriptEnum.LoadScript);
        if (script == null) {
            log.error("没有找到实名认证的脚本，ID：" + ScriptEnum.LoadScript);
            return null;
        } else {
            return (ILoadScript) script;
        }
    }
}
