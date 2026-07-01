/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.behavior;

import com.game.behavior.structs.BaseBehavior;
import com.game.behavior.structs.BehaviorStatus;
import com.game.behavior.structs.IBehavior;
import com.game.behavior.structs.type.SkillMoveBehavior;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Entity;
import com.game.utils.Utils;
import game.core.map.IMapObject;
import game.core.map.Position;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author zenghai
 */
public class SkillMoveBehaviorScript implements IScript, IBehavior {

    protected Logger log = LogManager.getLogger("BaseBehavior");

    @Override
    public int getId() {
        return ScriptEnum.SkillMoveBehaviorCommonScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public boolean Cancel(BaseBehavior behavior) {
        SkillMoveBehavior mb = (SkillMoveBehavior) behavior;
        if (!behavior.IsOver()) {
            //解除技能僵直
            IMapObject owner = behavior.getOwner();
            Entity mover = (Entity) owner;
            if (mb.getTarget() != null) {
                mover.changeCurPos(mb.getTarget(), true);
            }
        }
        behavior.Over();
        mb.setTarget(null);
        return true;
    }

    @Override
    public void action(BaseBehavior behavior) {

        IMapObject owner = behavior.getOwner();
        if (owner == null) {
            behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            return;
        }
        if (!(owner instanceof Entity)) {
            behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            return;
        }

        if (!(behavior instanceof SkillMoveBehavior)) {
            behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            return;
        }

        SkillMoveBehavior amb = (SkillMoveBehavior) behavior;

        Entity mover = (Entity) owner;

        if (amb.getTarget() == null) {
            behavior.Over();//行为结束
            return;
        }

        if (behavior.getOffset() <= 0) {
            behavior.Over();//行为结束
            return;
        }

        float moveDis = amb.getSpeed() * behavior.getOffset() / 1000f;

        float dis = Utils.getDistance(amb.getTarget(), mover.gainCurPos());

        if (moveDis > dis) {
            mover.changeCurPos(amb.getTarget(), true);
            log.info("[" + mover.getName() + "技能位移over=" + amb.getTarget());
            amb.setTarget(null);
            behavior.Over();//行为结束
        } else {
            Position dirPos = Utils.getDirPos(mover.gainCurPos(), amb.getTarget(), moveDis);
            mover.changeCurPos(dirPos, true);
            log.info("[" + mover.getName() + "]技能位移 mind=" + dirPos + "cur=" + mover.gainCurPos() + "target=" + amb.getTarget());
        }
    }

}
