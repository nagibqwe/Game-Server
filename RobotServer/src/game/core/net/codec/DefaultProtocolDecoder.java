/*
 * 2014/3/18 21:21 by ChenLong
 */
package game.core.net.codec;

import game.core.net.server.ServerContext;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 *
 * @author ChenLong
 */
public class DefaultProtocolDecoder implements ProtocolDecoder
{
    private static final String CONTEXT = "CONTEXT";

    @Override
    public void decode(IoSession session, IoBuffer buffer, ProtocolDecoderOutput out) throws Exception
    {
        //初始化服务器上下文
        ServerContext context = (ServerContext) session.getAttribute(CONTEXT);
        if (context == null)
        {
            context = new ServerContext();
            session.setAttribute(CONTEXT, context);
        }
        IoBuffer contextBuffer = context.getBuffer();
        contextBuffer.put(buffer); // 添加接收到的buffer到Context

        do
        {
            contextBuffer.flip();
            if (contextBuffer.remaining() < Integer.SIZE / Byte.SIZE)
            {
                contextBuffer.compact(); // 剩余字节长度不足，等待下次信息
                break;
            }
            int length = contextBuffer.getInt(); // 获得消息长度
            //log.error(session + ":message length " + length + " bytes, buff remain " + io.remaining() + " bytes");
            if (contextBuffer.remaining() < length)
            { //剩余字节长度不足，等待下次信息
                contextBuffer.rewind();
                contextBuffer.compact();
                break;
            }
            else
            { //读取指定长度的字节数
                byte[] bytes = new byte[length];
                contextBuffer.get(bytes);
                out.write(bytes); // 输出一个消息包
                if (contextBuffer.remaining() == 0)
                {
                    contextBuffer.clear();
                    break;
                }
                else
                {
                    contextBuffer.compact();
                }
            }
        } while (true);
    }

    @Override
    public void finishDecode(IoSession is, ProtocolDecoderOutput pdo) throws Exception
    {
    }

    @Override
    public void dispose(IoSession session) throws Exception
    {
        session.removeAttribute(CONTEXT);
    }
}
