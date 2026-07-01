package common.community;


import com.game.community.scripts.ICommunityScript;

import com.game.manager.Manager;
import com.game.player.manager.GlobalPlayerManager;
import com.game.player.structs.*;
import com.game.script.struct.ScriptEnum;
import com.game.server.SocialServer;
import com.game.utils.MessageUtils;
import game.core.util.IDConfigUtil;
import game.core.util.StringUtils;
import game.message.CommonMessage;
import game.message.CommunityMessage;
import game.message.PlayerMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

public class CommunityScript  implements ICommunityScript {

    private static final Logger logger = LogManager.getLogger("CommunityScript");

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
    public void  G2SReqPlayerCommunityInfo(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqPlayerCommunityInfo messInfo){
        //查看社区相关玩家消息
        GlobalPlayerWorldInfo viewPlayer = GlobalPlayerManager.getInstance().getPlayers().get(messInfo.getRoleId());
        if(viewPlayer == null){
            logger.error("G2SReqPlayerCommunityInfo is player is not "+messInfo.getRoleId());
            return;
        }
        PlayerCommunityInfoSettingInfo playerCommunityInfoSettingInfo = viewPlayer.getPlayerCommunityInfoSettingInfo();
        //响应消息
        CommunityMessage.ResPlayerCommunityInfo.Builder resPlayerCommunityInfo = CommunityMessage.ResPlayerCommunityInfo.newBuilder();
        CommunityMessage.PlayerCommunityInfo.Builder playerCommunityInfo = CommunityMessage.PlayerCommunityInfo.newBuilder();
        playerCommunityInfo.setRoleId(viewPlayer.getId());
        playerCommunityInfo.setRoleName(viewPlayer.getRoleName());
        playerCommunityInfo.setRoleLv(viewPlayer.getLevel());
        playerCommunityInfo.setCareer(viewPlayer.getCareer());
        playerCommunityInfo.setFightpower(viewPlayer.getFightPower());
        //公会名称
        if(!StringUtils.isEmpty(viewPlayer.getGuildName())){
            playerCommunityInfo.setGuildName(viewPlayer.getGuildName());
        }
//        playerCommunityInfo.setFashionHead(viewPlayer.getFashionHeadId());
//        playerCommunityInfo.setFashionFrame(viewPlayer.getFashionHeadFrameId());
        //头像数据
        playerCommunityInfo.setHead(GlobalPlayerManager.getHead(viewPlayer));

        playerCommunityInfo.setServerId(viewPlayer.getServerId());
        playerCommunityInfo.setStateLv(viewPlayer.getStateVip());
         //外观
        CommonMessage.FacadeAttribute.Builder  facadeAttribute = CommonMessage.FacadeAttribute.newBuilder();
        facadeAttribute.setFashionBody(viewPlayer.getFashionBodyId());
        facadeAttribute.setFashionWeapon(viewPlayer.getFashionWeaponId());
        facadeAttribute.setFashionHalo(viewPlayer.getFashionHalo());
        facadeAttribute.setFashionMatrix(viewPlayer.getFashionMatrix());
        facadeAttribute.setWingId(viewPlayer.getWingId());
        facadeAttribute.setSpiritId(viewPlayer.getSpiritId());
        facadeAttribute.setSoulArmorId(viewPlayer.getSoulArmorId());
        playerCommunityInfo.setFacade(facadeAttribute);
        //社区消息
        CommunityMessage.PlayerCommunityInfoSettingInfo.Builder playerCommunityInfoSettingInfoBuilder = CommunityMessage.PlayerCommunityInfoSettingInfo.newBuilder();
        playerCommunityInfoSettingInfoBuilder.setDecorate(playerCommunityInfoSettingInfo.getDecorate());
        playerCommunityInfoSettingInfoBuilder.setPendan(playerCommunityInfoSettingInfo.getPendan());

        if(!StringUtils.isEmpty(playerCommunityInfoSettingInfo.getSign())){
            playerCommunityInfoSettingInfoBuilder.setSign(playerCommunityInfoSettingInfo.getSign());
        }
        if(!StringUtils.isEmpty(playerCommunityInfoSettingInfo.getBrith())){
            playerCommunityInfoSettingInfoBuilder.setBrith(playerCommunityInfoSettingInfo.getBrith());
        }

        playerCommunityInfoSettingInfoBuilder.setIsNotFriendLeaveMsg(playerCommunityInfoSettingInfo.isNotFriendLeaveMsg());
        playerCommunityInfo.setPlayerCommunityInfoSettingInfo(playerCommunityInfoSettingInfoBuilder);
        resPlayerCommunityInfo.setPlayerCommunityInfo(playerCommunityInfo);

        MessageUtils.send_to_player(player,CommunityMessage.ResPlayerCommunityInfo.MsgID.eMsgID_VALUE,resPlayerCommunityInfo.build().toByteArray());
    }

