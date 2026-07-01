package com.game.jjc.script;

import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import com.game.robot.struct.Robot;
import com.game.structs.Fighter;
import game.message.JJCMessage;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public interface IJJCCloneScript extends IMapBaseScript {

    void OnRobotDie(MapObject map, Robot robot, Fighter attacker);

    void OnReqJJCexit(Player player, JJCMessage.ReqJJCexit mess);
    /**
     * 继续在jjc中挑战，不退出当前jjc副本
     * */
    void keepFighting(Player player, MapObject map);
    /**
     * 处理秒杀的情况
     * */
    void onRobotDieBySecondKill(int mapModelId, Robot robot, Player player);


    /**
     * 一键扫荡
     * @param player
     */
    void OneKeySweep(Player player,int count);

}
