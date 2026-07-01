package com.game.guildactivity.script;

import com.game.player.structs.Player;
import game.message.GuildActivityMessage;

public interface IGuildActivityHandler {
    /**
     * 请求打开排名面板，获取排行数据
     */
    void openRankPanel(Player player);

    /**
     * 打开福地boss总览
     */
    void openAllBossPanel(Player player);

    /**
     * 检查福地红点提示
     */
    void checkFudiBossRedPoint();

    void checkFudiBossRedPoint(Player player);

    /**
     * 领取日常奖励
     */
    void dayScoreReward(Player player, GuildActivityMessage.ReqDayScoreReward messInfo);

    /**
     * 关注怪物
     */
    void attentionMonster(Player player, int bossId, int type);

    /**
     * 返回福地夺宝积分
     */
    void guildNowScore(Player player);

    /**
     * 面板就绪，客户端要求在面板初始化以后再发送消息
     */
    void panelReady(Player player);

    /**
     * 宗派活动--福地称号，除去特殊称号后宗派内的成员的排名
     */
    void rank(long lastCheckTime, long now, boolean isGM);

    /**
     * 解散仙盟
     * @param guildId
     */
    void dissolveGuild(long guildId);

    /**
     * 退出仙盟
     * @param guildId
     * @param player
     */
    void quitGuild(long guildId, Player player);

    /**
     * 每小时称号重新分配，前三帮会内部排名刷新
     * @param isOver 最后一次奖励称号，将奖励永久称号
     */
    void guildMemberRankRef(boolean isOver);

    /**
     * 打开仙盟boss面板
     */
    void onReqOpenGuildBossPanel(Player player);

    /**
     * 仙盟boss开启
     * @param open
     */
    void optGuildBossActivity(boolean open);

    /**
     * 仙盟boss同步开启关闭时间
     * @return
     */
    void syncGuildBossOCTime(Player player);

    /**
     * 请求福地支援
     */
    void onReqFudiHelp(Player player, int cfgId);

    /**
     * 请求支援信息有效性
     */
    void onReqFudiCanHelp(Player player, int cfgId);

    /**
     * 获取本仙盟福地攻打信息
     * @param player
     */
    void reqMyFightingBoss(Player player);

    /**
     * 加载所有福地数据
     */
    void load();

    /**
     * 是否有
     * @param guildId
     * @return
     */
    boolean haveTopFudi(long guildId);

    /**
     * 自动关注boss
     * @param player
     */
    void autoFollowBoss(Player player);
}
