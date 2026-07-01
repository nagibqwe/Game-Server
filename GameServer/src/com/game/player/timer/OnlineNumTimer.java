package com.game.player.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import game.core.util.TimeUtils;

public class OnlineNumTimer extends TimerEvent {
    public OnlineNumTimer() {
        super(-1, 0,60 * 1000);
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        Manager.biManager.getScript().biOlineNum(Manager.playerManager.getOnLines());

        long now = TimeUtils.Time();
        if (TimeUtils.getDayOfMin(now) % 5 == 0) {
            Manager.biManager.get4399Script().onlineBiTo4399();
        }
    }
}
