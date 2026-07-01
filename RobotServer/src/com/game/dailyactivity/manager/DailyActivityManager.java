package com.game.dailyactivity.manager;

import com.game.dailyactivity.script.IDailyActivityScript;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import game.core.script.IScript;

public class DailyActivityManager {
    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        DailyActivityManager processor;

        Singleton() {
            this.processor = new DailyActivityManager();
        }

        DailyActivityManager getProcessor() {
            return processor;
        }
    }

    public static DailyActivityManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IDailyActivityScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.DailyActiveBaseScript);
        if (is instanceof IDailyActivityScript) {
            return (IDailyActivityScript) is;
        }
        return null;
    }

}
