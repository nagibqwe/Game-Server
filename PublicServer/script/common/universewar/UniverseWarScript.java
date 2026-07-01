package common.universewar;

import com.data.CfgManager;
import com.data.MessageString;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Daily_Bean;
import com.game.dailyactive.manager.DailyActiveManager;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.fightroom.manager.FightManager;
import com.game.fightroom.structs.FightRoom;
import com.game.fightroom.structs.FightRoomState;
import com.game.gameserver.manager.GameServerManager;
import com.game.gameserver.structs.ServerInfo;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.servermatch.structs.GameServerInfo;
import com.game.structs.ServerType;
import com.game.structs.SessionKey;
import com.game.universe.script.IUniverseWarScript;
import com.game.universe.struts.GuildBattleInfo;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.zone.structs.TeamPlayerInfo;
import com.game.zone.structs.ZoneTeam;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.CommandMessage;
import game.message.CommonMessage;
import game.message.CrossFightMessage;
import game.message.MSG_UniverseMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 太虚战场脚本
 */
public class UniverseWarScript implements IUniverseWarScript {
    private static final Logger log = LogManager.getLogger(UniverseWarScript.class);

    @Override
    public int getId() {
        return ScriptEnum.UniverseWarScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void openPanel(ChannelHandlerContext context, MSG_UniverseMessage.G2PReqUniverseWarPanel messInfo) {
        long roleId = messInfo.getRoleId();
        String serverKey = context.channel().attr(SessionKey.SERVERPLATID).get();
        Cfg_Daily_Bean dailyBean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR);
        if (dailyBean == null) {
            return;
        }
        List<ServerInfo> fsList = Manager.gameServerManager.GetType(ServerType.FIGHTSERVER);
        if (fsList.isEmpty()) {
            MessageUtils.notify_player(context, roleId, MessageString.ServerMachtFail);
            return;
        }
        FightRoom fightRoom = ServerMatchManager.deal().getFightRoomForCurOpenDay(serverKey, DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR);
        if (fightRoom == null){
            if(!canOpen(dailyBean)){
                GameServerInfo gameServerInfo = ServerMatchManager.infos.get(serverKey);
                log.info("玩家无法进入天墟战场，没有找到对应的房间,当前服务器:"+serverKey+",世界等级："+gameServerInfo.getServerWorldLv()+",开服时间:"+gameServerInfo.getOpenTime()+",开服天数:"+ GameServerManager.getOpenServerDay(gameServerInfo.getOpenTime()));
//            MessageUtils.notify_player(context, roleId, MessageString.C_TERRITORIAL_CLOSE);
                return;
            }
            fightRoom = ServerMatchManager.deal().getFightRoomForCurOpenDay(serverKey, DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR);
            if(fightRoom == null){
                GameServerInfo gameServerInfo = ServerMatchManager.infos.get(serverKey);
                log.info("玩家无法进入天墟战场，没有找到对应的房间,当前服务器:"+serverKey+",世界等级："+gameServerInfo.getServerWorldLv()+",开服时间:"+gameServerInfo.getOpenTime()+",开服天数:"+ GameServerManager.getOpenServerDay(gameServerInfo.getOpenTime()));
//            MessageUtils.notify_player(context, roleId, MessageString.C_TERRITORIAL_CLOSE);
                return;
            }
        }
        if(fightRoom.getModelId()<=0){
            log.info("跨服房间的副本ID错误,cloneId="+fightRoom.getModelId());
            return;
        }
        //通知战斗服同步面板消息
        for(ServerInfo fightServer:fsList){
            if(fightRoom.getServerId()==fightServer.getServerId()){
                MSG_UniverseMessage.P2FReqUniverseWarPanel.Builder msg = MSG_UniverseMessage.P2FReqUniverseWarPanel.newBuilder();
                msg.setRoleId(roleId);
                msg.setRoomID(fightRoom.getFid());
                msg.setPlatServerId(serverKey);
                msg.setAnger(messInfo.getAnger());
                MessageUtils.send_to_game(fightServer.getSession(), MSG_UniverseMessage.P2FReqUniverseWarPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
    }

    private boolean canOpen(Cfg_Daily_Bean dailyBean){
        if(!inOpenTime(dailyBean)){
            return false;
        }

        return createRoom(getCurCloneId(dailyBean));
    }

    @Override
    public void createRoom(Cfg_Daily_Bean bean, int index) {
        int cloneId = bean.getCloneID().get(index);
        createRoom(cloneId);
    }

    private boolean createRoom(int cloneId){
        Cfg_Clone_map_Bean mapBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(cloneId);
        if (mapBean == null) {
            log.error("Cfg_Clone_map_Bean无法找到数据，创建跨服房间失败，mapID = " + cloneId);
            return false;
        }
        List<ServerInfo> fsList = Manager.gameServerManager.GetType(ServerType.FIGHTSERVER);
        if (fsList.isEmpty()) {
            log.error("公共服没有收到战斗服的注册消息！！！");
            return false;
        }
        ConcurrentHashMap<Integer,List<GameServerInfo>> groupMap = ServerMatchManager.deal().getAllReachGroupServerList(DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR);
        if (groupMap.isEmpty()){
            log.error("未有达成匹配的服务器");
            return false;
        }

        boolean isCreate = false;
        int i = 0;
        for (Map.Entry<Integer,List<GameServerInfo>> e: groupMap.entrySet()) {
            Integer groupId = e.getKey();
            if(isExist(cloneId,groupId)){
                continue;
            }
            if (i >= fsList.size()) {
                i = 0;
            }
            ServerInfo fsInfo = fsList.get(i);
            i++;
            long fightMapID = IDConfigUtil.getLogId();
            FightRoom room = new FightRoom();
            room.setModelId(cloneId);
            room.setServerGroupId(groupId);
            room.setFid(fightMapID);
            room.setCtime(TimeUtils.Time());
            room.setWaitTime(room.getCtime());
            room.setEndwait(room.getCtime()+mapBean.getEnter_time());
            room.setAttackValue(0);
            room.setServerId(fsInfo.getServerId());
            room.setAllReadyStart(true);
            room.setRstate(FightRoomState.CREATEROOM);
            long copyMapEndTime = DailyActiveManager.getInstance().deal().getDailyNearlyEndTime(DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR);
            long endTime = copyMapEndTime - TimeUtils.Time();
            room.setFightTime(endTime);
            FightManager.getInstance().SaveRoomInfo(room, fsInfo.getPlatName(), fsInfo.getServerId());
            isCreate = true;

            //通知战斗服创建地图
            CrossFightMessage.P2FCreateCityMap.Builder msg = CrossFightMessage.P2FCreateCityMap.newBuilder();
            msg.setModelID(cloneId);
            msg.setRoomID(fightMapID);
            List<CommonMessage.CrossAttribute> result = new ArrayList<>();
            CommonMessage.CrossAttribute.Builder info = CommonMessage.CrossAttribute.newBuilder();
            info.setType(1);
            info.setValue(copyMapEndTime);
            String param = groupId.toString();
            //服务器组阵营的平均世界等级
            int avgLv = getAvgLv(e.getValue());
            param+=","+avgLv;
            info.setParam(param);
            result.add(info.build());

            //同步公会战信息
            CommonMessage.CrossAttribute.Builder guildInfo = CommonMessage.CrossAttribute.newBuilder();
            guildInfo.setType(2);
            guildInfo.setValue(0L);
            String guildBattleStr = getGuildBattleInfo(e.getValue());
            guildInfo.setParam(guildBattleStr);
            result.add(guildInfo.build());

            msg.addAllMapSetList(result);
            MessageUtils.send_to_game(fsInfo.getSession(), CrossFightMessage.P2FCreateCityMap.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            log.info("创建天墟战场房间成功：groupId="+groupId+",roomId="+fightMapID+",fsId="+fsInfo.getServerId()+",cloneId="+cloneId+",worldLv:"+avgLv+",结束时间:"+TimeUtils.format2string(copyMapEndTime));
        }
        return isCreate;
    }

    private String getGuildBattleInfo(List<GameServerInfo> gsList) {
        StringBuilder sb = new StringBuilder();
        ConcurrentHashMap<String, ConcurrentHashMap<String, GuildBattleInfo>> gbMap = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, GuildBattleInfo> bMap;
        for (GameServerInfo gs:gsList) {
            ConcurrentHashMap<Integer, GuildBattleInfo> battleMap = ServerParamUtil.guildBattleInfoMap.get(gs.getServerId());
            if(battleMap == null||battleMap.size()<=0){
                continue;
            }
            bMap = new ConcurrentHashMap<>();
            for (Map.Entry<Integer, GuildBattleInfo> entry:battleMap.entrySet()) {
                bMap.put(entry.getKey().toString(),entry.getValue());
            }
            gbMap.put(gs.getPlatName()+"_"+gs.getServerId(), bMap);
        }
        sb.append(JsonUtils.toJSONString(gbMap));
        return sb.toString();
    }

    private int getAvgLv(List<GameServerInfo> gsList) {
        int sumLv = 0;
        int count = 0;
        int avgLv = 0;
        for (GameServerInfo gs:gsList) {
            String key = gs.getPlatName() + "_" +gs.getServerId();
            GameServerInfo curGS = ServerMatchManager.infos.get(key);
            if (curGS.getIsMerge())
                continue;
//                log.info(curGS.getServerId()+",worldLv:"+curGS.getServerWorldLv());
            sumLv+=curGS.getServerWorldLv();
            count++;
        }
        if(count>0){
            avgLv = (int)Math.ceil(sumLv/count);
        }
        return avgLv;
    }

    @Override
    public void removeRoom(Cfg_Daily_Bean bean ) {
        //删除房间
        for (FightRoom room : Manager.fightManager.getFrcache().values()) {
            if (bean.getCloneID().contains(room.getModelId())) {
                FightManager.getInstance().RemoveFight(room);
            }
        }
    }

    @Override
    public void enterDaily(ChannelHandlerContext context, MSG_UniverseMessage.G2PEnterDaily messInfo) {
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR);
        if (bean == null) {
            log.error("Cfg_Daily_Bean配置有误，无法进入天墟战场，dailyID = " + DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR);
            return;
        }
        long roleId = messInfo.getRoleId();
        String serverKey = context.channel().attr(SessionKey.SERVERPLATID).get();
        FightRoom fightRoom = ServerMatchManager.deal().getFightRoomForCurOpenDay(serverKey, DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR);
        if (fightRoom == null){
            if(!canOpen(bean)){
                GameServerInfo gameServerInfo = ServerMatchManager.infos.get(serverKey);
                log.info("玩家无法进入天墟战场，没有找到对应的房间,当前服务器:"+serverKey+",世界等级："+gameServerInfo.getServerWorldLv()+",开服时间:"+gameServerInfo.getOpenTime()+",开服天数:"+ GameServerManager.getOpenServerDay(gameServerInfo.getOpenTime()));
                MessageUtils.notify_player(context, roleId, MessageString.C_TERRITORIAL_CLOSE);
                return;
            }
            fightRoom = ServerMatchManager.deal().getFightRoomForCurOpenDay(serverKey, DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR);
            if(fightRoom == null){
                GameServerInfo gameServerInfo = ServerMatchManager.infos.get(serverKey);
                log.info("玩家无法进入天墟战场，没有找到对应的房间,当前服务器:"+serverKey+",世界等级："+gameServerInfo.getServerWorldLv()+",开服时间:"+gameServerInfo.getOpenTime()+",开服天数:"+ GameServerManager.getOpenServerDay(gameServerInfo.getOpenTime()));
                MessageUtils.notify_player(context, roleId, MessageString.C_TERRITORIAL_CLOSE);
                return;
            }
        }
        if(fightRoom.getModelId()<=0){
            log.info("跨服房间的副本ID错误,cloneId="+fightRoom.getModelId());
            return;
        }

        int maxNum =  FightManager.getInstance().getLineMaxPeople(fightRoom.getModelId());
        if (fightRoom.hasPeoples() >= maxNum){
            MessageUtils.notify_player(context,roleId, MessageString.Activityp_Is_Full);
            log.info("太虚 战场地图人数已满：hasPeoples {}  maxNum  {}  mapModelId {}" ,fightRoom.hasPeoples(),maxNum,fightRoom.getModelId());
            return ;
        }

        FightManager.getInstance().deal().addZoneTeam(context,fightRoom,roleId);
        sendMsgToServer(fightRoom, roleId, context);
    }

    private void sendMsgToServer(FightRoom fightRoom, long roleId, ChannelHandlerContext context) {
        CrossFightMessage.P2GResFightStart.Builder msg = CrossFightMessage.P2GResFightStart.newBuilder();
        msg.setFightId(fightRoom.getFid());
        msg.setZoneModelId(fightRoom.getModelId());
        msg.setFightServerId(fightRoom.getServerId());

        CrossFightMessage.roleAtt.Builder mRole = CrossFightMessage.roleAtt.newBuilder();
        mRole.setCampNo(context.channel().attr(SessionKey.SERVERID).get());
        mRole.setRoleId(roleId);
        msg.addRoleInfo(mRole);
        msg.setMapModelId(fightRoom.getMapmodelId());

        List<CommonMessage.CrossAttribute> result = new ArrayList<>();
        int serverGroupId = fightRoom.getServerGroupId();
        CommonMessage.CrossAttribute.Builder info = CommonMessage.CrossAttribute.newBuilder();
        info.setType(100001);
        info.setValue(0);
        info.setParam1(serverGroupId);
        result.add(info.build());
        msg.addAllMapSetList(result);

        MessageUtils.send_to_game(context, CrossFightMessage.P2GResFightStart.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }



    @Override
    public void openReadyBlock() {
        Cfg_Daily_Bean dailyBean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR);
        if (dailyBean == null || dailyBean.getCrossMatch().isEmpty()) {
            log.error("Cfg_Daily_Bean配置有误，dailyId = " + DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR);
            return;
        }
        List<ServerInfo> list = Manager.gameServerManager.GetType(ServerType.FIGHTSERVER);
        if (list.isEmpty()) {
            log.error("公共服没有收到战斗服的注册消息！！！");
            return;
        }
        int cloneId = getCurCloneId(dailyBean);
        List<FightRoom> frList = FightManager.getInstance().getModelFight(cloneId);
        if(frList.isEmpty()){
            log.error("公共服没有找到对应的副本房间！！！");
            return;
        }

        //通知战斗服开启阻挡
        for (int i = 0; i < frList.size(); i++) {
            FightRoom fr = frList.get(i);
            for(ServerInfo s:list){
                if(fr.getServerId()==s.getServerId()){
                    fr.setRstate(FightRoomState.FIGHTING);
                    MSG_UniverseMessage.P2FOpenBlock.Builder msg = MSG_UniverseMessage.P2FOpenBlock.newBuilder();
                    msg.setRoomID(fr.getFid());
                    MessageUtils.send_to_game(s.getSession(),MSG_UniverseMessage.P2FOpenBlock.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                    log.error("通知战斗服打开太虚战场准备阶段阻挡,fid="+fr.getFid());
                }
            }
        }
    }

    /**
     * 获取的当前时间段对应的副本ID
     * @param bean
     * @return
     */
    private int getCurCloneId(Cfg_Daily_Bean bean){
        long nowTime = TimeUtils.Time();
        int nowMin = TimeUtils.getDayOfHour(nowTime) * 60 + TimeUtils.getDayOfMin(nowTime);
        for (int i = 0; i < bean.getTime().size(); i++) {
            int startMin = bean.getTime().get(i).get(0);
            int endMin = bean.getTime().get(i).get(1);

            if(startMin<=nowMin&&nowMin<=endMin){
                return bean.getCloneID().get(i);
            }
        }
        return bean.getCloneID().get(0);
    }

    private boolean inOpenTime(Cfg_Daily_Bean bean){
        long nowTime = TimeUtils.Time();
        int nowMin = TimeUtils.getDayOfHour(nowTime) * 60 + TimeUtils.getDayOfMin(nowTime);
        for (int i = 0; i < bean.getTime().size(); i++) {
            int startMin = bean.getTime().get(i).get(0);
            int endMin = bean.getTime().get(i).get(1);

            if(startMin<=nowMin&&nowMin<=endMin){
                return true;
            }
        }
        return false;
    }

    private boolean isExist(int cloneId, int groupId){
        List<FightRoom> fightRooms = FightManager.getInstance().getModelFight(cloneId);
        for (FightRoom fr:fightRooms) {
            if(fr.getServerGroupId() == groupId){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onG2PSynGuildBattleInfo(ChannelHandlerContext context, CommandMessage.G2PSynGuildBattleInfo messInfo) {
        int serverId = context.channel().attr(SessionKey.SERVERID).get();
        ConcurrentHashMap<Integer, GuildBattleInfo> guildMap = ServerParamUtil.guildBattleInfoMap.get(serverId);
        if(guildMap == null){
            guildMap = new ConcurrentHashMap<>();
            ServerParamUtil.guildBattleInfoMap.put(serverId, guildMap);
        }

        for(CommandMessage.GuildBattleInfo guildBattleInfoMsg:messInfo.getGuildBattleInfosList()){
            int rank = guildBattleInfoMsg.getRank();
            long masterId = guildBattleInfoMsg.getMasterId();
            if(masterId == 0L){//公会解散,删除记录
                guildMap.remove(rank);
                continue;
            }
            List<Long> secMasterIds = guildBattleInfoMsg.getSecMasterIdList();
            GuildBattleInfo gbi = guildMap.get(rank);
            if(gbi == null){
                gbi = new GuildBattleInfo();
                gbi.setMasterId(masterId);
                gbi.setSecMasterId(secMasterIds);
                guildMap.put(rank, gbi);
                continue;
            }
            gbi.setMasterId(masterId);
            gbi.setSecMasterId(secMasterIds);
        }
        ServerParamUtil.saveGuildBattleInfo();
    }
}
