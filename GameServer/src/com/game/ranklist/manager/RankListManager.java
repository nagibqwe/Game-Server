package com.game.ranklist.manager;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.Global;
import com.data.bean.Cfg_Equip_Bean;
import com.data.bean.Cfg_RankAwardType_Bean;
import com.data.bean.Cfg_Rank_base_Bean;
import com.game.attribute.BaseIntAttribute;
import com.game.backpack.structs.ItemCoinType;
import com.game.db.bean.RankPlayer;
import com.game.db.dao.RankPlayerDao;
import com.game.equip.struct.*;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.newfashion.manager.NewFashionManager;
import com.game.player.script.IPlayerAttribute;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.PlayerWorldInfo;
import com.game.ranklist.handler.SyncRankPlayerHandler;
import com.game.ranklist.script.*;
import com.game.ranklist.structs.RankType;
import com.game.ranklist.timer.RankListSortTimer;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.structs.GlobalType;
import com.game.universe.script.IUniverseRankScript;
import game.core.script.IScript;
import game.core.thread.ServerThread;
import game.core.util.TimeUtils;
import game.message.RankListMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 排行榜管理器
 */
public class RankListManager extends ServerThread {

    private static final Logger logger = LogManager.getLogger(RankListManager.class);

    private final RankPlayerDao dao = new RankPlayerDao();

    //缓存所有进入排行榜的玩家信息
    private static final ConcurrentHashMap<Long, RankPlayer> rankPlayerMap = new ConcurrentHashMap<>();

    //缓存所有类型排行信息
    private static final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Long>> tempRankMap = new ConcurrentHashMap<>();

    //排行榜类型列表
    private static final List<Integer> rankTypeList = new ArrayList<>();

    public RankListManager() {
        super(new ThreadGroup("RankThreadGroup"), "RankThread", GameServer.getInstance().getServerTimer());
    }

    public RankPlayerDao getDao() {
        return dao;
    }

    public static ConcurrentHashMap<Long, RankPlayer> getRankPlayerMap() {
        return rankPlayerMap;
    }

    public static List<Integer> getRankTypeList() {
        return rankTypeList;
    }

    public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Long>> getTempRankMap() {
        return tempRankMap;
    }

    //用枚举实现单例
    private enum Singleton {

        INSTANCE;
        RankListManager manager;

        Singleton() {
            this.manager = new RankListManager();
        }

        RankListManager getProcessor() {
            return manager;
        }
    }

    public static RankListManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 初始化排行榜
     */
    public void loadRankPlayer() {
        for (Cfg_Rank_base_Bean bean : CfgManager.getCfg_Rank_base_Container().getValuees()) {
            rankTypeList.add(bean.getId());
        }
        if (!rankPlayerMap.isEmpty()) {
            rankPlayerMap.clear();
        }

        logger.info("开始加载初始化排行榜玩家");
        long begin = TimeUtils.Time();
        List<RankPlayer> rankPlayerList = dao.selectAll();
        for (RankPlayer rankPlayer : rankPlayerList) {
            rankPlayerMap.put(rankPlayer.getRoleId(), rankPlayer);
        }
        long end = TimeUtils.Time();

        try {
            deal().sortAllRank();
            getUniverseRankScript().sortUniverseRank();
            Manager.marriageManager.activity().intimacyRank();
        } catch (Exception e) {
            logger.error(e, e);
        }
        logger.info("排行榜玩家加载初始化完毕！用时：" + (end - begin) + "ms" + "，玩家数=" + rankPlayerMap.size());

        long delay = GlobalType.MILLIS_PER_MINUTE - TimeUtils.Time() % GlobalType.MILLIS_PER_MINUTE;
        this.addTimerEvent(new RankListSortTimer(delay));
    }

    /**
     * 排行榜脚本
     */
    public IRankListScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.RankListBaseScript);
        if (is instanceof IRankListScript) {
            return (IRankListScript) is;
        }
        logger.error("找不到排行榜实现脚本：" + ScriptEnum.RankListBaseScript);
        return null;
    }

    /**
     * 具体类型的排行榜脚本
     */
    public IRankScript getRankScript(int rankType) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.RankListBaseScript * ScriptEnum.NodeLimit + rankType);
        if (is instanceof IRankScript) {
            return (IRankScript) is;
        }
        logger.error("找不到排行榜实现脚本：" + (ScriptEnum.RankListBaseScript * ScriptEnum.NodeLimit + rankType));
        return null;
    }

    /**
     * 获取公会脚本
     */
    public IGuildRankScript getGuildRankScript() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GuildRankScript);
        if (is instanceof IGuildRankScript) {
            return (IGuildRankScript) is;
        }
        logger.error("找不到排行榜实现脚本：" + ScriptEnum.GuildRankScript);
        return null;
    }

    /**
     * 名人堂脚本
     */
    public ITopHallScript getTopHallRankScript() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.TopHallRankScript);
        if (is instanceof ITopHallScript) {
            return (ITopHallScript) is;
        }
        logger.error("找不到排行榜实现脚本：" + ScriptEnum.TopHallRankScript);
        return null;
    }

    /**
     * 名人堂脚本
     */
    public IUniverseRankScript getUniverseRankScript() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.UniverseRankScript);
        if (is instanceof IUniverseRankScript) {
            return (IUniverseRankScript) is;
        }
        logger.error("找不到排行榜实现脚本：" + ScriptEnum.UniverseRankScript);
        return null;
    }

    /**
     * 获取排行榜的最大人数
     */
    public int getRankMax(int rankType) {
        Cfg_Rank_base_Bean bean = CfgManager.getCfg_Rank_base_Container().getValueByKey(rankType);
        if (bean == null) {
            logger.error("Cfg_Rank_base_Bean配置表不存在：" + rankType);
            return 0;
        }
        return bean.getRank_truthNum();
    }
}
