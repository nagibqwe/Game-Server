package com.game.utils;

import com.data.struct.ReadIntegerArray;
import com.game.chat.Manager.ChatManager;
import com.game.chat.structs.ChatChannel;
import com.game.chat.structs.Notify;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.fightserver.manager.FightClientManager;
import com.game.guild.structs.Guild;
import com.game.guild.structs.GuildMember;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.data.MessageString;
import com.game.map.structs.MapUtils;
import com.game.player.structs.Player;
import com.game.player.structs.SessionAttribute;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.server.social.SocialServerClient;
import com.game.thread.DispatchProcessor;
import game.core.map.IMapObject;
import game.core.message.MessageNumber;
import game.core.message.MsgSourceEnum;
import game.core.message.SMessage;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.message.ChatMessage;
import game.message.ChatMessage.PersonalChatNotice;
import game.message.ChatMessage.paramStruct;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 */
public class MessageUtils {

    private static final Logger log = LogManager.getLogger(MessageUtils.class);
    private static final long ServerType = 100000000000000L;

    public static void send(ChannelHandlerContext session, SMessage message) {
        if (session == null) {
            return;
        }
        if (GameServer.getInstance().IsFightServer()) {
            //背包的数据不发送
            if (DispatchProcessor.getInstance().deal().FightSendMsgID_Filter(message.getId())) {
                return;
            }
            int sourceId = MessageNumber.getSource(message.getId());
            if (sourceId == MsgSourceEnum.GameServerToClient) {
//                OtherServerToPlayerMessage mess = new OtherServerToPlayerMessage();
//                mess.setId(message.getId());
//                mess.setBytes(message.getData());
//                mess.setSendId(ServerConfig.getServerId());
//                mess.setRoleIds(new ArrayList<Long>());
//                mess.getRoleIds().add(message.getSender());
//                mess.setSendTime((int) (TimeUtils.Time() / 1000));
//                FightClientManager.sendToPlayer(session, mess);
                List<Long> roleIds = new ArrayList<>();
                roleIds.add(message.getSender());
                FightClientManager.GetInstance().send_to_player(session, roleIds, message.getId(), message.getData(), message.getSender());
            } else {
                FightClientManager.GetInstance().send_to_game(session, message.getSender(), message.getId(), message.getData());
//                FightClientManager.sendToGame(session, message);
            }
        } else {
            GameServer.SendtoMessage(session, message);
        }
        //  session.write(message);
    }

    public static void send_to_public(int msgId, byte[] msg) {
        SMessage mess = new SMessage(msgId, msg);
        mess.setSender(ServerType + GameServer.getInstance().getServerId());
        Manager.publicServerManager.sendToPublic(mess);
    }

    public static void send_to_social(int msgId, byte[] msg) {
        send_to_social(ServerType + GameServer.getInstance().getServerId(), msgId, msg);
    }

    public static void send_to_social(Player player, int msgId, byte[] msg) {
        send_to_social(player.getId(), msgId, msg);
    }

    public static void send_to_social(long playerId, int msgId, byte[] msg) {
        SMessage mess = new SMessage(msgId, msg);
        mess.setSender(playerId);
        SocialServerClient.getInstance().sendToSocial(mess);
    }

