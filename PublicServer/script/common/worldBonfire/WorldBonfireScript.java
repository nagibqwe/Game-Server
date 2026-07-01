package common.worldBonfire;

import com.data.CfgManager;
import com.data.Global;
import com.data.MessageString;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Daily_Bean;
import com.data.bean.Cfg_World_bonfire_Bean;
import com.game.dailyactive.manager.DailyActiveManager;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.fightroom.structs.FightRoom;
import com.game.gameserver.manager.GameServerManager;
import com.game.gameserver.structs.ServerInfo;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.structs.SessionKey;
import com.game.utils.MessageUtils;
import com.game.worldbonfire.manager.WorldBonfireManager;
import com.game.worldbonfire.script.IWorldBonfireScript;
import com.game.worldbonfire.structs.WorldBonfire;
import com.game.worldbonfire.structs.WorldBonfireMatch;
import com.game.worldbonfire.structs.WorldBonfireMm;
import com.game.worldbonfire.structs.WorldBonfireTeam;
import com.game.zone.structs.TeamPlayerInfo;
import com.game.zone.structs.ZoneTeam;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.WorldBonfireMessage;
import game.message.WorldBonfireMessage.*;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @auther lw
 * @create 2019-10-17 17:51
 */
public class WorldBonfireScript implements IWorldBonfireScript {

    private static final Logger logger = LogManager.getLogger(WorldBonfireScript.class);

    private Object obj = new Object();

    @Override
    public void beginWorldBonfire(int round) {
        Manager.worldBonfireManager.getRoleIDWithGroupID().clear();
        Manager.worldBonfireManager.getRooms().clear();
        Manager.worldBonfireManager.getJoinPlayers().clear();
        Manager.worldBonfireManager.getMatchTeam().clear();
        Manager.worldBonfireManager.getMatchMembers().clear();

        List<Integer> gruopList = ServerMatchManager.deal().getAllReachMatchGroupIDList(DailyActiveDefine.ACTIVITY_WORLD_BONFIRE);
        if (gruopList == null)
            return;
        for (Integer groupID :  gruopList){
            WorldBonfire worldBonfire  = new  WorldBonfire();
            worldBonfire.setExp(0);
            worldBonfire.setLv(1);
            worldBonfire.setBonFireCreateTime(TimeUtils.getTodayBeginTime() + WorldBonfireManager.getActiveBeginTime(round));
            Manager.worldBonfireManager.getWorldBonfire().put(groupID, worldBonfire);
        }
    }

    @Override
    public void endWorldBonfire() {
        Manager.worldBonfireManager.getRoleIDWithGroupID().clear();
        Manager.worldBonfireManager.getRooms().clear();
        Manager.worldBonfireManager.getJoinPlayers().clear();
        Manager.worldBonfireManager.getMatchTeam().clear();
        Manager.worldBonfireManager.getMatchMembers().clear();
        Manager.worldBonfireManager.getWorldBonfire().clear();
        Manager.worldBonfireManager.getRoleIDWithTeamID().clear();
    }

