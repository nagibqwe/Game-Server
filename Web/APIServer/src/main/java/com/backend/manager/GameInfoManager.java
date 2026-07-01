package com.backend.manager;

import com.backend.bean.GameInfo;
import com.backend.bean.RechargeItem;
import com.backend.bean.Server;
import com.backend.gm.GameServerRequestUtil;
import com.backend.struct.RechargeItemInfo;
import com.backend.utils.JsonUtils;
import com.backend.utils.MD5Util;
import com.backend.utils.ServerKeyUtil;
import com.backend.utils.Toolkit;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.util.List;
import java.util.TreeMap;

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

    private GameInfo gameInfo = new GameInfo();

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        if(gameInfo != null){
            this.gameInfo = gameInfo;
        }
    }

    public void init(Dao dao) {
        this.dao = dao;
        //加载游戏相关信息
        Cnd cnd = Cnd.where("gameId", "=", ServerKeyUtil.getGameID());
        setGameInfo(dao.fetch(GameInfo.class, cnd));
    }

    public void updateGameInfo(GameInfo gameInfo) {
        this.gameInfo.setRechargeSecretkey(gameInfo.getRechargeSecretkey());
        this.gameInfo.setAutoFirstServerId(gameInfo.getAutoFirstServerId());
        this.gameInfo.setAutoUserCount(gameInfo.getAutoUserCount());
        this.gameInfo.setAutoServerId(gameInfo.getAutoServerId());
    }
}
