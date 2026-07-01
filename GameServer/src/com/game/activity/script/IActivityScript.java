package com.game.activity.script;

import com.game.activity.struct.ActivityConfig;
import com.game.player.structs.Player;
import game.core.script.IRunScript;

/**
 * 运营活动接口类
 */
public interface IActivityScript extends IRunScript {

    /**
     * 请求操作运营活动
     */
    void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg);

    /**
     * 解析活动自己的自定义配置
     */
    boolean parseCustomConfig(ActivityConfig actCfg, String customStr);

    /**
     * 活动配置更新处理
     */
    boolean updateCustomConfig(ActivityConfig actCfg, String customStr);

    /**
     * 生成活动数据字符串
     */
    String getActivityDataStr(ActivityConfig actCfg, long roleId);

    /**
     * 活动结束时每个活动的特殊处理
     */
    void activityEndDeal(ActivityConfig actCfg);

    /**
     * 玩家上线处理
     */
    void playerOnline(Player player, ActivityConfig actCfg);

    /**
     * 0点处理某个玩家活动数据
     */
    void zeroClockPlayerDeal(Player player, ActivityConfig actCfg);

    /**
     * 5点处理某个玩家活动数据
     */
    void fiveClockPlayerDeal(Player player, ActivityConfig actCfg);

    /**
     * 0点处理运营活动数据
     */
    void zeroClockDeal(ActivityConfig actCfg);

    /**
     * 5点处理运营活动数据
     */
    void fiveClockDeal(ActivityConfig actCfg);

    /**
     * 每隔一小时处理运营活动
     */
    void everyHourDeal(ActivityConfig actCfg);

    /**
     * 活动掉落
     * @param player
     * @param bossId
     * @return
     */
    boolean bossDrop(Player player, int bossId, ActivityConfig actCfg);

    /**
     * 活动掉落
     * @param player
     * @param boxId
     * @return
     */
    boolean boxDrop(Player player, int boxId, ActivityConfig actCfg);

    /**
     * 副本掉落
     * @param player
     * @param cloneId
     * @return
     */
    boolean cloneDrop(Player player, int cloneId, ActivityConfig actCfg);

    /**
     * 充值后的处理
     * @param player
     */
    void rechargeDeal(Player player, int getGoodsCfgId, int rechargeNum, ActivityConfig actCfg);

    /**
     * 活动消耗
     */
    void consumeDeal(Player player, int coinType, int consumeNum, ActivityConfig actCfg);
}