    /**
     * 给玩家战斗服的拷贝对象发送消息
     *
     * @param player
     * @param msgId
     * @param msg
     */
    public static void send_to_fight(Player player, int msgId, byte[] msg) {
        if (player.playerCrossData.isToFightServer()) {
            ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(), msgId, msg);
        }
    }

    //给同地图玩家发送广播消息
    public static void send_to_map(MapObject map, int messageId, byte[] message) {
        if (map == null) {
            return;
        }
        if (GameServer.getInstance().IsFightServer()) {
            List<Player> pps = new ArrayList<>(map.getPlayers().values());
            FightClientManager.GetInstance().send_to_players(pps, messageId, message, map.getId());
            return;
        }
        send_to_players(map.getPlayers().values(), new SMessage(messageId, message));
    }

    //给同地图当前对象周围玩家广播消息
    public static void send_to_roundPlayer(IMapObject obj, int messageId, byte[] message) {
        try {
            IScript script = Manager.scriptManager.GetScriptClass(ScriptEnum.MessageBaseScript);
            MessageInterFace mScript = (MessageInterFace) script;
            mScript.OnBroadcastRound2(obj, new SMessage(messageId, message));

        } catch (Exception e) {
            log.error("发送消息失败send_to_roundPlayer2", e);
        }
    }

    //给同地图当前对象周围玩家广播消息
    public static void send_to_roundPlayer(IMapObject obj, int messageId, byte[] message, boolean isIncludeMe) {
        try {
            IScript script = Manager.scriptManager.GetScriptClass(ScriptEnum.MessageBaseScript);
            MessageInterFace mScript = (MessageInterFace) script;
            mScript.OnBroadcastRound3(obj, new SMessage(messageId, message), isIncludeMe);

        } catch (Exception e) {
            log.error("发送消息失败send_to_roundPlayer3", e);
        }
    }

    //给客户端发送消息
    public static void send_to_player(Player player, SMessage message) {
        if (player == null) {
            return;
        }
        if (!player.isOnline()) {
            return;
        }
        message.setSender(player.getId());
        send(player.getIosession(), message);
    }

    //给客户端发送消息
    public static void send_to_player(Player player, int messageId, byte[] msg) {
        send_to_player(player, new SMessage(messageId, msg));
    }

    public static void send_to_players(Collection<Player> plist, int messageId, byte[] msg, long actionId) {
        if (GameServer.getInstance().IsFightServer()) {
            FightClientManager.GetInstance().send_to_players(plist, messageId, msg, actionId);
            return;
        }

        SMessage mss = new SMessage(messageId, msg);
        for (Player pp : plist) {
            send_to_player(pp, mss);
        }
    }


    /**
     * 个人通知
     *
     * @param me      通知玩家
     * @param type    通知类型
     * @param message 通知消息
     */
    public static void notify_player(Player me, Notify type, String message) {
        ChatMessage.PersonalNotice.Builder msg = ChatMessage.PersonalNotice.newBuilder();
        msg.setType(type.getValue());
        msg.setContent(MessageString.WANGNENGTISHI);
        msg.addValue(parseParam(message));
        send_to_player(me, ChatMessage.PersonalNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 地图通知
     *
     * @param map        地图
     * @param type       通知类型
     * @param languageId 语言ID
     * @param values     参数值
     */
    public static void notify_Map(MapObject map, Notify type, int languageId, String... values) {
        if (map == null) {
            return;
        }

        ChatMessage.PersonalNotice.Builder msg = ChatMessage.PersonalNotice.newBuilder();
        msg.setType(type.getValue());
        msg.setContent(languageId);
        if (values != null) {
            for (String value : values) {
                msg.addValue(parseParam(value));
            }
        }

        if (GameServer.getInstance().IsFightServer()) {
            List<Player> pps = new ArrayList<>(map.getPlayers().values());
            FightClientManager.GetInstance().send_to_players(pps, ChatMessage.PersonalNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray(), map.getId());
            return;
        }
        send_to_players(map.getPlayers().values(), new SMessage(ChatMessage.PersonalNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray()));
    }

    /**
     * 个人通知
     *
     * @param me         通知玩家
     * @param type       通知类型
     * @param languageId 语言包ID值
     * @param values     参数值
     */
    public static void notify_player(Player me, Notify type, int languageId, Object... values) {
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

    /**
     * 战斗服通知 游戏服 公告
     *
     * @param session
     * @param type       通知类型
     * @param languageId 语言包ID值
     * @param values     参数值
     */
    public static void notify_server(ChannelHandlerContext session, Notify type, ChatChannel channel, int languageId, Object... values) {
        ChatMessage.F2GameServerNotice.Builder msg = ChatMessage.F2GameServerNotice.newBuilder();
        msg.setType(type.getValue());
        msg.addChatChannelList(channel.getChannel());
        msg.setContent(languageId);
        if (values != null) {
            for (Object value : values) {
                String strValue = String.valueOf(value);
                msg.addValue(parseParam(strValue));
            }
        }
        FightClientManager.GetInstance().send_to_game(session, ServerConfig.getServerId(), ChatMessage.F2GameServerNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 战斗服 通知 所有游戏服服务器
     *
     * @param session
     * @param type       通知类型
     * @param languageId 语言包ID值
     * @param values     参数值
     */
    public static void notify_AllServer(ChannelHandlerContext session, Notify type, int languageId, Object... values) {
        ChatMessage.F2PServerNotice.Builder msg = ChatMessage.F2PServerNotice.newBuilder();
        Integer serverId = session.channel().attr(SessionAttribute.SERVER_ID).get();
        String plat = session.channel().attr(SessionAttribute.PLATFORMNAME).get();
        msg.setPlat(plat);
        msg.setServerId(serverId);
        msg.setType(type.getValue());
        msg.setContent(languageId);
        if (values != null) {
            for (Object value : values) {
                String strValue = String.valueOf(value);
                msg.addValue(parseParam(strValue));
            }
        }
        send_to_public(ChatMessage.F2PServerNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 游戏服 通知 所有游戏服服务器
     *
     * @param type
     * @param languageId
     * @param values
     */
    public static void notify_AllServer(Notify type, ChatChannel channel, int languageId, Object... values) {
        ChatMessage.F2PServerNotice.Builder msg = ChatMessage.F2PServerNotice.newBuilder();
        int serverId = ServerConfig.getServerId();
        String plat = ServerConfig.getServerPlatform();
        msg.setPlat(plat);
        msg.setServerId(serverId);
        msg.setType(type.getValue());
        msg.addChatChannelList(channel.getChannel());
        msg.setContent(languageId);

        if (values != null) {
            for (Object value : values) {
                String strValue = String.valueOf(value);
                msg.addValue(parseParam(strValue));
            }
        }
        send_to_public(ChatMessage.F2PServerNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 通知同一坐骑的玩家
     *
     * @param player
     * @param isSendSelf
     * @param type
     * @param languageId
     * @param values
     */
    public static void notify_same_horse_palyer(Player player, boolean isSendSelf, Notify type, int languageId, String... values) {
        if (player == null) {
            return;
        }

        ChatMessage.PersonalNotice.Builder msg = ChatMessage.PersonalNotice.newBuilder();
        msg.setType(type.getValue());
        msg.setContent(languageId);
        if (values != null) {
            for (String value : values) {
                msg.addValue(parseParam(value));
            }
        }
        SMessage mess = new SMessage(ChatMessage.PersonalNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        if (isSendSelf) {
            send_to_player(player, mess);
        }
        List<Long> roleIds = Manager.horseManager.getMultiPlayerHashMap().get(player.getId());
        if (roleIds == null) {
            return;
        }
        for (long roleId : roleIds) {
            Player pp = Manager.playerManager.getPlayerOnline(roleId);
            if (pp == null) {
                continue;
            }
            send_to_player(pp, mess);
        }
    }

    public static void notify_Chat_channel(Player player, int channel, int languageId, String... values) {
        PersonalChatNotice.Builder msg = PersonalChatNotice.newBuilder();
        msg.setChannel(channel);
        msg.setContent(languageId);
        if (values != null) {
            for (String value : values) {
                msg.addValue(parseParam(value));
            }
        }
        msg.setChater(ChatManager.CHATTYPE_SYSTEM_GuildSendId);
        msg.setChatername("");
        msg.setVipLv(0);
        msg.setOcc(0);
        msg.setMoonandOver(0);
        msg.setChaterlevel(0);
//        msg.setIconState(0);
//        msg.setHeadFrameId(Manager.newFashionManager.deal().getHeadBox(player));
//        msg.setHeadId(Manager.newFashionManager.deal().getHead(player));

        msg.setHead(MapUtils.getHead(player));
        msg.setChatBgId(Manager.newFashionManager.deal().getQiPao(player));
        SMessage mess = new SMessage(ChatMessage.PersonalChatNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        send_to_player(player, mess);
    }

    public static void notify_Chat_channel(Collection<Player> players, int channel, int languageId, String... values) {
        PersonalChatNotice.Builder msg = PersonalChatNotice.newBuilder();
        msg.setChannel(channel);
        msg.setContent(languageId);
        if (values != null) {
            for (String value : values) {
                msg.addValue(parseParam(value));
            }
        }
        msg.setChater(ChatManager.CHATTYPE_SYSTEM_GuildSendId);
        msg.setChatername("");
        msg.setVipLv(0);
        msg.setOcc(0);
        msg.setMoonandOver(0);
        msg.setChaterlevel(0);
        msg.setHead(MapUtils.getDefaultHead());
        msg.setChatBgId(0);
        SMessage mess = new SMessage(ChatMessage.PersonalChatNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        for (Player player : players) {
            send_to_player(player, mess);
        }
    }

    //向所有玩家发送
    public static void notify_Chat_To_AllPlayer(Player srcPlayer, ChatChannel channel, int languageId, String... values) {
        List<Player> plist = new ArrayList<>();

        for (Player member : Manager.playerManager.getPlayersCache().values()) {
            if (!member.isOnline()) {
                continue;
            }
            plist.add(member);
        }
        if (plist.isEmpty()) {
            return;
        }
        PersonalChatNotice.Builder msg = PersonalChatNotice.newBuilder();
        msg.setChannel(channel.getChannel());
        msg.setContent(languageId);
        if (values != null) {
            for (String value : values) {
                msg.addValue(parseParam(value));
            }
        }
        // msg.setIconState(0);
        if (srcPlayer != null) {
            msg.setChater(srcPlayer.getId());
            msg.setChatername(srcPlayer.getName());
            msg.setVipLv(0);
            msg.setOcc(srcPlayer.getCareer());
            msg.setMoonandOver(srcPlayer.moonandOverCard());
            msg.setChaterlevel(srcPlayer.getLevel());
//            msg.setIconState(srcPlayer.getUseIconState());
//            msg.setHeadFrameId(Manager.newFashionManager.deal().getHeadBox(srcPlayer));
//            msg.setHeadId(Manager.newFashionManager.deal().getHead(srcPlayer));
            msg.setHead(MapUtils.getHead(srcPlayer));
            msg.setChatBgId(Manager.newFashionManager.deal().getQiPao(srcPlayer));
        } else {
            msg.setChater(ChatManager.CHATTYPE_SYSTEM_GuildSendId);
            msg.setChatername("");
            msg.setVipLv(0);
            msg.setOcc(0);
            msg.setMoonandOver(0);
            msg.setChaterlevel(0);
            // msg.setHeadFrameId(0);
            msg.setChatBgId(0);
            // msg.setHeadId(0);
            msg.setHead(MapUtils.getDefaultHead());
        }

        SMessage mess = new SMessage(ChatMessage.PersonalChatNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        for (Player player : plist) {
            send_to_player(player, mess);
        }
    }

    /**
     * 向所有帮会玩家发送
     *
     * @param srcPlayer  主动发起者
     * @param guild      帮会
     * @param HaveMe     是否包含发起者自己
     * @param languageId 语言包ID
     * @param values     语言参数
     */
    public static void notify_Chat_To_GuildPlayer(Player srcPlayer, Guild guild, boolean HaveMe, int languageId, String... values) {
        if (guild == null) {
            return;
        }
        List<Player> plist = new ArrayList<>();
        for (GuildMember guildMember : guild.getMembers().values()) {
            Player pp = Manager.playerManager.getPlayerOnline(guildMember.getId());
            if (pp == null) {
                continue;
            }
            //如果不包含我自己
            if (!HaveMe) {
                if (pp.getId() == srcPlayer.getId()) {
                    continue;
                }
            }
            plist.add(pp);
        }
        if (plist.isEmpty()) {
            return;
        }
        PersonalChatNotice.Builder msg = PersonalChatNotice.newBuilder();
        msg.setChannel(ChatChannel.CHATCHANNEL_GUILD.getChannel());
        msg.setContent(languageId);
        if (values != null) {
            for (String value : values) {
                msg.addValue(parseParam(value));
            }
        }
        //  msg.setIconState(0);
        if (srcPlayer != null) {
            msg.setChater(srcPlayer.getId());
            msg.setChatername(srcPlayer.getName());
            msg.setVipLv(0);
            msg.setOcc(srcPlayer.getCareer());
            msg.setMoonandOver(srcPlayer.moonandOverCard());
            msg.setChaterlevel(srcPlayer.getLevel());
//            msg.setIconState(srcPlayer.getUseIconState());
//            msg.setHeadFrameId(Manager.newFashionManager.deal().getHeadBox(srcPlayer));
//            msg.setHeadId(Manager.newFashionManager.deal().getHead(srcPlayer));
            msg.setHead(MapUtils.getHead(srcPlayer));

            msg.setChatBgId(Manager.newFashionManager.deal().getQiPao(srcPlayer));

        } else {
            msg.setChater(ChatManager.CHATTYPE_SYSTEM_GuildSendId);
            msg.setChatername("");
            msg.setVipLv(0);
            msg.setOcc(0);
            msg.setMoonandOver(0);
            msg.setChaterlevel(0);
            // msg.setHeadFrameId(0);
            msg.setChatBgId(0);
            // msg.setHeadId(0);
            msg.setHead(MapUtils.getDefaultHead());
        }

        SMessage mess = new SMessage(ChatMessage.PersonalChatNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        for (Player player : plist) {
            send_to_player(player, mess);
        }
    }

    /**
     * 向指定的玩家发送频道消息
     *
     * @param player
     * @param channel
     * @param languageId
     * @param values
     */
    public static void notify_chat_player(Player player, ChatChannel channel, int languageId, String... values) {
        if (player == null) {
            return;
        }

        PersonalChatNotice.Builder msg = PersonalChatNotice.newBuilder();
        msg.setChannel(channel.getChannel());
        msg.setContent(languageId);
        if (values != null) {
            for (String value : values) {
                msg.addValue(parseParam(value));
            }
        }
        msg.setChater(ChatManager.CHATTYPE_SYSTEM_GuildSendId);
        msg.setChatername("");
        msg.setVipLv(0);
        msg.setOcc(0);
        msg.setMoonandOver(0);
        msg.setChaterlevel(0);
        //msg.setIconState(0);
//        msg.setHeadFrameId(Manager.newFashionManager.deal().getHeadBox(player));
//        msg.setHeadId(Manager.newFashionManager.deal().getHead(player));

        msg.setHead(MapUtils.getHead(player));

        msg.setChatBgId(Manager.newFashionManager.deal().getQiPao(player));
        SMessage mess = new SMessage(ChatMessage.PersonalChatNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        send_to_player(player, mess);
    }

    public static void notify_guild_Chat(Guild guild, int languageId, String... values) {
        if (guild == null) {
            return;
        }
        List<Player> plist = new ArrayList<>();
        for (long id : guild.getMembers().keySet()) {
            Player pp = Manager.playerManager.getPlayerOnline(id);
            if (pp == null) {
                continue;
            }
            plist.add(pp);
        }

        if (plist.isEmpty()) {
            return;
        }

        notify_Chat_channel(plist, ChatChannel.CHATCHANNEL_GUILD.getChannel(), languageId, values);
    }

    public static void notify_guildPlayer(Guild guild, Notify type, int languageId, String... values) {
        if (guild == null) {
            return;
        }
        ChatMessage.PersonalNotice.Builder msg = ChatMessage.PersonalNotice.newBuilder();
        msg.setType(type.getValue());
        msg.setContent(languageId);
        if (values != null) {
            for (String value : values) {
                msg.addValue(parseParam(value));
            }
        }
        SMessage mess = new SMessage(ChatMessage.PersonalNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        for (long id : guild.getMembers().keySet()) {
            Player pp = Manager.playerManager.getPlayerOnline(id);
            if (pp == null) {
                continue;
            }
            send_to_player(pp, mess);
        }
    }

    private static paramStruct.Builder parseParam(String value) {
        paramStruct.Builder info = paramStruct.newBuilder();
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

    /**
     * 全服通知
     *
     * @param type       通知类型
     * @param languageId 语言包ID值
     * @param values     参数值
     */
    public static void notify_allOnlinePlayer(Notify type, int languageId, String... values) {
        ChatMessage.PersonalNotice.Builder msg = ChatMessage.PersonalNotice.newBuilder();
        msg.setType(type.getValue());
        msg.setContent(languageId);
        if (values != null) {
            for (String value : values) {
                msg.addValue(parseParam(value));
            }
        }
        send_to_all_player(ChatMessage.PersonalNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public static void notify_allOnlinePlayer(int type, ReadIntegerArray chatChannel, int languageId, String... values) {
        notify_allOnlinePlayer(null, type, chatChannel, languageId, values);
    }

    public static void notify_allOnlinePlayer(Notify type, ChatChannel chatChannel, int languageId, String... values) {
        Integer[] channels = {chatChannel.getChannel()};
        notify_allOnlinePlayer(null, type.getValue(), new ReadIntegerArray(channels), languageId, values);
    }

    /**
     * 全服通知
     *
     * @param type       通知类型
     * @param languageId 语言包ID值
     * @param values     参数值
     */
    public static void notify_allOnlinePlayer(Player player, int type, ReadIntegerArray chatChannels, int languageId, String... values) {
        if (type == 0 && chatChannels == null) {
            return;
        }

        ChatMessage.PersonalNotice.Builder msg = ChatMessage.PersonalNotice.newBuilder();
        msg.setType(type);
        if (chatChannels != null) {
            for (int channel : chatChannels.getValue()) {
                msg.addChatChannelList(channel);
            }
        }
        msg.setContent(languageId);
        if (values != null) {
            for (String value : values) {
                msg.addValue(parseParam(value));
            }
        }
        //发送到本服
        send_to_all_player(ChatMessage.PersonalNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        if (player != null && GameServer.getInstance().IsFightServer()) {
            //跨服需要转发到玩家原来的服务器 进行广播
            Manager.crossServerManager.crossFightdeal().sendF2GSendPersonalNotice(player, msg);
        }
    }

    public static void send_to_all_player(int messageId, byte[] message) {
        //战斗服暂时不会有全服公告之类的信息
        if (GameServer.getInstance().IsFightServer()) {
            return;
        }
        SMessage sMessage = new SMessage(messageId, message);
        ConcurrentHashMap<Long, ChannelHandlerContext> hashMap = Manager.registerManager.getAllsessions();
        Iterator<ChannelHandlerContext> iter = hashMap.values().iterator();
        while (iter.hasNext()) {
            ChannelHandlerContext io = iter.next();
            send(io, sMessage);
        }
    }

    private static void send_to_players(Collection<Player> players, SMessage message) {
        for (Player pp : players) {
            message.setSender(pp.getId());
            send(pp.getIosession(), message);
        }
    }

    public static void send_to_player(Long roleId, int messageId, byte[] msg) {
        Player player = Manager.playerManager.getPlayerOnline(roleId);
        send_to_player(player, messageId, msg);
    }

    public static void send_TO_Guild(Guild guild, int msgId, byte[] msg) {
        SMessage mess = new SMessage(msgId, msg);
        for (long id : guild.getMembers().keySet()) {
            Player pp = Manager.playerManager.getPlayerOnline(id);
            if (pp == null) {
                continue;
            }
            send_to_player(pp, mess);
        }
    }
}
