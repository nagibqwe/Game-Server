/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.worker;

import com.game.server.RobotServer;
import game.core.concurrent.AbstractWork;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.command.Handler;
import game.core.message.Dictionary;
import game.core.message.MessageDictionary;
import game.core.message.RMessage;
import org.apache.mina.core.session.IoSession;

/**
 * 具体的消息处理类
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class InnerWorker extends AbstractWork {

    private static final Logger log = LogManager.getLogger(InnerWorker.class);
    private final static Logger logger = LogManager.getLogger("flow");
    private final int msgId;
    private final IoSession session;
    private byte[] msgData;

    public InnerWorker(int msgId, IoSession session, byte[] msgData) {
        this.msgId = msgId;
        this.session = session;
        this.msgData = msgData;
    }

    @Override
    public String getKey() {
        return msgId + "";
    }

    @Override
    public void run() {
        //处理协议的问题            
        try {
            // 获取消息字典
            Dictionary dic = MessageDictionary.getInstance().getDictionary(msgId);
            if (dic != null) {
                Handler handler = dic.getHandlerInstance();
                // 创建消息对象
                RMessage message = new RMessage();
                message.setId(msgId);
                message.setSession(session);
                message.setData(dic.getMessage(), msgData);
                handler.action(message);
                RobotServer.getInstance().debugExecutorActioned(session, message);
            }
            //清理内存
            msgData = null;
        } catch (Exception e) {
            // do someting...
            log.error("Not found message dictionary! msgId = " + msgId, e);
        }
    }

}
