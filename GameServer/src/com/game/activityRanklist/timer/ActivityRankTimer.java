package com.game.activityRanklist.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ActivityRankTimer extends TimerEvent {

    private static final Logger logger = LogManager.getLogger(ActivityRankTimer.class);

    private long lastCheckDay = TimeUtils.getOpenServerDay();

    public ActivityRankTimer() {
        super(-1, 0, 10 * 1000);
    }

    @Override
    public void action() {
        int nowDay = TimeUtils.getOpenServerDay();
        try {
            Manager.activityRankManager.deal().tick(nowDay, lastCheckDay);
        } catch (Exception e) {
            logger.error(e, e);
        } finally {
            lastCheckDay = nowDay;
        }
    }
}