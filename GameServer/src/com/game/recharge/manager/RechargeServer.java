package com.game.recharge.manager;

import com.game.manager.Manager;
import com.game.recharge.structs.Recharge;
import com.game.recharge.structs.RechargeDefine;
import game.core.json.TypeReference;
import game.core.net.Config.ServerConfig;
import game.core.net.server.BackendServer;
import game.core.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MixedAttribute;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

/**
 * @explain: desc
 * @time Created on 2019/11/19 20:46.
 * @author: tc
 */
public class RechargeServer extends Thread {
	private final static Logger log = LogManager.getLogger("RechargeManager");

	public RechargeServer() {
		super("RechargeServer start");
	}

	@Override
	public void run() {
		BackendServer backServer = new BackendServer("充值监听", ServerConfig.getRechargePort());
		backServer.start(new HttpChannelImpl());
	}

	public static void writeResponse(Channel channel, int result) {
//		channel.writeAndFlush(String.valueOf(result));
		// Convert the response content to a ChannelBuffer.
		ByteBuf buf = copiedBuffer(String.valueOf(result), CharsetUtil.UTF_8);
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
		response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
		response.headers().set(CONTENT_LENGTH, buf.readableBytes());
		ChannelFuture future = channel.writeAndFlush(response);
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) {
				if (!future.isSuccess()) {
					log.error(channel.remoteAddress() + " back http result failure!");
				}
			}
		});
		future.addListener(ChannelFutureListener.CLOSE);
	}

	private static class HttpChannelImpl extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel c) throws Exception {
			c.pipeline().addLast(new HttpServerCodec());
			c.pipeline().addLast(new HttpObjectAggregator(65535));
			c.pipeline().addLast(new HttpServerExpectContinueHandler());
			c.pipeline().addLast(new ChunkedWriteHandler()); // 大数据包传输
			c.pipeline().addLast(new MsgHandler());
		}
	}

	private static class MsgHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			log.error(ctx.channel().remoteAddress() + " 发生错误，关闭链接：" + cause);
			ctx.disconnect();
		}

		@Override
		protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
			String uri = fullHttpRequest.uri();
			if (uri.equals(RechargeDefine.FAVICON)) {
				log.error("收到favicon.ico");
				writeResponse(channelHandlerContext.channel(), 0);
				return;
			}

			// TODO tc
			if (uri.length() > 0) {
				dispatcherRequest(channelHandlerContext, fullHttpRequest);
			} else {
				log.error(channelHandlerContext + " 错误的请求HTTP：" + fullHttpRequest);
				channelHandlerContext.close();
			}
		}

		private void dispatcherRequest(ChannelHandlerContext session, FullHttpRequest httpRequest) {
			HashMap<String, String> param = new HashMap<>();
			if (httpRequest.method().equals(HttpMethod.GET)) {
				QueryStringDecoder qsd = new QueryStringDecoder(httpRequest.uri(), Charsets.toCharset(CharEncoding.UTF_8));
				Map<String, List<String>> parameters = qsd.parameters();
				try {
					for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
						String name = entry.getKey();
						String value = entry.getValue().get(0);
						param.put(name, value);
					}
				} catch (Exception e) {
					log.error(e);
					writeResponse(session.channel(), 0);
					return;
				}
			} else if (httpRequest.method().equals(HttpMethod.POST)) {
				ByteBuf content = httpRequest.content();
				if (content.readableBytes() > 0) {
					HttpPostRequestDecoder postDecode = new HttpPostRequestDecoder(httpRequest);
					List<InterfaceHttpData> httpData = postDecode.getBodyHttpDatas();
					try {
						for (InterfaceHttpData data : httpData) {
							if (data instanceof MixedAttribute) {
								MixedAttribute atr = (MixedAttribute) data;
								String name = atr.getName();
								String value = atr.getValue();
								param.put(name, value);
							}
						}
					} catch (IOException e) {
						log.error(e);
						writeResponse(session.channel(), 0);
						return;
					}
				}
			}

			deal(session.channel(), param);
		}

		private void deal(Channel channel, HashMap<String, String> param) {
			String mess = JsonUtils.toJSONString(param);
			try {
				log.info("recv recharge data: " + mess);
				Recharge recharge = JsonUtils.toJavaObject(mess, Recharge.class);
				if (recharge == null) {
					log.error("解析数据失败：" + mess);
					writeResponse(channel, 0);
					return;
				}

				log.info("parse recharge: " + recharge.toString());

				// 检查数据
				String sign = Recharge.sign(param);
				if (!recharge.getSign().equalsIgnoreCase(sign)) {
					log.error("数据不正确，签名失败：" + recharge.toString());
					writeResponse(channel, 0);
					return;
				}
				Manager.rechargeManager.AddRecharge(channel, recharge, mess, RechargeDefine.SRC_NORMAL);
			} catch (Exception e) {
				log.error("解析数据失败：" + mess + " catch:" + e);
				writeResponse(channel, 0);
			}
		}
	}
}