    public void G2SReqPlayerCommunityInfoSetting(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqPlayerCommunityInfoSetting messInfo){
        //设置类型 1,装饰 2,挂件 3,个性签名 4,生日 5,是否允许好友留言
        if(messInfo.getSettingType() == 1){
            player.getPlayerCommunityInfoSettingInfo().setDecorate(messInfo.getPlayerCommunityInfoSettingInfo().getDecorate());
        }else  if(messInfo.getSettingType() == 2){
            player.getPlayerCommunityInfoSettingInfo().setPendan(messInfo.getPlayerCommunityInfoSettingInfo().getPendan());
        }else  if(messInfo.getSettingType() == 3){
            player.getPlayerCommunityInfoSettingInfo().setSign(messInfo.getPlayerCommunityInfoSettingInfo().getSign());
        }else  if(messInfo.getSettingType() == 4){
            player.getPlayerCommunityInfoSettingInfo().setBrith(messInfo.getPlayerCommunityInfoSettingInfo().getBrith());
        }else  if(messInfo.getSettingType() == 5){
            player.getPlayerCommunityInfoSettingInfo().setNotFriendLeaveMsg(messInfo.getPlayerCommunityInfoSettingInfo().getIsNotFriendLeaveMsg());
        }
        Manager.globalPlayerManager.deal().save2DB(player, SaveDeal.OneMinLater);

        CommunityMessage.ResPlayerCommunityInfoSetting.Builder resPlayerCommunityInfoSetting = CommunityMessage.ResPlayerCommunityInfoSetting.newBuilder();
        resPlayerCommunityInfoSetting.setSettingType(messInfo.getSettingType());
        resPlayerCommunityInfoSetting.setIsSettingSucceed(true);
        resPlayerCommunityInfoSetting.setPlayerCommunityInfoSettingInfo(messInfo.getPlayerCommunityInfoSettingInfo().toBuilder());
        MessageUtils.send_to_player(player,CommunityMessage.ResPlayerCommunityInfoSetting.MsgID.eMsgID_VALUE,resPlayerCommunityInfoSetting.build().toByteArray());
    }


    /**
     * 请求留言列表信息
     * @param player
     * @param messInfo
     */
    public void G2SReqCommunityLeaveMessage(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqCommunityLeaveMessage messInfo){
        //返回最新列表
        this.sendResCommunityLeaveMessage(player,messInfo.getRoleId());
    }

    /**
     * 添加留言
     * @param player
     * @param messInfo
     */
    public void G2SReqAddCommunityLeaveMessage(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqAddCommunityLeaveMessage messInfo){

        //目标玩家
        GlobalPlayerWorldInfo targetGlobalPlayerWorldInfo = GlobalPlayerManager.getInstance().getPlayers().get(messInfo.getRoleId());
        if(targetGlobalPlayerWorldInfo == null){
            return;
        }
        //判断是否自己
        if(player.getId() != targetGlobalPlayerWorldInfo.getId()){
            //是否非好友不能留言
            if(!targetGlobalPlayerWorldInfo.getPlayerCommunityInfoSettingInfo().isNotFriendLeaveMsg()){
                //判断是否是好友
                if(!messInfo.getIsFriend())
                {
                    return;
                }
            }
        }

        List<CommunityLeaveMessageInfo> list = targetGlobalPlayerWorldInfo.getCommcunityLeaveMessageInfoList();
        if(list.size()>=50){
            //删除第一个
            list.remove(0);
        }
        //添加留言
        CommunityLeaveMessageInfo communityLeaveMessageInfo = new CommunityLeaveMessageInfo();
        communityLeaveMessageInfo.setRoleId(player.getId());
        communityLeaveMessageInfo.setCondition(messInfo.getCondition());
        communityLeaveMessageInfo.setLeaveMessageId(IDConfigUtil.getId());
        communityLeaveMessageInfo.setTime(System.currentTimeMillis());
        list.add(communityLeaveMessageInfo);
        //返回最新列表
        this.sendResCommunityLeaveMessage(player,messInfo.getRoleId());
    }

