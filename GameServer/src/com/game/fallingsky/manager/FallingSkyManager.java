package com.game.fallingsky.manager;

import com.game.fallingsky.script.IFallingSky;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;

/**
 * 天禁令
 * Created by cxl on 2020/11/9.
 */
public class FallingSkyManager {


    private int round = 0;

    private long nextRoundTime = 0l;

    //周期阶段
    private int phaseRound =0;

    //下周起结束时间
    private long nextPhaseRoundTime =0l;

    //一周天数
    public static int WeekDay = 7;

    public static FallingSkyManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private enum Singleton {
        INSTANCE;
        FallingSkyManager manager;

        Singleton() {
            this.manager = new FallingSkyManager();
        }
        FallingSkyManager getProcessor() {
            return manager;
        }
    }

    public IFallingSky deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.FallingSkyScript);
        if (is instanceof IFallingSky) {
            return (IFallingSky) is;
        }
        return null;
    }

    public void loadData() {
        deal().loadData();
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public long getNextRoundTime() {
        return nextRoundTime;
    }

    public void setNextRoundTime(long nextRoundTime) {
        this.nextRoundTime = nextRoundTime;
    }

    public int getPhaseRound() {
        return phaseRound;
    }

    public void setPhaseRound(int phaseRound) {
        this.phaseRound = phaseRound;
    }

    public long getNextPhaseRoundTime() {
        return nextPhaseRoundTime;
    }

    public void setNextPhaseRoundTime(long nextPhaseRoundTime) {
        this.nextPhaseRoundTime = nextPhaseRoundTime;
    }
}
