package com.game.immortalsoul.script;


import com.game.backpack.structs.Item;
import com.game.player.structs.Player;
import java.util.List;


/**
 * 仙魂接口
 * Created by 542 on 2019/7/5. cxl
 */
public interface IImmortalSoul {


    void online(Player player);

    //镶嵌
    void  reqInlaySoul(Player player,long uid,int pos);

    //脱下
    void  getoffSoul(Player player,int pos);

    //升级
    void  soulLevelUp(Player player,int pos);

    //兑换
    void  exchangeSoul(Player player,int itemid,int num);

    //合成
    void  compoundSoul(Player player,int itemid);

    //分解
    void  resolveSoul(Player player,List<Long> uids);

    void gmAddSoul(Player player,int itemID,boolean tellClient,int reason);

    //获取穿上仙魄的总等级
    int getAllOnEquipLevel(Player player);

    //获取仙魄名字
    String getImmortalSoulName(Integer id);

    /**
     * 创建灵魄
     */
    Item createSoul(int id);

    /**
     * 添加灵魄
     * @param player
     * @param item
     */
    boolean addSoul(Player player,Item item,int reason);

    /**
     * 背包检测
     * @param player
     * @param itemList
     * @return
     */
    boolean canAddSoulBag(Player player,List<Item> itemList);


    /**
     * 拆解
     * @param player
     * @param id
     */
    void onDismountingSoul(Player player,long id);


}