    /**
     * @param player
     */
    private void sendResCommunityLeaveMessage(GlobalPlayerWorldInfo player,long targetRoleId){
        CommunityMessage.ResCommunityLeaveMessage.Builder resCommunityLeaveMessage = CommunityMessage.ResCommunityLeaveMessage.newBuilder();
        GlobalPlayerWorldInfo targetGlobalPlayerWorldInfo = GlobalPlayerManager.getInstance().getPlayers().get(targetRoleId);
        if(targetGlobalPlayerWorldInfo != null){
            List<CommunityLeaveMessageInfo> targetLeaveMessageList = targetGlobalPlayerWorldInfo.getCommcunityLeaveMessageInfoList();
            for(int i = 0;i<targetLeaveMessageList.size();i++){
                CommunityLeaveMessageInfo communityLeaveMessageInfo = targetLeaveMessageList.get(i);
                GlobalPlayerWorldInfo leaveGlobalPlayerWorldInfo = GlobalPlayerManager.getInstance().getPlayers().get(communityLeaveMessageInfo.getRoleId());
                if (leaveGlobalPlayerWorldInfo == null) { //查找玩家信息
                    continue;
                }
                CommunityMessage.CommunityLeaveMessageInfo.Builder communityLeaveMessageInfoBuilder = CommunityMessage.CommunityLeaveMessageInfo.newBuilder();
                communityLeaveMessageInfoBuilder.setLeaveMessageId(communityLeaveMessageInfo.getLeaveMessageId());
                communityLeaveMessageInfoBuilder.setChatername(leaveGlobalPlayerWorldInfo.getRoleName());
                communityLeaveMessageInfoBuilder.setCondition(communityLeaveMessageInfo.getCondition());
                communityLeaveMessageInfoBuilder.setTime((int)(communityLeaveMessageInfo.getTime()/1000));
                communityLeaveMessageInfoBuilder.setLevel(leaveGlobalPlayerWorldInfo.getLevel());
//            communityLeaveMessageInfoBuilder.setHeadId(targetGlobalPlayerWorldInfo.getFashionHeadId());
//            communityLeaveMessageInfoBuilder.setHeadFrameId(targetGlobalPlayerWorldInfo.getFashionHeadFrameId());
                communityLeaveMessageInfoBuilder.setHead(GlobalPlayerManager.getHead(leaveGlobalPlayerWorldInfo));
                communityLeaveMessageInfoBuilder.setChaterSid(leaveGlobalPlayerWorldInfo.getServerId());
                communityLeaveMessageInfoBuilder.setCareer(leaveGlobalPlayerWorldInfo.getCareer());
                communityLeaveMessageInfoBuilder.setRoleId(leaveGlobalPlayerWorldInfo.getId());
                resCommunityLeaveMessage.addCommunityLeaveMessageInfoList(communityLeaveMessageInfoBuilder);
            }
        }
        MessageUtils.send_to_player(player,CommunityMessage.ResCommunityLeaveMessage.MsgID.eMsgID_VALUE,resCommunityLeaveMessage.build().toByteArray());
    }

    /**
     * 删除留言
     * @param player
     * @param messInfo
     */
    public void G2SReqDeleteCommunityLeaveMessage(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqDeleteCommunityLeaveMessage messInfo){
        GlobalPlayerWorldInfo targetGlobalPlayerWorldInfo = GlobalPlayerManager.getInstance().getPlayers().get(messInfo.getRoleId());
        if(targetGlobalPlayerWorldInfo != null){
            List<CommunityLeaveMessageInfo> list = targetGlobalPlayerWorldInfo.getCommcunityLeaveMessageInfoList();
            for(int i = 0;i<list.size();i++){
                //不能删除别人的留言信息
                if(list.get(i).getLeaveMessageId() == messInfo.getLeaveMessageId()){
                    if(player.getId() != messInfo.getRoleId()){
                        if(list.get(i).getRoleId() != player.getId()){
                            break;
                        }
                    }

                    list.remove(i);
                    break;
                }
            }
        }
        //返回最新列表
        this.sendResCommunityLeaveMessage(player,messInfo.getRoleId());
    }


