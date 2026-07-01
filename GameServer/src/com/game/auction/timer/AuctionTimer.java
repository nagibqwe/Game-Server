package com.game.auction.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Description
 * @auther lw
 * @create 2019-10-08 18:08
 */
public class AuctionTimer extends TimerEvent {

    private static final Logger logger = LogManager.getLogger(AuctionTimer.class);

    public AuctionTimer(long delay) {
        super(-1, delay, 60 * 1000);
    }

    @Override
    public void action() {
        try {
            Manager.auctionManager.manager().auctionTick();
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
