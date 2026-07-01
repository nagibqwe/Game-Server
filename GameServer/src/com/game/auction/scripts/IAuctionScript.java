package com.game.auction.scripts;

import com.game.backpack.structs.Item;
import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.EquipMessage;

import java.util.List;

/**
 * @Description
 * @auther lw
 * @create 2019-10-08 15:44
 */

public interface IAuctionScript extends IScript {

    void auctionOnline(Player player);
    /**
     * 请求竞拍列表
     * @param player
     */
    void auctionList(Player player);

    /**
     * 请求竞拍信息列表
     * @param player
     */
    void auctionRecordList(Player player);

    /**
     * 竞拍
     * @param player
     * @param auctionId
     * @param price
     */
    void auction(Player player, long auctionId, int price);

    /**
     * 上架
     * @param player
     * @param itemUid
     * @param num
     * @param type
     */
    void auctionPut(Player player, long itemUid, int num, int type, String password, int price);

    /**
     * 活动上架
     * @param roleIDlist 分红玩家list
     * @param putItemList 上架物品LIST
     * @param dailyID 活动ID
     * @param guildID 工会ID
     */
    void auctionActivityPut(List<Long> roleIDlist , List<Item> putItemList, int dailyID , long guildID);


    /**
     * 下架
     * @param player
     * @param auctionId
     */
    void auctionOut(Player player, long auctionId);

    /**
     * 购买
     * @param player
     * @param auctionId
     */
    void auctionPur(Player player, long auctionId, String password);

    /**
     * 快速购买
     * @param player
     * @param mess
     * @return
     */
    void auctionFastPur(Player player, EquipMessage.ReqEquipSyn mess);

    /**
     * 竞拍tick
     */
    void auctionTick();


    /**
     * 检测是否在仙盟竞拍
     */
    boolean checkIsGuildAuction(Player player,long roleID);

}
