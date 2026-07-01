package com.game.skill.config.event;

import com.game.skill.config.FindTargetInfo;
import com.game.skill.config.SkillEvent;
import com.game.skill.config.SkillHitEffectInfo;
import com.game.skill.config.SkillReadConfigUtil;
import com.game.utils.Shape;
import com.game.utils.Symbol;

/**
 * Created by soko(xysoko@qq.com) on 2018/8/30. copyright 巨匠@雨墨
 */
public class PlayLockTrajectoryEvent extends SkillEvent {

    private FindTargetInfo findInfo = new FindTargetInfo();
    private SkillHitEffectInfo hitInfo = new SkillHitEffectInfo();
    private String startSlot = ""; //特效开始的插槽
    private int vfxID = 0;   //特效的ID
    private float vfxRadius = 1f;//特效半径
    private float flySpeed = 1f; //特效飞行速度
    private float flyAddSpeed = 0f;//特效飞行加速度
    private int canHitRepeatTarget = 0;//是否可以命中重复目标，为false的时候只对范围的内的每一个目标造成一次伤害，为true的时候，有可能会对某些模板造成多次伤害

    @Override
    public void split(String value) {
        String[] params = value.split(Symbol.FENHAO_REG);
        int begin = findInfo.split(params, 0);
        int end = hitInfo.parseData(params, begin);
        startSlot = SkillReadConfigUtil.getStringValue(params, end, "");
        vfxID = SkillReadConfigUtil.getIntValue(params, end + 1, 0);
        vfxRadius = SkillReadConfigUtil.getFloatValue(params, end + 2, 1f);
        flySpeed = SkillReadConfigUtil.getFloatValue(params, end + 3, 1f);
        flyAddSpeed = SkillReadConfigUtil.getFloatValue(params, end + 4, 0f);
        canHitRepeatTarget = SkillReadConfigUtil.getIntValue(params, end + 5, 0);
    }

    @Override
    public float getSpeed(int type) {
        return hitInfo.getSpeed(type);
    }

    @Override
    public int getRunTime(int type) {
        return hitInfo.getRunTime(type);
    }

    @Override
    public int getHitType() {
        return hitInfo.getHitType();
    }

    @Override
    public float getHitDis(int type) {
        return hitInfo.getHitDis(type);
    }

    @Override
    public Shape getShape() {
        return findInfo.getShape();
    }

    @Override
    public int getUniqueID() {
        return hitInfo.getUniqueID();
    }

    public FindTargetInfo getFindInfo() {
        return findInfo;
    }

    public SkillHitEffectInfo getHitInfo() {
        return hitInfo;
    }

    public String getStartSlot() {
        return startSlot;
    }

    public int getVfxID() {
        return vfxID;
    }

    public float getVfxRadius() {
        return vfxRadius;
    }

    public float getFlySpeed() {
        return flySpeed;
    }

    public float getFlyAddSpeed() {
        return flyAddSpeed;
    }

    public int getCanHitRepeatTarget() {
        return canHitRepeatTarget;
    }
}
