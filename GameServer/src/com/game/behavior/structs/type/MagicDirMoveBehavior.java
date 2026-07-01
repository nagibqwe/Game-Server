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
import com.game.structs.Fighter;
import game.core.map.IMapObject;
import game.core.map.Position;

/**
 * 召唤物的目标追踪移动事件
 * @author soko
 * @date 2018-9-29
 */
public class MagicDirMoveBehavior extends MagicMoveBehavior{

    //目标
    private Fighter defer;

    //目标位置是否已经改变的判定坐标
    private Position targetPos= new Position(0,0);
    //技能效果
    private String skillVisalName= "";

    public MagicDirMoveBehavior(IMapObject imo) {
        super(imo);
        SetState(BehaviorStatus.ACTIVITYSTATUS_INITIALIZED);
    }

    @Override
    public boolean Cancel() {
        try {
            IBehavior ib = (IBehavior) Manager.scriptManager.GetScriptClass(ScriptEnum.MagicBehaviorCommonScript);
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
            IBehavior ib = (IBehavior) Manager.scriptManager.GetScriptClass(ScriptEnum.MagicBehaviorCommonScript);
            ib.action(this);

        } catch (Exception e) {
            SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            log.error(e, e);
        }

    }

    @Override
    public BehaviorType getType() {
        return BehaviorType.MagicDirMoveEvent;
    }

    public Fighter getDefer() {
        return defer;
    }

    public void setDefer(Fighter defer) {
        this.defer = defer;
    }

    public Position getTargetPos() {
        return targetPos;
    }

    public void setXy(int x, int y){
        targetPos.setX(x);
        targetPos.setY(y);
    }

    public String getSkillVisalName() {
        return skillVisalName;
    }

    public void setSkillVisalName(String skillVisalName) {
        this.skillVisalName = skillVisalName;
    }
}
