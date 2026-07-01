package com.game.statevip.script;

import com.game.player.structs.Player;

/**
 * @author admin
 */
public interface IStateVip {
    /**
     * 初始化境界
     * @param player
     */
    void initStateVip(Player player);

    /**
     * 请求境界任务列表
     * @param player
     */
    void reqStateVip(Player player);

    /**
     * 请求境界升级
     * @param player
     */
    void reqStateVipUp(Player player);

    /**
     * gm境界升级
     * @param player
     * @param lv
     * @return
     */
    boolean gmStateVipUp(Player player, int lv);

    /**
     * 推送境界任务完成
     * @param player
     * @param type
     */
    void operateStateVip(Player player, int type);
}


