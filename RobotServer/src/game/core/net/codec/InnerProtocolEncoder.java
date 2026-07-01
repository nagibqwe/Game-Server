package game.core.net.codec;

import game.core.message.OtherServerToPlayerMessage;
import game.core.message.SMessage;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * <b>服务器内部通信使用的编码器.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class InnerProtocolEncoder implements ProtocolEncoder
{

    static final Logger LOG = LogManager.getLogger(InnerProtocolEncoder.class);

    @Override
    public void encode(IoSession session, Object obj, ProtocolEncoderOutput out)
            throws Exception
    {
        if (obj instanceof SMessage)
        {
            SMessage msg = (SMessage) obj;
            IoBuffer buff = IoBuffer.allocate(1024);
            buff.setAutoExpand(true);
            buff.setAutoShrink(true);
            buff.putInt(0);
            buff.putInt(msg.getId());
            buff.putLong(msg.getSender());
            buff.put(msg.getData());
            buff.flip();
            buff.putInt(buff.limit() - Integer.SIZE / Byte.SIZE);
            buff.rewind();

            out.write(buff);
            out.flush();
        }
        else if (obj instanceof OtherServerToPlayerMessage)
        {
            OtherServerToPlayerMessage msg = (OtherServerToPlayerMessage) obj;
            IoBuffer buff = IoBuffer.allocate(1024);
            buff.setAutoExpand(true);
            buff.setAutoShrink(true);
            buff.putInt(msg.getLengthWithRole());
            buff.putInt(msg.getId());
            buff.putLong(msg.getSendId());
            buff.putInt(msg.getSendTime());
            buff.putInt(msg.getRoleIds().size());
            for (int i = 0; i < msg.getRoleIds().size(); i++)
            {
                buff.putLong(msg.getRoleIds().get(i));
            }
            buff.put(msg.getBytes());
            buff.flip();
            
            //LOG.error("收到其它server过来的客户端内容， 角色个数："+ msg.getRoleIds().size());
            out.write(buff);
            out.flush();
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
