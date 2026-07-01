package com.game.cangbaoge.manager;

import com.game.cangbaoge.scripts.ICangbaogeScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;

/**
 * Created by cxl on 2020/9/2.
 */
public class CangbaogeManager {

    private int round = 0;

    private long nextRoundTime = 0l;


    public void loadData() {

        deal().loadData();
    }

    private enum Singleton {
        INSTANCE;
        CangbaogeManager manager;

        Singleton() {
            this.manager = new CangbaogeManager();
        }

        CangbaogeManager getProcessor() {
            return manager;
        }
    }

    public static CangbaogeManager getInstance() {
        return CangbaogeManager.Singleton.INSTANCE.getProcessor();
    }

    public ICangbaogeScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CangbaogeScript);
        if (is instanceof ICangbaogeScript) {
            return (ICangbaogeScript) is;
        }
        return null;
    }

    public void setRound(int round){this.round = round;}

    public int getRound(){return  round;}

    public void setNextRoundTime(long nextRoundTime){this.nextRoundTime = nextRoundTime;}

    public long getNextRoundTime(){return nextRoundTime;}
}
