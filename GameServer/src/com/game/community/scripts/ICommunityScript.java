package com.game.community.scripts;


import com.game.player.structs.Player;
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
    void reqPlayerCommunityInfo(Player player, CommunityMessage.ReqPlayerCommunityInfo messInfo);

    /**
     * 个人信息设置
     * @param player
     * @param messInfo
     */
    void reqPlayerCommunityInfoSetting(Player player, CommunityMessage.ReqPlayerCommunityInfoSetting messInfo);

    /**
     * 请求留言列表信息
     * @param player
     * @param messInfo
     */
    void reqCommunityLeaveMessage(Player player, CommunityMessage.ReqCommunityLeaveMessage messInfo);

    /**
     * 添加留言
     * @param player
     * @param messInfo
     */
    void reqAddCommunityLeaveMessage(Player player, CommunityMessage.ReqAddCommunityLeaveMessage messInfo);

    /**
     * 删除留言
     * @param player
     * @param messInfo
     */
    void reqDeleteCommunityLeaveMessage(Player player, CommunityMessage.ReqDeleteCommunityLeaveMessage messInfo);

    /**
     * 发送朋友圈
     * @param player
     * @param messInfo
     */
    void reqSendFriendCircle(Player player, CommunityMessage.ReqSendFriendCircle messInfo);

    /**
     * 请求朋友圈
     * @param player
     * @param messInfo
     */
    void reqFriendCircle(Player player, CommunityMessage.ReqFriendCircle messInfo);

    /**
     * 请求删除朋友圈
     * @param player
     * @param messInfo
     */
    void reqDeleteFriendCircle(Player player, CommunityMessage.ReqDeleteFriendCircle messInfo);

    /**
     * 评论朋友圈
     * @param player
     * @param messInfo
     */
    void reqCommentFriendCircle(Player player, CommunityMessage.ReqCommentFriendCircle messInfo);
}
