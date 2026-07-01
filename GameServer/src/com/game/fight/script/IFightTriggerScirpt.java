package com.game.fight.script;

import com.data.bean.Cfg_Skill_Bean;
import com.game.attribute.BaseIntAttribute;
import com.game.map.structs.MapObject;
import com.game.structs.Fighter;
import game.core.script.IScript;

/**
 * @Description
 * @auther lw
 * @create 2019-12-19 19:54
 */
public interface IFightTriggerScirpt extends IScript {

    /**
     * 被动技能效果触发
     * @param mapObject
     * @param source
     * @param target
     * @param ba
     * @param bf
     * @param type
     * @param isTrigger
     */
    void trigger(MapObject mapObject, Fighter source, Fighter target, BaseIntAttribute ba, BaseIntAttribute bf, int type, boolean isTrigger);

    /**
     * 主动技能效果触发
     * @param bean
     * @param mapObject
     * @param source
     * @param target
     * @param ba
     * @param bf
     * @param type
     * @param isTrigger
     */
    void activeTrigger(Cfg_Skill_Bean bean, MapObject mapObject, Fighter source, Fighter target, BaseIntAttribute ba, BaseIntAttribute bf, int type, boolean isTrigger);

}
