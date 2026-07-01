package game.core.net.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 服务器初始化
 *
 * @author soko
 *
 */
public abstract class BaseServer implements Runnable
{
    //服务器名字
    protected String server_name;
    //端口
    protected int port;
    //默认主线程
    public static final String DEFAULT_MAIN_THREAD = "ServerMain";

    /**
     * @param serverName
     * @param port
     *
     */
    protected BaseServer(String serverName, int port)
    {
        server_name = serverName;
        this.port = port;
    }

    //服务器初始化

    protected void init()
    {

    }

    //服务器运行
    @Override
    public void run()
    {
        // 初始化运行时
        init();
        //注册服务器关闭线程
        Runtime.getRuntime().addShutdownHook(new Thread(new CloseByExit(server_name)));
    }

    /**
     * 服务器关闭事件
     */
    protected abstract void stop();

    //服务器关闭线程
    private class CloseByExit implements Runnable
    {
        //日志
        private final Logger log = LogManager.getLogger(CloseByExit.class);

        //服务器名字
        private final String server_name;

        public CloseByExit(String server_name)
        {
            this.server_name = server_name;
        }

        @Override
        public void run()
        {
            //执行关闭事件
            stop();
            log.info(this.server_name + " Stop!");
        }

    }

}
