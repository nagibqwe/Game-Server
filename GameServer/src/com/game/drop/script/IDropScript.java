package com.game.drop.script;

import com.game.backpack.structs.Item;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import game.core.script.IScript;

import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 */
public interface IDropScript extends IScript {

    /**
     * 怪物的死亡掉落
     *
     * @param monster   怪物实例
     * @param player    最后杀的玩家
     */
    void doDeadDrop(MapObject map,Monster monster, Player player);

    /**
     * 获取掉落物品列表，每次获取会增加玩家掉落次数
     * @param player
     * @param itemDropId
     * @return
     */
    List<List<Integer>> getItemDrops(Player player, int itemDropId);


    /**
     * 获取掉落物品列表，每次获取会增加玩家掉落次数
     * @param player
     * @param itemDropId
     * @return
     */
    List<List<Integer>> getItemDrops(Player player, int itemDropId,int isBind);

    /**
     * 掉落物品
     * @param dropID
     */
    List<List<Integer>> dropExecute(int dropID);

    /**
     * 掉落物品
     * @param player
     * @param dropID
     * @param monster
     * @param reason
     */
    void dropExecute(MapObject map, Player player, int dropID, Monster monster, int reason);

    /**
     * 掉落经验
     * @param player
     * @param monster
     * @param exp
     */
    void dropExp(Player player, Monster monster, long exp);

    /**
     * 怪物特殊掉落奖励
     * @param monster 被击杀的怪物
     * @param player 击杀者
     * @param type 活动类型
     * @param record 是否需要记录次数
     * @param replaceDropId
     */
    HashMap<Long,Player> specialDropReward(Monster monster, Player player, int type, boolean record, int replaceDropId);

    /**
     * 同步掉落物品归属
     */
    void syncDropItemToRoundPlayer(Player player, Monster monster, List<Item> items, boolean filter);
}
