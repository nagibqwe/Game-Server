package com.game.robot.timer;

import com.game.behavior.structs.BaseBehavior;
import com.game.fight.script.IFightManagerScript;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.pet.structs.Pet;
import com.game.robot.script.IRobotScript;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.SkillLockTrajectory;
import com.game.skill.structs.SkillMagic;
import game.core.timer.TimerEvent;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;

/**
 * @author zenghai
 */
public class RobotBehaviorTimer extends TimerEvent {

    private static final Logger log = LogManager.getLogger(RobotBehaviorTimer.class);
    private final MapObject map;
    private long beginTime = 0;

    public RobotBehaviorTimer(MapObject map) {
        super(-1, 0,150);
        this.map = map;
        beginTime = TimeUtils.Time();
    }

    @Override
    public void action() {
        try {
            long offset = System.currentTimeMillis() - beginTime;
            beginTime = System.currentTimeMillis();
            Iterator<Robot> iter = map.getRobots().values().iterator();

            //开始攻击
            IFightManagerScript is = (IFightManagerScript) Manager.scriptManager.GetScriptClass(ScriptEnum.FightManagerBaseScript);

            IRobotScript iabs = (IRobotScript) Manager.scriptManager.GetScriptClass(ScriptEnum.RobotBaseScript);

            Robot robot;
            while (iter.hasNext()) {
                robot = iter.next();
                if (null == robot) {
                    iter.remove();
                    log.error("player is null!");
                    continue;
                }
                // huhu 召唤物的
                for (long magicid : robot.getMagics()) {
                    SkillMagic magic = map.getMagic(magicid);
                    if (magic != null) {
                        magic.UpdateBehavior(offset);
                    }
                }

                Iterator<SkillLockTrajectory> stIter = robot.getLockTrajectories().iterator();
                while(stIter.hasNext()){
                    SkillLockTrajectory st = stIter.next();
                    if( is.doSkillLockTrajectoryEndToDelete(robot, st)){
                        stIter.remove();
                    }
                }

                Pet pet = robot.getPet();
                if( pet != null){
                    Iterator<SkillLockTrajectory> petstIter = pet.getLockTrajectories().iterator();
                    while(petstIter.hasNext()){
                        SkillLockTrajectory st = petstIter.next();
                        if( is.doSkillLockTrajectoryEndToDelete(pet, st)){
                            petstIter.remove();
                        }
                    }
                }

                try {
                    is.onFightSkillEvent(robot);

                    if (robot.getPet() != null) {
                        is.onFightSkillEvent(robot.getPet());
                    }
                } catch (Exception e) {
                    log.error(e, e);
                }

                if (iabs != null) {
                    iabs.tick(robot);
                }


                if (robot.BehaviorList().isEmpty()) {
                    continue;
                }

                Iterator<BaseBehavior> it = robot.BehaviorList().iterator();
                while (it.hasNext()) {
                    BaseBehavior bb = it.next();

                    if (bb == null) {
                        log.error("行为数据为空， 则清除！");
                        it.remove();
                        continue;
                    }

                    bb.Update(offset);
                    if (bb.IsOver()) {
                        it.remove();
                    }
                }
            }
        } catch (Exception e) {
            log.error("执行怪物行为的时候， 地出错了！", e);
        }

    }

}
