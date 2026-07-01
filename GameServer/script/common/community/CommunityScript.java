package common.community;


import com.data.MessageString;
import com.game.chat.structs.Notify;
import com.game.community.scripts.ICommunityScript;
import com.game.friend.struct.PlayerRelation;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.server.social.SocialServerClient;
import com.game.utils.MessageUtils;
import game.message.CommunityMessage;
import game.message.PlayerMessage;

public class CommunityScript  implements ICommunityScript {

    @Override
    public int getId() {
        return ScriptEnum.CommunityScript;
    }
    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 请求获取玩家得社区信息
     * @param player
     * @param messInfo
     */
    public void reqPlayerCommunityInfo(Player player, CommunityMessage.ReqPlayerCommunityInfo messInfo){

        if (SocialServerClient.getInstance().channel == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.PublicServer_Close);
            return ;
        }

        //同步社交服务器
        PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo(messInfo.getRoleId());
        if(playerWorldInfo !=null){
            PlayerMessage.G2SSynPlayerSocialInfo.Builder g2SSynPlayerSocialInfo = PlayerMessage.G2SSynPlayerSocialInfo.newBuilder();
            g2SSynPlayerSocialInfo.setGlobalPlayerWorldInfo(playerWorldInfo.toGlobalPlayerWorldInfo());
            g2SSynPlayerSocialInfo.setType(2);
            MessageUtils.send_to_social(PlayerMessage.G2SSynPlayerSocialInfo.MsgID.eMsgID_VALUE, g2SSynPlayerSocialInfo.build().toByteArray());
        }
        CommunityMessage.G2SReqPlayerCommunityInfo.Builder g2SReqPlayerCommunityInfo = CommunityMessage.G2SReqPlayerCommunityInfo.newBuilder();
        g2SReqPlayerCommunityInfo.setRoleId(messInfo.getRoleId());
        MessageUtils.send_to_social(player.getId(), CommunityMessage.G2SReqPlayerCommunityInfo.MsgID.eMsgID_VALUE, g2SReqPlayerCommunityInfo.build().toByteArray());
    }

    /**
     * 个人信息设置
     * @param player
     * @param messInfo
     */
    public void reqPlayerCommunityInfoSetting(Player player, CommunityMessage.ReqPlayerCommunityInfoSetting messInfo){
        CommunityMessage.G2SReqPlayerCommunityInfoSetting.Builder g2SReqPlayerCommunityInfoSetting = CommunityMessage.G2SReqPlayerCommunityInfoSetting.newBuilder();
        g2SReqPlayerCommunityInfoSetting.setSettingType(messInfo.getSettingType());
        g2SReqPlayerCommunityInfoSetting.setPlayerCommunityInfoSettingInfo(messInfo.getPlayerCommunityInfoSettingInfo());
        MessageUtils.send_to_social(player.getId(), CommunityMessage.G2SReqPlayerCommunityInfoSetting.MsgID.eMsgID_VALUE, g2SReqPlayerCommunityInfoSetting.build().toByteArray());
    }
    /**
     * 请求留言列表信息
     * @param player
     * @param messInfo
     */
    public void reqCommunityLeaveMessage(Player player, CommunityMessage.ReqCommunityLeaveMessage messInfo){
        //同步社交服务器
        PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo(messInfo.getRoleId());
        if(playerWorldInfo !=null){
            PlayerMessage.G2SSynPlayerSocialInfo.Builder g2SSynPlayerSocialInfo = PlayerMessage.G2SSynPlayerSocialInfo.newBuilder();
            g2SSynPlayerSocialInfo.setGlobalPlayerWorldInfo(playerWorldInfo.toGlobalPlayerWorldInfo());
            g2SSynPlayerSocialInfo.setType(2);
            MessageUtils.send_to_social(PlayerMessage.G2SSynPlayerSocialInfo.MsgID.eMsgID_VALUE, g2SSynPlayerSocialInfo.build().toByteArray());
        }
        CommunityMessage.G2SReqCommunityLeaveMessage.Builder g2SReqCommunityLeaveMessage = CommunityMessage.G2SReqCommunityLeaveMessage.newBuilder();
        g2SReqCommunityLeaveMessage.setRoleId(messInfo.getRoleId());
        MessageUtils.send_to_social(player.getId(), CommunityMessage.G2SReqCommunityLeaveMessage.MsgID.eMsgID_VALUE, g2SReqCommunityLeaveMessage.build().toByteArray());
    }

    /**
     * 添加留言
     * @param player
     * @param messInfo
     */
    public void reqAddCommunityLeaveMessage(Player player, CommunityMessage.ReqAddCommunityLeaveMessage messInfo){

        CommunityMessage.G2SReqAddCommunityLeaveMessage.Builder g2SReqAddCommunityLeaveMessage = CommunityMessage.G2SReqAddCommunityLeaveMessage.newBuilder();
        //判断是否为好友
        PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(player.getId());
        if(playerRelation !=null && playerRelation.getFriends()!=null && playerRelation.getFriends().containsKey(messInfo.getRoleId())){
            g2SReqAddCommunityLeaveMessage.setIsFriend(true);
        }else{
            g2SReqAddCommunityLeaveMessage.setIsFriend(false);
        }
        g2SReqAddCommunityLeaveMessage.setRoleId(messInfo.getRoleId());
        g2SReqAddCommunityLeaveMessage.setCondition(messInfo.getCondition());
        MessageUtils.send_to_social(player.getId(), CommunityMessage.G2SReqAddCommunityLeaveMessage.MsgID.eMsgID_VALUE, g2SReqAddCommunityLeaveMessage.build().toByteArray());
    }

    /**
     * 删除留言
     * @param player
     * @param messInfo
     */
    public void reqDeleteCommunityLeaveMessage(Player player, CommunityMessage.ReqDeleteCommunityLeaveMessage messInfo){
        CommunityMessage.G2SReqDeleteCommunityLeaveMessage.Builder g2SReqDeleteCommunityLeaveMessage = CommunityMessage.G2SReqDeleteCommunityLeaveMessage.newBuilder();
        g2SReqDeleteCommunityLeaveMessage.setRoleId(messInfo.getRoleId());
        g2SReqDeleteCommunityLeaveMessage.setLeaveMessageId(messInfo.getLeaveMessageId());
        MessageUtils.send_to_social(player.getId(), CommunityMessage.G2SReqDeleteCommunityLeaveMessage.MsgID.eMsgID_VALUE, g2SReqDeleteCommunityLeaveMessage.build().toByteArray());
    }


    //朋友圈相关
    public void reqSendFriendCircle(Player player, CommunityMessage.ReqSendFriendCircle messInfo){
        CommunityMessage.G2SReqSendFriendCircle.Builder g2SReqSendFriendCircle = CommunityMessage.G2SReqSendFriendCircle.newBuilder();
        g2SReqSendFriendCircle.setRoleId(messInfo.getRoleId());
        g2SReqSendFriendCircle.setCondition(messInfo.getCondition());
        MessageUtils.send_to_social(player.getId(), CommunityMessage.G2SReqSendFriendCircle.MsgID.eMsgID_VALUE, g2SReqSendFriendCircle.build().toByteArray());
    }
    public void reqFriendCircle(Player player, CommunityMessage.ReqFriendCircle messInfo){
        CommunityMessage.G2SReqFriendCircle.Builder g2SReqFrgiendCircle = CommunityMessage.G2SReqFriendCircle.newBuilder();
        g2SReqFrgiendCircle.setRoleId(messInfo.getRoleId());
        MessageUtils.send_to_social(player.getId(), CommunityMessage.G2SReqFriendCircle.MsgID.eMsgID_VALUE, g2SReqFrgiendCircle.build().toByteArray());
    }

    public void reqDeleteFriendCircle(Player player, CommunityMessage.ReqDeleteFriendCircle messInfo){
        CommunityMessage.G2SReqDeleteFriendCircle.Builder g2SReqDeleteFriendCircle = CommunityMessage.G2SReqDeleteFriendCircle.newBuilder();
        g2SReqDeleteFriendCircle.setFriendCircleId(messInfo.getFriendCircleId());
        g2SReqDeleteFriendCircle.setRoleId(player.getId());
        MessageUtils.send_to_social(player.getId(), CommunityMessage.G2SReqDeleteFriendCircle.MsgID.eMsgID_VALUE, g2SReqDeleteFriendCircle.build().toByteArray());
    }

    public void reqCommentFriendCircle(Player player, CommunityMessage.ReqCommentFriendCircle messInfo){
        CommunityMessage.G2SReqCommentFriendCircle.Builder g2SReqCommentFriendCircle = CommunityMessage.G2SReqCommentFriendCircle.newBuilder();
        g2SReqCommentFriendCircle.setFriendCircleId(messInfo.getFriendCircleId());
        g2SReqCommentFriendCircle.setTargetRoleId(messInfo.getTargetRoleId());
        g2SReqCommentFriendCircle.setCommentCondition(messInfo.getCommentCondition());
        MessageUtils.send_to_social(player.getId(), CommunityMessage.G2SReqCommentFriendCircle.MsgID.eMsgID_VALUE, g2SReqCommentFriendCircle.build().toByteArray());
    }
}
