/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.soulanimalforest.script;

import game.core.script.IScript;
import game.message.CrossServerMessage;
import game.message.SoulAnimalForestMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * 魂兽森林的脚本支持系统
 *
 * @author xuchangming <xysoko@qq.com>
 */
public interface ISoulAnimalForestScript extends IScript {

    //--------------------先处理协议------------
    void onF2PReqCloneMonsterDie(ChannelHandlerContext session, SoulAnimalForestMessage.F2PReqCloneMonsterDie messInfo);

    void onF2PReqSoulAnimalForestBossInfo(ChannelHandlerContext session, SoulAnimalForestMessage.F2PReqSoulAnimalForestBossInfo messInfo);

    void onG2PReqSoulAnimalForestCrossPanel(ChannelHandlerContext session, SoulAnimalForestMessage.G2PReqSoulAnimalForestCrossPanel messInfo);

    void onG2PReqFollowSoulAnimalForestCrossBoss(ChannelHandlerContext session, SoulAnimalForestMessage.G2PReqFollowSoulAnimalForestCrossBoss messInfo);

    void onF2PUpdateOneSoulAnimalForestBossInfo(ChannelHandlerContext session, SoulAnimalForestMessage.F2PUpdateOneSoulAnimalForestBossInfo messInfo);
    
    void onF2PSoulAnimalCloneOpen(ChannelHandlerContext session, SoulAnimalForestMessage.F2PSoulAnimalCloneOpen messInfo);
    
    void onG2PReqCrossSoulAnimalForestBossKiller(ChannelHandlerContext session, SoulAnimalForestMessage.G2PReqCrossSoulAnimalForestBossKiller messInfo);

    //-------------------协议完成---------------//
    //------------------计时调试
    void init();

    void reloadBossData();

    void tickBossData();

    void onG2PFirstKillBossRefreshTime(ChannelHandlerContext session, long roleId);

    /**
     * GM指令刷新Boss
     * @param groupID
     * @param cloneMapID
     * @param configID
     * @param type
     */
    void gmRefreshSoulBoss(int groupID,int cloneMapID,int configID,int type);

    /**
     * 使用刷新boss劵
     * 跨服通知公共服刷新boss
     */
    void onF2PMakeBossRefresh(CrossServerMessage.F2PMakeBossRefresh messInfo);

}
