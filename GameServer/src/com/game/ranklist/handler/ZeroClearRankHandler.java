package com.game.ranklist.handler;

import com.game.manager.Manager;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 0点时清理排行榜的数据
 */
public class ZeroClearRankHandler implements ICommand {

    private static final Logger log = LogManager.getLogger(ZeroClearRankHandler.class);

    @Override
    public void action() {
        try {
            Manager.rankListManager.deal().onZeroClearRank();
        } catch (Exception ex) {
            log.error(ex, ex);
        }
    }

}
