package com.game.behavior.structs.type;

import com.game.behavior.structs.BaseBehavior;
import com.game.behavior.structs.BehaviorStatus;
import com.game.behavior.structs.BehaviorType;
import com.game.behavior.structs.IBehavior;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.map.IMapObject;
import game.core.map.Position;

/**
 * @author zenghai
 */
public class SkillMoveBehavior extends BaseBehavior {

    private float speed;
    private Position target;

    public SkillMoveBehavior(IMapObject imo) {
        super(imo);
        SetState(BehaviorStatus.ACTIVITYSTATUS_INITIALIZED);
    }

    @Override
    public boolean Cancel() {
        try {
            IBehavior ib = (IBehavior) Manager.scriptManager.GetScriptClass(ScriptEnum.SkillMoveBehaviorCommonScript);
            return ib.Cancel(this);

        } catch (Exception e) {
            SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            log.error(e, e);
        }
        return true;
    }

    @Override
    public void action() {
        try {
            IBehavior ib = (IBehavior) Manager.scriptManager.GetScriptClass(ScriptEnum.SkillMoveBehaviorCommonScript);
            ib.action(this);

        } catch (Exception e) {
            SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            log.error(e, e);
        }

    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Position getTarget() {
        return target;
    }

    public void setTarget(Position target) {
        this.target = target;
    }

    @Override
    public BehaviorType getType() {
        return BehaviorType.SkillMove;
    }
}
