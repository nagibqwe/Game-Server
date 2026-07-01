package com.game.crossfight.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.ChatMessage;
import game.message.CrossFightMessage.*;
import game.message.CrossServerMessage;
import game.message.CrossServerMessage.F2GCloneEnterAddOne;
import io.netty.channel.ChannelHandlerContext;

/**
 * 跨服战的协议接口
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface ICrossFightScript extends IScript {

    void OnP2GOutFightRoom(P2GOutFightRoom mess);

    void OnP2GResFightStart(ChannelHandlerContext context, P2GResFightStart mess);

    void OnReqOutFightRoom(ChannelHandlerContext context, Player player, ReqOutFightRoom mess);

    void OnF2GCloneEnterAddOne(ChannelHandlerContext context, F2GCloneEnterAddOne mess);


    public void OnF2GCloneCDRecordAdd(ChannelHandlerContext context, CrossServerMessage.F2GCloneCDRecordAdd messInfo);

    public void OnG2FNoticeSynRoleInfo(ChannelHandlerContext context, G2FNoticeSynRoleInfo messInfo);

    public void OnG2FSynPowerAttAndFace(ChannelHandlerContext context, G2FSynPowerAttAndFace messInfo);

    public void OnF2GPlayerOutCrossWorldMap(ChannelHandlerContext context, F2GPlayerOutCrossWorldMap messInfo);


    public void OnG2FSynPlayerInfo(ChannelHandlerContext context, G2FSynPlayerInfo messInfo);

    public void OnP2GCheckCrossInfoRes(ChannelHandlerContext context, P2GCheckCrossInfoRes messInfo);

    public void OnF2GSynPlayerInfoResult(ChannelHandlerContext context, F2GSynPlayerInfoResult messInfo);

    void P2FCreateCityMap(ChannelHandlerContext context, P2FCreateCityMap mess);

    /**
     * 机器人跨服助战
     */
    void G2FSynRobotInfoToHelpBattle(ChannelHandlerContext context , G2FSynRobotInfoToHelpBattle mess);

    /**
     * 发送变量信息给游戏服
     */
    void sendF2GSynRoleFVInfo(Player player, int fvType, int fvValue, int type, long con, int resetType, long value);

    /**
     * 处理战斗服发过来得功能变量信息
     */
    void onF2GSynRoleFVInfo(ChannelHandlerContext context, F2GSynRoleFVInfo mess);

    /**
     * 关闭战斗副本
     * @param mapId
     */
    void closeMap(long mapId);

    /**
     * 公告返回给 逻辑服执行
     * @param player
     * @param personalNotice
     */
    void sendF2GSendPersonalNotice(Player player, ChatMessage.PersonalNotice.Builder personalNotice);
}
