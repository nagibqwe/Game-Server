/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.client;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.server.RobotServer;
import com.game.server.worker.InnerWorker;
import com.game.structs.SessionAttribute;
import game.core.concurrent.OrderedQueuePoolExecutor;
import game.core.net.codec.RobotProtocolCodecFactoryimplements;
import game.core.net.communication.CommunicationC;
import game.core.net.server.IServer;
import game.message.MapMessage;
import game.message.heartMessage.ResHeart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

import java.util.HashMap;

/**
 *
 * @author hewei@haowan123.com
 */
public class Client implements IServer {

    private final Logger log = LogManager.getLogger(Client.class);
    private final static Logger logger = LogManager.getLogger("flow");
    private static final OrderedQueuePoolExecutor recvExcutor = new OrderedQueuePoolExecutor("消息接收队列", 1000, -1);
    private Player player;
    private CommunicationC connector;

    @Override
    public void doCommand(IoSession session, IoBuffer buf) {
        try {
            // 读取消息ID
            int msgId = buf.getInt();
            // 读取消息内容
            byte[] msgData = new byte[buf.remaining()];
            buf.get(msgData);
            if (msgId == ResHeart.MsgID.eMsgID_VALUE) {
                logger.error(player.getId() + "心跳返回,当前：" + player.getLastHeartSendTime());
            }
            //不要聊天消息的接收了
            if (msgId / 1000 == 104) {
                return;
            }
            //移动消息屏蔽
            if (msgId == 102110 || msgId ==103101){
                return;
            }


            //移动包也不需要了
            if (MapMessage.ResMoveTo.MsgID.eMsgID_VALUE == msgId) {
                return;
            }

            recvExcutor.addTask(player.getUserId(), new InnerWorker(msgId, session, msgData));
            RobotServer.getInstance().debugNioDispatched(session, msgId);

        } catch (Exception e) {
            log.info(e, e);
        }
    }

    public static OrderedQueuePoolExecutor getRecvExcutor() {
        return recvExcutor;
    }

    @Override
    public void sessionCreate(IoSession session) {
        RobotServer.addSession(session);
        session.setAttribute(SessionAttribute.PLAYER.getValue(), this.player);
        session.setAttribute(SessionAttribute.USER_ID.getValue(), this.player.getUserId());
        this.player.setSession(session);
        Manager.registerManager.deal().loginGame(this.player);
    }

    @Override
    public void sessionClosed(IoSession session) {
        RobotServer.removeSession(session);

        if (!session.containsAttribute(SessionAttribute.PLAYER.getValue())) {
            return;
        }

        //如果是相同，则使用， 如果不同，则不管了
        if (session.equals(this.player.getSession())) {
            Manager.registerManager.deal().quitGame(this.player, true);
            session.removeAttribute(SessionAttribute.PLAYER.getValue());
//            log.error(session.getId() + "," + player.getUserId() + " ," + player.getId() + " 正在进行重联！ ");
//            Player pp = new Player();
//            pp.setUserId(player.getUserId());
//            pp.setEventType(Config.getEventType());
//            //进行重新连接
//            start(Config.getServerIp(), Config.getServerPort(), pp);
//            player = pp;
        }

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {

    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        log.error(session, cause);
    }

    @Override
    public ProtocolCodecFactory getCodecFactory() {
        return new RobotProtocolCodecFactoryimplements();
    }

    public void start(String ip, int port, Player player) {
        this.player = player;
        if(connector == null){
            connector = new CommunicationC(this);
            connector.initialize();
        }
        connector.connectAsync(ip, port);
    }
}
