package com.game.txpushsdk.messageEvent;

import com.game.txpushsdk.manager.PushMessManager;
import com.game.txpushsdk.struct.PushMessInfo;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import game.core.disruptor.TaskEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PushMessEventHandler implements WorkHandler<TaskEvent<PushMessInfo>>, EventHandler<TaskEvent<PushMessInfo>> {

    private static final Logger log = LogManager.getLogger(PushMessEventHandler.class);

    @Override
    public void onEvent(TaskEvent<PushMessInfo> t, long l, boolean bln) throws Exception {
        onEvent(t);
    }

    @Override
    public void onEvent(TaskEvent<PushMessInfo> t) throws Exception {
        try {
            PushMessInfo fsm = t.getObj();
            PushMessManager.deal().dealPush(fsm);
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            t.setObj(null);
            t.setType(0);
        }
    }

}
