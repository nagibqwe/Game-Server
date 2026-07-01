package com.game.peak.timer;

import com.game.manager.Manager;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2021/3/26 16:29
 * @Auth ZUncle
 */
public class PeakZeroTickEvent implements ICommand {

    final Logger logger = LogManager.getLogger(PeakZeroTickEvent.class);

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        try {
            Manager.peakManager.deal().zeroClockDeal();
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
