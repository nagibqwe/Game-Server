package com.game.command.manager;

import com.game.command.scripts.ICommandScript;
import com.game.command.structs.CommandData;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {

    private final static Logger log = LogManager.getLogger(CommandManager.class);

    //公会战信息 地图ID》区服标识，指挥实体
    private ConcurrentHashMap<Long, ConcurrentHashMap<String, CommandData>> commandDataMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, ConcurrentHashMap<String, CommandData>> getCommandDataMap() {
        return commandDataMap;
    }

    public void setCommandDataMap(ConcurrentHashMap<Long, ConcurrentHashMap<String, CommandData>> commandDataMap) {
        this.commandDataMap = commandDataMap;
    }

    private enum Singleton {

        INSTANCE;
        CommandManager manager;

        Singleton() {
            this.manager = new CommandManager();
        }

        CommandManager getProcessor() {
            return manager;
        }
    }

    public static CommandManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public ICommandScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CommandScript);
        if (is instanceof ICommandScript) {
            return (ICommandScript) is;
        } else {
            return null;
        }
    }
}
