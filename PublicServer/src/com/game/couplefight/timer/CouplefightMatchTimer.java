package com.game.couplefight.timer;

import com.game.manager.Manager;
import com.game.server.filter.InnerMsgImpl;
import game.core.timer.TimerEvent;

/**
 * @Desc 仙侣对决匹配定时器 暂不使用
 * @Date 2020/11/16 18:03
 * @Auth ZUncle
 */
public class CouplefightMatchTimer extends TimerEvent {

    /**
     * 循环事件
     */
    public CouplefightMatchTimer() {
        super(-1, 0, 1 * 1000);
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        Manager.couplefightManager.addCommand(new CouplefightMatchCommand());
    }

}
