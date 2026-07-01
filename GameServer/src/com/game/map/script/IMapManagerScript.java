package com.game.map.script;

import com.data.bean.Cfg_Mapsetting_Bean;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.robot.struct.Robot;
import game.core.map.IMapObject;
import game.core.map.Position;

/**
 * @author lw
 * @doc 地图接口类
 */
public interface IMapManagerScript {
    /**
     * 创建副本
     * @param cloneId
     * @param level
     * @param ownId
     * @param objects
     */
    MapObject createCopyMap(int cloneId, int level, long ownId, Object... objects);

    /**
     * 创建野图
     */
    MapObject createWorldMap(Cfg_Mapsetting_Bean bean, int line);

    /**
     * 实体进入地图(除玩家)
     * @param play
     */
    void onEnterMap(IMapObject play);

    /**
     * 玩家进入地图
     * @param player
     * @param map
     * @param pos
     */
    void onEnterMap(Player player, MapObject map, Position pos);

    /**
     * 退出地图(包括地图)
     * @param map
     * @param play
     * @param isBroadcast
     */
    void onQuitMap(MapObject map, IMapObject play, boolean isBroadcast);

    /**
     * 离开跨服地图
     * @param player
     */
    void onCrossOutMap(Player player);

    //改变坐标
    void OnChangePos(Player player, Position oldPos);

    void OnChangePos(Monster monster, Position oldPos);

    void OnChangePos(Robot robot, Position oldPos);

    /**
     * 清理玩家
     * @param map
     * @param player
     * @return
     */
    boolean clearPlayer(MapObject map, Player player);

    void SynMeToOther(Player player);

    /**
     * 销毁地图
     * @param mapObject
     */
    void deleteMap(MapObject mapObject);

    /**
     * 新增地图
     * @param mapObject
     */
    void createMap(MapObject mapObject, Object[] objects);

    /**
     * 切换地图
     * @param player
     * @param mapId
     * @param modelId
     * @param line
     * @param position
     * @param type
     * @praam isLogin
     */
    void changeMap(Player player, long mapId, int modelId, int line, Position position, int type, boolean isLogin);

    /**
     * 跳出三界
     * @param player
     */
    void changeSpaceMap(Player player);


}
