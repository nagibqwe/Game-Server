package common.fightroom;

import com.data.CfgManager;
import com.data.Global;
import com.data.MessageString;
import com.data.bean.Cfg_Clone_map_Bean;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.db.bean.PeakBean;
import com.game.fightroom.manager.FightManager;
import com.game.fightroom.script.IFightManagerScript;
import com.game.fightroom.structs.*;
import com.game.gameserver.manager.GameServerManager;
import com.game.gameserver.structs.ServerInfo;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.server.MainServer;
import com.game.servermatch.structs.GameServerInfo;
import com.game.soulanimalforest.manager.SoulAnimalForestManager;
import com.game.structs.ServerType;
import com.game.structs.SessionKey;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import com.game.worldbonfire.structs.WorldBonfire;
import com.game.zone.structs.CopyMapType;
import com.game.zone.structs.TeamPlayerInfo;
import com.game.zone.structs.ZoneTeam;
import com.game.zone.structs.ZoneMapDefine;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CommonMessage.CrossAttribute;
import game.message.CrossFightMessage;
import game.message.CrossFightMessage.*;
import game.message.CrossServerMessage;
import game.message.CrossServerMessage.F2PCloneRewardNotGet;
import game.message.CrossServerMessage.G2FSynPlayerOut;
import game.message.CrossServerMessage.fightEndScore;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author soko <xuchangming@haowan123.com>
 */
public class FightManagerScript implements IFightManagerScript {

    private static final Logger LOG = LogManager.getLogger(FightManagerScript.class);

