package com.game.soulbeast.manager;

import com.data.Global;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.soulbeast.script.ISoulBeast;
import com.game.soulbeast.structs.SoulBeastConst;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 魂兽管理类
 * Created by zcd on 2018/5/4.
 */
public class SoulBeastManager {

    private static final Logger log = LogManager.getLogger(SoulBeastManager.class);

    public ISoulBeast deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.SoulBeastBaseScript);
        if (is instanceof ISoulBeast) {
            return (ISoulBeast) is;
        } else {
            return null;
        }
    }

    /**
     * 获取魂兽装备剩余的背包格子数
     * @param player
     * @return
     */
    public int getEmptyGridNum(Player player) {
        return Global.BossOld2_BagNum - player.getSoulBeastInfo().getSoulBeastEquipMap().size();
    }

    //用枚举来实现单例
    private enum Singleton {

        INSTANCE;
        SoulBeastManager manager;

        Singleton() {
            this.manager = new SoulBeastManager();
        }

        SoulBeastManager getProcessor() {
            return manager;
        }
    }

    public static SoulBeastManager getInstance() {
        return SoulBeastManager.Singleton.INSTANCE.getProcessor();
    }
}
