/**
 * @date 2014/3/18 15:40
 * @author ChenLong
 */
package game.core.net.communication;

import game.core.net.server.IServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * <b>Server端通信</b>
 * 
 * @author ChenLong
 */
public class CommunicationS extends AbstractCommunication
{
    private static final Logger log = LogManager.getLogger(CommunicationS.class);
    private NioSocketAcceptor acceptor;
    private int idleTime;

    /**
     * Server端通信构造函数
     *
     * @param server 处理接口
     * @param maxConnectNum 支持的最大连接数
     * @param idleTime 心跳时间，单位秒，心跳超时将回调sessionIdle
     */
    public CommunicationS(IServer server, int maxConnectNum, int idleTime)
    {
        super(server, maxConnectNum);
        this.idleTime = idleTime;
    }

    /**
     * Server端通信构造函数
     *
     * @param server 处理接口
     * @param maxConnectNum 支持的最大连接数
     */
    public CommunicationS(IServer server, int maxConnectNum)
    {
        this(server, maxConnectNum, 0);
    }

    /**
     * 获取连接数
     *
     * @return
     */
    @Override
    public int getSessionCount() {
        return acceptor.getManagedSessionCount();
    }
    
    /**
     * 启动Server监听
     *
     * @param port 监听端口
     * @return
     */
    @Override
    public int accept(int port)
    {
        acceptor = new NioSocketAcceptor();
        acceptor.setBacklog(65535);
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        chain.addLast("codec", new ProtocolCodecFilter(server.getCodecFactory())); // 添加数据编解码器
        // 添加有序线程池，以保证消息按先后顺序到达
        OrderedThreadPoolExecutor threadpool = new OrderedThreadPoolExecutor();
        chain.addLast("threadPool", new ExecutorFilter(threadpool));
        acceptor.setHandler(this);

        int recsize = 50 * 1024;
        int sendsize = 2048 * 1024;
        acceptor.setReuseAddress(true); // 设置每一个非主监听连接的端口可以重用
        SocketSessionConfig sc = acceptor.getSessionConfig();
        sc.setReuseAddress(true); // 设置每一个非主监听连接的端口可以重用
        sc.setReceiveBufferSize(recsize); // 设置输入缓冲区的大小
        sc.setSendBufferSize(sendsize); // 设置输出缓冲区的大小
        sc.setKeepAlive(true);
        sc.setTcpNoDelay(true);
        sc.setSoLinger(0);
        if (idleTime > 0) // 心跳时间设置
            sc.setIdleTime(IdleStatus.READER_IDLE, idleTime);

        try
        {
            acceptor.bind(new InetSocketAddress(port));
            log.info("CommunicationS listen on " + port + " port");
        }
        catch (IOException e)
        {
            log.error("CommunicationS bind Exception " + e.toString());
            System.exit(1);
        }
        return 0;
    }
    
    public void unaccept()
    {
        acceptor.unbind();
    }
}
