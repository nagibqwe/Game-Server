package common.eightdiagrams;

import com.data.CfgManager;
import com.data.MessageString;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Daily_Bean;
import com.data.bean.Cfg_EightCity_Bean;
import com.data.bean.Cfg_Monster_Bean;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.db.bean.EightIntegralRankBean;
import com.game.eightdiagrams.manager.EightDiagramsManager;
import com.game.eightdiagrams.script.IEightDiagrams;
import com.game.eightdiagrams.structs.*;
import com.game.fightroom.manager.FightManager;
import com.game.fightroom.structs.FightRoom;
import com.game.fightroom.structs.FightRoomState;
import com.game.gameserver.manager.GameServerManager;
import com.game.gameserver.structs.ServerInfo;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.servermatch.structs.GameServerInfo;
import com.game.servermatch.structs.ServerIDSort;
import com.game.structs.ServerType;
import com.game.structs.SessionKey;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.zone.structs.TeamPlayerInfo;
import com.game.zone.structs.ZoneTeam;
import game.core.json.TypeReference;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.core.util.VersionUpdateUtil;
import game.message.CommonMessage;
import game.message.CrossFightMessage;
import game.message.EightDiagramsMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map.Entry;

/**
 * Created by cxl on 2019/9/20.
 */
public class EightDiagramsScript implements IEightDiagrams {

    private static final Logger log = LogManager.getLogger(EightDiagramsScript.class);

    @Override
    public int getId() {
        return ScriptEnum.EightDiagramsScript;
    }
    @Override
    public Object call(Object... objects) {
        return null;
    }


    /**
     * 准备阶段
     */
    @Override
    public void preparationPhase() {
        resetCity();
    }

    /**
     * 活动开启
     */
    public void eightDiagramsStart() {
        //分配城市
        resetCity();
        initCityRoom();
        EightDiagramsManager.getInstance().setEightDiagramsIsOpen(true);

    }

    private void updateEightCitySql() {
        ServerParamUtil.saveEightCityInfo();
        ServerParamUtil.savaEightGroupInfo();
    }

    /**
     * 结束
     */
    public void eightDiagramsOver(){
        //updateEightCitySql();
        //activityOverSettle();
       // integralSettle();
        EightDiagramsManager.getInstance().setEightDiagramsIsOpen(false);
        EightDiagramsManager.getInstance().getPlayerWithCiytIDList().clear();
		log.info("eightDiagramsOver");
    }

