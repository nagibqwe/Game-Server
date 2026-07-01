/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.behavior;

import com.game.behavior.structs.BaseBehavior;
import com.game.behavior.structs.BehaviorStatus;
import com.game.behavior.structs.IBehavior;
import com.game.behavior.structs.type.AttackMoveBehavior;
import com.game.fight.structs.FightEnum;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Entity;
import com.game.utils.Utils;
import game.core.map.IMapObject;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author zenghai
 */
public class AttackMoveBehaviorScript implements IScript, IBehavior {

    protected Logger log = LogManager.getLogger("BaseBehavior");

    @Override
    public int getId() {
        return ScriptEnum.AttackMoveBehaviorCommonScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public boolean Cancel(BaseBehavior behavior) {
        AttackMoveBehavior mb = (AttackMoveBehavior) behavior;
        mb.setTarget(null);

        if (!behavior.IsOver()) {
            //解除技能僵直
            IMapObject owner = behavior.getOwner();
            Entity mover = (Entity) owner;

            mover.removeFightState(FightEnum.SkillFreeze);
        }
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

        if (!(behavior instanceof AttackMoveBehavior)) {
            behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            return;
        }

        AttackMoveBehavior mb = (AttackMoveBehavior) behavior;

        Entity mover = (Entity) owner;
        long curTime = TimeUtils.Time();
        if (curTime > mb.getEnd()) {
            if (!behavior.IsOver()) {
                //解除技能僵直
                mover.removeFightState(FightEnum.SkillFreeze);
            }
            behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            //LOGGER.error("时间过了：" + mb.getTarget());
        }

        if (mb.getTarget() == null) {
//            behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
//            LOGGER.error("[" + mover.getName() + "]移动行为执行完毕 server= " + mover.gainCurPos());
            return;
        }

        if (behavior.getOffset() <= 0) {
            return;
        }

        if (mb.getSpeed() <= 0) {
            //原地飞
            return;
        }

        float moveDis = mb.getSpeed() * behavior.getOffset() / 1000f;

        float dis = Utils.getDistance(mb.getTarget(), mover.gainCurPos());

        if (moveDis > dis) {
            mover.changeCurPos(mb.getTarget(), true);
           // LOGGER.info("[" + mover.getName() + "]受击移动执行 over=" + mb.getTarget());
            mb.setTarget(null);
        } else {
            Position dirPos = Utils.getDirPos(mover.gainCurPos(), mb.getTarget(), moveDis);
            mover.changeCurPos(dirPos, true);
           // LOGGER.info("[" + mover.getName() + "]受击移动行为执行 mind=" + dirPos + "cur=" + mover.gainCurPos() + "target=" + mb.getTarget());
        }

    }

}
