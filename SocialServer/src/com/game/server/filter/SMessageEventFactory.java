package com.game.server.filter;

import com.game.server.struct.MessageEvent;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorTwoArg;
import game.core.message.SMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Desc TODO
 * @Date 2021/6/9 14:57
 * @Auth ZUncle
 */
public class SMessageEventFactory implements EventFactory<MessageEvent<ChannelHandlerContext, SMessage>>,
        EventTranslatorTwoArg<MessageEvent<ChannelHandlerContext, SMessage>, ChannelHandlerContext, SMessage> {

    @Override
    public void translateTo(MessageEvent<ChannelHandlerContext, SMessage> event, long l, ChannelHandlerContext channel, SMessage message) {
        event.setKey(channel);
        event.setVal(message);
    }

    @Override
    public MessageEvent<ChannelHandlerContext, SMessage> newInstance() {
        return new MessageEvent<>();
    }
}
