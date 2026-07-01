package com.game.unrealEquip.manager;


import com.game.holyEquip.manager.HolyEquipManager;
import com.game.holyEquip.script.IHolyEquipScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.unrealEquip.script.IUnrealEquip;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 幻装 CXL
 */
public class UnrealEquipManager {

    public final Logger log = LogManager.getLogger(UnrealEquipManager.class);


    public static final int  UnrealPartMin = 441;
    public static final int  UnrealPartMax = 450;

    public static UnrealEquipManager getInstance() {
        return UnrealEquipManager.Singleton.INSTANCE.getProcessor();
    }
    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        UnrealEquipManager processor;

        Singleton() {
            this.processor = new UnrealEquipManager();
        }

        UnrealEquipManager getProcessor() {
            return processor;
        }
    }

    public IUnrealEquip deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.UnrealEquipScript);
        if (is instanceof IUnrealEquip) {
            return (IUnrealEquip) is;
        } else {
            log.error("IUnrealEquip 没有找到具体的实例！");
            return null;
        }
    }
}
