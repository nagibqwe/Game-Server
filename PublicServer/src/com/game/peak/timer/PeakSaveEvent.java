package com.game.peak.timer;

import com.game.manager.Manager;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2020/11/16 20:24
 * @Auth ZUncle
 */
public class PeakSaveEvent implements ICommand {

    final Logger logger = LogManager.getLogger(PeakSaveEvent.class);

    final long roleId;

    public PeakSaveEvent(long roleId) {
        this.roleId = roleId;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        try {
            Manager.peakManager.deal().saveDB(roleId);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
