package com.game.chum.inter;

import com.game.chum.struct.ChumPrivilege;
import com.game.player.structs.Player;
import com.game.team.structs.TeamInfo;
import game.core.script.IScript;

import java.util.List;

/**
 * @explain: desc
 * @time Created on 2019/10/22 17:16.
 * @author: tc
 */
public interface IChumScript extends IScript {
	/**
	 * 使用古道热肠道具效果
	 * @param player
	 * @param typ
	 */
	void useItem(Player player, ChumPrivilege typ);

	/**
	 * 获取同袍同泽次数与战力继承比
	 * @param player
	 * @return
	 */
	List<Integer> getCallSoul(Player player);

	/**
	 * 古道热肠奖励
	 * @param player
	 * @param typ
	 */
	void helpReward(Player player, ChumPrivilege typ);

	/**
	 * 挚友组队BUFF
	 * @param teamInfo
	 */
	void checkChumBuff(TeamInfo teamInfo);

	/**
	 * 移除组队BUFF
	 * @param player
	 */
	void removeChumBuff(Player player);

	/**
	 * 退出挚友组
	 * @param playerID
	 * @param lvl 退出时挚友组织的等级
	 */
	void exitChum(long playerID, int lvl);

	/**
	 * 添加活跃点
	 * @param player
	 * @param value
	 */
	void addActiveValue(Player player, int value);

	/**
	 * 请求挚友主面板信息
	 * @param player
	 */
	void onReqChum(Player player);

	/**
	 * 请求挚友排行
	 * @param player
	 */
	void onReqRank(Player player);

	/**
	 * 双向好友挚友信息
	 * @param player
	 */
	void onReqFriend(Player player);

	/**
	 * 邀请目标加入挚友
	 * @param player
	 * @param targetID
	 */
	void onReqInvite(Player player, long targetID);

	/**
	 * 邀请确认
	 * @param player
	 * @param inviteID
	 * @param agree
	 */
	void onReqInviteConfirm(Player player, int inviteID, boolean agree);

	/**
	 * 改名
	 * @param player
	 * @param name
	 */
	void onReqChangeName(Player player, String name);

	/**
	 * 改公告
	 * @param player
	 * @param anno
	 */
	void onReqChangeAnno(Player player, String anno);

	/**
	 * 踢人
	 * @param player
	 * @param targetID
	 */
	void onReqKick(Player player, long targetID);

	/**
	 * 退出
	 * @param player
	 */
	void onReqExit(Player player);

	/**
	 * 召唤元神
	 * @param player
	 */
	void onReqCallSoul(Player player);
}
