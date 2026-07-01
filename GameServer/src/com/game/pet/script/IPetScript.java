package com.game.pet.script;

import com.game.backpack.structs.Item;
import com.game.map.structs.MapObject;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import game.message.PetMessage;

import java.util.List;

public interface IPetScript {
    /**
     * 系统激活初始化宠物
     */
    void initPet(Player player, int funcId);

    /**
     * 完成任务激活宠物
     */
    void finishTaskGetPet(Player player, int taskId);

    /**
     * 玩家上线处理
     */
    void online(Player player, boolean funcOpen);

    /**
     * 获取出战宠物
     */
    Pet getBattlePet(Player player);

    /**
     * 宠物出战
     */
    boolean callPet(Player player, int petId);

    /**
     * 宠物召回
     */
    boolean recyclePet(Player player);

    /**
     * 请求动作类型，1：激活，2：强化，3：出战，4：休息
     */
    boolean petAction(Player player, int actType, int modelId, boolean isGm);

    /**
     * 计算宠物属性
     */
    void calPetAttribute(Pet pet);

    /**
     * 吞噬装备
     */
    void eatEquip(Player player, PetMessage.ReqEatEquip messInfo);

    /**
     * 吞噬御魂
     */
    void eatSoul(Player player, PetMessage.ReqEatSoul messInfo);

    /**
     * 下线处理
     */
    void offLine(Player player);

    /**
     * 穿装备
     * */
    void wearEquip(Player player, long equipId, int assistantId, int cellId);

    /**
     * 脱装备
     * */
    void unwearEquip(Player player, int assistantId, int cellId);

    /**
     * 往宠物背包添加装备
     * */
    boolean addPetEquip(Player player, Item item, int reason);

    /**
     * 宠物背包中扣除装备
     * */
    Item removePetEquip(Player player, long itemId, int reason, long action);

    /**
     * 更换助阵宠物
     * */
    void changePetAssiant(Player player, int assistantId, int petId);

    /**
     * 强化宠物装备
     * */
    void intenPetEquip(Player player, int assistantId, int cellId);

    /**
     * 附魂（进阶）宠物装备
     * */
    void soulPetEquip(Player player, int assistantId, int cellId);

    /**
     * 宠物装备全身强化属性激活
     * */
    void petEquipIntenActive(Player player, int assistantId, int levelId);

    /**
     * 宠物装备全身附魂属性激活
     * */
    void petEquipSoulActive(Player player, int assistantId, int levelId);

    /**
     * 宠物装备合成
     * */
    void petEquipSynthetic(Player player, int assistantId, int cellId, List<Long> eqs);

    /**
     * 宠物装备分解
     * */
    void petEquipDecompose(Player player, List<Long> equipId);

    /**
     * 设置宠物装备自动分解
     * */
    void autoEquipDecomposeSet(Player player, boolean set);

    /**
     * 激活宠物装备槽位
     */
    void activePetEquip(Player player, Integer slotId);

    /**
     * 统计宠物装备槽激活个数
     * @param player
     * @return
     */
    int calcPetEquipSlotNumber(Player player);

    /**
     * 是否能添加
     * @param player
     * @param petEquipNum
     * @return
     */
    boolean canAdd(Player player, int petEquipNum);
}
