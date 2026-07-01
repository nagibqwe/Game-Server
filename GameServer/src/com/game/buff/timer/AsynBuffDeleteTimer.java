package com.game.buff.timer;

import com.game.manager.Manager;
import com.game.structs.Fighter;
import game.core.timer.TimerEvent;

/**
 * @Desc TODO
 * @Date 2020/8/1 16:43
 * @Auth ZUncle
 */
public class AsynBuffDeleteTimer extends TimerEvent {

    final Fighter target;
    final int buffId;

    public AsynBuffDeleteTimer(Fighter target, int buffId) {
        super(1, 0, 0);
        this.target = target;
        this.buffId = buffId;
    }

    @Override
    public void action() {
        Manager.buffManager.deal().onRemoveBuff(target, buffId);
    }
}
