package com.game.guildcrossfud.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.AlienBossMessage;
import game.message.GuildCrossFudMessage;

/**
 * @Desc TODO
 * @Date 2021/2/3 17:48
 * @Auth ZUncle
 */
public interface IFudScript extends IScript {

    /**
     * 获取所有福地列表
     * @param player
     * @param mess
     */
    void reqAllCrossFudInfoHandler(Player player, GuildCrossFudMessage.ReqAllCrossFudInfo mess);

    /**
     * 打开福地宝箱
     * @param player
     * @param mess
     */
    void reqCrossFudBoxOpenHandler(Player player, GuildCrossFudMessage.ReqCrossFudBoxOpen mess);

    /**
     * 关注boss
     * @param player
     * @param mess
     */
    void reqCrossFudCareBossHandler(Player player, GuildCrossFudMessage.ReqCrossFudCareBoss mess);

    /**
     * 获取福地数据
     * @param player
     * @param mess
     */
    void reqCrossFudCityInfoHandler(Player player, GuildCrossFudMessage.ReqCrossFudCityInfo mess);

    /**
     * 获取福地积分排名
     * @param player
     * @param mess
     */
    void reqCrossFudRankHandler(Player player, GuildCrossFudMessage.ReqCrossFudRank mess);

    /**
     * 打开福地个人积分宝箱奖励
     * @param player
     * @param mess
     */
    void reqCrossFudScoreBoxOpenHandler(Player player, GuildCrossFudMessage.ReqCrossFudScoreBoxOpen mess);

    /**
     * 解锁福地个人积分宝箱
     * @param player
     * @param mess
     */
    void reqCrossFudUnLockScoreBoxHandler(Player player, GuildCrossFudMessage.ReqCrossFudUnLockScoreBox mess);

    /**
     * 请求进入跨服福地
     * @param player
     * @param mess
     */
    void reqCrossFudEnterHandler(Player player, GuildCrossFudMessage.ReqCrossFudEnter mess);

    /**
     * 福地个人积分宝箱解锁
     */
    void P2GCrossFudBoxUnLock( GuildCrossFudMessage.P2GCrossFudBoxUnLock mess);

    /**
     * 福地占领奖励
     */
    void P2GCrossFudReward( GuildCrossFudMessage.P2GCrossFudReward mess);

    /**
     * 福地个人积分宝箱奖励
     */
    void P2GCrossFudScoreBoxOpen(GuildCrossFudMessage.P2GCrossFudScoreBoxOpen mess);

    /**
     *  福地状态刷新
     * @param mess
     */
    void P2GCrossFudProcess( GuildCrossFudMessage.P2GCrossFudProcess mess);

    /**
     * 福地占领奖励广播
     * @param messInfo
     */
    void P2GCrossFudOwnerNotice(GuildCrossFudMessage.P2GCrossFudOwnerNotice messInfo);

    /**
     * 获取魔王缝隙怪物列表
     * @param player
     * @param messInfo
     */
    void ReqDevilBossListHandler(Player player, GuildCrossFudMessage.ReqDevilBossList messInfo);

    /**
     * 自动关注boss
     * @param player
     */
    void autoFollowBoss(Player player);

    /**
     * 玩家开启道具宝箱
     * @param player
     * @param num
     * @return
     */
    boolean useBox(Player player, int num);

    /**
     * 进入混沌虚空
     * @param player
     * @param message
     */
    void reqEnterCrossAlienHandler(Player player, AlienBossMessage.ReqEnterCrossAlien message);

    /**
     * 进入须弥宝库
     * @param player
     * @param messInfo
     */
    void reqEnterCrossAlienGemHandler(Player player, AlienBossMessage.ReqEnterCrossAlienGem messInfo);

    /**
     * 获取虚空幻境信息
     * @param player
     * @param messInfo
     */
    void reqCrossAlienCityHandler(Player player, AlienBossMessage.ReqCrossAlienCity messInfo);
}
