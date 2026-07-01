package com.game.guildbattle.manager;

import com.game.guildbattle.script.IGuildBattleManagerScript;
import com.game.guildbattle.structs.GuildBattle;
import com.game.guildbattle.structs.GuildBattleWin;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @auther lw
 * @create 2020-02-11 15:01
 */
public class GuildBattleManager {

    private static final Logger log = LogManager.getLogger(GuildBattleManager.class);

    public static final int MAX_GUILD_NUM = 12;

    public static final int MAX_GUILD_NUM_EVERY_LV = 3; // 每一个评级区仙盟数量

    /**
     * 最新仙盟战绩
     */
    private ConcurrentHashMap<Long, GuildBattle> guildbattles = new ConcurrentHashMap<>();

    /**
     * 最新仙盟连赢或者终结
     */
    private GuildBattleWin guildBattleWin = new GuildBattleWin();

    private enum Singleton {
        INSTANCE;
        GuildBattleManager manager;

        Singleton() {
            this.manager = new GuildBattleManager();
        }

        GuildBattleManager getProcessor() {
            return manager;
        }
    }

    public static GuildBattleManager getInstance() {
        return GuildBattleManager.Singleton.INSTANCE.getProcessor();
    }

    public IGuildBattleManagerScript manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GuildBattleBaseScript);
        if (is instanceof IGuildBattleManagerScript) {
            return (IGuildBattleManagerScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public ConcurrentHashMap<Long, GuildBattle> getGuildbattles() {
        return guildbattles;
    }

    public void setGuildbattles(ConcurrentHashMap<Long, GuildBattle> guildbattles) {
        this.guildbattles = guildbattles;
    }

    public GuildBattleWin getGuildBattleWin() {
        return guildBattleWin;
    }

    public void setGuildBattleWin(GuildBattleWin guildBattleWin) {
        this.guildBattleWin = guildBattleWin;
    }
}
