package com.game.treasurehuntwuyou.manager;

import com.data.Global;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.treasurehuntwuyou.script.ITreasureHuntWuyou;
import game.core.script.IScript;
import game.core.util.TimeUtils;

import java.text.SimpleDateFormat;

/**
 * 无忧宝库
 * gouzhongliang
 */
public enum TreasureHuntWuyouManager {

    instance;

    TreasureHuntWuyouManager(){
    }
    /**抽奖类型*/
    public static final int HUNT_TYPE = 10;
    /**奖池类型*/
    public static final int REWARD_TYPE = 10;

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ITreasureHuntWuyou getScript() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.TreasureHuntWuyouScript);
        if (is instanceof ITreasureHuntWuyou) {
            return (ITreasureHuntWuyou) is;
        }
        return null;
    }

}
