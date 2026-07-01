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
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Entity;
import com.game.structs.EntityState;
import com.game.utils.Utils;
import game.core.map.IMapObject;
import game.core.map.Position;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author zenghai
 */
public class DirMoveBehaviorScript implements IScript, IBehavior {

    protected Logger log = LogManager.getLogger("BaseBehavior");

    @Override
    public int getId() {
        return ScriptEnum.DirMoveBehaviorCommonScript;
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
        if (!(owner instanceof Player)) {
            return behavior.Over();
        }

        Player mover = (Player) owner;
        mover.setMoveDir(null);
        mover.removeSate(EntityState.Move);
        mover.addState(EntityState.Stand);
        behavior.Over();
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
        Player mover = (Player) owner;

        if (mover.getMoveDir() == null) {
            mover.removeSate(EntityState.Move);
            mover.addState(EntityState.Stand);
            behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            //LOGGER.error("[" + mover.getName() + "]方向移动行为执行完毕");
            return;
        }

        if (behavior.getOffset() <= 0) {
            return;
        }

        float speed = mover.getAttribute().gainFinalMoveSpeed() / 100.0f;

        float movedis = speed * behavior.getOffset() / 1000f;

        Position target = Utils.getPosByDir(mover.gainCurPos(), mover.getMoveDir(), movedis);

        MapObject map = Manager.mapManager.getMap(owner.gainMapId());

        if (EntityState.Fly.compare(mover.getState())) {
            if (Utils.isCanFly(map, mover.gainCurPos(), target)) {
                mover.changeCurPos(target, true);
            } else {
                mover.changeCurPos(mover.gainCurPos(), true);
                behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
                Manager.mapManager.synStopMove(mover, true);
            }
            return;
        }
        if (Utils.isCanMove(map, owner.gainCurPos(), target)) {
            mover.changeCurPos(target, true);
            //LOGGER.error("[" + mover.getName() + "]方向移动行为执行" + target);
        } else {
            mover.changeCurPos(mover.gainCurPos(), true);
            behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            Manager.mapManager.synStopMove(mover, true);
            //LOGGER.error("[" + mover.getName() + "]方向移动行为执行" + target);
        }

    }

}
