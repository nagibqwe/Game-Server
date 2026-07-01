package com.game.crosshorseboss.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;

/**
 * Created by cxl on 2021/4/14.
 */
public class CrosssHorseBossTimer extends TimerEvent {


    public CrosssHorseBossTimer(int loop, long delay, long period) {
        super(loop, delay, period);
    }

    @Override
    public void action() {
        Manager.crossHorseBossManager.deal().tick();
    }
}
