package com.game.guild.manager;

import com.game.guild.script.IGuildScript;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import game.core.script.IScript;

public class GuildManager {
    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        GuildManager processor;

        Singleton() {
            this.processor = new GuildManager();
        }

        GuildManager getProcessor() {
            return processor;
        }
    }

    public static GuildManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IGuildScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GuildBaseScript);
        if (is instanceof IGuildScript) {
            return (IGuildScript) is;
        }
        return null;
    }

}