    /**
     * 打开主面板
     */
    public  void G2PReqEightDiagramsPanel(ChannelHandlerContext context, EightDiagramsMessage.G2PReqEightDiagramsPanel messInfo)
    {
        String platServerId = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupID = -1;
        if (EightDiagramsManager.getInstance().getServerPlat_SidToGroup().containsKey(platServerId))
              groupID = EightDiagramsManager.getInstance().getServerPlat_SidToGroup().get(platServerId);
        if (groupID<0) {
            log.info("平均等级没达到");
            return;
        }
        String serverName = "";
        int serverID =   context.channel().attr(SessionKey.SERVERID).get();
        if(!EightDiagramsManager.getInstance().getEightDiagramsIsOpen()) {
            if (ServerParamUtil.groupCityInfos.containsKey(groupID)) {

                ConcurrentHashMap<Integer ,EightDiagramCity> cityList =
                        ServerParamUtil.groupCityInfos.get(groupID);
                int selfCamp =  EightDiagramsManager.getInstance().getServerWithColorIDList().get(serverID);
                if (cityList  != null && cityList.size() >0) {
                    EightDiagramsMessage.ResEightDiagramsPanel.Builder msg =
                            EightDiagramsMessage.ResEightDiagramsPanel.newBuilder();
                    msg.setSelfSid(serverID);
                    msg.setSelfServerName(serverName);
                    msg.setSelfCamp(selfCamp);
                    List< EightDiagramsMessage.CityInfo>  cityInfoList = new ArrayList<>();
                    for (Integer key :cityList.keySet()) {
                        EightDiagramsMessage.CityInfo.Builder cityInfo = EightDiagramsMessage.CityInfo.newBuilder();
                        EightDiagramCity city = cityList.get(key);
                        if (city!=null) {
                            setCityInfoMessage( cityInfo,city );
                        }
                        cityInfoList.add(cityInfo.build());
                    }
                    msg.addAllCityListInfo(cityInfoList);
                    msg.setIsopen(false);
                    MessageUtils.send_to_player(context,messInfo.getRoleID(),
                            EightDiagramsMessage.ResEightDiagramsPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                }
            }
            else {
                log.info("未找到组ID "  + groupID  + "  对应的城市列表");
                return;
            }
        }
        else {
            int  fightSid = findFightSessionFoGroupID(groupID);
            if (fightSid>0) {
                int selfCamp =  EightDiagramsManager.getInstance().getServerWithColorIDList().get(serverID);
                EightDiagramsMessage.P2FReqEightDiagramsPanel.Builder msg = EightDiagramsMessage.P2FReqEightDiagramsPanel.newBuilder();
                msg.setGroupID(groupID);
                msg.setRoleID(messInfo.getRoleID());
                msg.setServerID(serverID);
                msg.setCamp(selfCamp);
                msg.setServerName(serverName);
                msg.setFightSid(fightSid);
                MessageUtils.send_to_game(context,
                        EightDiagramsMessage.P2FReqEightDiagramsPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            }
        }
    }

    /**
     * 请求排行数据
     */
   public void  G2PReqRankPanel(ChannelHandlerContext context, EightDiagramsMessage.G2PReqRankPanel messInfo)
   {
       String platServerId = context.channel().attr(SessionKey.SERVERPLATID).get();
       long roleID = messInfo.getRoleID();
       int groupID = -1;
       if (EightDiagramsManager.getInstance().getServerPlat_SidToGroup().containsKey(platServerId))
           groupID = EightDiagramsManager.getInstance().getServerPlat_SidToGroup().get(platServerId);
       if (groupID<0) {
           log.info("平均等级没达到  rank info");
           return;
       }
       if(!EightDiagramsManager.getInstance().getEightDiagramsIsOpen()) {
           EightDiagramsMessage.ResRankPanel.Builder resRankPanel =  EightDiagramsMessage.ResRankPanel.newBuilder();
           if (EightDiagramsManager.getInstance().getDayOf_playerBattleInfo().containsKey(groupID)) {
               ConcurrentHashMap<Long, EightIntegralRankBean> dayPlayerBattleInfoList =
                       EightDiagramsManager.getInstance().getDayOf_playerBattleInfo().get(groupID);
               List<EightIntegralRankBean> ranklist50Num  =
                       EightDiagramsManager.getInstance().getDayOf_Ranklist50Num().get(groupID);
               if (dayPlayerBattleInfoList!=null && dayPlayerBattleInfoList.size()>0) {
                   addRankMessageInfo(ranklist50Num,  dayPlayerBattleInfoList, resRankPanel,1,roleID);
               }
           }
           if (  Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().containsKey(groupID)) {
               ConcurrentHashMap<Long, EightIntegralRankBean> periodPlayerBattleInfoList =
                       Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().get(groupID);

               List<EightIntegralRankBean> ranklist50Num  =
                       EightDiagramsManager.getInstance().getPeriodOf_Ranklist50Num().get(groupID);
               if (periodPlayerBattleInfoList!=null && periodPlayerBattleInfoList.size()>0) {
                   addRankMessageInfo(ranklist50Num, periodPlayerBattleInfoList, resRankPanel,2,roleID);
               }
           }
       MessageUtils.send_to_player(context,roleID,
               EightDiagramsMessage.ResRankPanel.MsgID.eMsgID_VALUE, resRankPanel.build().toByteArray());
   }
       else {
           int fightSid = findFightSessionFoGroupID(groupID);
           if (fightSid > 0) {
               EightDiagramsMessage.P2FReqRankPanel.Builder msg = EightDiagramsMessage.P2FReqRankPanel.newBuilder();
               msg.setGroupID(groupID);
               msg.setRoleID(messInfo.getRoleID());
               msg.setFightSid(fightSid);
               MessageUtils.send_to_game(context,
                       EightDiagramsMessage.P2FReqRankPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
           }
       }
   }
   private void addRankMessageInfo( List<EightIntegralRankBean> ranklist50Num,
                                    ConcurrentHashMap<Long, EightIntegralRankBean> playerBattleInfoList,
                                    EightDiagramsMessage.ResRankPanel.Builder resRankPanel,int type,long roleID) {
       int selfRank = -1;
       int rank = 1;
       for (EightIntegralRankBean inte : ranklist50Num) {
           EightDiagramsMessage.RankInfo.Builder curRankInfo = EightDiagramsMessage.RankInfo.newBuilder();
           curRankInfo.setIntegral(inte.getAllIntegral());
           curRankInfo.setName(inte.getName());
           curRankInfo.setRank(rank);
           curRankInfo.setColorCamp(inte.getColorCamp());
           curRankInfo.setServerid(inte.getServerid());
           if (inte.getRoleID() == roleID)
               selfRank = rank;
           if (type == 1)
               resRankPanel.addCurRankInfoList(curRankInfo.build());
           else
               resRankPanel.addSeasonRankInfoList(curRankInfo.build());
           rank++;
       }

       int selfIntrgral = 0;
       String myName = "";
       int myColorCamp = 0;
       int serverid = 0;
       if (playerBattleInfoList.containsKey(roleID)) {
           selfIntrgral = playerBattleInfoList.get(roleID).getAllIntegral();
           myName       = playerBattleInfoList.get(roleID).getName();
           myColorCamp  = playerBattleInfoList.get(roleID).getColorCamp();
           serverid     = playerBattleInfoList.get(roleID).getServerid();
       }
       EightDiagramsMessage.RankInfo.Builder selfRankInfo = EightDiagramsMessage.RankInfo.newBuilder();
       selfRankInfo.setName(myName);
       selfRankInfo.setRank(selfRank);
       selfRankInfo.setIntegral(selfIntrgral);
       selfRankInfo.setColorCamp(myColorCamp);
       selfRankInfo.setServerid(serverid);
       if (type == 1)
           resRankPanel.setSelfCurRankInfo(selfRankInfo.build());
       else
           resRankPanel.setSelfSeasonRankInfo(selfRankInfo.build());
   }

    /**
     *   //请求当前城市 数据信息
     */
   public void G2PReqTickMapInfo(ChannelHandlerContext context,EightDiagramsMessage.G2PReqTickMapInfo messInfo)
   {
       if (!EightDiagramsManager.getInstance().getEightDiagramsIsOpen()) {
           log.info("活动未开启");
           return;
       }
       int groupID = -1;
       String platServerId = context.channel().attr(SessionKey.SERVERPLATID).get();
       //int serverID = context.channel().attr(SessionKey.SERVERID).get();
       if (EightDiagramsManager.getInstance().getServerPlat_SidToGroup().containsKey(platServerId))
           groupID = EightDiagramsManager.getInstance().getServerPlat_SidToGroup().get(platServerId);
       if (groupID<0) {
           log.info("平均等级没达到  rank info");
           MessageUtils.notify_player(context,messInfo.getRoleID(), MessageString.ServerMachtFail);
           return;
       }
       if (ServerParamUtil.groupCityInfos.containsKey(groupID)) {
           ConcurrentHashMap<Integer, EightDiagramCity> cityList =
                   ServerParamUtil.groupCityInfos.get(groupID);
           if (cityList != null && cityList.size() > 0) {
               if (cityList.containsKey(messInfo.getCityID())) {
                   EightDiagramCity city = cityList.get(messInfo.getCityID());
                   if (city != null) {
                        FightRoom fr =  FightManager.getInstance().getFrcache().get(city.getRoomID());
                        if (fr!=null) {
                            ChannelHandlerContext session =
                                    GameServerManager.getInstance().GetSession(fr.getPlat(), fr.getServerId());
                            if (session!=null) {
                                EightDiagramsMessage.P2FReqTickRankPanel.Builder msg =
                                        EightDiagramsMessage.P2FReqTickRankPanel.newBuilder();
                                msg.setRoleID(messInfo.getRoleID());
                                msg.setCityID(messInfo.getCityID());
                                msg.setGroupID(groupID);
                                MessageUtils.send_to_game(session,
                                        EightDiagramsMessage.P2FReqTickRankPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                            }
                        }
                   }
               }
           }
       }
   }
   private void setCityInfoMessage( EightDiagramsMessage.CityInfo.Builder cityInfo, EightDiagramCity city  )
   {
       if (city!=null) {
           cityInfo.setCityID( city.getCityId());
           cityInfo.setBirthSid(city.getBirthSid());
           cityInfo.setModelID(city.getModelID());
           cityInfo.setCityID(city.getCityId());
           cityInfo.setCurSid(city.getCurSid());
           cityInfo.setServerName(city.getServerName());
           cityInfo.setBossID(city.getBossID());
           cityInfo.setCurHp(city.getCurBossHp());
           cityInfo.setMaxHp(city.getMaxBossHp());
           cityInfo.setColorCamp(city.getColorCamp());
           for (Integer sid :city.getCityBattleProgressList().keySet()) {
               CityBattleProgress cityBattle =  city.getCityBattleProgressList().get(sid);
               EightDiagramsMessage.CityBattleInfo.Builder cityBattleInfo
                       =  EightDiagramsMessage.CityBattleInfo.newBuilder();
               cityBattleInfo.setSid(cityBattle.getSid());
               cityBattleInfo.setPlayerNum(cityBattle.getPlayerNum());
               cityBattleInfo.setColorCamp(cityBattle.getColorCamp());
               cityBattleInfo.setBossHurt(cityBattle.getBossHurt());
               cityBattleInfo.setServerName(cityBattle.getServerName());

               cityInfo.addCityBattleInfoList(cityBattleInfo.build());
           }
       }
   }

   private void sendCityInfoToPublic(int groupid,ServerInfo fightserver)
   {

       ConcurrentHashMap<Integer ,EightDiagramCity> cityList =
               ServerParamUtil.groupCityInfos.get(groupid);
       ConcurrentHashMap<Long, EightIntegralRankBean> periodlsit = new ConcurrentHashMap<>();
       if ( Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().containsKey(groupid)) {
           periodlsit =    Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().get(groupid);
       }
       EightDiagramsMessage.P2FSendEightCityInfo.Builder msg = EightDiagramsMessage.P2FSendEightCityInfo.newBuilder();
       List<EightDiagramsMessage.EightCityAttribute> attributeslist = new ArrayList<>();
       EightDiagramsMessage.EightCityAttribute.Builder attribute = EightDiagramsMessage.EightCityAttribute.newBuilder();
       String cityStringList =  VersionUpdateUtil.dataSave(JsonUtils.toJSONString(cityList));
       attribute.setType(1);
       attribute.setValue(groupid);
       attribute.setParam(cityStringList);
       attributeslist.add(attribute.build());
       String periodStringList =  VersionUpdateUtil.dataSave(JsonUtils.toJSONString(periodlsit));
       attribute = EightDiagramsMessage.EightCityAttribute.newBuilder();
       attribute.setType(2);
       attribute.setValue(groupid);
       attribute.setParam(periodStringList);
       attributeslist.add(attribute.build());
       msg.addAllEightCityStartInfo(attributeslist);
       MessageUtils.send_to_game(fightserver.getSession(), EightDiagramsMessage.P2FSendEightCityInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
   }
   private void initCityRoom()
   {
       List<ServerInfo> list = Manager.gameServerManager.GetType(ServerType.FIGHTSERVER);
       if(list.size()<=0) {
           log.info("没有战斗服");
           return;
       }
       int fightServerIndex = 0;
       for (Integer groupid : ServerParamUtil.groupCityInfos.keySet()) {
           ConcurrentHashMap<Integer ,EightDiagramCity> cityList =
                   ServerParamUtil.groupCityInfos.get(groupid);
           if (fightServerIndex >= list.size() )
               fightServerIndex = 0;

           ServerInfo fightserver = list.get(fightServerIndex);
           for (EightDiagramCity ci :cityList.values()) {
               Cfg_Clone_map_Bean cloneMapCfg = CfgManager.getCfg_Clone_map_Container().getValueByKey(ci.getModelID());
               if (cloneMapCfg == null)
                   continue;
               long fightMapID = IDConfigUtil.getLogId();
               FightRoom room = new FightRoom();
               room.setModelId( ci.getModelID());
               room.setServerGroupId(groupid);
               room.setFid(fightMapID);
               room.setCtime(TimeUtils.Time());
               room.setAttackValue(0);
               room.setServerId(fightserver.getServerId());
               room.setpPlat(fightserver.getPlatName());
               room.setAllReadyStart(true);
               room.setRstate(FightRoomState.CREATEROOM);
               room.setFightTime(cloneMapCfg.getExist_time() + cloneMapCfg.getEnter_time());
               FightManager.getInstance().SaveRoomInfo(room, ci.getPlatName(), ci.getCurSid());//保存并且写log

               ci.setRoomID(fightMapID);
               ci.getCityBattleProgressList().clear();
               Cfg_EightCity_Bean bean =   CfgManager.getCfg_EightCity_Container().getValueByKey(ci.getCityId());
               if (bean!=null) {
                   Cfg_Monster_Bean monsterCfg = CfgManager.getCfg_Monster_Container().getValueByKey( bean.getBossID());
                   if (monsterCfg != null) {
                       ci.setCurBossHp(monsterCfg.getMaxHp());
                       ci.setMaxBossHp(monsterCfg.getMaxHp());
                       ci.setBossID(bean.getBossID());
                   }
               }

               //发送给战斗服创建地图
               int camp = 0;
               if (EightDiagramsManager.getInstance().getServerWithColorIDList().containsKey(ci.getCurSid()))
                   camp =  EightDiagramsManager.getInstance().getServerWithColorIDList().get(ci.getCurSid());

               CrossFightMessage.P2FCreateCityMap.Builder msg =  CrossFightMessage.P2FCreateCityMap.newBuilder();
               msg.setModelID(room.getModelId());
               msg.setRoomID(fightMapID);
               List<CommonMessage.CrossAttribute> result = new ArrayList<>();
               CommonMessage.CrossAttribute.Builder info = CommonMessage.CrossAttribute.newBuilder();
               info.setType(1);
               info.setValue(ci.getCityId());
               String value = ci.getCityId() + ","  + groupid +   ","  + ci.getBirthSid() + "," + ci.getCurSid() +"," + camp;
               info.setParam(value);
               result.add(info.build());
               msg.addAllMapSetList(result);
               MessageUtils.send_to_game(fightserver.getSession(), CrossFightMessage.P2FCreateCityMap.MsgID.eMsgID_VALUE, msg.build().toByteArray());
           }
           sendCityInfoToPublic( groupid, fightserver);
           fightServerIndex++;
       }
   }
    /**
     *   开始战斗
     */
     private void fightStart(FightRoom room, long roleID,String plat,int sid)
     {
         if (room == null) {
             log.info("房间不存在");
             return;
         }

         int colorCamp = 0;
         if (EightDiagramsManager.getInstance().getServerWithColorIDList().containsKey(sid))
             colorCamp = EightDiagramsManager.getInstance().getServerWithColorIDList().get(sid);


         checkZoneTeamInfo( room,  roleID, plat, sid, colorCamp);
         CrossFightMessage.P2GResFightStart.Builder msg = CrossFightMessage.P2GResFightStart.newBuilder();
         msg.setFightId(room.getFid());
         msg.setZoneModelId(room.getModelId());
         msg.setFightServerId(room.getServerId());
         CrossFightMessage.roleAtt.Builder mRole = CrossFightMessage.roleAtt.newBuilder();
         mRole.setCampNo(colorCamp);
         mRole.setRoleId(roleID);
         msg.addRoleInfo(mRole);
         msg.setMapModelId(room.getMapmodelId());
         room.setHaveNum(room.getHaveNum() +1);
         List<CommonMessage.CrossAttribute> result = new ArrayList<>();
         msg.addAllMapSetList(result);
         ChannelHandlerContext socket = Manager.gameServerManager.GetSession(plat, sid);
         MessageUtils.send_to_game(socket, CrossFightMessage.P2GResFightStart.MsgID.eMsgID_VALUE, msg.build().toByteArray());
         log.info("eightcity   P2GResFightStart  plat {} sid {} ", plat, sid);
     }

     private void checkZoneTeamInfo(FightRoom room, long roleID,String plat,int sid,int colorCamp)
     {
         boolean isInZoneTeam = false;
         for (ZoneTeam z:room.getTeam()) {
             if (z.getsId() == sid) {
                 if (!z.getPlist().containsKey(roleID)) {
                     TeamPlayerInfo teamInfo = new TeamPlayerInfo();
                     teamInfo.setRoleId(roleID);
                     teamInfo.setCampNo(colorCamp);
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
             zoneTeam.setCampNo(colorCamp);
             TeamPlayerInfo teamInfo = new TeamPlayerInfo();
             teamInfo.setRoleId(roleID);
             teamInfo.setCampNo(colorCamp);
             zoneTeam.getPlist().put(roleID,teamInfo);
             room.getTeam().add(zoneTeam);
         }
     }

    /**
     *   进入地图
     */
    public void G2PReqEnterEightCityMap(ChannelHandlerContext context,EightDiagramsMessage.G2PReqEnterEightCityMap messInfo )
    {

        if (!EightDiagramsManager.getInstance().getEightDiagramsIsOpen()) {
            MessageUtils.notify_player(context, messInfo.getRoleID(), MessageString.WorldAnswerAppError);
            return;
        }

        String platServerId = context.channel().attr(SessionKey.SERVERPLATID).get();
        int serverid = context.channel().attr(SessionKey.SERVERID).get();
        String plat = context.channel().attr(SessionKey.SERVERPLAT).get();
        int groupID = -1;
        if (EightDiagramsManager.getInstance().getServerPlat_SidToGroup().containsKey(platServerId))
            groupID = EightDiagramsManager.getInstance().getServerPlat_SidToGroup().get(platServerId);
        if (groupID<0) {
            log.info("八卦阵的平均等级没达到");
            return;
        }
        long roleID = messInfo.getRoleID();
        int cityID =  messInfo.getCityID();
        int  oldCityID = 0;
        if (ServerParamUtil.groupCityInfos.containsKey(groupID)) {
            ConcurrentHashMap<Integer ,EightDiagramCity>  citylist = ServerParamUtil.groupCityInfos.get(groupID);
            EightDiagramsManager.getInstance().getGroupIDForRoleID().put(roleID,groupID);
           if ( EightDiagramsManager.getInstance().getPlayerWithCiytIDList().containsKey( roleID))
                oldCityID=  EightDiagramsManager.getInstance().getPlayerWithCiytIDList().get(roleID);
            if (cityID>0) {
                if (cityID == oldCityID) {
                    log.info("不能同地图切换");
                    return;
                }
                enterSelectCity( context ,citylist, cityID,oldCityID, serverid, roleID,plat);
            }
            else {
                if ( EightDiagramsManager.getInstance().getPlayerWithCiytIDList().containsKey( roleID)) {
                    log.info("您已经在最高级地图了");
                    return;
                }
                //默认进一个等级最高的
                enterDefaultCity(citylist, serverid,roleID,plat);
            }
        }
    }

    //选择进入一个
    private void enterSelectCity( ChannelHandlerContext context ,ConcurrentHashMap<Integer ,EightDiagramCity>
                                 citylist,int cityID,int oldCityID,int serverid,long roleID,String plat)
    {
        if (citylist.containsKey(cityID)) {
            EightDiagramCity targetCity = citylist.get(cityID);
            boolean isCanEnter = targetCity.getBirthSid() == serverid ? true:false;
            isCanEnter = isCanEnter ? isCanEnter: targetCity.getCurSid() ==serverid;
            if (!isCanEnter) {
                Cfg_EightCity_Bean eightcity = CfgManager.getCfg_EightCity_Container().getValueByKey(cityID);
                for (int borderID:eightcity.getCanAttackCity().getValue()) {
                    EightDiagramCity  city  =   citylist.get(borderID);
                    if (city!=null) {
                        if (city.getCurSid() == serverid) {
                            isCanEnter = true;
                            break;
                        }
                    }
                }
            }
            if (isCanEnter) {
                enterMap(context,targetCity, roleID, plat, oldCityID, serverid);
            }
            else {
                MessageUtils.notify_player(context,roleID, MessageString.CantIntoEightCity);
                return;
            }
        }
        else {
            log.info("城市ID未找到 " + cityID);
            return;
        }
    }

    private void enterMap( ChannelHandlerContext context,EightDiagramCity targetCity,long roleID,String plat,int oldCityID,int serverid)
    {
        FightRoom fr = FightManager.getInstance().getFrcache().get(targetCity.getRoomID());
        if (fr == null) {
            log.info("房间不存在");
            return;
        }
        int maxNum =  FightManager.getInstance().getLineMaxPeople(fr.getModelId());
        if (fr.hasPeoples() >= maxNum){
            MessageUtils.notify_player(context,roleID, MessageString.Activityp_Is_Full);
            log.info("八级 阵图地图人数已满：hasPeoples {}  maxNum  {}  mapModelId {}" ,fr.hasPeoples(),maxNum,fr.getModelId());
            return ;
        }


        //if (fr.getRstate() > FightRoomState.READYROOM ) {
        //    log.info("房间不在战斗状态");
        //    return;
        //}
        if (oldCityID <=0)
            fightStart( fr, roleID, plat, serverid);
        else
        {
            //切换地图
            ChannelHandlerContext session  = GameServerManager.getInstance().GetSession( fr.getPlat(),fr.getServerId());
            if (session!=null) {
                EightDiagramsMessage.P2FRepChangeCityMap.Builder msg
                        = EightDiagramsMessage.P2FRepChangeCityMap.newBuilder();
                msg.setRoleID(roleID);
                msg.setMapID(fr.getFid());
                MessageUtils.send_to_game(session,EightDiagramsMessage.P2FRepChangeCityMap.MsgID.eMsgID_VALUE,msg.build().toByteArray());
                log.info("send  P2FRepChangeCityMap ");
            }
        }
    }

    //进入一个默认城市，选择最好的
    private void enterDefaultCity(ConcurrentHashMap<Integer ,EightDiagramCity>
                                           citylist, int serverid, long roleID, String plat) {
        //默认进一个等级最高的
        int maxLevel = 0;
        int selectCityID = 0;
        int myBirthID = 0;
        for (Integer citykey :citylist.keySet()) {
            int otherServerID = citylist.get(citykey).getCurSid();
            if (otherServerID == serverid) {
                Cfg_EightCity_Bean eightcity = CfgManager.getCfg_EightCity_Container().getValueByKey(citykey);
                if (eightcity != null) {
                    for (Integer canAttackCity : eightcity.getCanAttackCity().getValue()) {
                        Cfg_EightCity_Bean canAttackBean = CfgManager.getCfg_EightCity_Container().getValueByKey(canAttackCity);
                        if (canAttackBean != null) {
                            if (canAttackBean.getLevel() > maxLevel) {
                                maxLevel = canAttackBean.getLevel();
                                selectCityID = canAttackBean.getId();
                            }
                        }
                    }
                }
            }
            if (citylist.get(citykey).getBirthSid() == serverid) {
                myBirthID = citykey;
            }
        }
        EightDiagramCity selectCity;
        if (selectCityID>0) {
            //进入等级最高城市
             selectCity =  citylist.get(selectCityID);
        }
        else {
            //进入出生城市
             selectCity =  citylist.get(myBirthID);
        }
        FightRoom fr = FightManager.getInstance().getFrcache().get(selectCity.getRoomID());
        if (fr == null){
            log.error("八级征图房间没找到 {}",selectCity.getRoomID());
            return;
        }
        fightStart( fr, roleID, plat, serverid);
    }

    /**
     * 玩家进入城市
     */
    public void F2PResEnterMapSucc(ChannelHandlerContext context, EightDiagramsMessage.F2PResEnterMapSucc message)
    {
        long roleID = message.getRoleID();
        int cityID = message.getCityID();
        String platSid = message.getPlatSid();
        String[] serverArr =  platSid.split("_");
        int sid = Integer.parseInt(serverArr[1]);
        String plat = serverArr[0];
        EightDiagramsManager.getInstance().getPlayerWithCiytIDList().put(roleID,cityID);
        if (EightDiagramsManager.getInstance().getGroupIDForRoleID().containsKey(roleID)){
            int groupID =   EightDiagramsManager.getInstance().getGroupIDForRoleID().get(roleID);
            if ( ServerParamUtil.groupCityInfos.containsKey(groupID)){
                ConcurrentHashMap<Integer ,EightDiagramCity> citylist =   ServerParamUtil.groupCityInfos.get(groupID);
                if (citylist.containsKey(cityID)){
                    EightDiagramCity city =  citylist.get(cityID);
                    FightRoom fr = FightManager.getInstance().getFrcache().get(city.getRoomID());
                    int colorCamp = 0;
                    if (EightDiagramsManager.getInstance().getServerWithColorIDList().containsKey(sid))
                        colorCamp = EightDiagramsManager.getInstance().getServerWithColorIDList().get(sid);
                    checkZoneTeamInfo( fr,  roleID, plat, sid, colorCamp);
                }
            }
        }
    }

    /**
     * 玩家退出城市
     */
    public void F2PPlayerOutCity(ChannelHandlerContext context, EightDiagramsMessage.F2PPlayerOutCity message)
    {
        long roleID = message.getRoleID();
        int cityID = message.getCityID();
        if ( EightDiagramsManager.getInstance().getPlayerWithCiytIDList().containsKey( roleID)) {
            EightDiagramsManager.getInstance().getPlayerWithCiytIDList().remove(roleID);
        }
        if (EightDiagramsManager.getInstance().getGroupIDForRoleID().containsKey(roleID)){
            int groupID =   EightDiagramsManager.getInstance().getGroupIDForRoleID().get(roleID);
            if ( ServerParamUtil.groupCityInfos.containsKey(groupID)){
                ConcurrentHashMap<Integer ,EightDiagramCity> citylist =   ServerParamUtil.groupCityInfos.get(groupID);
                if (citylist.containsKey(cityID)){
                    EightDiagramCity city =  citylist.get(cityID);
                    FightRoom fr = FightManager.getInstance().getFrcache().get(city.getRoomID());
                    fr.removeRoleId(roleID);
                }
            }
        }
    }

    /**
     * 杀死BOSS
     */
    public void F2PKillBoss(ChannelHandlerContext context ,EightDiagramsMessage.F2PKillBoss message)
    {
        int cityID = message.getCityID();//城市ID
        //long roleID = message.getRoleID();//
        String platSid = message.getPlatSid();
        GameServerInfo gameServerInfo = ServerMatchManager.infos.get(platSid);
        if (gameServerInfo!=null) {
            if (EightDiagramsManager.getInstance().getServerPlat_SidToGroup().containsKey(platSid)) {
                int groupID = EightDiagramsManager.getInstance().getServerPlat_SidToGroup().get(platSid);
                if (ServerParamUtil.groupCityInfos.containsKey(groupID)) {
                    ConcurrentHashMap<Integer, EightDiagramCity> citylist
                            = ServerParamUtil.groupCityInfos.get(groupID);
                    EightDiagramCity cityifo = citylist.get(cityID);
                    if (cityifo != null) {
                        cityifo.setCurSid(gameServerInfo.getServerId());
                        cityifo.setCurBossHp(cityifo.getMaxBossHp());
                        cityifo.setServerName("");
                        cityifo.setColorCamp(message.getColorCamp());
                    }
                }
            }
        }
    }

    /**
     * 重置城市
     */
    private void resetCity()
    {
        EightDiagramsManager.getInstance().getDayOf_playerBattleInfo().clear();
        EightDiagramsManager.getInstance().getDayOf_Ranklist50Num().clear();
        for ( Integer groupID:  ServerParamUtil.groupWithServer.keySet()) {
            if (!ServerParamUtil.groupCityInfos.containsKey(groupID)){
                setCityCamp( groupID);
            }
        }
    }


    private void setCityCamp(int groupID)
    {
        List<EightDiageramSeverInfo> groupWithServerllist =
                ServerParamUtil.groupWithServer.get(groupID);
        ConcurrentHashMap<Integer ,EightDiagramCity> citylist = new ConcurrentHashMap<>();
        int size =  groupWithServerllist.size();
        int count = 0;
        for (int i =0 ;i< CfgManager.getCfg_EightCity_Container().size();i++) {
            Cfg_EightCity_Bean bean =  CfgManager.getCfg_EightCity_Container().getValueByIndex(i);
            EightDiagramCity city = new EightDiagramCity();
            city.setCityId(bean.getId());
            city.setModelID(bean.getModleID());
            city.setBossID(bean.getBossID());
            if (size == ServerMatchManager.serverMatchStage_4) {
                if (i % 2 != 0 && i<=ServerMatchManager.serverMatchStage_8) {
                    EightDiageramSeverInfo info =  groupWithServerllist.get(count);
                    city.setBirthSid(info.getServerId());
                    city.setCurSid(info.getServerId());
                    city.setColorCamp(info.getCampColor());
                    city.setServerName(info.getServerName());
                    city.setPlatName(info.getPlatName());
                    count++;
                }
            }
            else if (i < ServerMatchManager.serverMatchStage_8) {
                EightDiageramSeverInfo info =  groupWithServerllist.get(i);
                city.setServerName(info.getServerName());
                city.setBirthSid(info.getServerId());
                city.setCurSid(info.getServerId());
                city.setColorCamp(info.getCampColor());
                city.setPlatName(info.getPlatName());
            }
            citylist.put(city.getCityId(),city);
        }
        ServerParamUtil.groupCityInfos.put(groupID,citylist);
    }

    /**
     * 服务器划分
     */
    @Override
    public void eightDiagramsServerMarkOff() {
       if (EightDiagramsManager.getInstance().getEightDiagramsIsOpen()) {
           return;
       }
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.EIGHT_DIAGRAMS);
        if (bean == null) {
            log.info("八级阵图  daily id 未找到 "  +   DailyActiveDefine.EIGHT_DIAGRAMS);
            return;
        }
        if (bean.getCrossMatch().size() <=0) {
            log.info("  跨服分组  getCrossMatch  长度为0");
            return;
        }
        for ( Integer bigGroupID: ServerMatchManager.serverStageGrouplist.keySet()) {
            ConcurrentHashMap<Integer,ConcurrentHashMap<Integer,List<GameServerInfo>>>  serverMatchStage =   ServerMatchManager.serverStageGrouplist.get(bigGroupID);
            if (serverMatchStage.containsKey(ServerMatchManager.serverMatchStage_8)) {
                ConcurrentHashMap<Integer,List<GameServerInfo>> serverMatchStage_3 =  serverMatchStage.get(ServerMatchManager.serverMatchStage_8);
                boolean isAllEnough = true;
                for (Integer key:serverMatchStage_3.keySet()) {
                    List<GameServerInfo>  infoList = serverMatchStage_3.get(key);
                    infoList.sort(new ServerIDSort());
                    GameServerInfo info =  infoList.get(0);
                    int eightGroupID  = info.stageWithGroupIndex.get(ServerMatchManager.serverMatchStage_8);
                    if (infoList.size()>=ServerMatchManager.serverMatchStage_8) {
                        if ( ServerParamUtil.groupWithServer.containsKey(eightGroupID)) {
                            setPlatKeyGroupId(ServerParamUtil.groupWithServer.get(eightGroupID),eightGroupID);
                            continue;
                        }
                        isAllEnough = true;
                        int count = 0;
                        HashMap<Integer,List<GameServerInfo>> fourMathList = new HashMap<>();
                        List<GameServerInfo> fourGameInfoList = new ArrayList<>();
                        for (GameServerInfo i :infoList) {
                            int index = count / ServerMatchManager.serverMatchStage_4;
                            if (count % ServerMatchManager.serverMatchStage_4 == 0) {
                                fourGameInfoList = new ArrayList<>();
                            }
                            fourGameInfoList.add(i);
                            fourMathList.put(index,fourGameInfoList);
                            count++;
                            if(i.getIsMerge())
                                continue;
                            int openDay =  GameServerManager.getOpenServerDay(i.getOpenTime());
                            if (openDay < bean.getCrossMatch().get(1).get(1)) {
                                isAllEnough  = false;
                            }
                        }
                        if (isAllEnough) {
                            matchEightServer(  infoList, eightGroupID,  fourMathList );
                        }
                        else {
                            for(List<GameServerInfo> infolist :fourMathList.values()) {
                                if (infolist.size()>=ServerMatchManager.serverMatchStage_4) {
                                    machtFourServer(infolist,eightGroupID);
                                }
                            }
                        }
                    }
                    else if (infoList.size()>=ServerMatchManager.serverMatchStage_4) {
                        machtFourServer( infoList,eightGroupID);
                    }
                }
            }
        }
        resetCity();
    }

    private void  matchEightServer( List<GameServerInfo>  infoList,int eightGroupID,HashMap<Integer,List<GameServerInfo>> fourMathList)
    {

        if (ServerParamUtil.groupWithServer.containsKey(eightGroupID)){
            setPlatKeyGroupId(ServerParamUtil.groupWithServer.get(eightGroupID),eightGroupID);
            log.error("八级阵图8组ID 已包含  {}",eightGroupID );
            return;
        }
        List<EightDiageramSeverInfo> groupWithServerList = new ArrayList<>();
        int colorCamp = 1;
        for (GameServerInfo g :infoList) {
            EightDiageramSeverInfo edata = new EightDiageramSeverInfo();
            edata.setServerId(g.getServerId());
            edata.setPlatName(g.getPlatName());
            edata.setCampColor(colorCamp);
            edata.setServerName("");
            groupWithServerList.add(edata);
            String serverkey = g.getPlatName() +"_" +g.getServerId();
            EightDiagramsManager.getInstance().getServerPlat_SidToGroup().put(serverkey,eightGroupID);
            EightDiagramsManager.getInstance().getServerWithColorIDList().put(g.getServerId(),colorCamp);
            log.info("八级阵图8组划分  groupid  {}  serverId() {}  " ,eightGroupID ,g.getServerId());
            colorCamp++;
        }


        ServerParamUtil.groupWithServer.put(eightGroupID,groupWithServerList);
        setCityCamp(eightGroupID);


        //清理结算之前4服的成绩 奖励
        for (List<GameServerInfo> value : fourMathList.values()){
            int fourGroupID =  value.get(0).stageWithGroupIndex.get(ServerMatchManager.serverMatchStage_4);
            groupWithServerList =  ServerParamUtil.groupWithServer.get(fourGroupID);
            if (groupWithServerList!=null) {
                //跨到8服时候，结算前面4服的奖励
                if (   Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().containsKey(fourGroupID)) {
                    ConcurrentHashMap<Long, EightIntegralRankBean> eightMap = Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().get(fourGroupID);
                    settle( eightMap,3);
                    removePeriodRank(eightMap);
                    Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().remove(fourGroupID);
                    log.info("8服达成匹配 结算之前4服  {} " ,fourGroupID);
                }
                if (ServerParamUtil.groupCityInfos.containsKey(fourGroupID)) {
                    ServerParamUtil.groupCityInfos.remove(fourGroupID);
                }
                ServerParamUtil.groupWithServer.remove(fourGroupID);
            }
        }
    }

    private void machtFourServer(List<GameServerInfo> infolist,int eightGroupID)
    {

        if (ServerParamUtil.groupWithServer.containsKey(eightGroupID)){
            setPlatKeyGroupId(ServerParamUtil.groupWithServer.get(eightGroupID),eightGroupID);
            log.error("八级阵图8组阵营 已配对 groupID {}",eightGroupID );
            return;
        }

        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.EIGHT_DIAGRAMS);
        if (bean == null) {
            log.info("八级阵图  daily id 未找到 "  +   DailyActiveDefine.EIGHT_DIAGRAMS);
            return;
        }
        if (bean.getCrossMatch().size() <=0) {
            log.info("  跨服分组  getCrossMatch  长度为0");
            return;
        }
        int fourGroupID = infolist.get(0).stageWithGroupIndex.get(ServerMatchManager.serverMatchStage_4);
        if (ServerParamUtil.groupWithServer.containsKey(fourGroupID)){
            setPlatKeyGroupId(ServerParamUtil.groupWithServer.get(fourGroupID),fourGroupID);
            log.info("  八级阵图4组阵营 已配对 groupID {}",fourGroupID );
            return;
        }
        boolean isfourMatchEnough = true;
        List<EightDiageramSeverInfo> groupWithServerList = new ArrayList<>();
        int count = 0;
        for (GameServerInfo four:infolist) {
            if (count>=bean.getCrossMatch().get(0).get(0))
                break;
            count++;
            if (four.getIsMerge())
                continue;
            //bean.getCrossMatch().get(1)
            int openDay =  GameServerManager.getOpenServerDay(four.getOpenTime());
            if (openDay < bean.getCrossMatch().get(0).get(1)) {
                isfourMatchEnough =false;
                break;
            }
        }
        if (isfourMatchEnough) {
            int colorCamp = 1;
            for (GameServerInfo four:infolist) {
                if (colorCamp>ServerMatchManager.serverMatchStage_4)
                    break;
                EightDiageramSeverInfo edata = new EightDiageramSeverInfo();
                edata.setServerId(four.getServerId());
                edata.setPlatName(four.getPlatName());
                edata.setServerName("");
                groupWithServerList.add(edata);
                edata.setCampColor(colorCamp);
                String  keySer =  four.getPlatName() + "_" + four.getServerId() ;
                EightDiagramsManager.getInstance().getServerPlat_SidToGroup().put(keySer,fourGroupID);
                EightDiagramsManager.getInstance().getServerWithColorIDList().put(four.getServerId(),colorCamp);
                log.info("八级阵图4组划分  groupid  {}  keySer {}  " ,fourGroupID ,keySer);
                colorCamp++;
            }
            ServerParamUtil.groupWithServer.put(fourGroupID,groupWithServerList);
        }
    }

   public void F2PSendOverCityInfo(EightDiagramsMessage.F2PSendOverCityInfo message)
    {

        List<EightDiagramsMessage.EightCityAttribute> attributeslist = message.getEightCityOVerInfoList();
        try {
            for (EightDiagramsMessage.EightCityAttribute cab :attributeslist) {
                if (cab.getType() == 1) {
                    ConcurrentHashMap<Integer ,EightDiagramCity> cityInfo =
                            JsonUtils.parseObject(VersionUpdateUtil.dataLoad( cab.getParam()), new TypeReference<ConcurrentHashMap<Integer ,EightDiagramCity>>(){});
                    ServerParamUtil.groupCityInfos.put( cab.getValue(),cityInfo);
                }
                else if (cab.getType() == 2) {
                    ConcurrentHashMap<Long, EightIntegralRankBean> periodrankInfo =
                            JsonUtils.parseObject(VersionUpdateUtil.dataLoad( cab.getParam()), new TypeReference<ConcurrentHashMap<Long, EightIntegralRankBean>>(){});
                    updatePeriodRank(periodrankInfo);
                    EightDiagramsManager.getInstance().getPeriodOf_playerBattleInfo().put( cab.getValue(),periodrankInfo);
                    rankEightDiagram(periodrankInfo,EightDiagramsManager.getInstance().getPeriodOf_Ranklist50Num(),cab.getValue());
                }
                else if (cab.getType() == 3){
                    ConcurrentHashMap<Long, EightIntegralRankBean> dayrankInfo =
                            JsonUtils.parseObject(VersionUpdateUtil.dataLoad( cab.getParam()), new TypeReference<ConcurrentHashMap<Long, EightIntegralRankBean>>(){});
                    EightDiagramsManager.getInstance().getDayOf_playerBattleInfo().put( cab.getValue(),dayrankInfo);
                    rankEightDiagram(dayrankInfo,EightDiagramsManager.getInstance().getDayOf_Ranklist50Num(),cab.getValue());
                }
            }
            updateEightCitySql();
        }catch (Exception e) {
            log.error("F2PSendOverCityInfo" ,e);
        }
    }


    public void loadPeriodRank()
    {
        for (Integer groupID :EightDiagramsManager.getInstance().getPeriodOf_playerBattleInfo().keySet()) {
             ConcurrentHashMap<Long, EightIntegralRankBean> periodrankInfo =
                     EightDiagramsManager.getInstance().getPeriodOf_playerBattleInfo().get(groupID);
            rankEightDiagram(periodrankInfo,EightDiagramsManager.getInstance().getPeriodOf_Ranklist50Num(),groupID);
        }
    }

    private void rankEightDiagram(ConcurrentHashMap<Long, EightIntegralRankBean> rankInfo
            ,ConcurrentHashMap<Integer,List<EightIntegralRankBean>> setRanklist,int groupID)
    {
        List<EightIntegralRankBean> ranklsit = new ArrayList<>(rankInfo.values());
        ranklsit.sort(new EightDiagramsintegralSort());
        int sublen = ranklsit.size() >=EightDiagramsManager.Top50Num?EightDiagramsManager.Top50Num:ranklsit.size();
        List<EightIntegralRankBean>  num50ranklsit = ranklsit.subList(0,sublen);
        setRanklist.put(groupID,num50ranklsit);
    }
    /**
     * 周期结算
     * 每月的1号 和16号 结算
     */
    public void periodSettle()
    {

        log.info("---------------八级阵图周期计算检测 ---------------");
        int day =  TimeUtils.getDayOfMonth(TimeUtils.Time());
        if (day == 1 || day == 16) {
            for ( ConcurrentHashMap<Long, EightIntegralRankBean> ranklist :
                    Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().values()) {
                settle( ranklist, 3);
            }
            ServerParamUtil.groupWithServer.clear();
            ServerParamUtil.groupCityInfos.clear();
            EightDiagramsManager.getInstance().getServerPlat_SidToGroup().clear();
            EightDiagramsManager.getInstance().getServerWithColorIDList().clear();
            EightDiagramsManager.getInstance().getPeriodOf_Ranklist50Num().clear();
            Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().clear();
            Manager.eightDiagramsManager.getEightIntegralRankDao().clearAll();

            //从新分配
            eightDiagramsServerMarkOff();

            updateEightCitySql();
        }
    }

    public void settle( ConcurrentHashMap<Long, EightIntegralRankBean> ranklist,int type)
    {
        List<EightIntegralRankBean> allRanklsit = new ArrayList<>(ranklist.values());
        allRanklsit.sort(new EightDiagramsintegralSort());
        int sublen = allRanklsit.size() >=EightDiagramsManager.Top100Num?EightDiagramsManager.Top100Num:allRanklsit.size();
        allRanklsit =  allRanklsit.subList(0,sublen);
        int rank = 1;
        ConcurrentHashMap<String,ConcurrentHashMap<Long,Integer>> sendToPlayerlist = new ConcurrentHashMap<>();
        for (EightIntegralRankBean integral:allRanklsit) {
            if (!sendToPlayerlist.containsKey(integral.getPlatSid())) {
                ConcurrentHashMap<Long,Integer> rolelist = new ConcurrentHashMap<>();
                rolelist.put(integral.getRoleID(),rank);
                sendToPlayerlist.put(integral.getPlatSid(),rolelist);
            }
            else {
                ConcurrentHashMap<Long,Integer> rolelist =  sendToPlayerlist.get(integral.getPlatSid());
                rolelist.put(integral.getRoleID(),rank);
            }
            rank++;
        }
        for (String patSid:sendToPlayerlist.keySet()) {
            EightDiagramsMessage.P2GSendEightCityRward.Builder msg
                    = EightDiagramsMessage.P2GSendEightCityRward.newBuilder();
            EightDiagramsMessage.RewardInfo.Builder rewardInfo;
            List< EightDiagramsMessage.RewardInfo> rewardInfoList = new ArrayList<>();
            ChannelHandlerContext session  =  GameServerManager.getInstance().GetSession(patSid);
            ConcurrentHashMap<Long,Integer>  roleList = sendToPlayerlist.get(patSid);
            if (session!=null) {
                msg.setRewardType(type);
                for (Long  roleID :roleList.keySet()) {
                     rewardInfo = EightDiagramsMessage.RewardInfo.newBuilder();
                    int selfrank =  roleList.get(roleID);
                    rewardInfo.setRoleID(roleID);
                    rewardInfo.setValue(selfrank);
                    rewardInfoList.add(rewardInfo.build());
                }
                msg.addAllRewardInfo(rewardInfoList);
                MessageUtils.send_to_game(session, EightDiagramsMessage.P2GSendEightCityRward.MsgID.eMsgID_VALUE,msg.build().toByteArray());
            }
        }
        log.info("八级阵图赛季奖励  发放类型 type = {} " , type);
    }

    private int  findFightSessionFoGroupID(int groupID)
    {
        if (ServerParamUtil.groupCityInfos.containsKey(groupID)) {
            ConcurrentHashMap<Integer, EightDiagramCity> cityList =
                    ServerParamUtil.groupCityInfos.get(groupID);
            EightDiagramCity city =  cityList.get(1);
            if (city != null) {
               FightRoom fr =   FightManager.getInstance().getFrcache().get(city.getRoomID());
               if (fr!=null) {
                  return fr.getServerId();
               }
            }
        }
        return -1;
    }

    private void setPlatKeyGroupId( List<EightDiageramSeverInfo> diageramSeverInfos,int groupId){
        for (EightDiageramSeverInfo info : diageramSeverInfos){
            String platKey = info.getPlatName() + "_" + info.getServerId();
            EightDiagramsManager.getInstance().getServerPlat_SidToGroup().put(platKey,groupId);
            EightDiagramsManager.getInstance().getServerWithColorIDList().put(info.getServerId(),info.getCampColor());
        }
    }

    /**
     * 移除积分榜
     * @param ranklist
     */
    private void removePeriodRank( ConcurrentHashMap<Long, EightIntegralRankBean> ranklist){
        for (Map.Entry<Long, EightIntegralRankBean> entry : ranklist.entrySet()){
            Manager.eightDiagramsManager.getEightIntegralRankDao().delete(entry.getKey());
        }
        ranklist.clear();
    }

    private void updatePeriodRank(ConcurrentHashMap<Long, EightIntegralRankBean> ranklist){
        for (Map.Entry<Long, EightIntegralRankBean> entry:ranklist.entrySet()){
            Manager.eightDiagramsManager.getEightIntegralRankDao().update(entry.getValue());
        }
    }

    /**
     * 八级阵图如果遇到合服情况修复
     */
    public void onEightDiagramRepair(ServerInfo info){

        String curKey = "";
        for(Integer id : info.getSids()) {
            if (id == info.getServerId()) {
                curKey = ServerMatchManager.makeKey(info.getPlatName(),info.getServerId());
            }
        }
        int curGroupId = 0;
        if (!Manager.eightDiagramsManager.getServerPlat_SidToGroup().containsKey(curKey)){
            log.error("目标服务器 没有达成 8级阵图匹配  curKey {}" ,curKey);
            return;
        }
        curGroupId =  EightDiagramsManager.getInstance().getServerPlat_SidToGroup().get(curKey);
        ConcurrentHashMap<Long, EightIntegralRankBean> curPeriodlsit = Manager.eightDiagramsManager.
                getPeriodOf_playerBattleInfo().getOrDefault(curGroupId,new  ConcurrentHashMap<>());
        for(Integer id : info.getSids()) {
            if (id == info.getServerId()){
                continue;
            }
            String platServerId =  ServerMatchManager.makeKey(info.getPlatName(),id);
            int oldGroupID = 0;
            if (!Manager.eightDiagramsManager.getServerPlat_SidToGroup().containsKey(platServerId)){
                continue;
            }
            oldGroupID = Manager.eightDiagramsManager.getServerPlat_SidToGroup().get(platServerId);
            if(oldGroupID == curGroupId){
                continue;
            }
            if ( !Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().containsKey(oldGroupID)){
                continue;
            }
            ConcurrentHashMap<Long, EightIntegralRankBean> oldPeriodlsit = Manager.eightDiagramsManager.
                    getPeriodOf_playerBattleInfo().get(oldGroupID);
            Iterator<Entry<Long, EightIntegralRankBean>> iter = oldPeriodlsit.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<Long, EightIntegralRankBean> en = iter.next();
                if ( en.getValue().getServerid() != id){
                    continue;
                }
                curPeriodlsit.put(en.getKey(),en.getValue());
                iter.remove();
            }
            Manager.eightDiagramsManager.getServerPlat_SidToGroup().remove(platServerId);
        }
    }
}
