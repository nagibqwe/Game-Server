package com.game.nature.manager;

import com.game.manager.Manager;
import com.game.nature.script.INatureScript;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NatureManager {
    private static final Logger log = LogManager.getLogger(NatureManager.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        NatureManager manager;

        Singleton() {
            this.manager = new NatureManager();
        }

        NatureManager getProcessor() {
            return manager;
        }
    }

    /**
     * 获取NatureManager的实例对象
     * */
    public static NatureManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public INatureScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.NatureBaseScript);
        if (is instanceof INatureScript) {
            return (INatureScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }

    }
}
