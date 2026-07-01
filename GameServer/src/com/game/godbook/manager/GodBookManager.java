package com.game.godbook.manager;

import com.game.godbook.script.IGodBookScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;

public class GodBookManager {

    private GodBookManager(){}

    public enum Singleton{
        INSTANCE;
        GodBookManager manager;

        Singleton() {
            this.manager = new GodBookManager();
        }

        public GodBookManager getProcessor() {
            return manager;
        }
    }

    public static GodBookManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IGodBookScript deal() {
        IScript script = Manager.scriptManager.GetScriptClass(ScriptEnum.GodBookBaseScript);
        return (IGodBookScript) script;
    }

}
