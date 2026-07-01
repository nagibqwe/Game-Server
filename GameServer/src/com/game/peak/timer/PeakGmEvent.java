package com.game.peak.timer;

import com.game.db.bean.PeakBean;
import com.game.manager.Manager;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2020/12/22 15:30
 * @Auth ZUncle
 */
public class PeakGmEvent implements ICommand {

    final Logger logger = LogManager.getLogger(PeakGmEvent.class);

    final long playerId;
    final int score;

    public PeakGmEvent(long playerId, int score) {
        this.playerId = playerId;
        this.score = score;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        try {
            PeakBean bean = Manager.peakManager.getPeaks().get(playerId);
            if (bean != null){
                Manager.peakManager.deal().cloneOver(bean, score);
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
