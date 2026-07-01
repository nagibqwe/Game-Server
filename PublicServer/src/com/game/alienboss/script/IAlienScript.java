package com.game.alienboss.script;

import game.core.message.RMessage;
import game.core.script.IScript;
import game.message.AlienBossMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Desc TODO
 * @Date 2021/11/16 14:27
 * @Auth ZUncle
 */
public interface IAlienScript extends IScript {

    /**
     * 活动开启
     */
    void activeBegin();

    /**
     * 活动关闭
     */
    void activeEnd( );

    /**
     * 创建虚空副本
     */
    void createCity();

    /**
     * 进入虚空幻境
     * @param context
     * @param mess
     */
    void G2PEnterCrossAlien(ChannelHandlerContext context, AlienBossMessage.G2PEnterCrossAlien mess);

    /**
     * 进入须弥宝库
     * @param context
     * @param messInfo
     */
    void G2PEnterCrossAlienGem(ChannelHandlerContext context, AlienBossMessage.G2PEnterCrossAlienGem messInfo);

    /**
     * 获取虚空幻境数据
     * @param context
     * @param messInfo
     */
    void G2PCrossAlienCity(ChannelHandlerContext context, AlienBossMessage.G2PCrossAlienCity messInfo);

    /**
     * boss数据刷新消息
     * @param context
     * @param messInfo
     */
    void F2PCrossAlienBoss(ChannelHandlerContext context, AlienBossMessage.F2PCrossAlienBoss messInfo);

    /**
     * 怪物死亡消息
     * @param context
     * @param messInfo
     */
    void F2PCrossAlienBossDie(ChannelHandlerContext context, AlienBossMessage.F2PCrossAlienBossDie messInfo);
}
