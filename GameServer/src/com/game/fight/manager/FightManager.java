package com.game.fight.manager;

import com.game.fight.script.IFightManagerScript;
import com.game.fight.script.IFightTriggerScirpt;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;


public class FightManager {
    public static FightManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IFightManagerScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.FightManagerBaseScript);
        if (is instanceof IFightManagerScript) {
            return (IFightManagerScript) is;
        }
        return null;
    }

    public IFightTriggerScirpt trigger() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.FightTriggerBaseScript);
        if (is instanceof IFightTriggerScirpt) {
            return (IFightTriggerScirpt) is;
        }
        return null;
    }

    private enum Singleton {
        INSTANCE;
        FightManager manager;

        Singleton() {
            this.manager = new FightManager();
        }

        FightManager getProcessor() {
            return manager;
        }
    }
}
