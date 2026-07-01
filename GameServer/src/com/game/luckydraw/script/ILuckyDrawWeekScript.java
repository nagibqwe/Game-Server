package com.game.luckydraw.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.LuckyDrawMessage;

public interface ILuckyDrawWeekScript extends IScript {
    /**
     * 处理客户端的请求抽奖面板展示信息
     * @param player
     * @param msg
     */
    void onReqLuckyDrawPanelHandler(Player player, LuckyDrawMessage.ReqOpenLuckyDrawPanel msg);

    /**
     * 刷新抽奖数据
     * @param player
     */
    void onRefreshLuckyDrawData(Player player,boolean isFunctionVariable);

    /**
     * 处理客户端抽奖的操作
     * @param player
     * @param msg
     */
    void onReqLuckyDrawHandler(Player player, LuckyDrawMessage.ReqLuckyDraw msg);

    /**
     * 处理获取抽奖卷的操作
     * @param player
     * @param msg
     */
    void onReqGetLuckyDrawVolumeHandler(Player player, LuckyDrawMessage.ReqGetLuckyDrawVolume msg);

    /**
     * 获取客户端改变奖励索引
     * @param player
     * @param msg
     */
    void onReqChangeAwardIndexHandler(Player player, LuckyDrawMessage.ReqChangeAwardIndex msg);

    /**
     * 玩家关闭面板时的请求
     * @param player
     * @param msg
     */
    void onReqCloseLuckyDrawPanelHandler(Player player,LuckyDrawMessage.ReqCloseLuckyDrawPanel msg);

    /**
     * 玩家上线的调用
     * @param player
     */
    void onPlayerOnline(Player player);

    /**
     * 0点玩家刷新
     * @param player
     */
    void zeroClockHandler(Player player);

}
