/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.net.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 *
 * @author hewei@haowan123.com
 */
public class RobotProtocolCodecFactoryimplements implements ProtocolCodecFactory
{
    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception
    {
        return new RobotProtocolDecoderTest();
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception
    {
        return new RobotProtocolEncoder();
    }
}
