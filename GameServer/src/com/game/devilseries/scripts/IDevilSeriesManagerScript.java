package com.game.devilseries.scripts;

import com.game.backpack.structs.Item;
import com.game.devilseries.structs.Devilintegral;
import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.DevilSeriesMessage;

import java.util.List;

public interface IDevilSeriesManagerScript extends IScript {

    /**
     * 激活玩家魔魂
     * @param player 玩家
     */
    void activeDevilCard(Player player);

    /**
     * 玩家在线的处理
     * @param player 玩家
     */
    public void online(Player player);

    /**
     * 魔魂升级/升阶/解锁
     */
    void devilCardUp(Player player, int campId, int cardId, int type);

    /**
     * 魔魂突破
     * @param player 玩家
     */
    void devilCardBreak(Player player, int campId, int cardId, List<Long> equipIds);

    /**
     * 装备穿戴
     * @param player 玩家
     */
    void devilEquipWear(Player player, int campId, int cardId, int cellId, long equipId);

    /**
     * 添加装备
     * @param player 玩家
     * @param item 道具
     * @param reason 原因码
     * @param action 行动id
     * @return
     */
    boolean addDevilEquip(Player player, Item item, int reason, long action);

    /**
     * 是否可以添加装备
     * @param player
     * @param needDevilSoulGridNum
     * @return
     */
    boolean canAdd(Player player, int needDevilSoulGridNum);

    /**
     * 魔魂装备合成
     * @param player 玩家
     */
    void devilEquipSynthesis(Player player, int modelId, List<Long> equipIds);


    /**
     * 抽奖
     * @param player
     * @param huntType
     * @param consecutiveType
     */
     void onReqDevilHuntHandler(Player player,int huntType,int consecutiveType);

    /**
     * 打开抽奖面板
     * @param player
     */
     void onReqDevilHuntPanelHandler(Player player);



     //------------------------除魔团接口 Start-------------------


    /**
     * 请求关注
     * @param player
     */
    void onReqFollowDeviBoss(Player player,Integer cloneId,boolean followValue);


    /**
     * 请求打开除魔团面板
     * @param player
     */
    void onReqOpenDeviBossPanel(Player player);


    /**
     * 请求开启除魔团
     */
    void onReqCreateDeviBossMap(Player player,Integer cloneId);

    /**
     * 进入除魔团
     * @param player
     * @param mapId
     */
    void onReqEnterDeviBossMap(Player player,long mapId);


    /**
     * 请求积分面板
     * @param player
     */
    void onReqSynDeviBossIntegral(Player player);

    /**
     * 排序
     */
    int compareIntegral(Devilintegral i1, Devilintegral i2);


    /**
     * 默认关注副本
     * @param player
     */
    void followDefaultDeviCopy(Player player);


    //------------------------除魔团接口 end-------------------


}
