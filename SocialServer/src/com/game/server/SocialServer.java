package com.game.server;

import com.data.script.ScriptConfigManager;
import com.game.backgrand.server.BackGrandServer;
import com.game.manager.Manager;
import com.game.script.struct.ScriptEnum;
import com.game.server.filter.*;
import com.game.server.publicClient.PublicClientChannelImpl;
import com.game.server.publicClient.PublicClient;
import com.game.server.script.IServer;
import com.game.server.struct.MessageEvent;
import com.game.server.thread.PlayerSaveThread;
import com.game.server.thread.ServerSaveThread;
import com.game.server.timer.ServerHeartTimer;
import com.lmax.disruptor.dsl.ProducerType;
import game.core.command.ICommand;
import game.core.disruptor.DisruptorNoOrderPoolExecutor;
import game.core.disruptor.DisruptorOrderPoolExecutor;
import game.core.disruptor.MessageEventFactory;
import game.core.disruptor.MessageEventTranslator;
import game.core.message.MessageDictionary;
import game.core.message.RMessage;
import game.core.message.SMessage;
import game.core.net.Config.ServerConfig;
import game.core.net.server.SocketServer;
import game.core.script.IScript;
import game.core.thread.ServerThread;
import game.core.thread.TimerThread;
import game.core.timer.TimerEvent;
import game.core.util.IDConfigUtil;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Desc TODO
 * @Date 2021/6/7 17:51
 * @Auth ZUncle
 */
public class SocialServer extends SocketServer {

    final Logger logger = LogManager.getLogger(SocialServer.class);

    public DisruptorNoOrderPoolExecutor<game.core.disruptor.MessageEvent, ChannelHandlerContext, RMessage> decodeExc = null;

    public DisruptorOrderPoolExecutor<MessageEvent<ChannelHandlerContext, SMessage>, ChannelHandlerContext, SMessage> sendExc = null;

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        SocialServer processor;

        Singleton() {
            this.processor = new SocialServer();
        }

        SocialServer getProcessor() {
            return processor;
        }
    }

    public static SocialServer getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public SocialServer() {
        super("社交服务器", 9527);
    }

    /**
     * 主线程
     */
    private ServerThread mainThread;
    /**
     * 业务线程
     */
    private ServerThread workThread;
    /**
     * 主计时器
     */
    private TimerThread serverTimer;
    /**
     * 玩家数据保存线程
     */
    public PlayerSaveThread playerSaveThread;
    /**
     * 服务器参数保存线程
     */
    public ServerSaveThread serverSaveThread;
    /**
     * 公共服连接
     */
    public PublicClient pc;

    @Override
    protected void init() {
        super.init();
        try {
            if (ServerConfig.getLsId() < 1 || ServerConfig.getServerId() < 1) {
                logger.info("当前没有配置 serverId 或者 lsID, 请在server-config.xml中配置此两个值");
                System.exit(0);
            }

            IDConfigUtil.platfromID = ServerConfig.getLsId();
            IDConfigUtil.m_ServerId = ServerConfig.getServerId();
            port = ServerConfig.getServerPort() > 0 ? ServerConfig.getServerPort() : port;

            /*
             * 线程分组
             */
            ThreadGroup group = new ThreadGroup("服务器线程组");
            serverTimer = new TimerThread("Main-Timer", 100);
            mainThread = new ServerThread(group, "mainThread", serverTimer);
            workThread = new ServerThread(group, "workThread", serverTimer);

            playerSaveThread = new PlayerSaveThread("玩家数据保存线程");

            serverSaveThread = new ServerSaveThread("服务器参数保存线程");

            //初始化脚本
            Manager.scriptManager.load();
            //加载道具
            ScriptConfigManager.GetInstance().reloadCofigItem();
            //加载服务器参数
            Manager.serverManager.server().loadServerParams();

            Manager.globalPlayerManager.deal().loadAll();

            Manager.homeManager.deal().initRank();


            SMessageEventHandler handler = new SMessageEventHandler();
            SMessageEventFactory translator = new SMessageEventFactory();
            sendExc = new DisruptorOrderPoolExecutor<>("GameSender", translator, 1024 * 1024, ProducerType.MULTI, handler, 8, translator);
            handler.setExecutor(sendExc);

            decodeExc = new DisruptorNoOrderPoolExecutor<>(new MessageEventFactory(), ProducerType.MULTI, new RMessageDispatcher(), new MessageEventTranslator());

            pc = new PublicClient(ServerConfig.getPublicIp(), ServerConfig.getPublicPort());
        } catch (Exception e) {
            System.exit(-1);
        }

        try {
            MessageDictionary.getInstance().load("com.game");
        } catch (Exception e) {
            System.exit(-2);
        }

        logger.info("服务器初始化完成 mess={}", MessageDictionary.getInstance().size());

    }

    @Override
    public void run() {
        super.run();

        mainThread.start();

        workThread.start();

        playerSaveThread.start();

        serverSaveThread.start();

        serverTimer.start();

        sendExc.start();

        decodeExc.start();

        addTimerEvent(new ServerHeartTimer());

        new Thread(pc, "公共服连接").start();

        //TODO 启动后台监听线程
        new BackGrandServer(ServerConfig.getBackPort()).start();

        new Timer("游戏服消息发送队列").schedule(new TimerTask() {
            @Override
            public void run() {
                ServerMessageAdapter.BufferSend();
            }
        }, 1, 1);

        new Timer("公共服消息发送队列").schedule(new TimerTask() {
            @Override
            public void run() {
                pc.BufferSend();
            }
        }, 1, 1);

        ServerChannelImpl channelImpl = new ServerChannelImpl();
        start(channelImpl);
        logger.info("服务器启动完成");
    }

    @Override
    public void stop() {

        //关服之前保存下玩家数据
        Manager.globalPlayerManager.deal().saveAll();

        mainThread.stop(true);

        workThread.stop(true);

        serverTimer.stop();

        sendExc.stop();

        pc.stop();

        decodeExc.stop();

        playerSaveThread.stop(true);

        serverSaveThread.stop(true);

        super.stop();
        logger.info("服务器关闭完成");
    }

    public void addCommand(ICommand iCommand) {
        mainThread.addCommand(iCommand);
    }

    public void addTimerEvent(TimerEvent event) {
        serverTimer.addTimerEvent(mainThread, event);
    }

    public void removeTimerEvent(TimerEvent event) {
        serverTimer.removeTimerEvent(event);
    }

    public final ServerThread getWorkThread() {
        return workThread;
    }

    public IServer server() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ServerScript);
        if (is == null) {
            throw new NullPointerException("没有找到具体的脚本实例！script=" + ScriptEnum.ServerScript);
        }
        return (IServer) is;
    }

}
