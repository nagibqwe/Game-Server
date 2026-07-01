package common.eightDiagrams;

import com.data.*;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Daily_Bean;
import com.data.bean.Cfg_EightCityReward_Bean;
import com.data.bean.Cfg_EightCity_Bean;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.copymap.structs.CopyMapType;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.db.bean.EightIntegralRankBean;
import com.game.eightdiagrams.manger.EightDiagramsManager;
import com.game.eightdiagrams.script.IEightDiagrams;
import com.game.eightdiagrams.structs.CityBattleProgress;
import com.game.eightdiagrams.structs.EightDiagramCity;
import com.game.eightdiagrams.structs.EightDiagramsintegralSort;
import com.game.eightdiagrams.timer.EightDiagramsTimer;
import com.game.fightserver.manager.FightClientManager;
import com.game.fightserver.struct.FightClient;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.player.structs.SessionAttribute;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.utils.MessageUtils;
import game.core.json.TypeReference;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.JsonUtils;
import game.core.util.VersionUpdateUtil;
import game.message.DailyactiveMessage;
import game.message.EightDiagramsMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 542 on 2019/9/27.
 */
public class EightDiagramsScript implements IScript,IEightDiagrams {

    private static final Logger log = LogManager.getLogger(EightDiagramsScript.class);

    private static final Object obj = new Object();

