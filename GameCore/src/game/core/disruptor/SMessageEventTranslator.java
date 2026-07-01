package game.core.disruptor;

import com.lmax.disruptor.EventTranslatorTwoArg;
import game.core.message.SMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by soko(xysoko@qq.com) on 2017/10/14. copyright 巨匠@雨墨
 */
public class SMessageEventTranslator implements EventTranslatorTwoArg<SMessageEvent, ChannelHandlerContext, SMessage> {
    @Override
    public void translateTo(SMessageEvent sMessageEvent, long l, ChannelHandlerContext context, SMessage sMessage) {
        sMessageEvent.setSession(context);
        sMessageEvent.setSmess(sMessage);
    }
}
