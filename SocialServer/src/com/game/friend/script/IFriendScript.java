package com.game.friend.script;

import com.game.friend.struct.PlayerRelation;
import com.game.friend.struct.Relation;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.script.IScript;
import game.message.friendMessage;

public interface IFriendScript extends IScript {



    void G2SReqAddRelationHandler(GlobalPlayerWorldInfo player, friendMessage.G2SReqAddRelation messInfo);

    void G2SReqDeleteRelationHandler(GlobalPlayerWorldInfo player, friendMessage.G2SReqDeleteRelation messInfo);

    /**
     * 跨服审批
     *
     * @param messInfo
     */
    void G2SReqAddFriendApproval(friendMessage.G2SReqAddFriendApproval messInfo);

    /**
     * 跨服审批响应
     *
     * @param messInfo
     */
    void G2SReqAddFriendAnswer(friendMessage.G2SReqAddFriendAnswer messInfo);

    /**
     * 跨服赠送
     * @param messInfo
     */
    void G2SReqGiveFriendShipPoint(friendMessage.G2SReqGiveFriendShipPoint messInfo);
}
