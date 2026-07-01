/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.timer;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.structs.IAction;
import game.core.script.IScript;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class ServerHeartTimer extends TimerEvent {

    protected Logger log = LogManager.getLogger(ServerHeartTimer.class);

    public ServerHeartTimer() {
        super(-1, 0, 1000);

    }

    @Override
    public void action() {
        try {

            IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ServerHeartBaseScript);
            if (is instanceof IAction) {
                IAction ia = (IAction) is;
                ia.action();
            }
        } catch (Exception e) {
            log.error(e, e);
        }

    }
}
