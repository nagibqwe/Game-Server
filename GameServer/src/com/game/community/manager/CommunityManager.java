package com.game.community.manager;

import com.game.community.scripts.ICommunityScript;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommunityManager {
    private static final Logger log = LogManager.getLogger("CommunityManager");
    public static CommunityManager getInstance() {
        return CommunityManager.Singleton.INSTANCE.getProcessor();
    }

    private enum Singleton {

        INSTANCE;
        CommunityManager manager;
        Singleton() {
            this.manager = new CommunityManager();
        }
        CommunityManager getProcessor() {
            return manager;
        }
    }

    public ICommunityScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CommunityScript);
        if (is instanceof ICommunityScript) {
            return (ICommunityScript) is;
        } else {
            log.error("没有实现社区脚本");
            return null;
        }
    }
}
