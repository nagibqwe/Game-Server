package com.game.welfare.script;

import com.game.player.structs.Player;
import com.game.welfare.struct.RetrieveType;
import game.core.script.IScript;

/**
 * @explain: desc
 * @time Created on 2020/1/6 17:42.
 * @author: tc
 */
public interface IRetrieveResScript extends IScript {
	/**
	 * 完成一次活动
	 * @param player
	 * @param retrieveType
	 */
	void count(Player player, RetrieveType retrieveType);

	/**
	 * 完成count次活动
	 * @param player
	 * @param retrieveType
	 * @param count
	 */
	void count(Player player, RetrieveType retrieveType, long count);

	/**
	 * 请求找回资源
	 * @param player
	 * @param type
	 * @param rrType
	 * @param count
	 * @param isOnkey
	 */
	int onReqRetrieveRes(Player player, int type, int rrType,int count,boolean isOnkey);

	/**
	 * 玩家上线
	 * @param player
	 */
	void online(Player player);

	/**
	 * 跨天
	 * @param player
	 */
	void switchDay(Player player);

	/**
	 * 是否可以找回
	 * @param player
	 */
	boolean canRetrieveRes(Player player);

	/**
	 * VIP购买日常活动次数
	 * @param player
	 * @param dailyID
	 * @param addnum
	 */
	void addVipBuyCount(Player player,int dailyID,int addnum);


	/**
	 * // 请求一键找回
	 * @param player
	 */
	void onReqOneKeyRetrieveRes(Player player,int rrType,int baseTpe);


	/**
	 * 跨服发回游戏服资源统计
	 * @param player
	 * @param type
	 */
	void onSendResourceFindChangeToGame(Player player,int type);


	/**
	 * 接收跨服返回的活动参与
	 * @param roleId
	 * @param type
	 */
	void onF2GResourceFindChange(long roleId,int type);
}