    /**
     * 发送朋友圈
     * @param player
     * @param messInfo
     */
    public void G2SReqSendFriendCircle(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqSendFriendCircle messInfo){
        List<FriendCircleInfo> friendCircleInfoList = player.getFriendCircleInfoList();
        if(friendCircleInfoList.size()>=50){
            //删除第一个
            friendCircleInfoList.remove(0);
        }
        FriendCircleInfo friendCircleInfo = new FriendCircleInfo();
        friendCircleInfo.setFriendCircleId(IDConfigUtil.getId());
        friendCircleInfo.setCondition(messInfo.getCondition());
        friendCircleInfo.setTime(System.currentTimeMillis());
        friendCircleInfoList.add(friendCircleInfo);

        CommunityMessage.ResFriendCircleList.Builder resFriendCircleList = CommunityMessage.ResFriendCircleList.newBuilder();
        resFriendCircleList.setType(2);
        CommunityMessage.FriendCircleInfo.Builder friendCircleInfoBuilder = CommunityMessage.FriendCircleInfo.newBuilder();
        friendCircleInfoBuilder.setFriendCircleId(friendCircleInfo.getFriendCircleId());
        friendCircleInfoBuilder.setCondition(friendCircleInfo.getCondition());
        resFriendCircleList.addFriendCircleInfo(friendCircleInfoBuilder);
        MessageUtils.send_to_player(player,CommunityMessage.ResFriendCircleList.MsgID.eMsgID_VALUE,resFriendCircleList.build().toByteArray());

    }

    /**
     * 请求朋友圈数据
     * @param player
     * @param messInfo
     */
    public void G2SReqFriendCircle(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqFriendCircle messInfo){
        CommunityMessage.ResFriendCircleList.Builder resFriendCircleList = CommunityMessage.ResFriendCircleList.newBuilder();
        resFriendCircleList.setType(1);
        GlobalPlayerWorldInfo targetGlobalPlayerWorldInfo = GlobalPlayerManager.getInstance().getPlayers().get(messInfo.getRoleId());
        if(targetGlobalPlayerWorldInfo != null){
            List<FriendCircleInfo> friendCircleInfoList = targetGlobalPlayerWorldInfo.getFriendCircleInfoList();
            if(friendCircleInfoList!=null && friendCircleInfoList.size()>0){
                for(int i = 0;i<friendCircleInfoList.size();i++){
                    FriendCircleInfo friendCircleInfo = friendCircleInfoList.get(i);

                    CommunityMessage.FriendCircleInfo.Builder friendCircleInfoBuilder = CommunityMessage.FriendCircleInfo.newBuilder();
                    friendCircleInfoBuilder.setFriendCircleId(friendCircleInfo.getFriendCircleId());
                    friendCircleInfoBuilder.setCondition(friendCircleInfo.getCondition());

                    if(friendCircleInfo.getFriendCircleCommentInfoList() != null && friendCircleInfo.getFriendCircleCommentInfoList().size()>0){
                        for(int j = 0;j<friendCircleInfo.getFriendCircleCommentInfoList().size();j++){
                            CommunityMessage.FriendCircleLeaveMessageInfo.Builder friendCircleLeaveMessageInfoBuilder = CommunityMessage.FriendCircleLeaveMessageInfo.newBuilder();
                            friendCircleLeaveMessageInfoBuilder.setChatername(friendCircleInfo.getFriendCircleCommentInfoList().get(j).getChatername());
                            friendCircleLeaveMessageInfoBuilder.setCondition(friendCircleInfo.getFriendCircleCommentInfoList().get(j).getCondition());
                            friendCircleInfoBuilder.addFriendCircleLeaveMessageInfo(friendCircleLeaveMessageInfoBuilder);
                        }
                    }

                    resFriendCircleList.addFriendCircleInfo(friendCircleInfoBuilder);
                }
            }
        }
        MessageUtils.send_to_player(player,CommunityMessage.ResFriendCircleList.MsgID.eMsgID_VALUE,resFriendCircleList.build().toByteArray());
    }

