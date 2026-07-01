package com.game.couplefight.timer;

import com.game.manager.Manager;
import game.core.command.ICommand;
import game.core.timer.TimerEvent;

import java.util.concurrent.Executors;

/**
 * 冠军赛定时器
 * @Auther: gouzhongliang
 * @Date: 2021/7/13 15:43
 */
public class CouplefightChampionTimer extends TimerEvent {

    /**
     * 循环事件
     */
    public CouplefightChampionTimer() {
        super(4, 0, 120 * 1000);
    }

    @Override
    public void action() {
        Manager.couplefightManager.addCommand(new ICommand() {

            @Override
            public void action() {
                Manager.couplefightManager.getScript().onChampionNextRound();
            }
        });
    }
}
