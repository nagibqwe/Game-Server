package com.game.robot.script;

import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import com.game.robot.struct.Robot;
import com.game.structs.Fighter;

/**
 * Created by huhu on 2017/8/29.
 */
public interface IRobotFightScript {

    /**
     *
     * @param map
     * @param robot
     * @param attacker
     */
    void OnRobotDie(MapObject map, Robot robot, Fighter attacker);
}
