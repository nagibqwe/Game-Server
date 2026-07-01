/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.net.codec;

import game.core.message.SMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 *
 * @author hewei@haowan123.com
 */
public class RobotProtocolEncoder implements ProtocolEncoder {

    private static final Logger LOG = LogManager.getLogger(RobotProtocolEncoder.class);
    private static final String MSG_ORDER_KEY = "MSG_ORDER_KEY";

    @Override
    public void encode(IoSession session, Object obj, ProtocolEncoderOutput out) throws Exception {
        LOG.error(" 发送信息！");
        int msgOrder = 0;
        int msgTime = (int) (System.currentTimeMillis() / 1000L); // second
        { // get and set Msg Order
            Object orderObj = session.getAttribute(MSG_ORDER_KEY);
            if (orderObj != null && orderObj instanceof Integer) {
                msgOrder = ((Integer) orderObj) + 1;
            }
            session.setAttribute(MSG_ORDER_KEY, msgOrder);
        }

        if (obj instanceof SMessage) {
            SMessage msg = (SMessage) obj;
            IoBuffer buff = IoBuffer.allocate(1024);
            IoBuffer bb = IoBuffer.allocate(1024);
            buff.setAutoExpand(true);
            buff.setAutoShrink(true);

//            buff.putInt(0); // length
//            buff.putInt(0); // msgOrder
//            buff.putInt(0); //后三个ansii总和
//            buff.putInt(msgTime); // msgTime(second)
//            buff.putInt(msg.getId()); // msgId
//            buff.put(msg.getData()); // msg content
//            buff.flip();
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

            out.write(buff);
        } else {
            LOG.error("Class type error! obj must be game.core.message.SMessage: " + obj.getClass().getName());
        }
    }

    @Override
    public void dispose(IoSession session) throws Exception {

    }
}
