package com.game.dailyactive.manager;

import com.game.bravepeak.struct.BravePeakMapInfo;
import com.game.dailyactive.script.IDailyActiveScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;

import java.util.concurrent.ConcurrentHashMap;

public class DailyActiveManager {

    public static BravePeakMapInfo bravePeakMapInfo = new BravePeakMapInfo();

    /**
     * 日常活动对应地图
     * key:dailyId  -> value <cloneModelId, mapId>
     */
    public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Long>> dailyMap = new ConcurrentHashMap<>();

    /**
     * 日常活动开启状态
     */
    final ConcurrentHashMap<Integer, Integer> dailyOpenState = new ConcurrentHashMap<>();

    final ConcurrentHashMap<Integer, Boolean> gmControl = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer, Integer> getDailyOpenState() {
        return dailyOpenState;
    }

    public ConcurrentHashMap<Integer, Boolean> getGmControl() {
        return gmControl;
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        DailyActiveManager processor;

        Singleton() {
            this.processor = new DailyActiveManager();
        }

        DailyActiveManager getProcessor() {
            return processor;
        }
    }

    public static DailyActiveManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IDailyActiveScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.DailyActiveBaseScript);
        if (is instanceof IDailyActiveScript) {
            return (IDailyActiveScript) is;
        }
        return null;
    }
}
