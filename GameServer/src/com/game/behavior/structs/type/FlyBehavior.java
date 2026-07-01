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

/**
 * @author zenghai
 */
public class FlyBehavior extends BaseBehavior {

    public FlyBehavior(IMapObject imo) {
        super(imo);
        SetState(BehaviorStatus.ACTIVITYSTATUS_INITIALIZED);
    }

    @Override
    public boolean Cancel() {
        try {
            IBehavior ib = (IBehavior) Manager.scriptManager.GetScriptClass(ScriptEnum.FlyBehaviorCommonScript);
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
            IBehavior ib = (IBehavior) Manager.scriptManager.GetScriptClass(ScriptEnum.FlyBehaviorCommonScript);
            ib.action(this);

        } catch (Exception e) {
            SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            log.error(e, e);
        }

    }

    @Override
    public BehaviorType getType() {
        return BehaviorType.Fly;
    }
}
