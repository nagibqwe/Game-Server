package com.game.attribute;

import com.data.Global;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.structs.GlobalType;

/**
 * 基本属性值处理
 *
 */
public abstract class BaseAttribute {
    //移动速度（计算之后的数据）
    @JsonIgnore
    private transient float finalMoveSpeed = 0;
    //攻击速度（计算之后的数据）
    @JsonIgnore
    private transient float finalAttackSpeed = 0;
    @JsonIgnore
    private transient long maxHp = 0L;

    public abstract void clean();

    public abstract int getLength();

    protected abstract long gainMoveSpeed();

    protected abstract long gainAttackSpeed();


    public int gainFinalMoveSpeed() {
        int movespeed =   (int) (finalMoveSpeed * 100);
        if (movespeed <= 0){
            calFinalMoveSpeed();
        }
        return (int) (finalMoveSpeed * 100);
    }

    //移动速度基础值*(1+移动速度值/2000)
    public void calFinalMoveSpeed() {

        this.finalMoveSpeed =Global.BaseMoveSpeed * (gainMoveSpeed() * 1F / Global.MoveSpeed );
    }

    public int gainFinalAttackSpeed() {
        return (int) (finalAttackSpeed * 100);
    }

    //(1+攻击数值/2000)
    public void calFinalAttackSpeed() {
        this.finalAttackSpeed = 1 + gainAttackSpeed() / GlobalType.AttackSpeedOffset;
    }

    public long MaxHP() {
        return maxHp;
    }

    public void addMaxHP(long value) {
        maxHp += value;
    }

    public void cleanMaxHP() {
        maxHp = 0;
    }

}
