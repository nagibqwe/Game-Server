package com.game.utils;

import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import com.game.server.SocialServer;
import com.game.server.struct.ServerInfo;
import game.core.message.SMessage;
import game.core.net.Config.ServerConfig;
import game.message.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/6/9 15:31
 * @Auth ZUncle
 */
public class MessageUtils {

    static final Logger logger = LogManager.getLogger(MessageUtils.class);

    /**
     * 发送玩家消息
     *
     * @param player
     * @param messId
     * @param data
     */
    public static void send_to_player(GlobalPlayerWorldInfo player, int messId, byte[] data) {
        String key = player.getPlat() + "_" + player.getServerId();
        ServerInfo server = Manager.serverManager.getServers().get(key);
        if (server == null) {
            logger.warn("key={}", key);
        }
        send_to_player(server.getSession(), player.getId(), messId, data);
    }

    /**
     * 发送玩家消息
     *
     * @param plat
     * @param messId
     * @param data
     */
    public static void send_to_player(long playerId, String plat, int serverId, int messId, byte[] data) {
        String key = plat + "_" + serverId;
        ServerInfo server = Manager.serverManager.getServers().get(key);
        send_to_player(server.getSession(), playerId, messId, data);
    }

    /**
     * 发送玩家消息
     *
     * @param channel
     * @param roleId
     * @param messId
     * @param data
     */
    public static void send_to_player(ChannelHandlerContext channel, long roleId, int messId, byte[] data) {
        List<Long> recv = new ArrayList<>();
        recv.add(roleId);
        send_to_player(channel, recv, messId, data);
    }

    /**
     * 发送玩家消息
     *
     * @param channel
     * @param roleList
     * @param messId
     * @param data
     */
    public static void send_to_player(ChannelHandlerContext channel, List<Long> roleList, int messId, byte[] data) {
        SMessage message = new SMessage(messId, data);
        message.setSender(ServerConfig.getServerId());
        message.setRecv(roleList);
        SocialServer.getInstance().sendExc.publishEvent(channel, message);
    }

    /**
     * 发送给Other服务器
     *
     * @param channel
     * @param messId
     * @param data
     */
    public static void send_to_server(ChannelHandlerContext channel, int messId, byte[] data) {
        SMessage message = new SMessage(messId, data);
        message.setSender(ServerConfig.getServerId());
        SocialServer.getInstance().sendExc.publishEvent(channel, message);
    }

    /**
     * 发送给服务器
     * @param messId
     * @param data
     */
    public static void send_to_server(GlobalPlayerWorldInfo player, int messId, byte[] data) {
        send_to_server(player.getPlat(), player.getServerId(), messId, data);
    }

    /**
     * 发送给服务器
     * @param plat
     * @param serverId
     * @param messId
     * @param data
     */
    public static void send_to_server(String plat, int serverId, int messId, byte[] data) {
        String key = plat + "_" + serverId;
        ServerInfo server = Manager.serverManager.getServers().get(key);
        send_to_server(server.getSession(), messId, data);
    }

    /**
     * 发送给所有战斗服
     * @param messId
     * @param data
     */
    public static void send_all_fight(int messId, byte[] data) {
        for (ServerInfo server : Manager.serverManager.getServers().values()) {
            if (server.getServerType() != 4){
                continue;
            }
            send_to_server(server.getSession(), messId, data);
        }
    }

    /**
     * 发送给 public服务器
     *
     * @param messId
     * @param data
     */
    public static void send_to_public(int messId, byte[] data) {
        SMessage message = new SMessage(messId, data);
        message.setSender(ServerConfig.getServerId());
        SocialServer.getInstance().pc.sendToPublic(message);
    }


    /**
     * 个人通知
     *
     * @param me         通知玩家
     * @param type       通知类型
     * @param languageId 语言包ID值
     * @param values     参数值
     */
    public static void notify_player(GlobalPlayerWorldInfo me, Notify type, int languageId, Object... values) {
        ChatMessage.PersonalNotice.Builder msg = ChatMessage.PersonalNotice.newBuilder();
        msg.setType(type.getValue());
        msg.setContent(languageId);
        if (values != null) {
            for (Object value : values) {
                String strValue = String.valueOf(value);
                msg.addValue(parseParam(strValue));
            }
        }
        send_to_player(me, ChatMessage.PersonalNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }

    private static ChatMessage.paramStruct.Builder parseParam(String value) {
        ChatMessage.paramStruct.Builder info = ChatMessage.paramStruct.newBuilder();
        if (value.length() < 3) {
            info.setMark(0);
            info.setParamsValue(value);
            return info;
        }
        char sr = value.charAt(1);
        char srs = value.charAt(2);

        if (sr == '&' && srs == '_') {
            String[] tt = value.split("&_");
            info.setMark(Integer.parseInt(tt[0]));
            info.setParamsValue(value.substring(3));
        } else {
            info.setMark(0);
            info.setParamsValue(value);
        }
        return info;
    }
}
