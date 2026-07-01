package com.game.peak.timer;

import com.game.manager.Manager;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2020/11/5 15:45
 * @Auth ZUncle
 */
public class PeakStartEvent implements ICommand {

    final Logger logger = LogManager.getLogger(PeakStartEvent.class);

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        try {
            Manager.peakManager.deal().start();
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
