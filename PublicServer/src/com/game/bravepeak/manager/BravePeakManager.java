package com.game.bravepeak.manager;

import com.game.bravepeak.script.IBravePeakScript;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import game.core.script.IScript;

public class BravePeakManager {
    /**
     * 用枚举来实现单例
     */

    private enum Singleton {

        INSTANCE;
        BravePeakManager manager;

        Singleton() {
            this.manager = new BravePeakManager();
        }

        BravePeakManager getProcessor() {
            return manager;
        }
    }

    public static BravePeakManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IBravePeakScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.BravePeakScript);
        if (is instanceof IBravePeakScript) {
            return (IBravePeakScript) is;
        }
        return null;
    }
}
