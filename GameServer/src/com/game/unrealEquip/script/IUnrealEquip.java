package com.game.unrealEquip.script;

import com.game.backpack.structs.Item;
import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.HolyEquipMessage;
import game.message.UnrealEquipMessage;

import java.util.List;


/**
 * 幻装CXL
 */
public interface IUnrealEquip  extends IScript {



    /**
     * 上线初始化
     */
    void onLine(Player player);
    /**
     * 创建
     */
    Item createUnrealEquip(int id);

    /**
     * 添加
     */
    boolean addUnrealEquip(Player player,Item item, int reason);


    /**
     * 是否能添加
     * @param player
     * @param itemList
     * @return
     */
    boolean canAddUnrealEquipBag(Player player,List<Item> itemList);

    /**
     * 分解
     */
    void resolveUnrealEquip(Player player, long uid);

    /**
     * 强化部位
     */
    void intensifyUnrealPart(Player player,int part);


    /**
     * //使用幻魂
     */
    void useUnrealSoul(Player player,int itemID,int num);

    /**
     * 合成
     */
    void compoundUnrealSoul(Player player, UnrealEquipMessage.ReqCompoundUnreal message);

    /**
     * 镶嵌
     */
    void soulEquipInlay(Player player,long uid);


    /**
     * 删除装备
     */
    boolean delHolyEquip(Player player,Item item, int reason);


}
