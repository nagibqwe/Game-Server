package com.game.guildcrossfud.manager;

import com.game.guildcrossfud.script.IFudScript;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * @Desc TODO
 * @Date 2021/2/3 17:45
 * @Auth ZUncle
 */
public class CrossFudManager {

    final Logger logger = LogManager.getLogger(CrossFudManager.class);

    final HashMap<Long, MapObject> fud = new HashMap<>();

    public HashMap<Long, MapObject> getFud() {
        return fud;
    }

    public IFudScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CrossFudScript);
        if (is == null) {
            logger.error("未找到脚本CrossFudScript={}", ScriptEnum.CrossFudScript);
            return null;
        }
        return (IFudScript) is;
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        CrossFudManager manager;

        Singleton() {
            this.manager = new CrossFudManager();
        }

        CrossFudManager getProcessor() {
            return manager;
        }
    }

    public static CrossFudManager getInstance() {
        return CrossFudManager.Singleton.INSTANCE.getProcessor();
    }

}
