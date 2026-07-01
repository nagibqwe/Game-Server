package common.couplefight;

import com.data.CfgManager;
import com.data.Global;
import com.data.MessageString;
import com.data.bean.*;
import com.data.container.Cfg_Marry_battle_reward_Container;
import com.data.container.Cfg_Marry_battle_time_Container;
import com.data.struct.ReadArray;
import com.game.couplefight.exception.CouplefightException;
import com.game.couplefight.manager.CouplefightManager;
import com.game.couplefight.script.ICouplefightScript;
import com.game.couplefight.structs.*;
import com.game.couplefight.structs.params.TeamFightInfo;
import com.game.couplefight.timer.CouplefightChampionTimer;
import com.game.couplefight.timer.CouplefightGroupsTimer;
import com.game.couplefight.timer.CouplefightMatchCommand;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.db.bean.CouplefightBean;
import com.game.db.bean.CouplefightGuessBean;
import com.game.db.bean.CouplefightTeamBean;
import com.game.db.dao.CouplefightDao;
import com.game.fightroom.structs.FightRoom;
import com.game.fightroom.structs.FightRoomState;
import com.game.gameserver.manager.GameServerManager;
import com.game.gameserver.structs.ServerInfo;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.server.MainServer;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.structs.ServerType;
import com.game.structs.SessionKey;
import com.game.utils.MessageUtils;
import com.game.zone.structs.TeamPlayerInfo;
import com.game.zone.structs.ZoneTeam;
import game.core.command.ICommand;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.CouplefightMessage;
import game.message.CrossFightMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/7/1 11:08
 */
public class CouplefightScript implements ICouplefightScript {

    private final Logger log = LogManager.getLogger(CouplefightScript.class);

