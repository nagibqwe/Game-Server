/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.skill.config.event;

import com.game.skill.config.SkillEvent;
import com.game.skill.structs.SkillDefine;
import com.game.utils.Shape;
import com.game.utils.Symbol;

/**
 *
 * @author zenghai
 */
public class PlayPushEvent extends SkillEvent {

    public float MoveDis = 0f;
    public float MoveTime = 0f;
    //public AnimationCurve MoveCurve = null;

    public int HitVfx = 0; //受击特效
    public int HitSlot = 4; //受击特效插槽
    public float HitVfxScale = 1f;//缩放

    @Override
    public void split(String param) {
        String[] params = param.split(Symbol.FENHAO_REG);

        MoveDis = Float.parseFloat(params[0]);
        MoveTime = Float.parseFloat(params[1]);

        HitVfx = Integer.parseInt(params[3]);
        HitSlot = Integer.parseInt(params[4]);
        HitVfxScale = Float.parseFloat(params[5]);
    }

    public float getMoveDis() {
        return MoveDis;
    }

    public void setMoveDis(float MoveDis) {
        this.MoveDis = MoveDis;
    }

    public float getMoveTime() {
        return MoveTime;
    }

    public void setMoveTime(float MoveTime) {
        this.MoveTime = MoveTime;
    }

    public int getHitVfx() {
        return HitVfx;
    }

    public void setHitVfx(int HitVfx) {
        this.HitVfx = HitVfx;
    }

    public int getHitSlot() {
        return HitSlot;
    }

    public void setHitSlot(int HitSlot) {
        this.HitSlot = HitSlot;
    }

    public float getHitVfxScale() {
        return HitVfxScale;
    }

    public void setHitVfxScale(float HitVfxScale) {
        this.HitVfxScale = HitVfxScale;
    }

    @Override
    public  float getHitDis(int type) {
        return 0f;
    }
    
    @Override
    public float getSpeed(int type) {
        return 1f;
    }

    @Override
    public int getRunTime(int type) {
        return 1;
    }

    @Override
    public int getHitType() {
        return SkillDefine.SkillAttackMoveType_None;
    }
    
    @Override
    public Shape getShape() {
        return new Shape();
    }

    @Override
    public int getUniqueID() {
        return 0;
    }

}
