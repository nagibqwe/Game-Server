package com.game.peak.timer;

import com.game.manager.Manager;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2020/11/16 18:07
 * @Auth ZUncle
 */
public class PeakMatchEvent implements ICommand {

    final Logger logger = LogManager.getLogger(PeakMatchEvent.class);

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        try {
            Manager.peakManager.deal().match();
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
