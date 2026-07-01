package com.game.friend.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.friendMessage;

/**
 * 跨服好友相关脚本
 */
public interface ICrossFriendScript extends IScript {
    //跨服添加好友
    void S2GResAddRelation(friendMessage.S2GResAddRelation messInfo);

    /**
     * 跨服添加好友审批成功
     * @param messInfo
     */
    void S2GResAddFriendApproval(friendMessage.S2GResAddFriendApproval messInfo);

    /**
     * 跨服好友审批响应 把审批玩家加入自己的好友列表
     * @param messInfo
     */
    void S2GResAddFriendAnswer(friendMessage.S2GResAddFriendAnswer messInfo);

    /**
     * 跨服删除好友
     * @param messInfo
     */
    void S2GResDeleteRelation(friendMessage.S2GResDeleteRelation messInfo);

    /**
     * 跨服赠送
     * @param messInfo
     */
    void S2GReqGiveFriendShipPoint(friendMessage.S2GReqGiveFriendShipPoint messInfo);
}
