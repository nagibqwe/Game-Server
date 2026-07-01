package com.game.map.script;

import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.structs.Fighter;
import game.core.script.IScript;

/**
 * 地图处理基类接口
 *
 * @author lw
 */
public interface IMapBaseScript extends IScript {
    /**
     * 地图创建初始化
     * @param mapObject
     */
    void onCreate(MapObject mapObject, Object... objects);

    /**
     * 是否满足进入条件
     *
     * 若不满足，实现脚本给出提示或错误日志
     * @param player
     * @param model     副本zoneId
     * @param level
     * @return          是否满足条件
     */
    boolean canEnterMap(Player player, int model, int level);

    /**
     * 进入副本地图接口
     * @param player
     * @param map
     * @param login
     */
    void onEnterMap(Player player, MapObject map, boolean login);

    /**
     * 离开副本地图接口
     * @param player
     * @param map
     * @param isQuit
     */
    void onQuitMap(Player player, MapObject map, boolean isQuit);

    /**
     * 伤害接口
     * @param mapObject
     * @param monster
     * @param damage
     * @param attacker
     */
    void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker);

    /**
     * 怪物死亡接口
     * @param map
     * @param monster
     * @param attacker
     */
    void onMonsterDie(MapObject map, Monster monster, Fighter attacker);

    /**
     * 怪物死亡后
     * @param map
     * @param monster
     * @param attacker
     */
    void onMonsterAfterDie(MapObject map, Monster monster, Fighter attacker);

    /**
     * 怪物脱离战斗
     * @param map
     * @param monster
     * @param attacker
     */
    void onLeaveBattle(MapObject map, Monster monster, Player attacker);

    /**
     * 玩家死亡接口
     * @param map
     * @param attacker
     * @param player
     */
    void onPlayerDie(MapObject map, Fighter attacker, Player player);


    /**
     * 定时执行的函数
     * @param map
     * @param method
     * @param params
     */
    void action(MapObject map, String method, Object[] params);

    /**
     * 删除地图调用接口
     * @param map
     */
    void removeMap(MapObject map);
}
