package com.game.chat.script;

import com.game.player.structs.Player;

/**
 * 分享脚本接口
 *
 * @author luosv
 * Created on 2018/6/6 0006.
 */
public interface IShareScript {

    /**
     * 请求领取分享奖励
     *
     * @param player  玩家
     * @param shareId 分享ID
     */
    void onReqGetShareReward(Player player, int shareId);

    /**
     * 通知客户端分享
     *
     * @param player 玩家
     * @param type   类型
     * @param params 参数
     */
    void sendShareNotice(Player player, int type, String... params);

    /**
     * 玩家拒绝了你的分享
     *
     * @param player  玩家
     * @param shareId 分享ID
     */
    void onReqRefuseShare(Player player, int shareId);

    /**
     * GM操作
     *
     * @param player 玩家
     * @param params 参数
     */
    void gmAction(Player player, String[] params);

}
