package com.game.server;

import com.game.boss.struct.BossTypeConst;
import com.game.client.Client;
import com.game.client.LoginClient;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.server.worker.LoginServerWorker;
import com.game.server.worker.SendMessWorker;
import com.game.skill.config.SkillEventContainer;
import com.game.structs.Config;
import game.core.concurrent.OrderedQueuePoolExecutor;
import game.core.message.MessageDictionary;
import game.core.message.RMessage;
import game.core.message.SMessage;
import game.core.thread.ServerThread;
import game.core.thread.TimerThread;
import game.core.util.TimeUtils;
import game.message.heartMessage;
import org.apache.commons.lang.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.util.ConcurrentHashSet;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import org.apache.log4j.xml.DOMConfigurator;

public class RobotServer implements Runnable {
    private final static Logger log = LogManager.getLogger(RobotServer.class);
    private final static Logger logger = LogManager.getLogger("flow");

    // 服务器线程组
    private ThreadGroup group;
    //服务器主线程
    private ServerThread serverThread;
    //服务器主timer
    private final TimerThread serverMainTimer;

    public volatile long debugSessionId;
    public volatile long debugRoleId;

    public int heartResMsgId = heartMessage.ResHeart.MsgID.eMsgID.getNumber();

    class MsgRecorder {
        public int messageReceivedLast;
        public int dispatchingLast;
    }

    public HashMap<Integer, MsgRecorder> msgRecord = new HashMap<>();

    public ServerThread getServerThread() {
        return serverThread;
    }

