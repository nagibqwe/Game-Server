package com.game.gm.manager;

import com.game.gm.script.IGmScript;
import com.game.manager.Manager;
import com.game.script.struct.ScriptEnum;
import game.core.script.IScript;

/**
 * @Desc TODO
 * @Date 2021/6/24 15:03
 * @Auth ZUncle
 */
public class GmManager {

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        INSTANCE;
        GmManager manager;

        Singleton() {
            this.manager = new GmManager();
        }

        GmManager getProcessor() {
            return manager;
        }
    }

    public static GmManager getInstance() {
        return GmManager.Singleton.INSTANCE.getProcessor();
    }


    public IGmScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GmScript);
        return (IGmScript) is;
    }

}
