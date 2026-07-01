package com.game.player.manager;

import com.game.client.Client;
import com.game.client.LoginClient;
import com.game.manager.Manager;
import com.game.player.script.IPlayerScript;
import com.game.player.structs.Player;
import com.game.script.ScriptEnum;
import com.game.server.worker.TimeTickWorker;
import com.game.structs.Config;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    private final static Logger log = LogManager.getLogger(PlayerManager.class);

    private final ConcurrentHashMap<Long, Player> players = new ConcurrentHashMap<>();

    //登录服玩家
    private final ConcurrentHashMap<Integer, LoginClient> loginPlayers = new ConcurrentHashMap<>();

    private long maxid = 0;

    private enum Singleton {

        INSTANCE;
        PlayerManager processor;

        Singleton() {
            this.processor = new PlayerManager();
        }

        PlayerManager getProcessor() {
            return processor;
        }
    }

    public static PlayerManager getInstance() {
        return PlayerManager.Singleton.INSTANCE.getProcessor();
    }

    public ConcurrentHashMap<Long, Player> getPlayers() {
        return players;
    }

    public ConcurrentHashMap<Integer, LoginClient> getLoginPlayers() {
        return loginPlayers;
    }

    public long getMaxid() {
        return maxid;
    }

    public void setMaxid(long maxid) {
        this.maxid = maxid;
    }

    public IPlayerScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.PlayerScript);
        if (is instanceof IPlayerScript) {
            return (IPlayerScript) is;
        }
        return null;
    }

    /**
     * 事件心跳处理
     */
    public void tickEvent() {
        try {
            long time = TimeUtils.Time();
            for (Map.Entry<Long, Player> entry : Manager.playerManager.getPlayers().entrySet()) {
                Player player = entry.getValue();
                if (player == null) {
                    continue;
                }

                //转到同一个线程去执行事务
                Client.getRecvExcutor().addTask(player.getUserId(), new TimeTickWorker(time, player));
            }

        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
