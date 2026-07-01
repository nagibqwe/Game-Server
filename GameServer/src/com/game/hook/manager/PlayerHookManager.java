package com.game.hook.manager;

import com.game.hook.script.IPlayerHookScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;

public class PlayerHookManager {

    private enum Singleton {

        INSTANCE;
        PlayerHookManager manager;

        Singleton() {
            this.manager = new PlayerHookManager();
        }

        PlayerHookManager getProcessor() {
            return manager;
        }
    }

    public static PlayerHookManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public static final int ItemRate = 1;
    public static final int TeamRate = 2;
    public static final int VipRate = 3;
    public static final int AmuletRate = 4;
    public static final int WorldLvRate = 5;
    public static final int FaBaoRate = 6;
    public static final int Other = 7;//不用占坑的，客户端自己算的
    public static final int FcRate = 8;
    public static final int MarriageRate = 9;
    public static final int ShouZhuo = 10;
    public static final int EerHuan = 11;

    public IPlayerHookScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.PlayerHookBaseScript);
        return (IPlayerHookScript) is;
    }

}
