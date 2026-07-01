package com.game.welfare.script;
import com.game.player.structs.Player;
import game.core.script.IScript;

/**
 *@desc 更新公告逻辑脚本接口
 *@author gaozhaoguang
 *@date Created on 2020/7/14 12:15
 **/
public interface IUpdateNoticeScript extends IWelfareScript {

    /**
     * 领取更新公告奖励
     * @param player 玩家
     */
     void receiveAward(Player player);

    /**
     * 刷新更新公告
     * @param text 公告文本
     * @param items 公告附带的奖励物品
     * @param reset 是否复位奖励领取列表
     */
     void refreshNotice(String text, String items, Boolean resetReceives);
}
