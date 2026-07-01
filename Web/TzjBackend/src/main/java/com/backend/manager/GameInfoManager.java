package com.backend.manager;

import com.backend.bean.GameInfo;
import com.backend.utils.ServerKeyUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * 游戏相关数据管理
 */
public class GameInfoManager {

    private static final Log logger = Logs.get();

    private enum Singleton {

        INSTANCE;
        GameInfoManager manager;

        Singleton() {
            this.manager = new GameInfoManager();
        }

        GameInfoManager getProcessor() {
            return manager;
        }
    }

    public static GameInfoManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private Dao dao;

    private GameInfo gameInfo;

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public void init(Dao dao) {
        this.dao = dao;
        //加载游戏相关信息
        Cnd cnd = Cnd.where("gameId", "=", ServerKeyUtil.getGameID());
        this.gameInfo = dao.fetch(GameInfo.class, cnd);
    }

    public void updateGameInfo(GameInfo gameInfo) {
        this.gameInfo.setRechargeSecretkey(gameInfo.getRechargeSecretkey());
        this.gameInfo.setAutoFirstServerId(gameInfo.getAutoFirstServerId());
        this.gameInfo.setAutoUserCount(gameInfo.getAutoUserCount());
        this.gameInfo.setAutoServerId(gameInfo.getAutoServerId());
    }
}
