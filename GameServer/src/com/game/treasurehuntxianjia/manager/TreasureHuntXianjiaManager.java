package com.game.treasurehuntxianjia.manager;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.treasurehuntxianjia.script.ITreasureHuntXianjia;
import game.core.script.IScript;

/**
 * Created by cxl on 2020/2/25.
 */
public class TreasureHuntXianjiaManager {


    public static final int XianjiaHuntType = 6;

    public static final int MibaoHuntType = 7;

    public static final int QingyiHuntType = 8;



    private int round = 0;

    private long nextRoundTime = 0l;


    public void loadData() {
        deal().loadData();
    }

    private enum Singleton {
        INSTANCE;
        TreasureHuntXianjiaManager manager;

        Singleton() {
            this.manager = new TreasureHuntXianjiaManager();
        }

        TreasureHuntXianjiaManager getProcessor() {
            return manager;
        }
    }

    public static TreasureHuntXianjiaManager getInstance() {
        return TreasureHuntXianjiaManager.Singleton.INSTANCE.getProcessor();
    }
    public ITreasureHuntXianjia deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.TreasureHuntXianjiaScript);
        if (is instanceof ITreasureHuntXianjia) {
            return (ITreasureHuntXianjia) is;
        }
        return null;
    }

    public void setRound(int round){this.round = round;}

    public int getRound(){return  round;}

    public void setNextRoundTime(long nextRoundTime){this.nextRoundTime = nextRoundTime;}

    public long getNextRoundTime(){return nextRoundTime;}



}
