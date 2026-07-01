package game.core.disruptor;

import game.core.message.RMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by soko(xysoko@qq.com) on 2017/10/14. copyright 巨匠@雨墨
 */
public class MessageEvent{
    private ChannelHandlerContext session;
    private RMessage rmess;

    public ChannelHandlerContext getSession() {
        return session;
    }

    public void setSession(ChannelHandlerContext session) {
        this.session = session;
    }

    public RMessage getRmess() {
        return rmess;
    }

    public void setRmess(RMessage rmess) {
        this.rmess = rmess;
    }
}
