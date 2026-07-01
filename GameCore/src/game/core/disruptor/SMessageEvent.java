package game.core.disruptor;

import game.core.message.SMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by soko(xysoko@qq.com) on 2017/10/14. copyright 巨匠@雨墨
 */
public class SMessageEvent {
    private ChannelHandlerContext session;
    private SMessage smess;

    public ChannelHandlerContext getSession() {
        return session;
    }

    public void setSession(ChannelHandlerContext session) {
        this.session = session;
    }

    public SMessage getSmess() {
        return smess;
    }

    public void setSmess(SMessage smess) {
        this.smess = smess;
    }
}
