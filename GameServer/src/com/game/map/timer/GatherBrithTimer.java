package com.game.map.timer;

import com.game.manager.Manager;
import com.game.structs.Gather;
import game.core.timer.TimerEvent;
import game.core.util.IDConfigUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GatherBrithTimer extends TimerEvent{
    private Gather gather;

    public GatherBrithTimer(Gather gather, long delay) {
        super(1, delay, 0);
        this.gather = gather;
    }

    @Override
    public void action() {
        this.gather.setId(IDConfigUtil.getLogId());
        Manager.mapManager.manager().onEnterMap(gather);
        
    }
}
