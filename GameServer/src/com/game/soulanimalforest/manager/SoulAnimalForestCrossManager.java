/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.soulanimalforest.manager;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.soulanimalforest.script.ISoulAnimalForestManager;
import com.game.soulanimalforest.script.ISoulAnimalIslandClone;
import com.game.soulanimalforest.structs.CrossBossData;
import com.game.soulanimalforest.structs.CrossGroupBossData;
import game.core.script.IScript;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在战斗服的魂兽森林的数据管理类， 因为这个需要管理的数据有很不一样， 需要使用fightID 来分类管理
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class SoulAnimalForestCrossManager {

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        SoulAnimalForestCrossManager manager;

        Singleton() {
            this.manager = new SoulAnimalForestCrossManager();
        }

        SoulAnimalForestCrossManager getProcessor() {
            return manager;
        }
    }

    //SoulManager
    public static SoulAnimalForestCrossManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public ISoulAnimalForestManager manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.SoulAnimalForestManagerBaseScript);
        if (is instanceof ISoulAnimalForestManager) {
            return (ISoulAnimalForestManager) is;
        }
        throw new NullPointerException("没有实现魂兽森林的脚本！");
    }

    public ISoulAnimalIslandClone cloneScript() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.SoulAnimalForestCrossCloneCrossActivityScript);
        if (is instanceof ISoulAnimalIslandClone) {
            return (ISoulAnimalIslandClone) is;
        }
        throw new NullPointerException("没有实现神兽岛副本的脚本！");
    }

    /**
     * 跨服的分组数据集合
     */
    private static final ConcurrentHashMap<Integer, CrossGroupBossData> groupInfo = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Integer, CrossGroupBossData> getGroupInfo() {
        return groupInfo;
    }

    public static CrossGroupBossData getGroupBossData(int groupId) {
        if (groupInfo.containsKey(groupId)) {
            return groupInfo.get(groupId);
        }
        return null;
    }

}
