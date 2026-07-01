package com.game.guildcrossfud.manager;

import com.game.alienboss.script.IAlienScript;
import com.game.guildcrossfud.script.IDevilFudScript;
import com.game.guildcrossfud.struct.FudRole;
import com.game.guildcrossfud.script.IFudScript;
import com.game.guildcrossfud.struct.FudGroup;
import com.game.guildcrossfud.timer.FudTimer;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO  跨服福地功能
 * @Date 2021/2/2 14:33
 * @Auth ZUncle
 */
public class FudManager {

    final Logger logger = LogManager.getLogger(FudManager.class);

    //福地心跳
    final FudTimer timer = new FudTimer();
    //福地玩家积分数据 roleId -> role
    final ConcurrentHashMap<Long, FudRole> fudRole = new ConcurrentHashMap<>();
    //福地跨服分组数据 groupId -> group
    final ConcurrentHashMap<Integer, FudGroup> groups = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, FudRole> getFudRole() {
        return fudRole;
    }

    public ConcurrentHashMap<Integer, FudGroup> getGroups() {
        return groups;
    }

    public FudTimer getTimer() {
        return timer;
    }

    public IFudScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.FudScript);
        if (is == null) {
            logger.error("未找到脚本FudScript={}", ScriptEnum.FudScript);
            return null;
        }
        return (IFudScript) is;
    }

    public IDevilFudScript devil() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.DevilFudScript);
        if (is == null) {
            logger.error("未找到脚本DevilFudScript={}", ScriptEnum.DevilFudScript);
            return null;
        }
        return (IDevilFudScript) is;
    }

    public IAlienScript alien() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.AlienScript);
        if (is == null) {
            logger.error("未找到脚本AlienScript={}", ScriptEnum.AlienScript);
            return null;
        }
        return (IAlienScript) is;
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        FudManager manager;

        Singleton() {
            this.manager = new FudManager();
        }

        FudManager getProcessor() {
            return manager;
        }
    }

    public static FudManager getInstance() {
        return FudManager.Singleton.INSTANCE.getProcessor();
    }
}
