package com.game.welfare.script;

import com.game.player.structs.Player;

/**
 * @explain: desc
 * @time Created on 2019/12/24 17:38.
 * @author: tc
 */
public interface ILevelGiftScript extends IWelfareScript {
	/**
	 * 领取等级礼包
	 * @param player
	 * @param level
	 */
	void onReqReceiveLevelGift(Player player, int level);
}
