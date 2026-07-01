package com.game.kaoshangling.manager;

import com.game.kaoshangling.script.*;
import com.game.immortalsoul.manager.ImmortalSoulManager;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KaoShangLingManager {
    private static final Logger log = LogManager.getLogger(ImmortalSoulManager.class);

    public static final int EnKaoShangLingHorse = 1;
    public static final int EnKaoShangLingPet = 2;
    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        KaoShangLingManager manager;

        Singleton() {
            this.manager = new KaoShangLingManager();
        }

        KaoShangLingManager getProcessor() {
            return manager;
        }
    }

    //KaoShangLingManager
    public static KaoShangLingManager getInstance() {
        return KaoShangLingManager.Singleton.INSTANCE.getProcessor();
    }

    public IKaoShangLingScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.KaoShangLingScript);
        return (IKaoShangLingScript) is;
    }
}
