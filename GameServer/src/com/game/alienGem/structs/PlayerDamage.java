package com.game.alienGem.structs;

import com.game.player.structs.Player;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/11/17 15:03
 */
public class PlayerDamage {

    private Player player;

    private long damage;

    public PlayerDamage(Player player, long damage){
        this.player = player;
        this.damage = damage;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public long getDamage() {
        return damage;
    }

    public void setDamage(long damage) {
        this.damage = damage;
    }
}
