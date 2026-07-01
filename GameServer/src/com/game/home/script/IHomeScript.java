package com.game.home.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.HomeMessage;

import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/6/28 20:42
 * @Auth ZUncle
 */
public interface IHomeScript extends IScript {


    void reqAuthHomePemHandler(Player player, HomeMessage.ReqAuthHomePem messInfo);

    void reqEnterHomeHandler(Player player, HomeMessage.ReqEnterHome messInfo);

    void reqHomeInfoHandler(Player player, HomeMessage.ReqHomeInfo messInfo);

    void reqHomeTrimMatchScoreHandler(Player player, HomeMessage.ReqHomeTrimMatchScore messInfo);

    void reqHomeTrimRankHandler(Player player, HomeMessage.ReqHomeTrimRank messInfo);

    void reqHomeTrimVoteHandler(Player player, HomeMessage.ReqHomeTrimVote messInfo);

    void reqHomeVisitorGiftListHandler(Player player, HomeMessage.ReqHomeVisitorGiftList messInfo);

    void reqHomeVisitorNoteHandler(Player player, HomeMessage.ReqHomeVisitorNote messInfo);

    void reqRandomHomeTrimTargetHandler(Player player, HomeMessage.ReqRandomHomeTrimTarget messInfo);

    void reqSendVisitorGiftHandler(Player player, HomeMessage.ReqSendVisitorGift messInfo);

    void reqHomeDecorate(Player player, HomeMessage.ReqHomeDecorate messInfo);

    void reqHomeLevelUp(Player player, HomeMessage.ReqHomeLevelUp messInfo);

    void reqGetTupRewardHandler(Player player, HomeMessage.ReqGetTupReward messInfo);

    void reqHomeBuy(Player player, HomeMessage.ReqHomeBuy messInfo);

    void S2GEnterHome(Player player, HomeMessage.S2GEnterHome messInfo);

    void S2GHomeInfo(Player player, HomeMessage.S2GHomeInfo messInfo);

    /**
     * 添加家具
     * @param player
     * @param furnitureId
     * @param count
     * @param changeReason
     * @return
     */
    boolean addFurniture(Player player, int furnitureId, int count, int changeReason);

    /**
     * 领取任务奖励
     *
     * @param player
     * @param id
     */
    void reqTaskRewardHandler(Player player, int id);

    /**
     * 发送任务列表
     *
     * @param player
     */
    void sendTask(Player player);

    /**
     * 更新任务
     *
     * @param player
     * @param type
     * @param params
     */
    void doTaskAction(Player player, int type, List<Integer> params);

    void sendHomeShop(Player player);

    /**
     * 处理场景改变
     * @param messInfo
     */
    void doSceneChange(HomeMessage.S2FHomeSceneChange messInfo);
}
