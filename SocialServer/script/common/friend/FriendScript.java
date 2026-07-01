package common.friend;

import com.data.Global;
import com.data.MessageString;
import com.game.chat.structs.Notify;
import com.game.friend.script.IFriendScript;
import com.game.friend.struct.*;
import com.game.manager.Manager;
import com.game.player.manager.GlobalPlayerManager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import com.game.script.struct.ScriptEnum;
import com.game.server.SocialServer;
import com.game.server.struct.ServerInfo;
import com.game.utils.MessageUtils;
import game.core.message.SMessage;
import game.core.net.Config.ServerConfig;
import game.message.friendMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 好友处理接口处理
 *
 * @author gongshengjun
 */
public class FriendScript implements IFriendScript {

    private static final Logger logger = LogManager.getLogger("FriendScript");

    @Override
    public int getId() {
        return ScriptEnum.FriendBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


public void G2SReqDeleteRelationHandler(GlobalPlayerWorldInfo player, friendMessage.G2SReqDeleteRelation messInfo) {
    //查找加好友在线信息
    GlobalPlayerWorldInfo targetGlobalPlayerWorldInfo = GlobalPlayerManager.getInstance().getPlayers().get(messInfo.getTargetPlayerId());
    if (targetGlobalPlayerWorldInfo == null) { //离线
        return;
    }else{
        //发送给目标服务器
        friendMessage.S2GResDeleteRelation.Builder s2GResDeleteRelation = friendMessage.S2GResDeleteRelation.newBuilder();
        s2GResDeleteRelation.setTargetPlayerId(messInfo.getTargetPlayerId());
        s2GResDeleteRelation.setType(messInfo.getType());
        s2GResDeleteRelation.setSourcePlayerId(messInfo.getSourcePlayerId());
        MessageUtils.send_to_server(targetGlobalPlayerWorldInfo.getPlat(),targetGlobalPlayerWorldInfo.getServerId(),friendMessage.S2GResDeleteRelation.MsgID.eMsgID_VALUE,s2GResDeleteRelation.build().toByteArray());
    }
}
    /**
     * 跨服添加好友
     *
     * @param player
     * @param messInfo
     */
    public void G2SReqAddRelationHandler(GlobalPlayerWorldInfo player, friendMessage.G2SReqAddRelation messInfo) {
        //查找加好友在线信息
        GlobalPlayerWorldInfo targetGlobalPlayerWorldInfo = GlobalPlayerManager.getInstance().getPlayers().get(messInfo.getTargetPlayerId());
        if (targetGlobalPlayerWorldInfo == null) { //离线
            return;
        }else{
            //发送给目标服务器
            friendMessage.S2GResAddRelation.Builder s2GResAddRelation = friendMessage.S2GResAddRelation.newBuilder();
            s2GResAddRelation.setTargetPlayerId(messInfo.getTargetPlayerId());
            s2GResAddRelation.setType(messInfo.getType());
            s2GResAddRelation.setTargetServerId(messInfo.getTargetServerId());
            s2GResAddRelation.setSourceApprovalPlayerInfo(messInfo.getSourceApprovalPlayerInfo());
            MessageUtils.send_to_server(targetGlobalPlayerWorldInfo.getPlat(),messInfo.getTargetServerId(),friendMessage.S2GResAddRelation.MsgID.eMsgID_VALUE,s2GResAddRelation.build().toByteArray());
        }
    }

    /**
     * 跨服审批
     *
     * @param messInfo
     */
    public void G2SReqAddFriendApproval(friendMessage.G2SReqAddFriendApproval messInfo) {
        //查找加好友在线信息
        GlobalPlayerWorldInfo targetGlobalPlayerWorldInfo = GlobalPlayerManager.getInstance().getPlayers().get(messInfo.getAddFriendApproval().getTargetPlayerId());
        if (targetGlobalPlayerWorldInfo == null) { //查找玩家信息 
            return;
        }
         //发送给目标服务器
        friendMessage.S2GResAddFriendApproval.Builder s2GResAddFriendApproval = friendMessage.S2GResAddFriendApproval.newBuilder();
        s2GResAddFriendApproval.setAddFriendApproval(messInfo.getAddFriendApproval());
        SMessage message = new SMessage(friendMessage.S2GResAddFriendApproval.MsgID.eMsgID_VALUE, s2GResAddFriendApproval.build().toByteArray());
        message.setSender(ServerConfig.getServerId());
        String key = targetGlobalPlayerWorldInfo.getPlat() + "_"+ messInfo.getAddFriendApproval().getTargetServerId();
        ServerInfo server = Manager.serverManager.getServers().get(key);
        if(server == null || server.getSession() == null){
            logger.error("跨服审批目标服务器为空");
            return;
        }
        SocialServer.getInstance().sendExc.publishEvent(server.getSession(), message);
    }
    /**
     * 跨服审批响应
     *
     * @param messInfo
     */
    public void G2SReqAddFriendAnswer(friendMessage.G2SReqAddFriendAnswer messInfo) {

        //发送给目标服务器
        friendMessage.S2GResAddFriendAnswer.Builder s2GResAddFriendAnswer = friendMessage.S2GResAddFriendAnswer.newBuilder();
        s2GResAddFriendAnswer.setAddFriendAnswer(messInfo.getAddFriendAnswer());
        SMessage message = new SMessage(friendMessage.S2GResAddFriendAnswer.MsgID.eMsgID_VALUE, s2GResAddFriendAnswer.build().toByteArray());
        message.setSender(ServerConfig.getServerId());
        String key = messInfo.getAddFriendAnswer().getApprovalTargetPlat() +"_"+ messInfo.getAddFriendAnswer().getApprovalServerId();
        ServerInfo server = Manager.serverManager.getServers().get(key);
        if(server == null || server.getSession() == null){
            logger.error("跨服审批响应目标服务器为空");
            return;
        }
        SocialServer.getInstance().sendExc.publishEvent(server.getSession(), message);
    }

    /**
     * 跨服赠送
     * @param messInfo
     */
    public void G2SReqGiveFriendShipPoint(friendMessage.G2SReqGiveFriendShipPoint messInfo){
        //查找加好友在线信息
        GlobalPlayerWorldInfo targetGlobalPlayerWorldInfo = GlobalPlayerManager.getInstance().getPlayers().get(messInfo.getFriendPlayerId());
        if (targetGlobalPlayerWorldInfo == null) { //查找玩家信息
            return;
        }
        //发送给目标服务器
        friendMessage.S2GReqGiveFriendShipPoint.Builder s2GReqGiveFriendShipPoint = friendMessage.S2GReqGiveFriendShipPoint.newBuilder();
        s2GReqGiveFriendShipPoint.setFriendPlayerId(messInfo.getFriendPlayerId());
        s2GReqGiveFriendShipPoint.setGivePlayerId(messInfo.getGivePlayerId());
        MessageUtils.send_to_server(targetGlobalPlayerWorldInfo.getPlat(),targetGlobalPlayerWorldInfo.getServerId(),friendMessage.S2GReqGiveFriendShipPoint.MsgID.eMsgID_VALUE,s2GReqGiveFriendShipPoint.build().toByteArray());
    }

}
