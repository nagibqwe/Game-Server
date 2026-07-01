package com.game.copymap.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 副本房间组队计数器
 */
public class CopyMapTeamTimer extends TimerEvent {

    private final static Logger LOG = LogManager.getLogger(CopyMapTeamTimer.class);

    public CopyMapTeamTimer() {
        super(-1, 0, 1000);
    }

    @Override
    public void action() {
        try {
            Manager.copyMapManager.manager().copyMapTeamReady();
        } catch (Exception e) {
            LOG.error(e, e);
        }
    }

}
