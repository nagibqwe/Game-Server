package com.game.soulArmor.manager;


import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.soulArmor.script.ISoulArmor;
import game.core.script.IScript;

/**
 * @Desc TODO
 * @Date 2020/12/23 17:37
 * @Auth ZUncle
 */
public class SoulArmorManager {

    //用枚举来实现单例
    private enum Singleton {

        INSTANCE;
        SoulArmorManager manager;

        Singleton() {
            this.manager = new SoulArmorManager();
        }

        SoulArmorManager getProcessor() {
            return manager;
        }
    }

    public static SoulArmorManager getInstance() {
        return SoulArmorManager.Singleton.INSTANCE.getProcessor();
    }


    public ISoulArmor script() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.SoulArmorScript);
        if (is == null){
            return null;
        }
        return (ISoulArmor)is;
    }

}
