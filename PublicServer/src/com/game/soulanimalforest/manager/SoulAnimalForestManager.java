package com.game.soulanimalforest.manager;

import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.soulanimalforest.script.ISoulAnimalForestScript;
import com.game.soulanimalforest.struct.BossHaveFollow;
import com.game.soulanimalforest.struct.GroupBossData;
import game.core.script.IScript;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 魂兽森林的数据管理中心
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class SoulAnimalForestManager {

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        SoulAnimalForestManager manager;

        Singleton() {
            this.manager = new SoulAnimalForestManager();
        }

        SoulAnimalForestManager getProcessor() {
            return manager;
        }
    }

    //SoulAnimalForestManager
    public static SoulAnimalForestManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
    
    /**
     * 分组的魂兽森林的BOSS信息
     */
    private static final ConcurrentHashMap<Integer, GroupBossData> groupBossMap = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<Integer, ConcurrentHashMap<Long, BossHaveFollow>> bossFollowList = new ConcurrentHashMap<>();//bossConfigId, 角色ID， 服务器信息

    public static  List<Integer> allGroupList = new ArrayList<>();//所有达成分组的组ID

    public static ConcurrentHashMap<Integer, ConcurrentHashMap<Long, BossHaveFollow>> getBossFollowList() {
        return bossFollowList;
    }

    public static ConcurrentHashMap<Integer, GroupBossData> getGroupBossMap() {
        return groupBossMap;
    }

    public ISoulAnimalForestScript manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.SoulAnimalForestManagerScript);
        if (is instanceof ISoulAnimalForestScript) {
            return (ISoulAnimalForestScript) is;
        }
        throw new NullPointerException("没有实现魂兽森林的副本处理逻辑！");
    }

}
