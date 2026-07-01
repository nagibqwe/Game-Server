/**
 * @date 2014/3/18 17:15
 * @author ChenLong
 */
package game.core.net.communication;

import game.core.net.server.IServer;
import java.net.InetSocketAddress;
import org.apache.commons.lang.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * <b>Client端通信</b>
 *
 * @author ChenLong
 */
public class CommunicationC extends AbstractCommunication {

//    static OrderedThreadPoolExecutor threadpool = new OrderedThreadPoolExecutor();

    private static final Logger log = LogManager.getLogger(CommunicationC.class);
    private NioSocketConnector connector = new NioSocketConnector();
    private boolean isInitialize;

    /**
     * Client端通信构造函数
     *
     * @param server 处理接口
     */
    public CommunicationC(IServer server) {
        super(server);
        isInitialize = false;
    }

    /**
     * Client端通信构造函数
     *
     * @param server 处理接口
     * @param maxConnectNum 支持的最大连接数
     */
    public CommunicationC(IServer server, int maxConnectNum) {
        super(server, maxConnectNum);
        isInitialize = false;
    }

    public synchronized void initialize() {
        if (!isInitialize) {
            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(super.server.getCodecFactory()));
            connector.setHandler(this);
            int recsize = 1024 * 1024;
            int sendsize = 1024 * 1024;
            SocketSessionConfig sc = connector.getSessionConfig();
            sc.setReceiveBufferSize(recsize); // 设置输入缓冲区的大小
            sc.setSendBufferSize(sendsize); // 设置输出缓冲区的大小
            sc.setTcpNoDelay(true);
            sc.setSoLinger(0);
            sc.setKeepAlive(true);
            isInitialize = true;
        }

    }

    /**
     * 获取连接数
     *
     * @return
     */
    @Override
    public int getSessionCount() {
        return connector.getManagedSessionCount();
    }

    /**
     * 连接
     *
     * @param ip
     * @param port
     * @return
     */
    @Override
    public boolean connect(String ip, int port) {
        Validate.notNull(ip);
        Validate.notEmpty(ip);

        boolean connected = false;

        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(ip, port));
        connectFuture.awaitUninterruptibly(10 * 1000); // 1秒
        if (connectFuture.isConnected()) {
            IoSession se = connectFuture.getSession();
            log.info("connect success, ip: [" + ip + "], port = " + port + se);
            connected = true;
        } else {
            log.error("connect failure, ip: [" + ip + "], port = " + port);
            Throwable t = connectFuture.getException();
            if (t != null) {
                log.error("connect exception, ip: [" + ip + "], port = " + port, t);
            }
        }
        return connected;
    }

    @Override
    public ConnectFuture connectAsync(String ip, int port) {
        Validate.notNull(ip);
        Validate.notEmpty(ip);
        if(connector == null){
            connector = new NioSocketConnector();
            initialize();
        }
        return connector.connect(new InetSocketAddress(ip, port));
    }

    /*返回连接的对象*/
    public ConnectFuture Connect(String ip, int port) {
        Validate.notNull(ip);
        Validate.notEmpty(ip);

        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(ip, port));
        connectFuture.awaitUninterruptibly(120 * 1000); // 最长两分钟等待
        if (connectFuture.isConnected()) {
            log.info("connect success, ip: [" + ip + "], port = " + port);
        }
        /*  else
         {
         log.error("connect failure, ip: [" + ip + "], port = " + port);
         Throwable t = connectFuture.getException();
         if (t != null)
         log.error("connect exception, ip: [" + ip + "], port = " + port, t);
         }*/
        return connectFuture;
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session); //To change body of generated methods, choose Tools | Templates.
        //释放资源
        connector.dispose();
        connector = null;
        isInitialize = false;
    }

}
