package com.game.server;

import com.game.ipfind.manager.IPFinderManager;
import com.game.login.LoginVerify;
import com.game.login.UserCacheManager;
import com.game.thread.HttpThread;
import com.game.db.DBFactory;
import com.game.http.GameHttpServer;
import com.game.thread.ThreadPoolManager;
import game.core.dblog.LogService;
import game.core.net.Config.ServerConfig;
import game.core.script.ScriptManager;
import game.core.message.MessageDictionary;
import game.core.net.server.SocketServer;
import game.core.http.HttpAsyncClient;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.xml.sax.SAXException;

/**
 *
 * @author Administrator
 */
public class GateServer extends SocketServer {

    //连接世界服务器
    private static HttpThread httpThread = null;

    private final static Logger logger = LogManager.getLogger(GateServer.class);

    public GateServer(String serverName, int port) {
        super(serverName, port);
    }

    /*
     * 用枚举来实现单例九 零一 起玩www.901  75.com
     */
    private enum Singleton {

        INSTANCE;
        GateServer processor;

        Singleton() {
            this.processor = new GateServer();
        }

        GateServer getProcessor() {
            return processor;
        }
    }

    //返回单例
    public static GateServer GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
//

    private GateServer() {
        this("登录验证服务器", 8000);
//        initLogSystem();
        //初始当前时间系统
        TimeUtils.setServerBeginTime(System.currentTimeMillis());

    }

    @Override
    protected void init() {
        try {
            initDbSystem();
            IDConfigUtil.m_ServerId = ServerConfig.getServerId();//因要支持多个LS的话，就需要启用这个ID值
            IDConfigUtil.platfromID = ServerConfig.getLsId();

            //日志系统初始化
            LogService.getInstance();

            if (IDConfigUtil.platfromID < 1) {
                logger.error("当前没有配置 lsID, 请在server-config.xml中配置此两个值");
                System.exit(0);
            }
            port = ServerConfig.getServerPort();
            server_name = ServerConfig.getServerName();
            httpThread = new HttpThread("httpThread-Thread");
            loadMessageDictionaryConfig();
            ScriptManager.getInstance().load();
            DBFactory.LOGIN_DB.getSessionFactory();
            initCacheManager();
            LoginVerify.getInstance().loadDatas();
            IPFinderManager.getInstance().deal().loadData();
            //异步的http客户端
            HttpAsyncClient.instance.init(4);
            //定时任务线程池
            ThreadPoolManager.getInstance().init(1);
        } catch (Exception ex) {
            logger.error("当前没有配置 lsID, 请在server-config.xml中配置此两个值", ex);
            System.exit(1);
        }
    }

    @Override
    public void stop() {
        httpThread.stop(true);
        GameHttpServer.getInstance().stop();
        ClientHandlerAdapter.stop();
        super.stop();
    }

    @Override
    public void run() {
        super.run();
        ClientHandlerAdapter.start();
        httpThread.start();
        //外网消息定时发送
        new Timer("Send-Timer").schedule(new TimerTask() {
            @Override
            public void run() {
                ClientHandlerAdapter.Send();
            }
        }, 1, 1);
        start(new ClientChannelImpl(30));//开始监听
    }

    //初始化数据库系统
    private void initDbSystem() throws ParserConfigurationException, IOException, SAXException {
        //连接DB数据库
        String filePath = System.getProperty("user.dir") + File.separator + "config" + File.separator + "server-config.xml";
        ServerConfig.getInstance().load(filePath);
    }

    /**
     * 加载系统配置信息.
     *
     * 系统启动时初始化请传true，系统启动后重新加载请传false.
     */
    private void loadMessageDictionaryConfig() throws Exception {
        MessageDictionary.getInstance().load("com.game");
    }

    /**
     * 初始缓存系统.
     *
     * @param
     */
    private void initCacheManager() {
        String filePath = System.getProperty("user.dir") + File.separator + "config" + File.separator + "ehcache.xml";
        UserCacheManager.getInstance().initialize(filePath);
    }

}
