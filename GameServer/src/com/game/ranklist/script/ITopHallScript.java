package com.game.ranklist.script;

import com.game.player.structs.Player;

/**
 * 名人堂脚本
 */
public interface ITopHallScript extends IRankScript {

    /**
     * 请求名人堂面板信息
     */
    void onReqTopHallRankPanel(Player player);

    /**
     * 名人堂排名刷新
     */
    void sortTopHallRank();

    /**
     * 零点名人堂阶段检查
     */
    void zeroCheckTopHallStage();

}
