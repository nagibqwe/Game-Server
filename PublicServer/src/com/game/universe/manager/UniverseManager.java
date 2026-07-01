package com.game.universe.manager;

import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.universe.script.IUniverseWarScript;
import game.core.script.IScript;

/**
 * 太虚战场管理类
 */
public class UniverseManager {

    private enum Singleton {
        INSTANCE;

        UniverseManager manager;

        Singleton() {
            this.manager = new UniverseManager();
        }

        UniverseManager getProcessor() {
            return manager;
        }
    }

    public static UniverseManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IUniverseWarScript manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.UniverseWarScript);
        if (is instanceof IUniverseWarScript) {
            return (IUniverseWarScript) is;
        }
        return null;
    }

}
