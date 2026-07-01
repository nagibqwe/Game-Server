package com.game.title.manager;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.title.script.ITitleScript;
import game.core.script.IScript;

public class TitleManager {

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        TitleManager processor;

        Singleton() {
            this.processor = new TitleManager();
        }

        TitleManager getProcessor() {
            return processor;
        }
    }

    public static TitleManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }


    public ITitleScript deal() {
        IScript script = Manager.scriptManager.GetScriptClass(ScriptEnum.TitleBaseScript);
        if (script instanceof ITitleScript) {
            return (ITitleScript) script;
        } else {
            return null;
        }
    }
}
