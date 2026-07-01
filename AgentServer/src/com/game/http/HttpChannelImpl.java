
package com.game.http;

import com.game.login.LoginDataUpdate;
import com.game.background.BackGroundReLoadScripts;
import com.game.background.BackgroundDoUser;
import io.netty.buffer.ByteBuf;
import static io.netty.buffer.Unpooled.copiedBuffer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * http服务器的处理接口
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
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        if (msg.uri().length() > 0) {
            dispatcherRequest(ctx, msg);
        } else {
            log.error(ctx + "错误的请求HTTP：" + msg);
            ctx.close();
        }
    }

    public void dispatcherRequest(ChannelHandlerContext session, HttpRequest httpRequest) {
        QueryStringDecoder qsd = new QueryStringDecoder(httpRequest.uri());
        switch (qsd.path().toLowerCase()) {
            case "/test":
                writeResponse(session.channel(), httpRequest, "连接成功");
            case "/changelogindata":
                LoginDataUpdate.changeLoginData(httpRequest);
                writeResponse(session.channel(), httpRequest, "ok");
                break;
            case "/reloadscripts": //重新加载脚本
                String result = BackGroundReLoadScripts.reLoadScripts(httpRequest);
                writeResponse(session.channel(), httpRequest, result);
                break;
            case "/deleteuser": //删除账号
                String deleteuserresult = BackgroundDoUser.deleteuser(httpRequest);
                writeResponse(session.channel(), httpRequest, deleteuserresult);
                break;
            case "/recoveruser": //恢复已删除账号
                String recoveruserresult = BackgroundDoUser.recoveruser(httpRequest);
                writeResponse(session.channel(), httpRequest, recoveruserresult);
                break;
            case "/forbiddenuser": //屏蔽、解封账号
                String forbiddenUserresult = BackgroundDoUser.forbiddenUser(httpRequest);
                writeResponse(session.channel(), httpRequest, forbiddenUserresult);
                break;
            case "/cancelforbiddenuser": //解封账号
                String cancelforbiddenUserresult = BackgroundDoUser.cancelForbiddenUser(httpRequest);
                writeResponse(session.channel(), httpRequest, cancelforbiddenUserresult);
                break;
            case "/whiteadd": //加白名单账号
                String str = BackgroundDoUser.whiteAdd(httpRequest);
                writeResponse(session.channel(), httpRequest, str);
                break;
            case "/whitecancel": //取消白名单账号
                String str1 = BackgroundDoUser.whiteCancel(httpRequest);
                writeResponse(session.channel(), httpRequest, str1);
                break;
            case "/register": //注册账号暂时使用
                break;
            case "/callscript": //调用脚本call方法
//                ScriptManager.getInstance().call(1, httpRequest);
                break;
            default:
                log.error("unknow getRequestPath: [" + qsd.path() + "]");
                writeResponse(session.channel(), httpRequest, "404");
                break;
        }
    }

    private void writeResponse(Channel channel, HttpRequest httpRequest, String responseMsg) {
        ByteBuf buf = copiedBuffer(responseMsg, CharsetUtil.UTF_8);
        boolean close = httpRequest.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE, true)
                || httpRequest.protocolVersion().equals(HttpVersion.HTTP_1_0)
                && !httpRequest.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE, true);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (!close) {
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
        }
        ChannelFuture future = channel.writeAndFlush(response);
        future.addListener((ChannelFutureListener) future1 -> {
            if (future1.isSuccess()) {
                log.info("back http result success!");
            } else {
                log.error("back http result failure!");
            }
        });
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

}
