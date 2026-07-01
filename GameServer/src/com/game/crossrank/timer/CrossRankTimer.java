package com.game.crossrank.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by cxl on 2020/4/13.
 */
public class CrossRankTimer extends TimerEvent {

    private static final Logger LOG = LogManager.getLogger("CrossRankTimer");

    //60分钟同步一次
    public CrossRankTimer() {
        super(-1, 0,3600000);
    }

    @Override
    public void action() {

        try {
            Manager.crossRankManager.deal().sendRankInfoToPublic();
        }catch (Exception e) {
            LOG.error(e, e);
        }
    }
}
