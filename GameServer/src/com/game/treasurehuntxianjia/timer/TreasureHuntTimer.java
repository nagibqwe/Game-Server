package com.game.treasurehuntxianjia.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by cxl on 2020/2/27.
 */
public class TreasureHuntTimer extends TimerEvent {

    private static final Logger logger = LogManager.getLogger(TreasureHuntTimer.class);

    public TreasureHuntTimer(long delay) {
        super(-1, delay,   1000);
    }

    @Override
    public void action() {
        try {
            Manager.treasureHuntXianjiaManager.deal().treasureTick();
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
