package com.game.immortalequip.script;

import com.game.backpack.structs.Item;
import com.game.player.structs.Player;

import java.util.List;

/**
 * Created by clc on 2020/2/12.
 */
public interface IImmortalEquip {


    /**
     * 上线初始化部位
     * @param player
     */
    void onLineInitPart(Player player);


    /**
     * 镶嵌 穿戴
     * @param player
     * @param uid
     */
    void  ReqInlayImmortalEquip(Player player,long uid);

    /**
     * 合成
     * @param player
     * @param part
     */
    void ReqCompoundImmortal(Player player,int part);


    /**
     * 分解
     * @param player
     * @param uid
     */
    void ReqResolveImmortal(Player player,long uid);


    /**
     * 兑换
     * @param player
     * @param modelID
     */
    void ReqExchangeImmortal(Player player,int modelID);

    /**
     * 获得仙甲武器部位模型ID
     */
    int getXianjiaPart30(Player player);

    /**
     * 获得仙甲身体部位模型ID
     */
    int getXianjiaPart31(Player player);
    /**
     * 获得仙甲32部位
     */
    int getXianjiaPart32(Player player);
    /**
     * 获得仙甲33部位
     */
    int getXianjiaPart33(Player player);

    /**
     * 添加仙甲装备
     */
    boolean addImmEquip(Player player,Item item,int reason);

    /**
     * 删除仙甲
     * @param player
     * @param item
     * @param reason
     * @return
     */
    boolean delImmEquip(Player player,Item item, int reason);


    /**
     * 添加背包判断
     * @param player
     * @param itemList
     * @return
     */

    boolean canAddImmEquipBag(Player player,List<Item> itemList);


    /**
     * 切换仙甲外观
     * @param player
     * @param partType
     * @param part
     */
    void onReqChangeImmEquipAppearance(Player player,int partType,int part);


    /**
     * 获得仙甲外观
     * @param player
     * @param type
     * @return
     */
    int getImmFacadeForType(Player player,int type);




}
