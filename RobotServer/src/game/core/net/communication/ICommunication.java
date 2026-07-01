/**
 * @date 2014/3/18 16:50
 * @author ChenLong
 */
package game.core.net.communication;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;

/**
 * <b>通信接口</b>
 * 
 * @author ChenLong
 */
public interface ICommunication
{
    /**
     * 启动server
     *
     * @param port 监听端口
     * @return
     */
    int accept(int port);

    /**
     * 启动client连接
     *
     * @param ip 连接IP
     * @param port 连接端口
     * @return
     */
    boolean connect(String ip, int port);

    /**
     * 异步连接
     * @param ip
     * @param port
     * @return
     */
    ConnectFuture connectAsync(String ip, int port);

    /**
     * 获取session
     *
     * @param sessionId
     * @return
     */
    IoSession getSession(long sessionId);

    /**
     * 获取连接数
     *
     * @return
     */
    int getSessionCount();

    /**
     * 发送数据
     *
     * @param sessionId
     * @param buf
     * @return
     */
    public int send(long sessionId, IoBuffer buf);
    
    /**
     * 发送数据
     * 
     * @param session
     * @param buf
     * @return 
     */
    public int send(IoSession session, IoBuffer buf);

    /**
     * 主动关闭连接
     *
     * @param sessionId
     */
    public void close(long sessionId);
    
    /**
     * 主动关闭连接
     * @param session 
     */
    public void close(IoSession session);
}
