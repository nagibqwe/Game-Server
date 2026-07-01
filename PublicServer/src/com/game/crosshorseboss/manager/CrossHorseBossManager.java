package com.game.crosshorseboss.manager;


import com.game.crosshorseboss.scripts.ICrossHorseBoss;
import com.game.crosshorseboss.structs.CrosshorseBossData;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.soulanimalforest.struct.BossHaveFollow;
import game.core.script.IScript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cxl on 2021/4/14.
 */
public class CrossHorseBossManager {

    private   List<Integer> allGroupList = new ArrayList<>();//所有达成分组的组ID


    //所有Boss信息
    private ConcurrentHashMap<Integer,ConcurrentHashMap<Integer,CrosshorseBossData>> allHosrseBossMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Integer, ConcurrentHashMap<Long, BossHaveFollow>> bossFollowList = new ConcurrentHashMap<>();//bossConfigId, 角色ID， 服务器信息



    public ICrossHorseBoss deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CrossHorseBossScript);
        if (is instanceof ICrossHorseBoss) {
            return (ICrossHorseBoss) is;
        } else {
            return null;
        }
    }


    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        CrossHorseBossManager manager;

        Singleton() {
            this.manager = new CrossHorseBossManager();
        }

        CrossHorseBossManager getProcessor() {
            return manager;
        }
    }

    public static CrossHorseBossManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }


    public List<Integer> getAllGroupList() {
        return allGroupList;
    }

    public void setAllGroupList(List<Integer> allGroupList) {
        this.allGroupList = allGroupList;
    }

    public ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, CrosshorseBossData>> getAllHosrseBossMap() {
        return allHosrseBossMap;
    }

    public void setAllHosrseBossMap(ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, CrosshorseBossData>> allHosrseBossMap) {
        this.allHosrseBossMap = allHosrseBossMap;
    }

    public ConcurrentHashMap<Integer, ConcurrentHashMap<Long, BossHaveFollow>> getBossFollowList() {
        return bossFollowList;
    }

    public void setBossFollowList(ConcurrentHashMap<Integer, ConcurrentHashMap<Long, BossHaveFollow>> bossFollowList) {
        this.bossFollowList = bossFollowList;
    }
}
