/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.robot.manager;

import com.game.manager.Manager;
import com.game.robot.script.IRobotAiScript;
import com.game.robot.script.IRobotScript;
import com.game.script.structs.ScriptEnum;
import com.game.yed.scripts.YedMethodScript;
import game.core.script.IScript;

/**
 *
 * @author zenghai
 */
public class RobotManager {

    public static final String ROBOT_HELP_AI = "HelpRobot";
//    private final ConcurrentHashMap<Long, Robot> robots = new ConcurrentHashMap<>();

    public IRobotAiScript ai() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.RobotAiCommonScript);
        if (is instanceof IRobotAiScript) {
            return (IRobotAiScript) is;
        }
        return null;
    }

    public YedMethodScript yedAi() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.RobotAiCommonScript);
        if (is instanceof YedMethodScript) {
            return (YedMethodScript) is;
        }
        return null;
    }

    private enum Singleton {

        INSTANCE;
        RobotManager manager;

        Singleton() {
            this.manager = new RobotManager();
        }

        RobotManager getProcessor() {
            return manager;
        }
    }

    public static RobotManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
//
//    public ConcurrentHashMap<Long, Robot> getRobots() {
//        return robots;
//    }

    public IRobotScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.RobotBaseScript);
        if (is instanceof IRobotScript) {
            return (IRobotScript) is;
        }
        return null;
    }

}
