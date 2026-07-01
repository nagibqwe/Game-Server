/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.utils;

import com.game.fightroom.structs.FightRoom;
import com.game.gameserver.structs.ServerInfo;
import com.game.manager.Manager;
import com.game.server.worker.FightSMessage;
import com.game.server.worker.FightSMessageEventTranslator;
import com.game.server.worker.FightSMessageHandler;
import com.game.structs.ServerType;
import com.game.zone.structs.ZoneTeam;
import com.lmax.disruptor.dsl.ProducerType;
import game.core.disruptor.DisruptorOrderPoolExecutor;
import game.core.disruptor.TaskEvent;
import game.core.disruptor.TaskEventFactory;
import game.core.net.Config.ServerConfig;
import game.message.ChatMessage;
import game.message.ChatMessage.PersonalNotice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Administrator
 */
public class MessageUtils {

    private static DisruptorOrderPoolExecutor<TaskEvent<FightSMessage>, Long, FightSMessage> sendExc = null;

    public static void start() {
        TaskEventFactory<FightSMessage> tef = new TaskEventFactory<>();
        FightSMessageEventTranslator fsmet = new FightSMessageEventTranslator();
        FightSMessageHandler fsmh = new FightSMessageHandler();
        sendExc = new DisruptorOrderPoolExecutor<>("PublicSender", tef, 1024 * 1024, ProducerType.MULTI, fsmh, 8, fsmet);
        fsmh.setDisruptorPool(sendExc);
        sendExc.start();
    }

    public static void stop() {
        sendExc.stop();
    }

    private final static Logger log = LogManager.getLogger(MessageUtils.class);

    private static boolean send(ChannelHandlerContext session, int msgId, byte[] data, long sender) {
        if (session == null) {
            log.error("传过来的连接为空", new NullPointerException());
            return false;
        }

        FightSMessage fsm = new FightSMessage(null, msgId, data, sender, session, 1);
        sendExc.publishEvent(sender, fsm);
        return true;
    }

    private static boolean sendPlayers(ChannelHandlerContext session, List<Long> roleIds, int msgId, byte[] data, long sender) {
        FightSMessage fsm = new FightSMessage(roleIds, msgId, data, sender, session, 2);
        sendExc.publishEvent(sender, fsm);
        return true;

    }

    public static boolean send_to_game(ChannelHandlerContext session, int msgId, byte[] msg) {
        return send(session, msgId, msg, ServerConfig.getServerId());
    }

    /**
     * 发送社交服务器
     * @param msgId
     * @param msg
     */
    public static void send_to_social(int msgId, byte[] msg) {
        send_to_game( Manager.gameServerManager.socialServer.getSession(), msgId, msg);
    }

    /**
     * 向指定的区与角色ID发送某个信息
     *
     * @param players
     * @param msgId
     * @param msg
     */
    public static void send_to_players(Map<ChannelHandlerContext, List<Long>> players, int msgId, byte[] msg) {

        if (players == null) {
            return;
        }

        if (players.isEmpty()) {
            return;
        }

//        OtherServerToPlayerMessage mess = new OtherServerToPlayerMessage();
//        mess.setId(msgId);
//        mess.setBytes(msg);
//        mess.setSendId(ServerConfig.getServerId());
//        mess.setSendTime((int) (TimeUtils.Time() / 1000));
        Iterator<Entry<ChannelHandlerContext, List<Long>>> iter = players.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<ChannelHandlerContext, List<Long>> en = iter.next();
            long scrId = ServerConfig.getServerId();
            if (!en.getValue().isEmpty()) {
                scrId = en.getValue().get(0);
            }
//            mess.setRoleIds(en.getValue());
            sendPlayers(en.getKey(), en.getValue(), msgId, msg, scrId);
        }
    }

    /**
     * 向指定玩家发送消息
     *
     * @param context
     * @param roleId
     * @param msgId
     * @param msg
     */
    public static void send_to_player(ChannelHandlerContext context, long roleId, int msgId, byte[] msg) {
        Map<ChannelHandlerContext, List<Long>> players = new HashMap<>();
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(roleId);
        players.put(context, roleIds);
        send_to_players(players, msgId, msg);
    }

    /**
     * 向指定的玩家列表发送信息
     *
     * @param context
     * @param roleIds
     * @param msgId
     * @param msg
     */
    public static void send_to_player(ChannelHandlerContext context, Collection<Long> roleIds, int msgId, byte[] msg) {
        Map<ChannelHandlerContext, List<Long>> players = new HashMap<>();
        players.put(context, new ArrayList<>(roleIds));
        send_to_players(players, msgId, msg);
    }

    public static void send_to_player(String plat, int sid, long roleId, int msgId, byte[] msg) {
        ChannelHandlerContext session = Manager.gameServerManager.GetSession(plat, sid);
        Map<ChannelHandlerContext, List<Long>> players = new HashMap<>();
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(roleId);
        players.put(session, roleIds);
        send_to_players(players, msgId, msg);
    }

    /**
     * 向房间中的所有人发送协议
     *
     * @param fr
     * @param msgId
     * @param msg
     */
    public static void send_to_room(FightRoom fr, int msgId, byte[] msg) {
        Map<ChannelHandlerContext, List<Long>> players = new HashMap<>();
        for (ZoneTeam ri : fr.getTeam()) {
            ChannelHandlerContext session = Manager.gameServerManager.GetSession(ri.getPlat(), ri.getsId());
            if (session == null) {
                continue;
            }
            List<Long> roleIds;
            if (players.containsKey(session)) {
                roleIds = players.get(session);
            } else {
                roleIds = new ArrayList<>();
                players.put(session, roleIds);
            }
            roleIds.addAll(ri.getPlist().keySet());
        }
        send_to_players(players, msgId, msg);
    }

    public static boolean send_to_game(String plat, int sid, int msgId, byte[] msg) {
        ChannelHandlerContext session = Manager.gameServerManager.GetSession(plat, sid);
        if (session != null) {
            return send_to_game(session, msgId, msg);
        }
        return false;
    }

    public static void notify_player(ChannelHandlerContext context, long roleId, int languageId, String... values) {

        Map<ChannelHandlerContext, List<Long>> players = new HashMap<>();
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(roleId);
        players.put(context, roleIds);
        ChatMessage.PersonalNotice.Builder msg = ChatMessage.PersonalNotice.newBuilder();
        msg.setType(7);
        msg.setContent(languageId);
        if (values != null) {
            for (String value : values) {
                msg.addValue(parseParam(value));
            }
        }
        send_to_players(players, PersonalNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public static void notify_player(ChannelHandlerContext context, int notifyType, long roleId, int languageId, String... values) {

        Map<ChannelHandlerContext, List<Long>> players = new HashMap<>();
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(roleId);
        players.put(context, roleIds);
        ChatMessage.PersonalNotice.Builder msg = ChatMessage.PersonalNotice.newBuilder();
        msg.setType(notifyType);
        msg.setContent(languageId);
        if (values != null) {
            for (String value : values) {
                msg.addValue(parseParam(value));
            }
        }
        send_to_players(players, PersonalNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
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
