package com.game.activity.script;

import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityLucky;
import com.game.backpack.structs.Item;
import com.game.player.structs.Player;

import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/4/9 15:25
 * @Auth ZUncle
 */
public interface IActivityLucky {

    /**
     * 增加幸运值
     * @param player
     * @param lucky
     */
    void incrLucky(Player player, ActivityLucky lucky);

    /**
     * 是否触发幸运值
     * @param player
     * @param lucky
     */
    boolean isTriggerLucky(Player player,  ActivityLucky lucky);

    /**
     * 清空幸运值
     * @param player
     * @param lucky
     */
    void cleanLucky(Player player,  ActivityLucky lucky, List<Item> items);

}
