package com.game.server.filter;

import com.game.server.script.IhandlerScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.command.ICommand;
import game.core.command.ICommandFilter;
import game.core.message.RMessage;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandlerFilter implements ICommandFilter {

    protected Logger log = LogManager.getLogger(HandlerFilter.class);

    public HandlerFilter() {
    }

    @Override
    public boolean filter(ICommand command) {
        if (command instanceof RMessage) {
            RMessage handler = (RMessage) command;
            IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.HandlerScript);
            if (is instanceof IhandlerScript) {
                return ((IhandlerScript) is).Filte_Handler(handler);
            }
        }
        return true;
    }

}
