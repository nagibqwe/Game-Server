package com.game.servermatch.script;
import com.game.fightroom.structs.FightRoom;
import com.game.gameserver.structs.ServerInfo;
import com.game.servermatch.structs.GameServerInfo;
import game.core.script.IScript;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 *  联赛活动相关 接口
 * @author zhaibiao
 */
public interface IServerMatchManagerScript extends IScript {

    void markOff(ServerInfo info);
    /**
     * 获取服务器对应阶段的 组ID
     */
    int getStageServerGroupID(String  serverKey ,int stage);
    /**
     *获取当前开服天数所对应的所在服务器组列表
     */
    List<GameServerInfo> getSeverlistForCurOpenDay(String platServerID,int dailyID);
    /**
     *获取当前开服天数所对应的groupID
     */
    int getGroupIDForCurOpenDay(String platServerID,int dailyID);
    /**
     *获取所有达成次活动要求匹配的组ID
     */
     List<Integer> getAllReachMatchGroupIDList(int dailyID);

    /**
     * 获取跨服聊天匹配到的服务器
     * @return
     */
     List<GameServerInfo> getCrossChatServerlist(String platServerID);


    /**
     * 获取对应活动的所有服务器 列表
     * @param dailyID
     * @return
     */
    ConcurrentHashMap<Integer,List<GameServerInfo>> getAllReachGroupServerList(int dailyID);

    /**
     *获取当前世界等级所对应的房间
     */
    FightRoom getFightRoomForCurOpenDay(String platServerID, int dailyID);

    /**
     * 获取跨服排行数据
     * @return
     */
    List<Integer> getCrossRankServerlist(String platServerID);

    /**
     * 修复活动数据
     */
    void  onRepairActivityData(ServerInfo info);


    /**
     * 接收GM后台发来的服务器分组匹配
     */
    void onReceiveOperatingServerGroup(String serverGroup);

    /**
     * 发送当前匹配组给GM后台
     */
    String onSendServerGroupToBackground();


    /**
     * 获取所有阶段达成了的服务器组信息Key_1=groupID,,HashMap<Integer,List<GameServerInfo>>-> 阶段ID对应 当前阶段服务器列表
     */
    HashMap<Integer,HashMap<Integer,List<GameServerInfo>>> getAllReachStageServerInfo(int dailyID);
}
