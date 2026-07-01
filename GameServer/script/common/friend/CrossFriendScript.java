package common.friend;

import com.data.Global;
import com.data.MessageString;
import com.game.chat.structs.ChatChannel;
import com.game.chat.structs.LeaveMsg;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.friend.script.ICrossFriendScript;
import com.game.friend.struct.Friend;
import com.game.friend.struct.LatelyPlayer;
import com.game.friend.struct.PlayerRelation;
import com.game.friend.struct.Relation;
import com.game.manager.Manager;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.message.ChatMessage;
import game.message.friendMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CrossFriendScript  implements ICrossFriendScript {

    private static final Logger logger = LogManager.getLogger("CrossFriendScript");

    @Override
    public int getId() {
        return ScriptEnum.CrossFriendScript;
    }
    @Override
    public Object call(Object... objects) {
        return null;
    }
    public void S2GResAddRelation(friendMessage.S2GResAddRelation messInfo){
        PlayerRelation targetPlayerRelation = Manager.friendManager.getPlayerRelation(messInfo.getTargetPlayerId());
        //对方好友数量已达上线
        if (targetPlayerRelation.getFriends().size() >= Global.FriendMax) {
            // MessageUtils.notify_player(player, Notify.ERROR, MessageString.FriendMaxSize);
            return;
        }
        //对方屏蔽你的好友申请
        if (targetPlayerRelation.getShieldAddFriend().contains(messInfo.getSourceApprovalPlayerInfo().getPlayerId())) {
            return;
        }
        //申请列表
        targetPlayerRelation.getApprovalList().put(messInfo.getSourceApprovalPlayerInfo().getPlayerId(),messInfo.getSourceApprovalPlayerInfo().getServerId());
        targetPlayerRelation.setDataChanged(true);

        Manager.friendManager.savePlayerRelation(targetPlayerRelation);
        Player targetPlayer =  Manager.playerManager.getPlayerOnline(messInfo.getTargetPlayerId());
        if(targetPlayer!=null){
            //改为需要对方同意
            friendMessage.ResAddFriendApprovalToTarget.Builder resAddFriendApprovalToTarget = friendMessage.ResAddFriendApprovalToTarget.newBuilder();
            resAddFriendApprovalToTarget.setSourcePlayer(messInfo.getSourceApprovalPlayerInfo());
            MessageUtils.send_to_player(messInfo.getTargetPlayerId(),friendMessage.ResAddFriendApprovalToTarget.MsgID.eMsgID_VALUE,resAddFriendApprovalToTarget.build().toByteArray());
        }
    }
    //添加最近列表
    public void addLatelyPlayer(long playerId, PlayerWorldInfo otherPlayerWorldInfo) {
        if (!Manager.playerManager.getAllPlayerWorldInfo().containsKey(playerId)) {
            return;
        }
        PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(playerId);
        if (playerRelation.getShields().containsKey(otherPlayerWorldInfo.getRoleid())) {
            logger.info("玩家已被屏蔽，不能添加到最近列表");
            return;
        }


        LatelyPlayer latelyPlayer = new LatelyPlayer();
        latelyPlayer.setRoleId(otherPlayerWorldInfo.getRoleid());
        latelyPlayer.setInfo(otherPlayerWorldInfo);
        playerRelation.addLatelyPlayer(latelyPlayer);
        Manager.friendManager.savePlayerRelation(playerRelation);

        //返回消息
        Player player = Manager.playerManager.getPlayerCache(playerId);
        friendMessage.ResAddFriendSuccess.Builder msg = friendMessage.ResAddFriendSuccess.newBuilder();
        msg.setType(Relation.RelationType_LaterPlayerList.getValue());

        if(playerRelation.getFriends().containsKey(latelyPlayer.getRoleId())){
            Friend friend = playerRelation.getFriends().get(latelyPlayer.getRoleId());
            msg.addResultList(Manager.friendManager.deal().toCommonInfoBuilder(player,friend));
        }else {
            msg.addResultList(Manager.friendManager.deal().toCommonInfoBuilder(player,latelyPlayer));
        }

        MessageUtils.send_to_player(player, friendMessage.ResAddFriendSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
    /**
     * 跨服审批 把对方加为自己的好友
     * @param messInfo
     */
    public void S2GResAddFriendApproval(friendMessage.S2GResAddFriendApproval messInfo){



        friendMessage.ResAddFriendSuccess.Builder approvalAddFriendSuccessMsg = friendMessage.ResAddFriendSuccess.newBuilder();
        approvalAddFriendSuccessMsg.setType(Relation.RelationType_Friend.getValue());
        friendMessage.AddFriendApproval addFriendApproval = messInfo.getAddFriendApproval();

        Player targetPlayer = PlayerManager.getInstance().getPlayer(addFriendApproval.getTargetPlayerId());
        if(targetPlayer == null){
            return;
        }
        PlayerRelation targetPlayerRelation = Manager.friendManager.getPlayerRelation(addFriendApproval.getTargetPlayerId());
        //对方好友数量已达上线
        if (targetPlayerRelation.getFriends().size() >= Global.FriendMax) {
            return;
        }
        PlayerWorldInfo approvalPlayerWorldInfo = new PlayerWorldInfo();
        approvalPlayerWorldInfo.fromGlobalPlayerWorldInfo(addFriendApproval.getGlobalPlayerWorldInfo());
        if(targetPlayerRelation.getFriends().containsKey(addFriendApproval.getGlobalPlayerWorldInfo().getRoleid())){
            //好友已经存在
            return;
        }
        //构建好友实体
        Friend friend = new Friend();
        friend.setRoleId(addFriendApproval.getGlobalPlayerWorldInfo().getRoleid());
        friend.setInfo(approvalPlayerWorldInfo);
        targetPlayerRelation.addFriend(friend);
        //留言
        LeaveMsg leaveMsg = new LeaveMsg();
        leaveMsg.fromBuildInfo(addFriendApproval.getLeaveMessage());
        this.addLatelyPlayer(targetPlayer.getId(), approvalPlayerWorldInfo);
        Manager.chatManager.deal().leaveMsg(addFriendApproval.getTargetPlayerId(), leaveMsg);
        //在线
        if(targetPlayer != null && targetPlayer.isOnline()){
            //触发事件
            Manager.friendManager.deal().addFriendEvent(targetPlayer,approvalPlayerWorldInfo.getSex());
            approvalAddFriendSuccessMsg.addResultList(Manager.friendManager.deal().toCommonInfoBuilder(targetPlayer,friend));
            MessageUtils.send_to_player(targetPlayer, friendMessage.ResAddFriendSuccess.MsgID.eMsgID_VALUE, approvalAddFriendSuccessMsg.build().toByteArray());
            //添加好友自动聊天
            ChatMessage.ChatResSC.Builder  msg = ChatMessage.ChatResSC.newBuilder();
            msg.setInfo(addFriendApproval.getChatResSC());
            MessageUtils.send_to_player(targetPlayer, ChatMessage.ChatResSC.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }else {
            //上线延迟处理
            friend.setIsOffLineAdd(1);
        }
        Manager.friendManager.savePlayerRelation(targetPlayerRelation);
        //获取玩家信息
        //响应原来服 把自己添加 返回
        friendMessage.G2SReqAddFriendAnswer.Builder  g2SReqAddFriendAnswer = friendMessage.G2SReqAddFriendAnswer.newBuilder();
        friendMessage.AddFriendAnswer.Builder  addFriendAnswer = friendMessage.AddFriendAnswer.newBuilder();
        addFriendAnswer.setApprovalPlayerId(messInfo.getAddFriendApproval().getApprovalPlayerId());
        addFriendAnswer.setApprovalServerId(messInfo.getAddFriendApproval().getApprovalServerId());
        addFriendAnswer.setApprovalTargetPlat(messInfo.getAddFriendApproval().getApprovalPlat());
        addFriendAnswer.setAnswerType(1);
        addFriendAnswer.setGlobalPlayerWorldInfo(Manager.playerManager.getPlayerWorldInfo(targetPlayer.getId()).toGlobalPlayerWorldInfo());
        addFriendAnswer.setLeaveMessage(LeaveMsg.makeLeaveMsg(targetPlayer,MessageString.C_FriendFirstTalk, "", messInfo.getAddFriendApproval().getApprovalPlayerId(), ChatChannel.CHATCHANNEL_ROLE).toBuildInfo());
        addFriendAnswer.setChatResSC(Manager.chatManager.deal().MakeChatResInfoBuilder(targetPlayer, approvalPlayerWorldInfo, ChatChannel.CHATCHANNEL_ROLE, 0, "", MessageString.C_FriendFirstTalk));
        g2SReqAddFriendAnswer.setAddFriendAnswer(addFriendAnswer);
        MessageUtils.send_to_social(messInfo.getAddFriendApproval().getApprovalPlayerId(), friendMessage.G2SReqAddFriendAnswer.MsgID.eMsgID_VALUE, g2SReqAddFriendAnswer.build().toByteArray());
    }

    /**
     * 审批响应
     * @param messInfo
     */
    public void S2GResAddFriendAnswer(friendMessage.S2GResAddFriendAnswer messInfo)
    {
        friendMessage.ResAddFriendSuccess.Builder addFriendSuccessMsg = friendMessage.ResAddFriendSuccess.newBuilder();
        addFriendSuccessMsg.setType(Relation.RelationType_Friend.getValue());
        friendMessage.AddFriendAnswer addFriendAnswer = messInfo.getAddFriendAnswer();
        PlayerRelation approvalPlayerRelation = Manager.friendManager.getPlayerRelation(addFriendAnswer.getApprovalPlayerId());
        //对方好友数量已达上线
        if (approvalPlayerRelation.getFriends().size() >= Global.FriendMax) {
            return;
        }
        if(approvalPlayerRelation.getFriends().containsKey(addFriendAnswer.getGlobalPlayerWorldInfo().getRoleid())){
            //好友已经存在
            return;
        }
        PlayerWorldInfo targetPlayerWorldInfo = new PlayerWorldInfo();
        targetPlayerWorldInfo.fromGlobalPlayerWorldInfo(addFriendAnswer.getGlobalPlayerWorldInfo());
        Friend friend = new Friend();
        friend.setRoleId(addFriendAnswer.getGlobalPlayerWorldInfo().getRoleid());
        friend.setInfo(targetPlayerWorldInfo);
        approvalPlayerRelation.addFriend(friend);
        // String tipMsg = "我们是好友快来聊天吧";
        //添加好友成功 默认说化
        Player approvalPlayer = PlayerManager.getInstance().getPlayer(addFriendAnswer.getApprovalPlayerId());
        //留言
        LeaveMsg leaveMsg = new LeaveMsg();
        leaveMsg.fromBuildInfo(addFriendAnswer.getLeaveMessage());
        Manager.chatManager.deal().leaveMsg(addFriendAnswer.getApprovalPlayerId(), leaveMsg);
       this.addLatelyPlayer(approvalPlayer.getId(), targetPlayerWorldInfo);
        //在线
        if(approvalPlayer != null && approvalPlayer.isOnline()){
            Manager.friendManager.deal().addFriendEvent(approvalPlayer,targetPlayerWorldInfo.getSex());
            addFriendSuccessMsg.addResultList(Manager.friendManager.deal().toCommonInfoBuilder(approvalPlayer,friend));
            MessageUtils.send_to_player(approvalPlayer, friendMessage.ResAddFriendSuccess.MsgID.eMsgID_VALUE, addFriendSuccessMsg.build().toByteArray());
            //ChatMessage.ChatResSC msg = addFriendAnswer.getChatResSC();
           // MessageUtils.send_to_player(approvalPlayer, ChatMessage.ChatResSC.MsgID.eMsgID_VALUE, msg.toByteArray());
            //添加好友自动聊天
            ChatMessage.ChatResSC.Builder  msg = ChatMessage.ChatResSC.newBuilder();
            msg.setInfo(addFriendAnswer.getChatResSC());
            MessageUtils.send_to_player(approvalPlayer, ChatMessage.ChatResSC.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }else {
            //上线延迟处理
            friend.setIsOffLineAdd(1);
        }
        Manager.friendManager.savePlayerRelation(approvalPlayerRelation);
    }

    /**
     * 跨服删除
     * @param messInfo
     */
    public void S2GResDeleteRelation(friendMessage.S2GResDeleteRelation messInfo){
        PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(messInfo.getTargetPlayerId());
        if(playerRelation == null){
            return;
        }
        if (!playerRelation.getFriends().containsKey(messInfo.getSourcePlayerId())) {
            return;
        }
        playerRelation.deleteFriend(messInfo.getSourcePlayerId());
        Player targetPlayer = Manager.playerManager.getPlayerOnline(messInfo.getTargetPlayerId());
        if(targetPlayer != null){
            friendMessage.ResDeleteRelationSuccess.Builder msg = friendMessage.ResDeleteRelationSuccess.newBuilder();
            msg.setType(Relation.RelationType_Friend.getValue());
            msg.setTargetPlayerId(messInfo.getSourcePlayerId());
            MessageUtils.send_to_player(targetPlayer, friendMessage.ResDeleteRelationSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
        Manager.friendManager.savePlayerRelation(playerRelation);
    }

    /**
     * 跨服赠送
     * @param messInfo
     */
    public void S2GReqGiveFriendShipPoint(friendMessage.S2GReqGiveFriendShipPoint messInfo){

        PlayerRelation receivePlayerRelation = Manager.friendManager.getPlayerRelation(messInfo.getFriendPlayerId());
        Friend receivePlayerFriend = receivePlayerRelation.getFriends().get(messInfo.getGivePlayerId());
        if(receivePlayerFriend == null){
            return;
        }

        //接受玩家是否在线
        Player receivePlayer = Manager.playerManager.getPlayer(messInfo.getFriendPlayerId());
        if(receivePlayer != null){

            Manager.countManager.addCount(receivePlayer, BaseCountType.NpcFriendReceiveShipPoint,receivePlayerFriend.getRoleId(), Count.RefreshType.CountType_Day,1);
            Manager.friendManager.deal().sendResFriendShipPointCommonInfo(receivePlayer,receivePlayerFriend);
        }
        Manager.friendManager.savePlayerRelation(receivePlayerRelation);
    }
}
