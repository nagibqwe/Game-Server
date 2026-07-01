package com.game.crossrank.manager;

import com.game.crossrank.scripts.ICrossRank;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;

/**
 * Created by 542 on 2020/4/13.
 */
public class CrossRankManager {


    public static int CrossRankMax = 10;

    private enum Singleton {

        INSTANCE;
        CrossRankManager manager;

        Singleton() {
            this.manager = new CrossRankManager();
        }

        CrossRankManager getProcessor() {
            return manager;
        }
    }

    public static CrossRankManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public ICrossRank deal() throws Exception {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CrossRankScript);
        if (is instanceof ICrossRank) {
            return (ICrossRank) is;
        }
        throw new Exception("没有实现跨服战场的副本！荣耀之战ICrossRankScript没有找到具体的实例！");
    }
}
