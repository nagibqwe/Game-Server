package game.core.disruptor;

import com.lmax.disruptor.EventTranslatorTwoArg;
import game.core.message.RMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by soko(xysoko@qq.com) on 2017/10/14. copyright 巨匠@雨墨
 */
public class MessageEventTranslator implements EventTranslatorTwoArg<MessageEvent, ChannelHandlerContext, RMessage> {
    @Override
    public void translateTo(MessageEvent messageEvent, long l, ChannelHandlerContext context, RMessage rMessage) {
        messageEvent.setSession(context);
        messageEvent.setRmess(rMessage);
    }
}
