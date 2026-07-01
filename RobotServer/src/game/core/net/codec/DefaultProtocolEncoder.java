/*
 * 2014/3/18 21:22 by ChenLong
 */
package game.core.net.codec;

import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 *
 * @author ChenLong
 */
public class DefaultProtocolEncoder implements ProtocolEncoder
{
    private static final Logger log = LogManager.getLogger(DefaultProtocolEncoder.class);

    @Override
    public void encode(IoSession session, Object obj, ProtocolEncoderOutput out) throws Exception
    {
        if (obj instanceof byte[])
        {
            byte[] bytes = (byte[]) obj;
            IoBuffer buffer = IoBuffer.allocate(512);
            buffer.setAutoExpand(true);
            buffer.setAutoShrink(true);
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
        }
        else if (obj instanceof IoBuffer)
        {
            out.write((IoBuffer) obj);
        }
        else
        {
            log.error("obj instance of " + obj.getClass().getName());
        }
    }

    @Override
    public void dispose(IoSession session) throws Exception
    {
    }
}
