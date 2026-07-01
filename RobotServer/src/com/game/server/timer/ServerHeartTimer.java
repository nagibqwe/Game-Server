/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.timer;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerHeartTimer extends TimerEvent {

    protected Logger log = LogManager.getLogger(ServerHeartTimer.class);

    public ServerHeartTimer(int loop, long delay, long period) {
        super(loop, delay);
        //        super(loop, delay, period);
    }

    @Override
    public void action() {
        try {
            //
        } catch (Exception e) {
            log.error(e, e);
        }

    }
}
