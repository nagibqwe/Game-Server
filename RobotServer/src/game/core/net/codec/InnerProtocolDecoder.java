package game.core.net.codec;

import game.core.net.server.ServerContext;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 *
 * <b>服务器内部通信使用的解码器.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class InnerProtocolDecoder implements ProtocolDecoder
{

    static final Logger LOG = LogManager.getLogger(InnerProtocolDecoder.class);

    private static final String CONTEXT = "CONTEXT";

    @Override
    public void decode(IoSession session, IoBuffer buff, ProtocolDecoderOutput out)
            throws Exception
    {
        // 初始化服务器上下文
        ServerContext context = (ServerContext) session.getAttribute(CONTEXT);
        if (context == null)
        {
            context = new ServerContext();
            session.setAttribute(CONTEXT, context);
        }

        // 读取信息
        IoBuffer contextBuff = context.getBuffer();
        // 将接收到的信息添加到Context
        contextBuff.put(buff);

        do
        {
            contextBuff.flip();
            if (contextBuff.remaining() < Integer.SIZE / Byte.SIZE)
            {
                // 剩余字节长度不足，等待下次信息
                contextBuff.compact();
                break;
            }
            // 获得消息长度
            int length = contextBuff.getInt();
//            LOG.error(session + ":message length " + length + " bytes, buff remain " + contextBuff.remaining() + " bytes");
            if (contextBuff.remaining() < length)
            {
                contextBuff.rewind();
                // 剩余字节长度不足，等待下次信息
                contextBuff.compact();
                break;
            }
            else
            {
                // 读取指定长度的字节数
                byte[] bytes = new byte[length];
                contextBuff.get(bytes);
                // 写入指定长度的字节数
                out.write(bytes);

                if (contextBuff.remaining() == 0)
                {
                    contextBuff.clear();
                    break;
                }
                else
                {
                    contextBuff.compact();
                }
            }
        } while (true);

    }

    @Override
    public void dispose(IoSession session) throws Exception
    {
        if (session.getAttribute(CONTEXT) != null)
        {
            session.removeAttribute(CONTEXT);
        }
    }

    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
            throws Exception
    {

    }

}
