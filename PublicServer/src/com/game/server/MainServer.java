package com.game.server;

import com.data.script.ScriptConfigManager;
import com.game.dailyactive.timer.DailyActiveTimer;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.servermatch.structs.LeagueConfig;
import com.game.servermatch.timer.LeagueTimer;
import com.game.http.GameHttpServer;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.server.errorlog.ErrorLogThread;
import com.game.server.filter.InnerChannelImpl;
import com.game.server.filter.InnerMsgAdapter;
import com.game.server.script.IErrorLogScript;
import com.game.server.script.IGSManagerScript;
import com.game.server.script.IServerStart;
import com.game.server.thread.*;
import com.game.server.timer.ServerHeartTimer;
import com.game.soulanimalforest.timer.SoulAnimalForestBossBirthTimer;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.worldbonfire.timer.WorldBonfireMatchTimer;
import game.core.dblog.LogService;
import game.core.message.MessageDictionary;
import game.core.net.Config.ServerConfig;
import game.core.net.server.SocketServer;

import game.core.script.IScript;
import game.core.script.ScriptManager;
import game.core.thread.ServerThread;
import game.core.thread.TimerThread;
import game.core.timer.TimerEvent;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器启动类处理加载！
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class MainServer extends SocketServer {

    //日志系统
    private final static Logger log = LogManager.getLogger(MainServer.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        MainServer processor;

        Singleton() {
            this.processor = new MainServer();
        }

        MainServer getProcessor() {
            return processor;
        }
    }

    //单例对象接口
    public static MainServer getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    //具体的初始化函数
    public MainServer() {
        this("Public服务", 9888);
    }

    /**
     * 构造函数，初始些服务端口号
     *
     * @param serverName
     * @param port
     */
    public MainServer(String serverName, int port) {
        super(serverName, port);
        TimeUtils.setTime(System.currentTimeMillis());
    }

    private static ConcurrentHashMap<Long, String> roleName = new ConcurrentHashMap<>();

    //角色名列表
    public static ConcurrentHashMap<Long, String> getRoleName() {
        return roleName;
    }

    public IServerStart deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ServerStart);
        if (is instanceof IServerStart) {
            return (IServerStart) is;
        }
        throw new NullPointerException("没有找到具体的实例！");
    }

    public IErrorLogScript webErrorLog() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ErrorLogReport);
        if (is instanceof IErrorLogScript) {
            return (IErrorLogScript) is;
        }
        throw new NullPointerException("没有找到具体的实例！");
    }

    public IGSManagerScript gsmanager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GSManagerScript);
        if (is instanceof IGSManagerScript) {
            return (IGSManagerScript) is;
        }
        throw new NullPointerException("没有找到具体的实例！");
    }

    //服务器启动线程组
    //主线程业务
    private ServerThread wServerThread;
    private TimerThread serverTimer;//主计时器
    private ThreadGroup group;
    private ErrorLogThread errorLogThread;//日志报告
    private SaveServerParamThread wSaveServerParamThread;//服务器参数保证


    public ErrorLogThread getErrorLogThread() {
        return errorLogThread;
    }

    public ServerThread getwServerThread() {
        return wServerThread;
    }

    public SaveServerParamThread getwSaveServerParamThread() {
        return wSaveServerParamThread;
    }



    //初始化数据库系统
    private void initDbSystem() throws ParserConfigurationException, IOException, SAXException {
        //连接DB数据库
        String filePath = SystemUtils.USER_DIR + File.separator + "config" + File.separator + "server-config.xml";
        ServerConfig.getInstance().load(filePath);
    }

    /**
     * 加载系统配置信息.
     *
     */
    private void loadMessageDictionaryConfig() {
        try {
            MessageDictionary.getInstance().load("com.game");
        } catch (Exception ex) {
            log.error(ex, ex);
            System.exit(1);
        }
    }


    /**
     * 服务器初始化参数函数
     */
    @Override
    protected void init() {
        super.init();
        try {
            //初始化系统参数
            initDbSystem();
            IDConfigUtil.m_ServerId = ServerConfig.getServerId();
            IDConfigUtil.platfromID = ServerConfig.getLsId();

            if (IDConfigUtil.platfromID < 1 || IDConfigUtil.m_ServerId < 1) {
                log.info("当前没有配置 serverId 或者 lsID, 请在server-config.xml中配置此两个值");
                System.exit(0);
            }
            errorLogThread = new ErrorLogThread("errorlog_report_thread");

            int pp = ServerConfig.getServerPort();
            if (pp > 0) {
                port = pp;
            }
            server_name = ServerConfig.getServerName();
            group = new ThreadGroup("服务器线程池");
            serverTimer = new TimerThread("public-Timer", 100);
            //线程初始化
            wServerThread = new ServerThread(group, "public服务", serverTimer);

            //加载配置表
            ScriptConfigManager.GetInstance();
            ScriptConfigManager.GetInstance().reloadCofigItem();

            //注册协议文件 
            loadMessageDictionaryConfig();
            //初始化脚本
            ScriptManager.getInstance().load();
            //日志系统初始化
            LogService.getInstance();

            //分发系统变量
            LeagueConfig.load();
            //设置错误执行类
            Manager.scriptManager.setErrorServer(this);
            //初始保存线程
            wSaveServerParamThread = new SaveServerParamThread("wSaveServerParamThread");
            //加载服务器参数设置
            ServerParamUtil.loadServerParam();
            //加载服务器分组数据
            ServerMatchManager.loadAll();
            //活动排行加载数据
            Manager.activityManager.loadRank();
            //加载魂兽森林的BOSS数据
            Manager.soulAnimalForestManager.manager().init();
            //跨服排行数据
            Manager.crossRankManager.loadCrossRank();
            //加载跨服福地数据
            Manager.fudManager.deal().load();
            //加载巅峰竞技场数据
            Manager.peakManager.deal().loadAll();
            //八级阵图数据加载
            Manager.eightDiagramsManager.load();

            //跨服坐骑BOSS
            Manager.crossHorseBossManager.deal().initData();
            //仙侣对决初始化
            Manager.couplefightManager.init();
            log.info("公共服开始启动了！");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            log.error(e, e);
            System.exit(1);
        }
    }

    /**
     * 服务器的启动接口函数
     */
    @Override
    public void run() {
        super.run();
        try {
            errorLogThread.start();
            wServerThread.start();
            serverTimer.start();
            serverTimer.addTimerEvent(wServerThread, new ServerHeartTimer());
            serverTimer.addTimerEvent(wServerThread, new DailyActiveTimer());
            serverTimer.addTimerEvent(wServerThread, new WorldBonfireMatchTimer());
            serverTimer.addTimerEvent(wServerThread, new SoulAnimalForestBossBirthTimer());
            serverTimer.addTimerEvent(wServerThread, new LeagueTimer());
            wSaveServerParamThread.start();
            MessageUtils.start();
            InnerMsgAdapter.start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    GameHttpServer.getInstance().accept(ServerConfig.getBackPort());
                }
            }).start();

            new Timer("发送消息队列").schedule(new TimerTask() {
                @Override
                public void run() {
                    //发送网络消息处理
                    InnerMsgAdapter.BufferSend();
                }
            }, 1, 1);
            start(new InnerChannelImpl());
        } catch (Exception e) {
            log.error(e, e);
            System.exit(2);
        }
    }

    /**
     * 服务器停止处理
     */
    @Override
    public void stop() {
        super.stop();
        //保存数据
        ServerParamUtil.saveCounts();
        Manager.fudManager.deal().close();
        ServerMatchManager.stop();
        wSaveServerParamThread.stop(true);
        serverTimer.stop();
        wServerThread.stop(true);
        GameHttpServer.getInstance().stop();
        errorLogThread.stop(true);
        MessageUtils.stop();
        InnerMsgAdapter.stop();
        Manager.peakManager.stop(true);
        //日志系统初始化
        LogService.getInstance().shutdown();
        Manager.crossRankManager.stop();
        //关闭仙侣对决
        Manager.couplefightManager.getScript().stop();
        log.info("public服务器停止了！");
    }

    @Override
    public void setErrorLog(String key, String content) {
        MainServer.getInstance().getErrorLogThread().pushErrorExcptionLog(key, content);
    }

    //连接退出
    public void SessionQuit(ChannelHandlerContext ctx) {
        deal().OnSessionOut(ctx, 0);
    }

    public void addTimerEvent(TimerEvent te) {
        serverTimer.addTimerEvent(wServerThread, te);
    }

    public void removeTimerEvent(TimerEvent te) {
        serverTimer.removeTimerEvent(te);
    }
}
