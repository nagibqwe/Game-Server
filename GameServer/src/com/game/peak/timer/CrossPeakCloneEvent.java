package com.game.peak.timer;

import com.game.manager.Manager;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2020/11/16 16:23
 * @Auth ZUncle
 */
public class CrossPeakCloneEvent implements ICommand {

    final Logger logger = LogManager.getLogger(CrossPeakCloneEvent.class);

    long roleId;
    int isWin;
    long exp;

    public CrossPeakCloneEvent(long roleId, int isWin, long exp) {
        this.roleId = roleId;
        this.isWin = isWin;
        this.exp = exp;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        try {
            Manager.peakManager.deal().P2GPeakCloneResult(roleId, isWin, exp);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
