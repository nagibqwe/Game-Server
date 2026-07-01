package com.game.peak.timer;

import com.game.server.filter.InnerMsgImpl;
import game.core.command.ICommand;
import game.core.timer.TimerEvent;

/**
 * @Desc TODO
 * @Date 2020/11/16 18:03
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
        InnerMsgImpl.getInstance().addCommand(cmd);
    }

}
