package com.game.ranklist.timer;

import com.game.ranklist.manager.RankListManager;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 排行榜刷新定时器
 */
public class RankListSortTimer extends TimerEvent {

    private static final Logger log = LogManager.getLogger(RankListSortTimer.class);

    public RankListSortTimer(long delay) {
        super(-1, delay, 60 * 1000);
    }

    @Override
    public void action() {
        try {
            RankListManager.getInstance().deal().tick();
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
