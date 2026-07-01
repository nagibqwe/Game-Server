package com.game.player.script;

import com.data.bean.Cfg_Item_Bean;
import com.game.backpack.structs.Item;
import com.game.player.structs.Player;
import game.core.script.IScript;

import java.util.List;

/**
 * @explain: desc
 * @time Created on 2020/2/3 18:31.
 * @author: tc
 */
public interface IXiSuiScript extends IScript {
	/**
	 * 请求洗髓升级
	 * @param player
	 * @param free true免费洗髓（消耗道具），false收费洗髓（消耗货币）
	 */
	void onReqXiSui(Player player, boolean free);

	/**
	 * 该玩家是否需要
	 * @param player
	 * @param itemId
	 * @return
	 */
	boolean isNeed(Player player, int itemId);

	/**
	 * 使用道具
	 * @param player
	 * @param itemModuleId
	 * @param num
	 * @param reason
	 * @param action
	 */
	void useItem(Player player, int itemModuleId, int num, int reason, long action);

	/**
	 * 获取洗髓包的掉落
	 * @param player
	 * @param itemModuleId
	 * @param num
	 * @return
	 */
	List<Item> getDropItems(Player player, int itemModuleId, int num);

	/**
	 * 获取洗髓包的掉落
	 * @param player
	 * @param bean
	 * @param num
	 * @return
	 */
	List<Item> getDropItems(Player player, Cfg_Item_Bean bean, int num);

    /**
     * 获取对应转职阶数需要的元宝数
     */
	int calcOneKeyCoinNum(Player player, int curGenderClass);

	/**
	 * 一键洗髓完成
	 */
	void oneKeySucess(Player player, int genderClass);

}
