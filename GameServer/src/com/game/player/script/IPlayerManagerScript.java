package com.game.player.script;

import com.game.db.bean.roleBean;
import com.game.player.structs.Player;
import com.game.player.structs.SavePlayerLevel;
import game.core.script.IScript;

/**
 * @explain: base
 * @time Created on 2019/11/27 11:34.
 * @author: tc
 */
public interface IPlayerManagerScript extends IScript {
	/**
	 * 非主动动加载玩家
	 * @param roleId
	 * @return
	 */
	Player LoadPlayerFromDB(long roleId);

	/**
	 * 玩家的数据保存
	 *
	 * @param player
	 */
	void TickSavePlayer(Player player);

	/**
	 * 定时保存玩家数据
	 */
	void TickSavePlayer();

	/**
	 * 立即保存玩家数据
	 * @param player
	 * @return
	 */
	boolean SavePlayer(Player player);

	/**
	 * 保存玩家,不同步数据到世界服
	 * @param player
	 * @param level
	 */
	void SavePlayer(Player player, SavePlayerLevel level);

	/**
	 * 停服时保存所有玩家数据
	 */
	void SaveAllPlayer();

	/**
	 * 玩家登录完成同步数据
	 *
	 * @param player
	 * @param isReConnect
	 */
	void OnSendPlayerAllInfo(Player player, boolean isReConnect);

    /**
     * 同步离线玩家的数据
     * @param player
     * @param isSave
     */
    void syncPlayerWorldInfo(Player player, boolean isSave);

	/**
	 * remove player
	 * @param player
	 */
	void removePlayer(Player player);

	/**
	 * remove player
	 * @param roleId
	 */
	void removePlayer(long roleId);

	/**
	 * 生成数据库bean
	 * @param player
	 * @return
	 */
	roleBean makeRoleBeanByPlayer(Player player);

	/**
	 * 切换玩家的PK模式
	 *
	 * @param player   指定玩家
	 * @param state    PK模式
	 * @param isNotice 是否通知
	 */
	void onUpdatePkState(Player player, int state, boolean isNotice);

	/**
	 * 玩家等级变化
	 *
	 * @param player
	 * @param oldLevel 升级之前等级
	 * @param oldExp 升级之前经验
	 * @param nowLevel 当前等级
	 * @param nowExp 当前经验
	 * @param changeExp 改变的经验
	 * @param reason 升级原因
	 * @param action
	 */
	void playerLevelUp(Player player, int oldLevel, long oldExp, int nowLevel, long nowExp, long changeExp, int reason, long action);

	/**
	 * 增加修改玩家职业的接口
	 *
	 * @param player
	 * @param career
	 */
	void onChangeCareer(Player player, int career);

	/**
	 * 改变等级、职业后更改登录服的角色登录数据
	 *
	 * @param playerId
	 * @param name
	 */
	void changeLoginName(long playerId, String name);

	void changeLoginLevel(long playerId, int level);

	void changeLoginCareer(long playerId, int career);

	void changeLoginDelete(long playerId, int deleteTime);

	void changeLoginFight(long playerId, long fight);

	void changeLoginUserId(long playerId, long userId);

	void insertLoginData(long roleId, long userId, int serverId, String roleName, int lv, int career, int deleteTime);

	/**
	 * TODO 玩家零点刷新
	 * @param player
	 */
	void zeroClockPlayerDeal(Player player);
}
