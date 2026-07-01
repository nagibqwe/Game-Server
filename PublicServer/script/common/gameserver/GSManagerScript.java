/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.gameserver;

import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.fightroom.structs.FightRoom;
import com.game.fightroom.structs.FightRoomState;
import com.game.gameserver.manager.GameServerManager;
import com.game.gameserver.structs.ServerInfo;
import com.game.guildcrossfud.struct.FudGroup;
import com.game.server.MainServer;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.servermatch.structs.GameServerInfo;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.server.script.IGSManagerScript;
import com.game.structs.ServerType;
import com.game.structs.SessionKey;
import com.game.utils.MessageUtils;
import com.game.zone.structs.TeamPlayerInfo;
import com.game.zone.structs.ZoneTeam;
import game.core.net.Config.ServerConfig;
import game.message.*;
import game.message.CrossFightMessage.G2PCheckCrossInfo;
import game.message.CrossFightMessage.P2GCheckCrossInfoRes;
import game.message.CrossServerMessage.P2GConnectHeartRes;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 服务器列表逻辑类脚本
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class GSManagerScript implements IGSManagerScript {

    private static final Logger log = LogManager.getLogger("GameServerManager");

    @Override
    public int getId() {
        return ScriptEnum.GSManagerScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    private Object object  =  new Object();

    @Override
    public void OnG2PServerOpentimeChange(ChannelHandlerContext context, CrossServerMessage.G2PServerOpentimeChange messInfo) {
        if (!context.channel().hasAttr(SessionKey.SERVERPLATID)) {
            log.error(messInfo.getServerId() + "服务器还没来注册过，请检查一下问题！", new NullPointerException());
            return;
        }
        String key = context.channel().attr(SessionKey.SERVERPLATID).get();
        ServerInfo serverInfo = GameServerManager.getInstance().getServerCache().get(key);
        if (serverInfo == null) {
            String plat = messInfo.getPlat();
            key = GameServerManager.getInstance().makeKey(plat, messInfo.getServerId());
            serverInfo = GameServerManager.getInstance().getServerCache().get(key);
            if (serverInfo == null) {
                log.error(messInfo.getServerId() + "服务器还没来注册过，请检查一下问题！2", new NullPointerException());
                return;
            }
        }
        serverInfo.setOpenTime(messInfo.getServeropentime());
        serverInfo.getSids().clear();
        serverInfo.getSids().addAll(messInfo.getServerIdsListList());

        GameServerInfo info = ServerMatchManager.infos.get(key);
        if (info != null) {
            info.setOpenTime(messInfo.getServeropentime());
            info.getSids().clear();
            info.getSids().addAll(messInfo.getServerIdsListList());
            if (ServerMatchManager.serverStageGrouplist.containsKey(info.getbigGroupID())) {
                ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, List<GameServerInfo>>> stageGroupList
                        = ServerMatchManager.serverStageGrouplist.get(info.getbigGroupID());
                for (Integer stage : info.stageWithGroupIndex.keySet()) {
                    int group = info.stageWithGroupIndex.get(stage);
                    List<GameServerInfo> groupList = stageGroupList.get(stage).get(group);
                    for (GameServerInfo gameServerInfo : groupList) {
                        if (gameServerInfo.getServerId() == info.getServerId()) {
                            gameServerInfo.setOpenTime(messInfo.getServeropentime());
                            gameServerInfo.getSids().clear();
                            gameServerInfo.getSids().addAll(messInfo.getServerIdsListList());
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 修改对应服务器的服务器名字
     *
     * @param context
     * @param messInfo
     */
    @Override
    public void OnG2PServerNameChange(ChannelHandlerContext context, CrossServerMessage.G2PServerNameChange messInfo) {
        // if (!context.channel().hasAttr(SessionKey.SERVERPLATID)) {
        //     log.error(messInfo.getServerId() + "服务器还没来注册过，请检查一下问题！", new NullPointerException());
        //     return;
        // }
        // String key = context.channel().attr(SessionKey.SERVERPLATID).get();
        // ServerInfo serverInfo = GameServerManager.getInstance().getServerCache().get(key);
        // if (serverInfo == null) {
        //     String plat = messInfo.getPlat();
        //     key = GameServerManager.getInstance().makeKey(plat, messInfo.getServerId());
        //     serverInfo = GameServerManager.getInstance().getServerCache().get(key);
        //     if (serverInfo == null) {
        //         log.info(messInfo.getServerId() + "服务器还没来注册过，请检查一下问题！2", new NullPointerException());
        //         return;
        //     }
        // }
        // serverInfo.setServerName(messInfo.getServerName());
        // serverInfo.getSids().clear();
        // serverInfo.getSids().addAll(messInfo.getServerIdsListList());
        // //修改名字的时候也需要同步修改分组的名字
        // GameServerInfo info = ServerMatchManager.infos.get(key);
        // if (info!=null)
        // {
        //     info.setServerName(messInfo.getServerName());
        // }
    }

    /**
     * 游戏服世界等级改变
     *
     * @param context
     * @param messInfo
     */
    @Override
    public void OnG2PServerWorldLvChange(ChannelHandlerContext context, CrossServerMessage.G2PServerWorldLvChange messInfo) {
        if (!context.channel().hasAttr(SessionKey.SERVERPLATID)) {
            log.error(messInfo.getServerId() + "服务器还没来注册过，请检查一下问题！", new NullPointerException());
            return;
        }
        String key = context.channel().attr(SessionKey.SERVERPLATID).get();
        ServerInfo serverInfo = GameServerManager.getInstance().getServerCache().get(key);
        if (serverInfo == null) {
            String plat = messInfo.getPlat();
            key = GameServerManager.getInstance().makeKey(plat, messInfo.getServerId());
            serverInfo = GameServerManager.getInstance().getServerCache().get(key);
            if (serverInfo == null) {
                log.error(messInfo.getServerId() + "服务器还没来注册过，请检查一下问题！2", new NullPointerException());
                return;
            }
        }
        serverInfo.setServerWorldLv(messInfo.getServerWorldLv());
        GameServerInfo info = ServerMatchManager.infos.get(key);
        if (info != null) {
            info.setServerWorldLv(messInfo.getServerWorldLv());
        }
    }

    /**
     * 检测副本存在否
     *
     * @param context
     * @param messInfo
     */
    @Override
    public void OnG2PCheckCrossInfo(ChannelHandlerContext context, G2PCheckCrossInfo messInfo) {
        FightRoom room = Manager.fightManager.getFrcache().get(messInfo.getRoomId());

        P2GCheckCrossInfoRes.Builder res = P2GCheckCrossInfoRes.newBuilder();
        res.setRoleId(messInfo.getRoleId());
        if (room == null || room.getRstate() >= FightRoomState.FIGHTEND) {
            res.setCloneID(0);
            res.setIsCanEnter(false);
            res.addAllMapSetList(new ArrayList<>());
        } else {
            res.setCloneID(room.getModelId());
            res.setIsCanEnter(true);
            res.addAllMapSetList(Manager.fightManager.deal().getFightRoomParam(room));
            TeamPlayerInfo player = null;
            ZoneTeam zoneTeam = null;
            for (ZoneTeam zt : room.getTeam()) {
                player = zt.getPlist().get(messInfo.getRoleId());
                if (player == null) {
                    continue;
                }
                zoneTeam = zt;
                break;
            }
            if (player == null) {
                res.setCloneID(0);
                res.setIsCanEnter(false);
                res.addAllMapSetList(new ArrayList<>());
            } else {
                CrossFightMessage.roleAtt.Builder ratt = CrossFightMessage.roleAtt.newBuilder();
                ratt.setRoleId(player.getRoleId());
                ratt.setCampNo(zoneTeam.getCampNo());
                res.setCross(ratt);
            }
        }
        //TODO判断副本是否可以进入
        MessageUtils.send_to_game(context, P2GCheckCrossInfoRes.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    serverMessage.gameServerInfo.Builder pack(ServerInfo server) {
        serverMessage.gameServerInfo.Builder mServer = serverMessage.gameServerInfo.newBuilder();
        mServer.setPlatformMark(server.getPlatName());
        mServer.setServerId(server.getServerId());
        mServer.setServerType(server.getServerType());
        mServer.setServerIP(server.getServerIp());
        mServer.setServerPort(server.getPort());
        mServer.setVersion(server.getVersion());
        mServer.addAllMapIds(server.getMapIds());
        mServer.setServerOpentime(server.getOpenTime());
        mServer.setServerWorldlv(server.getServerWorldLv());
        return mServer;
    }

    //服务器注册
    @Override
    public void OnG2PReqRegister(ChannelHandlerContext context, serverMessage.G2PReqRegister messInfo) {

        synchronized (object) {
            ServerInfo info = new ServerInfo();
            serverMessage.gameServerInfo sinfo = messInfo.getSinfo();
            info.setPlatName(sinfo.getPlatformMark());
            info.setServerId(sinfo.getServerId());
            info.setServerType(sinfo.getServerType());
            info.setServerIp(sinfo.getServerIP());
            info.setPort(sinfo.getServerPort());
            info.setVersion(sinfo.getVersion());
            info.setSession(context);
            info.getSids().addAll(messInfo.getCombinedIdsList());
            info.getMapIds().addAll(sinfo.getMapIdsList());
            info.setOpenTime(sinfo.getServerOpentime());
            info.setServerWorldLv(sinfo.getServerWorldlv());

            context.channel().attr(SessionKey.SERVERID).set(info.getServerId());
            context.channel().attr(SessionKey.SERVERPLATID).set(info.getPlatName() + "_" + info.getServerId());
            context.channel().attr(SessionKey.SERVERPLAT).set(info.getPlatName());
            Manager.gameServerManager.addServer(info);
            log.info(context.channel() + " 注册来的信息是：" + messInfo);

            serverMessage.P2GResRegister.Builder msg = serverMessage.P2GResRegister.newBuilder();
            msg.setPublicId(ServerConfig.getServerId());
            msg.setPublicName(ServerConfig.getServerName());
            MessageUtils.send_to_game(context, serverMessage.P2GResRegister.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            //如果是战斗服
            if (sinfo.getServerType() == ServerType.FIGHTSERVER) {
                serverMessage.P2GResFightServerList.Builder ms = serverMessage.P2GResFightServerList.newBuilder();
                ms.addInfoList(pack(info));
                GameServerManager.getInstance().send_all_game(serverMessage.P2GResFightServerList.MsgID.eMsgID_VALUE, ms.build().toByteArray());
                ms.clear();
                //给战斗服同步社交服务器
                if (Manager.gameServerManager.socialServer != null) {
                    ms.setSocial(pack(Manager.gameServerManager.socialServer));
                }
                MessageUtils.send_to_game(context, serverMessage.P2GResFightServerList.MsgID.eMsgID_VALUE, ms.build().toByteArray());
            } else {
                //通知活动状态
                Manager.couplefightManager.getScript().sendStatus(context);

                List<ServerInfo> slist = GameServerManager.getInstance().GetType(ServerType.FIGHTSERVER);
                if (slist == null) {
                    return;
                }
                serverMessage.P2GResFightServerList.Builder ms = serverMessage.P2GResFightServerList.newBuilder();
                for (ServerInfo linfo : slist) {
                    ms.addInfoList(pack(linfo));
                }
                if (Manager.gameServerManager.socialServer != null) {
                    ms.setSocial(pack(Manager.gameServerManager.socialServer));
                }
                MessageUtils.send_to_game(context, serverMessage.P2GResFightServerList.MsgID.eMsgID_VALUE, ms.build().toByteArray());
            }

            //通知所有副本的状态
            Manager.zoneManager.noticeTime(context);
        }

    }

    @Override
    public void OnG2PConnectHeart(ChannelHandlerContext context, CrossServerMessage.G2PConnectHeart mess) {
        int state = 0;//成功
        String key = null;
        if (context.channel().attr(SessionKey.SERVERPLATID).get() != null) {
            key = context.channel().attr(SessionKey.SERVERPLATID).get();
            ChannelHandlerContext session = Manager.gameServerManager.GetSession(key);
            if (session == null) {
                //session ==null
                state = 2;
            }
        } else {
            //还没有注册信息
            state = 1;
        }
        if (key != null) {
            ServerInfo serverInfo = GameServerManager.getInstance().getServerCache().get(key);
            if (serverInfo != null && serverInfo.getServerType() == ServerType.FIGHTSERVER) {
                serverInfo.setHaveNum(mess.getRoleSize());
                // log.info("战斗服  serverId {} 人数：{}" ,serverInfo.getServerId(), mess.getRoleSize());
            }
        }
        P2GConnectHeartRes.Builder msg = P2GConnectHeartRes.newBuilder();
        msg.setReason(state);
        MessageUtils.send_to_game(context, P2GConnectHeartRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 获取自己服务器对应的分组数据
     */
    public void OnG2PReqCrossServerMatch(ChannelHandlerContext context, DailyactiveMessage.G2PReqCrossServerMatch mess) {
        String key = context.channel().attr(SessionKey.SERVERPLATID).get();
        GameServerInfo info = ServerMatchManager.infos.get(key);
        if (info == null) {
            log.info("未找到相应服务器   " + key);
            return;
        }
        if (ServerMatchManager.serverStageGrouplist.containsKey(info.getbigGroupID())) {
            DailyactiveMessage.ResCrossServerMatch.Builder msg = DailyactiveMessage.ResCrossServerMatch.newBuilder();
            DailyactiveMessage.ServerMatchInfo.Builder serverMatchStage = DailyactiveMessage.ServerMatchInfo.newBuilder();
            ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, List<GameServerInfo>>> stageGroupList
                    = ServerMatchManager.serverStageGrouplist.get(info.getbigGroupID());
            setStageGroupMatchInfo(msg, serverMatchStage, stageGroupList, info, ServerMatchManager.serverMatchStage_2);
            setStageGroupMatchInfo(msg, serverMatchStage, stageGroupList, info, ServerMatchManager.serverMatchStage_4);
            setStageGroupMatchInfo(msg, serverMatchStage, stageGroupList, info, ServerMatchManager.serverMatchStage_8);
            // setStageGroupMatchInfo(msg, serverMatchStage, stageGroupList, info, ServerMatchManager.serverMatchStage_16);
            //setStageGroupMatchInfo(msg, serverMatchStage, stageGroupList, info, ServerMatchManager.serverMatchStage_32);
            MessageUtils.send_to_player(context, mess.getRoleid(),
                    DailyactiveMessage.ResCrossServerMatch.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        } else
            log.info("未找到相应服大组ID bigGroupID     " + info.getbigGroupID());

    }

    private void setStageGroupMatchInfo(DailyactiveMessage.ResCrossServerMatch.Builder msg,
                                        DailyactiveMessage.ServerMatchInfo.Builder serverMatchStage,
                                        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, List<GameServerInfo>>> stageGroupList, GameServerInfo info, int stage) {
        if (stageGroupList.containsKey(stage)) {
            ConcurrentHashMap<Integer, List<GameServerInfo>> stageGroup = stageGroupList.get(stage);
            int groupIndex = info.stageWithGroupIndex.get(stage);
            List<GameServerInfo> serverMatch = stageGroup.get(groupIndex);
            addServerMatchInfo(msg, serverMatch, serverMatchStage, stage);
        }
    }

    private void addServerMatchInfo(DailyactiveMessage.ResCrossServerMatch.Builder msg, List<GameServerInfo>
            serverMatchStageList, DailyactiveMessage.ServerMatchInfo.Builder matchData, int stage) {

        for (GameServerInfo i : serverMatchStageList) {
            matchData.setServerid(i.getServerId());
            matchData.setServerWroldLv(i.getServerWorldLv());
            matchData.setOpenTime(GameServerManager.getOpenServerTime(i.getOpenTime()));
            switch (stage) {
                case ServerMatchManager.serverMatchStage_2:
                    msg.addServerMatch2(matchData);
                    break;
                case ServerMatchManager.serverMatchStage_4:
                    msg.addServerMatch4(matchData);
                    break;
                case ServerMatchManager.serverMatchStage_8:
                    msg.addServerMatch8(matchData);
                    break;
            }
        }
    }

    /**
     * 通知游戏服刷新BOSS TIPS
     */
    public void sendBossTipsToGame(int groupID, int bossID, int type) {
        if (!ServerMatchManager.groupIDWithServerKey.containsKey(groupID)) {
            log.info("未找到对应的  gruopID " + groupID);
            return;
        }
        CrossServerMessage.P2GBossRefreshTip.Builder msg = CrossServerMessage.P2GBossRefreshTip.newBuilder();
        msg.setBossID(bossID);
        msg.setType(type);
        List<String> serverkeyList = ServerMatchManager.groupIDWithServerKey.get(groupID);
        for (String serverkey : serverkeyList) {
            ChannelHandlerContext session = GameServerManager.getInstance().GetSession(serverkey);
            if (session != null) {
                MessageUtils.send_to_game(session, CrossServerMessage.P2GBossRefreshTip.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
    }

    @Override
    public void onG2PDailyData(ChannelHandlerContext context, CrossServerMessage.G2PDailyData mess) {
        int dailyId = mess.getDailyId();
        switch (dailyId) {
            case DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR:
                String platSid = context.channel().attr(SessionKey.SERVERPLATID).get();
                List<GameServerInfo> sList = ServerMatchManager.deal().getSeverlistForCurOpenDay(platSid, dailyId);
                if (sList != null) {
                    CrossServerMessage.P2GDailyData.Builder msg = CrossServerMessage.P2GDailyData.newBuilder();
                    msg.setDailyId(dailyId);
                    msg.setData(String.valueOf(sList.size()));
                    MessageUtils.send_to_game(context, CrossServerMessage.P2GDailyData.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                }
                break;
        }
    }

    @Override
    public void onG2PReqChatMess(ChannelHandlerContext context, CrossServerMessage.G2PReqChatMess messInfo) {
        String platSid = context.channel().attr(SessionKey.SERVERPLATID).get();
        List<GameServerInfo> chatServerList = ServerMatchManager.deal().getCrossChatServerlist(platSid);
        CrossServerMessage.P2GResChatMess.Builder msg = CrossServerMessage.P2GResChatMess.newBuilder();
        msg.setChatData(messInfo.getChatData());
        for (GameServerInfo info : chatServerList) {
            ChannelHandlerContext socket = Manager.gameServerManager.GetSession(info.getPlatName(), info.getServerId());
            if (socket == null) {
                log.error("跨服聊天不能发送的服务器id = " + info.getServerIp() + "，platName = " + info.getPlatName());
                continue;
            }
            MessageUtils.send_to_game(socket, CrossServerMessage.P2GResChatMess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    /**
     * 服务器广播notice
     *
     * @param messInfo
     */
    @Override
    public void F2PServerNotice(ChatMessage.F2PServerNotice messInfo) {

        ChatMessage.P2GameServerNotice.Builder msg = ChatMessage.P2GameServerNotice.newBuilder();
        msg.setType(messInfo.getType());
        msg.setContent(messInfo.getContent());
        msg.addAllValue(messInfo.getValueList());
        msg.addAllChatChannelList(messInfo.getChatChannelListList());

        String serverKey = messInfo.getPlat() + "_" + messInfo.getServerId();

        int groupID = ServerMatchManager.deal().getGroupIDForCurOpenDay(serverKey, DailyActiveDefine.CrossFud);

        FudGroup group = Manager.fudManager.getGroups().get(groupID);
        if (group == null) {
            return;
        }
        for (Map.Entry<Integer, Integer> set : group.getServerCamp().entrySet()) {
            int serverId = set.getKey();
            ServerInfo serverInfo = Manager.gameServerManager.getServerCache().get(serverKey);
            ChannelHandlerContext socket = Manager.gameServerManager.GetSession(serverInfo.getPlatName(), serverId);
            if (socket == null) {
                log.error("跨服Notice不能发送的服务器id = " + serverInfo.getServerIp() + "，platName = " + serverInfo.getPlatName());
                continue;
            }
            MessageUtils.send_to_game(socket, ChatMessage.P2GameServerNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    /**
     * 社交服务器注册
     *
     * @param context
     * @param messInfo
     */
    @Override
    public void S2PRegisterServerHandler(ChannelHandlerContext context, serverMessage.S2PRegisterServer messInfo) {
        serverMessage.gameServerInfo sinfo = messInfo.getSinfo();

        ServerInfo info = new ServerInfo();
        info.setPlatName(sinfo.getPlatformMark());
        info.setServerId(sinfo.getServerId());
        info.setServerType(sinfo.getServerType());
        info.setServerIp(sinfo.getServerIP());
        info.setPort(sinfo.getServerPort());
        info.setVersion(sinfo.getVersion());
        info.setSession(context);
        info.getMapIds().addAll(sinfo.getMapIdsList());
        info.setOpenTime(sinfo.getServerOpentime());
        info.setServerWorldLv(sinfo.getServerWorldlv());

        Manager.gameServerManager.socialServer = info;

        context.channel().attr(SessionKey.SERVERID).set(info.getServerId());
        context.channel().attr(SessionKey.SERVERPLATID).set(info.getPlatName() + "_" + info.getServerId());
        context.channel().attr(SessionKey.SERVERPLAT).set(info.getPlatName());

        serverMessage.P2SRegisterCallback.Builder msg = serverMessage.P2SRegisterCallback.newBuilder();
        msg.setPublicId(ServerConfig.getServerId());
        msg.setPublicName(ServerConfig.getServerName());
        MessageUtils.send_to_social(serverMessage.P2SRegisterCallback.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        serverMessage.P2GResFightServerList.Builder ms = serverMessage.P2GResFightServerList.newBuilder();
        ms.setSocial(pack(info));
        GameServerManager.getInstance().send_all_game(serverMessage.P2GResFightServerList.MsgID.eMsgID_VALUE, ms.build().toByteArray());

        ms.clear();
        if (Manager.gameServerManager.socialServer != null) {
            ms.setSocial(pack(Manager.gameServerManager.socialServer));
        }
        GameServerManager.getInstance().send_all_FightGame(serverMessage.P2GResFightServerList.MsgID.eMsgID_VALUE, ms.build().toByteArray());
        log.info("注册 社交服务器 server={}｝", info);
    }

    /**
     * 发送所有的战斗服列表到游戏服
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnG2PReqFightServerList(ChannelHandlerContext context, serverMessage.G2PReqFightServerList mess) {
        List<ServerInfo> slist = GameServerManager.getInstance().GetType(ServerType.FIGHTSERVER);
        if (slist == null) {
            return;
        }
        serverMessage.P2GResFightServerList.Builder ms = serverMessage.P2GResFightServerList.newBuilder();
        for (ServerInfo sinfo : slist) {
            ms.addInfoList(pack(sinfo));
        }
        if (Manager.gameServerManager.socialServer != null) {
            ms.setSocial(pack(Manager.gameServerManager.socialServer));
        }
        MessageUtils.send_to_game(context, serverMessage.P2GResFightServerList.MsgID.eMsgID_VALUE, ms.build().toByteArray());
    }
}