    @Override
    public int getId() {
        return ScriptEnum.EightDiagramsScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


    @Override
    public void online(Player player) {
        DailyactiveMessage.G2PReqCrossServerMatch.Builder msg = DailyactiveMessage.G2PReqCrossServerMatch.newBuilder();
        msg.setRoleid(player.getId());
        MessageUtils.send_to_public(DailyactiveMessage.G2PReqCrossServerMatch.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public void EightDiagramsChangeState(boolean isOpen) {
        Manager.eightDiagramsManager.eightDiagramsIsOpen = isOpen;
        if (isOpen)
            init();
         else
            F2PSendOverCityInfo();
    }

    private void init() {
        Manager.eightDiagramsManager.getDayOf_Ranklist100Num().clear();
        Manager.eightDiagramsManager.getDayOf_playerBattleInfo().clear();
        eightDiagramsTimerLoop();
    }

    /**
     * 请求打开界面
     */
    public void ReqEightDiagramsPanel(Player player, EightDiagramsMessage.ReqEightDiagramsPanel message) {
        Cfg_Daily_Bean daily_bean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.EIGHT_DIAGRAMS.getValue());
        if (daily_bean == null)
            return;
        //是否满足等级开启条件
        if (player.getLevel() < daily_bean.getOpenLevel()) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Daily_Level_Not_Enough);
            return;
        }
        EightDiagramsMessage.G2PReqEightDiagramsPanel.Builder msg
                = EightDiagramsMessage.G2PReqEightDiagramsPanel.newBuilder();
        msg.setRoleID(player.getId());
        MessageUtils.send_to_public( EightDiagramsMessage.G2PReqEightDiagramsPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    /**
     * 请求排行
     */
    public void ReqRankPanel(Player player, EightDiagramsMessage.ReqRankPanel message) {
        EightDiagramsMessage.G2PReqRankPanel.Builder msg = EightDiagramsMessage.G2PReqRankPanel.newBuilder();
        msg.setRoleID(player.getId());
        MessageUtils.send_to_public( EightDiagramsMessage.G2PReqRankPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public void P2FReqEightDiagramsPanel(EightDiagramsMessage.P2FReqEightDiagramsPanel message) {
        long roleID = message.getRoleID();
        int groupID = message.getGroupID();
        int serverID = message.getServerID();
        int camp = message.getCamp();
        String servername = message.getServerName();
        int fightSid = message.getFightSid();


        EightDiagramsMessage.G2FReqEightDiagramsPanel.Builder msg =  EightDiagramsMessage.G2FReqEightDiagramsPanel.newBuilder();
        msg.setRoleID(roleID);
        msg.setCamp(camp);
        msg.setGroupID(groupID);
        msg.setServerID(serverID);
        msg.setServerName(servername);
        ConnectFightManager.GetInstance().send_to_fight(fightSid, roleID,  EightDiagramsMessage.G2FReqEightDiagramsPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }

    public void P2FReqRankPanel(EightDiagramsMessage.P2FReqRankPanel message) {
        long roleID = message.getRoleID();
        int groupID = message.getGroupID();
        int fightSid = message.getFightSid();

        EightDiagramsMessage.G2FReqRankPanel.Builder msg =  EightDiagramsMessage.G2FReqRankPanel.newBuilder();
        msg.setRoleID(roleID);
        msg.setGroupID(groupID);
        ConnectFightManager.GetInstance().send_to_fight(fightSid, roleID,  EightDiagramsMessage.G2FReqRankPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
    /**
     * 请求地图信息
     */
    public void  ReqTickMapInfo(Player player, EightDiagramsMessage.ReqTickMapInfo message) {
       // if (!Manager.eightDiagramsManager.eightDiagramsIsOpen) {
       //     MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldAnswerAppError);
       //     log.info("活动未开启");
       //     return;
       // }

        EightDiagramsMessage.G2PReqTickMapInfo.Builder msg =
                EightDiagramsMessage.G2PReqTickMapInfo.newBuilder();
        msg.setRoleID(player.getId());
        msg.setCityID(message.getCityID());
        MessageUtils.send_to_public( EightDiagramsMessage.G2PReqTickMapInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
    /**
     * 请求进入地图
     */
    public void ReqEnterEightCityMap(Player player, EightDiagramsMessage.ReqEnterEightCityMap message) {

        Cfg_Daily_Bean daily_bean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.EIGHT_DIAGRAMS.getValue());
        if (daily_bean == null)
            return;
        //是否满足等级开启条件
        if (player.getLevel() < daily_bean.getOpenLevel()) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Daily_Level_Not_Enough);
            return;
        }

       //if (!Manager.eightDiagramsManager.eightDiagramsIsOpen) {
       //    MessageUtils.notify_player(player,Notify.ERROR, MessageString.WorldAnswerAppError);
       //    log.info("活动未开启");
       //    return;
       //}

        if (  player.playerCrossData.isToFightServer() ){
            Cfg_Clone_map_Bean cloneMapCfg = CfgManager.getCfg_Clone_map_Container().getValueByKey(player.playerCrossData.toZoneModelId);
            if (cloneMapCfg !=null){
                if (cloneMapCfg.getType() != CopyMapType.EightCity_CopyMap ){
                    log.error("不能跨服里面 切换跨服 ");
                    return;
                }
            }
        }
        EightDiagramsMessage.G2PReqEnterEightCityMap.Builder msg =
                EightDiagramsMessage.G2PReqEnterEightCityMap.newBuilder();
        msg.setRoleID(player.getId());
        msg.setCityID(message.getCityID());
        MessageUtils.send_to_public( EightDiagramsMessage.G2PReqEnterEightCityMap.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
    /**
     * 发奖
     */
    public void P2GSendEightCityRward(EightDiagramsMessage.P2GSendEightCityRward message) {

        int type =message.getRewardType();
        if (type == EightDiagramsManager.Occupy_City_Reward) {
            List<EightDiagramsMessage.RewardInfo>  rewardInfoList =  message.getRewardInfoList();
            for(int i  = 0; i<rewardInfoList.size();i++) {
                long roleID =  rewardInfoList.get(i).getRoleID();
                int value = rewardInfoList.get(i).getValue();
                Cfg_EightCity_Bean  bean = CfgManager.getCfg_EightCity_Container().getValueByKey(value);
                if (bean==null) {
                    log.error("配置表  EightCity  无法查找 value" + value);
                    continue;
                }
                List<Item> rewardItem = Item.createItems( bean.getReward());
                Manager.mailManager.sendMailToPlayer(roleID, 1, MessageString.System,
                        MessageString.EightCityParticipantTitle, MessageString.EightCityParticipantReward, rewardItem, ItemChangeReason.EightCityParticipantReward);
                Player player = PlayerManager.getInstance().getPlayer(roleID);
                if(player != null){
                    Manager.controlManager.operate(player, FunctionVariable.Bajizhentu_hold_city, 1);
                }
            }
        }
        else {
            sendIntegralReward(message);
        }
        log.info("八级阵图奖励发放  发放类型 type = {} " , type);
    }

    private void sendIntegralReward(EightDiagramsMessage.P2GSendEightCityRward message) {
        int messageID =  message.getRewardType() == EightDiagramsManager.IntegralSettle?MessageString.EightCityIntegralRward:MessageString.EightCitySeasonRward;
        List<EightDiagramsMessage.RewardInfo>  rewardInfoList =  message.getRewardInfoList();
        int rewardType = message.getRewardType() -1;
        for(int i  = 0; i<rewardInfoList.size();i++) {
            long roleID =  rewardInfoList.get(i).getRoleID();
            if (message.getRewardType() == EightDiagramsManager.IntegralSettle){
                Player player = Manager.playerManager.getPlayerCache(roleID);
                if (player != null) {
                    Manager.controlManager.operate(player, FunctionVariable.EightCity_Reward_Num, 1);
                    Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.EightCity_Reward_Num, Count.RefreshType.CountType_Forever, 1);
                }
            }
            int value = rewardInfoList.get(i).getValue();
            for (Cfg_EightCityReward_Bean bean :  CfgManager.getCfg_EightCityReward_Container().getValuees()) {
                if (bean.getType() == rewardType && value  <=  bean.getRank().get(1) && value  >= bean.getRank().get(0)) {
                    List<Item> rewardItem = Item.createItems( bean.getReward());
                    Manager.mailManager.sendMailToPlayer(roleID, 1, MessageString.System,
                            MessageString.EightCityParticipantTitle, messageID, rewardItem, ItemChangeReason.EightCityIntegralReward);
                    break;
                }
            }
        }
    }

    public void P2FRepChangeCityMap(EightDiagramsMessage.P2FRepChangeCityMap message) {
        long roleID =  message.getRoleID();
        long mapID =message.getMapID();
        Player player  =  PlayerManager.getInstance().getPlayerOnline(roleID);
        if (player != null) {
            MapObject map = Manager.mapManager.getMap(mapID);
            Manager.mapManager.getMap(player.gainMapId());
            if (map != null) {
                Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(map.getMapModelId());
                if (bean.getType() == CopyMapType.EightCity_CopyMap) {//八级阵图出生点判断
                    int curSid = MapParam.getEightDaramsMapData(map).getCurSid();
                    int sourceServerID = FightClientManager.getServerIdInFightServer(player);
                    Position position = sourceServerID == curSid ?  map.getBriths().get(0):map.getBriths().get(1);
                    Manager.mapManager.changeMap(player, map.getId(), position, false);
                }
            }
        }
    }

    /**
     * //玩家对BOSS伤害
     */
    public void  playerToBossHurt(long hurt,int groupID,int cityID,long bossCurHP,Player player) {
        if (!Manager.eightDiagramsManager.eightDiagramsIsOpen)
            return;
        synchronized(obj) {
            //参与玩家统计
            FightClient fc = player.getIosession().channel().attr(SessionAttribute.FIGHT_CLIENT_INFO).get();
            if (fc == null)
                return;
            int sid =fc.getSid();
            if (  Manager.eightDiagramsManager.getParticipationList().containsKey(sid)) {
                ConcurrentHashMap<Long, String> roleList =   Manager.eightDiagramsManager.getParticipationList().get(sid);
                if (!roleList.containsKey(player.getId())) {
                    roleList.put(player.getId(),player.playerCrossData.platSid);
                }
            }
            else {
                ConcurrentHashMap<Long, String> roleList = new ConcurrentHashMap<>();
                roleList.put(player.getId(),player.playerCrossData.platSid);
                Manager.eightDiagramsManager.getParticipationList().put(sid,roleList);
            }

            //统计每个服务器对城市的总伤害
            ConcurrentHashMap<Integer, EightDiagramCity> citylist
                    =  Manager.eightDiagramsManager.getGroupCityInfos().get(groupID);
            EightDiagramCity cityifo = citylist.get(cityID);
            if (cityifo != null) {
                cityifo.setCurBossHp(bossCurHP);
                // cityifo.setMaxBossHp(bossMaxHP);
                setCityInfo(0, hurt, cityID,player, groupID);
            }

            //统计每个人的总伤害
            syncBattleRankInfo( player, groupID, hurt,0
                    , Manager.eightDiagramsManager.getDayOf_playerBattleInfo(),player.playerCrossData.platSid ,sid);
        }
    }

    private void syncBattleRankInfo(Player player,int groupID,long hurt,int integral,
                                    ConcurrentHashMap<Integer,ConcurrentHashMap<Long, EightIntegralRankBean>> playerBattleInfo,
                                    String platSid,int serverid )
    {
        ConcurrentHashMap<Long, EightIntegralRankBean> dayPlayerBattleInfoList = null;
        EightIntegralRankBean diagramIntegral = null;
        long roleID = player.getId();
        String roleName = player.getName();
        int colorCamp = player.getCamp();
        if (playerBattleInfo.containsKey(groupID)) {
            dayPlayerBattleInfoList = playerBattleInfo.get(groupID);
            if (!dayPlayerBattleInfoList.containsKey(roleID)){
                diagramIntegral = new EightIntegralRankBean();
                diagramIntegral.setRoleID(roleID);
                diagramIntegral.setName(roleName);
                diagramIntegral.setColorCamp(colorCamp);
                diagramIntegral.setGroupId(groupID);
                dayPlayerBattleInfoList.put(roleID,diagramIntegral);
            }
            diagramIntegral = dayPlayerBattleInfoList.get(roleID);
            diagramIntegral.setHurt(diagramIntegral.getHurt() + hurt);
            diagramIntegral.setIntegral(diagramIntegral.getIntegral() + integral);
            diagramIntegral.setPlatSid(platSid);
            diagramIntegral.setServerid(serverid);
        }
        else {
            diagramIntegral = new EightIntegralRankBean();
            dayPlayerBattleInfoList =  new ConcurrentHashMap<>();
            diagramIntegral.setRoleID(roleID);
            diagramIntegral.setName(roleName);
            diagramIntegral.setHurt(diagramIntegral.getHurt() + hurt);
            diagramIntegral.setPlatSid(platSid);
            diagramIntegral.setIntegral(diagramIntegral.getIntegral() + integral);
            diagramIntegral.setColorCamp(colorCamp);
            diagramIntegral.setServerid(serverid);
            diagramIntegral.setGroupId(groupID);
            dayPlayerBattleInfoList.put(roleID,diagramIntegral);
            playerBattleInfo.put(groupID,dayPlayerBattleInfoList);
        }
    }
    private void setCityInfo(int playerChangeValue,long bosHurtValue,int cityID,Player player,int groupID)
    {
        ConcurrentHashMap<Integer ,EightDiagramCity>  citylist
                =  Manager.eightDiagramsManager.getGroupCityInfos().get(groupID);
        EightDiagramCity  cityifo = citylist.get(cityID);
        FightClient fc = player.getIosession().channel().attr(SessionAttribute.FIGHT_CLIENT_INFO).get();
        if (cityifo!=null && fc!=null) {
            if ( cityifo.getCityBattleProgressList().containsKey(fc.getSid())) {
                CityBattleProgress battleInfo = cityifo.getCityBattleProgressList().get(fc.getSid());
                if (battleInfo!=null) {
                    int num = battleInfo.getPlayerNum() + playerChangeValue ;
                    num = num < 0 ? 0:num;
                    battleInfo.setPlayerNum(num);
                    battleInfo.setBossHurt(bosHurtValue);
                }
            }
            else {
                CityBattleProgress battleInfo = new CityBattleProgress();
                battleInfo.setSid(fc.getSid());
                battleInfo.setBossHurt(bosHurtValue);
                battleInfo.setPlayerNum(playerChangeValue);
                battleInfo.setColorCamp(player.getCamp());
                battleInfo.setServerName("");
                cityifo.getCityBattleProgressList().put(fc.getSid() ,battleInfo);
            }
        }
    }
    /**
     * 杀死玩家
     */
    public void killPlayer(int groupID,Player player)
    {
        if (!Manager.eightDiagramsManager.eightDiagramsIsOpen)
            return;
        synchronized(obj) {
            String platSid = player.playerCrossData.platSid;
            FightClient fc = player.getIosession().channel().attr(SessionAttribute.FIGHT_CLIENT_INFO).get();
            if (fc == null)
                return;
            int sid = fc.getSid();
            syncBattleRankInfo(player, groupID, 0, Global.Eight_City_Count_PK, Manager.eightDiagramsManager.getDayOf_playerBattleInfo(),platSid,sid );
        }
    }

    /**
     * 杀死BOSS
     */
    public void killBoss(Player player ,int cityID,int groupID,int colorCamp)
    {

        if (!Manager.eightDiagramsManager.eightDiagramsIsOpen)
            return;
        synchronized(obj) {
            FightClient fc = player.getIosession().channel().attr(SessionAttribute.FIGHT_CLIENT_INFO).get();
            if (fc == null)
                return;
            int sid = fc.getSid();

            if ( Manager.eightDiagramsManager.getGroupCityInfos().containsKey(groupID)) {
                ConcurrentHashMap<Integer, EightDiagramCity> citylist
                        =  Manager.eightDiagramsManager.getGroupCityInfos().get(groupID);
                EightDiagramCity cityifo = citylist.get(cityID);
                if (cityifo != null) {
                    cityifo.setCurSid(sid);
                    cityifo.setServerName("");
                    cityifo.setColorCamp(colorCamp);
                    cityifo.setCurBossHp(cityifo.getMaxBossHp());
                }
            }
        }
    }
    /**
     * 玩家进入城市
     */
    public void enterMapSucc(Player player,int cityID,int groupID)
    {
        if (!Manager.eightDiagramsManager.eightDiagramsIsOpen)
            return;
        synchronized(obj) {
            FightClient fc = player.getIosession().channel().attr(SessionAttribute.FIGHT_CLIENT_INFO).get();
            if (fc == null)
                return;
            setCityInfo(1,0,cityID,player,groupID);
            ChannelHandlerContext session = player.getIosession();
            if (session!=null) {
                int selfCamp =  player.getCamp();
                sendMapInfo( groupID,cityID, player,selfCamp);
            }
        }
    }
    /**
     * 玩家退出城市
     */
    public void playerOutCity(Player player,int cityID,int groupID) {
        if (!Manager.eightDiagramsManager.eightDiagramsIsOpen)
            return;
        synchronized(obj) {
            if (player == null)
                return;
            setCityInfo(-1,0,cityID,player,groupID);
        }
    }


    /**
     * 活动开始公共服发送过来的 排行 城市 等数据
     */

    public  void P2FSendEightCityInfo(EightDiagramsMessage.P2FSendEightCityInfo message) {

        try {
            List<EightDiagramsMessage.EightCityAttribute> cityInfoList =   message.getEightCityStartInfoList();
            for (EightDiagramsMessage.EightCityAttribute cab :cityInfoList ) {
                if (cab.getType() == 1) {
                    if ( ! Manager.eightDiagramsManager.getGroupCityInfos().containsKey(cab.getValue())) {
                        ConcurrentHashMap<Integer ,EightDiagramCity> cityInfo =
                                JsonUtils.parseObject(VersionUpdateUtil.dataLoad(cab.getParam()), new TypeReference<ConcurrentHashMap<Integer ,EightDiagramCity>>(){});
                        Manager.eightDiagramsManager.getGroupCityInfos().put(cab.getValue(),cityInfo);
                    }
                }
                else  if (cab.getType() == 2) {
                    if (!Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().containsKey(cab.getValue())) {
                        ConcurrentHashMap<Long, EightIntegralRankBean> rankInfo =
                                JsonUtils.parseObject(VersionUpdateUtil.dataLoad( cab.getParam()), new TypeReference<ConcurrentHashMap<Long, EightIntegralRankBean>>(){});

                        List<EightIntegralRankBean> ranklsit = new ArrayList<>(rankInfo.values());
                        ranklsit.sort(new EightDiagramsintegralSort());
                        int sublen = ranklsit.size() >=50?50:ranklsit.size();
                        List<EightIntegralRankBean>  num50ranklsit = ranklsit.subList(0,sublen);
                        Manager.eightDiagramsManager.getPeriodOf_Ranklist50Num().put(cab.getValue(),num50ranklsit);
                        Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().put(cab.getValue(),rankInfo);
                    }
                }
            }
        }catch (Exception e)
        {
            log.error("P2FSendEightCityInfo" ,e);
        }
    }
    /**
     * 请求地图信息以及当前排行
     */
    public void P2FReqTickRankPanel(EightDiagramsMessage.P2FReqTickRankPanel message) {
        long roleID = message.getRoleID();
        int groupID = message.getGroupID();
        int cityID = message.getCityID();
        Player player =  PlayerManager.getInstance().getPlayerCache(roleID);
        if (player!=null) {
            sendMapInfo( groupID,  cityID,  player, player.getCamp());
        }
    }
    private void sendMapInfo(int groupID, int cityID, Player player, int selfCamp) {
        EightDiagramsMessage.ResTickMapInfo.Builder msg =  EightDiagramsMessage.ResTickMapInfo.newBuilder();
        EightDiagramsMessage.CityInfo.Builder cityInfo = EightDiagramsMessage.CityInfo.newBuilder();
        if ( Manager.eightDiagramsManager.getGroupCityInfos().containsKey(groupID)) {
            ConcurrentHashMap<Integer ,EightDiagramCity> cityList =
                    Manager.eightDiagramsManager.getGroupCityInfos().get(groupID);
            if (cityList  != null && cityList.size() >0) {
                if (cityList.containsKey(cityID)) {
                    EightDiagramCity city = cityList.get(cityID);
                    if (city!=null) {
                        setCityInfoMessage(cityInfo,city);
                        msg.setCurCityInfo(cityInfo);
                    }
                }
            }
            EightDiagramsMessage.RankInfo.Builder myRankInfo = EightDiagramsMessage.RankInfo.newBuilder();
            //自己的排名信息
            int integral = 0;
            String myName = "";
            int selfRank = -1;
            int serverid = 0;
            int colorCamp = 0;
            if (Manager.eightDiagramsManager.getDayOf_Ranklist100Num().containsKey(groupID)) {
                List<EightIntegralRankBean> ranklsit =
                        Manager.eightDiagramsManager.getDayOf_Ranklist100Num().get(groupID);
                int sublen = ranklsit.size() >=50?50:ranklsit.size();
                List<EightIntegralRankBean> dayRanklsit =  ranklsit.subList(0,sublen);
                int rank = 1;
                selfRank = dayRanklsit.size();
                for (EightIntegralRankBean integralRankBean : dayRanklsit) {
                    EightDiagramsMessage.RankInfo.Builder curRankInfo = EightDiagramsMessage.RankInfo.newBuilder();
                    if (integralRankBean.getAllIntegral() <=0){
                        continue;
                    }
                    curRankInfo.setIntegral(integralRankBean.getAllIntegral());
                    curRankInfo.setName(integralRankBean.getName());
                    curRankInfo.setRank(rank);
                    curRankInfo.setServerid(integralRankBean.getServerid());
                    curRankInfo.setColorCamp(integralRankBean.getColorCamp());
                    msg.addCurRankInfoList(curRankInfo.build());
                    if (integralRankBean.getRoleID() == player.getId()) {
                        selfRank = rank;
                    }
                    rank++;
                }
                ConcurrentHashMap<Long, EightIntegralRankBean> dayPlayerBattleInfoList =
                        Manager.eightDiagramsManager.getDayOf_playerBattleInfo().get(groupID);
                if (dayPlayerBattleInfoList.containsKey(player.getId())) {
                    integral =  dayPlayerBattleInfoList.get(player.getId()).getAllIntegral();
                    myName = dayPlayerBattleInfoList.get(player.getId()).getName();
                    serverid = dayPlayerBattleInfoList.get(player.getId()).getServerid();
                    colorCamp = dayPlayerBattleInfoList.get(player.getId()).getColorCamp();
                }
            }
            myRankInfo.setName(myName);
            myRankInfo.setIntegral(integral);
            myRankInfo.setRank(selfRank);
            myRankInfo.setServerid(serverid);
            myRankInfo.setColorCamp(colorCamp);
            msg.setSelfRankInfo(myRankInfo.build());
            msg.setSelfCamp(selfCamp);
            MessageUtils.send_to_player(player,
                    EightDiagramsMessage.ResTickMapInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }
    /**
     * 请求排行
     */
   public void G2FReqRankPanel(ChannelHandlerContext context,EightDiagramsMessage.G2FReqRankPanel message)
   {
        long roleID = message.getRoleID();
        int groupID = message.getGroupID();
       try {
           EightDiagramsMessage.ResRankPanel.Builder resRankPanel =  EightDiagramsMessage.ResRankPanel.newBuilder();
           if (Manager.eightDiagramsManager.getDayOf_playerBattleInfo().containsKey(groupID)) {
               ConcurrentHashMap<Long, EightIntegralRankBean> dayPlayerBattleInfoList =
                       Manager.eightDiagramsManager.getDayOf_playerBattleInfo().get(groupID);
               List<EightIntegralRankBean> day100NumList =
                       Manager.eightDiagramsManager.getDayOf_Ranklist100Num().get(groupID);
               if (dayPlayerBattleInfoList!=null && dayPlayerBattleInfoList.size()>0) {
                   addRankMessageInfo(day100NumList, dayPlayerBattleInfoList, resRankPanel,1,roleID);
               }
           }
           if ( Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().containsKey(groupID)) {
               ConcurrentHashMap<Long, EightIntegralRankBean> periodPlayerBattleInfoList =
                       Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().get(groupID);
               List<EightIntegralRankBean> period50NumList =
                       Manager.eightDiagramsManager.getPeriodOf_Ranklist50Num().get(groupID);
               if (periodPlayerBattleInfoList != null && periodPlayerBattleInfoList.size()>0) {
                   addRankMessageInfo(period50NumList, periodPlayerBattleInfoList, resRankPanel,2,roleID);
               }
           }
           List<Long> roleIds = new ArrayList<>();
           roleIds.add(roleID);
           FightClientManager.GetInstance().send_to_player(context, roleIds, EightDiagramsMessage.ResRankPanel.MsgID.eMsgID_VALUE, resRankPanel.build().toByteArray(), roleID);
       }catch (Exception e) {
           log.error("P2FReqRankPanel" ,e);
       }
   }
    private void addRankMessageInfo( List<EightIntegralRankBean> ranklist,ConcurrentHashMap<Long, EightIntegralRankBean> playerBattleInfoList,
                                     EightDiagramsMessage.ResRankPanel.Builder resRankPanel,int type,long roleID) {
        int selfRank = -1;
        int rank = 1;
        for (EightIntegralRankBean integral : ranklist) {
            EightDiagramsMessage.RankInfo.Builder curRankInfo = EightDiagramsMessage.RankInfo.newBuilder();
            if(integral.getAllIntegral() <= 0){
                continue;
            }
            curRankInfo.setIntegral(integral.getAllIntegral());
            curRankInfo.setRank(rank);
            curRankInfo.setName(integral.getName());
            curRankInfo.setColorCamp(integral.getColorCamp());
            curRankInfo.setServerid(integral.getServerid());
            if (integral.getRoleID() == roleID)
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
            myColorCamp = playerBattleInfoList.get(roleID).getColorCamp();
            myName = playerBattleInfoList.get(roleID).getName();
            serverid = playerBattleInfoList.get(roleID).getServerid();
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
     * 打开 主面板 信息
     */
   public void G2FReqEightDiagramsPanel(ChannelHandlerContext context,EightDiagramsMessage.G2FReqEightDiagramsPanel message)
   {
       long roleID = message.getRoleID();
       int groupID = message.getGroupID();
       int serverID = message.getServerID();
       int camp = message.getCamp();
       String servername = message.getServerName();

       try {
           if ( Manager.eightDiagramsManager.getGroupCityInfos().containsKey(groupID)) {
               ConcurrentHashMap<Integer ,EightDiagramCity> cityList =
                       Manager.eightDiagramsManager.getGroupCityInfos().get(groupID);
               if (cityList  != null && cityList.size() >0) {
                   EightDiagramsMessage.ResEightDiagramsPanel.Builder msg =  EightDiagramsMessage.ResEightDiagramsPanel.newBuilder();
                   msg.setSelfCamp(camp);
                   msg.setSelfServerName(servername);
                   msg.setSelfSid(serverID);
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
                   msg.setIsopen(true);
                   List<Long> roleIds = new ArrayList<>();
                   roleIds.add(roleID);
                   FightClientManager.GetInstance().send_to_player(context, roleIds, EightDiagramsMessage.ResEightDiagramsPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray(), roleID);

               }
           }
           else {
               log.info("未找到组ID "  + groupID  + "  对应的城市列表");
               return;
           }
       }catch (Exception e) {
           log.error("P2FReqEightDiagramsPanel" ,e);
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
            cityInfo.setServerName("");
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
                cityBattleInfo.setServerName("");
                cityInfo.addCityBattleInfoList(cityBattleInfo.build());
            }
        }
    }

    /**
     * 活动结束发送信息回公共服存储
     */
    public void F2PSendOverCityInfo() {
      mergeCurIntergralToPeriodkInfo();
      sendOverCityInfoToPublic();//活动结束发送结果给公共服统计
      activityOverSettle();//活动结束发送占领奖励
      integralSettle();//当天积分奖励
      clearAll();
   }

    /**
     * 当前的活动积分合并到周期
     */
   private void mergeCurIntergralToPeriodkInfo() {
       for (Integer groupID: Manager.eightDiagramsManager.getDayOf_playerBattleInfo().keySet()) {
           ConcurrentHashMap<Long, EightIntegralRankBean> daylist =
                   Manager.eightDiagramsManager.getDayOf_playerBattleInfo().get(groupID);
           if (Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().containsKey(groupID)) {
               ConcurrentHashMap<Long, EightIntegralRankBean> periodList =
                       Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().get(groupID);
               for (EightIntegralRankBean inter:daylist.values()) {
                  if (periodList.containsKey(inter.getRoleID())) {
                      EightIntegralRankBean pinter =  periodList.get(inter.getRoleID());
                      pinter.setHurt(pinter.getHurt()+ inter.getHurt());
                      pinter.setIntegral(pinter.getIntegral()+inter.getIntegral());
                  }
                  else {
                      periodList.put(inter.getRoleID(),inter);
                  }
               }
           }
           else {
               Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().put(groupID,daylist);
           }
       }
   }

    /**
     * 结束把积分 城市信息发回公共服
     */
   private void sendOverCityInfoToPublic(){
       EightDiagramsMessage.EightCityAttribute.Builder cityAttribute;
       for (Integer groupID :  Manager.eightDiagramsManager.getGroupCityInfos().keySet()) {
           ConcurrentHashMap<Integer ,EightDiagramCity> cityInfo =  Manager.eightDiagramsManager.getGroupCityInfos().get(groupID);
           for (EightDiagramCity city:cityInfo.values()) {
               city.getCityBattleProgressList().clear();
           }
           //发送城市战斗信息
           EightDiagramsMessage.F2PSendOverCityInfo.Builder  msg = EightDiagramsMessage.F2PSendOverCityInfo.newBuilder();
           List< EightDiagramsMessage.EightCityAttribute> attributeslist = new ArrayList<>();
           String cityString =   VersionUpdateUtil.dataSave(JsonUtils.toJSONString(cityInfo));
           cityAttribute  =  EightDiagramsMessage.EightCityAttribute.newBuilder();
           cityAttribute.setType(1);
           cityAttribute.setValue(groupID);
           cityAttribute.setParam(cityString);
           attributeslist.add(cityAttribute.build());

           //发送周期积分
           ConcurrentHashMap<Long, EightIntegralRankBean> periodOflist = Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().get(groupID);
           if (periodOflist == null)
               continue;
           String rankStringList =  VersionUpdateUtil.dataSave(JsonUtils.toJSONString(periodOflist));
           cityAttribute  =  EightDiagramsMessage.EightCityAttribute.newBuilder();
           cityAttribute.setType(2);
           cityAttribute.setValue(groupID);
           cityAttribute.setParam(rankStringList);
           attributeslist.add(cityAttribute.build());


           //发送当天积分
           ConcurrentHashMap<Long, EightIntegralRankBean> dayOflist = Manager.eightDiagramsManager.getDayOf_playerBattleInfo().get(groupID);
           if (dayOflist == null)
               continue;
           String dayRankStringList =  VersionUpdateUtil.dataSave(JsonUtils.toJSONString(dayOflist));
           cityAttribute  =  EightDiagramsMessage.EightCityAttribute.newBuilder();
           cityAttribute.setType(3);
           cityAttribute.setValue(groupID);
           cityAttribute.setParam(dayRankStringList);
           attributeslist.add(cityAttribute.build());

           msg.addAllEightCityOVerInfo(attributeslist);
           MessageUtils.send_to_public( EightDiagramsMessage.F2PSendOverCityInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
       }
   }

   private void clearAll(){
       Manager.eightDiagramsManager.getPeriodOf_playerBattleInfo().clear();
       Manager.eightDiagramsManager.getDayOf_playerBattleInfo().clear();
       Manager.eightDiagramsManager.getGroupCityInfos().clear();
       Manager.eightDiagramsManager.getParticipationList().clear();
       Manager.eightDiagramsManager.getPeriodOf_Ranklist50Num().clear();
       Manager.eightDiagramsManager.getDayOf_Ranklist100Num().clear();
   }


    private void startRanking()
    {
        for (Integer groupID : Manager.eightDiagramsManager.getDayOf_playerBattleInfo().keySet()) {
            ConcurrentHashMap<Long, EightIntegralRankBean> groupDayRanklist
                    = Manager.eightDiagramsManager.getDayOf_playerBattleInfo().get(groupID);
            List<EightIntegralRankBean> ranklsit = new ArrayList<>(groupDayRanklist.values());
            ranklsit.sort(new EightDiagramsintegralSort());
            int sublen = ranklsit.size() >=100?100:ranklsit.size();
            List<EightIntegralRankBean>  num100ranklsit = ranklsit.subList(0,sublen);
            Manager.eightDiagramsManager.getDayOf_Ranklist100Num().put(groupID,num100ranklsit);
        }
    }

    public void eightDiagramsTimerLoop()
    {
        if(!Manager.eightDiagramsManager.eightDiagramsIsOpen)
            return;
        GameServer.getInstance().getMainThread().addTimerEvent(new EightDiagramsTimer(1, 5000));
        startRanking();
    }

    /**
     * 活动结束结算
     */
    private void activityOverSettle() {
        for (Integer groupID :  Manager.eightDiagramsManager.getGroupCityInfos().keySet()) {
            ConcurrentHashMap<Integer ,EightDiagramCity> citylist =
                    Manager.eightDiagramsManager.getGroupCityInfos().get(groupID);
            for (EightDiagramCity city :citylist.values()) {
                if(  Manager.eightDiagramsManager.getParticipationList().containsKey(city.getCurSid())) {
                    ConcurrentHashMap<Long, String> roleList =
                            Manager.eightDiagramsManager.getParticipationList().get(city.getCurSid());
                    ConcurrentHashMap<String,ConcurrentHashMap<Long,Integer>> sendToPlayerlist = new ConcurrentHashMap<>();
                    for (long roleid :roleList.keySet()) {
                        String platname =  roleList.get(roleid);
                        if (!sendToPlayerlist.containsKey(platname)) {
                            ConcurrentHashMap<Long,Integer> rolelist = new ConcurrentHashMap<>();
                            rolelist.put(roleid,city.getCityId());
                            sendToPlayerlist.put(platname,rolelist);
                        }
                        else {
                            ConcurrentHashMap<Long,Integer> rolelist =  sendToPlayerlist.get(platname);
                            rolelist.put(roleid,city.getCityId());
                        }
                    }
                    for (String patSid:sendToPlayerlist.keySet()) {
                        EightDiagramsMessage.P2GSendEightCityRward.Builder msg
                                = EightDiagramsMessage.P2GSendEightCityRward.newBuilder();
                        EightDiagramsMessage.RewardInfo.Builder rewardInfo;
                        List< EightDiagramsMessage.RewardInfo> rewardInfoList = new ArrayList<>();
                        ChannelHandlerContext session  =  FightClientManager.GetInstance().getSession(patSid);
                        ConcurrentHashMap<Long,Integer>  roleInfoList = sendToPlayerlist.get(patSid);
                        if (session!=null) {
                            msg.setRewardType(EightDiagramsManager.Occupy_City_Reward);
                            for (Long  roleID :roleList.keySet()) {
                                rewardInfo = EightDiagramsMessage.RewardInfo.newBuilder();
                                int value =  roleInfoList.get(roleID);
                                rewardInfo.setRoleID(roleID);
                                rewardInfo.setValue(value);
                                rewardInfoList.add(rewardInfo.build());
                            }
                            msg.addAllRewardInfo(rewardInfoList);
                            FightClientManager.GetInstance().send_to_game(session, EightDiagramsMessage.P2GSendEightCityRward.MsgID.eMsgID_VALUE,msg.build().toByteArray());
                        }
                    }
                }
            }
        }
    }

    /**
     * 每天活动结束的积分奖励
     */
    private void integralSettle()
    {
        ConcurrentHashMap<String,ConcurrentHashMap<Long,Integer>> sendToPlayerlist = new ConcurrentHashMap<>();
        for (  List<EightIntegralRankBean> ranklist :
                Manager.eightDiagramsManager.getDayOf_Ranklist100Num().values()) {
            int rank = 1;
            for (EightIntegralRankBean integral:ranklist) {
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
        }
        for (String patSid:sendToPlayerlist.keySet()) {
            EightDiagramsMessage.P2GSendEightCityRward.Builder msg
                    = EightDiagramsMessage.P2GSendEightCityRward.newBuilder();
            EightDiagramsMessage.RewardInfo.Builder rewardInfo;
            List< EightDiagramsMessage.RewardInfo> rewardInfoList = new ArrayList<>();
            ChannelHandlerContext session  =  FightClientManager.GetInstance().getSession(patSid);
            ConcurrentHashMap<Long,Integer>  roleList = sendToPlayerlist.get(patSid);
            if (session!=null) {
                msg.setRewardType(EightDiagramsManager.IntegralSettle);
                for (Long  roleID :roleList.keySet()) {
                    rewardInfo = EightDiagramsMessage.RewardInfo.newBuilder();
                    int value =  roleList.get(roleID);
                    rewardInfo.setValue(value);
                    rewardInfo.setRoleID(roleID);
                    rewardInfoList.add(rewardInfo.build());
                }
                msg.addAllRewardInfo(rewardInfoList);
                FightClientManager.GetInstance().send_to_game(session, EightDiagramsMessage.P2GSendEightCityRward.MsgID.eMsgID_VALUE,msg.build().toByteArray());
            }
        }
    }
}
