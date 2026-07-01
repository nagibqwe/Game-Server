/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.messageEvent;

import com.lmax.disruptor.EventTranslatorTwoArg;
import game.core.disruptor.TaskEvent;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class OtherToGameMesageTranslator implements EventTranslatorTwoArg<TaskEvent<OtherToGameMessage>, Long, OtherToGameMessage> {

    @Override
    public void translateTo(TaskEvent<OtherToGameMessage> t, long l, Long a, OtherToGameMessage b) {
//        t.setType(a);
        t.setObj(b);
    }
}
