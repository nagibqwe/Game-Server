package com.game.universe.script;

import com.game.command.structs.CommandData;
import com.game.player.structs.Player;
import game.message.MSG_UniverseMessage;

public interface IUniverseWarScript {
    /**
     * 公共服通知战斗服同步太虚战场面板数据
     */
    void P2FReqUniverseWarPanel(MSG_UniverseMessage.P2FReqUniverseWarPanel messInfo);
    /**
     * 战斗服通知游戏服同步太虚战场面板数据
     */
    void F2GResUniverseWarPanel(MSG_UniverseMessage.F2GResUniverseWarPanel messInfo);
    /**
     * 公共服通知战斗服打开阻挡
     */
    void P2FOpenBlock(MSG_UniverseMessage.P2FOpenBlock messInfo);

    /**
     * 请求关注怪物
     * @param player
     * @param messInfo
     */
    void onReqCareMonster(Player player, MSG_UniverseMessage.ReqCareMonster messInfo);

    /**
     * 请求单服个人伤害排名
     * @param player
     * @param messInfo
     */
    void onReqDamageRank(Player player, MSG_UniverseMessage.ReqDamageRank messInfo);

    /**
     * 加载怪物信息数据
     */
    void load();

    /**
     * boss重生处理
     */
    void calcBossBirth();

    /**
     * 通知玩家关注的boss刷新
     * @param modelId boss模型ID
     */
    void sendCareMonsterRefreshTip(int modelId);

    /**
     * 检查怒气重置
     */
    void checkAnger(Player player);

    void synAnger(Player player);

    void updateCmdBuff(Player player, CommandData cmd);

    /**
     * 是否可以操作称号
     * @param player
     * @return
     */
    boolean canOptTitle(Player player);
}
