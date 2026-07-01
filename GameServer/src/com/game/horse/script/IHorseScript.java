/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.horse.script;

import com.game.backpack.structs.Item;
import com.game.horse.structs.HorseEquip;
import com.game.player.structs.Player;

import java.util.List;

/**
 *
 * @author Administrator
 */
public interface IHorseScript {
    //坐骑
    
    public void onReqChangeHorse(Player player,int horseLayer);
    
    public boolean onReqChangeRideState(Player player,int rideState);

    //飞行
    
    public void onReqFlyActionHandler(Player player,boolean isFly, float x, float y); //起飞否
    
    public void onReqUpdateHightHandler(Player player, float high ); //调整高度

    void onReqInviteOtherPlayer(Player player, long invitedPlayerId);
    
    void onReqResponseInvite(Player player, long invitePlayerId, boolean agreeOrRefuse);

    /**
     * 初始化坐骑装备
     * @param player
     */
    void initPlayerHorseEquip(Player player);

    /**
     * 玩家上线
     * @param player
     */
    void online(Player player);

    void changeHorseAssiant(Player player, int assistantId, int mountModelId);

    /**
     * 坐骑脉轮穿装备
     * @param player
     * @param equipId
     * @param assistantId 脉轮id
     * @param cellId
     */
    void wearEquip(Player player, long equipId, int assistantId, int cellId);

    /**
     * 强化
     * @param player
     * @param assistantId  脉轮id
     * @param cellId 位置id
     */
    void intenHorseEquip(Player player, int assistantId, int cellId);

    /**
     * 附魂
     * @param player
     * @param assistantId 脉轮id
     * @param cellId 位置id
     */
    void soulHorseEquip(Player player, int assistantId, int cellId);

    /**
     * 坐骑脉轮装备强化激活
     * @param player
     * @param assistantId
     * @param levelId
     */
    void horseEquipIntenActive(Player player, int assistantId, int levelId);

    /**
     * 坐骑脉轮装备附魂激活
     * @param player
     * @param assistantId
     * @param levelId
     */
    void horseEquipSoulActive(Player player, int assistantId, int levelId);

    /**
     * 坐骑装备合成
     * @param player
     * @param assistantId
     * @param cellId
     * @param equipsList
     */
    void horseEquipSynthetic(Player player, int assistantId, int cellId, List<Long> equipsList);

    /**
     * 坐骑装备分解
     * @param player
     * @param equipIdList
     */
    void horseEquipDecompose(Player player, List<Long> equipIdList);

    /**
     * 添加坐骑装备
     * @param player 玩家
     * @param item 道具
     * @param reason 原因
     * @param action 行动id
     * @return
     */
    boolean addHorseEquip(Player player, Item item, int reason, long action);

    /**
     * 设置自动分解
     * @param player
     * @param set
     */
    void autoEquipDecomposeSet(Player player, boolean set);

    /**
     * 激活坐骑装备槽位
     * @param player
     * @param slotId
     */
    void activeHorseEquip(Player player, int slotId);

    /**
     * 分数更新
     * @param horseEquip
     */
    void horseEquipUpdateScore(Player player, HorseEquip horseEquip);

    /**
     * 是否可添加装备
     * @param player
     * @param horseEquipNum
     * @return
     */
    boolean canAdd(Player player, int horseEquipNum);

    /**
     * 坐骑脉轮是否激活
     * @param id
     * @return
     */
    int isHorseEquipActived(Player player, Integer id);
}
