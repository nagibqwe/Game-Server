package com.game.godbook.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.GodBook;

public interface IGodBookScript extends IScript {

    /**
     * 符咒初始化
     */
    void initAmulet(Player player);

    /**
     * 激活符咒
     */
    void onReqActiveAmulet(Player player, GodBook.ReqActiveAmulet messInfo);

    /**
     * 领取奖励
     */
    void onReqGetReward(Player player, GodBook.ReqGetReward messInfo);

    /**
     * 检查符咒任务条件进度
     */
    void checkUpdateAmulet(Player player, int type);

    /**
     * 发送天书面板信息
     */
    void sendGodBookInfo(Player player);

    /**
     * 获取符咒经验加成
     */
    int getAmuletExpRate(Player player);

}
