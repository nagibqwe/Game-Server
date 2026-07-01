package com.game.worldbonfire.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Description
 * @auther lw
 * @create 2019-10-21 15:54
 */
public class WorldBonfireMatchTimer extends TimerEvent {
    private static final Logger logger = LogManager.getLogger(WorldBonfireMatchTimer.class);

    public WorldBonfireMatchTimer() {
        super(-1, 0, 1000);
    }
    @Override
    public void action() {
        long now = TimeUtils.Time();
        try {
            Manager.worldBonfireManager.manager().tickWorldBonfireMatch();
            Manager.worldBonfireManager.manager().tickWorldBonfireMatchTeam();
            long dealtime = TimeUtils.Time() - now;
            if (dealtime > 300) {
                logger.error("DailyActiveTimer deal too long time;dealTime:" + dealtime);
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
