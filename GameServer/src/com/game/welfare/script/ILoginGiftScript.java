package com.game.welfare.script;

import com.game.backpack.structs.Item;
import com.game.player.structs.Player;
import game.message.WelfareMessage;

import java.util.List;

public interface ILoginGiftScript extends IWelfareScript {
    /**
     * 请求领奖
     * @param player
     * @param day
     */
    void onReqLoginGiftReward(Player player, int day);

    /**
     * 获得奖励
     * @param player
     * @param type
     * @param item
     */
    void freshWelfareRewardNtf(Player player, WelfareMessage.WelfareType type, List<Item> item);
}
