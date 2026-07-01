package com.game.server;

import com.data.script.ScriptConfigManager;
import com.game.backgrand.manager.BackGrandServer;
import com.game.backgrand.script.IBackCommandScript;
import com.game.bi.manager.BIManager;
import com.game.chat.timer.ChatHeartTimer;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.copymap.timer.CopyMapTeamTimer;
import com.game.crossserver.timer.CrossFightTimer;
import com.game.dailyactive.timer.DailyActiveTimer;
import com.game.fightserver.manager.FightClientManager;
import com.game.log.LogDataManager;
import com.game.manager.Manager;
import com.game.map.timer.MapDelTimer;
import com.game.player.structs.SessionAttribute;
import com.game.player.timer.OnlineNumTimer;
import com.game.publicserver.structs.ConnectPublicServer;
import com.game.recharge.manager.RechargeServer;
import com.game.roleLog.RoleUpdateLogService;
import com.game.script.structs.ScriptEnum;
import com.game.server.filter.ClientChannelImpl;
import com.game.server.filter.ClientMsgAdapter;
import com.game.server.filter.InnerChannelImpl;
import com.game.server.impl.GameMapServerGroup;
import com.game.server.impl.MapServer;
import com.game.server.impl.MapServerGroup;
import com.game.server.social.SocialServerClient;
import com.game.server.messageEvent.OtherToGameMesageTranslator;
import com.game.server.messageEvent.OtherToGameMessage;
import com.game.server.messageEvent.OtherToGameMessageHandler;
import com.game.server.messageEvent.RMessageWorkHandler;
import com.game.server.script.IServerScript;
import com.game.server.thread.ErrorLogThread;
import com.game.server.timer.ServerHeartTimer;
import com.game.structs.GlobalType;
import com.game.team.timer.TeamHeartTimer;
import com.game.thread.DispatchProcessor;
import com.game.thread.InnerServerProcessor;
import com.game.timer.GameTimer;
import com.game.txpushsdk.manager.PushMessManager;
import com.game.utils.MessageEnum;
import com.game.utils.ServerParamUtil;
import com.lmax.disruptor.dsl.ProducerType;
import game.core.db.DataBaseConfig;
import game.core.dblog.LogService;
import game.core.disruptor.*;
import game.core.http.HttpAsyncClient;
import game.core.message.*;
import game.core.net.Config.ServerConfig;
import game.core.net.Config.ServerEnum;
import game.core.net.server.SocketServer;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import game.core.thread.ServerThread;
import game.core.thread.TimerThread;
import game.core.util.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.traffic.TrafficCounter;
import io.netty.util.AttributeKey;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static game.core.util.SessionUtils.SEND_BUF;


public class GameServer extends SocketServer {

    public static final AttributeKey<Long> SESSIONID = AttributeKey.newInstance("sessionId");

    //版本号,后续的版本号修改
    public static final String version = "20170615";
    private final static Logger logger = LogManager.getLogger(GameServer.class);
    private final static Logger flowlog = LogManager.getLogger("SERVERFLOW");
    private final static Logger messagelog = LogManager.getLogger("MESSAGELOG");
    private static final ConcurrentHashMap<Long, Long> worldExcTimeOut = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<ChannelHandlerContext, Long> sessiontime = new ConcurrentHashMap<>();
    //技能配置
    private static final String skillConfg = "config/SkillCfgXML.xml";
    //开始运行
    public static boolean isConnectOver = false;
    private static DisruptorOrderPoolExecutor<SMessageEvent, ChannelHandlerContext, SMessage> sendExc = null;
    private static DisruptorOrderPoolExecutor<MessageEvent, ChannelHandlerContext, RMessage> decodeExcutor = null;
    private static DisruptorOrderPoolExecutor<TaskEvent<OtherToGameMessage>, Long, OtherToGameMessage> worldExcutor;
    private static int worldLevelCheckTime = 0;
    private static boolean isStop = false;

    // 野图线程组
    private MapServerGroup worldMapServerGroup;

    // 副本线程组
    private MapServerGroup copyMapServerGroup;

    //服务器主timer
    private final TimerThread serverTimer;

    //副本timer
    private final TimerThread copyMapTimer;

    //野图timer
    private final TimerThread worldMapTimer;

    //错误日志分析线程
    private ErrorLogThread errorLogThread;

    //服务器主线程
    private ServerThread mainThread;

    //服务器业务线程
    private ServerThread assistThread;

    private GameServer() {
        super("游戏逻辑服务器", 6000);
        isConnectOver = false;
        serverTimer = new TimerThread("main_timer", 100);
        copyMapTimer = new TimerThread("copyMap_timer", 100);
        worldMapTimer = new TimerThread("worldMap_timer", 100);

        errorLogThread = new ErrorLogThread("errorlog_report_thread");
        ThreadGroup group = new ThreadGroup("startGroup");
        mainThread = new ServerThread(group, "mainThread", serverTimer);
        assistThread = new ServerThread(group, "assistThread", serverTimer);
    }

