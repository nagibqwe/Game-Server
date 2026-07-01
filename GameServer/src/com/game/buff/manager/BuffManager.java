package com.game.buff.manager;

import com.game.buff.script.IBuffBehavior;
import com.game.buff.script.IBuffScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;

/**
 * @author admin
 */
public class BuffManager {

    private enum Singleton {

        INSTANCE;
        BuffManager manager;

        Singleton() {
            this.manager = new BuffManager();
        }

        BuffManager getProcessor() {
            return manager;
        }
    }

    public static BuffManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IBuffScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.BuffScript);
        return (IBuffScript) is;
    }

    public IBuffBehavior script(int type) {
        int scriptId = ScriptEnum.BuffScript * ScriptEnum.NodeLimit + type;
        IScript is = Manager.scriptManager.GetScriptClass(scriptId);
        if (is == null) {
            return null;
        }
        return (IBuffBehavior) is;
    }
}
