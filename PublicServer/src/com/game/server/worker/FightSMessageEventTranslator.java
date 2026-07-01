/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.worker;

import com.lmax.disruptor.EventTranslatorTwoArg;
import game.core.disruptor.TaskEvent;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class FightSMessageEventTranslator implements EventTranslatorTwoArg<TaskEvent<FightSMessage>, Long, FightSMessage> {

    @Override
    public void translateTo(TaskEvent<FightSMessage> t, long l, Long a, FightSMessage b) {
        t.setObj(b);
    }
}
