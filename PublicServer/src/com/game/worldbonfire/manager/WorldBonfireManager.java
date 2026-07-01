package com.game.worldbonfire.manager;

import com.data.CfgManager;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Daily_Bean;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.fightroom.structs.FightRoom;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.worldbonfire.script.IWorldBonfireScript;
import com.game.worldbonfire.structs.WorldBonfire;
import com.game.worldbonfire.structs.WorldBonfireMatch;
import com.game.worldbonfire.structs.WorldBonfireMm;
import com.game.worldbonfire.structs.WorldBonfireTeam;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @auther lw
 * @create 2019-10-17 15:32
 */
public class WorldBonfireManager {

    private static final Logger logger = LogManager.getLogger(WorldBonfireManager.class);

    //参加活动人数列表
    private ConcurrentHashMap<Integer, ConcurrentHashMap<Long, WorldBonfireMm>> joinPlayers = new ConcurrentHashMap<>();

    //匹配队列
    private ConcurrentHashMap<Integer,List<WorldBonfireMatch>> matchMembers = new ConcurrentHashMap<>();

    //划拳匹配队列
    private ConcurrentHashMap<Integer, ConcurrentHashMap<Long, WorldBonfireTeam>> matchTeam = new ConcurrentHashMap<>();

    //房间
    private List<FightRoom> rooms = new ArrayList<>();

    //篝火
    private  ConcurrentHashMap<Integer,WorldBonfire>   worldBonfire = new ConcurrentHashMap();



    private ConcurrentHashMap<Long,Long> roleIDWithTeamID = new ConcurrentHashMap<>();

    //玩家对应自己的组ID
    private ConcurrentHashMap<Long,Integer> roleIDWithGroupID = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, Integer> getRoleIDWithGroupID() {
        return roleIDWithGroupID;
    }

    public void setRoleIDWithGroupID(ConcurrentHashMap<Long, Integer> roleIDWithGroupID) {
        this.roleIDWithGroupID = roleIDWithGroupID;
    }

    //胜利
    public static final int WIN = 1;

    //失败
    public static final int FAIL = 2;

    //平局
    public static final int DRAW = 3;

    public ConcurrentHashMap<Integer, ConcurrentHashMap<Long, WorldBonfireMm>> getJoinPlayers() {
        return joinPlayers;
    }

    public void setJoinPlayers(ConcurrentHashMap<Integer, ConcurrentHashMap<Long, WorldBonfireMm>> joinPlayers) {
        this.joinPlayers = joinPlayers;
    }


    public ConcurrentHashMap<Integer,List<WorldBonfireMatch>> getMatchMembers() {
        return matchMembers;
    }

    public void setMatchMembers(ConcurrentHashMap<Integer,List<WorldBonfireMatch>> matchMembers) {
        this.matchMembers = matchMembers;
    }

    public ConcurrentHashMap<Integer, ConcurrentHashMap<Long, WorldBonfireTeam>> getMatchTeam() {
        return matchTeam;
    }

    public void setMatchTeam(ConcurrentHashMap<Integer, ConcurrentHashMap<Long, WorldBonfireTeam>> matchTeam) {
        this.matchTeam = matchTeam;
    }

    public List<FightRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<FightRoom> rooms) {
        this.rooms = rooms;
    }

    public ConcurrentHashMap<Integer,WorldBonfire> getWorldBonfire() {
        return worldBonfire;
    }

    public void setWorldBonfire(ConcurrentHashMap<Integer,WorldBonfire> worldBonfire) {
        this.worldBonfire = worldBonfire;
    }
    public ConcurrentHashMap<Long, Long> getRoleIDWithTeamID() {
        return roleIDWithTeamID;
    }

    public void setRoleIDWithTeamID(ConcurrentHashMap<Long, Long> roleIDWithTeamID) {
        this.roleIDWithTeamID = roleIDWithTeamID;
    }

    public static long getActiveTime() {
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.ACTIVITY_WORLD_BONFIRE);
        return (bean.getTime().get(0).get(1) - bean.getTime().get(0).get(0)) * 60 * 1000L;
    }

    public static long getActiveBeginTime(int round) {
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.ACTIVITY_WORLD_BONFIRE);
        return bean.getTime().get(round).get(0) * 60 * 1000L;
    }

    public static int getLineMaxPeople() {
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.ACTIVITY_WORLD_BONFIRE);
        Cfg_Clone_map_Bean clone_map_bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(bean.getCloneID().get(0));
        Cfg_Mapsetting_Bean mapsetting_bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(clone_map_bean.getMapid());
        return mapsetting_bean.getOnline();
    }

    public IWorldBonfireScript manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.WorldBonfireBaseScript);
        if (is instanceof IWorldBonfireScript) {
            return (IWorldBonfireScript) is;
        }
        logger.error("没有找到世界篝火管理脚本!");
        return null;
    }

    private enum Singleton {
        INSTANCE;
        WorldBonfireManager manager;

        Singleton() {
            manager = new WorldBonfireManager();
        }

        WorldBonfireManager getProcessor() {
            return manager;
        }
    }

    public static WorldBonfireManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
