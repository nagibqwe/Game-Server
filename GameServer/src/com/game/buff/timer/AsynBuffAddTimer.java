package com.game.buff.timer;

import com.game.manager.Manager;
import com.game.structs.Fighter;
import game.core.timer.TimerEvent;

/**
 * @Desc TODO
 * @Date 2020/8/1 16:35
 * @Auth ZUncle
 */
public class AsynBuffAddTimer extends TimerEvent {

    final Fighter source;
    final Fighter target;
    final int buffId;

    public AsynBuffAddTimer(Fighter source, Fighter target, int buffId) {
        super(1,0,0);
        this.source = source;
        this.target = target;
        this.buffId = buffId;
    }

    @Override
    public void action() {
        Manager.buffManager.deal().onAddBuff(source, target, buffId);
    }
}
