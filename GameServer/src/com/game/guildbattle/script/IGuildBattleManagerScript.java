package com.game.guildbattle.script;


import com.game.player.structs.Player;
import game.message.GuildBattleMessage;

public interface IGuildBattleManagerScript {

    /**
     * 公会评级
     */
    void guildBattleRate();

    /**
     * 公会战活动开启
     * @param mapModelId
     */
    void guildBattleBegin(int mapModelId);

    /**
     * 公会战结束
     */
    void guildBattleEnd();

    /**
     *请求公会战红点
     * @param player
     */
    void sendGuildBattleRed(Player player);

    /**
     * 请求公会评级列表
     * @param player
     */
    void reqGuildBattleRate(Player player);

    /**
     * 请求公会战记录
     * @param player
     */
    void reqGuildBattleRecord(Player player);

    /**
     * 请求公会战连赢或者终结数据
     * @param player
     */
    void reqGuildBattleWin(Player player);

    /**
     *  请求工会战连赢奖励
     * @param player
     * @param id
     */
    void reqGuildBattleRecordReward(Player player, int id);



    /**
     * 请求回城
     * @param player
     */
    void reqGuildBattleBack(Player player);

    /**
     * 请求召集
     * @param player
     */
    void reqGuildBattleCall(Player player);

    /**
     * 请求点赞
     * @param player
     */
    void reqGuildBattleLike(Player player, GuildBattleMessage.ReqGuildBattleLike mess);

    /**
     * 请求战绩结果
     * @param player
     */
    void reqGuildBattleResult(Player player);

    /**
     * 请求活动内战绩
     * @param player
     */
    void reqGuildBattleStat(Player player);

    /**
     * 解散仙盟信息
     * @param guildId
     */
    void dissolveGuild(long guildId);

    /**
     * 发送弹幕
     * @param player
     * @param messInfo
     */
    void reqSendBulletScreen(Player player, GuildBattleMessage.ReqSendBulletScreen messInfo);
}
