package com.game.register.manager;

import com.game.register.script.IRegisterScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class RegisterManager {

    private final static Logger log = LogManager.getLogger(RegisterManager.class);

    private final ConcurrentHashMap<Long, Player> players = new ConcurrentHashMap<>();

    private enum Singleton {

        INSTANCE;
        RegisterManager processor;

        Singleton() {
            this.processor = new RegisterManager();
        }

        RegisterManager getProcessor() {
            return processor;
        }
    }

    public static RegisterManager getInstance() {
        return RegisterManager.Singleton.INSTANCE.getProcessor();
    }

    public ConcurrentHashMap<Long, Player> getPlayers() {
        return players;
    }

    public IRegisterScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.RegisterScript);
        if (is instanceof IRegisterScript) {
            return (IRegisterScript) is;
        }
        return null;
    }
}
