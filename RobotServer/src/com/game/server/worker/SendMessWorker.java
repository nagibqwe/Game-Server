/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.worker;

import game.core.concurrent.AbstractWork;
import game.core.message.SMessage;
import game.message.heartMessage.ReqHeart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class SendMessWorker extends AbstractWork {

    private final static Logger log = LogManager.getLogger("flow");

    private final IoSession session;
    private final SMessage msg;
    int type = 0;


    public SendMessWorker(IoSession session, SMessage msg) {
        this.session = session;
        this.msg = msg;
    }
    public SendMessWorker(IoSession session, SMessage msg,int type) {
        this.session = session;
        this.msg = msg;
        this.type = type;
    }
    private static final String MSG_ORDER_KEY = "MSG_ORDER_KEY";

    @Override
    public String getKey() {
        return msg.getId() + "";
    }

    @Override
    public void run() {

        if (session == null) {
            return;
        }
        if (session.isClosing()) {
            return;
        }
        synchronized (session) {
            if (type>0){

                session.write(this.msg);
                return;
            }
            int msgOrder = 0;
            int msgTime = (int) (System.currentTimeMillis() / 1000L); // second
            Object orderObj = session.getAttribute(MSG_ORDER_KEY);
            if (orderObj != null && orderObj instanceof Integer) {
                msgOrder = ((Integer) orderObj) + 1;
            }
            session.setAttribute(MSG_ORDER_KEY, msgOrder);

            IoBuffer buff = IoBuffer.allocate(1024);
            IoBuffer bb = IoBuffer.allocate(1024);
            buff.setAutoExpand(true);
            buff.setAutoShrink(true);
            bb.setAutoExpand(true);
            bb.setAutoShrink(true);

            int length = msg.getData().length + Integer.SIZE / Byte.SIZE * 4;//buff.limit();
            int cryptMsgOrder = (msgOrder ^ (0x6b << 10)) ^ length;

            bb.putInt(msgTime);
            bb.putInt(msg.getId()); // msgId
            bb.put(msg.getData()); // msg content
            bb.flip();
            byte[] byteArray = new byte[bb.limit()];

            byte key = (byte) (cryptMsgOrder % 100);
            int all = 0;
            for (int i = 0; i < byteArray.length; i++) {
                byte b = bb.get(i);
                int tm = b;
                if (tm < 0) {
                    tm += 256;
                }
                all += tm;
                b ^= key;
                byteArray[i] = b;
            }

            buff.putInt(length);
            buff.putInt(cryptMsgOrder);
            buff.putInt(all);
            buff.put(byteArray);
            buff.flip();
            buff.rewind();

            bb.clear();

            IoBuffer sendbuf = null;
            if (msg.getId() == ReqHeart.MsgID.eMsgID_VALUE) {
                session.setAttribute("sendheart", 1);
                log.error(session.getAttribute("roleId") + "心跳压包！");
            }
            if (session.containsAttribute("SEND_BUF")) {
                sendbuf = (IoBuffer) session.getAttribute("SEND_BUF");
            } else {
                sendbuf = IoBuffer.allocate(1024);
                sendbuf.setAutoExpand(true);
                sendbuf.setAutoShrink(true);
                session.setAttribute("SEND_BUF", sendbuf);
            }
            sendbuf.put(buff);
            buff.clear();
        }
    }

}
