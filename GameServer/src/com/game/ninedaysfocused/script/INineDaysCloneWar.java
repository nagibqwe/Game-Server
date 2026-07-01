package com.game.ninedaysfocused.script;

import com.game.map.structs.MapObject;
import com.game.robot.struct.Robot;
import com.game.structs.Fighter;

/**
 * Created by 542 on 2019/7/23.
 */
public interface INineDaysCloneWar {


    void OnRobotDie(MapObject map, Robot robot, Fighter attacker);
}
