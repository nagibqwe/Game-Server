package com.game.home.manager;

import com.game.home.script.IHomeScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;

/**
 * @Desc TODO
 * @Date 2021/6/28 20:40
 * @Auth ZUncle
 */
public class HomeManager {

    private enum Singleton {

        INSTANCE;
        HomeManager manager;

        Singleton() {
            this.manager = new HomeManager();
        }

        HomeManager getProcessor() {
            return manager;
        }
    }

    public static HomeManager getInstance() {
        return HomeManager.Singleton.INSTANCE.getProcessor();
    }

    public IHomeScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.HomeScript);
        return (IHomeScript) is;
    }

}
