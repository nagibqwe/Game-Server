/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.chat.script;

import com.game.chat.structs.ChatChannel;
import com.game.chat.structs.LeaveMsg;
import com.game.guild.structs.Guild;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import game.core.script.IScript;
import game.message.ChatMessage;
import game.message.ChatMessage.*;
import game.message.CrossServerMessage;

/**
 * 聊天的接口
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface IChatScript extends IScript {

    void OnChatGetContentCS(Player player, ChatGetContentCS messInfo);

    void OnChatReqCS(Player player, ChatReqCS messInfo);

    void onRedAddChatRoom(Player player, ReqAddChatRooM mess);

    void onReqChatRoom(Player player, ReqChatRoom mess);

    void onReqExitChatRoom(Player player, ReqExitChatRoom mess);

    boolean inChatRoom(long id, long roleId);

    void playerLogout(Player player);

    void onP2GResChatMess(CrossServerMessage.P2GResChatMess messInfo);

    /**
     * 上线发送留言数据
     */
    void sendLevelMsg(Player player);

    /**
     * 上线发送世界频道历史消息
     */
    void sendWorldLevelMsg(Player player);

    /**
     * 通知
     * @param messInfo
     */
    void F2GameServerNotice(F2GameServerNotice messInfo);

    void P2GameServerNotice(P2GameServerNotice messInfo);

    /**
     * 离线消息
     * @param recvPlayerId
     * @param msg
     */
    void leaveMsg(long recvPlayerId, LeaveMsg msg);

    /**
     * 检测多媒体
     */
    void tickMultiMedia();

    /**
     * 发送消息到系统频道
     * @param player
     * @param target
     * @param channel
     * @param type
     * @param str
     * @param messageStr
     */
    void sendChatMessage(Player player, PlayerWorldInfo target, ChatChannel channel, int type, String str, int messageStr);

    /**
     * 发送屏蔽字
     * @param player
     */
    void sendForbidWord(Player player);

    /**
     * 组装聊天消息
     * @param sender
     * @param recv
     * @param channel
     * @param type
     * @param str
     * @param messageStr
     * @return
     */
    ChatMessage.ChatResInfo.Builder MakeChatResInfoBuilder(Player sender, PlayerWorldInfo recv, ChatChannel channel, int type, String str, int messageStr);

}
