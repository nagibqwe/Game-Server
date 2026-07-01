package com.game.guildactivity.struct;

import java.util.HashMap;

/**
 * @Desc TODO
 * @Date 2021/1/12 21:03
 * @Auth ZUncle
 */
public class GuildFudiAttack {

    int bossId;                     //福地boss
    int level;                      //
    long hp;
    HashMap<Long, Long> attacking = new HashMap<>();  //本仙盟正在攻击的玩家

    public int getBossId() {
        return bossId;
    }

    public void setBossId(int bossId) {
        this.bossId = bossId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getHp() {
        return hp;
    }

    public void setHp(long hp) {
        this.hp = hp;
    }

    public HashMap<Long, Long> getAttacking() {
        return attacking;
    }

    public void setAttacking(HashMap<Long, Long> attacking) {
        this.attacking = attacking;
    }
}
