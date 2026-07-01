/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.filter;

import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.fightserver.manager.FightClientManager;
import com.game.manager.Manager;
import com.game.server.GameServer;
import com.game.server.handler.InnerServerCloseHandler;
import com.game.thread.InnerServerProcessor;
import game.core.command.Handler;
import game.core.message.Dictionary;
import game.core.message.MessageDictionary;
import game.core.message.RMessage;
import game.core.net.Config.ServerEnum;
import game.core.util.SessionUtils;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author soko <xuchangming@haowan123.com>
 */
public class InnerHandlerAdapter extends SimpleChannelInboundHandler<RMessage> {

    private static final Logger INNERLOG = LogManager.getLogger(InnerHandlerAdapter.class);

    private final int type;

    public InnerHandlerAdapter(int serverType) {
        this.type = serverType;
    }

    private static final AtomicLong SESSIONID = new AtomicLong();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //super.channelActive(ctx); //To change body of generated methods, choose Tools | Templates.
//        clserver.register(ctx, type);
        INNERLOG.info(" server channelActive connect :" + ctx.channel());
        switch (type) {
            case ServerEnum.PUBLIC_SERVER: {
                Thread.sleep(1000);
                Manager.publicServerManager.register(ctx);
            }
            break;
            case ServerEnum.FIGHT_CLIENT_LIMIT: {
                Thread.sleep(1000);
                ConnectFightManager.GetInstance().Register(ctx);
            }
            break;
            case ServerEnum.FIGHT_SERVER_LISTEN:
                ctx.channel().attr(GameServer.SESSIONID).set(SESSIONID.getAndIncrement());
                break;
            default:
                break;
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
//        INNERLOG.error(" server registered connect :" + ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        SessionUtils.release(ctx.channel());
        //INNERLOG.info("client session " + ctx.channel() + " close");
        switch (type) {
            case ServerEnum.PUBLIC_SERVER:
                if (Manager.publicServerManager.getPublicSession() == null) {
                    return;
                }
                break;
            case ServerEnum.FIGHT_CLIENT_LIMIT:
                ConnectFightManager.GetInstance().RemoveSession(ctx);//注销战斗服
                return;
            case ServerEnum.FIGHT_SERVER_LISTEN:
                FightClientManager.GetInstance().RemoveSession(ctx);//
                return;
            default:
                break;
        }

//        clserver.register(null, type);
        INNERLOG.error("client session " + ctx.channel() + " close");
        InnerServerCloseHandler handler = new InnerServerCloseHandler(ctx, type);
        InnerServerProcessor.getInstance().addCommand(handler);
//        LOGGER.info(ctx.channel().remoteAddress() + "　连接关闭！");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof ReadTimeoutException) {
            INNERLOG.error(ctx.channel().remoteAddress() + "　远程连接30秒验证超时，关闭了！");
        } else {
            INNERLOG.error(ctx.channel().remoteAddress() + " ClientHandlerAdapter 远程关闭了！", cause);
        }
        //上报日志系统加上
        GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog(ctx.channel().remoteAddress() + " ClientHandlerAdapter 远程关闭了！", cause.toString());
        if (TimeUtils.isIDEEnvironment()) {
            INNERLOG.error(cause, cause);
        }
//        ctx.close();
//        ctx.channel().close();
        SessionUtils.closeSession(ctx, cause.getMessage());
//        if (cause instanceof IOException && type == ServerInfo.FIGHT_SERVER_LISTEN) {
//            FightClientManager.GetInstance().RemoveSession(ctx);//
//        }
//        SessionUtils.closeSession(ctx.channel(), "exceptionCaught=远程主机强迫关闭了一个现有的连接！");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext chc, RMessage i) {
        boolean isHert = false;
        //战斗的返回心跳包
        if (i.getId() == CrossServerMessage.F2GResHeart.MsgID.eMsgID_VALUE) {
            isHert = true;
        }
        //公共服的返回心跳
        if (i.getId() == CrossServerMessage.P2GConnectHeartRes.MsgID.eMsgID_VALUE) {
            isHert = true;
        }

        //战斗服的接收心跳
        if (i.getId() == CrossServerMessage.G2FReqHeart.MsgID.eMsgID_VALUE) {
            isHert = true;
        }

        if (isHert) {
            Dictionary dic = MessageDictionary.getInstance().getDictionary(i.getId());
            if (dic != null) {
                Handler handler = dic.getHandlerInstance();
                //handler.setAfterHanler(MessageHandleAfterHandler.getInstance());
                if (handler != null) {
//                        i.setData(dic.getMessage(), i.getByteData());
//                        handler.setMessage(i);
                    i.setContext(chc);
                    handler.action(i, i.getData());
                }
            }
            return;
        }
        if (type == ServerEnum.FIGHT_SERVER_LISTEN) {
            GameServer.FightExcu(chc, i);
        } else {
            GameServer.worldExec(chc, i);
        }
    }

}
