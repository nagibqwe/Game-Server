package com.game.dailyactive.manager;

import com.game.dailyactive.scripts.IDailyActiveScript;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import game.core.script.IScript;

import java.util.concurrent.ConcurrentHashMap;

public class DailyActiveManager {

    public static int DailyLastTime = 30;//剩余时间
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
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.DailyActiveScript);
        if (is instanceof IDailyActiveScript) {
            return (IDailyActiveScript) is;
        }
        return null;
    }
}
