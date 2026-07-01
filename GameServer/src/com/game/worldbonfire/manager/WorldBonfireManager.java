package com.game.worldbonfire.manager;

import com.data.CfgManager;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Daily_Bean;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.worldbonfire.script.IWorldBonfireScript;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Description
 * @auther lw
 * @create 2019-10-15 9:57
 */
public class WorldBonfireManager {

    private static final Logger logger = LogManager.getLogger(WorldBonfireManager.class);

    public static long getActiveTime() {
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.ACTIVITY_WORLD_BONFIRE.getValue());
        return (bean.getTime().get(0).get(1) - bean.getTime().get(0).get(0)) * 60 * 1000L;
    }

    public static int getMapId() {
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.ACTIVITY_WORLD_BONFIRE.getValue());
        Cfg_Clone_map_Bean clone_map_bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(bean.getCloneID().get(0));
        return clone_map_bean.getMapid();
    }

    public IWorldBonfireScript manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.WorldBonfireBaseScript);
        if (is instanceof IWorldBonfireScript) {
            return (IWorldBonfireScript) is;
        }
        logger.error("没有找到世界篝火管理脚本!");
        return null;
    }

    private enum Singleton {
        INSTANCE;
        WorldBonfireManager manager;

        Singleton() {
            manager = new WorldBonfireManager();
        }

        WorldBonfireManager getProcessor() {
            return manager;
        }
    }

    public static WorldBonfireManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
