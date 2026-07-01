package game.core.net.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 
 * <b>通信编解码工厂.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 * 
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class ServerProtocolCodecFactory implements ProtocolCodecFactory
{

    // 每次向一个Session发送的最大字节数，超过将拒绝服务并断开连接
    private int sendMaxSize = 1 * 1024 * 1024;

    // 每次从一个Session接收的最大字节数，超过将拒绝服务并断开连接
    private int receivedMaxSize = 60 * 1024;

    // 每秒最多处理同一个Session发送的多少条消息，超过将拒绝服务并断开连接
    private int receivedMaxCount = 30;

    public ServerProtocolCodecFactory()
    {
        this(null, null, null);
    }

    /**
     * 通信编解码工厂的构造函数.
     *
     * @param sendMaxSize 设置每次向一个Session发送的最大字节数，超过将拒绝服务并断开连接，null表示采用默认值
     * @param receivedMaxSize 设置每次从一个Session接收的最大字节数，超过将拒绝服务并断开连接，null表示采用默认值
     * @param receivedMaxCount 设置每秒最多处理同一个Session发送的多少条消息，超过将拒绝服务并断开连接，null表示采用默认值
     */
    public ServerProtocolCodecFactory(Integer sendMaxSize, Integer receivedMaxSize, Integer receivedMaxCount)
    {
        if (sendMaxSize != null)
        {
            this.sendMaxSize = sendMaxSize;
        }

        if (receivedMaxSize != null)
        {
            this.receivedMaxSize = receivedMaxSize;
        }

        if (receivedMaxCount != null)
        {
            this.receivedMaxCount = receivedMaxCount;
        }
    }

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
        return new ServerProtocolDecoder(receivedMaxSize, receivedMaxCount);
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
        return new ServerProtocolEncoder(sendMaxSize);
    }

}
