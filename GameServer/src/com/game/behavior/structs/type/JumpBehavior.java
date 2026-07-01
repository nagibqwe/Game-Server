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
public class JumpBehavior extends BaseBehavior {

    private long start; //起跳时间
    private int stage; //跳跃阶段
    private float height; //跳跃高度
    private float sh; //垂直速度
    private float ss; //水平速度

    private Position startTarPos; //前段目标
    private boolean down = false;
    private Position endTarPos; //后段目标

    public JumpBehavior(IMapObject imo) {
        super(imo);
        SetState(BehaviorStatus.ACTIVITYSTATUS_INITIALIZED);
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getSh() {
        return sh;
    }

    public void setSh(float sh) {
        this.sh = sh;
    }

    public float getSs() {
        return ss;
    }

    public void setSs(float ss) {
        this.ss = ss;
    }

    public Position getStartTarPos() {
        return startTarPos;
    }

    public void setStartTarPos(Position startTarPos) {
        this.startTarPos = startTarPos;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public Position getEndTarPos() {
        return endTarPos;
    }

    public void setEndTarPos(Position endTarPos) {
        this.endTarPos = endTarPos;
    }

    @Override
    public boolean Cancel() {
        try {
            IBehavior ib = (IBehavior) Manager.scriptManager.GetScriptClass(ScriptEnum.JumpBehaviorCommonScript);
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
            IBehavior ib = (IBehavior) Manager.scriptManager.GetScriptClass(ScriptEnum.JumpBehaviorCommonScript);
            ib.action(this);

        } catch (Exception e) {
            SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            log.error(e, e);
        }

    }

    @Override
    public BehaviorType getType() {
        return BehaviorType.Jump;
    }
}
