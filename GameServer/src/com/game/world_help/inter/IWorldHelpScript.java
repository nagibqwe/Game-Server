package com.game.world_help.inter;

import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.structs.Entity;
import com.game.structs.Fighter;
import com.game.world_help.struct.SpecialHatred;
import game.core.script.IScript;

import java.util.List;

/**
 * @explain: desc
 * @time Created on 2019/12/11 17:16.
 * @author: tc
 */
public interface IWorldHelpScript extends IScript {
	/**
	 * 请求世界支援
	 * @param player
	 * @param bossCode
	 */
	void onReqWorldHelp(Player player, long bossCode);

	/**
	 * 请求已有的世界支援列表
	 * @param player
	 */
	void onReqWorldHelpList(Player player);

	/**
	 * 同意加入世界支援
	 * @param player
	 * @param id
	 */
	void onReqJoinHelp(Player player, int id);

	/**
	 * 获取上一次被支援的信息
	 * @param player
	 */
	void onReqAtLastHelp(Player player);

	/**
	 * thk
	 * @param player
	 * @param id
	 * @param words
	 */
	void onReqThkHelp(Player player, int id, String words);

	/**
	 * 取消支援
	 * @param player
	 */
	void onReqCancelHelp(Player player);

	/**
	 * 请求发起公会任务支援
	 * @param player
	 */
	void onReqGuildTaskHelp(Player player);

	/**
	 * 玩家上线
	 * @param player
	 */
	void online(Player player);

	/**
	 * offline
	 * @param player
	 */
	void offline(Player player);

	/**
	 * 玩家死亡
	 * @param player
	 */
	void die(Player player);

	/**
	 * enter map
	 * @param player
	 * @param map
	 */
	void enterMap(Player player, MapObject map);

	/**
	 * BOSS死亡
	 * @param mapObject
	 * @param monster
	 */
	void bossDie(MapObject mapObject, Monster monster);

	/**
	 * 检查是否清空仇恨列表
	 */
	boolean canClear(Entity entity);

	/**
	 * 清空仇恨目标
	 * @param code
	 */
	void clearHatred(long code);

	/**
	 * 移除仇恨目标
	 * @param code
	 * @param tar
	 */
	void removeHatred(long code, long tar);

	/**
	 * 玩家当前是否在仙盟BOSS支援的地图
	 * @param player
	 * @return
	 */
	boolean isInWorldHelpMap(Player player);

	/**
	 * 是否世界支援的玩家
	 * @param roleId
	 * @return
	 */
	boolean isHelp(long roleId);

	/**
	 * 是否世界支援的玩家
	 * @param roleId
	 * @param bossCode
	 * @return
	 */
	boolean isHelpBoss(long roleId, long bossCode);

	/**
	 * 检查是否在支援状态，且在支援地图
	 * @param player
	 * @return
	 */
	boolean isHelpAndMap(Player player);

	/**
	 * 检查是否在支援状态，且在支援地图
	 * @param player
	 * @param bossCode
	 * @return
	 */
	boolean isHelpAndMapBoss(Player player, long bossCode);

	/**
	 * 通知客户端BOSS伤害
	 * @param bossTyp
	 * @param attacker
	 * @param monster
	 */
	void sendResSynHarmRank(int bossTyp, Player attacker, Monster monster);

	/**
	 * 得到新的仇恨伤害列表
	 * @param monster
	 * @return
	 */
	List<SpecialHatred> getSpecialHatredRes(Monster monster);

	/**
	 * 清空任务支援相关
	 * @param roleId
	 */
	void clearTask(long roleId);

	/**
	 * 加入任务支援列表
	 * @param leaderId
	 * @param roleId
	 */
	void joinTaskHelpInfo(long leaderId, long roleId);

	/**
	 * 提升队长
	 */
	void changeTeamLeader(Player player, long helperId, long teamId);

	void sendTaskHelpReward(Player player, MapObject mapObject, int helpId, boolean isHelp);

	/**
	 * 检查仙盟支援的阵营问题
	 */
	void checkWorldHelpCamp(Monster monster, Fighter defer);

	/**
	 * 死亡求援
	 * @param player
	 */
	void reqDieCallHelp(Player player);
}
