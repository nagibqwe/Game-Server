package com.game.skill.config.event;

import com.game.skill.config.SkillEvent;
import com.game.skill.structs.SkillDefine;
import com.game.utils.Shape;
import com.game.utils.Symbol;

public class FindTargetHitEvent extends SkillEvent {

    public int AreaType = SkillDefine.SkillShape_Rectangle;
    public float RectWidth = 1f;
    public float RectHeight = 1f;
    public float SectorAngle = 100f;
    public float SectorRadius = 1f;
    public float RoundRadius = 1f;
    public int MaxTargetCount = 10;

    private int hitType = SkillDefine.SkillAttackMoveType_None;
    private int hitdir = SkillDefine.SkillHitAway0; //击退类型0,真实方向击退，1同方向击退

    public int HitTime = 0;   //受击僵直时间

    public float BackDis = 0;//击退的距离
    public int BackTime = 0;//击退的时间

    public float FlyDis = 0f;   //击飞，追击击飞的距离
    public float FlyHeight = 0; //击飞，追击击飞的高度
    public int FlyTime = 0;   //击飞，追击击飞的时间
    public int LieTime = 0;   //击飞，追击击飞的躺地时间

    public float GrabPos = 0; //抓取目标点离使用者面向方向的距离
    public float GrabDis = 0; //抓取到离目标点的距离
    public int GrabTime = 0; //抓取时间

    public int HitVfx = 0; //受击特效
    public int HitSlot = 4; //受击特效插槽
    public float HitVfxScale = 1f;//缩放
    public String HitAnim = ""; //受击动作

//    public AnimationCurve HitFlyHeightCurve = null;       //飞行高度变化曲线，用以决定高度变化
//    public AnimationCurve TimeCurve = null;   //飞行距离变化曲线，用以决定距离变化
//
//    public Vector2 FixPosition = Vector2.zero; //击退类型固定位置的值
    private float attackDis = 1f; //攻击范围
    
    private final Shape shape = new Shape();

    @Override
    public void split(String param) {
        String[] params = param.split(Symbol.FENHAO_REG);
        AreaType = Integer.parseInt(params[0]);
        RectWidth = Float.parseFloat(params[1]);
        RectHeight = Float.parseFloat(params[2]);
        SectorAngle = Float.parseFloat(params[3]);
        SectorRadius = Float.parseFloat(params[4]);
        RoundRadius = Float.parseFloat(params[5]);
        MaxTargetCount = Integer.parseInt(params[6]);
        hitType = Integer.parseInt(params[7]);
        hitdir = Integer.parseInt(params[8]);//击退类型0,真是方向击退，1同方向击退

        HitTime = (int) (Float.parseFloat(params[9]) * 1000);
        BackDis = Float.parseFloat(params[10]);
        BackTime = (int) (Float.parseFloat(params[11]) * 1000);
        FlyDis = Float.parseFloat(params[12]);
        FlyHeight = Float.parseFloat(params[13]);
        FlyTime = (int) (Float.parseFloat(params[14]) * 1000);
        LieTime = (int) (Float.parseFloat(params[15]) * 1000);
        GrabPos = Float.parseFloat(params[16]);
        GrabDis = Float.parseFloat(params[17]);
        GrabTime = (int) (Float.parseFloat(params[18]) * 1000);

        HitVfx = Integer.parseInt(params[19]);
        HitSlot = Integer.parseInt(params[20]);
        HitVfxScale = Float.parseFloat(params[21]);

        shape.setType(AreaType);
        switch (AreaType) {
            case SkillDefine.SkillShape_Rectangle:
                attackDis = RectHeight > RectWidth ? RectHeight : RectWidth;
                shape.setHeight(RectHeight);
                shape.setWidth(RectWidth);
                break;
            case SkillDefine.SkillShape_Sector:
                attackDis = SectorRadius;
                shape.setAngle(SectorAngle);
                shape.setR(SectorRadius);
                break;
            case SkillDefine.SkillShape_Circle:
                attackDis = RoundRadius;
                shape.setR(RoundRadius);
                break;
            default:
        }
    }

    @Override
    public  float getHitDis(int type) {
        switch (type) {
            case SkillDefine.SkillAttackMoveType_Back:
                return BackDis;
            case SkillDefine.SkillAttackMoveType_Fly:
                return FlyDis;
            case SkillDefine.SkillAttackMoveType_Catch:
                return GrabDis;
            default:
                return 0f;
        }
    }
    
