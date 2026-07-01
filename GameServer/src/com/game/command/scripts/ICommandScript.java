package com.game.command.scripts;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.CommandMessage;

public interface ICommandScript extends IScript {

    /**
     * 请求加入指挥队列
     */
   void onReqJoinCommand(Player player, CommandMessage.ReqJoinCommand messInfo);

    /**
     * 请求退出指挥队列
     */
    void onReqExitCommand(Player player, CommandMessage.ReqExitCommand messInfo);

    /**
     * 请求集火目标
     */
    void onReqFocusTarget(Player player, CommandMessage.ReqFocusTarget messInfo);

    /**
     * 请求目标位置
     */
    void onReqTargetPos(Player player, CommandMessage.ReqTargetPos messInfo);

    /**
     * 请求目标位置
     */
    void onReqCommandBulletScreen(Player player, CommandMessage.ReqCommandBulletScreen messInfo);

}
