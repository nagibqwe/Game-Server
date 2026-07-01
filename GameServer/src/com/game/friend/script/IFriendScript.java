package com.game.friend.script;

import com.game.friend.struct.Friend;
import com.game.friend.struct.PlayerRelation;
import com.game.friend.struct.Relation;
import com.game.friend.struct.RelationInfo;
import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.friendMessage;

public interface IFriendScript extends IScript {

    /**
     * 添加亲密度
     * @param player
     * @param otherId
     * @param num
     * @return
     */
    boolean addIntimacy(Player player, long otherId, int num);

    /**
     * 添加关系
     * @param player
     * @param messInfo
     */
    void addRelation(Player player, friendMessage.ReqAddRelation messInfo);

    /**
     * 添加好友
     * @param player
     * @param otherId
     */
    void addFriend(Player player, long otherId,friendMessage.ReqAddRelation messInfo);
    void addFriend(Player player, long otherId);
    /**
     * 添加最近
     * @param playerId
     * @param otherId
     */
    void addLatelyPlayer(long playerId, long otherId);

    /**
     * 删除关系
     * @param player
     * @param messInfo
     */
    void deleteRelation(Player player, friendMessage.ReqDeleteRelation messInfo);

    /**
     * 查找好友
     * @param player
     * @param messInfo
     */
    void dimSelect(Player player, friendMessage.ReqDimSelect messInfo);

    /**
     * 获取关系列表
     * @param player
     * @param type
     */
    void getRelationList(Player player, int type);

    /**
     * 获取与玩家的关系
     * @param relation
     * @param otherId
     * @return
     */
    Relation getFriendRelation(PlayerRelation relation, long otherId);

    /**
     * 是否双向好友
     * @param player
     * @param targetId
     * @return
     */
    boolean isRealFriend(Player player, long targetId);

    /**
     * 获取亲密度
     * @param relation
     * @param otherId
     * @return
     */
    int getFriendIntimacy(PlayerRelation relation, long otherId);

    /**
     * 击杀玩家处理
     * @param diePlayer
     * @param attackerPlayer
     */
    void dealPlayerKillPlayer(Player diePlayer, Player attackerPlayer);

    /**
     * 举报玩家
     * @param player
     */
    void onReport(Player player, friendMessage.ReqReport messInfo);


    /**
     * 添加好友 审批结果请求
     * @param player
     * @param messInfo
     */
    void reqAddFriendApproval(Player player, friendMessage.ReqAddFriendApproval messInfo);

    /**
     * 在线检测
     * @param player
     */
     void online(Player player);


     void reqGiveFriendShipPoint(Player player, friendMessage.ReqGiveFriendShipPoint messInfo);

    /**
     * 零点刷新好友变量
     */
     void zeroClockDeal(Player player);

    /**
     * 添加好友成功派发
     * @param player
     * @param ohterPlayerSex
     */
    void addFriendEvent(Player player,int ohterPlayerSex);

    /**
     * 转换公共信息
     * @param info
     * @return
     */
    friendMessage.CommonInfo.Builder toCommonInfoBuilder(Player player,RelationInfo info);

    /**
     * 好友条件相关刷新
     * @param player
     */
    void onRefreshUpProgress(Player player);

    /**
     * 发送情义点
     * @param player
     * @param friend
     */
    void sendResFriendShipPointCommonInfo(Player player, Friend friend);

    /**
     * npc好友赠送情义点
     * @param player
     */
    void ReqNpcFriendGiveShipPoint(Player player, friendMessage.ReqNpcFriendGiveShipPoint messInfo);
}
