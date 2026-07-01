/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.crossserver.scripts;

import com.game.backpack.structs.Item;
import com.game.player.structs.Player;
import game.message.BravePeakMessage;
import game.message.CrossServerMessage;
import game.message.CrossServerMessage.*;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * 跨服服务器脚本实现
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface ICrossServerScript {

    //公共服的通知事件处理
    void OnP2GNoticeEvent(P2GNoticeEvent mess);

    //玩家退出跨服
    void OnQuitUnknowMap(Player player, int copyModleId);

    void SendFightStateToPublic(long roomid, int state);

    void OnCrossHeartTick(long time);

    void G2F_UpMorale(ChannelHandlerContext context, CrossServerMessage.G2F_UpMorale mess);

    void OnF2GResCrossDropCoin(ChannelHandlerContext context, F2GResCrossDropCoin mess);

    void OnF2GCloneClose(ChannelHandlerContext context, F2GCloneClose mess);

    void OnF2GFightEnd(ChannelHandlerContext context, F2GFightEnd mess);

    void OnG2FReqCrossUseItem(ChannelHandlerContext context, G2FReqCrossUseItem mess);

    void OnF2GResHeart(ChannelHandlerContext context, F2GResHeart mess);

    void OnG2FReqHeart(ChannelHandlerContext context, G2FReqHeart mess);

    void OnF2GResCrossDropItem(ChannelHandlerContext context, F2GResCrossDropItem mess);

    void OnF2GResCrossUseItem(ChannelHandlerContext context, F2GResCrossUseItem mess);

    void OnF2GTaskAction(ChannelHandlerContext context, CrossServerMessage.F2GTaskAction messInfo);

    void OnF2GTaskFresh(ChannelHandlerContext context, CrossServerMessage.F2GTaskRresh messInfo);

    void OnP2GPlayerStateChange(ChannelHandlerContext context, CrossServerMessage.P2GPlayerStateChange mess);

    void OnG2FReqCrossDropItemString(ChannelHandlerContext context, G2FReqCrossDropItemString mess);
    //跨服发送奖励回来
    void OnF2GSendReward(ChannelHandlerContext context, F2GSendReward mess);

    //跨服发回来得掉落信息
    void onF2GSendDropData(ChannelHandlerContext context, F2GDropData mess);

    //跨服发送邮件奖励回来
    void OnF2GSendMailReward(ChannelHandlerContext context, F2GSendMailReward mess);

    //公共服发送邮件奖励
    void onP2GSendMailReward(P2GSendMailReward mess);

    /**
     * 资源找回变化的通知
     * @param context
     * @param mess
     */
    void OnF2GResouceFindChange(ChannelHandlerContext context, F2GResourceFindChange mess);

    /**
     * 师徒任务变化的通知
     * @param context
     * @param mess
     */
    void onF2GShituTaskChange(ChannelHandlerContext context, F2GShituTaskChange0 mess);

    /**
     * 处理跨服勇者巅峰奖励
     * @param context
     * @param mess
     */
    void onF2GSendBravePeakReward(ChannelHandlerContext context, BravePeakMessage.F2GSendBravePeakReward mess);


    public void OnP2GConnectHeartRes(ChannelHandlerContext context, CrossServerMessage.P2GConnectHeartRes mess);

    /**
     * 通知关注的BOSS刷新
     * @param context
     * @param mess
     */
    void OnP2GBossRefreshTip(ChannelHandlerContext context, CrossServerMessage.P2GBossRefreshTip mess);

    /**
     * 请求跨服战报
     */
    void OnG2FReqCloneFightInfoHandler(ChannelHandlerContext context, CrossServerMessage.G2FReqCloneFightInfo mess);

    /**
     *  跨服添加经验
     */
    void OnF2GAddExp(ChannelHandlerContext context ,CrossServerMessage.F2GAddExp mess);
    /**
     *  跨服发奖
     */
    void sendReward(Player player, List<Item> items,int reason);

    /**
     * 跨服发送邮件奖励
     */
    void sendMailReward(Player player, String sender, String title, String content, List<Item> items, int reason);

    /**
     *  玩家掉线
     */
     void onG2FSynPlayerOut(G2FSynPlayerOut mess);

     void onP2GDailyData(ChannelHandlerContext context, CrossServerMessage.P2GDailyData mess);

     void onF2GSendPersonalNotice(ChannelHandlerContext context, CrossServerMessage.F2GSendPersonalNotice mess);
}
