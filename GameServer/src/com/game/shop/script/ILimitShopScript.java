package com.game.shop.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

/**
 * @explain: desc
 * @time Created on 2020/2/5 14:28.
 * @author: tc
 */
public interface ILimitShopScript extends IScript {
	/**
	 * 购买限购商品
	 * @param player
	 * @param id
	 */
	void onReqLimitBuy(Player player, int id);

	/**
	 * 刷新商品
	 * @param player
	 */
	void refresh(Player player);

	/**
	 * online
	 * @param player
	 */
	void online(Player player);
}
