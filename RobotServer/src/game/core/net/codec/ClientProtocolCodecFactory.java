/**
 * @date 2014/7/2
 * @author ChenLong
 */
package game.core.net.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 *
 * @author ChenLong
 */
public class ClientProtocolCodecFactory implements ProtocolCodecFactory
{
    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception
    {
        return new ClientProtocolDecoder();
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception
    {
        return new ClientProtocolEncoder();
    }
}
