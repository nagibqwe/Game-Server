package com.game.guildactivity.manager;

import com.game.count.structs.Count;
import com.game.count.structs.ICount;
import com.game.guildactivity.script.IGuildActivityHandler;
import com.game.guildactivity.script.IGuildLastBattle;
import com.game.guildactivity.struct.GuildFud;
import com.game.guildactivity.struct.GuildFudiAttack;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class GuildActivityManager implements ICount {

    final Logger log = LogManager.getLogger(GuildActivityManager.class);
    /**
     * 仙盟ID->value
     */
    private HashMap<Long, HashMap<Integer, GuildFudiAttack>> fudiAttacking = new HashMap<>();

    //福地数据 福地ID-> 福地
    final HashMap<Integer, GuildFud> fud = new HashMap<>();

    final ConcurrentHashMap<String, Count> counts = new ConcurrentHashMap<>();

    /**
     * 仙盟Boss相关
     */
    private boolean GuildBossRun = false;       //仙盟boss活动是否开启
    private ConcurrentHashMap<Long, Long> bossGuildDamage = new ConcurrentHashMap<>();//仙盟伤害
    private ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> bossPersonDamage = new ConcurrentHashMap<>();//仙盟个人伤害
    private ConcurrentHashMap<Long, ConcurrentHashMap<Long, Integer>> moneyInspire = new ConcurrentHashMap<>();//仙盟成员金币鼓舞次数
    private ConcurrentHashMap<Long, ConcurrentHashMap<Long, Integer>> goldInspire = new ConcurrentHashMap<>();//仙盟成员元宝鼓舞次数
    private ConcurrentHashMap<Long, Integer> totalInspire = new ConcurrentHashMap<>();//仙盟鼓舞总次数

    public static GuildActivityManager getInstance() {
        return GuildActivityManager.Singleton.INSTANCE.getProcessor();
    }

    public IGuildActivityHandler deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GuildActivityBaseScript);
        if (is instanceof IGuildActivityHandler) {
            return (IGuildActivityHandler) is;
        } else {
            log.error("宗派活动GuildActivity，没有找到具体的实例！");
            return null;
        }
    }

    public IGuildLastBattle guildLastBattle() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GuildLastBattleScript);
        if (is == null) {
            log.error("仙盟论剑，没有找到具体的实例！");
            return null;
        }
        return (IGuildLastBattle) is;
    }

    public IMapBaseScript base() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GuildBaseMapScript);
        if (is instanceof IMapBaseScript) {
            return (IMapBaseScript) is;
        }
        return null;
    }

    /**
     * 获取计数数据
     *
     * @return
     */
    @Override
    public ConcurrentHashMap<String, Count> getCounts() {
        return counts;
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        INSTANCE;
        GuildActivityManager processor;

        Singleton() {
            this.processor = new GuildActivityManager();
        }

        GuildActivityManager getProcessor() {
            return processor;
        }
    }

    public HashMap<Integer, GuildFud> getFud() {
        return fud;
    }


    public HashMap<Long, HashMap<Integer, GuildFudiAttack>> getFudiAttacking() {
        return fudiAttacking;
    }

    public ConcurrentHashMap<Long, Long> getBossGuildDamage() {
        return bossGuildDamage;
    }

    public ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> getBossPersonDamage() {
        return bossPersonDamage;
    }

    public ConcurrentHashMap<Long, ConcurrentHashMap<Long, Integer>> getMoneyInspire() {
        return moneyInspire;
    }

    public ConcurrentHashMap<Long, ConcurrentHashMap<Long, Integer>> getGoldInspire() {
        return goldInspire;
    }

    public ConcurrentHashMap<Long, Integer> getTotalInspire() {
        return totalInspire;
    }

    public boolean isGuildBossRun() {
        return GuildBossRun;
    }

    public void setGuildBossRun(boolean guildBossRun) {
        GuildBossRun = guildBossRun;
    }

}
