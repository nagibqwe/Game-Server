package com.game.pet.manager;

import com.game.manager.Manager;
import com.game.pet.script.IPetScript;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 宠物管理类
 */
public class PetManager {
    private final static Logger log = LogManager.getLogger(PetManager.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        PetManager manager;

        Singleton() {
            this.manager = new PetManager();
        }

        PetManager getProcessor() {
            return manager;
        }
    }

    //获取PetManager的实例对象
    public static PetManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    //宠物脚本调用入口
    public IPetScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.PetBaseScript);
        if (is instanceof IPetScript) {
            return (IPetScript) is;
        } else {
            return null;
        }
    }

    /**
     * 玩家上线
     */
    public void online(Player player) {
        deal().online(player, false);
    }

    /**
     * 玩家下线
     */
    public void offLine(Player player) {
        deal().offLine(player);
    }

    /**
     * 获取出征的宠物
     */
    public Pet getBattlePet(Player player) {
        return deal().getBattlePet(player);
    }
}
