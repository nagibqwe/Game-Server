package com.game.soulbeast.script;

import com.game.backpack.structs.Item;
import com.game.player.structs.Player;
import com.game.skill.structs.Skill;
import game.message.SoulBeastMessage;

import java.util.List;

/**
 * Created by zcd on 2018/5/4.
 */
public interface ISoulBeast {

    /**
     * 玩家上线处理
     * @param player
     */
    void online(Player player);

    /**
     *  获取神兽技能
     * @param player
     * @return
     */
    List<Skill> sumAllChildSkill(Player player);

    /**
     * 请求魂兽出战 或脱战
     * @param player
     * @param soulId 出战魂兽配置Id
     */
    void reqSoulBeastFight(Player player, int soulId);

    /**
     * 玩家请求穿装备
     * @param player
     * @param soulBeastId
     * @param equipIds
     */
    void reqSoulBeastEquipWear(Player player, int soulBeastId, List<Long> equipIds);

    /**
     * 玩家请求脱装备
     * @param player
     * @param equipIds
     */
    void reqSoulBeastEquipDown(Player player, int soulBeastId, List<Long> equipIds);

    /**
     * 玩家增加魂兽装备
     * @param player
     * @param item
     * @return
     */
    boolean addSoulBeastEquip(Player player, Item item, int reason, long actionId);

    /**
     * 判断此道具是不是魂兽装备
     * @param item 物品
     * @return 结果
     */
    boolean isSoulBeastEquipOrItem(Item item);

    /**
     * 功能开放
     * @param player
     */
    void functionOpen(Player player);

    /**
     * 请求扩充格子
     * @param player
     */
    void reqAddGrid(Player player);

    /**
     * 创建一个魂兽装备
     * @param modleId 物品id 对应魂兽装备id
     * @return
     */
    Item createSoulBeastEquip(int modleId);

    /**
     * 创建一个魂兽道具
     *
     * @param modleId
     * @return
     */
    Item createSoulBeastItem(int modleId);

    /**
     * 出售魂兽装备
     * @param player
     * @param ids
     */
    void sellSoulBeastEquipOrItem(Player player,List<Long> ids);

    /**
     *  强化 神兽
     * @param player
     * @param fixEquipId
     * @param swallowEquipIdsList
     * @param needDouble
     */
    void reqSoulBeastEquipUp(Player player, int soulId, long fixEquipId, List<SoulBeastMessage.SoulCostItem> swallowEquipIdsList, boolean needDouble);

    /**
     * 神兽装备合成
     * @param player
     * @param part
     * @param equipIdsList
     */
    void reqSoulBeastEquipSynthetic(Player player, int part, List<Long> equipIdsList);
}
