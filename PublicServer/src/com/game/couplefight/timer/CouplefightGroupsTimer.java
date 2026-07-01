package com.game.couplefight.timer;

import com.game.manager.Manager;
import game.core.command.ICommand;
import game.core.timer.TimerEvent;

/**
 * 小组赛定时器
 * @Auther: gouzhongliang
 * @Date: 2021/7/13 15:43
 */
public class CouplefightGroupsTimer extends TimerEvent {

    /**
     * 循环事件
     */
    public CouplefightGroupsTimer() {
        super(10, 0, 120 * 1000);
    }

    @Override
    public void action() {
        Manager.couplefightManager.addCommand(new ICommand() {
            @Override
            public void action() {
                Manager.couplefightManager.getScript().onGroupsNextRound();
            }
        });
    }
}
