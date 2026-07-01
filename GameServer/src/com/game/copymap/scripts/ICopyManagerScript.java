package com.game.copymap.scripts;

import com.data.bean.Cfg_Clone_map_Bean;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.CopyMapMessage;
import game.message.ZoneMessage;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;


/**
 * @author lw
 */
public interface ICopyManagerScript extends IScript {
    /**
     * 进入副本
     * @param player
     * @param modelId
     */
    boolean onReqCopyMapEnter(Player player, int modelId, int param);

    /**
     * 退出副本
     * @param player
     */
    void onReqCopyMapOut(Player player);

    /**
     * 检查玩家进入副本
     * @param player
     * @param bean
     * @param level
     * @param leader
     * @return
     */
    boolean copyMapCheck(Player player, Cfg_Clone_map_Bean bean, int level, Player leader);

    /**
     * 副本进度刷怪
     * @param map
     * @param copyModelID
     * @param loop
     * @return
     */
    List<Monster> copyMapRefreshMonster(MapObject map, int copyModelID, int loop);

    /**
     * 副本队伍准备队列
     */
    void copyMapTeamReady();

    /**
     * 在组队情况下准备
     * @param player
     * @param mess
     */
    void onReqReadyZone(Player player, ZoneMessage.ReqReadyZone mess);

    /**
     * 跨服反馈玩家的准备状态值
     * @param context
     * @param mess
     */
    void onP2GResReadyZone(ChannelHandlerContext context, ZoneMessage.P2GResCrossZoneReadyZone mess);

    /**
     * 跨服反馈创建跨服的状态值
     * @param context
     * @param mess
     */
    void onP2GResEnterZone(ChannelHandlerContext context, ZoneMessage.P2GResEnterZone mess);

    /**
     * 玩家取消跨服匹配
     * @param player
     */
    void onReqCancelMatch(Player player);

    /**
     * 匹配成功后通知玩家
     * @param context
     * @param mess
     */
    void onP2GMatchSucceed(ChannelHandlerContext context, ZoneMessage.P2GReqMatchSucceed mess);

    /**
     * 跨服回来取消玩家标志
     * @param context
     * @param mess
     */
    void onP2GReqCancelCrossTag(ChannelHandlerContext context, ZoneMessage.P2GReqCancelCrossTag mess);

    /**
     * 写日志
     * @param cloneId
     * @param player
     * @param isAuto
     */
    void writeEnterLog(int cloneId, Player player, boolean isAuto);

    /**
     * 客户端通知刷怪
     * @param player
     * @param messInfo
     */
    void onReqFlashMonster(Player player, CopyMapMessage.ReqFlashMonster messInfo);

    /**
     * 增加跨服副本进入次数
     * @param map
     * @param player
     */
    void sendF2GCloneEnterAddOne(MapObject map, Player player,Object... objects);

    /**
     * 数据初始化
     */
    void load();

}
