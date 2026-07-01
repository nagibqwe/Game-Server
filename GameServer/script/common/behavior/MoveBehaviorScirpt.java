/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.behavior;

import com.game.behavior.structs.BaseBehavior;
import com.game.behavior.structs.BehaviorStatus;
import com.game.behavior.structs.IBehavior;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.SkillMagic;
import com.game.structs.Entity;
import com.game.structs.EntityState;
import com.game.utils.Utils;
import com.game.yed.YedMgr;
import game.core.map.IMapObject;
import game.core.map.Position;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author zenghai
 */
public class MoveBehaviorScirpt implements IScript, IBehavior {

    protected Logger log = LogManager.getLogger("MoveBehaviorScirpt");

    @Override
    public int getId() {
        return ScriptEnum.MoveBehaviorCommonScirpt;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public boolean Cancel(BaseBehavior behavior) {
        IMapObject owner = behavior.getOwner();
        if (owner == null) {
            return behavior.Over();
        }
        if (!(owner instanceof Entity)) {
            return behavior.Over();
        }
        
        if (owner instanceof Monster) {
            Monster monster = (Monster) owner;
            if (monster.isTdMonster()) {
                log.error(monster.nameIdString() + " ==> TD怪取消了移动行为！", new NullPointerException());
            }
            monster.setDoStep(0);
        }
        Entity mover = (Entity) owner;
        mover.clearRoads();
        mover.removeSate(EntityState.Move);
        mover.addState(EntityState.Stand);
        behavior.Over();
//        if (mover instanceof Player) {
//            log.info("移动 behavior cancel mover={}", mover);
//        }
        return true;
    }

    @Override
    public void action(BaseBehavior behavior) {

        IMapObject owner = behavior.getOwner();
        if (owner == null) {
            return;
        }
        if (!(owner instanceof Entity)) {
            return;
        }
        Entity mover = (Entity) owner;

        if (mover.getRoads().isEmpty()) {
            mover.removeSate(EntityState.Move);
            mover.addState(EntityState.Stand);
            behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            return;
        }

        if (behavior.getOffset() <= 0) {
            return;
        }

        if (Manager.buffManager.deal().haveDing(mover)) {
            
            return;
        }

        //晕的时候，不执行事件
        if (Manager.buffManager.deal().haveDizziness(mover)) {
            return;
        }

        float speed = mover.gainFinalMoveSpeed() / 100.0f;

        if (speed <= 0) {
            mover.clearRoads();
            return;
        }

        float moveDis = speed * behavior.getOffset() / 1000f;

        MapObject map = Manager.mapManager.getMap(owner.gainMapId());

        while (!mover.getRoads().isEmpty()) {
            Position pos = mover.getRoads().get(0);
//            if (mover instanceof Player) {
//                log.info("移动 behavior pos={} mover={}", pos, mover);
//            }

            float dis = Utils.getDistance(pos, mover.gainCurPos());

            if (moveDis > dis) {
                mover.changeCurPos(pos, true);

                moveDis -= dis;
                mover.getRoads().remove(0);
                continue;
            }
            Position dirPos = Utils.getDirPos(mover.gainCurPos(), pos, moveDis);

            //如果是飞行移动
            if (EntityState.Fly.compare(mover.getState())) {
                if (Utils.isCanFly(map, mover.gainCurPos(), dirPos)) {
                    mover.changeCurPos(dirPos, true);
                } else {
                    mover.changeCurPos(mover.gainCurPos(), true);
                    behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
                    Manager.mapManager.synStopMove((Player) mover, true);
                }
                return;
            }

            if (mover.isNeedCheckCanMove() && Utils.isCanMove(map, mover.gainCurPos(), dirPos)) {
                mover.changeCurPos(dirPos, true);
                if (mover instanceof SkillMagic) {
                    YedMgr.getInstance().getAiLogger().info(String.format("debug move isCanMove Magic:%d speed:%.2f curpos:%s", mover.getId(), speed, mover.gainCurPos()));
                }
                return;
            }
            if (mover instanceof Pet) {
                mover.changeCurPos(dirPos, true);
                return;
            }
            if (mover instanceof Robot) {
                mover.changeCurPos(dirPos, true);
                return;
            }
            if (mover instanceof SkillMagic) {
                mover.changeCurPos(dirPos, true);
                return;
            }
            mover.changeCurPos(mover.gainCurPos(), true);
            mover.clearRoads();
            if (mover instanceof Player) {
                Manager.mapManager.synStopMove((Player) mover, true);
            }
            behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);

        }
    }

}
