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
public class PlaySummonObject extends SkillEvent {

    public int PosType = 0; //召唤物位置0：自身位置，1：主目标位置
    public String CfgName = ""; //配置名字

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
    public float getHitDis(int type) {
        return 1f;
    }

    @Override
    public void split(String param) {
        String[] params = param.split(Symbol.FENHAO_REG);
        PosType = Integer.parseInt(params[0]);
        CfgName = params[1];
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
