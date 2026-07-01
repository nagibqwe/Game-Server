package com.game.marriage.script;

import com.game.backpack.structs.Item;
import com.game.player.structs.Player;
import game.core.script.IScript;

import java.util.List;

/**
 * 情缘活动相关脚本
 */
public interface IMarryActivityScript extends IScript {

    /**
     * 活动检测
     */
    void tick();

    /**
     * 活动结束 处理接口
     */
    void doActivityEnd();

    /**
     * 计数亲密度排序
     */
    void intimacyRank();

    /**
     * 结算亲密度排名未领取奖励
     */
    void calcRankMail();

    /**
     * 回收福袋
     */
    void recoveryMarryActivityItem(Player player);

    /**
     * 触发活动掉落
     *
     * @param career
     * @param zone   副本ID
     * @param ber    倍率
     * @return
     */
    List<Item> triggerDrop(int career, int monsterId, int zone, int ber);

    /**
     * 情缘商店购买
     *
     * @param player
     */
    void reqMarryActivityShopBuy(Player player, int shopId);

    /**
     * 获取亲密度信息
     *
     * @param player
     */
    void reqMarryActivityIntimacy(Player player);

    /**
     * 获取亲密度奖励
     *
     * @param player
     * @param id
     */
    void reqMarryActivityIntimacyReward(Player player, int id);


    /**
     * 请求情缘任务奖励
     *
     * @param player
     * @param taskID
     */
    void onReqGetMarryActivityTaskReward(Player player, int taskID);


    /**
     * 进度刷新
     *
     * @param player
     * @param type
     */
    void onRefreshUpProgress(Player player, int type, boolean isSendClient);

    /**
     * 上线推送
     *
     * @param player
     */
    void online(Player player);

    /**
     * 计算情缘活动结束 任务奖励
     */
    void calcEndMarryActivityTaskMail(Player player);
}
