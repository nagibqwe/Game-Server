package com.game.peak.timer;

import com.game.db.bean.PeakBean;
import com.game.manager.Manager;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2020/11/6 14:59
 * @Auth ZUncle
 */
public class PeakCloneEvent implements ICommand {

    final Logger logger = LogManager.getLogger(PeakCloneEvent.class);

    final PeakBean win;
    final int winScore;
    final PeakBean loser;
    final int loserScore;

    public PeakCloneEvent(PeakBean win, int winScore, PeakBean loser, int loserScore) {
        this.win = win;
        this.winScore = winScore;
        this.loser = loser;
        this.loserScore = loserScore;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        try {
            Manager.peakManager.deal().cloneOver(win, winScore, loser, loserScore);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
