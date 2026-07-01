package com.game.marriage.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Description
 * @auther admin
 * @create 2020-06-04 10:08
 */
public class MarriageTimer extends TimerEvent {

    private static final Logger logger = LogManager.getLogger(MarriageTimer.class);

    public MarriageTimer() {
        super(-1, 0, 10 * 1000);
    }

    @Override
    public void action() {
        try {
            Manager.marriageManager.manager().tick();
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
