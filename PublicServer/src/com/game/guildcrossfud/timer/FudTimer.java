package com.game.guildcrossfud.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import game.core.util.TimeUtils;

/**
 * @Desc TODO
 * @Date 2021/2/20 16:07
 * @Auth ZUncle
 */
public class FudTimer extends TimerEvent{

    /**
     * 默认1秒后执行1次的timer
     */
    public FudTimer() {
        super(-1, 60000 - TimeUtils.Time() % 60000, 30 * 1000);
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        Manager.fudManager.deal().tick();
    }
}
