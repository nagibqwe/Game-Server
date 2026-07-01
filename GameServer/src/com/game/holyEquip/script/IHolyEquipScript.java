package com.game.holyEquip.script;

import com.game.backpack.structs.Item;
import com.game.player.structs.Player;
import game.message.HolyEquipMessage;

import java.util.List;

/**
 * Created by 542 on 2019/10/24.
 */
public interface IHolyEquipScript{


    /**
     * 上线初始化
     */
    void onLine(Player player);
    /**
     * 创建
     */
    Item createHolyEquip(int id);

    /**
     * 添加
     */
    boolean addHolyEquip(Player player,Item item, int reason);

    /**
     * 分解
     */
    void resolveHolyEquip(Player player,List <Long> uids);

    /**
     * 强化部位
     */
    void intensifyHolyPart(Player player,int part);


    /**
     * //使用圣魂
     */
    void useHolySoul(Player player,int itemID,int num);

    /**
     * 合成
     */
    void compoundSoul(Player player, HolyEquipMessage.ReqCompoundHoly message);

    /**
     * 镶嵌
     */
    void holyEquipInlay(Player player,long uid);
    /**
     * 自动分解设置
     */
    void ReqSetAutoResolve(Player player, HolyEquipMessage.ReqSetAutoResolve message);

    /**
     * 删除装备
     */
    boolean delHolyEquip(Player player,Item item, int reason);

    boolean canAddHolyEquipBag(Player player,List<Item> itemList);

     int getSpiritNum(Player player, int grad, int quality, int star);

    void gmclearHolyEuiqp(Player player);

    /**
     * 获取EquipHolyTypeWorn;穿戴X类圣装X件
     */

    int getHolyEquipNumForType(Player player,int type);

    /**
     * 获取圣装总等级
     * @param player
     * @return
     */
    int getHolyEquipStrengthenLevel(Player player);

    /**
     * 获取斗心
     * @param player
     * @param grade
     * @param quality
     * @return
     */
    int getDouxin(Player player, Integer grade, Integer quality);

    /**
     * 装备的圣装数量
     * @param player
     * @return
     */
    int getHolyEquipNum(Player player);
}
