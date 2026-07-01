package com.game.player.script;

import com.game.player.structs.GlobalPlayerWorldInfo;
import com.game.player.structs.Item;
import com.game.player.structs.ItemCoin;
import com.game.player.structs.SaveDeal;
import game.core.script.IScript;
import game.message.PlayerMessage;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public interface IGlobalPlayerScript extends IScript {

    /**
     * 玩家心跳
     */
    void tick();

    /**
     * 玩家心跳
     * @param curTime
     * @param player
     */
    void tick(long curTime, GlobalPlayerWorldInfo player);

    /**
     * 加载全球玩家数据
     */
    void loadAll();

    /**
     * 获得货币
     * @param player
     * @param coin
     * @param count
     * @param changeReason
     */
    void addCoin(GlobalPlayerWorldInfo player, ItemCoin coin, long count, int changeReason);

    /**
     * 发放道具 九 零一起玩 www.901 75.com
     * @param player
     * @param items
     * @param changeReason
     */
    void addItems(GlobalPlayerWorldInfo player, List<Item> items, int changeReason);

    /**
     * 保存玩家
     */
    void save2DB(GlobalPlayerWorldInfo player, SaveDeal deal);

    /**
     * 更新玩家数据
     * @param channel
     * @param mess
     */
    void G2SSynPlayerSocialInfo(ChannelHandlerContext channel, PlayerMessage.G2SSynPlayerSocialInfo mess);


    /**
     * 查看玩家信息
     * @param channel
     * @param messInfo
     */
    void G2SReqPlayerSummaryInfoHandler(ChannelHandlerContext channel,PlayerMessage.G2SReqPlayerSummaryInfo messInfo);

    /**
     * 保存所有玩家数据
     */
    void saveAll();
}
