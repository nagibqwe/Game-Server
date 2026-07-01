package com.game.robot.script;

import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import com.game.robot.struct.Robot;

/**
 * Created by huhu on 2017/8/29.
 */
public interface IRobotAiScript {
    /**
     * ai循环调用
     * @param op
     */
    void OnRobotAi(Robot op, Player maker, MapObject curmap);
}
