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
 * 召唤物的直线移动事件
 * @author soko
 * @date 2018-9-29
 */
public class MagicMoveBehavior extends BaseBehavior {

    private long beginTime = 0;
    private float aSpeed = 0f;
    private float speed  = 0f;
    //上一次移动的距离
    private float lastDis = 0f;

    private Position dirPos = null;


    public MagicMoveBehavior(IMapObject imo) {
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
        return BehaviorType.MagicMoveEvent;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public float getaSpeed() {
        return aSpeed;
    }

    public void setaSpeed(float aSpeed) {
        this.aSpeed = aSpeed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getLastDis() {
        return lastDis;
    }

    public void setLastDis(float lastDis) {
        this.lastDis = lastDis;
    }

    public Position getDirPos() {
        return dirPos;
    }

    public void setDirPos(Position dirPos) {
        this.dirPos = dirPos;
    }
}
