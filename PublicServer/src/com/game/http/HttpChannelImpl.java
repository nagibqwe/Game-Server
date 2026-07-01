package com.game.http;

import com.game.http.script.IHttpScript;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import game.core.script.IScript;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * http服务器的处理接口
 *
 */
public class HttpChannelImpl extends ChannelInitializer<SocketChannel> {

    public HttpChannelImpl() {

    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast("httd-decoder", new HttpRequestDecoder());
        ch.pipeline().addLast("httd-aggregator", new HttpObjectAggregator(65535));
        ch.pipeline().addLast("httd-encoder", new HttpResponseEncoder());
        ch.pipeline().addLast("httd-chunked", new ChunkedWriteHandler());
        ch.pipeline().addLast("fileServerHandler", new httpServerHandler());
    }

}

class httpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger log = LogManager.getLogger(httpServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        if (msg.uri().length() > 0) {
            dispatcherRequest(ctx, msg);
        } else {
            log.error(ctx + "错误的请求HTTP：" + msg);
            ctx.close();
        }
    }

    private void dispatcherRequest(ChannelHandlerContext session, HttpRequest httpRequest) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.HttpBackScript);
        if (is instanceof IHttpScript) {
            ((IHttpScript) is).onHttp(session, httpRequest);
        }
    }
}
