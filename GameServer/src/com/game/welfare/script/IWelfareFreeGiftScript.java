package com.game.welfare.script;

import com.game.player.structs.Player;

/**
 * 福利免费礼包
 * @explain: desc
 * @time Created on 2019/12/24 17:38.
 * @author: tc
 */
public interface IWelfareFreeGiftScript extends IWelfareScript {
	/**
	 * 领取免费礼包
	 * @param player
	 */
	void ReqWelfareFreeGift(Player player);
}