    /**
     * 删除朋友圈
     * @param player
     * @param messInfo
     */
    public void G2SReqDeleteFriendCircle(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqDeleteFriendCircle messInfo){
        List<FriendCircleInfo> friendCircleInfoList = player.getFriendCircleInfoList();
        if(friendCircleInfoList!=null && friendCircleInfoList.size()>0){
            for(int i = 0;i<friendCircleInfoList.size();i++){
                if(friendCircleInfoList.get(i).getFriendCircleId() == messInfo.getFriendCircleId()){
                    friendCircleInfoList.remove(i);
                    break;
                }
            }
        }
        CommunityMessage.ResFriendCircleList.Builder resFriendCircleList = CommunityMessage.ResFriendCircleList.newBuilder();
        resFriendCircleList.setType(3);
        CommunityMessage.FriendCircleInfo.Builder friendCircleInfoBuilder = CommunityMessage.FriendCircleInfo.newBuilder();
        friendCircleInfoBuilder.setFriendCircleId(messInfo.getFriendCircleId());
        resFriendCircleList.addFriendCircleInfo(friendCircleInfoBuilder);
        MessageUtils.send_to_player(player,CommunityMessage.ResFriendCircleList.MsgID.eMsgID_VALUE,resFriendCircleList.build().toByteArray());
    }
    /**
     * 评论朋友圈
     * @param player
     * @param messInfo
     */
    public void G2SReqCommentFriendCircle(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqCommentFriendCircle messInfo){
        GlobalPlayerWorldInfo targetGlobalPlayerWorldInfo = GlobalPlayerManager.getInstance().getPlayers().get(messInfo.getTargetRoleId());
        if(targetGlobalPlayerWorldInfo == null){
            return;
        }
        List<FriendCircleInfo> friendCircleInfoList = targetGlobalPlayerWorldInfo.getFriendCircleInfoList();
        if(friendCircleInfoList == null || friendCircleInfoList.size()==0){
            return;
        }
        FriendCircleInfo friendCircleInfo = null;
        for(int i = 0;i<friendCircleInfoList.size();i++){
            if(friendCircleInfoList.get(i).getFriendCircleId() == messInfo.getFriendCircleId()){
                friendCircleInfo = friendCircleInfoList.get(i);
                break;
            }
        }
        if(friendCircleInfo == null){
            return;
        }
        FriendCircleCommentInfo friendCircleCommentInfo = new FriendCircleCommentInfo();
        friendCircleCommentInfo.setChatername(player.getRoleName());
        friendCircleCommentInfo.setCondition(messInfo.getCommentCondition());
        friendCircleInfo.getFriendCircleCommentInfoList().add(friendCircleCommentInfo);


        CommunityMessage.ResFriendCircleList.Builder resFriendCircleList = CommunityMessage.ResFriendCircleList.newBuilder();
        resFriendCircleList.setType(4);
        CommunityMessage.FriendCircleInfo.Builder friendCircleInfoBuilder = CommunityMessage.FriendCircleInfo.newBuilder();
        friendCircleInfoBuilder.setFriendCircleId(friendCircleInfo.getFriendCircleId());
        friendCircleInfoBuilder.setCondition(friendCircleInfo.getCondition());


        for(int j = 0;j<friendCircleInfo.getFriendCircleCommentInfoList().size();j++){
            CommunityMessage.FriendCircleLeaveMessageInfo.Builder friendCircleLeaveMessageInfoBuilder = CommunityMessage.FriendCircleLeaveMessageInfo.newBuilder();
            friendCircleLeaveMessageInfoBuilder.setChatername(friendCircleInfo.getFriendCircleCommentInfoList().get(j).getChatername());
            friendCircleLeaveMessageInfoBuilder.setCondition(friendCircleInfo.getFriendCircleCommentInfoList().get(j).getCondition());
            friendCircleInfoBuilder.addFriendCircleLeaveMessageInfo(friendCircleLeaveMessageInfoBuilder);
        }

        resFriendCircleList.addFriendCircleInfo(friendCircleInfoBuilder);
        MessageUtils.send_to_player(player,CommunityMessage.ResFriendCircleList.MsgID.eMsgID_VALUE,resFriendCircleList.build().toByteArray());
    }
}
