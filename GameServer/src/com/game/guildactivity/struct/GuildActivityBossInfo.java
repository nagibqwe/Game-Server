package com.game.guildactivity.struct;

import java.util.concurrent.ConcurrentHashMap;

public class GuildActivityBossInfo {

    /**
     * 玩家玩家造成伤害
     */
    private ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> playerDamage = new ConcurrentHashMap<>();

    /**
     * 仙盟造成的伤害
     */
    private ConcurrentHashMap<Long, Long> guildDamage = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> getPlayerDamage() {
        return playerDamage;
    }

    public void setPlayerDamage(ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> playerDamage) {
        this.playerDamage = playerDamage;
    }

    public ConcurrentHashMap<Long, Long> getGuildDamage() {
        return guildDamage;
    }

    public void setGuildDamage(ConcurrentHashMap<Long, Long> guildDamage) {
        this.guildDamage = guildDamage;
    }
}