    @Override
    public int getId() {
        return ScriptEnum.FightManagerScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 房间TICK处理
     *
     * @param now
     */
    @Override
    public void OnTick(long now) {
        //检查有没有战斗服
        List<FightRoom> list = new ArrayList<>(FightManager.getInstance().getFrcache().values());
        for (FightRoom room : list) {

            if (room.getRstate() >=  FightRoomState.FIGHTEND) {
                FightManager.getInstance().RemoveFight(room);//房间删除掉
                continue;
            }

            //如果房间为0 ，则房间不删除
            if (room.getFightTime() == 0) {
                continue;
            }

            long lenTime = room.getCtime() + room.getFightTime() + 2 * 60 * 1000;
            if (lenTime < now) {
                FightManager.getInstance().RemoveFight(room);//房间删除掉
            }
        }
    }

    /**
     * 玩家主动离开
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnG2PReqOutFightRoom(ChannelHandlerContext context, G2PReqOutFightRoom mess) {
        long fid = mess.getFightId();
        FightRoom fr = FightManager.getInstance().getFrcache().get(fid);
        if (fr == null) {
            fr = FightManager.getInstance().GetRoomByRoleId(mess.getRoleid());
        }

        if (fr == null) {
            sendOutInfo(context, mess.getRoleid(), mess.getFightId(), 1);
            return;
        }

        //战斗已经在进行了， 不能删除房间
        if (fr.getRstate() >= FightRoomState.READYROOM) {
            sendOutInfo(context, mess.getRoleid(), mess.getFightId(), 5);
            return;
        }

        //到结算了， 可以退出
        if (fr.getRstate() >= FightRoomState.FIGHTEND) {
//            fr.getRoles().remove(mess.getRoleid());
            fr.removeRoleId(mess.getRoleid());
            sendOutInfo(context, mess.getRoleid(), mess.getFightId(), 0);
            return;
        }

        if (!fr.hasRoleId(mess.getRoleid())) {
            sendOutInfo(context, mess.getRoleid(), mess.getFightId(), 2);
            return;
        }

        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(fr.getModelId());
        if (bean == null) {
            sendOutInfo(context, mess.getRoleid(), mess.getFightId(), 4);
            return;
        }

        fr.removeRoleId(mess.getRoleid());

        LOG.info("由于" + mess.getFightId() + "房间中的" + mess.getRoleid() + "主动退出！当前房间人员：" + fr.hasPeoples() + "数");
        sendOutInfo(context, mess.getRoleid(), mess.getFightId(), 0);
//        sendOutInfoToGame(fi, mess.getFightId());//通知玩家离开
        for (Iterator<ZoneTeam> it = fr.getTeam().iterator(); it.hasNext();) {
            ZoneTeam haveZt = it.next();
            if (haveZt.getPlist().isEmpty()) {
                it.remove();
            }
        }
        long now =  TimeUtils.Time();
        long lenTime = fr.getCtime() + fr.getFightTime() + 2 * 60 * 1000;
        if (lenTime > now) {
            return;
        }
        //房间中没有人了
        if (fr.hasPeoples() < 1) {
            if (fr.getFightTime() > 0) {
                LOG.info("由于房主主动退出而结束！");
                Manager.fightManager.RemoveFight(fr);//房间删除
            }
        }
    }

    private void sendOutInfo(ChannelHandlerContext context, long roleId, long fightid, int state) {
        LOG.info("玩家roleId : " + roleId + "主动离开了房间:" + fightid + "， state =" + state);
        ResOutFightRoom.Builder msg = ResOutFightRoom.newBuilder();
        msg.setFightId(fightid);
        msg.setState(state);
        MessageUtils.send_to_player(context, roleId, ResOutFightRoom.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 玩家离线了， 直接房间处理干掉
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnG2FSynPlayerOut(ChannelHandlerContext context, G2FSynPlayerOut mess) {
        long roleId = mess.getRoleId();

        List<FightRoom> frlist = new ArrayList<>(Manager.fightManager.getFrcache().values());
        for (FightRoom fr : frlist) {


            //副本已经开始就不在处理这个了
            if (fr.getRstate() >= FightRoomState.READYROOM) {
                continue;
            }

            if (fr.hasRoleId(roleId)) {
                LOG.info("房间中的玩家离开了， 副本应该结束");
                fr.removeRoleId(roleId);

            }
            for (Iterator<ZoneTeam> it = fr.getTeam().iterator(); it.hasNext();) {
                ZoneTeam haveZt = it.next();
                if (haveZt.getPlist().isEmpty()) {
                    it.remove();
                }
            }
            if (fr.hasPeoples() > 0) {
                return;
            }
            /**
             * 检查是不是永久副本
             */
            long now =  TimeUtils.Time();
            long lenTime = fr.getCtime() + fr.getFightTime() + 2 * 60 * 1000;
            if (lenTime > now) {
                return;
            }
            if (fr.getFightTime() > 0) {
                FightManager.getInstance().RemoveFight(fr);//房间删除掉
            }
        }
    }

    @Override
    public boolean fightStart(FightRoom mine) {
        if (mine.getServerId() == 0) {
            //开始分配服务器
            ServerInfo serverInfo = getFightServerId(Math.max(mine.hasPeoples(), mine.getHaveNum()));
            if (serverInfo == null) {
                List<ServerInfo> list = Manager.gameServerManager.GetType(ServerType.FIGHTSERVER);
                LOG.error("没有战斗服连接在线， 请运维检查一下战斗服是否有！ 战斗服个数：" + list.size());
                return false;
            }
            mine.setServerId(serverInfo.getServerId());
            mine.setpPlat(serverInfo.getPlatName());
        }
        P2GResFightStart.Builder msg = P2GResFightStart.newBuilder();
        msg.setFightId(mine.getFid());
        Map<ChannelHandlerContext, List<roleAtt.Builder>> players = new HashMap<>();
        for (ZoneTeam zt : mine.getTeam()) {
            ChannelHandlerContext session = Manager.gameServerManager.GetSession(zt.getPlat(), zt.getsId());
            if (session == null) {
                continue;
            }
            List<roleAtt.Builder> roleIds;
            if (players.containsKey(session)) {
                roleIds = players.get(session);
            } else {
                roleIds = new ArrayList<>();
                players.put(session, roleIds);
            }
            Iterator<Entry<Long, TeamPlayerInfo>> pp = zt.getPlist().entrySet().iterator();
            while (pp.hasNext()) {
                TeamPlayerInfo tp = pp.next().getValue();
                roleAtt.Builder info = roleAtt.newBuilder();
                info.setCampNo(zt.getCampNo());
                info.setRoleId(tp.getRoleId());
                roleIds.add(info);
            }
        }
        msg.addAllMapSetList(getFightRoomParam(mine));
        msg.setZoneModelId(mine.getModelId());
        msg.setFightServerId(mine.getServerId());
        msg.setMapModelId(mine.getMapmodelId());
        for (ChannelHandlerContext context : players.keySet()) {
            List<roleAtt.Builder> roleIds = players.get(context);
            for (roleAtt.Builder rb : roleIds) {
                msg.addRoleInfo(rb);
            }
            MessageUtils.send_to_game(context, P2GResFightStart.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
        //进入战斗状态
        mine.setRstate(FightRoomState.FIGHTING);
        return true;
    }

    /**
     * 根据最大人数计算最佳战斗服给玩家分配
     * @param max_num
     * @return
     */
    @Override
    public ServerInfo getFightServerId(int max_num) {
        List<ServerInfo> list = Manager.gameServerManager.GetType(ServerType.FIGHTSERVER);
        if (list.size() < 1) {
            LOG.error("没有收到有战斗服的注册信息！");
            return null;
        }
        int min_num = list.get(0).getHaveNum();
        ServerInfo needSid = list.get(0);
        int lastminnum = min_num;
        //找到最小的数量值
        for (ServerInfo si : list) {
            min_num = Math.min(si.getHaveNum(), min_num);
            if (lastminnum != min_num) {
                lastminnum = min_num;
                needSid = si;
            }
        }

        if (min_num > 2000) {
            MainServer.getInstance().getErrorLogThread().pushErrorExcptionLog(list.size() + "个战斗服已经承受了2000多个人了", "所有战斗服都已经申请了2000多个的战场斗争了！");
        }

        LOG.info(needSid.getServerId() + "_ " + needSid.getServerIp() + "人数:" + needSid.getHaveNum() + "当前要添加的数量:" + max_num);
        needSid.setHaveNum(min_num + max_num);//更新服务器人数
        LOG.info(needSid.getServerId() + "_ " + needSid.getServerIp() + "人数添加后为：" + needSid.getHaveNum());
        return needSid;
    }

    /**
     * 改变房间的状态值， 主是等待， 战斗， 及结束，发奖 等的状态
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnF2PFightRoomState(ChannelHandlerContext context, F2PFightRoomState mess) {

        long fid = mess.getFightId();
        FightRoom fr = FightManager.getInstance().getFrcache().get(fid);

        if (fr == null) {
            return;
        }

        fr.setRstate(mess.getState());
        LOG.info("当前房间" + fr.getFid() + "的战斗状态：" + fr.getRstate());
    }


    @Override
    public void OnF2PCloneRewardNotGet(ChannelHandlerContext context, F2PCloneRewardNotGet mess) {

        //接收到没有发完玩家的奖励数据
        long fightid = mess.getFightId();
        int modelId = mess.getModelId();
        Set<String> lk = new HashSet<>();
        for (fightEndScore fes : mess.getRoleInfoList()) {
            FightPlayerReward fpr = new FightPlayerReward();
            fpr.setCloneModelid(modelId);
            fpr.setFightId(fightid);
            fpr.setPlatsid(fes.getPlatSid());
            fpr.setRoleId(fes.getRoleId());
            fpr.setScore(fes.getScore());
            fpr.setSuccess(fes.getIsSuccess());
            fpr.setTime(fes.getTime());
            fpr.setRewardTime(fes.getRewardtime());
            fpr.setSortIndex(fes.getSortIndex());
            lk.add(fes.getPlatSid());
        }
        LOG.info("收到战斗服发上来的副本没有收取的奖励数据：fid=" + fightid + " , 副本ID=" + modelId + "(" + Manager.zoneManager.GetCopymapName(modelId) + ")");

    }


    //指定的玩家进入房间
    @Override
    public void fightStart(FightRoom room, Collection<ZoneTeam> zt) {
        if (room.getServerId() == 0) {
            //开始分配服务器
            ServerInfo serverInfo = getFightServerId(Math.max(room.hasPeoples(), room.getHaveNum()));
            if (serverInfo ==  null) {
                LOG.error("没有战斗服连接在线， 请运维检查一下战斗服是否有！" + zt);
                return;
            }
            room.setServerId(serverInfo.getServerId());
            room.setpPlat(serverInfo.getPlatName());
        }
        HashMap<Integer, String> sendPlat = new HashMap<>();
        P2GResFightStart.Builder msg = P2GResFightStart.newBuilder();
        msg.setFightServerId(room.getServerId());
        msg.setFightId(room.getFid());
        msg.setZoneModelId(room.getModelId());
        msg.setLevel(room.getLevel());
        for (ZoneTeam z : zt) {
            for (TeamPlayerInfo tpi : z.getPlist().values()) {
                roleAtt.Builder mRole = roleAtt.newBuilder();
                int camp =   tpi.getCampNo()>0?tpi.getCampNo():z.getCampNo();
                mRole.setCampNo(camp);
                mRole.setIsRobot(tpi.isRobot());
                mRole.setRoleId(tpi.getRoleId());
                mRole.setCarear(tpi.getCareer());
                msg.addRoleInfo(mRole);
            }
            if (z.getsId() != 0 && !z.getPlat().isEmpty()) {
                sendPlat.put(z.getsId(), z.getPlat());
            }
        }
        msg.setMapModelId(room.getMapmodelId());
        msg.addAllMapSetList(getFightRoomParam(room));
        //分别发到自己的服务器,如果来自同一服，也不能发两遍
        for (Integer key : sendPlat.keySet()) {
            ChannelHandlerContext socket = Manager.gameServerManager.GetSession(sendPlat.get(key), key);
            if (socket == null) {
                LOG.error("发送到各自的逻辑服失败了 key " + key + "plat" + sendPlat.get(key));
                continue;
            }
            MessageUtils.send_to_game(socket, P2GResFightStart.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    /**
     * 匹配的多人副本创建房间
     *
     * @param bean
     * @param zt
     * @return
     */
    @Override
    public FightRoom createFightRoom(Cfg_Clone_map_Bean bean, List<ZoneTeam> zt) {
        FightRoom room = new FightRoom();
        room.setModelId(bean.getId());
        room.setCtime(TimeUtils.Time());
        room.setCrId(zt.get(0).leader().getRoleId());//取第一个了
        room.setCname(zt.get(0).leader().getName());
        room.setFid(IDConfigUtil.getLogId());
        room.setAllReadyStart(false);
        room.setType(bean.getType());
        room.setWaitTime(room.getCtime());
        room.setEndwait(room.getCtime() + bean.getEnter_time());
        room.setFightTime(bean.getExist_time() + bean.getEnter_time());
        room.getTeam().addAll(zt);
        room.setAttackValue(0);
        room.setMinP(ZoneMapDefine.GM_ZONE_MAP_MIN_NUM);//最低要求的人数
        room.setRstate(FightRoomState.CREATEROOM);
        FightManager.getInstance().SaveRoomInfo(room, zt.get(0).getPlat(), zt.get(0).getsId());//保存并且写log
        return room;
    }

    /**
     * 收到战斗服的通知，有玩家离开战场， 要检查是否需要清除此玩家的排队序列
     *
     * @param context
     * @param mess
     */
    @Override
    public void PlayerOutFightRoomFromFight(ChannelHandlerContext context, CrossServerMessage.F2PPlayerOutFightRoom mess) {
        LOG.info("收到来致战斗服：" + context.channel() + "的玩家离开协议！");
        //策划要求神魔战场 退出后，在进来还是回原图，所以这里特殊处理不向公共服发玩家退出房间
        if (mess.getModelId() == 61001){
            return;
        }
        long fightId = mess.getFightId();
        FightRoom fr = Manager.fightManager.GetRoomByFightId(fightId);
        if (fr == null) {
            LOG.info(mess + "数据过来已经没有房间了！");
            return;
        }

        if (!fr.hasRoleId(mess.getRoleId())) {
            LOG.info(mess + "数据过来已经没有此角色参加此战斗！");
            return;
        }
        List<ZoneTeam> delzt = new ArrayList<>();
        for (ZoneTeam zt : fr.getTeam()) {
            if (zt.getPlist().containsKey(mess.getRoleId())) {
                zt.getPlist().remove(mess.getRoleId());
                LOG.info(mess + "数据过来清除此玩家！");
            }

            if (zt.getPlist().size() < 1) {
                delzt.add(zt);
            }
        }

        if (delzt.size() < 1) {
            return;
        }

        for (ZoneTeam zt : delzt) {
            LOG.info(fightId + "房间清除小队" + zt);
            fr.getTeam().remove(zt);
        }

        //暂时不清理房间
//        if ( fr.hasPeoples() < 1)
//        {
//            Manager.fightManager.RemoveFight(fr);
//            LOG.error("将当前fightID清除了！");
//        }
    }

    @Override
    public long EnterSoulAnimalForestClone(ChannelHandlerContext context, ZoneTeam zt, int modelId, Cfg_Clone_map_Bean bean) {
        LOG.info("创建房间的副本id：" + modelId);
        if (zt == null) {
            return 0;
        }   //获取组号
        String groupKey = zt.getPlat() + "_" + zt.getsId();
        GameServerInfo info = ServerMatchManager.infos.get(groupKey);
        if (info == null) {
            LOG.info("未找到相应服务器   "  +groupKey);
            return 0;
        }
        int openDay =  GameServerManager.getOpenServerDay(info.getOpenTime());
        if (openDay <  Global.Soul_Beasts_Open_Time){

            for (Long roleID : zt.getPlist().keySet()){
                MessageUtils.notify_player(context,roleID, MessageString.Soul_Beasts_Open_Time,Global.Soul_Beasts_Open_Time+"",openDay+"");
            }
            return 0;
        }
        Integer groupId =  ServerMatchManager.deal().getGroupIDForCurOpenDay(groupKey, DailyActiveDefine.ACTIVITY_SOULANIMALISLAND);
        if (groupId<0) {
            LOG.info("创建房间失败  当前服务器不足以玩跨服：" + groupKey);
             for (Long roleID : zt.getPlist().keySet()){
                 MessageUtils.notify_player(context,roleID, MessageString.ServerMachtFail);
             }
            return 0;
        }
        if ( !SoulAnimalForestManager.allGroupList.contains(groupId)){
            LOG.info("等待下一阶段匹配： " + groupId);
            return 0;
        }
        LOG.info("进入房间groupid：" + groupId);
        //查找已经创建好的房间
        FightRoom mine = null;
        //如果组的队是没有满的
        List<FightRoom> list = FightManager.getInstance().getBravePeakRoom(modelId, groupId);
        if (list.size() > 0) {
            mine = list.get(0);
            int maxNum =  FightManager.getInstance().getLineMaxPeople(modelId);
            if (mine.hasPeoples() >= maxNum){
                for (Long roleID : zt.getPlist().keySet()){
                    MessageUtils.notify_player(context,roleID, MessageString.Activityp_Is_Full);
                }
                LOG.info("神兽岛 地图人数已满：hasPeoples {}  maxNum  {}  mapModelId {}" ,mine.hasPeoples(),maxNum,modelId);
                return 0;
            }
            if (list.size() > 1) {
                for (int i = 1; i < list.size(); ++i) {
                    Manager.fightManager.RemoveFight(list.get(i));//把多余的删除
                }
            }
        }
        //如果没有分配到房间， 则创建一个新的房间
        List<ZoneTeam> zts = new ArrayList<>();
        zts.add(zt);
        if (mine == null) {
            mine = createFightRoom(bean, zts);
            mine.setServerGroupId(groupId);
        } else {
            clearRoomPlayer(mine, zt);
            mine.getTeam().add(zt);
        }
        mine.setAllReadyStart(true);
        fightStart(mine, zts);
        return mine.getFid();
    }

    //清理玩家以前的队列值
    private void clearRoomPlayer(FightRoom fr, ZoneTeam zt) {
        Set<Long> plist = zt.getPlist().keySet();
        for (long roleId : plist) {
            if (fr.hasRoleId(roleId)) {
                fr.removeRoleId(roleId);
            }
        }
        fr.getTeam().removeIf(haveZt -> haveZt.getPlist().isEmpty());
    }

    @Override
    public List<CrossAttribute> getFightRoomParam(FightRoom fr) {
        List<CrossAttribute> result = new ArrayList<>();

        if (fr.getType() == CopyMapType.House_CopyMap) {
            CrossAttribute.Builder info = CrossAttribute.newBuilder();
            info.setType(0);
            info.setValue(fr.getCrId());
            info.setParam1(0);
            result.add(info.build());
            return result;
        }

        if (fr.getType() == CopyMapType.MonsterSoul_CopyMap) {
            int serverGroupId = fr.getServerGroupId();
            CrossAttribute.Builder info = CrossAttribute.newBuilder();
            info.setType(100000);
            info.setValue(0);
            info.setParam1(serverGroupId);
            result.add(info.build());
            return result;
        }

        if (fr.getType() == CopyMapType.WorldBonfire_CopyMap) {
            CrossAttribute.Builder info = CrossAttribute.newBuilder();

            WorldBonfire worldBonfire = Manager.worldBonfireManager.getWorldBonfire().get(fr.getServerGroupId());
            info.setParam1(worldBonfire.getExp());
            info.setType(worldBonfire.getLv());
            info.setValue(worldBonfire.getBonFireCreateTime());
            result.add(info.build());
            return result;
        }

        if (fr.getType() == CopyMapType.Brave_CopyMap) {
            CrossAttribute.Builder info = CrossAttribute.newBuilder();
            info.setType(10009);
            info.setValue(fr.getFightTime());
            result.add(info.build());
            return result;
        }

        if (fr.getType() == CopyMapType.PeakPk){
            for (ZoneTeam team: fr.getTeam()){
                for (TeamPlayerInfo player: team.getPlist().values()) {
                    PeakBean bean = Manager.peakManager.getPeaks().get(player.getRoleId());
                    CrossAttribute.Builder info = CrossAttribute.newBuilder();
                    info.setValue(player.getRoleId());
                    info.setType(bean.getScore());
                    info.setParam1(bean.getRankId());
                    result.add(info.build());
                }
            }
            return result;
        }
        return result;
    }

    @Override
    public ServerInfo getMinServer() {
        List<ServerInfo> list = Manager.gameServerManager.GetType(ServerType.FIGHTSERVER);

        if (list.size() == 0) {
            return null;
        }
        ServerInfo minServerInfo = null;
        int minNum = 0;
        for (ServerInfo serverInfo : list) {
            if (serverInfo.getHaveNum() == 0) {
                return serverInfo;
            }

            if (minNum == 0 || serverInfo.getHaveNum() < minNum) {
                minServerInfo = serverInfo;
                minNum = serverInfo.getHaveNum();
            }
        }
        return minServerInfo;
    }

    @Override
    public boolean isMaxServer(ServerInfo serverInfo) {
        if (serverInfo.getHaveNum() >= 1500) {
            return true;
        }

        return false;
    }

    public void addZoneTeam(ChannelHandlerContext context,FightRoom room,long roleID){
        int sid = context.channel().attr(SessionKey.SERVERID).get();
        String plat = context.channel().attr(SessionKey.SERVERPLAT).get();

        boolean isInZoneTeam = false;
        for (ZoneTeam z:room.getTeam()) {
            if (z.getsId() == sid) {
                if (!z.getPlist().containsKey(roleID)) {
                    TeamPlayerInfo teamInfo = new TeamPlayerInfo();
                    teamInfo.setRoleId(roleID);
                    z.getPlist().put(roleID,teamInfo);
                }
                isInZoneTeam =true;
                break;
            }
        }
        if (!isInZoneTeam) {
            ZoneTeam zoneTeam = new ZoneTeam();
            zoneTeam.setPlat(plat);
            zoneTeam.setsId(sid);
            TeamPlayerInfo teamInfo = new TeamPlayerInfo();
            teamInfo.setRoleId(roleID);
            zoneTeam.getPlist().put(roleID,teamInfo);
            room.getTeam().add(zoneTeam);
        }
    }

    /**
     * 进入家园地图
     * @param context
     * @param modelId
     * @param level
     * @param roleId
     * @param zt
     * @param bean
     */
    @Override
    public void enterHouseMap(ChannelHandlerContext context, int modelId, int level, long roleId, ZoneTeam zt, Cfg_Clone_map_Bean bean) {

        String groupKey = zt.getPlat() + "_" + zt.getsId();
        GameServerInfo info = ServerMatchManager.infos.get(groupKey);
        if (info == null) {
            LOG.info("未找到相应服务器   "  +groupKey);
            return;
        }
        //查找已经创建好的房间
        FightRoom mine = FightManager.getInstance().getFrcache().get(roleId);

        //如果没有分配到房间， 则创建一个新的房间
        if (mine == null) {
            mine = new FightRoom();
            mine.setFid(zt.leader().getRoleId());
            mine.setModelId(bean.getId());
            mine.setCtime(TimeUtils.Time());
            mine.setCrId(roleId);
            mine.setCname("房主");
            mine.setAllReadyStart(false);
            mine.setType(bean.getType());
            mine.setWaitTime(mine.getCtime());
            mine.setEndwait(mine.getCtime() + bean.getEnter_time());
            mine.setFightTime(bean.getExist_time() + bean.getEnter_time());
            mine.getTeam().add(zt);
            mine.setAttackValue(0);
            mine.setMinP(ZoneMapDefine.GM_ZONE_MAP_MIN_NUM);//最低要求的人数
            mine.setRstate(FightRoomState.CREATEROOM);
            FightManager.getInstance().SaveRoomInfo(mine, zt.getPlat(), zt.getsId());//保存并且写log
        } else {
            clearRoomPlayer(mine, zt);
            mine.getTeam().add(zt);
        }
        mine.setLevel(level);
        mine.setAllReadyStart(true);

        fightStart(mine, mine.getTeam());
    }

    @Override
    public void closeFightRoom(FightRoom fightRoom) {
        CrossFightMessage.P2FCloseMap.Builder req = CrossFightMessage.P2FCloseMap.newBuilder();
        req.setRoomID(fightRoom.getFid());
        ChannelHandlerContext context = Manager.gameServerManager.GetSession(fightRoom.getPlat(), fightRoom.getServerId());
        if(context != null){
            MessageUtils.send_to_game(context, P2FCloseMap.MsgID.eMsgID_VALUE, req.build().toByteArray());
        }
//        MessageUtils.send_to_room(fightRoom, P2FCloseMap.MsgID.eMsgID_VALUE, req.build().toByteArray());
    }
}
