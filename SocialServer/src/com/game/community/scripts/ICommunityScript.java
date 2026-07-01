package com.game.community.scripts;


import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.script.IScript;
import game.message.CommunityMessage;

/**
 * 社区脚本的接口
 *
 * @author
 */
public interface ICommunityScript extends IScript {
    /**
     * 请求获取玩家得社区信息
     * @param player
     * @param messInfo
     */
    void G2SReqPlayerCommunityInfo(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqPlayerCommunityInfo messInfo);

    /**
     * 设置家园信息
     * @param player
     * @param messInfo
     */
    void G2SReqPlayerCommunityInfoSetting(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqPlayerCommunityInfoSetting messInfo);
    /**
     * 请求留言列表信息
     * @param player
     * @param messInfo
     */
     void G2SReqCommunityLeaveMessage(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqCommunityLeaveMessage messInfo);
    /**
     * 添加留言
     * @param player
     * @param messInfo
     */
     void G2SReqAddCommunityLeaveMessage(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqAddCommunityLeaveMessage messInfo);
    /**
     * 删除留言
     * @param player
     * @param messInfo
     */
     void G2SReqDeleteCommunityLeaveMessage(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqDeleteCommunityLeaveMessage messInfo);

    /**
     * 发送朋友圈
     * @param messInfo
     */
     void G2SReqSendFriendCircle(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqSendFriendCircle messInfo);

    /**
     * 请求朋友圈数据
     * @param player
     * @param messInfo
     */
     void G2SReqFriendCircle(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqFriendCircle messInfo);

    /**
     * 删除朋友圈
     * @param player
     * @param messInfo
     */
     void G2SReqDeleteFriendCircle(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqDeleteFriendCircle messInfo);

    /**
     * 评论朋友圈
     * @param player
     * @param messInfo
     */
     void G2SReqCommentFriendCircle(GlobalPlayerWorldInfo player, CommunityMessage.G2SReqCommentFriendCircle messInfo);
}
