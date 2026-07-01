package com.game.guildcrossfud.script;

import com.game.guildcrossfud.struct.FudCity;
import com.game.guildcrossfud.struct.FudGroup;
import game.core.script.IScript;
import game.message.GuildCrossFudMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Desc TODO
 * @Date 2021/2/2 14:35
 * @Auth ZUncle
 */
public interface IFudScript extends IScript {

    /**
     * 请求跨服福地数据
     *
     * @param context
     * @param mess
     */
    void G2PAllCrossFudInfo(ChannelHandlerContext context,  GuildCrossFudMessage.G2PAllCrossFudInfo mess);

    /**
     * 解锁积分宝箱
     *
     * @param context
     * @param mess
     */
    void G2PCrossFudUnLockScoreBox(ChannelHandlerContext context,  GuildCrossFudMessage.G2PCrossFudUnLockScoreBox mess);

    /**
     * 领取积分宝箱奖励
     *
     * @param context
     * @param mess
     */
    void G2PCrossFudScoreBoxOpen(ChannelHandlerContext context,GuildCrossFudMessage.G2PCrossFudScoreBoxOpen mess);

    /**
     * 领取福地宝箱奖励
     *
     * @param context
     * @param mess
     */
    void G2PCrossFudBoxOpen(ChannelHandlerContext context,  GuildCrossFudMessage.G2PCrossFudBoxOpen mess);

    /**
     * 获取福地详情
     *
     * @param context
     * @param mess
     */
    void G2PCrossFudCityInfo(ChannelHandlerContext context,  GuildCrossFudMessage.G2PCrossFudCityInfo mess);

    /**
     * 获取福地积分排名
     *
     * @param context
     * @param mess
     */
    void G2PCrossFudRank(ChannelHandlerContext context, GuildCrossFudMessage.G2PCrossFudRank mess);

    /**
     * 关注boss
     * @param context
     * @param mess
     */
    void G2PCrossFudCareBoss(ChannelHandlerContext context, GuildCrossFudMessage.G2PCrossFudCareBoss mess);

    /**
     * 战斗服 同步福地数据到公共服
     * @param context
     * @param mess
     */
    void F2PCrossFudInfo(ChannelHandlerContext context, GuildCrossFudMessage.F2PCrossFudInfo mess);

    /**
     * 福地占领通知
     * @param context
     * @param messInfo
     */
    void F2PCrossFudGain(ChannelHandlerContext context, GuildCrossFudMessage.F2PCrossFudGain messInfo);

    /**
     * 请求进入跨服福地
     * @param context
     * @param mess
     */
    void G2PCrossFudEnter(ChannelHandlerContext context, GuildCrossFudMessage.G2PCrossFudEnter mess);

    /**
     * 加载跨服福地数据
     */
    void load();

    /**
     * 关闭服务器
     */
    void close();

    /**
     * 重新分配福地
     */
    void allocCity( boolean  zeroMatchTick);

    /**
     * 活动开启
     */
    void activeBegin();

    /**
     * 活动关闭
     */
    void activeEnd();

    /**
     * 福地心跳
     */
    void tick();

    /**
     * 刷新boss
     * @param group
     * @param city
     */
    void refreshCityBoss(FudGroup group, FudCity city);

    /**
     * 获取魔王缝隙怪物列表
     * @param context
     * @param messInfo
     */
    void G2PDevilBossList(ChannelHandlerContext context, GuildCrossFudMessage.G2PDevilBossList messInfo);

    /**
     * 通知公共服检测房间
     * @param context
     */
    void G2PSyncRoomInfo(ChannelHandlerContext context);

    /**
     * 福地boss击杀
     * @param context
     * @param messInfo
     */
    void F2PKillFudBoss(ChannelHandlerContext context, GuildCrossFudMessage.F2PKillFudBoss messInfo);
}
