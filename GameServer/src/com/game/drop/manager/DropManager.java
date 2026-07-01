package com.game.drop.manager;

import com.game.drop.script.IDropScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author admin
 */
public class DropManager {

    private static final Logger log = LogManager.getLogger(DropManager.class);

    private enum Singleton {
        INSTANCE;

        DropManager processor;

        Singleton() {
            this.processor = new DropManager();
        }

        DropManager getProcessor() {
            return processor;
        }
    }

    public static DropManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }


    public IDropScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.DropBaseScript);
        if (is instanceof IDropScript) {
            return (IDropScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

}
