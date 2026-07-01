package com.game.worldbonfire.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.WorldBonfireMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Description 世界篝火逻辑脚本
 * @auther lw
 * @create 2019-10-15 9:58
 */
public interface IWorldBonfireScript extends IScript {

    /**
     * 请求篝火升级
     * @param player
     */
    void onBonfireLevel(Player player);

    /**
     * 请求本服扣取道具
     * @param player
     * @param lv
     */
    void onBonfireDecItem(Player player, int lv);

    /**
     * 请求跨服扣取道具
     * @param context
     * @param player
     * @param lv
     */
    void onBonfireCrossDecItem(ChannelHandlerContext context, Player player, int lv);

    /**
     * 请求跨服所有线路篝火升级
     */
    void onBonfireCrossLevel(WorldBonfireMessage.P2FWorldBonfireAddWoodLv messInfo);

    /**
     * 请求匹配
     * @param player
     */
    void onBonfireFingerMatch(Player player);

    /**
     * 请求猜拳
     * @param player
     * @param teamId
     * @param total
     * @param type
     */
    void onBonfireFingerGuess(Player player, long teamId, int total, int type);

    /**
     * 请求猜拳离开
     * @param player
     * @param teamId
     */
    void onBonfireFingerLeave(Player player, long teamId);

    /**
     * 请求篝火领奖
     * @param player
     */
    void onBonfireFingerReward(Player player);

    /**
     * 篝火划拳领取奖励
     * @param player
     */
    void onBonfireFingerLocalReward(Player player);

    /**
     * 篝火划拳跨服领取奖励
     * @param player
     */
    void onBonfireFingerCrossReward(Player player);
    /**
     * 请求匹配取消
     * @param player
     */
    void onBonfireFingerCancelMatch(Player player);
}
