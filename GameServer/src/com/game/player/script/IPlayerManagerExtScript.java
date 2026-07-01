package com.game.player.script;

import com.game.player.structs.Player;
import com.google.protobuf.ByteString;
import game.core.script.IScript;
import game.message.CommonMessage;
import game.message.CrossFightMessage;
import game.message.PlayerMessage;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @explain: sync 相关
 * @time Created on 2019/11/27 11:34.
 * @author: tc
 */
public interface IPlayerManagerExtScript extends IScript {
	/**
	 * 同步PlayerInfo到客户端
	 *
	 * @param player
	 */
	void synSelfPlayerInfo(Player player);

	/**
	 * 查看他人信息
	 *
	 * @param player
	 * @param otherId
	 */
	void lookOtherPlayer(Player player, long otherId);

	/**
	 * 同步玩家的账号
	 *
	 * @param fid
	 * @param player
	 * @param fightId
	 * @param modelId
	 * @param ratt
	 * @param mapModelId
	 * @param zoneLevel
	 * @param cloneAtt
	 * @param onlyJoin
	 * @return
	 */
	boolean OnSynPlayerInfoToFight(int fid, Player player, long fightId, int modelId, CrossFightMessage.roleAtt ratt, int mapModelId, int zoneLevel, List<CommonMessage.CrossAttribute> cloneAtt, boolean onlyJoin);

	/**
	 * 通知所有的战斗服玩家应该下线了
	 *
	 * @param player
	 */
	void onCrossPlayerOut(Player player);

	void sendGiftToFriend(Player player, PlayerMessage.ReqSendGift mess);

	void onReqGetGiftLog(Player player, int type);

	void onReqReadGiftLog(Player player, List<Long> ids);

	/**
	 * 续上玩家的连接
	 *
	 * @param session
	 * @param platsid
	 */
	void addPlayerSession(ChannelHandlerContext session, String platsid);

	/**
	 * 清理跨服玩家的连接信息
	 * @param key
	 */
	void removePlayerSession(String key);

	/**
	 * 通知战斗服更新某个功能的最新信息(广播战斗服周围玩家)
	 * @param player
	 * @param type
	 * @param value
	 * @param msgId
	 * @param messData
	 */
	void noticeSynRoleInfoToFight(Player player, int type, java.util.Map<Integer, Object> value, int msgId, ByteString messData);

	void SendMaxHpChange(Player player);

    /**
     * 请求玩家简要信息
     */
	void onReqPlayerSummaryInfo(Player player, long roleId);
}
