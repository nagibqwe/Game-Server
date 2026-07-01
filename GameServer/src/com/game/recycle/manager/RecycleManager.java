package com.game.recycle.manager;

import com.game.equip.script.IEquipScript;
import com.game.manager.Manager;
import com.game.recycle.script.IRecycleScript;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RecycleManager {
    private static final Logger log = LogManager.getLogger(RecycleManager.class);
    /**
     * 获取RecycleManager的实例对象
     */
    public static RecycleManager getInstance() {
        return RecycleManager.Singleton.INSTANCE.getProcessor();
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        /**
         * 一个枚举的元素，它就代表了Singleton的一个实例
         */
        INSTANCE;
        RecycleManager manager;

        Singleton() {
            this.manager = new RecycleManager();
        }

        RecycleManager getProcessor() {
            return manager;
        }
    }

    public IRecycleScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.RecycleBaseScript);
        if (is instanceof IRecycleScript) {
            return (IRecycleScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }
}
