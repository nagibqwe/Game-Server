package com.game.guild.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Description
 * @auther lw
 * @create 2020-1-10 10:08
 */
public class GuildTimer extends TimerEvent {

    private static final Logger logger = LogManager.getLogger(GuildTimer.class);

    public GuildTimer(long delay) {
        super(-1, delay, 60 * 1000);
    }

    @Override
    public void action() {
        try {
            Manager.guildsManager.manager().tick();
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
