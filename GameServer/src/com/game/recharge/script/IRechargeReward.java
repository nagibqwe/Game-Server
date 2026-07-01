package com.game.recharge.script;

import com.game.player.structs.Player;

/**
 * @explain: desc
 * @time Created on 2020/1/3 13:50.
 * @author: tc
 */
public interface IRechargeReward {
	/**
	 * 能否给奖励
	 * @param player
	 * @param goodId
	 * @return
	 */
	boolean canReward(Player player, int goodId);

	/**
	 * 给奖励之后的逻辑处理
	 * @param player
	 * @param orderId
	 * @param goodId
	 */
	void afterReward(Player player, String orderId, int goodId);
}
