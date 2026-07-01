package com.game.skill.config;

import com.game.skill.structs.SkillDefine;

/**
 * Created by soko(xysoko@qq.com) on 2018/9/5. copyright 巨匠@雨墨
 */
public class SkillHitEffectInfo {
    private int UniqueID = 0;//唯一id
    private float HitEffectTime = 0;//受击效果的表现时间
//    public AnimationCurve HitTimeCurve = null;   //受击效果的时间变化曲线，用于控制受击效果的表现时间
    private int HitType = SkillDefine.SkillAttackMoveType_None; //受击效果的类型
    private int HitDirType = SkillDefine.SkillHitAway0; //受击方向的类型
    //受击特效
    private int HitVfx = 0; //受击特效
    private String HitSlot = ""; //受击特效插槽
    private float HitVfxScale = 1f;//缩放
    private String HitAnim = ""; //受击动作
    //受击闪烁
    private float BlinkTime = 0.4f;             //闪烁时间
    private float BlinkPower = 1.5f;            //闪烁强度
//    private Color BlinkColor = Color.white;     //闪烁颜色
//    private AnimationCurve BlinkCurve = null;   //闪烁曲线
    //击退的数据
    private float BackDis = 0;//击退的距离
    //击飞的数据
    private float FlyDis = 1f;   //击飞的距离
    private float FlyTime = 1f;//击飞的时间
    private float LieTime = 2f;   //击飞的躺地时间
//    private AnimationCurve HitFlyHeightCurve = null; //飞行高度变化曲线，用以决定高度变化
    //抓取的数据
    private float GrabPos = 0; //抓取目标点离使用者面向方向的距离
    private float GrabDis = 0; //抓取到离目标点的距离

   // private static AnimationCurve _defaultTimeCurve = null;//默认时间曲线
//    private static AnimationCurve _defaultBlinkCurve = null;//默认闪烁曲线
//    private static AnimationCurve _defaultFlyCurve = null;//默认击飞曲线

    /**
     * 构建技能数据
     * @param param 传入字符串
     */
    public int parseData(String[] param , int begin)
    {

        UniqueID = SkillReadConfigUtil.getIntValue(param, begin +0, 0);
        //赋值一个唯一id
        HitEffectTime = SkillReadConfigUtil.getFloatValue(param, begin +1, 1f);
//        HitTimeCurve = SkillEventDataHelper.GetCurveValue(_defaultTimeCurve);
        HitType = SkillReadConfigUtil.getIntValue(param, begin +3, 0);//(EditorSkillHitType)SkillEventDataHelper.GetIntValue((int)EditorSkillHitType.None);
        HitDirType =SkillReadConfigUtil.getIntValue(param, begin +4, 0);// (EditorSkillHitDirType)SkillEventDataHelper.GetIntValue((int)EditorSkillHitDirType.RealDir);
        HitVfx = SkillReadConfigUtil.getIntValue(param, begin +5, 0);
        HitSlot = SkillReadConfigUtil.getStringValue(param, begin + 6, "");//SkillEventDataHelper.GetStringValue(string.Empty);
        HitVfxScale = SkillReadConfigUtil.getFloatValue(param, begin +7, 0);//SkillEventDataHelper.GetFloatValue(1f);
        HitAnim = SkillReadConfigUtil.getStringValue(param, begin +8, "beattacked");
        BlinkTime = SkillReadConfigUtil.getFloatValue(param, begin +9,0.4f);
        BlinkPower = SkillReadConfigUtil.getFloatValue(param, begin +10, 1.5f);
//        BlinkColor = SkillEventDataHelper.GetColorValue(Color.white);
//        BlinkCurve = SkillEventDataHelper.GetCurveValue(_defaultBlinkCurve);
        BackDis = SkillReadConfigUtil.getFloatValue(param, begin +13,0.5f);
        FlyDis = SkillReadConfigUtil.getFloatValue(param, begin +14,1f);
        FlyTime = SkillReadConfigUtil.getFloatValue(param, begin +15,1f);
        LieTime = SkillReadConfigUtil.getFloatValue(param, begin +16,2f);
//        HitFlyHeightCurve = SkillEventDataHelper.GetCurveValue(_defaultFlyCurve);
        GrabPos = SkillReadConfigUtil.getFloatValue(param, begin +18,2f);
        GrabDis = SkillReadConfigUtil.getFloatValue(param, begin +19,2f);
        return 20;
    }

    public int getHitType() {
        return HitType;
    }

    public float getHitDis(int type) {
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

    public float getSpeed(int type) {
        switch (type) {
            case SkillDefine.SkillAttackMoveType_Back:
                return BackDis / HitEffectTime * 1000f;
            case SkillDefine.SkillAttackMoveType_Fly:
                return FlyDis / FlyTime * 1000f;
            case SkillDefine.SkillAttackMoveType_Catch:
                return GrabDis / HitEffectTime * 1000f;
            default:
                return 1f;
        }
    }

    //获取事件执行时间
    public int getRunTime(int type) {
        switch (type) {
            case SkillDefine.SkillAttackMoveType_Back:
                return (int)(HitEffectTime+0.5f);
            case SkillDefine.SkillAttackMoveType_Fly:
                return (int)(FlyTime + LieTime);
            case SkillDefine.SkillAttackMoveType_Catch:
                return (int)(HitEffectTime + 0.5f);
            case SkillDefine.SkillAttackMoveType_AttackHit:
                return 1;
            default:
                return 1;
        }
    }

    public int getUniqueID() {
        return UniqueID;
    }

    @Override
    public String toString() {
        return "SkillHitEffectInfo{" +
                "UniqueID=" + UniqueID +
                ", HitEffectTime=" + HitEffectTime +
                ", HitType=" + HitType +
                ", HitDirType=" + HitDirType +
                ", HitVfx=" + HitVfx +
                ", HitSlot='" + HitSlot + '\'' +
                ", HitVfxScale=" + HitVfxScale +
                ", HitAnim='" + HitAnim + '\'' +
                ", BlinkTime=" + BlinkTime +
                ", BlinkPower=" + BlinkPower +
                ", BackDis=" + BackDis +
                ", FlyDis=" + FlyDis +
                ", FlyTime=" + FlyTime +
                ", LieTime=" + LieTime +
                ", GrabPos=" + GrabPos +
                ", GrabDis=" + GrabDis +
                '}';
    }
}
