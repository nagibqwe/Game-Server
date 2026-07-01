package com.game.crossrank.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;

/**
 * 跨服世界等级timer
 * Created by cxl on 2021/4/2.
 */
public class CrossWorldLvTimer  extends TimerEvent {

    /**
     * 一个小时执行一次
     * @param loop
     * @param delay
     */
    public CrossWorldLvTimer(int loop, long delay) {
        super(loop, delay, 60*60*1000);
    }

    @Override
    public void action() {
        Manager.crossRankManager.deal().calcAllCrossWorldLv();
    }
}