    public void setServerThread(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    public RobotServer() {
        serverMainTimer = new TimerThread("main_timer", 100);

        group = new ThreadGroup("group");
        serverThread = new ServerThread(group, "机器人服务器", serverMainTimer);

        initLog();
    }

    private void initLog() {
        log.info("初始化日志系统。。。。。。");
        StringBuilder pathBuilder = new StringBuilder(System.getProperty("user.dir") + File.separator + "config" + File.separator);
        DOMConfigurator.configureAndWatch(pathBuilder.toString() + "log4j_socket.xml");
        pathBuilder.append("log4j2.xml");
        try {
            ConfigurationSource source = new ConfigurationSource(new FileInputStream(pathBuilder.toString()));
            Configurator.initialize(null, source);
        } catch (Exception e) {
            log.error(e.getMessage());
            System.exit(1);
        }
    }

    private void init() {
        try {
            log.error("加载协议。。。。。。！");
            loadMessageDictionaryConfig();

            log.error("加载数据库连接。。。。。。！");
            initDbSystem();

            //加载地图配置
            log.error("加载地图。。。。。。！");
            Manager.mapCfgManager.loadMaps();

            //技能配置
            log.error("加载技能。。。。。。！");
            String skillConfig = "config/SkillCfg.xml";
            SkillEventContainer.getInstance().load(skillConfig);

            log.error("加载完成， 可以执行命令了！");
            //配置表中的预设机器人创建连接并登陆游戏
            Manager.playerManager.deal().init();
        } catch (ParserConfigurationException | IOException | SAXException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        init();
        logger.info("开始启动线程");
//        serverThread.start();
//        serverThread.addTimerEvent(new ServerHeartTimer(-1, 0, 1000));
        new Timer("Heart-Timer").schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Manager.playerManager.deal().tickEvent();

                } catch (Exception e) {
                    log.info(e, e);
                }
            }
        }, 1000, 500);


        new Timer("GetMap-Num-Timer").schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    for (Map.Entry<Long, Player> entry : Manager.playerManager.getPlayers().entrySet()) {
                        Player player = entry.getValue();
                        if (player == null) {
                            continue;
                        }
//                        Manager.bossManager.deal().sendReqOpenDreamBoss(player, BossTypeConst.WORLD_BOSS);
//                        Manager.bossManager.deal().sendReqSuitGemBossPanel(player, 0);
                        break;
                    }
                    long now =  TimeUtils.Time();
                    for (Map.Entry<Integer, LoginClient> entry: Manager.playerManager.getLoginPlayers().entrySet()){
                        LoginClient loginClient =  entry.getValue();
                        if (!loginClient.canConnect(now)){
                            continue;
                        }
                        Client.getRecvExcutor().addTask((long)loginClient.id, new LoginServerWorker(loginClient,Config.getLoginServerPort()));
                    }
                } catch (Exception e) {
                    log.info(e, e);
                }
            }
        }, 1000, 10000);

        //内网消息定时发送
        new Timer("Inner-Send-Timer").schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage();
            }
        }, 1, 1);
    }

    /**
     * 加载系统配置信息.
     */
    private void loadMessageDictionaryConfig() {
        try {
            String filePath = SystemUtils.USER_DIR + File.separator + "config" + File.separator + "messages.xml";
//            log.info(filePath);
            MessageDictionary.getInstance().load(filePath);
        } catch (Exception ex) {
            System.out.println(ex);
            System.exit(1);
        }
    }

    //初始化数据库系统
    private void initDbSystem() throws ParserConfigurationException, IOException, SAXException {
        //连接DB数据库
        String filePath = SystemUtils.USER_DIR + File.separator + "config" + File.separator + "server-config.xml";
        Config.load(filePath);
    }

    private static final ConcurrentHashSet<IoSession> sessions = new ConcurrentHashSet<>();

    public static void addSession(IoSession session) {
        synchronized (sessions) {
            sessions.add(session);
        }
    }

    public static void removeSession(IoSession session) {
        synchronized (sessions) {
            sessions.remove(session);
        }
    }

    private void sendMessage() {
        ArrayList<IoSession> sess = new ArrayList<>();
        synchronized (sessions) {
            sess.addAll(sessions);
        }
        for (IoSession ioSession : sess) {
            if (ioSession.isWriteSuspended()) {
                log.error(ioSession.getId() + "正在忙， 不能写数据了！");
                continue;
            }

            IoBuffer sendbuf = null;
            synchronized (ioSession) {

                if (ioSession.containsAttribute("sendheart")) {
                    logger.error(ioSession.getAttribute("roleId") + "发送心跳了！");
                    ioSession.removeAttribute("sendheart");
                }

                if (ioSession.containsAttribute("SEND_BUF")) {
                    sendbuf = (IoBuffer) ioSession.getAttribute("SEND_BUF");
                    ioSession.removeAttribute("SEND_BUF");
                }
            }
            try {
                if (sendbuf != null && sendbuf.position() > 0) {
                    sendbuf.flip();
                    WriteFuture wf = ioSession.write(sendbuf);
                    wf.await();
                    if (debugSessionId == ioSession.getId()) {
                        String info = "";
                        synchronized (ioSession) {
                            if (ioSession.containsAttribute("补发一个心跳消息")) {
                                info = "本次包含补发心跳消息";
                                ioSession.removeAttribute("补发一个心跳消息");
                            }
                        }
                        logger.error(String.format("huhu debug role:%d send msg complete size:%d %s", debugRoleId, sendbuf.array().length, info));
                    }
                }
            } catch (Exception e) {
                log.info(e, e);
            }
        }
    }

    private static final OrderedQueuePoolExecutor SendExcutor = new OrderedQueuePoolExecutor("发送队列", 300, -1);

    //发送消息 
    public static void sendMess(IoSession session, SMessage msg) {
        SendExcutor.addTask(session.getId(), new SendMessWorker(session, msg));
    }

    public static OrderedQueuePoolExecutor getSendExcutor() {
        return SendExcutor;
    }

    public void debugNioReceived(IoSession session, IoBuffer msg) {
        int prepos = msg.position();
        int msgid = msg.getInt();
        msg.position(prepos);
        if (msgid != heartResMsgId) {
            return;
        }
        MsgRecorder recorder = null;
        synchronized (msgRecord) {
            if (!msgRecord.containsKey(msgid)) {
                recorder = new MsgRecorder();
                msgRecord.put(msgid, recorder);
            } else {
                recorder = msgRecord.get(msgid);
            }
            recorder.messageReceivedLast++;
        }
    }

    public void debugNioDispatched(IoSession session, int msgid) {
        if (msgid != heartResMsgId) {
            return;
        }
        synchronized (msgRecord) {
            MsgRecorder recorder = msgRecord.get(msgid);
            recorder.messageReceivedLast--;
            recorder.dispatchingLast++;
        }
    }

    public void debugExecutorActioned(IoSession session, RMessage msg) {
        if (msg.getId() != heartResMsgId) {
            return;
        }
        synchronized (msgRecord) {
            MsgRecorder recorder = msgRecord.get(msg.getId());
            recorder.dispatchingLast--;
        }
    }

    public String getMsgRecord() {
        StringBuilder ret = new StringBuilder("心跳消息流程统计:\n");
        MsgRecorder retr = new MsgRecorder();

        synchronized (msgRecord) {
            MsgRecorder recorder = msgRecord.get(heartResMsgId);
            retr.messageReceivedLast = recorder.messageReceivedLast;
            retr.dispatchingLast = recorder.dispatchingLast;
        }

        ret.append(String.format("停在接收时候的数量:%d 停在分发处的数量:%d (正常应该都是0)\n", retr.messageReceivedLast, retr.dispatchingLast));

        return ret.toString();
    }

    /**
     * 获取GameServer的实例对象.
     *
     * @return
     */
    public static RobotServer getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        RobotServer processor;

        Singleton() {
            this.processor = new RobotServer();
        }

        RobotServer getProcessor() {
            return processor;
        }
    }
}
