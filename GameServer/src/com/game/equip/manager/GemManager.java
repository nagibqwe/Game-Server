package com.game.equip.manager;

import com.game.equip.script.IGemScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;

public class GemManager {

    public static final int Gem_Type = 1;

    public static final int Jade_Type = 2;

    private enum Singleton {

        INSTANCE;
        GemManager manager;

        Singleton() {
            this.manager = new GemManager();
        }

        GemManager getProcessor() {
            return manager;
        }
    }

    public static GemManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IGemScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GemBaseScript);
        return (IGemScript) is;
    }
}
