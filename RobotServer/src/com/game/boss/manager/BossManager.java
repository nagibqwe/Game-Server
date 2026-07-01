package com.game.boss.manager;

import com.game.boss.script.IBossScript;
import com.game.dailyactivity.script.IDailyActivityScript;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import game.core.script.IScript;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BossManager {
    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        BossManager processor;

        Singleton() {
            this.processor = new BossManager();
        }

        BossManager getProcessor() {
            return processor;
        }
    }

    public static BossManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IBossScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.BossBaseScript);
        if (is instanceof IBossScript) {
            return (IBossScript) is;
        }
        return null;
    }

    private ConcurrentHashMap<Integer, Integer> numMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer, Integer> getNumMap() {
        return numMap;
    }

    public void setNumMap(ConcurrentHashMap<Integer, Integer> numMap) {
        this.numMap = numMap;
    }
}
