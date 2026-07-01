package com.game.fight.structs;

/**
 * 枚举值2的 n 次方， n < 32
 *
 * @author 战斗状态
 */
public enum FightEnum {

    SkillFreeze(0), // 技能僵直 
    SuperArmorCount(1), //霸体状态
    ;

    private final int value;

    private FightEnum(int state) {
        this.value = 1 << state;
    }

    public int getState() {
        return value;
    }

    public boolean compare(int state) {
        return (this.value & state) != 0;
    }

}
