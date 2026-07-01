package com.game.copymap.scripts;

import com.game.player.structs.Player;
import com.game.structs.Gather;

/**
 * 采集物的接口
 *
 * @author
 */
public interface ICopyGatherScript {

    /**
     * 采集前的调用接口
     * @param player
     * @param gather
     * @return
     */
    boolean onBeginGather(Player player, Gather gather);

    /**
     * 采集完成调用接口
     * @param player
     * @param gather
     */
    void onGather(Player player, Gather gather);

    /**
     * 取消采集
     * @param player
     * @param gather
     */
    void onOutGather(Player player, Gather gather);

}
