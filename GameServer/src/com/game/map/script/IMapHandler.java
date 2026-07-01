package com.game.map.script;

import com.game.player.structs.Player;
import game.message.MapMessage;
import game.message.MapMessage.*;

/**
 * 地图的处理接口
 *
 * @author admin
 */
public interface IMapHandler {

    void OnReqDirMove(Player player, ReqDirMove messInfo);

    void OnReqGather(Player player, ReqGather messInfo);

    void OnReqGetLines(Player player, ReqGetLines messInfo);

    void OnReqGetMonsterPos(Player player, ReqGetMonsterPos messInfo);

    void OnReqJumpBlock(Player player, ReqJumpBlock messInfo);

    void OnReqJump(Player player, ReqJump messInfo);

    void OnReqMoveTo(Player player, ReqMoveTo messInfo);

    void OnReqJumpDownHandler(Player player, ReqJumpDown messInfo);

    void OnReqRelive(Player player, ReqRelive messInfo);

    void OnReqSelectLine(Player player, ReqSelectLine messInfo);

    void OnReqStopMove(Player player, ReqStopMove messInfo);

    void OnReqGroundBuffStar(Player player, ReqGroundBuffStar messInfo);

    /**
     * 客户端同步玩家位置
     *
     * @param player
     * @param messInfo
     */
    void OnReqSynPos(Player player, MapMessage.ReqSynPos messInfo);

    /**
     * 宠物移动
     * @param player
     * @param messInfo
     */
    void OnReqPetMoveTo(Player player, ReqPetMoveTo messInfo);

    /**
     * 宠物移动停止
     * @param player
     * @param messInfo
     */
    void OnReqPetStopMove(Player player, ReqPetStopMove messInfo);

    /**
     * 宠物瞬移
     * @param player
     * @param messInfo
     */
    void OnReqPetJumpBlock(Player player, ReqPetJumpBlock messInfo);

    void OnReqFabaoJumpBlock(Player player, ReqFabaoJumpBlock messInfo);
}
