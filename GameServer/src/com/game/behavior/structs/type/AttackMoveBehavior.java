/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class AttackMoveBehavior extends BaseBehavior {

    private int hitType; //受击类型
    private long start;
    private long end;
    private float speed;   //m/s
    private Position target;

    public AttackMoveBehavior(IMapObject imo) {
        super(imo);
        SetState(BehaviorStatus.ACTIVITYSTATUS_INITIALIZED);
    }

    @Override
    public boolean Cancel() {
        try {
            IBehavior ib = (IBehavior) Manager.scriptManager.GetScriptClass(ScriptEnum.AttackMoveBehaviorCommonScript);
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
            IBehavior ib = (IBehavior) Manager.scriptManager.GetScriptClass(ScriptEnum.AttackMoveBehaviorCommonScript);
            ib.action(this);

        } catch (Exception e) {
            SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            log.error(e, e);
        }

    }

    public int getHitType() {
        return hitType;
    }

    public void setHitType(int hitType) {
        this.hitType = hitType;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
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
        return BehaviorType.AttackMove;
    }

}
