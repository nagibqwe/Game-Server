/**
 * @date 2014/3/18 21:09
 * @author ChenLong
 */
package game.core.net.communication;

import com.game.server.RobotServer;
import game.core.net.server.IServer;
import game.core.util.SessionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>通信抽象基类</b>
 *
 * @author ChenLong
 */
public abstract class AbstractCommunication extends IoHandlerAdapter implements ICommunication {

    private static final Logger log = LogManager.getLogger(AbstractCommunication.class);
    private final static Logger logger = LogManager.getLogger("flow");
    private final Map<Long, IoSession> sessionMap = new ConcurrentHashMap<>();
    private int maxConnectNum = 0;
    protected IServer server = null;

    public AbstractCommunication(IServer server) {
        this(server, 10);
    }

    public AbstractCommunication(IServer server, int maxConnectNum) {
        this.server = server;
        this.maxConnectNum = maxConnectNum;
    }

    @Override
    public int accept(int port) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean connect(String ip, int port) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ConnectFuture connectAsync(String ip, int port) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private boolean bindSessionID(long sessionId, IoSession session) {
        return (sessionMap.put(sessionId, session) == null);
    }

    private boolean unbindSessionID(long sessionId) {
        return (sessionMap.remove(sessionId) != null);
    }

    @Override
    public IoSession getSession(long sessionId) {
        return sessionMap.get(sessionId);
    }

    @Override
    public int getSessionCount() {
        return sessionMap.size();
    }

    @Override
    public int send(long sessionId, IoBuffer buf) {
        IoSession session = getSession(sessionId);
        if (session != null) {
            send(session, buf);
        } else {
            log.error("Cannot find session. sessionId = " + sessionId);
        }
        return 0;
    }

    @Override
    public int send(IoSession session, IoBuffer buf) {
        try {
            session.write(buf);
        } catch (Exception e) {
            log.error("Message send error! cause: " + e.getMessage());
        }

        return 0;
    }

    @Override
    public void close(long sessionId) {
        IoSession session = this.getSession(sessionId);
        if (session != null) {
            close(session);
        } else {
            log.error("Cannot find session. sessionId = " + sessionId);
        }
    }

    @Override
    public void close(IoSession session) {
        try {
            SessionUtils.closeSession(session, "AbstractCommunication close", true);
        } catch (InterruptedException e) {
            log.error("Cannot find session. sessionId = ", e);
        }
//        CloseFuture cf = session.closeNow();
//        try {
//            cf.await();
//        } catch (InterruptedException ex) {
//            log.error("Cannot find session. sessionId = ", ex);
//        }
    }

    /**
     * session创建
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void sessionCreated(IoSession session) throws Exception {
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        if (getSessionCount() <= maxConnectNum) {
            bindSessionID(session.getId(), session);
            server.sessionCreate(session);
        } else {
            log.error("too many connect " + maxConnectNum);
        }
    }

    /**
     * @param session
     * @throws Exception
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        server.sessionClosed(session);
        unbindSessionID(session.getId());
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        server.sessionIdle(session, status);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        server.exceptionCaught(session, cause);
    }

    /**
     * 数据接收
     *
     * @param session
     * @param obj
     * @throws Exception
     */
    @Override
    public void messageReceived(IoSession session, Object obj) throws Exception {
        if (obj instanceof byte[]) {
            byte[] bytes = (byte[]) obj;
            IoBuffer buf = IoBuffer.allocate(bytes.length);
            buf.put(bytes);
            buf.flip();
            RobotServer.getInstance().debugNioReceived(session, buf);
            server.doCommand(session, buf);
        } else if (obj instanceof IoBuffer) {
            RobotServer.getInstance().debugNioReceived(session, (IoBuffer) obj);
            server.doCommand(session, (IoBuffer) obj);
        } else {
            log.error("obj instanceof " + obj.getClass().getName());
        }
    }
}
