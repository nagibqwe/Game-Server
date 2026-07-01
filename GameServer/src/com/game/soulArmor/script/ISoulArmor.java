package com.game.soulArmor.script;

import com.game.backpack.structs.Item;
import com.game.player.structs.Player;
import game.core.script.IScript;

import java.util.List;

/**
 * @Desc TODO
 * @Date 2020/12/23 17:37
 * @Auth ZUncle
 */
public interface ISoulArmor extends IScript {

    /**
     * 获取魂甲抽奖等级
     */
    void reqOpenSoulArmorLottery(Player player);

    /**
     * 获取魂甲背包数据
     */
    void reqSoulArmorBag(Player player);

    /**
     * 魂甲抽奖
     */
    void reqSoulArmorLottery(Player player, int type, int count, boolean isGoldDouble);

    /**
     * 魂珠分解
     */
    void reqSplitSoulArmorBall(Player player, List<Long> ids);

    /**
     * 脱下
     */
    void reqUnWearSoulArmorBall(Player player, int slotId);

    /**
     * 淬炼魂甲
     *
     * @param player
     */
    void reqUpSoulArmor(Player player);

    /**
     * 突破
     */
    void reqUpSoulArmorQuality(Player player);

    /**
     * 觉醒
     */
    void reqUpSoulArmorSkill(Player player);

    /**
     * 觉醒技能升级
     * @param player
     * @param skillId
     */
    void reqChangeArmorSkill(Player player, int skillId);

    /**
     * 强化魂珠孔位
     * @param player
     * @param slotId
     */
    void reqUpSoulArmorSlotLevel(Player player, int slotId);

    /**
     * 穿戴魂珠
     */
    void reqWearSoulArmorBall(Player player, int slotId, long id);

    /**
     * 魂印合成
     * @param player
     * @param slotId
     * @param equipsList
     */
    void reqSoulArmorMerge(Player player, int slotId, List<Long> equipsList);

    /**
     * 魂甲ID
     * @param player
     * @param soulArmorId
     */
    void wearSoulArmor(Player player ,int soulArmorId);

    /**
     * 同步魂甲信息
     * @param player
     */
    void sendSoulArmorInfo(Player player);

    /**
     * 魂甲背包能否添加道具
     *
     * @param player
     */
    boolean canAdd(Player player, List<Item> items);

    /**
     * 添加魂印
     * @param player
     * @param item
     * @param reason
     * @return
     */
    boolean addSoulArmorEquip(Player player, Item item, int reason);

    /**
     *  检测孔位激活
     * @param player
     */
    void checkActiveSlot(Player player);

}
