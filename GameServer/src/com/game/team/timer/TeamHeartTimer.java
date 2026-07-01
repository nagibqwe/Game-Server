/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.team.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author hewei@haowan123.com
 */
public class TeamHeartTimer extends TimerEvent{
    
    private static final Logger log = LogManager.getLogger(TeamHeartTimer.class);
    
    public TeamHeartTimer(){
        super(-1, 0, 1000);
    }
    
    @Override
    public void action() {
        try {
            Manager.teamManager.deal().tick();
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
