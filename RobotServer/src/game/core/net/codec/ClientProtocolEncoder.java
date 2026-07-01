/**
 * @date 2014/7/2
 * @author ChenLong
 */
package game.core.net.codec;

import game.core.message.SMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * 客户端Encoder
 *
 * @author ChenLong
 */
public class ClientProtocolEncoder implements ProtocolEncoder
{
    private static final Logger LOG = LogManager.getLogger(ClientProtocolEncoder.class);
    private static final String MSG_ORDER_KEY = "MSG_ORDER_KEY";

    @Override
    public void encode(IoSession session, Object obj, ProtocolEncoderOutput out) throws Exception
    {
        int msgOrder = 0;
        int msgTime = (int) (System.currentTimeMillis() / 1000L); // second
        { // get and set Msg Order
            Object orderObj = session.getAttribute(MSG_ORDER_KEY);
            if (orderObj != null && orderObj instanceof Integer)
            {
                msgOrder = ((Integer) orderObj) + 1;
            }
            session.setAttribute(MSG_ORDER_KEY, msgOrder);
        }

        if (obj instanceof SMessage)
        {
            SMessage msg = (SMessage) obj;
            IoBuffer buff = IoBuffer.allocate(1024);
            buff.setAutoExpand(true);
            buff.setAutoShrink(true);

            buff.putInt(0); // length
            buff.putInt(0); // msgOrder
            buff.putInt(msgTime); // msgTime(second)
            buff.putInt(msg.getId()); // msgId
            buff.put(msg.getData()); // msg content

            buff.flip();

            int length = buff.limit() - Integer.SIZE / Byte.SIZE;
            int cryptMsgOrder = (msgOrder ^ (0x6B << 10)) ^ length;

            buff.putInt(length);
            buff.putInt(cryptMsgOrder);

            buff.rewind();

            out.write(buff);
        }
        else
        {
            LOG.error("Class type error! obj must be game.core.message.SMessage: " + obj.getClass().getName());
        }
    }

    @Override
    public void dispose(IoSession session) throws Exception
    {
    }
}
