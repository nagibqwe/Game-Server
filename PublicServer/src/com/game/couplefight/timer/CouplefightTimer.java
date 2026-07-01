package com.game.couplefight.timer;

import com.game.manager.Manager;
import com.game.peak.timer.PeakMatchEvent;
import com.game.server.filter.InnerMsgImpl;
import game.core.command.ICommand;
import game.core.timer.TimerEvent;

/**
 * @Desc 仙侣对决全局定时器
 * @Date 2020/11/16 18:03
 * @Auth ZUncle
 */
public class CouplefightTimer extends TimerEvent {

    /**
     * 循环事件
     */
    public CouplefightTimer() {
        super(-1, 0, 1000);
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        Manager.couplefightManager.addCommand(new ICommand() {
            @Override
            public void action() {
                Manager.couplefightManager.getScript().refresh();
            }
        });
    }

}
