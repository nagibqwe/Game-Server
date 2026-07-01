package game.core.net.server;

import game.core.net.Config.ServerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 服务器初始化
 *
 * @author soko
 *
 */
public abstract class BaseServer implements Runnable {

    //服务器名字
    private static String server_name;
    //服务器Id
    public static int server_id;
    //服务器平台
    private static String server_platfrom;
    //Mina服务器配置信息
    protected ServerConfig serverConfig;
    //默认主线程
    public static final String DEFAULT_MAIN_THREAD = "ServerMain";

    /**
     * @param serverConfig 服务器配置信息
     *
     */
    protected BaseServer(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;

        if (this.serverConfig != null) {
            //获得服务器名称
            if ((server_name == null || ("").equals(server_name)) && (this.serverConfig.getName() != null && !("").equals(this.serverConfig.getName()))) {
                server_name = this.serverConfig.getName();
            }

            if (server_id <= 0 && this.serverConfig.getId() > 0) {
                server_id = this.serverConfig.getId();
            }

            if ((server_platfrom == null || ("").equals(server_platfrom)) && (this.serverConfig.getPlatformGroupID() != null && !("").equals(this.serverConfig.getPlatformGroupID()))) {
                server_platfrom = this.serverConfig.getPlatformGroupID();
            }
        }
    }

    //服务器初始化
    protected void init() {

    }

    //服务器运行
    @Override
    public void run() {
        // 初始化运行时
        init();
        //注册服务器关闭线程
        Runtime.getRuntime().addShutdownHook(new Thread(new CloseByExit(server_name)));
    }

    /**
     * 获得服务器名称
     *
     * @return 服务器名称
     */
    public String getServerName() {
        return server_name;
    }

    /**
     * 获得服务器ID
     *
     * @return 服务器ID
     */
    public int getServerId() {
        return server_id;
    }

    /**
     * 获得服务器平台
     *
     * @return 服务器平台
     */
    public String getServerPlatform() {
        return server_platfrom;
    }

    /**
     * 服务器关闭事件
     */
    protected abstract void stop();

    //服务器关闭线程
    private class CloseByExit implements Runnable {

        //日志
        private final Logger log = LogManager.getLogger(CloseByExit.class);

        //服务器名字
        private final String server_name;

        public CloseByExit(String server_name) {
            this.server_name = server_name;
        }

        @Override
        public void run() {
            //执行关闭事件
            stop();
            log.info(this.server_name + " Stop!");
        }

    }

}
