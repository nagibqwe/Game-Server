package com.game.eightdiagrams.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by 542 on 2019/10/14.
 */
public class EightDiagramsTimer extends TimerEvent {
    private static final Logger log = LogManager.getLogger(EightDiagramsTimer.class);

    public EightDiagramsTimer(int loop, long delay) {
        super(loop, delay, 0);
    }
    @Override
    public void action() {
        Manager.eightDiagramsManager.deal().eightDiagramsTimerLoop();
    }
}
