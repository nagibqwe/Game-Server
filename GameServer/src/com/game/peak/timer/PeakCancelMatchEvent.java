package com.game.peak.timer;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2020/11/10 11:57
 * @Auth ZUncle
 */
public class PeakCancelMatchEvent implements ICommand {

    final Logger logger = LogManager.getLogger(PeakCancelMatchEvent.class);

    final Player player;

    public PeakCancelMatchEvent(Player player) {
        this.player = player;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        try {
            Manager.peakManager.deal().reqCancelPeakMatch(player);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
