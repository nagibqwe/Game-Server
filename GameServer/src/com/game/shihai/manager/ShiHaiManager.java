package com.game.shihai.manager;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.shihai.script.IShiHaiHandler;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShiHaiManager {

    public final Logger log = LogManager.getLogger(ShiHaiManager.class);

    public IShiHaiHandler deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ShiHaiBaseScript);
        if (is instanceof IShiHaiHandler) {
            return (IShiHaiHandler) is;
        } else {
            log.error("ShiHaiManager.deal()，没有找到具体的实例！");
            return null;
        }
    }

    public static ShiHaiManager getInstance() {
        return ShiHaiManager.Singleton.INSTANCE.getProcessor();
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        ShiHaiManager processor;

        Singleton() {
            this.processor = new ShiHaiManager();
        }

        ShiHaiManager getProcessor() {
            return processor;
        }
    }
}
