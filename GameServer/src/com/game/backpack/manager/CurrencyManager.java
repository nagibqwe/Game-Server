package com.game.backpack.manager;

import com.game.backpack.script.ICurrencyManagerScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 货币管理
 * @author hank
 */
public class CurrencyManager {

    private static final Logger log = LogManager.getLogger("CurrencyManager");

    private enum Singleton {
        INSTANCE;
        CurrencyManager processor;

        Singleton() {
            this.processor = new CurrencyManager();
        }

        CurrencyManager getProcessor() {
            return processor;
        }
    }

    public static CurrencyManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public ICurrencyManagerScript manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CurrencyManagerBaseScript);
        if (is instanceof ICurrencyManagerScript) {
            return (ICurrencyManagerScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }
}
