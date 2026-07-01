package com.game.treasurehunt.manager;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.treasurehunt.script.ITreasureHuntScript;
import game.core.script.IScript;

/**
 * Created by 瞿冰冰
 * 2019/7/9
 */
public class TreasureHuntManager {


    public final static int ONE_TIMES = 1;//1次

    public final static int TEN_TIMES = 10;//10次



    /**
     * 枚举实现单例
     */
    private enum Singleton {
        INSTANCE;
        TreasureHuntManager manager;

        Singleton() {
            this.manager = new TreasureHuntManager();
        }

        TreasureHuntManager getProcessor() {
            return manager;
        }
    }

    public static TreasureHuntManager getInstance() {
        return TreasureHuntManager.Singleton.INSTANCE.getProcessor();
    }

    public ITreasureHuntScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.TreasureHuntBaseScript);
        if (is instanceof ITreasureHuntScript) {
            return (ITreasureHuntScript) is;
        }
        return null;
    }
}
