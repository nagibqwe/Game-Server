package com.game.universe.manager;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.universe.script.IUniverseWarScript;
import com.game.universe.struct.GuildBattleInfo;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 太虚战场管理类
 */
public class UniverseManager {
    private static final Logger log = LogManager.getLogger(UniverseManager.class);

    //公会战信息 区服ID》排名，战绩
    private ConcurrentHashMap<String, ConcurrentHashMap<Integer, GuildBattleInfo>> guildBattleInfoMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, ConcurrentHashMap<Integer, GuildBattleInfo>> getGuildBattleInfoMap() {
        return guildBattleInfoMap;
    }

    public void setGuildBattleInfoMap(ConcurrentHashMap<String, ConcurrentHashMap<Integer, GuildBattleInfo>> guildBattleInfoMap) {
        this.guildBattleInfoMap = guildBattleInfoMap;
    }

    private enum Singleton {

        INSTANCE;
        UniverseManager manager;

        Singleton() {
            this.manager = new UniverseManager();
        }

        UniverseManager getProcessor() {
            return manager;
        }
    }

    public static UniverseManager getInstance() {
        return UniverseManager.Singleton.INSTANCE.getProcessor();
    }

    public IUniverseWarScript deal()  {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.UniverseWarActivityScript);
        if (is instanceof IUniverseWarScript) {
            return (IUniverseWarScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }
}
