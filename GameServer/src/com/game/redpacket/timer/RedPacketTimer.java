package com.game.redpacket.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/12/7 16:25
 */
public class RedPacketTimer extends TimerEvent {

    public RedPacketTimer() {
        super(-1, 0,60 * 1000);
    }

    @Override
    public void action() {
        Manager.redPacketManager.clearExpireRedpacket();
    }
}
