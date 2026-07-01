package com.game.txpushsdk.messageEvent;

import com.game.txpushsdk.struct.PushMessInfo;
import com.lmax.disruptor.EventTranslatorTwoArg;
import game.core.disruptor.TaskEvent;

public class PushMessEventTranslator implements EventTranslatorTwoArg<TaskEvent<PushMessInfo>, Integer, PushMessInfo> {

    @Override
    public void translateTo(TaskEvent<PushMessInfo> t, long l, Integer a, PushMessInfo b) {
        t.setType(a);
        t.setObj(b);
    }

}
