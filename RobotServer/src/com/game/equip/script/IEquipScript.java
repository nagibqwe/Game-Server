package com.game.equip.script;

import com.data.bean.Cfg_Task_Bean;
import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.EquipMessage;
import game.message.backpackMessage;

public interface IEquipScript extends IScript {
    /**
     * 初始化背包数据
     *
     * @param messInfo
     */
    void initBagItem(Player player, backpackMessage.ResItemInfos messInfo);

    void setBagItem(Player player, backpackMessage.ItemInfo it);
    /**
     * 初始化装备部位信息
     * @param messInfo
     */
    void initBodyEquip(Player player, EquipMessage.ResEquipPartInfo messInfo);

    /**
     * 检查新增加的装备，战力高则穿戴上
     */
    void checkEquipWear(Player player, backpackMessage.ItemInfo it);

    /**
     * 检查背包中是否有新的装备，有则穿戴上
     */
    void checkAllEquipWear(Player player);

    /**
     * 执行穿戴装备任务,穿戴X阶X色X星X件装备  e.g:15_3_6_1_3
     * @return
     */
    int doWornEquipTask(Player player, Cfg_Task_Bean bean);
}
