package com.game.copymap.manager;

import com.game.copymap.script.ICopyMapScript;
import com.game.dailyactivity.script.IDailyActivityScript;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import game.core.script.IScript;

public class CopyMapManager {
    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        CopyMapManager processor;

        Singleton() {
            this.processor = new CopyMapManager();
        }

        CopyMapManager getProcessor() {
            return processor;
        }
    }

    public static CopyMapManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public ICopyMapScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CopyMapScript);
        if (is instanceof ICopyMapScript) {
            return (ICopyMapScript) is;
        }
        return null;
    }

}
