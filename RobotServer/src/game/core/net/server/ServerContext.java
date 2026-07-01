package game.core.net.server;

import org.apache.mina.core.buffer.IoBuffer;

/**
 *
 * <b>服务器上下文对象.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class ServerContext
{

    private IoBuffer buff;

    public ServerContext()
    {
        buff = IoBuffer.allocate(256);
        buff.setAutoExpand(true);
        buff.setAutoShrink(true);
    }

    public IoBuffer getBuffer()
    {
        return buff;
    }

}
