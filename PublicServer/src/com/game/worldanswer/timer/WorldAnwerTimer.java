package com.game.worldanswer.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by cxl on 2019/7/15.
 */
public class WorldAnwerTimer  extends TimerEvent {
    private static final Logger log = LogManager.getLogger(WorldAnwerTimer.class);

    public WorldAnwerTimer(int loop, long delay) {
        super(loop, delay, 0);
    }
    @Override
    public void action() {
        Manager.worldAnswerManager.getIWorldAnswer().worldAnswerTimeLoop();
    }
}