    @Override
    public float getSpeed(int type) {
        switch (type) {
            case SkillDefine.SkillAttackMoveType_Back:
                return BackDis / BackTime * 1000f;
            case SkillDefine.SkillAttackMoveType_Fly:
                return FlyDis / FlyTime * 1000f;
            case SkillDefine.SkillAttackMoveType_Catch:
                return GrabDis / GrabTime * 1000f;
            default:
                return 1f;
        }
    }

    //获取事件执行时间
    @Override
    public int getRunTime(int type) {
        switch (type) {
            case SkillDefine.SkillAttackMoveType_Back:
                return BackTime;
            case SkillDefine.SkillAttackMoveType_Fly:
                return FlyTime + LieTime;
            case SkillDefine.SkillAttackMoveType_Catch:
                return GrabTime;
            case SkillDefine.SkillAttackMoveType_AttackHit:
                return HitTime;
            default:
                return 1;
        }
    }

    public float getAttackDis() {
        return attackDis;
    }

    public void setAttackDis(float attackDis) {
        this.attackDis = attackDis;
    }

    public int getAreaType() {
        return AreaType;
    }

    public void setAreaType(int AreaType) {
        this.AreaType = AreaType;
    }

    public float getRectWidth() {
        return RectWidth;
    }

    public void setRectWidth(float RectWidth) {
        this.RectWidth = RectWidth;
    }

    public float getRectHeight() {
        return RectHeight;
    }

    public void setRectHeight(float RectHeight) {
        this.RectHeight = RectHeight;
    }

    public float getSectorAngle() {
        return SectorAngle;
    }

    public void setSectorAngle(float SectorAngle) {
        this.SectorAngle = SectorAngle;
    }

    public float getSectorRadius() {
        return SectorRadius;
    }

    public void setSectorRadius(float SectorRadius) {
        this.SectorRadius = SectorRadius;
    }

    public float getRoundRadius() {
        return RoundRadius;
    }

    public void setRoundRadius(float RoundRadius) {
        this.RoundRadius = RoundRadius;
    }

    public int getMaxTargetCount() {
        return MaxTargetCount;
    }

    public void setMaxTargetCount(int MaxTargetCount) {
        this.MaxTargetCount = MaxTargetCount;
    }

    @Override
    public int getHitType() {
        return hitType;
    }

    public void setHitType(int hitType) {
        this.hitType = hitType;
    }

    public int getHitdir() {
        return hitdir;
    }

    public void setHitdir(int hitdir) {
        this.hitdir = hitdir;
    }

    public int getHitTime() {
        return HitTime;
    }

    public void setHitTime(int HitTime) {
        this.HitTime = HitTime;
    }

    public float getBackDis() {
        return BackDis;
    }

    public void setBackDis(float BackDis) {
        this.BackDis = BackDis;
    }

    public int getBackTime() {
        return BackTime;
    }

    public void setBackTime(int BackTime) {
        this.BackTime = BackTime;
    }

    public float getFlyDis() {
        return FlyDis;
    }

    public void setFlyDis(float FlyDis) {
        this.FlyDis = FlyDis;
    }

    public float getFlyHeight() {
        return FlyHeight;
    }

    public void setFlyHeight(float FlyHeight) {
        this.FlyHeight = FlyHeight;
    }

    public int getFlyTime() {
        return FlyTime;
    }

    public void setFlyTime(int FlyTime) {
        this.FlyTime = FlyTime;
    }

    public int getLieTime() {
        return LieTime;
    }

    public void setLieTime(int LieTime) {
        this.LieTime = LieTime;
    }

    public float getGrabPos() {
        return GrabPos;
    }

    public void setGrabPos(float GrabPos) {
        this.GrabPos = GrabPos;
    }

    public float getGrabDis() {
        return GrabDis;
    }

    public void setGrabDis(float GrabDis) {
        this.GrabDis = GrabDis;
    }

    public int getGrabTime() {
        return GrabTime;
    }

    public void setGrabTime(int GrabTime) {
        this.GrabTime = GrabTime;
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

    public String getHitAnim() {
        return HitAnim;
    }

    public void setHitAnim(String HitAnim) {
        this.HitAnim = HitAnim;
    }
    
    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public int getUniqueID() {
        return 0;
    }

}