    public static IServerScript getServerScript() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ServerBaseScript);
        if (is instanceof IServerScript) {
            return (IServerScript) is;
        } else {
            return null;
        }
    }

    public static int getWorldLevelCheckTime() {
        return worldLevelCheckTime;
    }

    public static void setWorldLevelCheckTime(int worldLevelCheckTime) {
        GameServer.worldLevelCheckTime = worldLevelCheckTime;
    }

    //服务器处理进程
    public static void decodeExcu(ChannelHandlerContext ctx, RMessage msg) {
        int functionID = MessageNumber.getFunction(msg.getId());
        if (functionID == MessageEnum.MSG_REGISTER) {
            decodeExcutor.publishEvent(ctx, msg);
            sessiontime.put(ctx, System.currentTimeMillis());
            return;
        }
        msg.setContext(ctx);
        DispatchProcessor.getInstance().deal().dispatch(msg);
    }

    //战斗服处理消息
    public static void FightExcu(ChannelHandlerContext ctx, RMessage msg) {
        msg.setContext(ctx);
//        int sessionid = ctx.channel().attr(SESSIONID).get().intValue();

        decodeExcutor.publishEvent(ctx, msg);
//        msg.debuginfo = MSG_TO_FIGHT;
//        Long srcId = msg.getSrcId();
//        if (srcId < 1) {
//            decodeExcutor.addTask(sessionid, new FightWorker(msg.getId(), ctx, msg));
//        } else {
//            int sess = srcId.intValue();
//            decodeExcutor.addTask(sess, new FightWorker(msg.getId(), ctx, msg));
//        }
        sessiontime.put(ctx, System.currentTimeMillis());
    }

    //战斗服或pub发给游戏服要处理的消息
    public static void worldExec(ChannelHandlerContext ctx, RMessage msg) {
        msg.setContext(ctx);
//        if (ctx == GameServer.getInstance().publicSession) {
//            msg.debuginfo = MSG_PUB_TO_GAME;
//        } else {
//            msg.debuginfo = MSG_FIGHT_TO_GAME;
//        }
//        worldExcutor.addTask(RandomUtils.randomIntValue(0, 5), new WorldWorker(msg));

        worldExcutor.publishEvent(msg.getSrcId(), new OtherToGameMessage(msg, ctx, 1, msg.getSrcId()));
        worldExcTimeOut.put(msg.getSrcId(), System.currentTimeMillis());
    }

    // 战斗服发给客户端的消息
    public static void worldExec(ChannelHandlerContext ctx, OtherServerToPlayerMessage msg) {
//        Work work = new Work(msg.getRoleIds(), msg);
//        if (ctx == GameServer.getInstance().publicSession) {
//        } else {
////            flowlog.error(" 战斗通知玩家msgId = " + msg.getId() + ", 角色ID数量："  + msg.getRoleIds().size());
//        }
//        //取整形值处理
//        Long lvalue = msg.getSendId();
//        worldExcutor.addTask(lvalue.intValue(), work);
        worldExcutor.publishEvent(msg.getSendId(), new OtherToGameMessage(msg.getRoleIds(), msg.getId(), msg.getBytes(), 2, msg.getSendId()));
        worldExcTimeOut.put(msg.getSendId(), System.currentTimeMillis());
    }

    public static void SendtoMessage(ChannelHandlerContext ctx, SMessage message) {
        //message.debuginfo = MSG_GAME_TO_CLIENT;
        //每一个玩家有序的加入到一个队列中
//        sendExcutor.addTask(ctx.channel().attr(SESSIONID).get().intValue(), new SendWorker(ctx, message));
//        SendWorker.sendMess(ctx, message);
        sendExc.publishEvent(ctx, message);
        sessiontime.put(ctx, System.currentTimeMillis());
    }

    public static void SessionOut(ChannelHandlerContext session) {
        if (sendExc != null) {
            sendExc.removeDataCache(session);
        }

        if (decodeExcutor != null) {
            decodeExcutor.removeDataCache(session);
        }
        sessiontime.remove(session);
    }

    //返回服务器ID值
    public int getServerId() {
        return ServerConfig.getServerId();
    }

    public String getServerName() {
        return ServerConfig.getServerName();
    }

    public String getServerPlatform() {
        return ServerConfig.getServerPlatform();
    }

    //获取地图执行线程
    public MapServer getMServer(long groupId) {
        MapServer ms = worldMapServerGroup.getMapServer(groupId);
        if (ms != null) {
            return ms;
        }
        return copyMapServerGroup.getMapServer(groupId);
    }

    @Override
    public void setErrorLog(String key, String content) {
        GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog(key, content);
    }

    @Override
    protected void init() {
        super.init();
        try {
//            initDbSystem();
            IDConfigUtil.m_ServerId = ServerConfig.getServerId();
            IDConfigUtil.platfromID = ServerConfig.getLsId();

            if (IDConfigUtil.platfromID < 1 || IDConfigUtil.m_ServerId < 1) {
                logger.error("当前没有配置 serverId 或者 lsID, 请在server-config.xml中配置此两个值");
                System.exit(0);
            }
            port = ServerConfig.getServerPort();
            server_name = ServerConfig.getServerName();
            String http = ServerConfig.GetHttpHeart();
            //加载配置表
            ScriptConfigManager.GetInstance();

            ScriptConfigManager.GetInstance().reloadCofigItem();

            if (http.length() > 0) {
                GlobalType.HEART_WEB = http;
                logger.info("当前心跳HTTP为：" + http);
            }

            //设置当前游戏支持人数等级为
            String param = ServerConfig.getPeopleLevels();
            int minThread = 8;
            int sendSize = 1024 * 256;
            int wordSize = 512 * 128;
            int innerSize = 1024 * 8;
            if (param != null) {
                logger.info("设置当前游戏支持人数等级为=" + param);
                switch (param) {
                    case "1": {
                        minThread = 8;
                        sendSize = 1024 * 256;
                        wordSize = 1024 * 64;
                        innerSize = wordSize;
                    }
                    break;
                    case "2": {
                        minThread = 16;
                        sendSize = 1024 * 512;
                        wordSize = 1024 * 128;
                        innerSize = sendSize;
                    }
                    break;
                    case "3": {
                        minThread = 32;
                        sendSize = 1024 * 1024;
                        wordSize = 1024 * 256;
                        innerSize = sendSize;
                    }
                    break;
                    case "4": {
                        minThread = 58;
                        sendSize = 2048 * 2048;
                        wordSize = 1024 * 256;
                        innerSize = 1024 * 1024;
                    }
                    break;
                    case "5": {
                        minThread = 64;
                        sendSize = 2048 * 2048;
                        wordSize = 1024 * 1024;
                        innerSize = 1024 * 2048;
                    }
                    break;
                    case "6": {
                        minThread = 80;
                        sendSize = 2048 * 2048;
                        wordSize = 1024 * 1024;
                        innerSize = 1024 * 2048;
                    }
                    break;
                    case "7": {
                        minThread = 88;
                        sendSize = 2048 * 2048;
                        wordSize = 1024 * 1024;
                        innerSize = 1024 * 2048;
                    }
                    break;
                    case "8": {
                        minThread = 96;
                        sendSize = 2048 * 2048;
                        wordSize = 1024 * 1024;
                        innerSize = 1024 * 2048;
                    }
                    break;
                    case "9": {
                        minThread = 112;
                        sendSize = 2048 * 2048;
                        wordSize = 1024 * 1024;
                        innerSize = 1024 * 2048;
                    }
                    break;
                    case "10": {
                        minThread = 128;
                        sendSize = 2048 * 2048;
                        wordSize = 1024 * 1024;
                        innerSize = 1024 * 2048;
                    }
                    break;
                    default:
                        minThread = 48;
                        sendSize = 2048 * 2048;
                        wordSize = 1024 * 1024;
                        innerSize = 1024 * 2048;
                        break;
                }
            }

//            decodeExcutor = new OrderedQueuePoolExecutor("网关消息解析队列", minThread, -1);
//            worldExcutor = new OrderedQueuePoolExecutor("世界消息解析队列", minThread / 2, -1);
            OtherToGameMessageHandler otgmh = new OtherToGameMessageHandler();
            worldExcutor = new DisruptorOrderPoolExecutor<>("innerWorker", new TaskEventFactory<>(), wordSize, ProducerType.MULTI, otgmh, minThread / 2, new OtherToGameMesageTranslator());
            otgmh.setDisruptorPool(worldExcutor);

            Manager.init();

            Manager.publicServerManager.init();

            Manager.scriptManager.load();

            loadMessageDictionaryConfig();
            //日志系统初始化
            LogService.getInstance();

            RoleUpdateLogService.getInstance();

            //加载技能事件配置
            Manager.skilleventManager.load(skillConfg);

            // //加载语言包
            // ServerStr.load(ServerConfig.getLangType());

            //加载脚本
            ScriptManager.getInstance();

            //设置错误日志的报错指针
            Manager.scriptManager.setErrorServer(this);

            //加载服务器的参数
            ServerParamUtil.loadServerParam();

            String severname = ServerParamUtil.serverName;
            if (StringUtils.isNotEmpty(severname)) {
                ServerConfig.setServerName(severname);
                server_name = severname;
            }

            String opentime = ServerParamUtil.serverOpenTime;
            if (StringUtils.isNotEmpty(opentime)) {
                ServerConfig.setOpenTime(opentime);
                if (!IsFightServer()) {
                    Manager.biManager.getScript().BiServer_op(103);
                }
            }

            Manager.mapCfgManager.loadMaps();
            Manager.mapCfgManager.reloadJumpTran();
            Manager.controlManager.deal().load();
            Manager.copyMapManager.manager().load();
            Manager.biManager.get4399Script().createDictTo4399();
//            decodeExcutor.setAfterEvent();

            //不是战斗服， 不加载单服的数据
            if (!IsFightServer()) {
                SMessageHandler sendhandler = new SMessageHandler();
                sendExc = new DisruptorOrderPoolExecutor<>("sendWorker", new SMessageEventFactory(), sendSize, ProducerType.MULTI, sendhandler, minThread, new SMessageEventTranslator());
                sendhandler.setDisruptorPool(sendExc);
                RMessageWorkHandler rmfAfter = new RMessageWorkHandler();
                decodeExcutor = new DisruptorOrderPoolExecutor<>("recvWorker", new MessageEventFactory(), innerSize, ProducerType.MULTI, rmfAfter, minThread / 2, new MessageEventTranslator());
                rmfAfter.setDisruptorPool(decodeExcutor);
//                sendExc.setAfterEvent();

                //加载竞拍物品
                Manager.auctionManager.load();
                //加载所有离线玩家
                Manager.playerManager.loadAllPlayer();
                //帮会，王府
                Manager.guildsManager.loadAllGuildData();
                //加载婚姻数据
                Manager.marriageManager.load();
                //加载所有的已经死亡的boss数据
                Manager.bossManager.initBossDieRecord();
                // 加载商城数据
                Manager.shopManager.load();
                //加载boss
                Manager.bossManager.loadBossConfig();
                //加载竞技场数据
                Manager.jjcManager.deal().loadAll();
                //元宝数据加载
                Manager.goldManager.initGolds();
                //禁言数据
                Manager.chatManager.loadForbidChatAll();
                //加载所有名字
                Manager.registerManager.loadAllUsedNames();
                //加载所有名字
                Manager.registerManager.deal().loadForbidAndWhite();
                //加载巅峰数据
                Manager.peakManager.deal().loadAll();
                //加载排行榜玩家数据
                Manager.rankListManager.loadRankPlayer();
                //把任务按等级加载
                Manager.taskManager.deal().loadAllCanReceiveTask();
                //加载红包的数据
                Manager.redPacketManager.initload();
                // 加载挚友数据
                Manager.chumManager.load();
                Manager.rechargeManager.load();
                Manager.guildActivityManager.deal().load();
                //仙甲寻宝数据加载
                Manager.treasureHuntXianjiaManager.loadData();
                //活动配置及活动数据加载
                Manager.activityManager.deal().load();
                //活动标签数据加载
                Manager.activityManager.deal().loadTagInfo();
                //藏宝阁
                Manager.cangbaogeManager.loadData();
                //初始化狂欢周
                Manager.crazyWeekManager.deal().initializeData();
                //天禁令
                Manager.fallingSkyManager.deal().loadData();
                //日志统计
                Manager.logManager.start();
                //功能任务
                Manager.functionTaskManager.init();
                //仙侣对决
                Manager.couplefightManager.init();
                //充值返利
                Manager.rechargeRebateManager.load();
            } else {
                RMessageWorkHandler rmfAfter = new RMessageWorkHandler();
                decodeExcutor = new DisruptorOrderPoolExecutor<>("fightRecvWorker", new MessageEventFactory(), innerSize, ProducerType.MULTI, rmfAfter, minThread, new MessageEventTranslator());
                rmfAfter.setDisruptorPool(decodeExcutor);
                FightClientManager.GetInstance().init(sendSize);
            }
            //初始主线程，并增加一个1秒执行一次的计时器
            Manager.saveThreadManager.init();
        } catch (Exception e) {
            logger.error("服务器启动进程失败了！", e);
            System.exit(1);
        }

    }

    @Override
    public void run() {
        super.run();
        logger.info("开始启动线程");
        mainThread.start();
        assistThread.start();
        mainThread.addTimerEvent(new ServerHeartTimer());
        mainThread.addTimerEvent(new ChatHeartTimer());
        mainThread.addTimerEvent(new TeamHeartTimer());
        mainThread.addTimerEvent(new CopyMapTeamTimer());
        mainThread.addTimerEvent(new MapDelTimer());
        if (!IsFightServer()) {
            mainThread.addTimerEvent(new CrossFightTimer());//加入跨服连接心跳计时
            mainThread.addTimerEvent(new DailyActiveTimer());
            sendExc.start();
            BIManager.getInstance().start1();
        }

        errorLogThread.start();
        decodeExcutor.start();
        FightClientManager.GetInstance().start();
        worldExcutor.start();
        PushMessManager.start();
        Manager.publicServerManager.start();
        serverTimer.start();
        worldMapTimer.start();
        copyMapTimer.start();

        if (!IsFightServer()) {
            // 普通地图线程组
            worldMapServerGroup = new GameMapServerGroup(new ThreadGroup("worldMap"), "city", worldMapTimer, 5);
            // 副本线程组
            copyMapServerGroup = new GameMapServerGroup(new ThreadGroup("copyMap"), "zone", copyMapTimer, 10);
        } else {
            worldMapServerGroup = new GameMapServerGroup(new ThreadGroup("worldMap"), "city", worldMapTimer, 1, 1, 1);
            // 副本线程组
            copyMapServerGroup = new GameMapServerGroup(new ThreadGroup("copyMap"), "zone", copyMapTimer, 2, 50, 3);
        }
        Manager.mapManager.start();
        Manager.saveThreadManager.start();
        Manager.rankListManager.start();
        Manager.mapManager.startMap();
        //异步的http客户端
        HttpAsyncClient.instance.init(4);

        if(!IsFightServer()){
            //加载服务器id列表
            getServerScript().loadServerIdList();
        }
        try {
            if (!GlobalType.HEART_WEB.equals("")) {
                HashMap<String, String> infoMap = getServerInfoMap();
                HttpUtils.sendPost(GlobalType.HEART_WEB, String.format(GlobalType.HEART_PARA + "&serverInfos=%s", "start", getServerId(), 1, "start", JsonUtils.toJSONString(infoMap)));
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
        //内网消息定时发送
        if (!GlobalType.HEART_WEB.equals("")) {
            new GameTimer(1000, 1 * 60 * 1000, -1) {

                @Override
                public void execute() {
                    HttpUtils.sendPost(GlobalType.HEART_WEB,
                            String.format(GlobalType.HEART_PARA + "&online=%d&port=%d&serverType=%d&serverOpenTime=%s&registerNum=%d", "heart",
                                    getServerId(), 2, ServerConfig.getServerName(),
                                    Manager.playerManager.getOnLinePlayerNum(),
                                    ServerConfig.getBackPort(), ServerConfig.GetServerType(), ServerConfig.getServerOpenTime(), Manager.playerManager.getAllPlayerWorldInfo().size()));
                    //启动服务器发送拉取充值信息给APPSERVER
                    if (Manager.rechargeManager.getRechargeItemInfoMap().size() <= 0 && !IsFightServer()) {
                        HttpUtils.sendPost(GlobalType.HEART_WEB, String.format(GlobalType.HEART_PARA, "rechargeItem", GameServer.getInstance().getServerId(), 0, "rechargeItem"));
                    }
                    //自动开服后 登录和充值 检测
                    checkLoginOrRechargeException();
                }

                @Override
                public String getName() {
                    return "Server-Alive-Timer";
                }
            }.executeScheduleAtFixedRate();
        }
        //外网消息定时发送
        new GameTimer(1, 1, -1) {
            @Override
            public void execute() {
                try {
                    Object[] objs = ClientMsgAdapter.getChannels().toArray();
                    if (objs.length < 1) {
                        return;
                    }
                    for (Object obj : objs) {
                        if (obj == null) {
                            continue;
                        }

                        Channel ioSession = (Channel) obj;
                        ByteBuf sendbuf;
                        try {
                            synchronized (ioSession) {
                                if (ioSession.unsafe() == null) {
                                    logger.info(ioSession + "发送队列时， 连接已经断开了！2");
                                    continue;
                                }
                                if (!ioSession.isActive()) {
                                    logger.info(ioSession + "发送队列时， 连接已经断开了！4");
                                    SessionUtils.release(ioSession);
                                    continue;
                                }
                                if (ioSession.unsafe().outboundBuffer() == null) {
                                    logger.info(ioSession + "发送队列时， 连接已经断开了！3");
                                    //ioSession.attr(SEND_BUF).set(null);
                                    SessionUtils.release(ioSession);
                                    continue;
                                }
                                if (!ioSession.isWritable()) {
                                    logger.info(ioSession + "发送队列时，暂时不可写！ size=" + ioSession.unsafe().outboundBuffer().totalPendingWriteBytes());
                                    continue;
                                }
                                if (ioSession.hasAttr(SEND_BUF)) {
                                    sendbuf = ioSession.attr(SEND_BUF).get();
                                    if (sendbuf != null) {
                                        ioSession.attr(SEND_BUF).set(null);
                                    }
                                } else {
//                                    logger.info(ioSession + "没有发送的数据");
                                    continue;
                                }
                            }

                            long netsize = ioSession.unsafe().outboundBuffer().totalPendingWriteBytes();
                            if (sendbuf != null && sendbuf.readableBytes() > 0) {
                                ChannelFuture cf = ioSession.writeAndFlush(sendbuf);
//                                cf.await(1000);
                            }
                            //流量超标了， 单个的
                            if (netsize > 10000000) {
                                logger.error(" 当前连接的流量数据加之前：" + netsize);
                            }
                        } catch (Exception e) {
                            flowlog.error(e, e);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e, e);
                }
            }

            @Override
            public String getName() {
                return "Send-Timer";
            }
        }.executeScheduleAtFixedRate();

        //外网消息定时发送
        new GameTimer(1, 1, -1) {
            @Override
            public void execute() {

                SocialServerClient.getInstance().BufferSend();

                ByteBuf sendbuf;
                if (null == Manager.publicServerManager.getPublicSession()) {
                    return;
                }
                synchronized (Manager.publicServerManager.getPublicSession()) {
                    sendbuf = Manager.publicServerManager.getPublicSession().channel().attr(SEND_BUF).get();
                    if (sendbuf != null) {
                        Manager.publicServerManager.getPublicSession().channel().attr(SEND_BUF).set(null);
                    }
                }
                try {
                    if (sendbuf != null && sendbuf.readableBytes() > 0) {
                        ChannelFuture cf = Manager.publicServerManager.getPublicSession().writeAndFlush(sendbuf);
//                        cf.await();
                    }
                } catch (Exception e) {
                    flowlog.error(e, e);
                }
            }

            @Override
            public String getName() {
                return "Send-public-Timer";
            }
        }.executeScheduleAtFixedRate();

        new GameTimer(30 * 1000, 30 * 1000, -1) {
            @Override
            public void execute() {
                long now = System.currentTimeMillis();
                Iterator<Entry<Long, Long>> iter = worldExcTimeOut.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<Long, Long> en = iter.next();
                    long mapId = en.getKey();
                    long offtime = en.getValue();
                    if (now - offtime > 300000) {
                        worldExcutor.removeDataCache(mapId);
                        iter.remove();
                    }
                }

                //此处的session只是KEY作用，　并不能做其它的使用，　请注意
                List<ChannelHandlerContext> ls = new ArrayList<>(sessiontime.keySet());
                for (ChannelHandlerContext session : ls) {
                    Long offtime = sessiontime.get(session);
                    if (offtime == null) {
                        continue;
                    }
                    if ((now - offtime) > 300000) {
                        SessionOut(session);
                    }
                }
            }

            @Override
            public String getName() {
                return "clear-WorldExc-Timer";
            }
        }.executeScheduleAtFixedRate();

        //后台服务器启动
        new BackGrandServer().start();
        // 充值服务
        if (!IsFightServer()) {
            new RechargeServer().start();

            // 每分钟统计一次在线玩家数量
            assistThread.addTimerEvent(new OnlineNumTimer());
        }
        //如果是战斗服则开启战斗服的消息TICK
        if (IsFightServer()) {
            //外网消息定时发送
            new GameTimer(1, 1, -1) {
                @Override
                public void execute() {
                    FightClientManager.GetInstance().Tick();
                }

                @Override
                public String getName() {
                    return "Send-Fight-Timer-Client";
                }
            }.executeScheduleAtFixedRate();

            //如果是战斗服就返回了
            final InnerChannelImpl netimpl = new InnerChannelImpl(ServerEnum.FIGHT_SERVER_LISTEN);
            trafficStatistics(netimpl);
            start(netimpl);
            return;
        } else {
            //玩家心跳检查使用

            new GameTimer(60 * 1000, 60 * 1000, -1) {
                @Override
                public void execute() {
                    Manager.heartManager.heartCheckAll();
                }

                @Override
                public String getName() {
                    return "HeartCheck-Timer";
                }
            }.executeScheduleAtFixedRate();

            //外网消息定时发送
            new GameTimer(1, 1, -1) {
                @Override
                public void execute() {
                    ConnectFightManager.GetInstance().tick();
                }

                @Override
                public String getName() {
                    return "Send-Fight-Timer-Fightmanager";
                }
            }.executeScheduleAtFixedRate();
        }

        //1个小时的超时处理
        final ClientChannelImpl impl = new ClientChannelImpl(3600);
        trafficStatistics(impl);
        try {
            GameServer.getBackCommandScript().gmBuildGmDoc(null);
        } catch (Exception e) {
            logger.error("服务器启动生成GM文档失败", e);
        }

        if (!IsFightServer()) {
            Manager.biManager.getScript().BiServer_op(101);
            if (TimeUtils.Time() >= TimeUtils.getOpenServerTime()) {
                Manager.biManager.getScript().BiServer_op(2);
            }
        }

        start(impl);//不设置超时时间

    }

    public static IBackCommandScript getBackCommandScript() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.BackCommandBaseScript);
        if (is instanceof IBackCommandScript) {
            return (IBackCommandScript) is;
        } else {
            return null;
        }
    }

    @Override
    public void stop() {
        isStop = true;
        logger.error("游戏服接收到系统停止信号： server close!");
        //保存公会数据
        if (!GameServer.getInstance().IsFightServer()) {
            //游戏服
            Manager.guildsManager.manager().saveAllGuild();
            ServerParamUtil.saveWeddingData();
            //游戏服日志数据统计记录
            LogDataManager.instance.stop();
            //游戏服操作记录
            if (TimeUtils.Time() >= TimeUtils.getOpenServerTime()) {
                Manager.biManager.getScript().BiServer_op(3);
            }
            Manager.biManager.getScript().BiServer_op(102);
        }

        //保存玩家
        Manager.playerManager.manager().SaveAllPlayer();

        copyMapTimer.stop();
        worldMapTimer.stop();
        InnerServerProcessor.getInstance().stop(true);

        worldMapServerGroup.stop();

        copyMapServerGroup.stop();

        serverTimer.stop();

        mainThread.stop(true);

        assistThread.stop(true);

        Manager.mapManager.stop(true);

        Manager.publicServerManager.stop();

        Manager.saveThreadManager.stop();

        Manager.peakManager.stop(true);

        Manager.activityManager.stop(true);

        Manager.rankListManager.stop(true);

        Manager.taskManager.stop(true);

        decodeExcutor.stop();

        worldExcutor.stop();

        PushMessManager.stop();

        FightClientManager.GetInstance().stop();
        if (!GameServer.getInstance().IsFightServer()) {
            sendExc.stop();
            Manager.bossManager.saveBossDieRecord();
            BIManager.getInstance().stop1();
        }

        errorLogThread.stop(true);

        LogService.getInstance().shutdown();

        SocialServerClient.getInstance().stop();

        try {
            HttpUtils.sendPost(GlobalType.HEART_WEB, String.format(GlobalType.HEART_PARA, "end", getServerId(), 3, "end"));
        } catch (Exception e) {
            logger.error(e, e);
        }
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException ex) {
            logger.error(ex, ex);
        }
        RoleUpdateLogService.getInstance().shutdown();
        super.stop();
        System.out.println("停服完毕!!!!!!!!!");
    }

    public void ConnectionOut(int type, ChannelHandlerContext session) {
        switch (type) {
            case ServerEnum.PUBLIC_SERVER: {
                if (Manager.publicServerManager.getPublicServer() != null) {
                    Integer id = session.channel().attr(SessionAttribute.CONNECT_SERVER_ID).get();//, id);
                    logger.error(" id = " + id + "  的public服务器断开连接了！");
                    Manager.publicServerManager.setPublicSession(null);
                }

                if (isStop) {
                    return;
                }

                if (Manager.publicServerManager.getPublicServer() == null) {
                    Manager.publicServerManager.setPublicServer(new ConnectPublicServer(ServerConfig.getPublicIp(), ServerConfig.getPublicPort(), ServerEnum.PUBLIC_SERVER));
                }

                //重新连接public服务器
                Manager.publicServerManager.reconnect();
            }
            break;
            default: {
                int id = session.channel().attr(SessionAttribute.CONNECT_SERVER_ID).get();//, id);
                String ip = session.channel().attr(SessionAttribute.CONNECT_SERVER_IP).get();//, ip);
                int sport = session.channel().attr(SessionAttribute.CONNECT_SERVER_PORT).get();//, port);
                logger.error(" id = " + id + " ip= " + ip + " port=" + sport + " 的未知服务器断开连接了！");
            }
            break;
        }
    }

    /**
     * 加载系统配置信息.
     */
    private void loadMessageDictionaryConfig() {
        try {
            MessageDictionary.getInstance().load("com.game");
            int size = MessageDictionary.getInstance().size();
            logger.info("加载消息 size={}", size);

        } catch (Exception ex) {
            logger.error("游戏服加载协议消息异常", ex);
            System.exit(1);
        }

    }

    //检查是否是战斗服
    public boolean IsFightServer() {
        return ServerConfig.GetServerType() >= ServerEnum.FIGHT_SERVER_LISTEN && ServerConfig.GetServerType() <= ServerEnum.FIGHT_CLIENT_LIMIT;
    }

    public MapServerGroup getWorldMapServerGroup() {
        return worldMapServerGroup;
    }

    public MapServerGroup getCopyMapServerGroup() {
        return copyMapServerGroup;
    }

    public TimerThread getServerTimer() {
        return serverTimer;
    }

    public ServerThread getMainThread() {
        return mainThread;
    }


    public ServerThread getAssistThread() {
        return assistThread;
    }

    public ErrorLogThread getErrorLogThread() {
        return errorLogThread;
    }


    private enum Singleton {
        INSTANCE;
        GameServer processor;

        Singleton() {
            this.processor = new GameServer();
        }

        GameServer getProcessor() {
            return processor;
        }
    }

    public static GameServer getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private void trafficStatistics(ChannelInitializer<SocketChannel> channel) {
        new GameTimer(5 * 1000, 5 * 1000, -1) {
            @Override
            public void execute() {
                TrafficCounter trafficCounter;
                int num;
                if (channel instanceof InnerChannelImpl) {
                    num = Manager.playerManager.getPlayersCache().size();
                    trafficCounter = ((InnerChannelImpl) channel).getTrafficShaping().trafficCounter();
                } else if (channel instanceof ClientChannelImpl) {
                    trafficCounter = ((ClientChannelImpl) channel).getTrafficShaping().trafficCounter();
                    num = Manager.playerManager.getOnLinePlayerNum();
                } else {
                    return;
                }

                StringBuffer buf = new StringBuffer();
                buf.append("/****************流量统计******************/\n");
                buf.append("LastReadBytes:").append(trafficCounter.lastReadBytes()).append("\n");
                buf.append("LastReadThroughput:").append(trafficCounter.lastReadThroughput()).append("\n");
                buf.append("LastWrittenBytes:").append(trafficCounter.lastWrittenBytes()).append("\n");
                buf.append("LastWriteThroughput:").append(trafficCounter.lastWriteThroughput()).append("\n");
                buf.append("RealWrittenBytes:").append(trafficCounter.getRealWrittenBytes()).append("\n");
                buf.append("RealWriteThroughput:").append(trafficCounter.getRealWriteThroughput()).append("\n");
                buf.append("当前玩家的连接数量:").append(num).append("\n");
                int tlen = worldMapServerGroup.size() + copyMapServerGroup.size();
                buf.append("当前地图的线程个数：").append(tlen).append("\n");
                tlen = worldMapServerGroup.mapSize() + copyMapServerGroup.mapSize();
                buf.append("当前地图的个数:").append(tlen).append("\n");
                buf.append("当前数据库执行队列数:").append(Manager.saveThreadManager.getOtherServerSave().getQueueSize()).append("\n");
                if (sendExc != null) {
                    buf.append("当前发送队列缓存的对象数量:").append(sendExc.getDataQueueSize()).append("\n");
                }
                long maxMemoryValue = 0;
                AtomicLong reservedMemoryValue = null;
                try {
                    Class<?> c = Class.forName("java.nio.Bits");
                    Field maxMemory = c.getDeclaredField("maxMemory");
                    maxMemory.setAccessible(true);
                    Field reservedMemory = c.getDeclaredField("reservedMemory");
                    reservedMemory.setAccessible(true);
                    maxMemoryValue = maxMemory.getLong(null);
                    reservedMemoryValue = (AtomicLong) reservedMemory.get(null);

                    Runtime runtime = Runtime.getRuntime();
                    long free = runtime.freeMemory();
                    long total = runtime.totalMemory();
                    long useTime = total - free;
                    buf.append("totalMemory：").append(total).append(", use ：").append(useTime).append(", free ：").append(free);
                } catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    messagelog.error(e, e);
                }
                buf.append("DirectMemory：").append(maxMemoryValue).append(", 恢复 ：").append(reservedMemoryValue.get());
                buf.append("\n当前时间 ：").append(TimeUtils.NowToString());
                if (sendExc != null) {
                    buf.append("send keng：").append(sendExc.getCursor()).append(", sheng ：").append(sendExc.getRemainingCapacity()).append("\n");
                }
                if (decodeExcutor != null) {
                    buf.append("decode keng：").append(decodeExcutor.getCursor()).append(", sheng ：").append(decodeExcutor.getRemainingCapacity()).append("\n");
                }
                if (worldExcutor != null) {
                    buf.append("world keng：").append(worldExcutor.getCursor()).append(", sheng ：").append(worldExcutor.getRemainingCapacity()).append("\n");
                }
                messagelog.info(buf);
            }

            @Override
            public String getName() {
                return "TrafficShaping-Timer";
            }
        }.executeScheduleAtFixedRate();
    }

    private HashMap<String, String> getServerInfoMap() {
        HashMap<String, String> infoMap = new HashMap<>();
        infoMap.put("serverName", ServerConfig.getServerName());
        infoMap.put("serverIp", ServerConfig.getGameServerIp());
        infoMap.put("serverPort", String.valueOf(ServerConfig.getBackPort()));
        infoMap.put("serverType", String.valueOf(ServerConfig.GetServerType()));
        if (ServerConfig.getLogUrl() != null && !ServerConfig.getLogUrl().equals("")) {
            String[] str1 = ServerConfig.getLogUrl().split("\\?");
            String[] str2 = str1[0].split("//");
            String[] str3 = str2[1].split("/");

            infoMap.put("dblogIpPort", str3[0]);
            infoMap.put("dblogName", str3[1]);
        }
        infoMap.put("dblogUser", ServerConfig.getLogUser());
        infoMap.put("dblogPassword", ServerConfig.getLogPassword());

        //数据库信息
        DataBaseConfig db = ServerConfig.getInstance().getGameDBConfig();
        if(db != null && StringUtils.isNotEmpty(db.getUrl())){
            String[] str1 = db.getUrl().split("\\?");
            String[] str2 = str1[0].split("//");
            String[] str3 = str2[1].split("/");

            infoMap.put("dbIpPort", str3[0]);
            infoMap.put("dbName", str3[1]);
            infoMap.put("dbUser", db.getUsername());
            infoMap.put("dbPassword", db.getPassword());
        }
        infoMap.put("serverOpenTime", ServerConfig.getServerOpenTime());
        return infoMap;
    }


    private void checkLoginOrRechargeException() {
        if (IsFightServer()) {
            return;
        }
        //开服登录异常检测
        Manager.registerManager.deal().loginCheckException();
        //开服充值异常检测
        Manager.rechargeManager.deal().rchargeCheckException();
    }
}
