package common.gm;

import com.game.client.Client;
import com.game.client.LoginClient;
import com.game.couplefight.CoupleManager;
import com.game.db.bean.roleBean;
import com.game.db.dao.roleDao;
import com.game.gm.script.IGMScript;
import com.game.manager.Manager;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.EventDefine;
import com.game.player.structs.Player;
import com.game.script.ScriptEnum;
import com.game.server.RobotServer;
import com.game.server.worker.LoginServerWorker;
import com.game.structs.Config;
import com.game.team.manager.TeamManager;
import game.core.concurrent.AbstractWork;
import game.core.concurrent.CommandQueue;
import game.core.concurrent.OrderedQueuePool;
import game.core.util.TimeUtils;
import game.message.MSG_UniverseMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class CmdScript implements IGMScript {

    private static final Logger log = LogManager.getLogger(CmdScript.class);

    @Override
    public int getId() {
        return ScriptEnum.GMScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    ///////////////////机器人操作指令//////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void cmd(String command) {
        try {
            String[] strCommand = command.split(" "); //cmd命令以空格分隔，全部转为小写字母方便进行switch
            switch (strCommand[0]) {
                case "#changeevent":
                    changeEvent(strCommand);
                    break;
                case "#setpos":
                    setRandMovePos(strCommand);
                    break;
                case "#quit":
                    quit(strCommand);
                    break;
                case "#gm":
                    chatGM(strCommand);
                    break;
                case "#login":
                    login(strCommand);
                    break;
                case "#logindb":
                    logindb(strCommand);
                    break;
                case "#loginrandom":
                    loginRandom(strCommand);
                    break;
                case "#robot"://进入竞技场
                    robot(strCommand);
                    break;
                case "#entercopymap":
                    enterCopyMap(strCommand);
                    break;
                case "#quitcopymap":
                    quitCopyMap(strCommand);
                    break;
                case "#enterdaily":
                    dailyActivity(strCommand);
                    break;
                case "#enterworldmap":
                    changeWorldMap(strCommand);
                    break;
                case "#changetaskstate":
                    changeTaskState(strCommand);
                    break;
                case "#universe":
                    reqReqUniverseWarPanel(strCommand);
                    break;
                case "#testxianjiaxunbao"://仙甲寻宝 传寻宝次数：1,10,50
                    reqReqTreasureHuntXijia(strCommand);
                    break;
                case "#testlevelgift"://福利等级礼包 传等级：240,280,320
                    reqReqReceiveLevelGift(strCommand);
                    break;
                case "#testpeakmatch": //巅峰竞技匹配
                    reqReqEnterPeakMatch(strCommand);
                    break;
                case "#loginserver":
                    loginAgentServer(strCommand);
                    break;
                case "#player":
                    player(strCommand);
                    break;
                case "#team":
                    team(strCommand);
                    break;
                case "#couple":
                    couple(strCommand);
                    break;
                default:
                    log.error("unknow cmd :" + command);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void player(String[] strCommand) {
        String cmd = strCommand[1];
        switch(cmd){
            case "add":
                String num = strCommand[2];
                int a = Integer.parseInt(num);
                PlayerManager.getInstance().deal().addPlayer(a);
                break;
            case "info":
                log.info(PlayerManager.getInstance().getPlayers().size());
                break;
        }
    }

    /**
     * 队伍相关命令
     * @param strCommand
     */
    private void team(String[] strCommand) {
        String cmd = strCommand[1];
        switch (cmd){
            case "createcoupleteam":
                int num = Integer.parseInt(strCommand[2]);
                TeamManager.getInstance().createCoupleTeam(num);
                break;
            case "info":
                TeamManager.getInstance().info();
                break;
            case "joincoupleteam":
                num = Integer.parseInt(strCommand[2]);
                CoupleManager.getInstance().joincoupleteam(num);
                break;
            case "quit":
                int player = Integer.parseInt(strCommand[2]);
                TeamManager.getInstance().quit(player);
                break;
        }
    }

    /**
     * 仙侣对决命令处理
     * @param strCommand
     */
    private void couple(String[] strCommand) {
        String cmd = strCommand[1];
        switch (cmd){
            case "apply":
                int num = Integer.parseInt(strCommand[2]);
                CoupleManager.getInstance().apply(num);
                break;
            case "match":
                CoupleManager.getInstance().match();
                break;
            case "info":
                CoupleManager.getInstance().info();
                break;
            case "premap":
                String type = strCommand[2];
                if(type.equals("group")){
                    CoupleManager.getInstance().enterGroup();
                } else if(type.equals("di")){
                    CoupleManager.getInstance().enterDi();
                } else if(type.equals("tian")){
                    CoupleManager.getInstance().enterTian();
                }
                break;
            case "clear":
                CoupleManager.getInstance().clear();
                break;
        }
    }

    private void qc(String[] command) {
        StringBuilder recv = new StringBuilder("接收队列信息\n");
        // recvExcutor
        //recv.append(String.format("当前Excutor队列:%s\n", Client.getRecvExcutor().toString()));
        OrderedQueuePool<Long, AbstractWork> pool = Client.getRecvExcutor().getPool();
        recv.append(makeContent(pool));
        String s = recv.toString();
        log.error(s);
        System.out.println(s);
        // SendExcutor
        recv = new StringBuilder("发送队列信息\n");
        //recv.append(String.format("当前Excutor队列:%s\n", RobotServer.getSendExcutor().toString()));
        pool = RobotServer.getSendExcutor().getPool();
        recv.append(makeContent(pool));
        s = recv.toString();
        log.error(s);
        System.out.println(s);
    }

    private void cleandebugid(String[] command) {
        RobotServer.getInstance().debugSessionId = 0;
        RobotServer.getInstance().debugRoleId = 0;
        System.out.println("debugsessionid 置 0");
    }

    private void ot(String[] command){
        if(RobotServer.getInstance().debugSessionId == 0)
            return;
        Player p = Manager.playerManager.deal().getPlayerByRoleId(RobotServer.getInstance().debugRoleId);
        if(p == null){
            return;
        }
        long dt = TimeUtils.Time() - p.getLastHeartSendTime();
        System.out.println("当前超时时间:" + dt);
    }

    /**
     * 反射看看有没有对应的方法 command(String[])
     *
     * @param str
     * @return
     */
    private boolean tryReflect(String str) {
        if (str.startsWith("&")) {
            str = str.substring(1);
        }

        String[] command = str.split(" ");
        if (command.length < 1) {
            return false;
        }
        try {
            Method m = this.getClass().getDeclaredMethod(command[0], String[].class);
            if (m != null) {
                m.invoke(this, (Object) command);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String makeContent(OrderedQueuePool<Long, AbstractWork> pool) {
        // key1:worker class key2:worker key val:diy info
        HashMap<Class<?>, HashMap<String, Integer>> queueinfomap = new HashMap<>();
        for (CommandQueue<AbstractWork> q : pool.getTasksQueues().values()) {
            for (AbstractWork w : q.getQueue()) {
                Class<?> c = w.getClass();
                HashMap<String, Integer> h = queueinfomap.containsKey(c) ? queueinfomap.get(c) : new HashMap<String, Integer>();
                String key = w.getKey();
                int v = h.containsKey(key) ? h.get(key) : 0;
                h.put(key, ++v);

                if (!queueinfomap.containsKey(c)) {
                    queueinfomap.put(c, h);
                }
            }
        }

        StringBuilder ret = new StringBuilder();
        for (Map.Entry<Class<?>, HashMap<String, Integer>> e : queueinfomap.entrySet()) {
            ret.append(String.format("class:%s\n", e.getKey().toString()));
            for (Map.Entry<String, Integer> se : e.getValue().entrySet()) {
                ret.append(String.format("key:%s count:%d\n", se.getKey(), se.getValue()));
            }
        }
        return ret.toString();
    }

    private void qs(String[] command) {
        StringBuilder recv = new StringBuilder("接收队列信息\n");
        // recvExcutor
        recv.append(String.format("当前Excutor队列:%s\n", Client.getRecvExcutor().toString()));
        int[] queuesize = Client.getRecvExcutor().getQueueSize(-1);
        for (int c : queuesize) {
            recv.append(String.format("队列数量:%d\n", c));
        }
        String s = recv.toString();
        log.error(s);
        System.out.println(s);
        // SendExcutor
        recv = new StringBuilder("发送队列信息\n");
        recv.append(String.format("当前Excutor队列:%s\n", RobotServer.getSendExcutor().toString()));
        queuesize = RobotServer.getSendExcutor().getQueueSize(-1);
        for (int c : queuesize) {
            recv.append(String.format("队列数量:%d\n", c));
        }
        s = recv.toString();
        log.error(s);
        System.out.println(s);
    }

    private void r(String[] command) {
        String s = RobotServer.getInstance().getMsgRecord();
        log.error(s);
        System.out.println(s);
    }

    //@PropertySet.Property("向服务器发送gm命令 gm [-1(所有机器人) 不填(当前的debugrole) roleid] [cmd name]")
    private void gm(String[] command) {
        long roleid = RobotServer.getInstance().debugRoleId;
        String cmd = command[1];
        if (command.length > 2) {
            roleid = Long.getLong(command[1]);
            cmd = command[2];
        }
        if (roleid == -1) {
            System.out.println("所有人执行一次");
            for (Player p : Manager.playerManager.getPlayers().values()) {
                p.chatGM(cmd);
            }
        } else {
            Player p = Manager.playerManager.deal().getPlayerByRoleId(roleid);
            if (p == null) {
                System.out.println("找不到执行者");
                return;
            }
            p.chatGM(cmd);
        }

        System.out.println("执行结束");
    }

    //@PropertySet.Property("关闭除debugrole以外所有的网络连接")
    private void allq(String[] command) {
        Manager.registerManager.deal().allQuitGame(RobotServer.getInstance().debugRoleId);
        log.error("关闭除debugrole以外所有的网络连接");
        System.out.println("关闭除debugrole以外所有的网络连接");
    }

    //改变机器人当前状态
    private void changeEvent(String[] strCommand) {
        int beginId = Integer.parseInt(strCommand[1]);
        int endId = Integer.parseInt(strCommand[2]);
        int event = Integer.parseInt(strCommand[3]);
        for (Player entry : Manager.playerManager.getPlayers().values()) {
            if (beginId == -1 || (entry.getrUserId() >= beginId && entry.getrUserId() <= endId)) {
                entry.setEventType(event);
//                log.info("机器人"+beginId+"~"+endId+"切换状态》》》"+event);
            }
        }
    }

    //设置指定账号机器人范围随机移动的坐标和大小
    private void setRandMovePos(String[] strCommand) {
        int beginId = Integer.parseInt(strCommand[1]);
        int endId = Integer.parseInt(strCommand[2]);
        int posX = Integer.parseInt(strCommand[3]);
        int posY = Integer.parseInt(strCommand[4]);
        int range = Integer.parseInt(strCommand[5]);
        for (Player entry : Manager.playerManager.getPlayers().values()) {
            if (beginId == -1 || (entry.getrUserId() >= beginId && entry.getrUserId() <= endId)) {
                entry.setRandPos(posX, posY);
                entry.setRange(range);
                entry.setEventType(EventDefine.Event_AreaMove);
            }
        }
    }

    //机器人退出游戏
    private void quit(String[] strCommand) {
        int beginId = Integer.parseInt(strCommand[1]);
        int endId = Integer.parseInt(strCommand[2]);
        for (Player entry : Manager.playerManager.getPlayers().values()) {
            if (beginId == -1 || (entry.getrUserId() >= beginId && entry.getrUserId() <= endId)) {
                Manager.registerManager.deal().quitGame(entry, false);
            }
        }
    }

    //机器人gm
    public void chatGM(String[] strCommand) {
        int beginId = Integer.parseInt(strCommand[1]);
        int endId = Integer.parseInt(strCommand[2]);
        String gm = "";
        int len = strCommand.length;
        for (int i = 3; i < len; i++) {
            gm += (strCommand[i] + " ");
        }
        for (Player entry : Manager.playerManager.getPlayers().values()) {
            if (beginId == -1 || (entry.getrUserId() >= beginId && entry.getrUserId() <= endId)) {
                entry.chatGM(gm);
            }
        }
    }

    private void enterCopyMap(String[] strCommand) {
        int beginId = Integer.parseInt(strCommand[1]);
        int endId = Integer.parseInt(strCommand[2]);
        int modelId = Integer.parseInt(strCommand[3]);
        for (Player entry : Manager.playerManager.getPlayers().values()) {
            if (beginId == -1 || (entry.getrUserId() >= beginId && entry.getrUserId() <= endId)) {
                entry.reqEnterZone(modelId);
            }
        }
    }

    private void changeTaskState(String[] strCommand){
        int beginId = Integer.parseInt(strCommand[1]);
        int endId = Integer.parseInt(strCommand[2]);
        int type = Integer.parseInt(strCommand[3]);
        int modelId = Integer.parseInt(strCommand[4]);
        for (Player player : Manager.playerManager.getPlayers().values()) {
            if (beginId == -1 || (player.getrUserId() >= beginId && player.getrUserId() <= endId)) {
                Manager.taskManager.deal().reqChangeTaskState(player, type,modelId);
            }
        }
    }

    private void changeWorldMap(String[] strCommand){
        int beginId = Integer.parseInt(strCommand[1]);
        int endId = Integer.parseInt(strCommand[2]);
        int mapId = Integer.parseInt(strCommand[3]);
        for (Player entry : Manager.playerManager.getPlayers().values()) {
            if (beginId == -1 || (entry.getrUserId() >= beginId && entry.getrUserId() <= endId)) {
                entry.reqTransportControl(1,mapId);
            }
        }
    }

    private void dailyActivity(String[] strCommand) {
        int beginId = Integer.parseInt(strCommand[1]);
        int endId = Integer.parseInt(strCommand[2]);
        int dailyId = Integer.parseInt(strCommand[3]);
        int modelId = 0;
        if (strCommand.length > 4) {
            modelId = Integer.parseInt(strCommand[4]);
        }
        for (Player player : Manager.playerManager.getPlayers().values()) {
            if (beginId == -1 || (player.getrUserId() >= beginId && player.getrUserId() <= endId)) {
                Manager.dailyActivityManager.deal().enterDaily(player,dailyId,modelId);
            }
        }
    }

    private void quitCopyMap(String[] strCommand) {
        int beginId = Integer.parseInt(strCommand[1]);
        int endId = Integer.parseInt(strCommand[2]);
        for (Player player : Manager.playerManager.getPlayers().values()) {
            if (beginId == -1 || (player.getrUserId() >= beginId && player.getrUserId() <= endId)) {
                player.reqCopyMapOut();
            }
        }
    }

    //指定账号机器人登录
    private void loginRandom(String[] strCommand) {
        //账号ID起始编号
        int beginId = Integer.parseInt(strCommand[1]);
        //账号ID结束编号
        int endId = Integer.parseInt(strCommand[2]);
        //角色行为类型
        if (strCommand.length > 3) {
            int event = Integer.parseInt(strCommand[3]);
            Config.setEventType(event);
        }
        //角色登陆地图
        if (strCommand.length > 4) {
            int toMap = Integer.parseInt(strCommand[4]);
            Config.setToMapid(toMap);
        }
        Config.setUserIdBegin(beginId);
        //角色职业
        int career = 0;
        if (strCommand.length > 5) {
            career = Integer.parseInt(strCommand[5]);
        }

        for(int i = beginId; i <= endId; ++i){
            Player p = new Player();
            p.setrUserId(i);
            p.setUserId(i);
            p.setCareer(career);
            p.init();
            p.setEventType(Config.getEventType());
//            Manager.playerManager.deal().addPlayer(p.getrUserId(), p);
        }
    }

    //指定数据库机器人登录
    private void logindb(String[] strCommand) {
        //账号ID起始编号
        int beginId = Integer.parseInt(strCommand[1]);
        //账号ID结束编号
        int endId = Integer.parseInt(strCommand[2]);
        //角色行为类型
        if (strCommand.length > 3) {
            int event = Integer.parseInt(strCommand[3]);
            Config.setEventType(event);
        }
        //角色登陆地图
        if (strCommand.length > 4) {
            int toMap = Integer.parseInt(strCommand[4]);
            Config.setToMapid(toMap);
        }
        Config.setUserIdBegin(beginId);

        roleDao dao = new roleDao();
        List<roleBean> roleList = dao.selectAll();
        int rUserId = beginId;
        for (roleBean role : roleList) {
            if(rUserId>endId){
                break;
            }
            try {
                Player p = roleBeanToPlayer(role);
                p.setrUserId(rUserId);
                p.init();
                p.setEventType(Config.getEventType());
//                Manager.playerManager.deal().addPlayer(p.getId(), p);
            } catch (Exception e) {
                e.printStackTrace();
            }
            rUserId++;
        }
        log.info("实际登陆游戏人数："+(rUserId-beginId));
    }

    private Player roleBeanToPlayer(roleBean role) throws Exception{
//        Player player = JSON.parseObject(VersionUpdateUtil.dataLoad(role.getRoledata()), Player.class);
        Player player = new Player();
        player.setId(role.getRoleid());
        player.setName(role.getRolename());
        player.setUserId(role.getUserId());
        player.setCareer(role.getCareer());
        player.setLevel(role.getLv());

        player.setPlatformName(role.getPlatformName());
        return player;
    }

    //指定账号机器人登录
    private void login(String[] strCommand) {
        //账号ID起始编号
        int beginId = Integer.parseInt(strCommand[1]);
        //账号ID结束编号
        int endId = Integer.parseInt(strCommand[2]);
        //角色行为类型
        if (strCommand.length > 3) {
            int event = Integer.parseInt(strCommand[3]);
            Config.setEventType(event);
        }
        Config.setUserIdBegin(beginId);
        //角色职业
        int career = 0;
        if (strCommand.length > 4) {
            career = Integer.parseInt(strCommand[4]);
        }

        for(int i = beginId; i <= endId; ++i){
            Player p = new Player();
            p.setrUserId(i);
            p.setUserId(i);
            p.setCareer(career);
            p.init();
            p.setEventType(Config.getEventType());
//            Manager.playerManager.deal().addPlayer(p.getrUserId(), p);
        }
    }
    class Work2 implements Runnable {

        private final CountDownLatch countDownLatch;

        public Work2(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                /**
                 *
                 */
                countDownLatch.await();
                LoginClient l = new LoginClient();
                int id = Integer.parseInt(Thread.currentThread().getName());
                l.id = id;
                l.start(Config.getLoginServerIp(),Config.getLoginServerPort());
                System.out.println(Thread.currentThread().getName() + "启动时间是" + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void loginAgentServer(String[] strCommand){
        //账号ID起始编号
        int beginId = Integer.parseInt(strCommand[1]);
        //账号ID结束编号
        int endId = Integer.parseInt(strCommand[2]);
        long start =  TimeUtils.Time();

        for (int i=beginId; i<=endId; i++) {
            LoginClient loginClient;
            if ( !Manager.playerManager.getLoginPlayers().containsKey(i)){
                loginClient = new LoginClient();
                loginClient.id = i;
                Manager.playerManager.getLoginPlayers().put(i,loginClient);
            }else {
                loginClient = Manager.playerManager.getLoginPlayers().get(i);
            }
            loginClient.setLastDoTime(0l);
            Client.getRecvExcutor().addTask((long)i, new LoginServerWorker(loginClient,Config.getLoginServerPort()));
        }
        //CountDownLatch countDownLatch = new CountDownLatch(100);
        //for (int i=beginId; i<endId; i++) {
        //    Thread thread = new Thread(new Work2(countDownLatch));
        //    thread.setName(i+"");
        //    thread.start();
        //    countDownLatch.countDown();
        //}
        long end = TimeUtils.Time();
        log.error("useTime -------   {}",end - start);
    }

    //设置机器人所有外观属性
    private void robot(String[] strCommand) {
        int beginId = Integer.parseInt(strCommand[1]);
        int endId = Integer.parseInt(strCommand[2]);
        for (Player entry : Manager.playerManager.getPlayers().values()) {
            if (beginId == -1 || (entry.getrUserId() >= beginId && entry.getrUserId() <= endId)) {
                entry.chatGM("&robot");
            }
        }
    }


    public void reqReqUniverseWarPanel(String[] strCommand) {
        int beginId = Integer.parseInt(strCommand[1]);
        int endId = Integer.parseInt(strCommand[2]);
        MSG_UniverseMessage.ReqUniverseWarPanel.Builder msg = MSG_UniverseMessage.ReqUniverseWarPanel.newBuilder();
        for (Player entry : Manager.playerManager.getPlayers().values()) {
            if (beginId == -1 || (entry.getrUserId() >= beginId && entry.getrUserId() <= endId)) {
                entry.sendMsg(MSG_UniverseMessage.ReqUniverseWarPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
    }
    //仙甲寻宝抽50次
    public void reqReqTreasureHuntXijia(String[] strCommand) {
        int beginId = Integer.parseInt(strCommand[1]);
        int endId = Integer.parseInt(strCommand[2]);
        int timesId = Integer.parseInt(strCommand[3]);
        game.message.TreasureHuntXianjiaMessage.ReqTreasureHuntXijia.Builder msg = game.message.TreasureHuntXianjiaMessage.ReqTreasureHuntXijia.newBuilder();
        msg.setType(6);
        msg.setTimes(timesId);
        for (Player entry : Manager.playerManager.getPlayers().values()) {
            if (beginId == -1 || (entry.getrUserId() >= beginId && entry.getrUserId() <= endId)) {
                entry.sendMsg(game.message.TreasureHuntXianjiaMessage.ReqTreasureHuntXijia.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
    }
    //福利等级礼包MSG_Welfare.ReqReceiveLevelGift
    public void reqReqReceiveLevelGift(String[] strCommand) {
        int beginId = Integer.parseInt(strCommand[1]);
        int endId = Integer.parseInt(strCommand[2]);
        int levelId = Integer.parseInt(strCommand[3]);
        game.message.WelfareMessage.ReqReceiveLevelGift.Builder msg = game.message.WelfareMessage.ReqReceiveLevelGift.newBuilder();
        msg.setLevel(levelId);
        for (Player entry : Manager.playerManager.getPlayers().values()) {
            if (beginId == -1 || (entry.getrUserId() >= beginId && entry.getrUserId() <= endId)) {
                entry.sendMsg(game.message.WelfareMessage.ReqReceiveLevelGift.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
    }

    //巅峰竞技参赛
    public void reqReqEnterPeakMatch(String[] strCommand) {
        int beginId = Integer.parseInt(strCommand[1]);
        int endId = Integer.parseInt(strCommand[2]);
        game.message.PeakMessage.ReqEnterPeakMatch.Builder msg = game.message.PeakMessage.ReqEnterPeakMatch.newBuilder();
        for (Player entry : Manager.playerManager.getPlayers().values()) {
            if (beginId == -1 || (entry.getrUserId() >= beginId && entry.getrUserId() <= endId)) {
                entry.sendMsg(game.message.PeakMessage.ReqEnterPeakMatch.MsgID.eMsgID_VALUE,msg.build().toByteArray());
                log.info(entry.getrUserId() + "：发送巅峰竞技匹配");
            }
        }
    }

}
