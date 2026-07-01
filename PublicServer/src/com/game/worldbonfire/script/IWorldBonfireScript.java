package com.game.worldbonfire.script;

import com.game.fightroom.structs.FightRoom;
import game.core.script.IScript;
import game.message.WorldBonfireMessage;
import game.message.WorldBonfireMessage.G2PWorldBonfireFinger;
import game.message.WorldBonfireMessage.G2PWorldBonfireMatch;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Description
 * @auther lw
 * @create 2019-10-17 15:38
 */
public interface IWorldBonfireScript extends IScript {

    /**
     * 活动开始
     */
    void beginWorldBonfire(int round);

    /**
     * 活动结束
     */
    void endWorldBonfire();

    /**
     * 匹配tick
     */
    void tickWorldBonfireMatch();

    /**
     * 匹配tick队列
     */
    void tickWorldBonfireMatchTeam();

    /**
     * 玩家进入世界篝火
     * @param context
     * @param roleId
     */
    void onWorldBonfireEnter(ChannelHandlerContext context, long roleId);

    /**
     * 玩家请求世界篝火面板
     * @param context
     * @param roleId
     */
    void onWorldBonfirePanel(ChannelHandlerContext context, long roleId,int gatherCount);

    /**
     * 请求跨服篝火检查升级
     * @param context
     * @param roleId
     */
    void onWorldBonfireCheckLevel(ChannelHandlerContext context, long roleId);

    /**
     *
     * @param messInfo
     */
    void onWorldBonfireLevel(WorldBonfireMessage.F2PWorldBonfireAddWood messInfo);

    /**
     * 玩家世界篝火匹配
     * @param context
     * @param messInfo
     */
    void onWorldBonfireMatch(ChannelHandlerContext context, G2PWorldBonfireMatch messInfo);

    /**
     * 玩家世界篝火划拳
     * @param messInfo
     */
    void onWorldBonfireFinger(G2PWorldBonfireFinger messInfo);

    /**
     * 玩家世界篝火划拳离开
     * @param teamId
     * @param roleId
     */
    void onWorldBonfireFingerLeave(long teamId, long roleId);

    /**
     * 玩家世界篝火划拳领奖
     * @param context
     * @param roleId
     */
    void onWorldBonfireReward(ChannelHandlerContext context, long roleId);

    /**
     * 玩家世界篝火匹配取消
     * @param context
     * @param roleId
     */
    void onWorldBonfireCancelMatch(ChannelHandlerContext context, long roleId);

}
