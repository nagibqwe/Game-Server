/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.fightroom.script;

import com.data.bean.Cfg_Clone_map_Bean;
import com.game.fightroom.structs.FightRoom;
import com.game.gameserver.structs.ServerInfo;
import com.game.zone.structs.ZoneTeam;
import game.core.script.IScript;
import game.message.CommonMessage;
import game.message.CrossFightMessage;
import game.message.CrossServerMessage;
import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface IFightManagerScript extends IScript {

    void OnTick(long curtime);

    boolean fightStart(FightRoom room);

    /**
     * 根据人数计算最佳服务器
     * @param max_num
     * @return
     */
    ServerInfo getFightServerId(int max_num);

    void OnF2PCloneRewardNotGet(ChannelHandlerContext context, CrossServerMessage.F2PCloneRewardNotGet mess);

    void OnF2PFightRoomState(ChannelHandlerContext context, CrossFightMessage.F2PFightRoomState mess);

    void OnG2FSynPlayerOut(ChannelHandlerContext context, CrossServerMessage.G2FSynPlayerOut mess);

    void OnG2PReqOutFightRoom(ChannelHandlerContext context, CrossFightMessage.G2PReqOutFightRoom mess);

    FightRoom createFightRoom(Cfg_Clone_map_Bean bean, List<ZoneTeam> zt);

    void fightStart(FightRoom room, Collection<ZoneTeam> zt);

    void PlayerOutFightRoomFromFight(ChannelHandlerContext context, CrossServerMessage.F2PPlayerOutFightRoom mess);

    /**
     * 创建魂兽森林的副本ID值
     *
     * @param context
     * @param zt
     * @param modelId
     * @param bean
     * @return
     */
    long EnterSoulAnimalForestClone(ChannelHandlerContext context, ZoneTeam zt, int modelId, Cfg_Clone_map_Bean bean);

    /**
     * 获得副本的额外参数设置
     *
     * @param fr 房间实例
     * @return 返回额外参数
     */
    List<CommonMessage.CrossAttribute> getFightRoomParam(FightRoom fr);


    /**
     * 获取最小人数服务器
     * @return
     */
    ServerInfo getMinServer();

    /**
     * 判断服务器人数是否已满
     * @param serverInfo
     * @return
     */
    boolean isMaxServer(ServerInfo serverInfo);


    /**
     * 加入房间
     * @param context
     * @param room
     * @param roleID
     */
    void addZoneTeam(ChannelHandlerContext context,FightRoom room,long roleID);

    /**
     * 进入家园地图
     * @param context
     * @param modelId
     * @param level
     * @param roleId
     * @param zt
     * @param bean
     */
    void enterHouseMap(ChannelHandlerContext context, int modelId, int level, long roleId, ZoneTeam zt, Cfg_Clone_map_Bean bean);

    /**
     * 关闭战斗房间
     * @param fightRoom
     */
    void closeFightRoom(FightRoom fightRoom);
}