    /**
     * 匹配中有玩家退出，踢出匹配队伍
     * @param groupid
     * @param roleID
     */
    private void playerLeaveInMatching(int groupid,long roleID){
        synchronized(obj){
            if ( Manager.worldBonfireManager.getMatchMembers().containsKey(groupid)){
                List<WorldBonfireMatch> list =  Manager.worldBonfireManager.getMatchMembers().get(groupid);
                for (WorldBonfireMatch worldBonfireMatch :list){
                    if (worldBonfireMatch.getRoleId() == roleID){
                        logger.info("playerLeaveInMatching  " + roleID);
                        list.remove(worldBonfireMatch);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void tickWorldBonfireMatch() {
        synchronized(obj){
            ConcurrentHashMap<Integer,List<WorldBonfireMatch>> listMatchMembers = Manager.worldBonfireManager.getMatchMembers();
            for (Integer serverGroupID : listMatchMembers.keySet()){
                List<WorldBonfireMatch> list =  listMatchMembers.get(serverGroupID);
                int size = list.size();
                if (size == 0) {
                    continue;
                }
                long now = TimeUtils.Time();
                int remain = size / 2;
                ConcurrentHashMap<Long, WorldBonfireTeam> teamList =
                        Manager.worldBonfireManager.getMatchTeam().get(serverGroupID);
                if (teamList == null){
                    teamList = new ConcurrentHashMap<>();
                    Manager.worldBonfireManager.getMatchTeam().put(serverGroupID,teamList);
                }
                for (int i = 0; i < remain; i++) {
                    WorldBonfireTeam team = new WorldBonfireTeam();
                    long id = IDConfigUtil.getId();
                    team.setTime(now);
                    team.setId(id);
                    WorldBonfireMatch match0 =  list.remove(0);
                    WorldBonfireMatch match1 =  list.remove(0);
                    team.getMms().add(match0);
                    team.getMms().add(match1);
                    teamList.put(id, team);
                    Manager.worldBonfireManager.getRoleIDWithTeamID().put(match0.getRoleId(),id);
                    Manager.worldBonfireManager.getRoleIDWithTeamID().put(match1.getRoleId(),id);
                    logger.info("匹配组:" + i + "成功" + team.toString());
                    resMatchList(team);
                }

                logger.info("匹配组剩余:" + list.size());

                if (list.size() == 0) {
                    continue;
                }

                if (now < list.get(0).getTime() + Global.World_Bonfire_Game_Robot_Time * 1000L) {
                    continue;
                }

                WorldBonfireTeam team = new WorldBonfireTeam();
                long id = IDConfigUtil.getId();
                team.setTime(now);
                team.setId(id);
                WorldBonfireMatch match3 =  list.remove(0);
                team.getMms().add(match3);

                WorldBonfireMatch mm = new WorldBonfireMatch();
                mm.setCarrer(Global.World_Bonfire_Game_Robot_model.get(0));
                mm.setFashionBodyId(Global.World_Bonfire_Game_Robot_model.get(1));
                team.getMms().add(mm);
                teamList.put(id, team);
                Manager.worldBonfireManager.getRoleIDWithTeamID().put(match3.getRoleId(),id);
                logger.info("匹配组机器人"+ "成功" + team.toString());
                resMatchList(team);
            }
        }
    }

    @Override
    public void tickWorldBonfireMatchTeam() {
        long now = TimeUtils.Time();
        Iterator iterator = Manager.worldBonfireManager.getMatchTeam().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer,ConcurrentHashMap< Long, WorldBonfireTeam>> entry =
                    (Map.Entry<Integer,ConcurrentHashMap< Long, WorldBonfireTeam>>) iterator.next();
            Iterator<Map.Entry<Long, WorldBonfireTeam>> teamList = entry.getValue().entrySet().iterator();
            while (teamList.hasNext()){
                WorldBonfireTeam team =  teamList.next().getValue();
                if (now >= team.getTime() + Global.World_Bonfire_Game_Round_Time * 1000L) {
                    for (WorldBonfireMatch player : team.getMms()) {
                        WorldBonfireMm mm = getWorldBonfireMm(player.getRoleId());
                        logger.error("team timeOut  " + team.getId());
                        if (mm != null) {
                            resFinger(mm, 2, true);
                        }
                    }
                    teamList.remove();
                }else {
                    if (tickFingerTimeOut(team ,now)){
                        teamList.remove();
                    }
                }
            }
        }
    }

    private boolean tickFingerTimeOut( WorldBonfireTeam team,long now){
        if (team.getFingerTime() >0){
            if (now > (team.getFingerTime() + 5000)){
                for (WorldBonfireMatch mt:team.getMms()){
                    WorldBonfireMm mm = getWorldBonfireMm(mt.getRoleId());
                    if (mm == null)
                        continue;
                    if (mt.getCurFinger() <= 0  && mt.getCurTotal() <= 0){
                        resFinger(mm, 2,true);
                        MessageUtils.notify_player(mm.getContext(), mm.getRoleId(), MessageString.WorldBonfireGameBreak2);
                    }else {
                        resFinger(mm, 1,false);
                        MessageUtils.notify_player(mm.getContext(), mm.getRoleId(), MessageString.WorldBonfireGameBreak1);
                    }
                }
                return true;
            }
        }
        return false;
    }

    private WorldBonfireMm getWorldBonfireMm(long roleID)
    {
        WorldBonfireMm mm = null;
        if ( Manager.worldBonfireManager.getRoleIDWithGroupID().containsKey(roleID)){
            int serverGroupId = Manager.worldBonfireManager.getRoleIDWithGroupID().get(roleID);
            ConcurrentHashMap<Long, WorldBonfireMm> groupJoingPlayerlsit =
                    Manager.worldBonfireManager.getJoinPlayers().get(serverGroupId);
            if (groupJoingPlayerlsit!=null){
                mm =  groupJoingPlayerlsit.get(roleID);
            }
        }
       return   mm;
    }

    @Override
    public void onWorldBonfireEnter(ChannelHandlerContext context, long roleId) {
        if (!Manager.dailyActiveManager.deal().isActiveOpen(DailyActiveDefine.ACTIVITY_WORLD_BONFIRE)) {
            MessageUtils.notify_player(context, roleId, MessageString.WorldAnswerAppError);
            //活动未开放
            return;
        }

        ServerInfo serverInfo = Manager.fightManager.deal().getMinServer();
        if (serverInfo == null) {
            //跨服未开放
            return;
        }
        long copyMapTime =   DailyActiveManager.getInstance().deal().getDailyNearlyEndTime(DailyActiveDefine.ACTIVITY_WORLD_BONFIRE);
        long endTime =  copyMapTime - TimeUtils.Time();
        long endTimeSecond = (copyMapTime - TimeUtils.Time())/1000;
        if (endTimeSecond <=0){
            MessageUtils.notify_player(context, roleId, MessageString.WorldAnswerAppError);
            return;
        }
        if (endTimeSecond <= DailyActiveManager.DailyLastTime){
            MessageUtils.notify_player(context, roleId, MessageString.DailyActiveTimeNotEngouh);
            return;
        }
        if (Manager.fightManager.deal().isMaxServer(serverInfo)) {

            logger.error("所有跨服人数已满");
            //人数已满
            return;
        }
        String platServerId = context.channel().attr(SessionKey.SERVERPLATID).get();
        int serverGroupId = ServerMatchManager.deal().getGroupIDForCurOpenDay(platServerId,DailyActiveDefine.ACTIVITY_WORLD_BONFIRE);
        if (serverGroupId < 0){
            logger.error("服务器世界等级不满足 未匹配到服务器");
            return;
        }
        //添加组
        ConcurrentHashMap<Long, WorldBonfireMm> groupJoingPlayerlsit =
                Manager.worldBonfireManager.getJoinPlayers().get(serverGroupId);
        if (groupJoingPlayerlsit == null){
            groupJoingPlayerlsit = new ConcurrentHashMap<>();
            Manager.worldBonfireManager.getJoinPlayers().put(serverGroupId,groupJoingPlayerlsit);
        }
        Manager.worldBonfireManager.getRoleIDWithGroupID().put(roleId,serverGroupId);
        //添加玩家
        WorldBonfireMm mm = groupJoingPlayerlsit.get(roleId);
        if (mm == null) {
            mm = new WorldBonfireMm();
            mm.setRoleId(roleId);
            mm.setContext(context);
            groupJoingPlayerlsit.put(roleId, mm);
        } else {
            mm.setContext(context);
        }

        //分配房间,找人数最多的房间
        FightRoom r = null;
        int maxNum = 0;
        for (FightRoom room : Manager.worldBonfireManager.getRooms()) {
            if (room.getServerGroupId() != serverGroupId)
                continue;
            if (room.hasPeoples() >= WorldBonfireManager.getLineMaxPeople()) {
                continue;
            }

            if (r == null || room.hasPeoples() > maxNum) {
               r = room;
               maxNum = room.hasPeoples();
            }
        }

        //创建zoneTeam
        ZoneTeam zt = new ZoneTeam();
        int serverId = context.channel().attr(SessionKey.SERVERID).get();
        zt.setsId(serverId);
        zt.setPlat(context.channel().attr(SessionKey.SERVERPLAT).get());

        //创建TeamPlayer
        TeamPlayerInfo tpi;
        tpi = new TeamPlayerInfo();
        tpi.setRoleId(roleId);
        tpi.setServerId(serverId);
        zt.getPlist().put(roleId, tpi);
        List<ZoneTeam> zoneTeams = new ArrayList<>();
        zoneTeams.add(zt);

        if (r == null) {
            Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.ACTIVITY_WORLD_BONFIRE);
            Cfg_Clone_map_Bean cloneBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(bean.getCloneID().get(0));
            r = Manager.fightManager.deal().createFightRoom(cloneBean, zoneTeams);
            r.setFightTime(endTime);
            Manager.worldBonfireManager.getRooms().add(r);
        }
        r.setServerId(serverInfo.getServerId());
        r.setpPlat(serverInfo.getPlatName());
        r.setServerGroupId(serverGroupId);
        r.setFightTime(endTime);
        Manager.fightManager.deal().fightStart(r, zoneTeams);
    }

    @Override
    public void onWorldBonfirePanel(ChannelHandlerContext context, long roleId,int gatherCount) {

       if (!Manager.worldBonfireManager.getRoleIDWithGroupID().containsKey(roleId)){
            return;
       }
       int serverGroupId = Manager.worldBonfireManager.getRoleIDWithGroupID().get(roleId);

        ConcurrentHashMap<Long, WorldBonfireMm> groupJoingPlayerlsit =
                Manager.worldBonfireManager.getJoinPlayers().get(serverGroupId);
        if (groupJoingPlayerlsit == null){
            return;
        }
        WorldBonfireMm mm = groupJoingPlayerlsit.get(roleId);
        if (mm == null) {
           return;
        }

        WorldBonfire worldBonfire = Manager.worldBonfireManager.getWorldBonfire().get(serverGroupId);
        if (worldBonfire == null)
            return;
        long createTime =worldBonfire.getBonFireCreateTime();
        long nowTime = TimeUtils.Time();
        long activeTime = WorldBonfireManager.getActiveTime();
        ResWorldBonfirePanel.Builder msg = ResWorldBonfirePanel.newBuilder();
        msg.setRemainTime((int) (activeTime - (nowTime - createTime)));
        msg.setGatherCount(gatherCount);
        if ((nowTime - createTime) < Global.World_Bonfire_Wood_time * 1000) {
            msg.setParam1(worldBonfire.getLv());
            msg.setParam2(worldBonfire.getExp());
        } else {
            msg.setParam1(mm.getWinNum());
            msg.setParam2(mm.getJoinNum());
            if (mm.isGet()) {
                msg.setParam3(1);
            } else {
                msg.setParam3(0);
            }
        }
        MessageUtils.send_to_player(context, roleId, ResWorldBonfirePanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onWorldBonfireCheckLevel(ChannelHandlerContext context, long roleId) {

        if (!Manager.worldBonfireManager.getRoleIDWithGroupID().containsKey(roleId)){
            return;
        }
        int serverGroupId = Manager.worldBonfireManager.getRoleIDWithGroupID().get(roleId);
       WorldBonfire worldBonfire =   Manager.worldBonfireManager.getWorldBonfire().get(serverGroupId);
       if (worldBonfire == null)
           return;

        int res = 1;
        int level =worldBonfire.getLv();
        long curTime = TimeUtils.Time();
        if (curTime - worldBonfire.getBonFireCreateTime() >= Global.World_Bonfire_Wood_time * 1000L) {
            res = 0;
        }

        if (res != 0) {
            Cfg_World_bonfire_Bean nextBean = CfgManager.getCfg_World_bonfire_Container().getValueByKey(level + 1);
            if (nextBean == null) {
                res = 0;
            }
        }

        if (res == 0) {
            return;
        }

        P2GWorldBonfireAddWoodCheckRes.Builder resCheck = P2GWorldBonfireAddWoodCheckRes.newBuilder();
        resCheck.setLv(level);
        resCheck.setRoleId(roleId);
        MessageUtils.send_to_game(context, P2GWorldBonfireAddWoodCheckRes.MsgID.eMsgID_VALUE, resCheck.build().toByteArray());
    }

    @Override
    public void onWorldBonfireLevel(WorldBonfireMessage.F2PWorldBonfireAddWood messInfo) {
        if (!Manager.worldBonfireManager.getRoleIDWithGroupID().containsKey(messInfo.getRoleID())){
            return;
        }
        int serverGroupId = Manager.worldBonfireManager.getRoleIDWithGroupID().get(messInfo.getRoleID());
        Cfg_World_bonfire_Bean bean = CfgManager.getCfg_World_bonfire_Container().getValueByKey(messInfo.getLv());
        WorldBonfire worldBonfire = Manager.worldBonfireManager.getWorldBonfire().get(serverGroupId);
        if (worldBonfire == null)
            return;
        int addExp = worldBonfire.getExp() + Global.World_Bonfire_Wood_cost.get(2);
        if (addExp >= bean.getLevel_exp()) {
            worldBonfire.setExp(0);
            worldBonfire.setLv(messInfo.getLv() + 1);
        } else {
            worldBonfire.setExp(addExp);
        }
        List<FightRoom> rooms =  getRoomList( serverGroupId);
        if (rooms.size() == 0)
            return;

        for (FightRoom room:rooms){
            ChannelHandlerContext session =
                    GameServerManager.getInstance().GetSession(room.getPlat(), room.getServerId());
            if (session==null)
                continue;

            P2FWorldBonfireAddWoodLv.Builder builder = P2FWorldBonfireAddWoodLv.newBuilder();
            builder.setExp(worldBonfire.getExp());
            builder.setLv(worldBonfire.getLv());
            builder.setMapID(room.getFid());
            builder.setName(messInfo.getName());
            MessageUtils.send_to_game(session,
                    P2FWorldBonfireAddWoodLv.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
    }

    private List<FightRoom> getRoomList(int serverGroupId){
        List<FightRoom> rooms = new ArrayList<>();
        for (FightRoom room : Manager.worldBonfireManager.getRooms()) {
            if (room.getServerGroupId() == serverGroupId){
                rooms.add(room);
            }
        }
        return rooms;
    }


    @Override
    public void onWorldBonfireMatch(ChannelHandlerContext context, G2PWorldBonfireMatch messInfo) {

        if (!Manager.dailyActiveManager.deal().isActiveOpen(DailyActiveDefine.ACTIVITY_WORLD_BONFIRE)) {
            return;
        }

        if (!Manager.worldBonfireManager.getRoleIDWithGroupID().containsKey( messInfo.getMember().getRoleId())){
            return;
        }
        int serverGroupId = Manager.worldBonfireManager.getRoleIDWithGroupID().get( messInfo.getMember().getRoleId());


        List<WorldBonfireMatch> groupWorldMatchLsit =
                Manager.worldBonfireManager.getMatchMembers().get(serverGroupId);
        if (groupWorldMatchLsit == null){
            groupWorldMatchLsit = new ArrayList<>();
            Manager.worldBonfireManager.getMatchMembers().put(serverGroupId, groupWorldMatchLsit);
        }

        WorldBonfire worldBonfire = Manager.worldBonfireManager.getWorldBonfire().get(serverGroupId);
        if (worldBonfire == null)
            return;

        long curTime = TimeUtils.Time();
        if (curTime - worldBonfire.getBonFireCreateTime() < Global.World_Bonfire_Wood_time * 1000L) {
            return;
        }

        for (Iterator<WorldBonfireMatch> it = groupWorldMatchLsit.iterator(); it.hasNext();) {
            WorldBonfireMatch match = it.next();
            if (messInfo.getMember().getRoleId() == match.getRoleId()) {
                match.setRemainWine(0);
                return;
            }
        }

        synchronized(obj){
            List<WorldBonfireMatch> list = groupWorldMatchLsit;
            WorldBonfireMatch mm = new WorldBonfireMatch();
            mm.setFashionBodyId(messInfo.getMember().getFacade().getFashionBody());
            mm.setFashionWeaponId(messInfo.getMember().getFacade().getFashionWeapon());
            mm.setFashionHalo(messInfo.getMember().getFacade().getFashionHalo());
            mm.setFashionMatrix(messInfo.getMember().getFacade().getFashionMatrix());
            mm.setSpiritId(messInfo.getMember().getFacade().getSpiritId());
            mm.setRoleId(messInfo.getMember().getRoleId());
            mm.setName(messInfo.getMember().getName());
            mm.setStateLv(messInfo.getMember().getStateLv());
            mm.setWingId(messInfo.getMember().getFacade().getWingId());
            mm.setCarrer(messInfo.getMember().getCarrer());
            mm.setTime(TimeUtils.Time());
            mm.setContext(context);
            list.add(mm);
        }
    }

    @Override
    public void onWorldBonfireFinger( G2PWorldBonfireFinger messInfo) {
        logger.info("onWorldBonfireFinger begin roleId======:" + messInfo.getRoleId());
        if (!Manager.worldBonfireManager.getRoleIDWithGroupID().containsKey(messInfo.getRoleId())){
            logger.info("getRoleIDWithGroupID   :  null  " + messInfo.getRoleId());
            return;
        }
        int serverGroupId = Manager.worldBonfireManager.getRoleIDWithGroupID().get(messInfo.getRoleId());
        ConcurrentHashMap<Long, WorldBonfireMm> groupJoingPlayerlsit =
                Manager.worldBonfireManager.getJoinPlayers().get(serverGroupId);
        if (groupJoingPlayerlsit == null){
            logger.error("serverGroupId  玩家列表为空"  + serverGroupId);
            return;
        }
        if (!Manager.worldBonfireManager.getMatchTeam().containsKey(serverGroupId)){
            logger.error("serverGroupId  队伍列表为空"  + serverGroupId);
            return;
        }
        ConcurrentHashMap<Long, WorldBonfireTeam> teamlist = Manager.worldBonfireManager.getMatchTeam().get(serverGroupId);
        long teamId = messInfo.getTeamId();
        WorldBonfireTeam team = teamlist.get(teamId);
        if (team == null) {
            logger.info("team======:  null  " + messInfo.getRoleId());
            return;
        }

        boolean isCalRes = true;
        for (WorldBonfireMatch mm : team.getMms()) {
            if (mm.getRoleId() == messInfo.getRoleId()) {
                mm.setCurTotal(messInfo.getTotal());
                mm.setCurFinger(messInfo.getType());
                continue;
            }

            if (mm.getRoleId() == 0) {
                List<Integer> list = getRobotFinger(team.getCurNum() + 1);
                mm.setCurTotal(list.get(0));
                mm.setCurFinger(list.get(1));
                continue;
            }

            if (mm.getRoleId() != 0 && mm.getCurTotal() < 0 && mm.getCurFinger() < 0) {
                isCalRes = false;

            }
        }

        if (isCalRes == false) {
            logger.info("isCalRes     false  " + messInfo.getRoleId());
            team.setFingerTime(TimeUtils.Time());
            return;
        }
        logger.info("onWorldBonfireFinger end roleId======:" + messInfo.getRoleId());

        WorldBonfireMatch bm1 = team.getMms().get(0);
        WorldBonfireMatch bm2 = team.getMms().get(1);
        onceFinger(bm1, bm2, team);
        bm1.setCurFinger(-1);
        bm1.setCurTotal(-1);
        bm2.setCurFinger(-1);
        bm2.setCurTotal(-1);
        team.setCurNum(team.getCurNum() + 1);
        team.setFingerTime(0);

        allFinger(groupJoingPlayerlsit,bm1, bm2, teamId,teamlist);
    }

    @Override
    public void onWorldBonfireFingerLeave(long teamId, long roleId) {

        if (!Manager.worldBonfireManager.getRoleIDWithGroupID().containsKey(roleId)){
            return;
        }
        int serverGroupId = Manager.worldBonfireManager.getRoleIDWithGroupID().get(roleId);
        ConcurrentHashMap<Long, WorldBonfireMm> groupJoingPlayerlsit =
                Manager.worldBonfireManager.getJoinPlayers().get(serverGroupId);
        if (groupJoingPlayerlsit == null){
            logger.error("serverGroupId  玩家列表为空"  + serverGroupId);
            return;
        }
        if (!Manager.worldBonfireManager.getMatchTeam().containsKey(serverGroupId)){
            logger.error("serverGroupId  队伍列表为空"  + serverGroupId);
            return;
        }
        ConcurrentHashMap<Long, WorldBonfireTeam> teamlist = Manager.worldBonfireManager.getMatchTeam().get(serverGroupId);

        if (teamId<=0){
            if ( Manager.worldBonfireManager.getRoleIDWithTeamID().containsKey(roleId)) {
                teamId =  Manager.worldBonfireManager.getRoleIDWithTeamID().get(roleId);
            }
            playerLeaveInMatching(serverGroupId,roleId);
        }
        WorldBonfireTeam team = teamlist.get(teamId);
        if (team == null) {
            return;
        }

        teamlist.remove(teamId);
        for (WorldBonfireMatch match : team.getMms()) {
            if (match.getRoleId() == roleId) {
                WorldBonfireMm wm = groupJoingPlayerlsit.get(roleId);
                wm.setJoinNum(wm.getJoinNum() + 1);
                resFinger(wm, 2,true);
            }

            if (match.getRoleId() != roleId && match.getRoleId() != 0) {
                WorldBonfireMm wm = groupJoingPlayerlsit.get(match.getRoleId());
                wm.setJoinNum(wm.getJoinNum() + 1);
                wm.setWinNum(wm.getWinNum() + 1);
                resFinger(wm, 1,true);
            }
        }
    }

    @Override
    public void onWorldBonfireReward(ChannelHandlerContext context, long roleId) {

        String platServerId = context.channel().attr(SessionKey.SERVERPLATID).get();
        int serverGroupId = ServerMatchManager.deal().
                getGroupIDForCurOpenDay(platServerId,DailyActiveDefine.ACTIVITY_WORLD_BONFIRE);
        if (serverGroupId < 0){
            logger.error("服务器世界等级不满足 未匹配到服务器");
            return;
        }
        ConcurrentHashMap<Long, WorldBonfireMm> groupJoingPlayerlsit =
                Manager.worldBonfireManager.getJoinPlayers().get(serverGroupId);
        if (groupJoingPlayerlsit == null){
            return;
        }
        WorldBonfireMm mm = groupJoingPlayerlsit.get(roleId);
        if (mm.isGet()) {
            return;
        }
        if (mm.getWinNum() == Global.World_Bonfire_Game_Aim.get(0) ||
                mm.getJoinNum() == Global.World_Bonfire_Game_Aim.get(1)) {
            mm.setGet(true);
            P2GWorldBonfireReward.Builder builder = P2GWorldBonfireReward.newBuilder();
            builder.setRoleId(roleId);
            MessageUtils.send_to_game(context, P2GWorldBonfireReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
    }

    @Override
    public void onWorldBonfireCancelMatch(ChannelHandlerContext context, long roleId) {
        if (!Manager.worldBonfireManager.getRoleIDWithGroupID().containsKey(roleId)){
            return;
        }
        int serverGroupId = Manager.worldBonfireManager.getRoleIDWithGroupID().get(roleId);
       List<WorldBonfireMatch>  matchList =   Manager.worldBonfireManager.getMatchMembers().get(serverGroupId);
       if (matchList == null)
           return;

        boolean isSucess = false;
        for (Iterator<WorldBonfireMatch> it = matchList.iterator(); it.hasNext();) {
            WorldBonfireMatch match = it.next();
            if (roleId == match.getRoleId()) {
                isSucess = true;
                it.remove();
                break;
            }
        }

        ResWorldBonfireCancelMatch.Builder builder = ResWorldBonfireCancelMatch.newBuilder();
        if (isSucess) {
            builder.setRes(1);
        } else {
            builder.setRes(2);
        }
        MessageUtils.send_to_player(context, roleId, ResWorldBonfireCancelMatch.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private void onceFinger(WorldBonfireMatch bm1, WorldBonfireMatch bm2, WorldBonfireTeam team) {
        int fristTotal = bm1.getCurTotal();
        int fristFinger = bm1.getCurFinger();
        int secondTotal = bm2.getCurTotal();
        int secondFinger = bm2.getCurFinger();
        int curTotal = secondFinger + fristFinger;


        if (curTotal == fristTotal   && secondTotal != curTotal) {
            bm1.setLastRes(WorldBonfireManager.WIN);
            bm2.setLastRes(WorldBonfireManager.FAIL);
            bm2.setRemainWine(bm2.getRemainWine() + 1);
        } else if (curTotal == secondTotal  && fristTotal != curTotal) {
            bm1.setLastRes(WorldBonfireManager.FAIL);
            bm2.setLastRes(WorldBonfireManager.WIN);
            bm1.setRemainWine(bm1.getRemainWine() + 1);
        } else {
            if (bm1.getLastRes() == WorldBonfireManager.DRAW && bm2.getLastRes() == WorldBonfireManager.DRAW) {
                bm1.setLastRes(WorldBonfireManager.FAIL);
                bm2.setLastRes(WorldBonfireManager.FAIL);
                bm1.setRemainWine(bm1.getRemainWine() + 1);
                bm2.setRemainWine(bm2.getRemainWine() + 1);
            } else {
                bm1.setLastRes(WorldBonfireManager.DRAW);
                bm2.setLastRes(WorldBonfireManager.DRAW);
            }
        }

        ResWorldBonfireFinger.Builder builder = ResWorldBonfireFinger.newBuilder();
        builder.addFingers(getFinger(bm1));
        builder.addFingers(getFinger(bm2));

        for (WorldBonfireMatch mm : team.getMms()) {
            if (mm.getRoleId() != 0) {
                MessageUtils.send_to_player(mm.getContext(), mm.getRoleId(),
                        ResWorldBonfireFinger.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            }
        }
    }

    private void allFinger( ConcurrentHashMap<Long, WorldBonfireMm> groupJoingPlayerlsit,
                            WorldBonfireMatch bm1, WorldBonfireMatch bm2, long teamId,
                            ConcurrentHashMap<Long, WorldBonfireTeam> teamlist) {
        WorldBonfireMm worldBonfireMm1 = groupJoingPlayerlsit.get(bm1.getRoleId());
        WorldBonfireMm worldBonfireMm2 =groupJoingPlayerlsit.get(bm2.getRoleId());
        if (bm1.getRemainWine() != Global.World_Bonfire_Game_Hp && bm2.getRemainWine() != Global.World_Bonfire_Game_Hp) {
            return;
        }

        int bm1Res;
        int bm2Res;
        if (bm1.getRemainWine() == Global.World_Bonfire_Game_Hp && bm2.getRemainWine() != Global.World_Bonfire_Game_Hp) {
            if (worldBonfireMm2 != null) {
                worldBonfireMm2.setWinNum(worldBonfireMm2.getWinNum() + 1);
            }
            bm1Res = 2;
            bm2Res = 1;
        } else if (bm2.getRemainWine() == Global.World_Bonfire_Game_Hp && bm1.getRemainWine() != Global.World_Bonfire_Game_Hp) {
            if (worldBonfireMm1 != null) {
                worldBonfireMm1.setWinNum(worldBonfireMm1.getWinNum() + 1);
            }
            bm1Res = 1;
            bm2Res = 2;
        } else {
            bm1Res = 3;
            bm2Res = 3;
        }

        if (worldBonfireMm1 != null) {
            worldBonfireMm1.setJoinNum(worldBonfireMm1.getJoinNum() + 1);

            resFinger(worldBonfireMm1, bm1Res,false);
        }

        if (worldBonfireMm2 != null) {
            worldBonfireMm2.setJoinNum(worldBonfireMm2.getJoinNum() + 1);

            resFinger(worldBonfireMm2, bm2Res,false);
        }

        teamlist.remove(teamId);
    }


    private void resMatchList(WorldBonfireTeam team) {
        ResWorldBonfireMatchList.Builder builder = ResWorldBonfireMatchList.newBuilder();
        for (WorldBonfireMatch mm : team.getMms()) {
            WorldBonfireMember.Builder builderMM = WorldBonfireMember.newBuilder();
            builderMM.setRoleId(mm.getRoleId());
            builderMM.setName(mm.getName());
            builderMM.setStateLv(mm.getStateLv());
            builderMM.setCarrer(mm.getCarrer());
            builderMM.setFacade(getFacade(mm.getWingId(), mm.getFashionBodyId(), mm.getFashionHalo(), mm.getFashionMatrix(), mm.getFashionWeaponId(), mm.getSpiritId()));
            builderMM.setRemainWine(0);
            builder.addMembers(builderMM);
        }
        builder.setTeamId(team.getId());
        for (WorldBonfireMatch mm : team.getMms()) {
            if (mm.getRoleId() == 0) {
                continue;
            }
            MessageUtils.send_to_player(mm.getContext(), mm.getRoleId(),
                    ResWorldBonfireMatchList.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
    }

    private void resFinger(WorldBonfireMm mm, int res,boolean isLeave) {
        ResWorldBonfireAllFinger.Builder builder = ResWorldBonfireAllFinger.newBuilder();
        builder.setRes(res);
        builder.setJoinNum(mm.getJoinNum());
        builder.setWinNum(mm.getWinNum());
        builder.setIsLeave(isLeave);
        MessageUtils.send_to_player(mm.getContext(), mm.getRoleId(),
                ResWorldBonfireAllFinger.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private List<Integer> getRobotFinger(int num) {
        List<Integer> list = new ArrayList<>();
        if (num == 1) {
            list.add(0);
            list.add(0);
        } else if(num == 2) {
            list.add(5);
            list.add(0);
        } else if (num == 3) {
            list.add(5);
            list.add(5);
        } else if (num == 4) {
            list.add(10);
            list.add(0);
        } else if (num == 5) {
            list.add(10);
            list.add(5);
        } else {
            list.add(10);
            list.add(10);
        }
        return list;
    }

    private Finger.Builder getFinger(WorldBonfireMatch mm) {
        Finger.Builder builder = Finger.newBuilder();
        builder.setRes(mm.getLastRes());
        builder.setRoleId(mm.getRoleId());
        builder.setTotal(mm.getCurTotal());
        builder.setType(mm.getCurFinger());
        return builder;
    }

    private CommonMessage.FacadeAttribute.Builder getFacade(int windId, int bodyId, int halo, int matrix, int weaponId, int spiritId) {
        CommonMessage.FacadeAttribute.Builder msg = CommonMessage.FacadeAttribute.newBuilder();
        msg.setWingId(windId);
        msg.setFashionBody(bodyId);
        msg.setFashionHalo(halo);
        msg.setFashionMatrix(matrix);
        msg.setFashionWeapon(weaponId);
        msg.setSpiritId(spiritId);
        return msg;
    }

    @Override
    public int getId() {
        return ScriptEnum.WorldBonfireBaseScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
}
