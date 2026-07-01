/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.servermatch;

import com.data.CfgManager;
import com.data.Global;
import com.data.bean.Cfg_Daily_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.eightdiagrams.manager.EightDiagramsManager;
import com.game.fightroom.manager.FightManager;
import com.game.fightroom.structs.FightRoom;
import com.game.gameserver.manager.GameServerManager;
import com.game.gameserver.structs.ServerInfo;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.servermatch.script.IServerMatchManagerScript;
import com.game.servermatch.structs.GameServerInfo;
import com.game.servermatch.structs.ServerIDSort;
import com.game.soulanimalforest.manager.SoulAnimalForestManager;
import com.game.utils.ServerParamUtil;
import game.core.json.TypeReference;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器匹配
 */
public class ServerMatchManagerScript implements IServerMatchManagerScript {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(ServerParamUtil.class);

    @Override
    public int getId() {
        return ScriptEnum.ServerMatchManagerScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 服务器阶段划分
     */
    private void serverGroupMark(GameServerInfo newinfo, int stage, int bigGroupID, int grouid) {
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, List<GameServerInfo>>> stageGroup;
        if (ServerMatchManager.serverStageGrouplist.containsKey(bigGroupID))
            stageGroup = ServerMatchManager.serverStageGrouplist.get(bigGroupID);
        else
            stageGroup = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer, List<GameServerInfo>> group;
        if (stageGroup.containsKey(stage))
            group = stageGroup.get(stage);
        else
            group = new ConcurrentHashMap<>();

        newinfo.stageWithGroupIndex.put(stage, grouid);
        // int serverIndex =  count/stage;
        List<GameServerInfo> v;
        if (group.containsKey(grouid)) {
            v = group.get(grouid);
        } else {
            v = new ArrayList<>();
        }
        v.add(newinfo);
        group.put(grouid, v);
        stageGroup.put(stage, group);
        ServerMatchManager.serverStageGrouplist.put(bigGroupID, stageGroup);


        List<String> serverkeylsit = new ArrayList<>();
        if (ServerMatchManager.groupIDWithServerKey.containsKey(grouid)) {
            serverkeylsit = ServerMatchManager.groupIDWithServerKey.get(grouid);
        } else {
            ServerMatchManager.groupIDWithServerKey.put(grouid, serverkeylsit);
        }
        String serverkey = newinfo.getPlatName() + "_" + newinfo.getServerId();
        serverkeylsit.add(serverkey);
    }


    /**
     * 检测运营从GM后台匹配的组ID
     * @param defaultGroupId
     * @param gmSetGroupIds
     * @return
     */
    private int checkOperatingGroupId(int defaultGroupId, List<Integer> gmSetGroupIds ){

        if (gmSetGroupIds.contains(defaultGroupId)) {
            logger.info(" 该 defaultGroupId 已被GM后台设置  {} ",defaultGroupId);
            return  checkOperatingGroupId(defaultGroupId+1,gmSetGroupIds);
        }
        return defaultGroupId;
    }

    /**
     * 清除GM后台匹配好了的服务器
     * @param newAllServerlist
     * @param gmSetGroupIds
     */
    private void clearOperatingGroupServers(List<GameServerInfo> newAllServerlist,List<Integer> gmSetGroupIds ){
        for (Map.Entry<Integer,HashMap<Integer,List<String>>> entry : ServerMatchManager.gm_OperatingGroup.entrySet()){
            HashMap<Integer,List<String>> eightgroups =  entry.getValue();
            int bigGroupId =  entry.getKey();
            gmSetGroupIds.add(bigGroupId);
            for (Map.Entry< Integer,List<String>> entry1: eightgroups.entrySet()){
                List<String> serverKeys =   entry1.getValue();
                int groupId = entry1.getKey();
                gmSetGroupIds.add(groupId);
                for (String serverkey : serverKeys){
                    for (Iterator<GameServerInfo> it = newAllServerlist.iterator(); it.hasNext();) {
                        GameServerInfo g = it.next();
                        String serverkey1  = g.getPlatName() +"_" +g.getServerId();
                        if (serverkey.equals(serverkey1)){
                            it.remove();
                            logger.info(" 该 服务器 已被GM后台 匹配   serverkey  {} ",serverkey1);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 在把运营从GM后台匹配的的服务器添加到总组里面
     */
    private void addOperatingGroupToAllGroup(){
        if (ServerMatchManager.gm_OperatingGroup == null || ServerMatchManager.gm_OperatingGroup.size()<=0){
            return;
        }
        for (Map.Entry<Integer,HashMap<Integer,List<String>>> entry : ServerMatchManager.gm_OperatingGroup.entrySet()){
            HashMap<Integer,List<String>> eightgroups =  entry.getValue();
            int bigGroupId = entry.getKey();
            ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, List<GameServerInfo>>> stageGroup = new ConcurrentHashMap<>();
            ConcurrentHashMap<Integer, List<GameServerInfo>> group = new ConcurrentHashMap<>();
            stageGroup.put(ServerMatchManager.serverMatchStage_8,group);
            ServerMatchManager.serverStageGrouplist.put(bigGroupId,stageGroup);
            logger.info("bigGroupId  {}",bigGroupId);
            for (Map.Entry< Integer,List<String>> entry1: eightgroups.entrySet()){
                List<String> serverKeys =   entry1.getValue();
                int groupId = entry1.getKey();
                List<GameServerInfo> v = new ArrayList<>();
                group.put(groupId,v);
                for (String serverkey : serverKeys){
                    if (!ServerMatchManager.infos.containsKey(serverkey)){
                        logger.error("serverkey  {} 不存在 ： " ,serverkey);
                        continue;
                    }
                    GameServerInfo info1 =   ServerMatchManager.infos.get(serverkey);
                    info1.stageWithGroupIndex.put(ServerMatchManager.serverMatchStage_8,groupId);
                    info1.setbigGroupID(bigGroupId);
                    info1.setGMbackgroundSet(true);
                    v.add(info1);
                    List<String> serverkeylsit;
                    if (ServerMatchManager.groupIDWithServerKey.containsKey(groupId)) {
                        serverkeylsit = ServerMatchManager.groupIDWithServerKey.get(groupId);
                    } else {
                        serverkeylsit = new ArrayList<>();
                        ServerMatchManager.groupIDWithServerKey.put(groupId, serverkeylsit);
                    }
                    serverkeylsit.add(serverkey);
                    logger.info(" newBigGroupID {} stageID {}   newGroupID {} serverinfo {} " ,bigGroupId,
                            ServerMatchManager.serverMatchStage_8, groupId,serverkey);
                }
            }
        }
    }
    //划分服务器  每晚凌晨 执行一次
    @Override
    public void markOff(ServerInfo info) {

        if (ServerMatchManager.infos == null || ServerMatchManager.infos.size() <= 0)
            return;
        ServerMatchManager.serverStageGrouplist.clear();
        ServerMatchManager.groupIDWithServerKey.clear();


        addOperatingGroupToAllGroup();//在把运营从GM后台匹配的添加到总组里面
        List<GameServerInfo> newAllServerlist = new ArrayList<>(ServerMatchManager.infos.values());
        newAllServerlist.sort(new ServerIDSort());
        //TODO 先分配最大 8组
        markServer(newAllServerlist, ServerMatchManager.serverMatchStage_8, 30000,0);//30000-40000分配是8服组

        //TODO 在从8组里面去分配  1-2-4阶段
        int stage_1_GroupStartId = 1;
        int stage_2_GroupStartId = 10000;
        int stage_4_GroupStartId = 20000;
        for (Map.Entry<Integer,ConcurrentHashMap<Integer,ConcurrentHashMap<Integer,List<GameServerInfo>>>> entry:
                ServerMatchManager.serverStageGrouplist.entrySet()){
            int bigGourpID =  entry.getKey();
            ConcurrentHashMap<Integer,ConcurrentHashMap<Integer,List<GameServerInfo>>> stageGroupMap =  entry.getValue();
            ConcurrentHashMap<Integer,List<GameServerInfo>> groupServerMap =  stageGroupMap.get(ServerMatchManager.serverMatchStage_8);
            for (Map.Entry<Integer,List<GameServerInfo>> entry1 :  groupServerMap.entrySet()){
                List<GameServerInfo> serverInfoList = entry1.getValue();
                serverInfoList.sort(new ServerIDSort());
                stage_1_GroupStartId = markServer(serverInfoList, ServerMatchManager.serverMatchStage_1,stage_1_GroupStartId,bigGourpID);
                stage_2_GroupStartId = markServer(serverInfoList, ServerMatchManager.serverMatchStage_2,stage_2_GroupStartId,bigGourpID);
                stage_4_GroupStartId = markServer(serverInfoList, ServerMatchManager.serverMatchStage_4,stage_4_GroupStartId,bigGourpID);
            }
        }
        //八级阵图划分逻辑
        EightDiagramsManager.getInstance().deal().eightDiagramsServerMarkOff();
        //魂兽岛达成分组组ID
        SoulAnimalForestManager.allGroupList = getAllReachMatchGroupIDList(DailyActiveDefine.ACTIVITY_SOULANIMALISLAND);

        Manager.crossHorseBossManager.setAllGroupList(getAllReachMatchGroupIDList(DailyActiveDefine.CrossHorseBoss));

        if (info != null){
            onRepairActivityData(info);
        }
    }


    private int markServer(List<GameServerInfo> allServerList, int stage, int groupID,int normalBigGroupID) {
        int bigCount = 0;
        int count = 0;
        int bigGroupID;
        for (GameServerInfo g : allServerList) {
            bigGroupID =normalBigGroupID >0?normalBigGroupID:(bigCount / ServerMatchManager.serverMatchStage_8) + 1;
            if (count % stage == 0) {
                groupID++;
            }
            count++;
            bigCount++;
            if (g.getIsMerge()){
                continue;
            }
            //进行过后台操作过的服务器不在进行八组分组
            if(stage == ServerMatchManager.serverMatchStage_8){
                if (ServerMatchManager.gm_OperatingGroup.containsKey(bigGroupID)){
                    if (ServerMatchManager.gm_OperatingGroup.get(bigGroupID).containsKey(groupID)){
                        continue;
                    }
                }
            }
            g.setbigGroupID(bigGroupID);
            serverGroupMark(g, stage, bigGroupID, groupID);
            int openDay =GameServerManager.getOpenServerDay(g.getOpenTime());
            logger.info("markServer bigGroupId-- {}  state-- {} groupID-- {}  serverId{}-- openDay-- {}  firstConnect-- {} newinfo-- {} "
                    ,bigGroupID,stage,groupID,g.getServerId(),openDay,g.getFirstConnectTime(),g.stageWithGroupIndex);
        }
        return  groupID;
    }


    /**
     * 获取所有达成次活动要求匹配的组ID
     */
    public List<Integer> getAllReachMatchGroupIDList(int dailyID) {
        Cfg_Daily_Bean daily = CfgManager.getCfg_Daily_Container().getValueByKey(dailyID);
        if (daily == null) {
            logger.error("日常表不存在   " + dailyID);
            return null;
        }
        ReadIntegerArrayEs readIntegerArrayEs = null;
        if (daily.getCrossMatch().size() < 0)
            readIntegerArrayEs = Global.Cross_Match_OpneTime;
        else
            readIntegerArrayEs = daily.getCrossMatch();
        List<Integer> allGroup = new ArrayList<>();
        for (ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, List<GameServerInfo>>> stageGroupList :
                ServerMatchManager.serverStageGrouplist.values()) {
            for (int i = readIntegerArrayEs.size() - 1; i >= 0; i--) {
                int matchServerNum = readIntegerArrayEs.get(i).get(0);
                int needOpenDay =  readIntegerArrayEs.get(i).get(1);//getNeedOpenDay(matchServerNum);
                if (stageGroupList.containsKey(matchServerNum)) {
                    ConcurrentHashMap<Integer, List<GameServerInfo>> stageGroup = stageGroupList.get(matchServerNum);
                    for (int key : stageGroup.keySet()) {
                        List<GameServerInfo> serverInfoList = null;
                        List<GameServerInfo> groupServerList = stageGroup.get(key);
                        if (daily.getCrosstype() == ServerMatchManager.crossMatchType_0) {
                            serverInfoList = getCrossTypeZero(groupServerList,needOpenDay);
                        } else if (daily.getCrosstype() == ServerMatchManager.crossMatchType_1){
                            serverInfoList = getCrossTypeOne(groupServerList,needOpenDay);
                        }
                        if (serverInfoList != null) {
                            allGroup.add(key);
                        }
                    }
                }
            }
        }
        return allGroup;
    }

    @Override
    public ConcurrentHashMap<Integer, List<GameServerInfo>> getAllReachGroupServerList(int dailyID) {
        ConcurrentHashMap<Integer, List<GameServerInfo>> groupServerlist = new ConcurrentHashMap<>();
        Cfg_Daily_Bean daily = CfgManager.getCfg_Daily_Container().getValueByKey(dailyID);
        if (daily == null) {
            logger.error("日常表不存在   " + dailyID);
            return null;
        }
        ReadIntegerArrayEs readIntegerArrayEs = null;
        if (daily.getCrossMatch().size() <= 0)
            readIntegerArrayEs = Global.Cross_Match_OpneTime;
        else
            readIntegerArrayEs = daily.getCrossMatch();
        for (ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, List<GameServerInfo>>> stageGroupList :
                ServerMatchManager.serverStageGrouplist.values()) {
            for (int i = readIntegerArrayEs.size() - 1; i >= 0; i--) {
                int matchServerNum = readIntegerArrayEs.get(i).get(0);
                int needOpenDay = readIntegerArrayEs.get(i).get(1);
                if (stageGroupList.containsKey(matchServerNum)) {
                    ConcurrentHashMap<Integer, List<GameServerInfo>> stageGroup = stageGroupList.get(matchServerNum);
                    for (int groupId : stageGroup.keySet()) {
                        List<GameServerInfo> serverInfoList = null;
                        List<GameServerInfo> groupServerList = stageGroup.get(groupId);
                        if (daily.getCrosstype() == ServerMatchManager.crossMatchType_0) {
                            serverInfoList = getCrossTypeZero(groupServerList,needOpenDay);
                        } else if (daily.getCrosstype() == ServerMatchManager.crossMatchType_1) {
                            serverInfoList = getCrossTypeOne(groupServerList,needOpenDay);
                        }
                        if (serverInfoList != null) {
                            groupServerlist.put(groupId, serverInfoList);
                        }
                    }
                }
            }
        }
        return groupServerlist;
    }

    /**
     * 获取所有阶段达成了的服务器组信息Key_1=groupID,,HashMap<Integer,List<GameServerInfo>>-> 阶段ID对应 当前阶段服务器列表
     */
    public HashMap<Integer,HashMap<Integer,List<GameServerInfo>>> getAllReachStageServerInfo(int dailyID){
        HashMap<Integer,HashMap<Integer,List<GameServerInfo>>> allStageServerInfo = new HashMap<>();
        Cfg_Daily_Bean daily = CfgManager.getCfg_Daily_Container().getValueByKey(dailyID);
        if (daily == null) {
            logger.error("日常表不存在   " + dailyID);
            return null;
        }
        ReadIntegerArrayEs readIntegerArrayEs = null;
        if (daily.getCrossMatch().size() < 0)
            readIntegerArrayEs = Global.Cross_Match_OpneTime;
        else
            readIntegerArrayEs = daily.getCrossMatch();
        for (ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, List<GameServerInfo>>> stageGroupList :
                ServerMatchManager.serverStageGrouplist.values()) {
            for (int i = readIntegerArrayEs.size() - 1; i >= 0; i--) {
                int matchServerNum = readIntegerArrayEs.get(i).get(0);
                int needOpenDay = readIntegerArrayEs.get(i).get(1);
                if (stageGroupList.containsKey(matchServerNum)) {
                    ConcurrentHashMap<Integer, List<GameServerInfo>> stageGroup = stageGroupList.get(matchServerNum);
                    for (int groupId : stageGroup.keySet()) {
                        List<GameServerInfo> serverInfoList = null;
                        List<GameServerInfo> groupServerList = stageGroup.get(groupId);
                        if (daily.getCrosstype() == ServerMatchManager.crossMatchType_0) {
                            serverInfoList = getCrossTypeZero(groupServerList,needOpenDay);
                        } else if (daily.getCrosstype() == ServerMatchManager.crossMatchType_1) {
                            serverInfoList = getCrossTypeOne(groupServerList,needOpenDay);
                        }
                        if (serverInfoList != null) {
                            HashMap<Integer,List<GameServerInfo>>  stageServerInfo = new HashMap<>();
                            stageServerInfo.put(matchServerNum,serverInfoList);
                            allStageServerInfo.put(groupId, stageServerInfo);
                        }
                    }
                }
            }
        }
        return allStageServerInfo;
    }
    /**
     * 获取跨服聊天匹配到的服务器
     *
     * @return
     */
    public List<GameServerInfo> getCrossChatServerlist(String platServerID) {
        GameServerInfo sinfo = ServerMatchManager.infos.get(platServerID);
        if (sinfo == null) {
            logger.error("platServerID  不存在  " + platServerID);
            return null;
        }
        ReadIntegerArrayEs readIntegerArrayEs = Global.Cross_Match_OpneTime;
        return getServerlistForCrossType(readIntegerArrayEs, sinfo, ServerMatchManager.crossMatchType_1);
    }

    /**
     * 获取跨服排行服务器列表
     * @return
     */

    public List<Integer> getCrossRankServerlist(String platServerID){
        GameServerInfo sinfo = ServerMatchManager.infos.get(platServerID);
        if (sinfo == null) {
            logger.error("platServerID  不存在  " + platServerID);
            return null;
        }
        ReadIntegerArrayEs readIntegerArrayEs = null;
        ReadArray<Integer>[] readArrays = new ReadArray[Global.Cross_Match_OpneTime.size()];
        int count =0;
        for (ReadArray<Integer> readArray:   Global.Cross_Match_OpneTime.getValuees()){
            readArrays[count] = readArray;
            count++;
        }
        readIntegerArrayEs = new ReadIntegerArrayEs(readArrays);

        List<GameServerInfo>  lists = getServerlistForCrossType(readIntegerArrayEs, sinfo, ServerMatchManager.crossMatchType_1);
        if (lists == null || lists.size()<=0)
            return null;

        List<Integer> allServerIds = new ArrayList<>();
        for (GameServerInfo g : lists){
            allServerIds.add(g.getServerId());
        }
        return allServerIds;
    }

    /**
     * 获取当前开服天数所对应的所在服务器组列表
     */
    public List<GameServerInfo> getSeverlistForCurOpenDay(String platServerID, int dailyID) {
        GameServerInfo sinfo = ServerMatchManager.infos.get(platServerID);
        if (sinfo == null) {
            logger.error("platServerID  不存在  " + platServerID);
            return null;
        }
        Cfg_Daily_Bean daily = CfgManager.getCfg_Daily_Container().getValueByKey(dailyID);
        if (daily == null) {
            logger.error("日常表不存在   " + dailyID);
            return null;
        }
        if (!ServerMatchManager.serverStageGrouplist.containsKey(sinfo.getbigGroupID())) {
            logger.error("serverStageGrouplist  不存在   " + sinfo.getbigGroupID());
            return null;
        }

        ReadIntegerArrayEs readIntegerArrayEs;
        if (daily.getCrossMatch().size() < 0)
            readIntegerArrayEs = Global.Cross_Match_OpneTime;
        else
            readIntegerArrayEs = daily.getCrossMatch();
        return getServerlistForCrossType(readIntegerArrayEs, sinfo, daily.getCrosstype());
    }

    /**
     * 获取当前开服天数所对应的groupID
     */
    public int getGroupIDForCurOpenDay(String platServerID, int dailyID) {

        GameServerInfo sinfo = ServerMatchManager.infos.get(platServerID);
        if (sinfo == null) {
            logger.error("platServerID  不存在  " + platServerID);
            return -1;
        }
        Cfg_Daily_Bean daily = CfgManager.getCfg_Daily_Container().getValueByKey(dailyID);
        if (daily == null) {
            logger.error("日常表不存在   " + dailyID);
            return -1;
        }
        if (!ServerMatchManager.serverStageGrouplist.containsKey(sinfo.getbigGroupID())) {
            logger.error("serverStageGrouplist  不存在   " + sinfo.getbigGroupID());
            return -1;
        }
        ReadIntegerArrayEs readIntegerArrayEs = null;
        if (daily.getCrossMatch().size() < 0)
            readIntegerArrayEs = Global.Cross_Match_OpneTime;
        else
            readIntegerArrayEs = daily.getCrossMatch();

        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, List<GameServerInfo>>> stageGroupList =
                ServerMatchManager.serverStageGrouplist.get(sinfo.getbigGroupID());
        List<GameServerInfo> serverInfoList = null;
        for (int i = readIntegerArrayEs.size() - 1; i >= 0; i--) {
            int matchServerNum = readIntegerArrayEs.get(i).get(0);
            int needOpenDay = readIntegerArrayEs.get(i).get(1);
            if (stageGroupList.containsKey(matchServerNum)) {
                ConcurrentHashMap<Integer, List<GameServerInfo>> stageGroup = stageGroupList.get(matchServerNum);
                int groupIndex = sinfo.stageWithGroupIndex.get(matchServerNum);
                List<GameServerInfo> serverMatch = stageGroup.get(groupIndex);
                if (serverMatch != null) {
                    if (daily.getCrosstype() ==  ServerMatchManager.crossMatchType_0) {
                        serverInfoList = getCrossTypeZero(serverMatch,needOpenDay);
                    } else if (daily.getCrosstype() ==  ServerMatchManager.crossMatchType_1) {
                        int openDay = GameServerManager.getOpenServerDay(sinfo.getOpenTime());
                        if (openDay >= needOpenDay)
                            serverInfoList = serverMatch;
                    }
                    if (serverInfoList != null) {
                        return groupIndex;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 获取当前世界等级所对应的房间
     */
    @Override
    public FightRoom getFightRoomForCurOpenDay(String platServerID, int dailyID) {
        GameServerInfo sinfo = ServerMatchManager.infos.get(platServerID);
        if (sinfo == null) {
            logger.error("platServerID  不存在  " + platServerID);
            return null;
        }
        Cfg_Daily_Bean dailyBean = CfgManager.getCfg_Daily_Container().getValueByKey(dailyID);
        if (dailyBean == null) {
            logger.error("日常表不存在   " + dailyID);
            return null;
        }
        if (!ServerMatchManager.serverStageGrouplist.containsKey(sinfo.getbigGroupID())) {
            logger.error("serverStageGrouplist  不存在   " + sinfo.getbigGroupID());
            return null;
        }
        int cloneId = getCurCloneId(dailyBean);
        ReadIntegerArrayEs readIntegerArrayEs = null;
        if (dailyBean.getCrossMatch().size() < 0)
            readIntegerArrayEs = Global.Cross_Match_OpneTime;
        else
            readIntegerArrayEs = dailyBean.getCrossMatch();

        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, List<GameServerInfo>>> stageGroupList =
                ServerMatchManager.serverStageGrouplist.get(sinfo.getbigGroupID());
        List<GameServerInfo> serverInfoList = null;
        for (int i = readIntegerArrayEs.size() - 1; i >= 0; i--) {
            int matchServerNum = readIntegerArrayEs.get(i).get(0);
            int needOpenDay = getNeedOpenDay(matchServerNum);
            if (stageGroupList.containsKey(matchServerNum)) {
                ConcurrentHashMap<Integer, List<GameServerInfo>> stageGroup = stageGroupList.get(matchServerNum);
                int groupIndex = sinfo.stageWithGroupIndex.get(matchServerNum);
                List<GameServerInfo> serverMatch = stageGroup.get(groupIndex);
                if (serverMatch != null) {
                    if (dailyBean.getCrosstype() == ServerMatchManager.crossMatchType_0) {
                        serverInfoList = getCrossTypeZero(serverMatch,needOpenDay);
                    } else if (dailyBean.getCrosstype() == ServerMatchManager.crossMatchType_1) {
                        int openDay =  GameServerManager.getOpenServerDay(sinfo.getOpenTime());
                        if (  openDay >= needOpenDay){
                            serverInfoList = serverMatch;
                        }
                    }
                    if (serverInfoList != null) {
                        FightRoom fr = FightManager.getInstance().getFightRoom(groupIndex, cloneId);
                        if (fr != null) {
                            return fr;
                        }
                    }
                }
            }
        }
        return null;
    }

    private List<GameServerInfo> getServerlistForCrossType(ReadIntegerArrayEs readIntegerArrayEs, GameServerInfo sinfo, int type) {
        List<GameServerInfo> serverInfoList = null;
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, List<GameServerInfo>>> stageGroupList =
                ServerMatchManager.serverStageGrouplist.get(sinfo.getbigGroupID());
        for (int i = readIntegerArrayEs.size() - 1; i >= 0; i--) {
            int matchServerNum = readIntegerArrayEs.get(i).get(0);
            int needOpenDay = readIntegerArrayEs.get(i).get(1);
            if (stageGroupList.containsKey(matchServerNum)) {
                ConcurrentHashMap<Integer, List<GameServerInfo>> stageGroup = stageGroupList.get(matchServerNum);
                int groupIndex = sinfo.stageWithGroupIndex.get(matchServerNum);
                List<GameServerInfo> serverMatch = stageGroup.get(groupIndex);
                if (serverMatch != null) {
                    if (type ==  ServerMatchManager.crossMatchType_0) {
                        serverInfoList = getCrossTypeZero(serverMatch,needOpenDay);
                    } else if (type == ServerMatchManager.crossMatchType_1) {
                        int openDay =  GameServerManager.getOpenServerDay(sinfo.getOpenTime());
                        if (openDay >= needOpenDay){
                            serverInfoList = serverMatch;
                        }
                    }
                    if (serverInfoList != null) {
                        return serverInfoList;
                    }
                }
            }
        }
        return serverInfoList;
    }

    /**
     * 跨服模式：0：所有的服务器都需要满足对应的等级，否则活动都不能进入
     * @param serverMatch
     * @param needOpenDay
     * @return
     */
    private List<GameServerInfo> getCrossTypeZero(List<GameServerInfo> serverMatch,int needOpenDay) {
        if (serverMatch != null) {
            for (GameServerInfo gameServerInfo : serverMatch) {
                if (gameServerInfo.getIsMerge())
                    continue;
                int openDay =  GameServerManager.getOpenServerDay(gameServerInfo.getOpenTime());
                if (openDay < needOpenDay)
                    return null;
            }
            return serverMatch;
        }
        return null;
    }

    /**
     * 跨服模式：1：达到要求的开发天数服务器可以进入
     * @param serverMatch
     * @param needOpenDay
     * @return
     */
    private List<GameServerInfo> getCrossTypeOne(List<GameServerInfo> serverMatch,int needOpenDay) {
        boolean isActive = false;
        if (serverMatch != null) {
            for (GameServerInfo gameServerInfo : serverMatch) {
                if (gameServerInfo.getIsMerge())
                    continue;
                int openDay =GameServerManager.getOpenServerDay(gameServerInfo.getOpenTime());
                if (openDay >= needOpenDay) {
                    isActive = true;
                    break;
                }
            }
        }
        return  isActive ? serverMatch: null;
    }
    /**
     * 跨服模式：2：达到平均的开服天数后，对应的跨服的所有服务器的玩家均可进入。
     * @param serverMatch
     * @param needOpenDay
     * @return
     */

    //TODO 暂时废弃

    private List<GameServerInfo> getCrossTypeTwo(List<GameServerInfo> serverMatch,int needOpenDay) {
        if (serverMatch != null) {
            int allOpenDay = 0;
            int count = 0;
            for (GameServerInfo gameServerInfo : serverMatch) {
                if (gameServerInfo.getIsMerge())
                    continue;
                int openDay = GameServerManager.getOpenServerDay(gameServerInfo.getOpenTime());
                allOpenDay += openDay;
                count++;
            }
            int averageOpenDay = (int) Math.ceil(allOpenDay /count);
            if (averageOpenDay >= needOpenDay) {
                return serverMatch;
            }
        }
        return null;
    }

    /**
     * 获取服务器对应阶段的 组ID
     */
    public int getStageServerGroupID(String serverKey, int stage) {
        if (ServerMatchManager.infos.containsKey(serverKey)) {
            GameServerInfo info = ServerMatchManager.infos.get(serverKey);
            return info.stageWithGroupIndex.get(stage);
        }
        return -1;
    }

    private boolean isCheck(Set<String> checkServers, List<GameServerInfo> list) {
        String sKey;
        boolean isCheck = false;
        for (GameServerInfo gsi : list) {
            sKey = gsi.getPlatName() + "_" + gsi.getServerId();
            if (checkServers.contains(sKey)) {
                isCheck = true;
                continue;
            } else {
                checkServers.add(sKey);
            }
        }
        return isCheck;
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


    //获取每个阶段 所需要的 开服天数
    private int getNeedOpenDay(int macthType){
        int needOpenDay =  0;
        for (ReadArray<Integer> array : Global.Cross_Match_OpneTime.getValuees()){
            if ( array.get(0) == macthType){
                return array.get(1);
            }
        }
        return needOpenDay;
    }
    /**
     * 修复活动数据
     */
    public void  onRepairActivityData(ServerInfo info){
        //八级阵图修复
        Manager.eightDiagramsManager.deal().onEightDiagramRepair(info);

        //修复跨服排行榜
        Manager.crossRankManager.deal().onRepairCrossRank(info);
    }
    /**
     * 接收GM后台发来的服务器分组匹配
     */

    @Override
    public void onReceiveOperatingServerGroup(String serverGroup) {
            //TODO 过了年来和飞哥讨论数据传送格式来 在写

        HashMap<Integer,HashMap<Integer,List<String>>> serverGroupMap = JsonUtils.parseObject(serverGroup, new TypeReference<HashMap<Integer,HashMap<Integer,List<String>>>>() {
        });
        ServerMatchManager.gm_OperatingGroup = serverGroupMap;
        ServerParamUtil.saveOperatingGroup();
    }
    /**
     * 发送当前匹配组给GM后台
     */
    @Override
    public String onSendServerGroupToBackground() {
        //TODO 检查所有达到 8组匹配的服务器 发送给GM后台
        HashMap<Integer,HashMap<Integer,List<String>>> reachOperatingServerInfo = new HashMap<>();
        for (Integer bigGroupId : ServerMatchManager.serverStageGrouplist.keySet()){
            ConcurrentHashMap<Integer,List<GameServerInfo>> stage_8_groups = ServerMatchManager.serverStageGrouplist.get(bigGroupId).get(ServerMatchManager.serverMatchStage_8);
            int needOpenDay =  Global.Cross_Match_OpneTime.get(3).get(1);
            for (Map.Entry<Integer,List<GameServerInfo>> entry : stage_8_groups.entrySet()){
                 int groupId = entry.getKey();
                 List<GameServerInfo> infos = entry.getValue();
                 boolean isMeet = true;
                 for (GameServerInfo g : infos){
                     int openDay = GameServerManager.getOpenServerDay(g.getOpenTime());
                     if (openDay < needOpenDay){
                         isMeet = false;
                         break;
                     }
                 }
                 if (isMeet){
                     List<String> servrKeys = new ArrayList<>();
                     for (GameServerInfo g : infos){
                         String serverKey = GameServerManager.getInstance().makeKey(g.getPlatName(),g.getServerId());
                         servrKeys.add(serverKey);
                     }
                     HashMap<Integer,List<String>> groupList = new HashMap<>();
                     groupList.put(groupId,servrKeys);
                     reachOperatingServerInfo.put(bigGroupId,groupList);
                 }
            }
        }
        String result = reachOperatingServerInfo.size()<=0 ? "" :  JsonUtils.toJSONString(reachOperatingServerInfo);
        return result;
    }
}
