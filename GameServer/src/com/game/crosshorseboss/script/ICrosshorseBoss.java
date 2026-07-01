package com.game.crosshorseboss.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.CrossHorseBossMessage;

/**
 * Created by cxl on 2021/4/16.
 */
public interface ICrosshorseBoss   extends IScript {


    /**
     * 取消归属
     * @param player
     */
    void onReqCancelAffiliation(Player player,int cfgId);

    /**
     * 取消归属
     * @param playerId
     * @param cfgId
     */
    void onG2FReqCancelAffiliation(long playerId,int cfgId);


    /**
     * 请求面板信息
     * @param player
     * @param level
     */
    void  onReqCrossHorseBossPanel(Player player,int level);


    /**
     * 请求关注
     * @param player
     * @param bossId
     */
    void onReqFollowCrossHorseBoss(Player player, boolean isFollow,int bossId);


    /**
     * 刷新提示
     * @param messInfo
     */
    void onP2GResCrossHorseBossRefreshTip(CrossHorseBossMessage.P2GResCrossHorseBossRefreshTip messInfo);



}
