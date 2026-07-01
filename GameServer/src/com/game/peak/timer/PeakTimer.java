package com.game.peak.timer;

import com.game.manager.Manager;
import game.core.command.ICommand;
import game.core.timer.TimerEvent;

/**
 * @Desc TODO
 * @Date 2020/11/5 15:03
 * @Auth ZUncle
 */
public class PeakTimer extends TimerEvent {

    ICommand cmd = new PeakMatchEvent();
    /**
     * 循环事件
     */
    public PeakTimer() {
        super(-1, 0, 5 * 1000);
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        Manager.peakManager.addCommand(cmd);
    }
}
