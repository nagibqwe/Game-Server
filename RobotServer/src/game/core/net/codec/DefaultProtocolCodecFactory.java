/*
 * 2014/3/18 21:20  by ChenLong
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
public class DefaultProtocolCodecFactory implements ProtocolCodecFactory
{
    @Override
    public ProtocolEncoder getEncoder(IoSession is) throws Exception
    {
        return new DefaultProtocolEncoder();
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession is) throws Exception
    {
        return new DefaultProtocolDecoder();
    }
}
