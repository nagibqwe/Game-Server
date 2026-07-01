package com.game.goddevilwar.manager;


import com.game.goddevilwar.script.IGodDevilWarScript;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GodDevilWarManager {

    private static final Logger log = LogManager.getLogger(GodDevilWarManager.class);

    public IGodDevilWarScript deal(){
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GodDevilWarScript);
        if (is instanceof IGodDevilWarScript) {
            return (IGodDevilWarScript) is;
        }
        log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
        return null;
    }

    private enum Singleton {

        INSTANCE;
        GodDevilWarManager manager;

        Singleton() {
            this.manager = new GodDevilWarManager();
        }

        GodDevilWarManager getProcessor() {
            return manager;
        }
    }

    public static GodDevilWarManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
