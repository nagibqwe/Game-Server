package com.game.skill.script;

import com.data.bean.Cfg_Skill_Bean;
import com.game.structs.Fighter;

public interface ISkillTriggerScript {

    /**
     * 触发被动技能检测
     * @param attack
     * @param defer
     * @param skillBean
     * @return
     */
    boolean OnTrigger(Fighter attack, Fighter defer, Cfg_Skill_Bean skillBean);

}
