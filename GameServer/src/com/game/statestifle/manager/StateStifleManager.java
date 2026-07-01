package com.game.statestifle.manager;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.statestifle.script.IStateStifleScript;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by 瞿冰冰
 * 2019/9/4
 */
public class StateStifleManager {

    public IStateStifleScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.StateStifleBaseScript);
        if (is instanceof IStateStifleScript) {
            return (IStateStifleScript) is;
        }
        return null;
    }


    //用枚举来实现单例
    private enum Singleton {

        INSTANCE;
        StateStifleManager manager;

        Singleton() {
            this.manager = new StateStifleManager();
        }

        StateStifleManager getProcessor() {
            return manager;
        }
    }

    public static StateStifleManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
