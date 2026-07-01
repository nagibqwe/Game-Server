package com.game.equip.manager;

import com.game.equip.script.IEquipScript;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import game.core.script.IScript;

public class EquipManager {
    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        EquipManager processor;

        Singleton() {
            this.processor = new EquipManager();
        }

        EquipManager getProcessor() {
            return processor;
        }
    }

    public static EquipManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IEquipScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.EquipScript);
        if (is instanceof IEquipScript) {
            return (IEquipScript) is;
        }
        return null;
    }

}
