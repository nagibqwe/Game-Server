package com.game.couplefight.scripts;

import com.game.player.structs.Player;
import game.core.script.IScript;

public interface ICoupleEscort extends IScript {


    /**
     * 活动状态切换
     * @param isStart
     */
    void onChangeCoupleEscortState(boolean isStart);

    /**
     * 请求仙女护送
     * @param player
     * @param type
     */
    void onReqEnterCoupleEscort(Player player,int type);


    /**
     * 请求护送结束
     * @param player
     */
    void onReqCoupleEscortOver(Player player);


    /**
     * 玩家掉线或者退出游戏
     * @param player
     */
    void onLeaveGame(Player player);
}
