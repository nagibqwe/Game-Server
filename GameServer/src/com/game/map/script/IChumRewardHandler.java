package com.game.map.script;

import com.game.player.structs.Player;

/**
 * @explain: desc
 * @time Created on 2019/10/31 15:54.
 * @author: tc
 */
public interface IChumRewardHandler {
	/**
	 * 使用挚友特权道具完成进度
	 * @param player
	 */
	void useChumItemCompleteCloneMap(Player player);
}
