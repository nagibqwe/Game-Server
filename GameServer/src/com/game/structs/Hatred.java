
package com.game.structs;

import game.core.pool.MemoryObject;

/**
 * @author 仇恨类
 */
public class Hatred implements MemoryObject, Comparable<Hatred> {
    //仇恨度
    private long hatred;
    //仇恨对象
    private Fighter target;
    //第一次攻击时间
    private long firstAttackTime;
    //最后一次攻击时间
    private long lastAttackTime;

    public long getHatred() {
        return hatred;
    }

    public void setHatred(long hatred) {
        this.hatred = hatred;
    }

    public void addHatred(long hatred) {
        this.hatred = this.hatred + hatred;
    }

    public Fighter getTarget() {
        return target;
    }

    public void setTarget(Fighter target) {
        this.target = target;
    }

    public long getFirstAttackTime() {
        return firstAttackTime;
    }

    public void setFirstAttackTime(long firstAttackTime) {
        this.firstAttackTime = firstAttackTime;
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }


    @Override
    public int compareTo(Hatred o) {
        if (o.getHatred() == this.hatred) {
            return 0;
        }
        return o.getHatred() > this.hatred ? 1 : -1;
    }

    @Override
    public void release() {
        this.firstAttackTime = 0;
        this.hatred = 0;
        this.lastAttackTime = 0;
        this.target = null;
    }

}
