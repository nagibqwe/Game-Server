package com.game.leaderpreach.inter;

import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import game.core.script.IScript;

/**
 * @explain: desc
 * @time Created on 2019/11/8 15:27.
 * @author: tc
 */
public interface ILeaderPreachScript extends IScript {
	/**
	 * 请求进入掌门传道副本
	 * @param player
	 * @param phase
	 */
	void onReqLeaderPreachEnter(Player player);

	/**
	 * 心跳
	 */
	void action();


	/**
	 * 下线踢出副本
	 * @param player
	 */
	void offline(Player player);


	/**
	 * 打坐姿势状态
	 * @param player
	 * @param isSit
	 */
	void onLeaderSitDown(Player player,boolean isSit);


	/**
	 * 离开传道
	 * @param player
	 */
	void onLeaveLeaderPreach(Player player);
}
