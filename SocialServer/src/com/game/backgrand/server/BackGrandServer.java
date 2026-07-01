package com.game.backgrand.server;

import com.game.backgrand.handler.BackCommandHandler;
import com.game.backgrand.thread.BackCommandProcessor;
import game.core.net.server.BackendServer;
import game.core.util.JsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc TODO
 * @Date 2021/6/8 16:19
 * @Auth ZUncle
 */
public class BackGrandServer extends Thread {

    private final static Logger log = LogManager.getLogger(BackGrandServer.class);

    public static final AttributeKey<Long> backSessionId = AttributeKey.newInstance("backSessionId");

    int port;

    public BackGrandServer(int port) {
        super("BackGrandServer start");
        this.port = port;
    }

    @Override
    public void run() {
        BackendServer backServer = new BackendServer("后台接口", port);
        backServer.start(new backHandler());
    }

    public static void Send(Channel session, String msg) {
        session.writeAndFlush(msg);
    }

    public static String result(boolean isOk, String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("ok", isOk);
        map.put("msg", msg);
        return JsonUtils.toJSONString(map);
    }

    private static class backHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast("httd-decoder", new HttpRequestDecoder());
            ch.pipeline().addLast("httd-aggregator", new HttpObjectAggregator(65535));
            ch.pipeline().addLast("httd-encoder", new HttpResponseEncoder());
            ch.pipeline().addLast("httd-chunked", new ChunkedWriteHandler());
            ch.pipeline().addLast("fileServerHandler", new httpServerHandler());
        }

    }
    static class httpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
            if (msg.uri().length() > 0) {
                dispatcherRequest(ctx, msg);
            } else {
                log.error(ctx + "错误的请求HTTP：" + msg);
                ctx.close();
            }
        }

        private void dispatcherRequest(ChannelHandlerContext session, HttpRequest request) {

            BackCommandHandler handle = new BackCommandHandler(session, request);
            BackCommandProcessor.Instance().addCommand(handle);

        }
    }

}
