package com.game.world_help.struct;

import com.game.player.structs.Player;

/**
 * @explain: desc
 * @time Created on 2019/12/16 14:23.
 * @author: tc
 */
public class SpecialHatred {
	private Player player; // player
	private long damage; // 伤害

	public SpecialHatred(Player player, long damage) {
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
