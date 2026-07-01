package com.game.cangbaoge.timer;

import com.game.manager.Manager;
import com.game.treasurehuntxianjia.timer.TreasureHuntTimer;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by cxl on 2020/9/3.
 */
public class CangbaogeTimer extends TimerEvent {
    private static final Logger logger = LogManager.getLogger(TreasureHuntTimer.class);


    public CangbaogeTimer(long delay) {
        super(-1, delay,   1000);
    }

    @Override
    public void action() {
        try {
            Manager.cangbaogeManager.deal().tick();
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
