package com.game.gm.manager;

import com.game.gm.script.IGMScript;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CmdManager {

    /**
     * 获取实例对象.
     *
     * @return
     */
    public static CmdManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        CmdManager processor;

        Singleton() {
            this.processor = new CmdManager();
        }

        CmdManager getProcessor() {
            return processor;
        }
    }

    public IGMScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GMScript);
        if (is instanceof IGMScript) {
            return (IGMScript) is;
        }
        return null;
    }
}
