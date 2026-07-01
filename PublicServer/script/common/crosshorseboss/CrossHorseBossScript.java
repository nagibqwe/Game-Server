package common.crosshorseboss;

import com.data.CfgManager;
import com.data.MessageString;
import com.data.bean.Cfg_Bossnew_HorseBoss_Bean;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Daily_Bean;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.game.crosshorseboss.scripts.ICrossHorseBoss;
import com.game.crosshorseboss.structs.CrosshorseBossData;
import com.game.crosshorseboss.timer.CrosssHorseBossTimer;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.fightroom.manager.FightManager;
import com.game.fightroom.structs.FightRoom;
import com.game.fightroom.structs.FightRoomState;
import com.game.gameserver.manager.GameServerManager;
import com.game.gameserver.structs.ServerInfo;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.server.MainServer;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.soulanimalforest.struct.BossHaveFollow;
import com.game.structs.SessionKey;
import com.game.utils.MessageUtils;
import com.game.zone.structs.TeamPlayerInfo;
import com.game.zone.structs.ZoneTeam;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.CrossFightMessage;
import game.message.CrossHorseBossMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cxl on 2021/4/14.
 */
public class CrossHorseBossScript implements ICrossHorseBoss {

    private static final Logger logger = LogManager.getLogger(CrossHorseBossScript.class);
    @Override
    public int getId() {
        return ScriptEnum.CrossHorseBossScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     *初始化
     */
    @Override
    public void initData() {
        List<Integer> allGroupList =  ServerMatchManager.deal().getAllReachMatchGroupIDList(DailyActiveDefine.CrossHorseBoss);
        Manager.crossHorseBossManager.setAllGroupList(allGroupList);
        for (Integer groupId : allGroupList){
            initBossData(groupId);
        }
        MainServer.getInstance().addTimerEvent(new CrosssHorseBossTimer(-1, 0l,300));
    }
    private void initBossData(int groupId){
        ConcurrentHashMap<Integer, CrosshorseBossData> integerCrosshorseBossDataHashMap = new ConcurrentHashMap<>();
        for (Cfg_Bossnew_HorseBoss_Bean bean :CfgManager.getCfg_Bossnew_HorseBoss_Container().getValuees()){
            CrosshorseBossData crosshorseBossData = new CrosshorseBossData();
            crosshorseBossData.setConfigId(bean.getID());
            crosshorseBossData.setCloneId(bean.getCloneid());
            crosshorseBossData.setDie(true);
            crosshorseBossData.setReBornBaseTime(bean.getInitial_time());
            crosshorseBossData.setRoomId(0);
            crosshorseBossData.setLevel(bean.getLayer());
            crosshorseBossData.setMonsterId(bean.getMonsterid());
            crosshorseBossData.setRebornTime(0);
            crosshorseBossData.setFightServerId(0);
            crosshorseBossData.setFlushFollow(true);
            integerCrosshorseBossDataHashMap.put(bean.getID(),crosshorseBossData);
        }
        Manager.crossHorseBossManager.getAllHosrseBossMap().put(groupId, integerCrosshorseBossDataHashMap);
    }

    /**
     *请求关注boss
     */
    public void onG2PReqFollowCrossHorseBoss(ChannelHandlerContext context,CrossHorseBossMessage.G2PReqFollowCrossHorseBoss messInfo){
        long roleId = messInfo.getRoleId();
        int configId = messInfo.getBossId();
        boolean isFollow = messInfo.getFollowValue();
        String plat = context.channel().attr(SessionKey.SERVERPLAT).get();
        int serverid = context.channel().attr(SessionKey.SERVERID).get();
        ConcurrentHashMap<Long, BossHaveFollow> flist = Manager.crossHorseBossManager.getBossFollowList().get(configId);


        if (!isFollow && flist == null) {
            sendFollowResult(context, messInfo, 2);
            return;
        }
        boolean save = false;
        if (flist != null) {
            if (flist.containsKey(messInfo.getRoleId())) {
                if (!isFollow) {
                    save = true;
                    flist.remove(roleId);
                } else {
                    sendFollowResult(context, messInfo, 1);
                    return;
                }
            } else {
                if (isFollow) {
                    BossHaveFollow bhf = new BossHaveFollow();
                    bhf.setOs("");
                    bhf.setPlat(plat);
                    bhf.setSid(serverid);
                    flist.put(roleId, bhf);
                    save = true;
                } else {
                    sendFollowResult(context, messInfo, 1);
                    return;
                }
            }
        } else {
            if (isFollow) {
                flist = new ConcurrentHashMap<>();
                ConcurrentHashMap<Long, BossHaveFollow> temp =  Manager.crossHorseBossManager.getBossFollowList().putIfAbsent(configId, flist);
                if (temp != null) {
                    flist = temp;
                }
                BossHaveFollow bhf = new BossHaveFollow();
                bhf.setOs("");
                bhf.setPlat(plat);
                bhf.setSid(serverid);
                flist.put(roleId, bhf);
                save = true;
            } else {
                sendFollowResult(context, messInfo, 1);
                return;
            }
        }

        if (save) {
            sendFollowResult(context, messInfo, 0);
        } else {
            sendFollowResult(context, messInfo, 3);
        }



    }
    private void sendFollowResult(ChannelHandlerContext session, CrossHorseBossMessage.G2PReqFollowCrossHorseBoss messInfo, int state) {
        CrossHorseBossMessage.ResFollowCrossHorseBoss.Builder msg = CrossHorseBossMessage.ResFollowCrossHorseBoss.newBuilder();
        msg.setBossId(messInfo.getBossId());
        msg.setFollowValue(messInfo.getFollowValue());
        msg.setState(state);
        MessageUtils.send_to_player(session, messInfo.getRoleId(),  CrossHorseBossMessage.ResFollowCrossHorseBoss.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 请求面板信息
     * @param context
     * @param msg
     */
    public void onG2PReqCrossHorseBossPanel(ChannelHandlerContext context,CrossHorseBossMessage.G2PReqCrossHorseBossPanel msg){
        long roleId     = msg.getRoleId();
        int maxCount    = msg.getMaxCount();
        int level       = msg.getLevel();
        int remainCount = msg.getRemainCount();
        String serverKey = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupId =  ServerMatchManager.deal().getGroupIDForCurOpenDay(serverKey,DailyActiveDefine.CrossHorseBoss);
        if (groupId <=0){
            logger.error("跨服坐骑BOSS 没有达成的 跨服组ID {}" ,groupId );
            return;
        }
        if (! Manager.crossHorseBossManager.getAllHosrseBossMap().containsKey(groupId)){
            logger.error(" 没找到该 组id  {}" ,groupId );
            return;
        }
        ConcurrentHashMap<Integer, CrosshorseBossData> cfgMap =  Manager.crossHorseBossManager.getAllHosrseBossMap().get(groupId);


        CrossHorseBossMessage.ResCrossHorseBossPanel.Builder buildermsg = CrossHorseBossMessage.ResCrossHorseBossPanel.newBuilder();
        CrossHorseBossMessage.HorseBossInfo.Builder horseBossInfo  = null;
        for (CrosshorseBossData crossHorseBossScript:cfgMap.values()){
            if ( crossHorseBossScript.getLevel() == level){
                horseBossInfo = CrossHorseBossMessage.HorseBossInfo.newBuilder();
                horseBossInfo.setBossId(crossHorseBossScript.getConfigId());
                long nowTime  =  TimeUtils.Time();
                long lastTime =  crossHorseBossScript.getRebornTime() > nowTime ? (crossHorseBossScript.getRebornTime() - nowTime) : 0;
                int refreshTime = lastTime  > 0 ? (int)(lastTime/1000):0;
                horseBossInfo.setRefreshTime(refreshTime);
                horseBossInfo.setIsFollowed(false);
                ConcurrentHashMap<Long, BossHaveFollow> flist =  Manager.crossHorseBossManager.getBossFollowList().get(crossHorseBossScript.getConfigId());
                if (flist != null) {
                    if (flist.containsKey(roleId)) {
                        horseBossInfo.setIsFollowed(true);
                    }
                }
                buildermsg.addBossList(horseBossInfo.build());
            }
        }
        buildermsg.setLevel(level);
        buildermsg.setMaxCount(maxCount);
        buildermsg.setRemainCount(remainCount);
        MessageUtils.send_to_player(context, roleId,  CrossHorseBossMessage.ResCrossHorseBossPanel.MsgID.eMsgID_VALUE, buildermsg.build().toByteArray());
    }


    /**
     * 请求进入副本
     * @param context
     * @param msg
     */
    @Override
    public void onG2PReqEnterHorseBoss(ChannelHandlerContext context, CrossHorseBossMessage.G2PReqEnterHorseBoss msg) {
        long roleId = msg.getPlayerId();
        int cfgId = msg.getCfgID();
        String serverKey = context.channel().attr(SessionKey.SERVERPLATID).get();
        String plat = context.channel().attr(SessionKey.SERVERPLAT).get();
        int serverid = context.channel().attr(SessionKey.SERVERID).get();
        int groupId =  ServerMatchManager.deal().getGroupIDForCurOpenDay(serverKey,DailyActiveDefine.CrossHorseBoss);
        if (groupId <=0){
            logger.error("跨服坐骑BOSS 没有达成的 跨服组ID {}" ,groupId );
            return;
        }
        if (!Manager.crossHorseBossManager.getAllHosrseBossMap().containsKey(groupId)){
            logger.error(" 组id没找到  {}" ,groupId );
            return;
        }
        ConcurrentHashMap<Integer, CrosshorseBossData> cfgMap =  Manager.crossHorseBossManager.getAllHosrseBossMap().get(groupId);
        CrosshorseBossData bossData =  cfgMap.get(cfgId);
        if (bossData == null){
            logger.error(" horseBoss配置 == null{}" ,cfgId );
            return;
        }
        FightRoom room = null;
        if (bossData.getRoomId() <= 0){
            if (bossData.isDie()  && TimeUtils.Time() < bossData.getRebornTime() ){
                logger.error("没到刷新Boss 时间 nowTime  {} " ,TimeUtils.Time(),bossData.getRebornTime());
                MessageUtils.notify_player(context,roleId, MessageString.HorseBossNotExit);
                return;
            }
            room =  createRoom(bossData,groupId , 0);
            if (room == null){
                return;
            }
            bossData.setRoomId(room.getFid());
            bossData.setDie(false);
        }else {
            room =  FightManager.getInstance().getFrcache().get(bossData.getRoomId());
            if (room == null){
                room =  createRoom(bossData,groupId , bossData.getRoomId());
                if (room == null){
                    return;
                }
                bossData.setRoomId(room.getFid());
                bossData.setDie(false);
            }else if (room.getRstate() <= FightRoomState.READYROOM  && room.getRstate() >= FightRoomState.FIGHTEND){
                logger.error("房间还在准备状态中 {} " ,room.getFid());
                return;
            }
        }
        if (room.hasPeoples() >= getLineMaxPeople(cfgId)) {
            logger.error("房间人数已经满 " ,room.getFid());
            MessageUtils.notify_player(context,roleId, MessageString.Activityp_Is_Full);
            return;
        }
        fightStart(room,roleId,plat,serverid,cfgId);
        logger.error("玩家 playerId {} 进入坐骑跨服Boss " ,roleId);

    }

    private void fightStart(FightRoom room, long roleID,String plat,int sid,int cfgId){

        long lenTime = room.getCtime() + room.getFightTime() + 2 * 60 * 1000;
        long lastTime = lenTime  > TimeUtils.Time()  ?(lenTime  - TimeUtils.Time()) : 0;
        checkZoneTeamInfo( room,  roleID, plat, sid);
        CrossFightMessage.P2GResFightStart.Builder msg = CrossFightMessage.P2GResFightStart.newBuilder();
        msg.setFightId(room.getFid());
        msg.setZoneModelId(room.getModelId());
        msg.setFightServerId(room.getServerId());
        CrossFightMessage.roleAtt.Builder mRole = CrossFightMessage.roleAtt.newBuilder();
        mRole.setCampNo(sid);
        mRole.setRoleId(roleID);
        msg.addRoleInfo(mRole);
        msg.setMapModelId(room.getMapmodelId());
        room.setHaveNum(room.getHaveNum() +1);

        //添加额外传送到战斗服信息
        List<CommonMessage.CrossAttribute> result = new ArrayList<>();
        CommonMessage.CrossAttribute.Builder info = CommonMessage.CrossAttribute.newBuilder();
        info.setType(room.getServerGroupId());
        info.setValue(lastTime);
        info.setParam1(cfgId);
        result.add(info.build());
        msg.addAllMapSetList(result);
        ChannelHandlerContext socket = Manager.gameServerManager.GetSession(plat, sid);
        MessageUtils.send_to_game(socket, CrossFightMessage.P2GResFightStart.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }

    private void checkZoneTeamInfo(FightRoom room, long roleID,String plat,int sid) {
        boolean isInZoneTeam = false;
        for (ZoneTeam z:room.getTeam()) {
            if (z.getsId() == sid) {
                if (!z.getPlist().containsKey(roleID)) {
                    TeamPlayerInfo teamInfo = new TeamPlayerInfo();
                    teamInfo.setCampNo(sid);
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
            zoneTeam.setCampNo(sid);
            TeamPlayerInfo teamInfo = new TeamPlayerInfo();
            teamInfo.setCampNo(sid);
            teamInfo.setRoleId(roleID);
            zoneTeam.getPlist().put(roleID,teamInfo);
            room.getTeam().add(zoneTeam);
        }
    }


    private FightRoom createRoom(CrosshorseBossData bossData,int groupid,long fightId){
        ServerInfo fightserver = Manager.fightManager.deal().getFightServerId(0);
        if (fightserver == null){
            logger.error("没有收到有战斗服的注册信息！");
            return null;
        }
        Cfg_Clone_map_Bean cloneMapCfg = CfgManager.getCfg_Clone_map_Container().getValueByKey(bossData.getCloneId());
        if (cloneMapCfg == null){
            logger.error("Cfg_Clone_map_Bean  is nul {}", bossData.getCloneId());
            return null;
        }
        bossData.setRebornTime(0);
        long fightMapID = fightId > 0? fightId: IDConfigUtil.getLogId();
        FightRoom room = new FightRoom();
        room.setModelId( bossData.getCloneId());
        room.setServerGroupId(groupid);
        room.setFid(fightMapID);
        room.setCtime(TimeUtils.Time());
        room.setAttackValue(0);
        room.setServerId(fightserver.getServerId());
        room.setpPlat(fightserver.getPlatName());
        room.setAllReadyStart(true);
        room.setRstate(FightRoomState.CREATEROOM);
        room.setFightTime(cloneMapCfg.getExist_time() + cloneMapCfg.getEnter_time());
        FightManager.getInstance().SaveRoomInfo(room, fightserver.getPlatName(), fightserver.getServerId());//保存并且写log
        return room;
    }


    /**
     * 怪物死亡
     * @param context
     * @param msg
     */
    public  void onF2PReqMonsterDie(ChannelHandlerContext context,CrossHorseBossMessage.F2PReqCrossHorseBossDie msg){
        int cfgId   =  msg.getModelConfigId();
        int groupId =  msg.getGroupId();
        ConcurrentHashMap<Integer, CrosshorseBossData> cfgMap =  Manager.crossHorseBossManager.getAllHosrseBossMap().get(groupId);
        CrosshorseBossData bossData =  cfgMap.get(cfgId);
        if (bossData == null){
            logger.error("没找到 horseBoss 配置 {}" ,cfgId );
            return;
        }
        if (bossData.getRebornTime() > 0){
            logger.error("怪物死亡信息已同步"  );
            return;
        }
        bossData.setDie(true);
        bossData.setRebornTime(TimeUtils.Time() + bossData.getReBornBaseTime() * 1000);
        bossData.setRoomId(0);
        bossData.setFlushFollow(false);
        logger.error("怪物死亡信息已同步"  );
    }

    @Override
    public void tick() {
            long curTime = TimeUtils.Time();
            for (Integer groupId :  Manager.crossHorseBossManager.getAllGroupList()){
                if (!Manager.crossHorseBossManager.getAllHosrseBossMap().containsKey(groupId)){
                    initBossData(groupId);
                }else {
                    tickGroupData(groupId,curTime);
                }
            }
    }

    private void tickGroupData(int groupId,long curTime){
        ConcurrentHashMap<Integer, CrosshorseBossData> groupList =  Manager.crossHorseBossManager.getAllHosrseBossMap().get(groupId);
        for (Integer cfgId : groupList.keySet()) {
            CrosshorseBossData bossData =  groupList.get(cfgId);
            if (bossData.getRebornTime() <= 0){
                continue;
            }
            //boss刷新前一分钟通知玩家
            if (curTime < bossData.getRebornTime() && !bossData.isFlushFollow()) {
                int betweenTime = (int) ((bossData.getRebornTime() - curTime) / 1000); //取秒
                if (betweenTime <= 60) {
                    sendHorseBossRefreshTip(cfgId);
                    bossData.setFlushFollow(true); //借用此字段表示刷新提示已通知过了
                }
            }
        }
    }
    private void sendHorseBossRefreshTip(int bossConfigId) {
        ConcurrentHashMap<Long, BossHaveFollow> flist = Manager.crossHorseBossManager.getBossFollowList().get(bossConfigId);

        if (flist == null) {
            return;
        }
        CrossHorseBossMessage.P2GResCrossHorseBossRefreshTip.Builder msg = CrossHorseBossMessage.P2GResCrossHorseBossRefreshTip.newBuilder();
        Iterator<Map.Entry<Long, BossHaveFollow>> iter = flist.entrySet().iterator();
        HashMap<String, List<Long>> roleSids = new HashMap<>();
        while (iter.hasNext()) {
            Map.Entry<Long, BossHaveFollow> en = iter.next();
            BossHaveFollow bhf = en.getValue();
            long roleId = en.getKey();
            String key = bhf.getPlat() + "_" + bhf.getSid();
            List<Long> roleIds = roleSids.get(key);
            if (roleIds == null) {
                roleIds = new ArrayList<>();
                roleSids.put(key, roleIds);
            }
            roleIds.add(roleId);
        }

        msg.setBossId(bossConfigId);
        Iterator<Map.Entry<String, List<Long>>> siter = roleSids.entrySet().iterator();
        while (siter.hasNext()) {
            Map.Entry<String, List<Long>> en = siter.next();

            ChannelHandlerContext session = GameServerManager.getInstance().GetSession(en.getKey());
            if (session == null) {
                continue;
            }
            msg.clearRoleIds();
            msg.addAllRoleIds(en.getValue());
            MessageUtils.send_to_game(session, CrossHorseBossMessage.P2GResCrossHorseBossRefreshTip.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    private  int getLineMaxPeople(int cfgId) {
        Cfg_Bossnew_HorseBoss_Bean horseBoss_bean = CfgManager.getCfg_Bossnew_HorseBoss_Container().getValueByKey(cfgId);
        Cfg_Clone_map_Bean clone_map_bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(horseBoss_bean.getCloneid());
        Cfg_Mapsetting_Bean mapsetting_bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(clone_map_bean.getMapid());
        return mapsetting_bean.getOnline();
    }
}
