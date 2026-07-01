package game.core.net.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 
 * <b>服务器内部通信使用的编解码工厂.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 * 
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class InnerProtocolCodecFactory implements ProtocolCodecFactory
{

    /**
     * 获取解码器.
     * 
     * @param session
     * @return
     * @throws Exception 
     */
    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception
    {
        return new InnerProtocolDecoder();
    }

    /**
     * 获取编码器.
     * 
     * @param session
     * @return
     * @throws Exception 
     */
    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception
    {
        return new InnerProtocolEncoder();
    }

}