    @Override
    public int getId() {
        return ScriptEnum.CouplefightScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void doInit() {
        loadData();
        //开启定时器
//        start();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        getCurrentConfig();
        int currentStatus = getCurrentStatus();
        if(currentStatus == 0){
            return;
        }
        int activityId = getActivityId();
        CouplefightManager manager = Manager.couplefightManager;
        CouplefightDao dao = manager.getDao();
        //加载数据库的数据
        List<CouplefightBean> datas = dao.selectAll(activityId);
        if(datas != null && datas.size() > 0){
            List<CouplefightTeamBean> ts = dao.selectCouplefightTeam(activityId);
            List<CouplefightGuessBean> gs = dao.selectCouplefightGuess(activityId);
            //初始化总体数据
            for(CouplefightBean data : datas){
                CoupleData d = JsonUtils.parseObject(data.getData(), CoupleData.class);
                manager.getDatas().put(d.getServerGroupId(), d);
                for(String s : d.getServerIds()){
                    manager.getServers().put(s, d.getServerGroupId());
                }
                CouplefightManager.getInstance().setStatus(data.getStatus());
            }
            //添加队伍数据
            for(CouplefightTeamBean t : ts){
                CoupleData d = manager.getDatas().get(t.getGroup());
                if(d != null){
                    CoupleTeam coupleTeam = JsonUtils.parseObject(t.getData(), CoupleTeam.class);
                    coupleTeam.getWomen().setTeam(coupleTeam);
                    coupleTeam.getMen().setTeam(coupleTeam);
                    d.addTeam(coupleTeam);
                }
            }
            //初始化小组赛信息
            for(CoupleData data : manager.getDatas().values()){
                if(data.getGroups() != null){
                    for(Group group : data.getGroups()){
                        for(Long tid : group.getTeamIds()){
                            CoupleTeam team = data.getTeams().get(tid);
                            group.getTeams().add(team);
                            team.getGroupsInfo().setGroup(group);
                        }
                    }
                }
            }
            //初始化冠军赛数据
            for(CoupleData data : manager.getDatas().values()){
                ChampionData di = data.getChampionDi();
                if(di != null){
                    di.load(data);
                }
                ChampionData tian = data.getChampionTian();
                if(tian != null){
                    tian.load(data);
                }
            }
            //添加竞猜数据
            for(CouplefightGuessBean g : gs){
                CoupleData d = manager.getDatas().get(g.getGroup());
                if(d != null){
                    d.getChampionFansData().update(new Fans());
                    ChampionData championData = null;
                    if(g.getType() == 1){
                        championData = d.getChampionTian();
                    }else{
                        championData = d.getChampionDi();
                    }
                    if(championData != null){
                        ChampionGroup championGroup = championData.getRounds().get(g.getRound() - 1);
                        if(championGroup != null){
                            ChampionRoom r = championGroup.getFight(g.getFightId());
                            if(r != null){
                                r.guess(d, g.getRid(), g.getTeamId());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void apply(ChannelHandlerContext context, CouplefightMessage.G2PReqApply messInfo) {
        int serverID = context.channel().attr(SessionKey.SERVERID).get();
        String serverPlat = context.channel().attr(SessionKey.SERVERPLAT).get();
        String serverPlatID = context.channel().attr(SessionKey.SERVERPLATID).get();
        CouplefightMessage.PlayerInfo man = messInfo.getMan();
        CouplefightMessage.PlayerInfo woman = messInfo.getWoman();

        CouplefightManager manager = Manager.couplefightManager;
        //是否是报名阶段
        CouplefightMessage.ResApply.Builder res = CouplefightMessage.ResApply.newBuilder();
        if(!(manager.getStatus() == CouplefightManager.status_apply
                || manager.getStatus() == CouplefightManager.status_select_pre
                || manager.getStatus() == CouplefightManager.status_select)){
            log.warn("非报名时间 serverPlatId:{} mid:{} wid:{}", serverPlatID, man.getId(), woman.getId());
            res.setSuccess(4);
        }else {

            Integer group = getServerGroupId(serverPlatID);
            if(group == null){
                group = ServerMatchManager.deal().getGroupIDForCurOpenDay(serverPlatID, DailyActiveDefine.COUPLE_FIGHT);
            }
            CoupleData data = Manager.couplefightManager.getDatasByGroupId(group);

            String teamName = messInfo.getName();

            synchronized (data){
                if(data.getPlayers().get(man.getId()) != null || data.getPlayers().get(woman.getId()) != null){
                    log.warn("有队员已经报名 serverPlatId:{} mid:{} wid:{}", serverPlatID, man.getId(), woman.getId());
                    res.setSuccess(5);
                }else{
                    if(data.getNames().containsKey(teamName)){
                        log.warn("战队名称重复 serverPlatId:{} mid:{} wid:{} name:{}", serverPlatID, man.getId(), woman.getId(), teamName);
                        res.setSuccess(7);
                    }else{
                        //成功报名的处理 杨梅汁甘露
                        CoupleTeam team = new CoupleTeam();
                        team.setServerId(serverID);
                        team.setPlatName(serverPlat);
                        team.setId(IDConfigUtil.getId());

                        team.setWomen(new Player(woman));
                        team.setMen(new Player(man));

                        team.setName(teamName);
                        data.addTeam(team);
                        if(data.getRanks().size() < 100) {
                            data.getRanks().add(new RankInfo(team.getId(), 0));
                        }
                        log.info("报名成功 team:" + team);

                        //告知游戏服玩家报名成功
                        res.setSuccess(0);
                        if(res.getSuccess() == 0){
                            res.setTeam(team.toProto());
                            res.setTrials(team.toTrialsProto());
                        }
                    }
                }
            }
        }
        MessageUtils.send_to_player(context, messInfo.getMan().getId(), CouplefightMessage.ResApply.MsgID.eMsgID_VALUE, res.build().toByteArray());
        MessageUtils.send_to_player(context, messInfo.getWoman().getId(), CouplefightMessage.ResApply.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    /**
     * 获取服务器分组
     * @param serverPlatID
     * @return
     */
    private Integer getServerGroupId(String serverPlatID) {
        Integer id = Manager.couplefightManager.getGroup(serverPlatID);
        if(id == null){
            id = ServerMatchManager.deal().getGroupIDForCurOpenDay(serverPlatID, DailyActiveDefine.COUPLE_FIGHT);
        }
        return id;
    }


    private CoupleData getDatasByGroupId(int groupId){
        CoupleData data = Manager.couplefightManager.getDatasByGroupId(groupId);
        if(data == null){

        }
        return data;
    }

    @Override
    public void matchStart(ChannelHandlerContext context, long mId, long mpower, long wId, long wpower, long captain) {
        String serverPlatId = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupId = Manager.couplefightManager.getGroup(serverPlatId);
        CoupleData data = Manager.couplefightManager.getDatasByGroupId(groupId);

        CoupleTeam team = data.getPlayers().get(captain);
        if(team == null){
            //队伍为空
            sendMatchStartFail(context, captain, 0);
            return;
        }
        if(Manager.couplefightManager.getStatus() != CouplefightManager.status_select){
            log.warn("仙侣对决 海选赛 未开始");
            sendMatchStartFail(context, captain, 6);
            return;
        }
        if(team.getMen().getId() != mId || team.getWomen().getId() != wId){
            //队伍不一致
            sendMatchStartFail(context, captain, 3);
            return;
        }
        if(team.getTrialsInfo().getCount() >= 15){
            //超过最大次数限制
            sendMatchStartFail(context, captain, 5);
            return;
        }
        if(team.getRoom() != null){
            log.warn("玩家在房间中");
            sendMatchStartFail(context, captain, 7);
            return;
        }

        //加入匹配队列
        team.getMen().setPower(mpower);
        team.getMen().setConfirm(null);
        team.getWomen().setPower(wpower);
        team.getWomen().setConfirm(null);
        data.addMatch(team);

        log.info("仙侣对决 海选赛 开始匹配 team:" + team);

        //告诉玩家
        CouplefightMessage.ResMatchStart.Builder res = CouplefightMessage.ResMatchStart.newBuilder();
        res.setSuccess(true);
        MessageUtils.send_to_player(context, mId, CouplefightMessage.ResMatchStart.MsgID.eMsgID_VALUE, res.build().toByteArray());
        MessageUtils.send_to_player(context, wId, CouplefightMessage.ResMatchStart.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    /**
     * 发送匹配开始返回信息
     * @param captain
     * @param reason 失败原因 0未报名 1非组队状态 2不是队长 3队友错误 4队友不在线 5达到最大次数限制 6未到开始时间
     */
    private void sendMatchStartFail(ChannelHandlerContext context, long captain, int reason) {
        CouplefightMessage.ResMatchStart.Builder res = CouplefightMessage.ResMatchStart.newBuilder();
        res.setSuccess(false);
        res.setReason(reason);
        MessageUtils.send_to_player(context, captain, CouplefightMessage.ResMatchStart.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }


    @Override
    public void refresh() {
        int status = Manager.couplefightManager.getStatus();
        getCurrentConfig();
        int currentStatus = getCurrentStatus();
        if(status != currentStatus){
            int nextStatus = Manager.couplefightManager.getNextStatus();
            log.info("仙侣对决状态刷新" + status + "-" + currentStatus + "-" +nextStatus);
            Manager.couplefightManager.executor.execute(()->{
                statusChange(nextStatus);
            });
            Manager.couplefightManager.setGame(-1);
        }else{
            checkNextRound();
        }
        if(Manager.couplefightManager.getStatus() == CouplefightManager.status_select){
            match();
        }
    }

    private void checkNextRound() {
        Cfg_Marry_battle_time_Bean beforeBean = Manager.couplefightManager.getBeforeBean();
        Cfg_Marry_battle_time_Bean currentBean = Manager.couplefightManager.getCurrentBean();
        if(beforeBean != null && currentBean != null && beforeBean.getType() == currentBean.getType()){
            if(Manager.couplefightManager.getGame() != currentBean.getGame()){
                if(currentBean.getType() == 2){
                    onGroupsNextRound();
                }else if(currentBean.getType() == 3 || currentBean.getType() == 4){
                    onChampionNextRound();
                }
                Manager.couplefightManager.setGame(currentBean.getGame());
            }
        }
    }

    public int statusChange(int status) {
        switch (status){
            case CouplefightManager.status_close:
                onClose();
                break;
            case CouplefightManager.status_apply:
                onOpenApply();
                break;
            case CouplefightManager.status_select_pre:
                onSelectPre();
                break;
            case CouplefightManager.status_select:
                onSelect();
                break;
            case CouplefightManager.status_group_pre:
                onGroupPre();
                break;
            case CouplefightManager.status_group:
                onGroupsFight();
                break;
            case CouplefightManager.status_champion_pre:
                onChampionPre();
                saveDB();
                break;
            case CouplefightManager.status_di_pre:
                onChampionDiPre();
                break;
            case CouplefightManager.status_di:
                onChampionDi();
                break;
            case CouplefightManager.status_tian_pre:
                onChampionTianPre();
                break;
            case CouplefightManager.status_tian:
                onChampionTian();
                break;
            case CouplefightManager.status_over:
                onOver();
                break;
        }
        //同步到游戏服
        for(String serverPlatId : CouplefightManager.getInstance().getServers().keySet()){
            ServerInfo server = Manager.gameServerManager.getServerCache().get(serverPlatId);
            if(server != null){
                CouplefightMessage.P2GChangeStatus.Builder res = CouplefightMessage.P2GChangeStatus.newBuilder();
                res.setStatus(status);
                MessageUtils.send_to_game(server.getSession(), CouplefightMessage.P2GChangeStatus.MsgID.eMsgID_VALUE, res.build().toByteArray());
            }
        }
        return status;
    }

    /**
     * 初始化活动数据
     */
    private void clear() {
        log.info("仙侣对决 清除所有数据");
        Manager.couplefightManager.getServers().clear();
        Manager.couplefightManager.getDatas().clear();
    }


    /**
     * 比赛结束
     */
    private void onOver() {
        log.info("仙侣对决结束 等待关闭");
        Manager.couplefightManager.setStatus(CouplefightManager.status_over);
    }

    /**
     * 保存活动进程
     */
    private void saveDB() {
        try {
            CouplefightDao dao = Manager.couplefightManager.getDao();
            for(CoupleData data : Manager.couplefightManager.getDatas().values()){
                CouplefightBean bean = new CouplefightBean(data);
                for(CoupleTeam team : data.getTeams().values()){
                    CouplefightTeamBean tbean = new CouplefightTeamBean(data.getActivityId(), data.getServerGroupId(), team);
                    dao.save(tbean);
                }
                dao.save(bean);
            }
        }catch (Exception e){
            log.error(e);
        }
    }

    private void onChampionDi() {
        log.info("仙侣对决 地榜赛开始");
        //设置状态
        Manager.couplefightManager.setStatus(CouplefightManager.status_di);
        //添加轮次定时器
//        MainServer.getInstance().addTimerEvent(new CouplefightChampionTimer());
    }

    private void onChampionTian() {
        log.info("仙侣对决 天榜赛开始");
        //设置状态
        Manager.couplefightManager.setStatus(CouplefightManager.status_tian);
        //添加轮次定时器
//        MainServer.getInstance().addTimerEvent(new CouplefightChampionTimer());
    }

    /**
     * 开始冠军赛轮次
     * @param championData
     */
    private void startChampionRound(CoupleData data, ChampionData championData) {
        //更新当前轮次
        championData.setGround(championData.getGround() + 1);
        int round = championData.getGround();
        if(round > championData.getRounds().size()){
            //强制结束上一轮
            onChampionRoundOver(championData, championData.getRounds().get(championData.getRounds().size() - 1));
        }
        if(round > championData.getRounds().size()){
            log.info("仙侣对决冠军赛{} 第{}轮 不存在", championData.getType() == 1 ? "天榜" : "地榜", round);
            return;
        }
        log.info("仙侣对决冠军赛{} 第{}轮 开始", championData.getType() == 1 ? "天榜" : "地榜", round);

        ChampionGroup group = championData.getRounds().get(round - 1);
        for(ChampionRoom room : group.getRooms()){
            startChampionRoomFight(data, championData, room);
        }
    }

    /**
     * 地榜准备
     */
    private void onChampionDiPre() {
        log.info("仙侣对决 地榜赛准备");
        //设置状态
        Manager.couplefightManager.setStatus(CouplefightManager.status_di_pre);
        for(CoupleData data : Manager.couplefightManager.getDatas().values()){
            data.getChampionDi().setGround(0);
        }
    }

    /**
     * 天榜准备
     */
    private void onChampionTianPre() {
        log.info("仙侣对决 天榜赛准备");
        //设置状态
        Manager.couplefightManager.setStatus(CouplefightManager.status_tian_pre);
        for(CoupleData data : Manager.couplefightManager.getDatas().values()){
            data.getChampionTian().setGround(0);
        }
    }

    /**
     * 冠军赛准备
     */
    private void onChampionPre() {
        log.info("仙侣对决 冠军赛准备");
        //设置状态
        Manager.couplefightManager.setStatus(CouplefightManager.status_champion_pre);
        //小组赛结束处理
        for(CoupleData data : Manager.couplefightManager.getDatas().values()){
            //晋级天榜队伍
            List<CoupleTeam> tians = new ArrayList<>();
            //晋级地榜队伍
            List<CoupleTeam> dis = new ArrayList<>();
            for(Group group : data.getGroups()){
                if(group != null){
                    if(!group.isOver()){//强制结算
                        log.warn("仙侣对决 小组赛强制结算 serverGroup:{} group:{}", data.getServerGroupId(), group.getId());
                        onGroupsOver(group);
                    }
                    group.setOver(true);
                    List<CoupleTeam> ts = group.getTeams();
                    int index = 0;
                    for(CoupleTeam t : ts){
                        index++;
                        if(index <= 2){
                            tians.add(t);
                            t.setChampionsInfo(new TeamChampionsInfo(1));
                        }else if(index <= 4){
                            dis.add(t);
                            t.setChampionsInfo(new TeamChampionsInfo(2));
                        }
                    }
                }
            }

            sendPromotionInfo(dis, 3);
            sendPromotionInfo(tians, 4);

            data.initChampionInfo(dis, tians);
            data.setChampionFansData(new ChampionFansData());
        }

    }

    /**
     * 发送晋级消息
     * @param teams
     * @param type 1海选赛 2小组赛 3冠军赛地榜 4冠军赛天榜
     */
    private void sendPromotionInfo(List<CoupleTeam> teams, int type) {
        Map<ChannelHandlerContext, List<Player>> players = new HashMap<>();
        //发送晋级消息
        for(CoupleTeam team : teams){
            ChannelHandlerContext session = Manager.gameServerManager.GetSession(team.getPlatName(), team.getServerId());
            if(session == null){
                continue;
            }
            List<Player> ps = players.get(session);
            if(ps == null){
                ps = new ArrayList<>();
                players.put(session, ps);
            }
            ps.add(team.getMen());
            ps.add(team.getWomen());
        }
        //发送晋级消息
        for(Map.Entry<ChannelHandlerContext, List<Player>> entry : players.entrySet()){
            ChannelHandlerContext session = entry.getKey();
            List<Player> ps = entry.getValue();
            CouplefightMessage.P2GPromotion.Builder res = CouplefightMessage.P2GPromotion.newBuilder();
            res.setType(type);
            for(Player p : ps){
                res.addId(p.getId());
            }
            MessageUtils.send_to_game(session, CouplefightMessage.P2GPromotion.MsgID.eMsgID_VALUE, res.build().toByteArray());
        }
    }


    /**
     * 小组赛结束
     */
    private void onGroupsOver(Group group) {
        if(group.isOver()){
            log.warn("仙侣对决小组已结束 不能重复结算");
            return;
        }

        // 踢出准备地图的玩家
        FightRoom preRoom = group.getPreRoom();
        if(preRoom != null){
            preRoom.close();
        }

        //设置结束
        group.setOver(true);
        group.setPreRoom(null);

        List<CoupleTeam> ts = group.getTeams();
        //发送奖励
        int index = 0;
        //按服务器分别发送排名奖励
        Map<ChannelHandlerContext,List<CouplefightMessage.RankAward>> contexts = new HashMap<>();
        for(CoupleTeam t : ts){
            index++;
            CouplefightMessage.RankAward.Builder rankAward = CouplefightMessage.RankAward.newBuilder();
            rankAward.setRank(index);
            rankAward.addRid(t.getMen().getId());
            rankAward.addRid(t.getWomen().getId());
            ChannelHandlerContext session = Manager.gameServerManager.GetSession(t.getServerPlatId());
            List<CouplefightMessage.RankAward> rs = contexts.get(session);
            if(rs == null){
                rs = new ArrayList<>();
                contexts.put(session, rs);
            }
            rs.add(rankAward.build());
        }

        //发送排行奖励消息
        CouplefightMessage.P2GResRankAward.Builder res = CouplefightMessage.P2GResRankAward.newBuilder();
        res.setType(CouplefightManager.award_group_rank);
        for(Map.Entry<ChannelHandlerContext,List<CouplefightMessage.RankAward>> entrys : contexts.entrySet()){
            ChannelHandlerContext context = entrys.getKey();
            List<CouplefightMessage.RankAward> rs = entrys.getValue();
            res.clearAwards();
            res.addAllAwards(rs);
            MessageUtils.send_to_game(context, CouplefightMessage.P2GResRankAward.MsgID.eMsgID_VALUE, res.build().toByteArray());
        }
    }

    /**
     * 小组赛战斗
     */
    private void onGroupsFight() {
        log.info("仙侣对决 小组赛战斗开始");
        //设置状态
        Manager.couplefightManager.setStatus(CouplefightManager.status_group);
//        CouplefightGroupsTimer timer = Manager.couplefightManager.getGroupsTimer();
//        if(timer == null){
//            timer = new CouplefightGroupsTimer();
//        }
//        //添加轮次定时器
//        MainServer.getInstance().addTimerEvent(timer);
    }

    /**
     * 开始下一轮
     */
    @Override
    public void onGroupsNextRound() {
        log.info("仙侣对决 小组赛新轮次开始");
        if(Manager.couplefightManager.getStatus() != CouplefightManager.status_group){
            log.warn("仙侣对决 不在小组赛比赛阶段");
            return;
        }
        for(CoupleData data : Manager.couplefightManager.getDatas().values()){
            int round = data.getGroupsRound();
            if(round >= 9){
                log.warn("仙侣对决 小组赛轮次已全部完成");
                return;
            }
            onGroupsRoundStart(data);
        }
    }

    /**
     * 小组赛轮次开始
     * @param data
     */
    private void onGroupsRoundStart(CoupleData data) {
        int round = data.getGroupsRound();
        data.setGroupsRound(round + 1);
        log.info("仙侣对决 小组赛战斗，跨服组：{}，第{}轮 start", data.getServerGroupId(), data.getGroupsRound());
        round = data.getGroupsRound();
        for(Group group : data.getGroups()){
            if(group == null){
                continue;
            }
            List<GroupRound> rounds = group.getRounds();
            if(rounds != null && rounds.size() >= round){
                GroupRound rs = rounds.get(round - 1);
                for(CoupleFightRoom r : rs.getRooms()){
                    startGroupsFight(data, group, r);
                }
            }else{
                log.warn("仙侣对决 小组赛 没有对应的轮次数据 round:{} group:{}", round, group.getTeams().size());
            }
        }
    }

    private void startGroupsFight(CoupleData data, Group group, CoupleFightRoom r) {
        //准备房间
        FightRoom preRoom = group.getPreRoom();
        //创建房间并开始
        CoupleTeam t1 = data.getTeams().get(r.getT1());
        List<Long> r1 = new ArrayList<>(2);
        if(t1 != null && preRoom != null){
            if(preRoom.hasRoleId(t1.getMen().getId())){
                r1.add(t1.getMen().getId());
            }
            if(preRoom.hasRoleId(t1.getWomen().getId())){
                r1.add(t1.getWomen().getId());
            }
        }
        CoupleTeam t2 = data.getTeams().get(r.getT2());
        List<Long> r2 = new ArrayList<>(2);
        if(t2 != null && preRoom != null){
            if(preRoom.hasRoleId(t2.getMen().getId())){
                r2.add(t2.getMen().getId());
            }
            if(preRoom.hasRoleId(t2.getWomen().getId())){
                r2.add(t2.getWomen().getId());
            }
        }
        if(r1.size() == 0 && r2.size() == 0){
            //都判断负
            onGroupsFightOver(data, group, r, new TeamFightInfo(t1, false, r1),
                    new TeamFightInfo(t2, false, r2));
            return;
        }
        if(r1.size() == 0 || r2.size() == 0){
            //一胜一负
            if(r1.size() == 0){

                ChannelHandlerContext session = Manager.gameServerManager.GetSession(t2.getPlatName(), t2.getServerId());
                if (session != null) {
                    for(Long roleId : r2){
                        MessageUtils.notify_player(session, roleId, MessageString.COUPLEFIGHT_WIN);
                    }
                }
                onGroupsFightOver(data, group, r, new TeamFightInfo(t2, true, r2),
                        new TeamFightInfo(t1, false, r1));

            }else{
                ChannelHandlerContext session = Manager.gameServerManager.GetSession(t1.getPlatName(), t1.getServerId());
                if (session != null) {
                    for(Long roleId : r1){
                        MessageUtils.notify_player(session, roleId, MessageString.COUPLEFIGHT_WIN);
                    }
                }
                onGroupsFightOver(data, group, r, new TeamFightInfo(t1, true, r1),
                        new TeamFightInfo(t2, false, r2));
            }
            return;
        }

        //创建战斗房间
        FightRoom fightRoom = createFightRoom(t1, t2, r1, r2);
        r.setRoomId(fightRoom.getFid());
        Manager.couplefightManager.getRooms().put(fightRoom.getFid(), r);

        fightRoom.setServerId(preRoom.getServerId());
        fightRoom.setpPlat(preRoom.getPlat());
        fightRoom.setRstate(FightRoomState.CREATEROOM);
        //告知玩家进入战斗房间
        CouplefightMessage.P2FReqGoToFight.Builder req = CouplefightMessage.P2FReqGoToFight.newBuilder();
        req.setFid(fightRoom.getFid());
        req.setPrefid(preRoom.getFid());
        req.setCloneId(fightRoom.getModelId());
        req.setType(2);
        req.setRound(data.getGroupsRound());

        for(Long p : r1){
            req.addR1(p);
        }
        for(Long p : r2){
            req.addR2(p);
        }
        ChannelHandlerContext context = GameServerManager.getInstance().GetSession(preRoom.getPlat(), preRoom.getServerId());
        MessageUtils.send_to_game(context, CouplefightMessage.P2FReqGoToFight.MsgID.eMsgID_VALUE, req.build().toByteArray());
    }


    private void startChampionRoomFight(CoupleData data, ChampionData championData, ChampionRoom room) {
        FightRoom preRoom = championData.getPreRoom();
        //创建房间并开始
        CoupleTeam t1 = data.getTeams().get(room.getT1());
        CoupleTeam t2 = data.getTeams().get(room.getT2());
        //轮空处理
        //获取已准备玩家
        List<Long> r1 = new ArrayList<>(2);
        if(t1 != null && preRoom != null){
            if(preRoom.hasRoleId(t1.getMen().getId())){
                r1.add(t1.getMen().getId());
            }
            if(preRoom.hasRoleId(t1.getWomen().getId())){
                r1.add(t1.getWomen().getId());
            }
        }
        List<Long> r2 = new ArrayList<>(2);
        if(t2 != null && preRoom != null){
            if(preRoom.hasRoleId(t2.getMen().getId())){
                r2.add(t2.getMen().getId());
            }
            if(preRoom.hasRoleId(t2.getWomen().getId())){
                r2.add(t2.getWomen().getId());
            }
        }
        //输赢判断 都没有准备
        if(r1.size() == 0 && r2.size() == 0){
            //都判断负
            onChampionFightOver(data, room, new TeamFightInfo(t1, false, r1), new TeamFightInfo(t2, false, r2));
            return;
        }
        if(r1.size() == 0 || r2.size() == 0){
            //一胜一负
            if(r1.size() == 0){
                ChannelHandlerContext session = Manager.gameServerManager.GetSession(t2.getPlatName(), t2.getServerId());
                if (session != null) {
                    for(Long roleId : r2){
                        MessageUtils.notify_player(session, roleId, MessageString.COUPLEFIGHT_WIN);
                    }
                }
                onChampionFightOver(data, room, new TeamFightInfo(t1, false, r1), new TeamFightInfo(t2, true, r2));
            }else{
                ChannelHandlerContext session = Manager.gameServerManager.GetSession(t1.getPlatName(), t1.getServerId());
                if (session != null) {
                    for(Long roleId : r1){
                        MessageUtils.notify_player(session, roleId, MessageString.COUPLEFIGHT_WIN);
                    }
                }
                onChampionFightOver(data, room, new TeamFightInfo(t2, false, r2), new TeamFightInfo(t1, true, r1));
            }
            return;
        }

        //创建战斗房间
        FightRoom fightRoom = createFightRoom(t1, t2, r1, r2);
        room.setRoomId(fightRoom.getFid());
        Manager.couplefightManager.getRooms().put(fightRoom.getFid(), room);

        fightRoom.setServerId(preRoom.getServerId());
        fightRoom.setpPlat(preRoom.getPlat());
        fightRoom.setRstate(FightRoomState.CREATEROOM);
        //告知玩家进入战斗房间
        CouplefightMessage.P2FReqGoToFight.Builder req = CouplefightMessage.P2FReqGoToFight.newBuilder();
        req.setFid(fightRoom.getFid());
        req.setPrefid(preRoom.getFid());
        req.setCloneId(fightRoom.getModelId());
        req.setType(3);
        req.setRound(data.getGroupsRound());
        for(Long p : r1){
            req.addR1(p);
        }
        for(Long p : r2){
            req.addR2(p);
        }
        ChannelHandlerContext context = GameServerManager.getInstance().GetSession(preRoom.getPlat(), preRoom.getServerId());
        MessageUtils.send_to_game(context, CouplefightMessage.P2FReqGoToFight.MsgID.eMsgID_VALUE, req.build().toByteArray());
    }

    private List<Long> getPlayerIdList(List<Player> r1) {
        List<Long> ids = new ArrayList<>(r1.size());
        for(Player p : r1){
            ids.add(p.getId());
        }
        return ids;
    }

    /**
     * 创建战斗房间
     * @param id1 队伍1玩家
     * @param id2 队伍2玩家
     * @return
     */
    private FightRoom createFightRoom(CoupleTeam t1, CoupleTeam t2, List<Long> id1, List<Long> id2) {
        Cfg_Daily_Bean daily = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.COUPLE_FIGHT);
        Cfg_Clone_map_Bean clone = CfgManager.getCfg_Clone_map_Container().getValueByKey(daily.getCloneID().get(1));

        List<ZoneTeam> teams = new ArrayList<>();
        if(t1 != null){
            ZoneTeam redTeam = new ZoneTeam();
            redTeam.setPlat(t1.getPlatName());
            redTeam.setsId(t1.getServerId());
            long power = 0;
            for(Long id : id1){
                Player r = t1.getPlayerById(id);
                power += r.getPower();
                TeamPlayerInfo teamPlayerInfo = createTeamPlayerInfo(r);
                redTeam.getPlist().put(r.getId(), teamPlayerInfo);
            }
            redTeam.setPow(power);
            teams.add(redTeam);
        }

        if(t2 != null){
            ZoneTeam blueTeam = new ZoneTeam();
            blueTeam.setPlat(t2.getPlatName());
            blueTeam.setsId(t2.getServerId());
            long power = 0;
            for(Long id : id2){
                Player r = t2.getPlayerById(id);
                power += r.getPower();
                TeamPlayerInfo teamPlayerInfo = createTeamPlayerInfo(r);
                blueTeam.getPlist().put(teamPlayerInfo.getRoleId(), teamPlayerInfo);
            }
            blueTeam.setPow(power);
            teams.add(blueTeam);
        }

        FightRoom room = Manager.fightManager.deal().createFightRoom(clone, teams);
        return room;
    }

    /**
     * 小组赛准备
     */
    private void onGroupPre() {
        log.info("仙侣对决 预选赛结束");
        CouplefightManager mamager = Manager.couplefightManager;
        mamager.setStatus(CouplefightManager.status_group_pre);
        //预算赛结束处理--------------
        //晋级信息
        Map<Integer, List<CoupleTeam>> updata = new HashMap<>();
        //发送奖励
        for(Map.Entry<Integer, CoupleData> entry : mamager.getDatas().entrySet()){
            CoupleData data = entry.getValue();
            Integer group = entry.getKey();
            int rank = 0;
            //可晋级的队伍
            List<CoupleTeam> ups = updata.get(group);
            if(ups == null){
                ups = new ArrayList<>(80);
                updata.put(group, ups);
            }
            for(RankInfo rankInfo : data.getRanks()){
                rank++;
                if(rank <= 80){
                    CoupleTeam team = data.getTeams().get(rankInfo.getId());
                    log.info("仙侣对决 海选赛奖励 group:{} rank:{} team:{}", group, rank, team);
                    if(team != null){
                        ups.add(team);
                        team.setGroupsInfo(new TeamGroupsInfo());
                    }
                }
            }
        }
        //晋级
        log.info("仙侣对决 小组赛准备");

        Map<ChannelHandlerContext, List<Player>> players = new HashMap<>();
        for(Map.Entry<Integer, List<CoupleTeam>> entry : updata.entrySet()){
            int group = entry.getKey();
            List<CoupleTeam> teams = entry.getValue();
            CoupleData data = mamager.getDatasByGroupId(group);
            data.initGroupInfo(teams);
            //发送晋级消息
            sendPromotionInfo(teams, 2);
        }

        //海选赛次数奖励
        List<Cfg_Marry_battle_reward_Bean> beans = new ArrayList<>();
        for(Cfg_Marry_battle_reward_Bean bean : Cfg_Marry_battle_reward_Container.GetInstance().getValuees()){
            if(bean.getType() == 4){
                beans.add(bean);
            }
        }

        //发送海选赛未领取的奖励
        for(Map.Entry<ChannelHandlerContext, List<Player>> entry : players.entrySet()){
            ChannelHandlerContext session = entry.getKey();
            List<Player> ps = entry.getValue();
            CouplefightMessage.P2GTrialsAward.Builder resAward = CouplefightMessage.P2GTrialsAward.newBuilder();
            for(Player p : ps){
                CoupleTeam team = p.getTeam();
                if(team == null){
                    log.warn("仙侣对决海选赛结束发送奖励错误，玩家"+p.getId()+"队伍为空");
                    continue;
                }

                CouplefightMessage.TrialsAward.Builder trialsAward = CouplefightMessage.TrialsAward.newBuilder();
                trialsAward.setId(p.getId());
                for(Cfg_Marry_battle_reward_Bean bean : beans){
                    if(team.getTrialsInfo().getCount() >= bean.getParm().get(0) && !p.getTrialsAward().contains(bean.getId())){
                        p.getTrialsAward().add(bean.getId());
                        trialsAward.addAwardId(bean.getId());
                    }
                }

                if(trialsAward.getAwardIdCount() > 0){
                    resAward.addAward(trialsAward);
                }
            }
            MessageUtils.send_to_game(session, CouplefightMessage.P2GTrialsAward.MsgID.eMsgID_VALUE, resAward.build().toByteArray());
        }

        log.info("仙侣对决 小组赛准备完毕");
    }

    /**
     * 预选赛开始
     */
    private void onSelect() {
        log.info("仙侣对决 预选赛开始");
        Manager.couplefightManager.setStatus(CouplefightManager.status_select);
    }

    /**
     * 预选赛准备
     */
    private void onSelectPre() {
        log.info("仙侣对决 预选赛准备");
        Manager.couplefightManager.setStatus(CouplefightManager.status_select_pre);
        //初始化排行榜
        for(CoupleData data : Manager.couplefightManager.getDatas().values()){
            for(CoupleTeam team : data.getTeams().values()){
                team.setRoom(null);
            }
        }

    }

    /**
     * 获取当前配置
     * @return
     */
    private Cfg_Marry_battle_time_Bean getCurrentConfig() {
        int weekDay = TimeUtils.getDayOfWeek(TimeUtils.Time());
        int hour = TimeUtils.getDayOfHour(TimeUtils.Time());
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(TimeUtils.Time());
        instance.get(Calendar.HOUR_OF_DAY);
        int minute = instance.get(Calendar.MINUTE);
        int dayMinute = hour * 60 + minute;

        Cfg_Marry_battle_time_Bean beforeBean = null;
        Cfg_Marry_battle_time_Bean currentBean = null;
        for(Cfg_Marry_battle_time_Bean bean : Cfg_Marry_battle_time_Container.GetInstance().getValuees()){
            int startDay = bean.getStart_time().get(0);
            int startMinute = bean.getStart_time().get(1);
            int endMinute = bean.getOver_time().get(1);
            if(weekDay < startDay){
                break;
            }else if(weekDay == startDay){
                if(dayMinute < startMinute){
                    break;
                }else{
                    if(dayMinute < endMinute){
                        if(currentBean != null){
                            beforeBean = currentBean;
                        }
                        currentBean = bean;
                        continue;
                    }
                }
            }
            beforeBean = bean;
            currentBean = null;
        }
        Manager.couplefightManager.setCurrentBean(currentBean);
        Manager.couplefightManager.setBeforeBean(beforeBean);
        return currentBean;
    }

    /**
     * 获得当前状态
     * @return
     */
    private int getCurrentStatus() {
        int status = 0;
        Cfg_Marry_battle_time_Bean beforeBean = Manager.couplefightManager.getBeforeBean();
        Cfg_Marry_battle_time_Bean currentBean = Manager.couplefightManager.getCurrentBean();
        if(currentBean != null){
            if (currentBean.getType() == 0) {
                status = CouplefightManager.status_apply;
            } else if (currentBean.getType() == 1) {
                status = CouplefightManager.status_select;
            } else if (currentBean.getType() == 2 && currentBean.getGame() == -1) {
                status = CouplefightManager.status_group_pre;
            } else if (currentBean.getType() == 2) {
                status = CouplefightManager.status_group;
            } else if (currentBean.getType() == 3 && currentBean.getGame() == -1) {
                status = CouplefightManager.status_di_pre;
            } else if (currentBean.getType() == 3) {
                status = CouplefightManager.status_di;
            } else if (currentBean.getType() == 4 && currentBean.getGame() == -1) {
                status = CouplefightManager.status_tian_pre;
            } else if (currentBean.getType() == 4) {
                status = CouplefightManager.status_tian;
            }
        }else if(beforeBean != null){
            if(beforeBean.getType() == 0 && beforeBean.getGame() == 1){
                status = CouplefightManager.status_select_pre;
            }else if(beforeBean.getType() == 1 && beforeBean.getGame() == 0){
                status = CouplefightManager.status_group_pre;
            }else if(beforeBean.getType() == 2 && beforeBean.getGame() == 9){
                status = CouplefightManager.status_champion_pre;
            }else if(beforeBean.getType() == 3 && beforeBean.getGame() == 4){
                status = CouplefightManager.status_tian_pre;
            }else if(beforeBean.getType() == 4 && beforeBean.getGame() == 4){
                status = CouplefightManager.status_over;
            }
        }else{
            return CouplefightManager.status_close;
        }
        return status == 0 ? Manager.couplefightManager.getStatus() : status;
    }

    /**
     * 开启报名
     */
    private void onOpenApply() {
        log.info("仙侣对决 开启报名");
        //初始化数据
        clear();
        clearDB();
        //本次活动id
        int activityId = getActivityId();

        //设置所有服务器分组
        List<ServerInfo> serverInfoList = GameServerManager.getInstance().getAllGameServer();
        for(ServerInfo s : serverInfoList){
            String severplat = s.getPlatName()+ "_" +s.getServerId();
            initServerInfo(severplat, activityId);
        }
        //设置状态
        Manager.couplefightManager.setStatus(CouplefightManager.status_apply);
    }

    private void clearDB() {
        Manager.couplefightManager.getDao().clear();
    }

    /**
     * 初始服务器信息
     * @param severplat
     * @param activityId
     */
    private void initServerInfo(String severplat, int activityId) {
        Map<String, Integer> servers = Manager.couplefightManager.getServers();
        Map<Integer, CoupleData> datas = Manager.couplefightManager.getDatas();
        int groupID = ServerMatchManager.deal().getGroupIDForCurOpenDay(severplat, DailyActiveDefine.COUPLE_FIGHT);
        if(groupID == -1){
            return;
        }
        CoupleData data = datas.get(groupID);
        if(data == null){
            data = new CoupleData();
            data.setActivityId(activityId);
            data.setServerGroupId(groupID);
            data.setCreateDate(new Date());
            datas.put(groupID, data);
        }
        List<String> ids = data.getServerIds();
        if(ids == null){
            ids = new ArrayList<>();
            data.setServerIds(ids);
        }
        ids.add(severplat);
        servers.put(severplat, groupID);
    }

    /**
     * 获取当前活动id
     * @return
     */
    public int getActivityId() {
        long time = TimeUtils.Time();
        int year = TimeUtils.getYear(time);
        int week = TimeUtils.getWeekOfYear(time);
        return year * 100 + week;
    }

    @Override
    public void updatePlayerInfo(ChannelHandlerContext context, CouplefightMessage.G2PSendPlayerInfo messInfo) {
        String serverPlatId = context.channel().attr(SessionKey.SERVERPLATID).get();
        //获取玩家所在分组
        Integer group = Manager.couplefightManager.getGroup(serverPlatId);
        CouplefightMessage.PlayerInfo playerInfo = messInfo.getPlayer();
        CoupleData data = Manager.couplefightManager.getDatasByGroupId(group);
        CoupleTeam team = data.getPlayers().get(playerInfo.getId());
        if(team == null){
            log.info("玩家" + playerInfo.getId() + "没有参加活动");
            return;
        }
        if(team.getWomen().getId() == playerInfo.getId()){
            team.getWomen().update(playerInfo);
        }else if(team.getMen().getId() == playerInfo.getId()){
            team.getMen().update(playerInfo);
        }
    }

    @Override
    public void match() {
        if(Manager.couplefightManager.getStatus() != CouplefightManager.status_select){
            return;
        }
        for(CoupleData data : Manager.couplefightManager.getDatas().values()){
            LinkedList<CoupleTeam> ts = data.getMatchs();
            int size = ts.size();
            if(size == 0){
                continue;
            }else{
                CoupleTeam t = ts.get(0);
                if(size == 1){
                    if(TimeUtils.Time() - t.getTrialsInfo().getTime() > 10000){
                        ts.remove(t);
                        matchSuccess(data, t, null);
                    }
                }else{
                    for(int i = 1; i < size; i++){
                        CoupleTeam o = ts.get(i);
                        if(t.getTrialsInfo().getLastMatchTeam() == o.getTrialsInfo().getLastMatchTeam() && t.getTrialsInfo().getLastMatchTeam() != 0){
                            continue;
                        }else{
                            ts.remove(t);
                            ts.remove(o);
                            matchSuccess(data, t, o);
                        }
                    }
                }
            }
        }
    }

    private CoupleTeam getRobotTeam(CoupleTeam t) {
        CoupleTeam team = new CoupleTeam();
        team.setRobot(true);
        Player men = new Player();
        men.setTeam(team);
        men.setId(t.getMen().getId());
        men.setName(t.getMen().getName());
        team.setMen(men);

        Player women = new Player();
        women.setTeam(team);
        women.setId(t.getWomen().getId());
        women.setName(t.getWomen().getName());
        team.setWomen(women);
        team.setPlatName(t.getPlatName());
        team.setServerId(t.getServerId());
        return team;
    }

    /**
     * 匹配成功的处理
     * @param t
     * @param o
     */
    private void matchSuccess(CoupleData data, CoupleTeam t, CoupleTeam o) {
        log.info("仙侣对决 海选赛 匹配成功 team:" + t);
        log.info("仙侣对决 海选赛 匹配成功 team:" + o);
        if(t != null && o != null){
            t.getTrialsInfo().setLastMatchTeam(o.getId());
            o.getTrialsInfo().setLastMatchTeam(t.getId());
        }
        CoupleFightRoom room = new CoupleFightRoom(t, o);
        if(t != null){
            t.setRoom(room);
        }
        if(o != null){
            o.setRoom(room);
        }
        //通知玩家匹配成功
        CouplefightMessage.ResMatchSuccess.Builder res = CouplefightMessage.ResMatchSuccess.newBuilder();
        byte[] sendData = res.build().toByteArray();

        if(t != null && !t.isRobot()){
            MessageUtils.send_to_player(t.getPlatName(), t.getServerId(), t.getMen().getId(), CouplefightMessage.ResMatchSuccess.MsgID.eMsgID_VALUE, sendData);
            MessageUtils.send_to_player(t.getPlatName(), t.getServerId(), t.getWomen().getId(), CouplefightMessage.ResMatchSuccess.MsgID.eMsgID_VALUE, sendData);
        }
        if(o != null && !o.isRobot()){
            MessageUtils.send_to_player(o.getPlatName(), o.getServerId(), o.getMen().getId(), CouplefightMessage.ResMatchSuccess.MsgID.eMsgID_VALUE, sendData);
            MessageUtils.send_to_player(o.getPlatName(), o.getServerId(), o.getWomen().getId(), CouplefightMessage.ResMatchSuccess.MsgID.eMsgID_VALUE, sendData);
        }

        //添加定时器处理玩家确认
        Manager.couplefightManager.executor.schedule(()->{
            Manager.couplefightManager.addCommand(()->{
                Manager.couplefightManager.getScript().matchConfirmOver(data, room, t, o);
            });
        }, 16, TimeUnit.SECONDS);
    }

    private CoupleFightRoom createRoom(CoupleTeam t, CoupleTeam o) {
        Cfg_Daily_Bean daily = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.COUPLE_FIGHT);
        Cfg_Clone_map_Bean clone = CfgManager.getCfg_Clone_map_Container().getValueByKey(daily.getCloneID().get(1));

        List<ZoneTeam> teams = new ArrayList<>();
        if(t != null){
            TeamPlayerInfo tm = createTeamPlayerInfo(t.getMen());
            TeamPlayerInfo tw = createTeamPlayerInfo(t.getWomen());

            ZoneTeam redTeam = new ZoneTeam();
            redTeam.setPlat(t.getPlatName());
            redTeam.setsId(t.getServerId());
            redTeam.setPow(t.getMen().getPower() + t.getWomen().getPower());
            redTeam.getPlist().put(tm.getRoleId(), tm);
            redTeam.getPlist().put(tw.getRoleId(), tw);
            teams.add(redTeam);
        }

        if(o != null){
            TeamPlayerInfo om = createTeamPlayerInfo(o.getMen());
            TeamPlayerInfo ow = createTeamPlayerInfo(o.getWomen());

            ZoneTeam blueTeam = new ZoneTeam();
            blueTeam.setPlat(o.getPlatName());
            blueTeam.setsId(o.getServerId());
            blueTeam.setPow(o.getWomen().getPower() + o.getMen().getPower());
            blueTeam.getPlist().put(om.getRoleId(), om);
            blueTeam.getPlist().put(ow.getRoleId(), ow);
            teams.add(blueTeam);
        }

        FightRoom room = Manager.fightManager.deal().createFightRoom(clone, teams);
        CoupleFightRoom r = new CoupleFightRoom(room, t, o);
        Manager.couplefightManager.getRooms().put(room.getFid(), r);
        return r;
    }

    private FightRoom createFightRoom(CoupleTeam t, CoupleTeam o) {
        Cfg_Daily_Bean daily = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.COUPLE_FIGHT);
        Cfg_Clone_map_Bean clone = CfgManager.getCfg_Clone_map_Container().getValueByKey(daily.getCloneID().get(1));

        List<ZoneTeam> teams = new ArrayList<>();
        if(t != null){
            TeamPlayerInfo tm = createTeamPlayerInfo(t.getMen());
            TeamPlayerInfo tw = createTeamPlayerInfo(t.getWomen());

            ZoneTeam redTeam = new ZoneTeam();
            redTeam.setPlat(t.getPlatName());
            redTeam.setsId(t.getServerId());
            redTeam.setPow(t.getMen().getPower() + t.getWomen().getPower());
            redTeam.getPlist().put(tm.getRoleId(), tm);
            redTeam.getPlist().put(tw.getRoleId(), tw);
            teams.add(redTeam);
        }

        if(o != null){
            TeamPlayerInfo om = createTeamPlayerInfo(o.getMen());
            TeamPlayerInfo ow = createTeamPlayerInfo(o.getWomen());

            ZoneTeam blueTeam = new ZoneTeam();
            blueTeam.setPlat(o.getPlatName());
            blueTeam.setsId(o.getServerId());
            blueTeam.setPow(o.getWomen().getPower() + o.getMen().getPower());
            blueTeam.getPlist().put(om.getRoleId(), om);
            blueTeam.getPlist().put(ow.getRoleId(), ow);
            teams.add(blueTeam);
        }

        FightRoom room = Manager.fightManager.deal().createFightRoom(clone, teams);
        return room;
    }

    /**
     *
     * @return
     */
    private TeamPlayerInfo createTeamPlayerInfo(Player player) {
        CoupleTeam t = player.getTeam();
        TeamPlayerInfo p = new TeamPlayerInfo();
        p.setServerId(t.getServerId());
        p.setRobot(t.isRobot());
        p.setRoleId(player.getId());
        p.setName(player.getName());
        if(t.isRobot()){
            p.setReady(true);
        }else{
            p.setReady(false);
        }
        return p;
    }

    @Override
    public void OnTick(long now) {

    }

    @Override
    public void start() {
        log.info("开始仙侣对决");
        MainServer.getInstance().getwServerThread().addTimerEvent(Manager.couplefightManager.getTimer());
    }

    @Override
    public void close() {
        log.info("仙侣对决 关闭");
        statusChange(CouplefightManager.status_close);
        MainServer.getInstance().getwServerThread().removeTimerEvent(Manager.couplefightManager.getTimer());
    }

    private void onClose() {
        Manager.couplefightManager.setStatus(CouplefightManager.status_close);
        //同步到游戏服
        for(String serverPlatId : CouplefightManager.getInstance().getServers().keySet()){
            ServerInfo server = Manager.gameServerManager.getServerCache().get(serverPlatId);
            if(server != null){
                CouplefightMessage.P2GChangeStatus.Builder res = CouplefightMessage.P2GChangeStatus.newBuilder();
                res.setStatus(CouplefightManager.status_close);
                MessageUtils.send_to_game(server.getSession(), CouplefightMessage.P2GChangeStatus.MsgID.eMsgID_VALUE, res.build().toByteArray());
            }
        }
        saveDB();
        //清除数据
        clear();
    }

    @Override
    public void matchConfirm(ChannelHandlerContext context, long uid, boolean confirm) {
        if(Manager.couplefightManager.getStatus() != CouplefightManager.status_select){
            return;
        }
        String serverPlatId = context.channel().attr(SessionKey.SERVERPLATID).get();
        CouplefightManager manager = Manager.couplefightManager;
        int groupId = manager.getGroup(serverPlatId);
        CoupleData data = manager.getDatasByGroupId(groupId);

        CoupleTeam team = data.getPlayers().get(uid);
        if(team == null){
            log.warn("uid:{} 队伍不存在", uid);
            return;
        }

        if(manager.getStatus() != CouplefightManager.status_select){
            log.warn("不在海选赛阶段rid：{}", uid);
            return;
        }
        CoupleFightRoom r = team.getRoom();
        if(r == null){
            log.warn("仙侣对决 海选赛 还没有匹配成功 team:" + team);
            return;
        }

        //改变状态
        if(team.getMen().getId() == uid){
            team.getMen().setConfirm(confirm);
        }else{
            team.getWomen().setConfirm(confirm);
        }

        //通知玩家的选择
        CouplefightMessage.ResMatchConfirmNotice.Builder notice = CouplefightMessage.ResMatchConfirmNotice.newBuilder();
        notice.setConfirm(confirm);
        notice.setUid(uid);
        MessageUtils.send_to_player(context, team.getMen().getId(), CouplefightMessage.ResMatchConfirmNotice.MsgID.eMsgID_VALUE, notice.build().toByteArray());
        MessageUtils.send_to_player(context, team.getWomen().getId(), CouplefightMessage.ResMatchConfirmNotice.MsgID.eMsgID_VALUE, notice.build().toByteArray());

        CoupleTeam team2 = null;
        if(r.getT1() == team.getId()){
            team2 = data.getTeams().get(r.getT2());
        }else{
            team2 = data.getTeams().get(r.getT1());
        }

        //是否全部选择
        if(team.getMen().getConfirm() == null || team.getWomen().getConfirm() == null){
            return;
        }
        if(team2 != null && (team2.getMen().getConfirm() == null || team2.getWomen().getConfirm() == null)){
            return;
        }

        matchConfirmOver(data, r, team, team2);
    }

    public void matchConfirmOver(CoupleData data, CoupleFightRoom r, CoupleTeam team, CoupleTeam team2){
        synchronized (r){
            if(r.isOver()){
                return;
            }
            r.setOver(true);
        }
        boolean t1 = true;
        boolean t2 = true;

        Boolean ment1 = team.getMen().getConfirm();
        Boolean woment1 = team.getWomen().getConfirm();
        if((ment1 != null && ment1 == false) || (woment1 != null && woment1 == false)){
            t1 = false;
        }
        if(team2 != null){
            Boolean ment2 = team2.getMen().getConfirm();
            Boolean woment2 = team2.getWomen().getConfirm();
            if((ment2 != null && ment2 == false) || (woment2 != null && woment2 == false)){
                t2 = false;
            }
        }

        log.info("仙侣对决 海选赛 匹配确认完成 team:" + team + t1);
        log.info("仙侣对决 海选赛 匹配确认完成 team2:" + team2 + t2);
        if(t1 == false || t2 == false){
            matchOver(data, new TeamFightInfo(team, t1), new TeamFightInfo(team2, t2));
        }

        if(t1 && t2){
            //开始进入副本战斗
            if(team2 == null){
                //匹配机器人
                team2 = getRobotTeam(team);
            }
            FightRoom room = createFightRoom(team, team2);
            r.setRoomId(room.getFid());
            fightStart(r);
        }
    }

    private boolean fightStart(CoupleFightRoom room) {
        Manager.couplefightManager.getRooms().put(room.getRoomId(), room);
        FightRoom fightRoom = Manager.fightManager.GetRoomByFightId(room.getRoomId());
        //开始分配服务器
        ServerInfo serverInfo = Manager.fightManager.deal().getFightServerId(Math.max(fightRoom.hasPeoples(), fightRoom.getHaveNum()));
        if (serverInfo == null) {
            List<ServerInfo> list = Manager.gameServerManager.GetType(ServerType.FIGHTSERVER);
            log.error("没有战斗服连接在线， 请运维检查一下战斗服是否有！ 战斗服个数：" + list.size());
            return false;
        }
        Cfg_Clone_map_Bean clone = CfgManager.getCfg_Clone_map_Container().getValueByKey(fightRoom.getModelId());

        Cfg_Mapsetting_Bean map = CfgManager.getCfg_Mapsetting_Container().getValueByKey(clone.getMapid());

        fightRoom.setServerId(serverInfo.getServerId());
        fightRoom.setpPlat(serverInfo.getPlatName());

        List<CommonMessage.CrossAttribute> params = new ArrayList<>();

        int i = 0;
        Map<ChannelHandlerContext, List<CrossFightMessage.roleAtt.Builder>> players = new HashMap<>();
        for (ZoneTeam team : fightRoom.getTeam()) {
            ChannelHandlerContext session = Manager.gameServerManager.GetSession(team.getPlat(), team.getsId());
            List<CrossFightMessage.roleAtt.Builder> list = players.getOrDefault(session, new ArrayList<>());
            ReadArray<Integer> pos = map.getBornPosition().get(i);
            for(TeamPlayerInfo player : team.getPlist().values()){
                CommonMessage.CrossAttribute.Builder param = CommonMessage.CrossAttribute.newBuilder();
                param.setValue(player.getRoleId());//id
                param.setType(0);//类型 0玩家信息 1其他参数
                param.setParam1(i);//阵营
                param.setParam(player.isRobot() ? "1" : "0");//是否机器人
                params.add(param.build());
                if(!player.isRobot()){
                    CrossFightMessage.roleAtt.Builder role = CrossFightMessage.roleAtt.newBuilder();
                    role.setRoleId(player.getRoleId());
                    role.setX(pos.get(0));
                    role.setY(pos.get(1));
                    role.setCampNo(i);
                    role.setIsRobot(player.isRobot());
                    role.setCarear(player.getCareer());
                    list.add(role);
                    players.put(session, list);
                }
            }
            i++;
        }

        CrossFightMessage.P2GResFightStart.Builder msg = CrossFightMessage.P2GResFightStart.newBuilder();
        msg.setFightId(fightRoom.getFid());
        msg.addAllMapSetList(params);
        msg.setZoneModelId(fightRoom.getModelId());
        msg.setFightServerId(fightRoom.getServerId());
        msg.setMapModelId(fightRoom.getMapmodelId());
        for (Map.Entry<ChannelHandlerContext, List<CrossFightMessage.roleAtt.Builder>> entry : players.entrySet()) {
            ChannelHandlerContext context = entry.getKey();
            List<CrossFightMessage.roleAtt.Builder> roles = entry.getValue();
            msg.clearRoleInfo();
            for(CrossFightMessage.roleAtt.Builder role : roles){
                msg.addRoleInfo(role);
            }
            MessageUtils.send_to_game(context, CrossFightMessage.P2GResFightStart.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
        //进入战斗状态
        fightRoom.setRstate(FightRoomState.FIGHTING);
        log.info("仙侣对决 开始战斗 red={} blue={} ", room.getT1(), room.getT2());
        return true;
    }

    /**
     * 匹配结束发送消息
     * @param info
     */
    private void sendMatchOverMsg(TeamFightInfo info, CoupleData data) {
        if(info == null || info.getTeam() == null){
            return;
        }
        CoupleTeam team = info.getTeam();
        //发信息给玩家
//        CouplefightMessage.ResTrialsInfoUpdate.Builder result = CouplefightMessage.ResTrialsInfoUpdate.newBuilder();
//        result.setTrials(team.toTrialsProto());
//        result.setScore(info.getScore());
//        result.setSuccess(info.isWin());
//        MessageUtils.send_to_player(team.getPlatName(),team.getServerId(), team.getWomen().getId(), CouplefightMessage.ResTrialsInfoUpdate.MsgID.eMsgID_VALUE, result.build().toByteArray());
//        MessageUtils.send_to_player(team.getPlatName(),team.getServerId(), team.getMen().getId(), CouplefightMessage.ResTrialsInfoUpdate.MsgID.eMsgID_VALUE, result.build().toByteArray());

        //发信息给游戏服
        CouplefightMessage.P2GResFightResult.Builder p2Gresult = CouplefightMessage.P2GResFightResult.newBuilder();
        p2Gresult.setType(1);
        p2Gresult.setWin(info.isWin());
        p2Gresult.setScore(info.getScore());
        p2Gresult.addRid(team.getWomen().getId());
        p2Gresult.addRid(team.getMen().getId());
        MessageUtils.send_to_game(team.getPlatName(),team.getServerId(), CouplefightMessage.P2GResFightResult.MsgID.eMsgID_VALUE, p2Gresult.build().toByteArray());

        //推送海选信息
        ChannelHandlerContext context = Manager.gameServerManager.GetSession(team.getPlatName(), team.getServerId());
        sendTrialsInfo(context, team, team.getMen().getId(), data);
        sendTrialsInfo(context, team, team.getWomen().getId(), data);
    }


    /**
     * 更新排行
     * @param data
     * @param t
     */
    private void updateTrialsRank(CoupleData data, CoupleTeam t) {
        if(t == null){
            return;
        }
        List<RankInfo> ranks = data.getRanks();
        RankInfo r = null;
        for(RankInfo rankInfo : ranks){
            if(rankInfo.getId() == t.getId()){
                r = rankInfo;
                break;
            }
        }
        if(r == null){
            //没有就添加
            if(ranks.size() < 100 || ranks.get(ranks.size() - 1).getScore() < t.getTrialsInfo().getScore()){
                ranks.add(new RankInfo(t.getId(), t.getTrialsInfo().getScore()));
            }else{
                return;
            }
        }else{
            r.setScore(t.getTrialsInfo().getScore());
        }
        ranks.sort(TrialsSort.instance);
        if(ranks.size() > 100){
            ranks.remove(100);
        }
        int rank = ranks.indexOf(t) + 1;
        t.getTrialsInfo().setRank(rank);
    }

    @Override
    public void matchStop(ChannelHandlerContext context, long uid) {
        String serverPlatId = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupId = Manager.couplefightManager.getGroup(serverPlatId);
        CoupleData data = Manager.couplefightManager.getDatasByGroupId(groupId);

        CoupleTeam team = data.getPlayers().get(uid);
        if(team == null){
            //队伍为空
            return;
        }

        data.removeMatch(team);

        log.info("仙侣对决 海选赛 停止匹配 team:" + team);

        //告诉玩家
        CouplefightMessage.ResMatchStop.Builder res = CouplefightMessage.ResMatchStop.newBuilder();
        MessageUtils.send_to_player(context, team.getWomen().getId(), CouplefightMessage.ResMatchStop.MsgID.eMsgID_VALUE, res.build().toByteArray());
        MessageUtils.send_to_player(context, team.getMen().getId(), CouplefightMessage.ResMatchStop.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    @Override
    public void fightResult(int type, long fid, List<Long> winList, List<Long> loseList) {
        log.info("仙侣对决 战斗结果 type:{} fid:{} win:{} lose:{}", type, fid, winList, loseList);
        CouplefightManager manager = Manager.couplefightManager;
        manager.addCommand(new ICommand() {
            @Override
            public void action() {
                CoupleFightRoom room = manager.getRooms().remove(fid);
                if(room == null){
                    log.warn("仙侣对决 房间不存在 fid:{}", fid);
                    return;
                }
                room.setOver(true);
                FightRoom fightRoom = Manager.fightManager.GetRoomByFightId(room.getRoomId());
                int groupId = getGroupId(fightRoom);
                CoupleData data = Manager.couplefightManager.getDatasByGroupId(groupId);
                CoupleTeam win = null;
                CoupleTeam lose = null;
                if(winList != null && winList.size() > 0){
                    win = data.getPlayers().get(winList.get(0));
                }
                if(loseList != null && loseList.size() > 0){
                    lose = data.getPlayers().get(loseList.get(0));
                }
                int status = manager.getStatus();
                if(status <= CouplefightManager.status_group_pre){
                    matchOver(data,new TeamFightInfo(win, true), new TeamFightInfo(lose, false));
                }else if(status <= CouplefightManager.status_group){
                    Group group = getGroup(win, lose);
                    onGroupsFightOver(data, group, room, new TeamFightInfo(win, true, winList), new TeamFightInfo(lose, false, loseList));
                }else if(status <= CouplefightManager.status_tian){//冠军赛
                    ChampionRoom championRoom = (ChampionRoom) room;
                    onChampionFightOver(data, championRoom,new TeamFightInfo(win, true, winList), new TeamFightInfo(lose, false, loseList));
                }
            }
        });
    }

    /**
     * 获取玩家所在的小组赛分组
     * @param win
     * @param lose
     * @return
     */
    private Group getGroup(CoupleTeam win, CoupleTeam lose) {
        if(win != null){
            return win.getGroupsInfo().getGroup();
        }
        if(lose != null){
            return lose.getGroupsInfo().getGroup();
        }
        return null;
    }

    /**
     * 获取服务器分组id
     * @param fightRoom
     * @return
     */
    private int getGroupId(FightRoom fightRoom) {
        String key = null;
        for(ZoneTeam team : fightRoom.getTeam()){
            key = GameServerManager.getInstance().makeKey(team.getPlat(), team.getsId());
        }
        return Manager.couplefightManager.getGroup(key);
    }

    /**
     * 冠军赛战斗结束
     * @param data
     * @param t1
     * @param t2
     */
    private void onChampionFightOver(CoupleData data, ChampionRoom room, TeamFightInfo t1, TeamFightInfo t2) {
        room.setOver(true);
        int status = Manager.couplefightManager.getStatus();
        ChampionData championData = null;
        int type = 0;
        if(status == CouplefightManager.status_di){
            championData = data.getChampionDi();
            type = 3;
        }else if(status == CouplefightManager.status_tian){
            championData = data.getChampionTian();
            type = 4;
        }
        ChampionGroup group = championData.getRounds().get(championData.getGround() - 1);
        if(t1 != null && t1.getTeam() != null){
            TeamChampionsInfo teamChampionsInfo = t1.getTeam().getChampionsInfo();
            teamChampionsInfo.setScore(teamChampionsInfo.getScore() + t1.getScore());
            if(t1.isWin()){
                room.setWin(t1.getTeam().getId());
            }
        }
        if(t2 != null && t2.getTeam() != null){
            TeamChampionsInfo teamChampionsInfo = t2.getTeam().getChampionsInfo();
            teamChampionsInfo.setScore(teamChampionsInfo.getScore() + t2.getScore());
            if(t2.isWin()){
                room.setWin(t2.getTeam().getId());
            }
        }

        log.info("仙侣对决 冠军赛战斗，跨服组：{}，第{}轮，战斗id:{},win:{}", data.getServerGroupId(), championData.getGround(), room.getId(), room.getWin());

        //发送胜利失败消息
        sendFightOverMsg(type, t1);
        sendFightOverMsg(type, t2);

        //竞猜奖励发放
        guessSettlement(data, room);
        //判断轮次是否结束
        boolean over = true;
        for(ChampionRoom r : group.getRooms()){
            if(!r.isOver()){
                over = false;
                break;
            }
        }
        if(over){
            log.info("跨服组"+data.getServerGroupId()+",仙侣对决" + (type==3?"地榜":"天榜")+ "赛第"+championData.getGround()+"轮结束");
            onChampionRoundOver(championData, group);
        }

    }

    /**
     * 竞猜结算
     * @param room
     */
    private void guessSettlement(CoupleData data, ChampionRoom room) {
        Long win = room.getWin();
        if(win == null){
            return;
        }
        //竞猜信息 玩家id-队伍id
        Map<Long, Long> guess = room.getGuess();

        int winT1 = room.getWinGoldByRate(room.getRate());
        int winT2 = room.getWinGoldByRate(100 - room.getRate());

        Map<String, List<CouplefightMessage.GuessResult>> guessResults = new HashMap<>();
        for(Map.Entry<Long, Long> entry : guess.entrySet()){
            long rid = entry.getKey();
            Fans fans = data.getChampionFansData().getFans().get(rid);
            long tid = entry.getValue();
            boolean guessResult = false;
            if(win != null && tid == win){//竞猜胜利
                guessResult = true;
            }
            CouplefightMessage.GuessResult.Builder guessMsg = CouplefightMessage.GuessResult.newBuilder();
            guessMsg.setWin(guessResult);
            guessMsg.setRid(rid);
            if(guessResult){
                guessMsg.setItemType(Global.Marry_battle_guess_success_reward.get(0).get(2));
                if(tid == room.getT1()){
                    guessMsg.setItemNum(winT1);
                }else{
                    guessMsg.setItemNum(winT2);
                }
            }else{
                guessMsg.setItemType(Global.Marry_battle_guess_failed_reward.get(0));
                guessMsg.setItemNum(Global.Marry_battle_guess_failed_reward.get(1));
            }

            List<CouplefightMessage.GuessResult> gs = guessResults.get(fans.getServerKey());
            if(gs == null){
                gs = new ArrayList<>();
                guessResults.put(fans.getServerKey(), gs);
            }
            gs.add(guessMsg.build());
        }

        for(Map.Entry<String, List<CouplefightMessage.GuessResult>> entry : guessResults.entrySet()){
            String serverKey = entry.getKey();
            List<CouplefightMessage.GuessResult> gs = entry.getValue();
            ChannelHandlerContext context = Manager.gameServerManager.GetSession(serverKey);
            if(context != null){
                CouplefightMessage.P2GResGuessResult.Builder res = CouplefightMessage.P2GResGuessResult.newBuilder();
                res.addAllGuess(gs);
                MessageUtils.send_to_game(context, CouplefightMessage.P2GResGuessResult.MsgID.eMsgID_VALUE, res.build().toByteArray());
            }else{
                for(CouplefightMessage.GuessResult g : gs){
                    log.warn("仙侣对决竞猜奖励发送失败 serverKey:{} guessInfo:{}", serverKey, g);
                }
            }
        }
    }

    /**
     * 冠军赛轮次结束
     * @param group
     */
    private void onChampionRoundOver(ChampionData championData, ChampionGroup group) {
        //刷新排行
        championData.sort();

        //比赛结束
        if(group.getRound() >= 4){
            onChampionOver(championData);
        }
        //设置下一轮数据
        else{
            List<CoupleTeam> wins = new ArrayList<>();
            for(ChampionRoom r : group.getRooms()){
                CoupleTeam team = null;
                if(r.getWin() != null){
                    team = championData.getCoupleData().getTeams().get(r.getWin());
                }
                wins.add(team);
            }
            championData.getRounds().add(new ChampionGroup(group.getRound() + 1, wins));

        }

    }

    /**
     * 冠军赛结束
     */
    private void onChampionOver(ChampionData championData) {
        if(championData.isOver()){
            return;
        }
        log.info("仙侣对决 冠军赛 {}结束 group:{} 发送奖励", championData.getType() == 1? "天榜":"地榜", championData.getCoupleData().getServerGroupId());
        championData.setOver(true);
        //关闭准备地图
        FightRoom preRoom = championData.getPreRoom();
        if(preRoom != null){
            preRoom.close();
        }

        championData.setPreRoom(null);
        championData.sort();
        List<CoupleTeam> ts = championData.getTeams();
        //发送奖励
        int index = 0;
        //按服务器分别发送排名奖励
        Map<ChannelHandlerContext,List<CouplefightMessage.RankAward>> contexts = new HashMap<>();
        for(CoupleTeam t : ts){
            CouplefightMessage.RankAward.Builder rankAward = CouplefightMessage.RankAward.newBuilder();
            //根据分数确定名次
            TeamChampionsInfo teamChampionsInfo = t.getChampionsInfo();
            if(teamChampionsInfo == null){
                continue;
            }
            int score = teamChampionsInfo.getScore();
            if(score >= 8){
                index = 1;
            }else if(score >= 7){
                index = 2;
            }else if(score >= 5){
                index = 4;
            }else if(score >= 3){
                index = 8;
            }else if(score >= 1){
                index = 16;
            }else{
                continue;
            }
            rankAward.setRank(index);
            rankAward.addRid(t.getMen().getId());
            rankAward.addRid(t.getWomen().getId());
            ChannelHandlerContext session = Manager.gameServerManager.GetSession(t.getPlatName(), t.getServerId());
            if(session == null){
                log.error("仙侣对决 冠军赛奖励发送 没有找到对应的服务器 plat:{} serverId:{}",t.getPlatName(), t.getServerId());
            }
            List<CouplefightMessage.RankAward> rs = contexts.get(session);
            if(rs == null){
                rs = new ArrayList<>();
                contexts.put(session, rs);
            }
            rs.add(rankAward.build());
        }

        //发送排行奖励消息
        CouplefightMessage.P2GResRankAward.Builder res = CouplefightMessage.P2GResRankAward.newBuilder();
        if(championData.getType() == 1){
            res.setType(CouplefightManager.award_champion_tian_rank);
        }else{
            res.setType(CouplefightManager.award_champion_di_rank);
        }

        for(Map.Entry<ChannelHandlerContext,List<CouplefightMessage.RankAward>> entrys : contexts.entrySet()){
            ChannelHandlerContext context = entrys.getKey();
            List<CouplefightMessage.RankAward> rs = entrys.getValue();
            res.clearAwards();
            res.addAllAwards(rs);
            MessageUtils.send_to_game(context, CouplefightMessage.P2GResRankAward.MsgID.eMsgID_VALUE, res.build().toByteArray());
        }
    }

    /**
     * 小组赛战斗结束
     * @param data
     * @param t1
     * @param t2
     */
    private void onGroupsFightOver(CoupleData data, Group group, CoupleFightRoom room, TeamFightInfo t1, TeamFightInfo t2) {
        synchronized (group){
            room.setOver(true);
            groupsFightOverSet(t1);
            groupsFightOverSet(t2);

            group.updateRank();
            //发送积分更新消息
            sendFightOverMsg(2, t1);
            sendFightOverMsg(2, t2);

            //小组赛结束
            int round = data.getGroupsRound();
            GroupRound groupRound = group.getRounds().get(round-1);
            boolean over = true;
            for(CoupleFightRoom r : groupRound.getRooms()){
                if(!r.isOver()){
                    over = false;
                    break;
                }
            }
            if(over){
                log.info("仙侣对决 小组赛战斗，跨服组：{}，组：{}，第{}轮结束", data.getServerGroupId(), group.getId(), round);
                if(round == 9){
                    log.info("仙侣对决小组结束,服务器组："+data.getServerGroupId()+"，玩家组：" + group.getId());
                    onGroupsOver(group);
                }
            }

//            if(data.getGroupsRound() == 9){
//                boolean over = true;
//                for(CoupleTeam t : group.getTeams()){
//                    if(t.getRoom() != null){
//                        over = false;
//                        break;
//                    }
//                }
//                if(over){
//                    log.info("仙侣对决小组结束,服务器组："+data.getServerGroupId()+"，玩家组：" + group.getId());
//                    onGroupsOver(group);
//                }
//            }

        }

    }

    /**
     * 小组赛战斗结束设置
     * @param teamFightInfo
     */
    private void groupsFightOverSet(TeamFightInfo teamFightInfo) {
        if(teamFightInfo != null){
            CoupleTeam team = teamFightInfo.getTeam();
            if(team != null){
                team.setRoom(null);
                team.getGroupsInfo().setScore(team.getGroupsInfo().getScore() + teamFightInfo.getScore());
                team.getGroupsInfo().setCount(team.getGroupsInfo().getCount() + 1);
                if(teamFightInfo.isWin()){
                    team.getGroupsInfo().setWinCount(team.getGroupsInfo().getWinCount() + 1);
                }
            }
        }
    }

    /**
     * 发送小组赛战斗结束信息给玩家
     * @param type
     * @param teamFightInfo
     */
    private void sendFightOverMsg(int type, TeamFightInfo teamFightInfo) {
        if(teamFightInfo == null || teamFightInfo.getTeam() == null){
            return;
        }
        CoupleTeam team = teamFightInfo.getTeam();
        //发信息给玩家
//        CouplefightMessage.ResFightResult.Builder result = CouplefightMessage.ResFightResult.newBuilder();
//        result.setWin(teamFightInfo.isWin());
//        result.setScore(teamFightInfo.getScore());
//        result.setType(type);
        List<Long> roleList = teamFightInfo.getValids();
//        if(roleList != null){
//            for(long id : roleList){
//                MessageUtils.send_to_player(team.getPlatName(),team.getServerId(), id, CouplefightMessage.ResFightResult.MsgID.eMsgID_VALUE, result.build().toByteArray());
//            }
//        }else{
//            MessageUtils.send_to_player(team.getPlatName(),team.getServerId(), team.getWomen().getId(), CouplefightMessage.ResFightResult.MsgID.eMsgID_VALUE, result.build().toByteArray());
//            MessageUtils.send_to_player(team.getPlatName(),team.getServerId(), team.getMen().getId(), CouplefightMessage.ResFightResult.MsgID.eMsgID_VALUE, result.build().toByteArray());
//        }

        //发信息给游戏服
        CouplefightMessage.P2GResFightResult.Builder p2Gresult = CouplefightMessage.P2GResFightResult.newBuilder();
        p2Gresult.setType(type);
        p2Gresult.setWin(teamFightInfo.isWin());
        p2Gresult.setScore(teamFightInfo.getScore());
        if(roleList != null){
            p2Gresult.addAllRid(roleList);
        }else{
            p2Gresult.addRid(team.getWomen().getId());
            p2Gresult.addRid(team.getMen().getId());
        }
        MessageUtils.send_to_game(team.getPlatName(),team.getServerId(), CouplefightMessage.P2GResFightResult.MsgID.eMsgID_VALUE, p2Gresult.build().toByteArray());

    }

    /**
     * 预选赛战斗结束
     * @param data
     * @param t1
     * @param t2
     */
    private void matchOver(CoupleData data, TeamFightInfo t1, TeamFightInfo t2) {
        synchronized (data){
            matchOverSet(t1);
            matchOverSet(t2);
            if(Manager.couplefightManager.getStatus() == CouplefightManager.status_select){
                //只在海选赛阶段更新排名和积分
                if(t1 != null){
                    updateTrialsRank(data, t1.getTeam());
                }
                if(t2 != null){
                    updateTrialsRank(data, t2.getTeam());
                }
            }
            //发送积分更新消息和场次奖励
            sendMatchOverMsg(t1, data);
            sendMatchOverMsg(t2, data);
        }
    }

    /**
     * 匹配结束玩家数据设置
     * @param teamFightInfo
     */
    private void matchOverSet(TeamFightInfo teamFightInfo){
        if(teamFightInfo != null && teamFightInfo.getTeam() != null){
            CoupleTeam team = teamFightInfo.getTeam();
            team.setRoom(null);
            team.getTrialsInfo().setScore(team.getTrialsInfo().getScore() + teamFightInfo.getScore());
            team.getTrialsInfo().setCount(team.getTrialsInfo().getCount() + 1);
            if(teamFightInfo.isWin()){
                team.getTrialsInfo().setWinCount(team.getTrialsInfo().getWinCount() + 1);
            }
        }


    }

    @Override
    public void reqInfo(ChannelHandlerContext context, int type, long rid, List<Long> params) {
        String serverPlatID = context.channel().attr(SessionKey.SERVERPLATID).get();
        //当前活动状态
        int status = Manager.couplefightManager.getStatus();
        //未开启活动的处理
        if(status == CouplefightManager.status_close){
            if(type == 1){
                CouplefightMessage.P2GResTrialsInfo.Builder res = CouplefightMessage.P2GResTrialsInfo.newBuilder();
                res.setIsApply(false);
                res.setRid(rid);
                MessageUtils.send_to_game(context, CouplefightMessage.P2GResTrialsInfo.MsgID.eMsgID_VALUE, res.build().toByteArray());
            }
            return;
        }
        //获取玩家所在分组
        Integer group = getServerGroupId(serverPlatID);
        if(group == null || group == -1){
            return;
        }
        try{
            switch (type){
                case 1://预选赛信息
                    sendTrialsInfo(context, group, rid);
                    break;
                case 2://预选赛排行信息
                    sendTrialsRankInfo(context, group, rid);
                    break;
                case 3://请求获取预选赛场次奖励
                    getTrialsAward(context, group, rid, params.get(0).intValue());
                    break;
                case 4://请求活动状态
                    sendStatus(context);
                    break;
                case 5://请求玩家晋级状态
                    sendPromotionInfo(context, group, rid, status);
                    break;
                case 20://小组赛信息
                    sendGroupInfo(context, group, rid);
                    break;
                case 21://小组赛排名信息
                    sendGroupRankInfo(context, group, rid);
                    break;
                case 22://进入小组赛准备地图
                    reqGroupPrepareMapEnter(context, rid);
                    break;
                case 30://冠军赛信息
                    sendChampionInfo(context, group, rid, params.get(0).intValue());
                    break;
                case 31://冠军赛竞猜界面数据
                    sendChampionGuessInfo(context, group, rid, params.get(0).intValue(), params.get(1).intValue());
                    break;
                case 32://冠军赛更新竞猜
//                    sendChampionGuessUpdate(context, group, rid, params.get(0).intValue(), params.get(1).intValue());
                    sendChampionGuessInfo(context, group, rid, params.get(0).intValue(), params.get(1).intValue());
                    break;
                case 33://支持战队列表
                    sendChampionTeamList(context, group, rid, params.get(0).intValue());
                    break;
                case 34://粉丝排行
                    sendChampionFansRankList(context, group, rid);
                    break;
                case 35://进入地图
                    championEnter(context, group, rid);
                    break;
                case 36://进入地图观战
                    championGuessWatching(context, group, rid, params.get(0).intValue(), params.get(1).intValue());
                    break;
            }
        }catch (Exception e){
            if(e instanceof CouplefightException){
                log.warn("仙侣对决" + e.getMessage() + " type:"+ type+" rid:"+rid+" params:"+params);
            }else{
                log.error("仙侣对决异常 type:"+ type+" rid:"+rid+" params:"+params, e);
            }
        }
    }

    /**
     * 发送当前活动状态给游戏服
     * @param context
     */
    public void sendStatus(ChannelHandlerContext context) {
        CouplefightMessage.P2GChangeStatus.Builder res = CouplefightMessage.P2GChangeStatus.newBuilder();
        res.setStatus(Manager.couplefightManager.getStatus());
        MessageUtils.send_to_game(context, CouplefightMessage.P2GChangeStatus.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    /**
     * 发送晋级信息给游戏服
     * @param context
     * @param group
     * @param rid
     * @param status
     */
    private void sendPromotionInfo(ChannelHandlerContext context, Integer group, long rid, int status) {
        CoupleData data = Manager.couplefightManager.getDatasByGroupId(group);
        CoupleTeam team = data.getPlayers().get(rid);
        if(team == null){
            return;
        }
        if(status == CouplefightManager.status_group_pre || status == CouplefightManager.status_group){
            if(team.getGroupsInfo() != null){
                CouplefightMessage.ResPromotionInfo.Builder res = CouplefightMessage.ResPromotionInfo.newBuilder();
                res.setType(2);
                MessageUtils.send_to_player(context, rid, CouplefightMessage.ResPromotionInfo.MsgID.eMsgID_VALUE, res.build().toByteArray());
            }
        }else if(status >= CouplefightManager.status_champion_pre && status <= CouplefightManager.status_tian){
            if(team.getChampionsInfo() != null){
                CouplefightMessage.ResPromotionInfo.Builder res = CouplefightMessage.ResPromotionInfo.newBuilder();
                if(team.getChampionsInfo().getType() == 2){
                    res.setType(3);
                }else{
                    res.setType(4);
                }
                MessageUtils.send_to_player(context, rid, CouplefightMessage.ResPromotionInfo.MsgID.eMsgID_VALUE, res.build().toByteArray());
            }
        }
    }

    /**
     * 请求进入观战
     * @param context
     * @param group
     * @param rid
     * @param type
     * @param fid
     */
    private void championGuessWatching(ChannelHandlerContext context, Integer group, long rid, int type, int fid) {
        String serverPlat = context.channel().attr(SessionKey.SERVERPLAT).get();
        Integer serverId = context.channel().attr(SessionKey.SERVERID).get();
        CouplefightManager manager = Manager.couplefightManager;
        CoupleData data = manager.getDatasByGroupId(group);
        ChampionData championData = null;
        if(manager.getStatus() == CouplefightManager.status_tian){
            championData = data.getChampionTian();
        }else if(manager.getStatus() == CouplefightManager.status_di){
            championData = data.getChampionDi();
        }
        if(championData.getGround() == 0){
            throw new CouplefightException("战斗还没开始");
        }
        ChampionGroup championGroup = championData.getRounds().get(championData.getGround() - 1);
        ChampionRoom room = championGroup.getFight(fid);
        if(room == null){
            throw new CouplefightException("没有找到对应的玩家分组");
        }
        if(room.getRoomId() == 0){
            throw new CouplefightException("没有对应的战斗房间");
        }
        FightRoom fightRoom = Manager.fightManager.GetRoomByFightId(room.getRoomId());
        if(fightRoom == null){
            throw new CouplefightException("战斗房间不存在");
        }
//        Manager.fightManager
        TeamPlayerInfo p = new TeamPlayerInfo();
        p.setServerId(serverId);
        p.setRobot(false);
        p.setRoleId(rid);
        p.setName("");
        p.setReady(true);

        ZoneTeam team = new ZoneTeam();
        team.setPlat(serverPlat);
        team.setsId(serverId);
        team.setPow(0);
        team.getPlist().put(rid, p);

        fightRoom.getTeam().add(team);

        List<ZoneTeam> teams = new ArrayList<>();
        teams.add(team);
        Manager.fightManager.deal().fightStart(fightRoom, teams);
    }

    /**
     * 添加服务器分组数据
     * @param group
     */
    private void addServerGroup(Integer group) {
        CoupleData data = new CoupleData();
        data.setCreateDate(new Date());
        data.setServerGroupId(group);
        Manager.couplefightManager.getDatas().put(group,data);
    }

    /**
     * 获取预选赛奖励
     * @param context
     * @param group
     * @param rid
     * @param id
     */
    private void getTrialsAward(ChannelHandlerContext context, Integer group, long rid, int id) {
        CouplefightManager manager = Manager.couplefightManager;
        CoupleData data = manager.getDatasByGroupId(group);
        CoupleTeam team = data.getPlayers().get(rid);
        if(team == null){
            throw new CouplefightException("没有队伍信息");
        }
        Player p = null;
        if(team.getMen().getId() == rid){
            p = team.getMen();
        }else{
            p = team.getWomen();
        }
        Cfg_Marry_battle_reward_Bean bean = Cfg_Marry_battle_reward_Container.GetInstance().getValueByKey(id);
        if(bean.getType() == 4 && bean.getParm().get(0) <= team.getTrialsInfo().getCount()){
            if(!p.getTrialsAward().contains(id)){
                log.info("仙侣对决 玩家领取海选赛奖励 rid:{} awardId:{}", rid, id);
                p.getTrialsAward().add(id);
                //发送海选赛次数奖励
                CouplefightMessage.P2GGetTrialsAward.Builder res = CouplefightMessage.P2GGetTrialsAward.newBuilder();
                res.setAwardId(id);
                res.setRid(rid);
                MessageUtils.send_to_game(context, CouplefightMessage.P2GGetTrialsAward.MsgID.eMsgID_VALUE, res.build().toByteArray());
                return;
            }
        }
    }

    /**
     * 进入冠军赛地图
     * @param context
     * @param group
     * @param rid
     */
    private void championEnter(ChannelHandlerContext context, Integer group, long rid) {
        CouplefightManager manager = Manager.couplefightManager;
        CoupleData data = manager.getDatasByGroupId(group);
        CoupleTeam team = data.getPlayers().get(rid);
        if(team == null){
            throw new CouplefightException("队伍不存在");
        }
        ChampionData championData = null;
        if(manager.getStatus() == CouplefightManager.status_di_pre || manager.getStatus() == CouplefightManager.status_di){
            championData = data.getChampionDi();
        }else if(manager.getStatus() == CouplefightManager.status_tian_pre || manager.getStatus() == CouplefightManager.status_tian){
            championData = data.getChampionTian();
        }
        if(championData != null){
            List<ChampionGroup> rs = championData.getRounds();
            boolean can = rs.get(rs.size() - 1).getTeamIds().contains(team.getId());
            if(can){
                Player p = team.getPlayerById(rid);
                enterChampionPrepareRoom(championData, p);
            }
        }
    }

    /**
     * 进入小组赛准备房间
     * @param player
     * @return
     */
    private FightRoom enterChampionPrepareRoom(ChampionData data, Player player) {
        synchronized (data){
            FightRoom room = data.getPreRoom();
            //创建队伍
            ZoneTeam team = createTeam(player);
            List<ZoneTeam> teams = new ArrayList<>();
            teams.add(team);
            if(room == null){
                Cfg_Daily_Bean daily = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.COUPLE_FIGHT);
                Cfg_Clone_map_Bean clone = CfgManager.getCfg_Clone_map_Container().getValueByKey(daily.getCloneID().get(0));

                room = Manager.fightManager.deal().createFightRoom(clone, teams);
                data.setPreRoom(room);
                Manager.fightManager.deal().fightStart(room);
            }else{
                room.getTeam().add(team);
                Manager.fightManager.deal().fightStart(room, teams);
            }

            return room;
        }
    }

    /**
     * 粉丝排行
     * @param context
     * @param group
     * @param rid
     */
    private void sendChampionFansRankList(ChannelHandlerContext context, Integer group, long rid) {
        CouplefightManager manager = Manager.couplefightManager;
        CoupleData data = manager.getDatasByGroupId(group);
        CouplefightMessage.ResChampionFansRankList.Builder res = data.getChampionFansData().toProto(50);
        MessageUtils.send_to_player(context, rid, CouplefightMessage.ResChampionFansRankList.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    /**
     * 可支持的战队列表
     * @param context
     * @param group
     * @param rid
     */
    private void sendChampionTeamList(ChannelHandlerContext context, Integer group, long rid, int type) {
        CouplefightManager manager = Manager.couplefightManager;
        CoupleData data = manager.getDatasByGroupId(group);
        ChampionData championData = null;
        ChampionGroup championGroup = null;
        CouplefightMessage.ResChampionTeamList.Builder res = CouplefightMessage.ResChampionTeamList.newBuilder();
        if(type == 2){
            championData = data.getChampionDi();
        }else if(type == 1){
            championData = data.getChampionTian();
        }
        List<ChampionGroup> championGroups = championData.getRounds();
        if(championGroups == null){
            throw new CouplefightException("没有冠军赛数据");
        }
        championGroup = championGroups.get(championGroups.size() - 1);
        res.setType(type);
        if(championGroup != null){
            res.setRound(championGroup.getRound());
            for(ChampionRoom room : championGroup.getRooms()){
                if(room.getT1() != 0 && room.getT2() != 0){
                    res.addGuess(room.toProtoGuess(data, rid));
                }
            }
            MessageUtils.send_to_player(context, rid, CouplefightMessage.ResChampionTeamList.MsgID.eMsgID_VALUE, res.build().toByteArray());
        }
    }

//    /**
//     * 参与冠军赛竞猜
//     * @param context
//     * @param group
//     * @param rid
//     * @param type 1天 2地
//     * @param fightId 战斗id
//     */
//    private void sendChampionGuessUpdate(ChannelHandlerContext context, Integer group, long rid, int type, int fightId) {
//        CouplefightManager manager = Manager.couplefightManager;
//        CoupleData data = manager.getDatasByGroupId(group);
//        ChampionData championData = data.getChampionInfoByType(type);
//        if(championData == null){
//            throw new CouplefightException("没有冠军赛数据");
//        }
//        List<ChampionGroup> championGroups = championData.getRounds();
//        int round = championData.getGround();
//        ChampionGroup cg = championGroups.get(round);
//        ChampionRoom room = cg.getFight(fightId);
//        if(room == null){
//            throw new CouplefightException("冠军赛 没有找到对应的房间");
//        }
//        CouplefightMessage.ResChampionGuessUpdate.Builder res = CouplefightMessage.ResChampionGuessUpdate.newBuilder();
//        res.setType(type);
//        res.setRound(round);
//        res.setFightId(fightId);
//        CouplefightMessage.GuessInfo.Builder obj = room.toProtoGuess(data, rid);
//        res.setG1(obj.getG1());
//        res.setG2(obj.getG2());
//        MessageUtils.send_to_player(context, rid, CouplefightMessage.ResChampionGuessUpdate.MsgID.eMsgID_VALUE, res.build().toByteArray());
//    }

    private void sendChampionGuessInfo(ChannelHandlerContext context, Integer group, long rid, int type, int fightId) {
        CouplefightManager manager = Manager.couplefightManager;
        CoupleData data = manager.getDatasByGroupId(group);
        ChampionData championData = data.getChampionInfoByType(type);
        if(championData == null){
            throw new CouplefightException("没有冠军赛数据");
        }
        List<ChampionGroup> championGroups = championData.getRounds();
        for(ChampionGroup cg : championGroups){
            ChampionRoom room = cg.getFight(fightId);
            if(room != null){
                CouplefightMessage.ResChampionGuessInfo.Builder res = CouplefightMessage.ResChampionGuessInfo.newBuilder();
                res.setGuess(room.toProtoGuess(data, rid));
                res.setType(type);
                res.setRound(championData.getGround());
                MessageUtils.send_to_player(context, rid, CouplefightMessage.ResChampionGuessInfo.MsgID.eMsgID_VALUE, res.build().toByteArray());
                break;
            }
        }
    }

    private void sendChampionInfo(ChannelHandlerContext context, Integer group, long rid, Integer type) {
        CouplefightManager manager = Manager.couplefightManager;
        CoupleData data = manager.getDatasByGroupId(group);
        CouplefightMessage.ResChampionInfo.Builder res = data.toProtoChampions(type);
        if(res == null){
            return;
        }
        MessageUtils.send_to_player(context, rid, CouplefightMessage.ResChampionInfo.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    /**
     * 发送小组赛排名信息
     * @param context
     * @param group
     * @param rid
     */
    private void sendGroupRankInfo(ChannelHandlerContext context, Integer group, long rid) {
        CouplefightManager manager = Manager.couplefightManager;
        CoupleData data = manager.getDatasByGroupId(group);

        CoupleTeam team = data.getPlayers().get(rid);
        int groupId = 1;
        if(team != null && team.getGroupsInfo() != null){
            groupId = team.getGroupsInfo().getGroup().getId();
        }

        CouplefightMessage.ResGroupRank.Builder res = CouplefightMessage.ResGroupRank.newBuilder();
        res.setGroupId(groupId);
        if(data.getGroups() != null && data.getGroups().size() > 0){
            Group g = data.getGroups().get(groupId - 1);
            synchronized (g){
                int rank = 0;
                for(CoupleTeam t : g.getTeams()){
                    rank++;
                    CouplefightMessage.GroupTeam.Builder po = t.toGroupsProto();
                    po.setRank(rank);
                    res.addTeam(po);
                }
            }
        }
        MessageUtils.send_to_player(context, rid, CouplefightMessage.ResGroupRank.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    @Override
    public void initGroupInfo(CoupleData coupleData, List<CoupleTeam> teams) {
        coupleData.setGroupsRound(0);

        //设置队伍
        int groupSize = 8;
        List<Group> groups = new ArrayList<>(groupSize);
        for(int i = 1; i <= groupSize; i++){
            Group g = new Group();
            g.setId(i);
            groups.add(g);
        }
        coupleData.setGroups(groups);
        int i = 0;
        for(CoupleTeam t : teams){
            int group = i % groupSize;
            i++;
            Group g = groups.get(group);
            if(g == null){
                g = new Group();
                g.setId(group + 1);
                groups.set(group, g);
            }
            g.getTeams().add(t);
            t.getGroupsInfo().setGroup(g);
        }

        //分组战斗
        for(Group g : groups){
            if(g != null){
                g.init();
            }
        }

    }

    @Override
    public void reqGroupPrepareMapEnter(ChannelHandlerContext context, long rid) {
        if(Manager.couplefightManager.getStatus() != CouplefightManager.status_group_pre
          && Manager.couplefightManager.getStatus() != CouplefightManager.status_group){
            return;
        }
        String serverPlatId = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupId = Manager.couplefightManager.getGroup(serverPlatId);
        CoupleData data = Manager.couplefightManager.getDatasByGroupId(groupId);

        CoupleTeam team = data.getPlayers().get(rid);
        if(team == null){
            log.warn("仙侣对决没有玩家：rid:{}", rid);
            return;
        }
        Group g = team.getGroupsInfo().getGroup();
        if(g == null){
            log.warn("仙侣对决小组赛没有玩家：rid:{}", rid);
            return;
        }

        synchronized (g){
            Player player = team.getPlayerById(rid);
            enterGroupPrepareRoom(g, team, player);
        }

    }

    @Override
    public void initChampionInfo(CoupleData coupleData, List<CoupleTeam> dis, List<CoupleTeam> tians) {
        ChampionData championData = new ChampionData(dis);
        championData.setType(2);
        championData.setCoupleData(coupleData);
        coupleData.setChampionDi(championData);

        ChampionData championData2 = new ChampionData(tians);
        championData2.setType(1);
        championData2.setCoupleData(coupleData);
        coupleData.setChampionTian(championData2);
    }

    @Override
    public void reqChampionGuess(ChannelHandlerContext context, CouplefightMessage.G2PReqChampionGuess messInfo) {
        int type = messInfo.getType();
//        int round = messInfo.getRound();
        int fightId = messInfo.getFightId();
        long teamId = messInfo.getTeamId();
        long rid = messInfo.getRid();
        String name = messInfo.getName();
        int level = messInfo.getLevel();
        long power = messInfo.getPower();
        String serverPlatId = context.channel().attr(SessionKey.SERVERPLATID).get();
        //获取玩家所在分组
        Integer group = Manager.couplefightManager.getGroup(serverPlatId);
        if(group == null){
            return;
        }
        CouplefightManager manager = Manager.couplefightManager;
        CoupleData data = manager.getDatasByGroupId(group);
        ChampionData championData = null;
        if(type == 1){//天榜
            championData = data.getChampionTian();
        }else{
            championData = data.getChampionDi();
        }
        if(championData == null){
            log.warn("仙侣对决冠军赛 没有冠军赛数据 group:{} type:{} fightId:{} rid:{}", group,type,fightId,rid);
            return;
        }

        int round = championData.getGround();
        List<ChampionGroup> championGroups = championData.getRounds();
        ChampionRoom room = null;
        for(ChampionGroup championGroup : championGroups){
            room = championGroup.getFight(fightId);
            if(room != null){
                break;
            }
        }
        if(room == null){
            log.warn("仙侣对决冠军赛 没有找到对应的房间 group:{} type:{} round:{} fightId:{} rid:{}", group,type,round,fightId,rid);
            return;
        }
        if(room.getRoomId() > 0){
            //已开始不能竞猜
            MessageUtils.notify_player(context, 2, rid, MessageString.COUPLEFIGHT_CANNOTGUESS);
            return;
        }
        int res = room.guess(data, rid, teamId);
        sendChampionGuessInfo(context, group, rid, type, fightId);
        if(res == 0){
            //粉丝排行更新
            data.getChampionFansData().updateAndSort(new Fans(rid, name, level, power, serverPlatId));
            //通知游戏服务器扣除金币
            CouplefightMessage.P2GResChampionGuess.Builder pt = CouplefightMessage.P2GResChampionGuess.newBuilder();
            pt.setRid(rid);
            MessageUtils.send_to_game(context, CouplefightMessage.P2GResChampionGuess.MsgID.eMsgID_VALUE, pt.build().toByteArray());
            //保存竞猜记录
            CouplefightGuessBean bean = new CouplefightGuessBean();
            bean.setActivityId(data.getActivityId());
            bean.setGroup(data.getServerGroupId());
            bean.setType(type);
            bean.setRound(round);
            bean.setFightId(fightId);
            bean.setTeamId(teamId);
            bean.setRid(rid);
            bean.setName(name);
            bean.setLevel(level);
            bean.setPower(power);
            manager.getDao().save(bean);
        }
    }

    @Override
    public void onChampionNextRound() {
        log.info("仙侣对决 冠军赛新轮次开始");
        int status = Manager.couplefightManager.getStatus();
        if(status == CouplefightManager.status_di || status == CouplefightManager.status_tian){
            for(CoupleData data : Manager.couplefightManager.getDatas().values()){
                ChampionData championData = null;
                if(status == CouplefightManager.status_di){
                    championData = data.getChampionDi();
                }else{
                    championData = data.getChampionTian();
                }
                startChampionRound(data, championData);
            }
        }else{
            log.warn("仙侣对局 不在冠军赛战斗时间");
        }
    }

    /**
     * 进入小组赛准备房间
     * @param player
     * @return
     */
    private FightRoom enterGroupPrepareRoom(Group group, CoupleTeam t, Player player) {
        FightRoom room = group.getPreRoom();

        //创建队伍
        ZoneTeam team = createTeam(player);
        List<ZoneTeam> teams = new ArrayList<>();
        teams.add(team);
        if(room == null){
            Cfg_Daily_Bean daily = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.COUPLE_FIGHT);
            Cfg_Clone_map_Bean clone = CfgManager.getCfg_Clone_map_Container().getValueByKey(daily.getCloneID().get(0));

            room = Manager.fightManager.deal().createFightRoom(clone, teams);
            group.setPreRoom(room);
            Manager.fightManager.deal().fightStart(room);
        }else{
            room.getTeam().add(team);
            Manager.fightManager.deal().fightStart(room, teams);
        }
        return room;
    }

    /**
     * 创建队伍
     * @param player
     * @return
     */
    private ZoneTeam createTeam(Player player) {
        CoupleTeam t = player.getTeam();
        TeamPlayerInfo tm = createTeamPlayerInfo(player);
        ZoneTeam team = new ZoneTeam();
        team.setPlat(t.getPlatName());
        team.setsId(t.getServerId());
        team.setPow(player.getPower());
        team.getPlist().put(tm.getRoleId(), tm);
        return team;
    }


    /**
     * 发送海选赛信息
     * @param context
     * @param group
     * @param rid
     */
    private void sendTrialsInfo(ChannelHandlerContext context, Integer group, long rid) {
        CoupleData data = Manager.couplefightManager.getDatasByGroupId(group);
        CoupleTeam mt = data.getPlayers().get(rid);
        sendTrialsInfo(context, mt, rid, data);//2651
    }

    private void sendTrialsInfo(ChannelHandlerContext context, CoupleTeam team, long rid, CoupleData data){
        CouplefightMessage.ResTrialsInfo.Builder res = CouplefightMessage.ResTrialsInfo.newBuilder();
        if(team != null){
            res.setIsApply(true);
            res.setTeam(team.toProto());
            CouplefightMessage.TrialsInfo.Builder trialsProto = team.toTrialsProto();
            Player player = null;
            if(team.getMen().getId() == rid){
                player = team.getMen();
            }else{
                player = team.getWomen();
            }
            trialsProto.addAllGetAwards(player.getTrialsAward());
            //设置排名
            int i = 0;
            int rank = 0;
            for(RankInfo r : data.getRanks()){
                i++;
                if(r.getId() == team.getId()){
                    rank = i;
                    break;
                }
            }
            trialsProto.setRank(rank);
            res.setTrials(trialsProto);
        }else{
            res.setIsApply(false);
        }
        if(context == null){
            context = Manager.gameServerManager.GetSession(team.getPlatName(), team.getServerId());
        }
        MessageUtils.send_to_player(context, rid, CouplefightMessage.ResTrialsInfo.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    /**
     * 发送小组赛信息
     * @param group
     * @param rid
     */
    private void sendGroupInfo(ChannelHandlerContext context, Integer group, long rid) {
        CoupleData data = Manager.couplefightManager.getDatasByGroupId(group);
        CoupleTeam mt = data.getPlayers().get(rid);
        synchronized (data){
            CouplefightMessage.ResGroupInfo.Builder res = CouplefightMessage.ResGroupInfo.newBuilder();
            res.setMygroup(0);
            res.setJoin(false);
            if(mt != null && mt.getGroupsInfo() != null){
                Group myGroup = mt.getGroupsInfo().getGroup();
                if(myGroup != null){
                    res.setJoin(true);
                    res.setMygroup(myGroup.getId());
                }
            }
            for(Group g : data.getGroups()){
                if(g.getTeams().size() == 0){
                    continue;
                }
                CouplefightMessage.Group.Builder builder_g = CouplefightMessage.Group.newBuilder();
                builder_g.setId(g.getId());
                for(CoupleTeam t : g.getTeams()){
                    builder_g.addTeam(t.toProto());
                }
                res.addGroup(builder_g);
            }
            MessageUtils.send_to_player(context, rid, CouplefightMessage.ResGroupInfo.MsgID.eMsgID_VALUE, res.build().toByteArray());
        }
    }

    /**
     * 发送预选赛排名信息
     * @param group
     * @param rid
     */
    private void sendTrialsRankInfo(ChannelHandlerContext context, Integer group, long rid) {
        CoupleData data = Manager.couplefightManager.getDatasByGroupId(group);
        CoupleTeam t = data.getPlayers().get(rid);
        synchronized (data){
            CouplefightMessage.ResTrialsRank.Builder res = CouplefightMessage.ResTrialsRank.newBuilder();
            CouplefightMessage.TrialsInfo.Builder self = null;
            int i = 0;
            for(RankInfo rankInfo : data.getRanks()){
                CoupleTeam o = data.getTeams().get(rankInfo.getId());
                if(o == null){
                    continue;
                }
                CouplefightMessage.TrialsRankInfo.Builder b = CouplefightMessage.TrialsRankInfo.newBuilder();
                b.setTeam(o.toProto());
                CouplefightMessage.TrialsInfo.Builder trialsInfo = CouplefightMessage.TrialsInfo.newBuilder();
                i++;
                trialsInfo.setRank(i);
                trialsInfo.setScore(o.getTrialsInfo().getScore());
                trialsInfo.setCount(o.getTrialsInfo().getCount());
                if( o.getTrialsInfo().getCount() == 0){
                    trialsInfo.setRate(0);
                }else{
                    trialsInfo.setRate(o.getTrialsInfo().getWinCount() * 100 / o.getTrialsInfo().getCount());
                }
                b.setTrials(trialsInfo);
                if(t != null && o == t){
                    self = trialsInfo;
                }
                res.addRanks(b);
            }
            if(self != null){
                res.setSelfRank(self.getRank());
            }else{
                res.setSelfRank(0);
            }
            MessageUtils.send_to_player(context, rid, CouplefightMessage.ResTrialsRank.MsgID.eMsgID_VALUE, res.build().toByteArray());
        }
    }

    @Override
    public void stop() {
        log.info("仙侣对决停止");
        saveDB();
        log.info("仙侣对决停止完成");
    }
}
