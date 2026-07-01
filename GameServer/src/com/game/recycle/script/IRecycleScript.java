package com.game.recycle.script;

import com.game.backpack.structs.Equip;
import com.game.backpack.structs.Item;
import com.game.player.structs.Player;
import game.message.RecycleMessage;

import java.util.List;

/**
 * 回收炉接口
 */
public interface IRecycleScript {
    /**
     * 回收物品
     * @param player
     * @param messInfo
     */
    void onReqRecycle(Player player, RecycleMessage.ReqRecycle messInfo);

    /**
     * 设置自动回收标识(自动分解金色1星以下装备)
     * @param player
     * @param messInfo
     */
    void onReqSetAuto(Player player, RecycleMessage.ReqSetAuto messInfo);

    /**
     * 设置自动熔炼开关
     * @param player
     * @param isOpen true开启，false关闭
     */
    void setAutoRecycle(Player player, boolean isOpen);

    void autoRecycle(Player player, Equip item);

    void online(Player player);
}
