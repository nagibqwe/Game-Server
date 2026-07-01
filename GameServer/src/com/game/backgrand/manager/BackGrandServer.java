package com.game.backgrand.manager;

import com.game.backgrand.command.BackCommandHandle;
import com.game.backgrand.thread.BackCommandProcessor;
import game.core.json.TypeReference;
import game.core.net.Config.ServerConfig;
import game.core.net.server.BackendServer;
import game.core.util.SessionUtils;
import game.core.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 后台服务器线程
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class BackGrandServer extends Thread {

    private final static Logger log = LogManager.getLogger(BackGrandServer.class);

    private final static ConcurrentHashMap<Long, Channel> backSession = new ConcurrentHashMap<>();

    public static final AttributeKey<Long> backSessionId = AttributeKey.newInstance("backSessionId");

    public static ConcurrentHashMap<Long, Channel> getBackSession() {
        return backSession;
    }

    public BackGrandServer() {
        super("BackGrandServer start");
    }

    @Override
    public void run() {
        BackendServer backServer = new BackendServer("后台接口", ServerConfig.getBackPort());
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

    private class backHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel c) throws Exception {
            c.pipeline().addLast(new LengthFieldBasedFrameDecoder(2048 * 2048, 0, 4, 0, 4));//最多接收4M的大小数据
            c.pipeline().addLast(new MessagetAdapter());
            c.pipeline().addLast(new readMessageAdapter());
        }

    }

    private class MessagetAdapter extends ByteToMessageCodec<String> {

        @Override
        protected void encode(ChannelHandlerContext chc, String i, ByteBuf bb) throws Exception {
            bb.writeBytes(i.getBytes());
        }

        @Override
        protected void decode(ChannelHandlerContext chc, ByteBuf bb, List<Object> list) throws Exception {
            if (bb.readableBytes() > 0) {
                ByteBuf fff = Unpooled.buffer(bb.readableBytes());
                fff.writeBytes(bb);
                String mess = new String(ByteBufUtil.getBytes(fff), StandardCharsets.UTF_8);
                Channel session = chc.channel();
                log.error(session + " http send:" + mess + "!");
                try {
                    Map<String, Object> map = JsonUtils.parseObject(mess, new TypeReference<HashMap<String, Object>>() {
                    });
                    if (map == null) {
                        log.error(mess + "解析出错");
                        bb.resetReaderIndex();
                        return;
                    }
                    list.add(map);
                } catch (Exception e) {
                    log.error(e, e);
                    chc.close();
                    SessionUtils.log("BackGrandServer " + e.getMessage());
                }
                fff.release();
            }
        }

    }

    private static final AtomicLong sessionAutoId = new AtomicLong();

    private class readMessageAdapter extends SimpleChannelInboundHandler<Map<String, Object>> {

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            log.error("连接出现异常了，sessionId:" + ctx.channel().attr(backSessionId).get() + " session" + ctx.channel(), cause);
//            ctx.writeAndFlush(result(false, "游戏服务器接收出现异常！"));
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.channel().attr(backSessionId).set(sessionAutoId.getAndIncrement());
        }

        @Override
        protected void channelRead0(ChannelHandlerContext chc, Map<String, Object> i) throws Exception {
            BackCommandHandle handle = new BackCommandHandle();
            handle.setSession(chc.channel());
            handle.setCmdMap(i);
            BackCommandProcessor.getInstance().addCommand(handle);//加入处理系统     
        }
    }
}
