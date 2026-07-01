package com.game.dailyactive.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DailyActiveTimer extends TimerEvent {

    private static final Logger logger = LogManager.getLogger(DailyActiveTimer.class);

    private long lastCheckTime = 0;

    public DailyActiveTimer() {
        super(-1, 60000 - TimeUtils.Time() % 60000, 60 * 1000);
    }

    @Override
    public void action() {
        long now = TimeUtils.Time();
        try {
            Manager.dailyActiveManager.deal().timerTicker(now, lastCheckTime);
            Manager.questionnaireManager.deal().checkActivityOpen(now, lastCheckTime);
            long dealtime = TimeUtils.Time() - now;
            if (dealtime > 300) {
                logger.error("DailyActiveTimer deal too long time;dealTime:" + dealtime);
            }
        } catch (Exception e) {
            logger.error(e, e);
        } finally {
            lastCheckTime = now;
        }
    }
}
