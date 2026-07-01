package com.game.fight.structs;

public enum FightEffect {

    /**
     *  受击僵直
     */
    AttackHit(0),
    /**
     * 击退
     */
    AttackBack(1),
    /**
     * 击飞
     */
    AttackFly(2),
    /**
     * 抓取
     */
    Catch(3),
    /**
     * 暴击
     */
    Crit(4),
    /**
     * 会心
     */
    HitDef(5),
    /**
     * 追击
     */
    Pursue(6),
    /**
     * 连击
     */
    DHit(7),
    /**
     * 无敌
     */
    Invincible(8),

    /**
     * 免疫
     */
    Immunity(9),
    /**
     * 多倍攻击效果
     */
    MultipleAtk(10),
    ;
    private final int value;

    FightEffect(int offset) {
        this.value = 1 << offset;
    }

    public int getValue() {
        return this.value;
    }

    //效果相加
    public int addEffect(int effect) {
        return this.value | effect;
    }

    public boolean compareTo(int value) {
        return (this.value & value) > 0;
    }

}
