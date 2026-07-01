package com.game.monster.script;

import com.game.attribute.BaseLongAttribute;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.PlayerAttributeType;
import com.game.structs.Entity;
import com.game.structs.Fighter;


public interface IMonsterAction {

    /**
     * 怪物死亡
     * @param monster
     * @param attacker
     */
    void monsterDie(Monster monster, Fighter attacker);

    /**
     * 怪物攻击
     * @param monster
     * @param attacker
     * @param damage
     */
    void monsterBeAttack(Monster monster, Fighter attacker, long damage);

    void changeLineOtherMonsterCurHp(Monster monster, long otherMonster, long curHp);

    //清理怪物的引导技能
    void clearSkillMagic(MapObject map, Monster monster);

    //怪物的属性变化通知变更
    void monsterAttributeChange(BaseLongAttribute oldAttributsVlaue, Entity monster, PlayerAttributeType type);
    

}
