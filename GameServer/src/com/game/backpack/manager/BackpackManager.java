package com.game.backpack.manager;

import com.game.backpack.script.IBackpackManagerScript;
import com.game.backpack.script.IBackpackScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 背包管理类
 */
public class BackpackManager {
    private static final Logger log = LogManager.getLogger(BackpackManager.class);

    private enum Singleton {
        INSTANCE;
        BackpackManager processor;

        Singleton() {
            this.processor = new BackpackManager();
        }

        BackpackManager getProcessor() {
            return processor;
        }
    }

    public static BackpackManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IBackpackScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.BackpackBaseScript);
        if (is instanceof IBackpackScript) {
            return (IBackpackScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public IBackpackManagerScript manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.BackpackManagerBaseScript);
        if (is == null) {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
        return (IBackpackManagerScript) is;
    }
}
