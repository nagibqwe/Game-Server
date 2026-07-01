package common.chat;

import com.data.FunctionStart;
import com.data.FunctionVariable;
import com.data.Global;
import com.data.MessageString;
import com.game.chat.Manager.ChatManager;
import com.game.chat.log.ChatLog;
import com.game.chat.script.IChatScript;
import com.game.chat.structs.*;
import com.game.cooldown.structs.CooldownTypes;
import com.game.count.structs.VariantType;
import com.game.db.bean.ChatWordBean;
import com.game.db.bean.ForbidChatBean;
import com.game.db.bean.ForbidWordBean;
import com.game.guild.structs.Guild;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.structs.ServerStr;
import com.game.team.structs.TeamInfo;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import com.google.protobuf.InvalidProtocolBufferException;
import game.core.dblog.LogService;
import game.core.net.Config.ServerConfig;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.ChatMessage;
import game.message.ChatMessage.*;
import game.message.CrossServerMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.game.chat.Manager.ChatManager.*;

/**
 * 聊天脚本
 */
public class ChatScript implements IChatScript {

    final Logger log = LogManager.getLogger("ChatManager");

    final int LeaveMsg_Count = 20;//留言条数
    final int MultiMediaTime = 20 * 60;//多媒体数据存在时间,20分钟
    final int WorldMsgCacheNum = 10;//世界频道消息缓存数量

    @Override
    public int getId() {
        return ScriptEnum.ChatBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void OnChatGetContentCS(Player player, ChatGetContentCS messInfo) {
        long keyId = Long.parseLong(messInfo.getKey());
        MultiMedia media = Manager.chatManager.getMultiMediaList().get(keyId);
        if (media == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GetContentFailed);
        } else {
            ChatGetContentSC.Builder msg = ChatGetContentSC.newBuilder();
            msg.setVoice(media.getCondition());
            msg.setVoiceKey(messInfo.getKey());
            MessageUtils.send_to_player(player, ChatGetContentSC.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    private boolean inForbidTime(ForbidChatBean bean) {
        if (bean.getEndTime() == -1) {
            return true;
        }
        return bean.getEndTime() > TimeUtils.Time();
    }

    private String replaceContent(String content, int type) {
        String fixContent = content.toLowerCase();
        String tmp = content.toLowerCase();
        for (ChatWordBean bean : Manager.chatManager.getChatWords().values()) {
            if (bean.getType() != type) {
                continue;
            }
            if (tmp.contains(bean.getWord().toLowerCase())) {
                fixContent = Utils.replaceAll(tmp, bean.getWord().toLowerCase(), bean.getReplace());
                return fixContent;
            }
        }
        return fixContent;
    }

    private boolean inChatBlackList(long userId) {
        return Manager.chatManager.getChatBlackList().containsKey(userId);
    }

    @Override
    public void OnChatReqCS(Player player, ChatReqCS messInfo) {
        if (GameServer.getInstance().IsFightServer()) {
            if (messInfo.getCondition().substring(0, 1).contains("&")) {
                Manager.gmCommandManager.clientGmDeal(player, messInfo.getCondition());
                return;
            }
        }
        //GM判断
        if ((player.isGM() || ServerConfig.isTestServer()) && (messInfo.getCondition().substring(0, 1).contains("&"))) {
            Manager.gmCommandManager.clientGmDeal(player, messInfo.getCondition());
            return;
        }
        if (messInfo.getChattype() == CHATTYPE_WORD && messInfo.getCondition().length() > CHATWORD_MAX) {
            log.error("发送的文字内容太长 sendPlayerId:" + player.getId());
            return;
        }
        if (messInfo.getChattype() == CHATTYPE_VOICE && messInfo.getCondition().length() > CHATVOICE_MAX) {
            log.error("发送的语音内容太长 sendPlayerId:" + player.getId());
            return;
        }
        ChatChannel channel = ChatChannel.find(messInfo.getChatchannel());

        String fixContent = "";
        int forbidType = 0;
        ForbidChatBean bean = Manager.chatManager.getForbids().get(player.getUserId());
        //禁言判断
        if (bean != null && inForbidTime(bean)) {
//            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.BAN_WORD_TITLE);
            if (bean.getForbidType() == ForbidChatType.Studio) {
                forbidType = ForbidChatType.Studio;
            } else if (bean.getForbidType() == ForbidChatType.Replace_All) {
                fixContent = replaceContent(messInfo.getCondition(), 1);
                forbidType = ForbidChatType.Replace_All;
            } else if (bean.getForbidType() == ForbidChatType.Replace_Word) {
                fixContent = replaceContent(messInfo.getCondition(), 0);
                forbidType = ForbidChatType.Replace_Word;
            } else if (bean.getForbidType() == ForbidChatType.Common) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.BAN_WORD_TITLE);
                return;
            } else if (bean.getForbidType() == ForbidChatType.Invisible) {
                //发送给自己
                PlayerWorldInfo target = Manager.playerManager.getPlayerWorldInfo(player.getId());
                if (ChatChannel.CHATCHANNEL_ROLE.equals(channel)) {
                    target = Manager.playerManager.getPlayerWorldInfo(messInfo.getRecRoleId());
                }
                sendChatMessage(player, target, channel, messInfo.getChattype(), messInfo.getCondition(), 0);
                return;
            } else if (bean.getForbidType() == ForbidChatType.Quarantine) {
                if (!(ChatChannel.CHATCHANNEL_ROLE.equals(channel) || ChatChannel.CHATCHANNEL_TEAM.equals(channel))) {
                    //无法在该频道发言
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.BAN_WORD_TITLE);
                    return;
                }
            }
        }
        //判断聊天冷却时间
        long cdTime = Manager.cooldownManager.getCooldownTime(player, CooldownTypes.Chat_CD, channel.getChannel());
        if (cdTime > 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ChatColding, (cdTime + 999) / 1000);
            return;
        }
        int add = channel.equals(ChatChannel.CHATCHANNEL_WORLD) ? Global.World_Chat_Interval : Global.Private_Chat_Interval;
        Manager.cooldownManager.addCooldown(player, CooldownTypes.Chat_CD, channel.getChannel(), add);

        //是否能发送
        boolean canSend = canSendChatMsg(player, channel, messInfo);
        if (!canSend) {
            return;
        }

        //BI用字段
        long recAccountId = 0l;
        long recRoleId = 0L;
        String recRoleName = "";
        String recRoleIP = "";
        String recRoleDevId = "";
        PlayerWorldInfo info = Manager.playerManager.getPlayerWorldInfo(messInfo.getRecRoleId());
        if (ChatChannel.CHATCHANNEL_ROLE.equals(channel)) {
            Player p = Manager.playerManager.getPlayerCache(messInfo.getRecRoleId());
            if (p != null) {
                recRoleId = p.getId();
                recAccountId = p.getUserId();
                recRoleName = p.getName();
                recRoleIP = p.getLoginIP();
                recRoleDevId = p.getMaCode();
            } else if (info != null) {
                recRoleId = info.getRoleid();
                recRoleName = info.getRolename();
            }
        } else if (ChatChannel.CHATCHANNEL_GUILD.equals(channel)) {
            Guild guild = Manager.guildsManager.GetGuildByPlayer(player);
            if (guild != null) {
                recRoleId = guild.getId();
                recRoleName = guild.getName();
            }
        }
        ChatResInfo.Builder cb = MakeChatResInfoBuilder(player, info, channel, 0, "", 0);
        ChatMessage.ChatResSC.Builder msg = ChatMessage.ChatResSC.newBuilder();
        msg.setInfo(cb);

        msg.getInfoBuilder().setOcc(player.getCareer());
        msg.getInfoBuilder().setChatchannel(messInfo.getChatchannel());
        msg.getInfoBuilder().setChattype(messInfo.getChattype());
        msg.getInfoBuilder().setCondition(messInfo.getCondition());
        if (messInfo.getChattype() != CHATTYPE_WORD) {
            long key = IDConfigUtil.getLogId();
            addMultiMedia(key, messInfo.getCondition(), messInfo.getVoiceLen());
            msg.getInfoBuilder().setCondition("" + key + "|" + messInfo.getVoiceLen());
        }

        //跨服聊天
        if (ChatChannel.CHATCHANNEL_CROSS.equals(channel)) {
            if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.CrossChat)) {
                return;
            }
            CrossServerMessage.G2PReqChatMess.Builder builder = CrossServerMessage.G2PReqChatMess.newBuilder();
            msg.getInfoBuilder().setChaterSid(ServerConfig.getServerId());
            builder.setChatData(msg.build().toByteString());
            MessageUtils.send_to_public(CrossServerMessage.G2PReqChatMess.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        } else {
            sendToChat(player, messInfo.getRecRoleId(), channel, msg, forbidType, fixContent);
        }
        Manager.biManager.getScript().biChat(player, msg.getInfoBuilder().getChatchannel(), 2, msg.getInfoBuilder().getCondition(), recAccountId, recRoleId, recRoleName, recRoleIP, recRoleDevId);
        Manager.biManager.get4399Script().chatBiTo4399(player, msg.getInfoBuilder().getChatchannel(), msg.getInfoBuilder().getCondition(), messInfo.getChattype() == CHATTYPE_VOICE ? 0 : 1,
                getChatTarget(player, channel, messInfo));
        //LOGGER
        writeLog(player, messInfo);
    }

    /**
     * BI获取聊天对象
     */
    private long getChatTarget(Player player, ChatChannel channel, ChatReqCS messInfo) {
        long target = 0L;
        if (ChatChannel.CHATCHANNEL_GUILD.equals(channel)) {
            Guild guild = Manager.guildsManager.GetGuildByPlayer(player);
            target = guild == null ? 0 : guild.getId();
        }
        if (ChatChannel.CHATCHANNEL_ROLE.equals(channel)) {
            target = messInfo.getRecRoleId();
        }
        return target;
    }

    //检查是否能发送对应频道的消息
    private boolean canSendChatMsg(Player player, ChatChannel channel, ChatReqCS messInfo) {
        boolean canSend = false;
        switch (channel) {
            case CHATCHANNEL_WORLD:
                canSend = getChatMsgToWorldBuilder(player);
                break;
            case CHATCHANNEL_GUILD:
                canSend = getChatMsgToGuildBuilder(player);
                break;
            case CHATCHANNEL_TEAM:
                canSend = getChatMsgToTeamBuilder(player);
                break;
            case CHATCHANNEL_ROLE:
                canSend = getChatMsgToPlayerBuilder(player, messInfo);
                break;
            case CHATCHANNEL_CURMAP:
                canSend = getChatMsgToCurMapBuilder(player, messInfo);
                break;
            case CHATCHANNEL_CROSS:
                canSend = true;
                break;
            default:
                log.error("错误的聊天消息类型 :" + messInfo.getChatchannel());
                break;
        }
        return canSend;
    }

    //世界
    private boolean getChatMsgToWorldBuilder(Player player) {
        int level = Global.ChatWorldLevel.get(0);
        int vipLevel = Global.ChatWorldLevel.get(1);
        if (player.getLevel() < level && player.getVipLv() < vipLevel) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CHATERROR_LEVELNOTENOUGH, ServerStr.getLevelNameHighStr(level), ServerStr.getLevelNameSlowStr(level), vipLevel + "");
            return false;
        }
        return true;
    }

    //公会
    private boolean getChatMsgToGuildBuilder(Player player) {
        Guild guild = Manager.guildsManager.GetGuildByPlayer(player);
        if (guild == null) {
            log.error("没有公会 不能发送公会聊天信息 sendPlayerId:" + player.getId());
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CHATERROR_NoGuild);
            return false;
        }
        return true;
    }

    //当前地图聊天
    private boolean getChatMsgToCurMapBuilder(Player player, ChatReqCS messInfo) {
        int level = Global.ChatWorldLevel.get(0);
        int vipLevel = Global.ChatWorldLevel.get(1);
        if (player.getLevel() < level && player.getVipLv() < vipLevel) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CHATERROR_LEVELNOTENOUGH, ServerStr.getLevelNameHighStr(level), ServerStr.getLevelNameSlowStr(level), vipLevel + "");
            return false;
        }
        return true;
    }

    //私聊
    private boolean getChatMsgToPlayerBuilder(Player player, ChatReqCS messInfo) {
        if (messInfo.getRecRoleId() == 0) {
            log.error("发送私聊消息时 传入接受者id为0 sendPlayerId:" + player.getId());
            return false;
        }

        int level = Global.ChatWorldLevel.get(0);
        int vipLevel = Global.ChatWorldLevel.get(1);
        if (player.getLevel() < level && player.getVipLv() < vipLevel) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CHATERROR_LEVELNOTENOUGH, ServerStr.getLevelNameHighStr(level), ServerStr.getLevelNameSlowStr(level), vipLevel + "");
            return false;
        }
        if (Manager.friendManager.isShield(player.getId(), messInfo.getRecRoleId())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CHATERROR_FORBIDDEN);
            return false;
        }
        if (Manager.friendManager.isShield(messInfo.getRecRoleId(), player.getId())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CHATERROR_FORBIDDEN);
            return false;
        }
        return true;
    }

    //组队
    private boolean getChatMsgToTeamBuilder(Player player) {
        if (player.getTeamId() == 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CHATERROR_NOTEAM);
            return false;
        }
        TeamInfo teaminfo = Manager.teamManager.getTeam(player.getTeamId());
        if (teaminfo == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CHATERROR_NOTEAM);
            return false;
        }
        if (teaminfo.getMembers().isEmpty()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CHATERROR_NOTEAM);
            return false;
        }
        return true;
    }

    //添加
    private void addMultiMedia(long key, String str, int len) {
        MultiMedia media = new MultiMedia();
        media.setCondition(str);
        media.setPlayTime(len);
        media.setTime((int) (TimeUtils.Time() / 1000));
        Manager.chatManager.getMultiMediaList().put(key, media);
        log.error(key + " = 收到语音的数据长度：" + str.getBytes().length + ", len=" + str.length() + " ,play len =" + len);
    }

    public void sendToChat(Player player, long recRoleId, ChatChannel channel, ChatResSC.Builder msg, int forbidType, String fixContent) {
        String content = msg.getInfoBuilder().getCondition();
        switch (channel) {
            case CHATCHANNEL_WORLD: {
                List<Player> plist = new ArrayList<>(Manager.playerManager.getOnLines());
                Iterator<Player> iterator = plist.iterator();
                while (iterator.hasNext()) {
                    Player pp = iterator.next();
                    if (isWorldForbidden(player, pp, forbidType)) {
                        iterator.remove();
                        continue;
                    }
                    if (Manager.friendManager.isShield(player.getId(), pp.getId())) {
                        iterator.remove();
                    }
                }

                sendMsgToPlayers(player, msg, plist, forbidType, fixContent, content);

                Manager.countManager.addVariant(player, VariantType.WorldChat, 1);
                Manager.controlManager.operate(player, FunctionVariable.WorldChat, 1);

                saveWorldMsgCache(ChatChannel.CHATCHANNEL_WORLD, LeaveMsg.makeLeaveMsg(player, 0, content, recRoleId, ChatChannel.CHATCHANNEL_WORLD), 0);
            }
            break;
            case CHATCHANNEL_TEAM: {
                if (player.getTeamId() == 0) {
                    return;
                }
                TeamInfo info = Manager.teamManager.getTeam(player.getTeamId());
                if (info == null) {
                    return;
                }
                for (Long roleId : info.getMembers()) {
                    Player member = Manager.playerManager.getPlayerOnline(roleId);
                    if (member == null) {
                        continue;
                    }
                    if (isForbidden(player, member, forbidType)) continue;
                    if (Manager.friendManager.isShield(player.getId(), roleId)) {
                        continue;
                    }

                    sendMsgToPlayer(player, msg, forbidType, fixContent, content, member);
                }
            }
            break;
            case CHATCHANNEL_ROLE: {
                Player recPlayer = Manager.playerManager.getPlayerCache(recRoleId);
                long rUserId;
                if (recPlayer == null) {
                    PlayerWorldInfo rpwi = Manager.playerManager.getPlayerWorldInfo(recRoleId);
                    if (rpwi == null) {
                        return;
                    }
                    rUserId = rpwi.getUserId();
                } else {
                    rUserId = recPlayer.getUserId();
                }

                if (isForbidden(player.getId(), recRoleId, forbidType, false)) {
                    MessageUtils.send_to_player(player, ChatResSC.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                    break;
                }
                if (forbidType == ForbidChatType.Replace_All || forbidType == ForbidChatType.Replace_Word) {
                    if (!inChatBlackList(rUserId)) {
                        msg.getInfoBuilder().setCondition(fixContent);
                    }
                }
                Manager.friendManager.deal().addLatelyPlayer(player.getId(), recRoleId);
                Manager.friendManager.deal().addLatelyPlayer(recRoleId, player.getId());
                leaveMsg(recRoleId, LeaveMsg.makeLeaveMsg(player, 0, content, recRoleId, ChatChannel.CHATCHANNEL_ROLE));
                leaveMsg(player.getId(), LeaveMsg.makeLeaveMsg(player, 0, content, recRoleId, ChatChannel.CHATCHANNEL_ROLE));

                //聊天增加亲密度
                long chatCount = Manager.countManager.getVariant(player, VariantType.ChAT_WITH_FRIEND_ADD_INTIMACY);
                if (chatCount < Global.DailyLoveTimes) {
                    boolean isAdd = Manager.friendManager.deal().addIntimacy(player, recRoleId, Global.PrivatelyChatLove);
                    if (isAdd) {
                        Manager.countManager.addVariant(player, VariantType.ChAT_WITH_FRIEND_ADD_INTIMACY, 1);
                    }
                }

                MessageUtils.send_to_player(recPlayer, ChatResSC.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                msg.getInfoBuilder().setCondition(content);
                MessageUtils.send_to_player(player, ChatResSC.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
            break;
            case CHATCHANNEL_GUILD: {
                Guild guild = Manager.guildsManager.GetGuildByPlayer(player);
                List<Player> ps = new ArrayList<>();
                for (long roleId : guild.getMembers().keySet()) {
                    Player pp = Manager.playerManager.getPlayerOnline(roleId);
                    if (pp == null) {
                        continue;
                    }
                    if (isForbidden(player, pp, forbidType)) continue;
                    if (Manager.friendManager.isShield(player.getId(), roleId)) {
                        continue;
                    }
                    ps.add(pp);
                }
                if (ps.isEmpty()) {
                    return;
                }
                sendMsgToPlayers(player, msg, ps, forbidType, fixContent, content);

                Manager.countManager.addVariant(player, VariantType.GuildChat, 1);
                Manager.controlManager.operate(player, FunctionVariable.GuildChat, 1);

                saveWorldMsgCache(ChatChannel.CHATCHANNEL_GUILD, LeaveMsg.makeLeaveMsg(player, 0, content, recRoleId, ChatChannel.CHATCHANNEL_GUILD), guild.getId());
            }
            break;
            case CHATCHANNEL_CURMAP: {
                MapObject map = MapManager.getInstance().getMap(player.gainMapId());
                List<Player> pps = new ArrayList<>(map.getPlayers().values());
                sendMsg(player, msg, pps, forbidType, fixContent);
            }
            break;
            default: {
                log.error("错误的聊天消息类型 :" + channel);
//                List<Player> plist = new ArrayList<>(Manager.playerManager.getOnLines());
//                sendMsg(player, msg, plist, forbidType, fixContent);
            }
            break;
        }
    }

    //留言信息处理
    public void saveWorldMsgCache(ChatChannel channel, LeaveMsg msg, long guildId) {
        List<LeaveMsg> leaveMsgs;
        if (ChatChannel.CHATCHANNEL_GUILD.equals(channel)) {
            leaveMsgs = Manager.chatManager.getGuildHistoryMsgCacheList().computeIfAbsent(guildId, k -> new ArrayList<>());
        } else {
            leaveMsgs = Manager.chatManager.getWorldHistoryMsgCacheList().computeIfAbsent(channel, k -> new ArrayList<>());
        }
        if (leaveMsgs.size() >= WorldMsgCacheNum) {
            leaveMsgs.remove(0);
            leaveMsgs.add(msg);
        } else {
            leaveMsgs.add(msg);
        }
    }

    private void sendMsg(Player player, ChatResSC.Builder msg, List<Player> plist, int forbidType, String fixContent) {
        String content = msg.getInfoBuilder().getCondition();
        Iterator<Player> iterator = plist.iterator();
        while (iterator.hasNext()) {
            Player pp = iterator.next();
            if (pp == null) {
                continue;
            }
            if (isForbidden(player, pp, forbidType)) {
                iterator.remove();
                continue;
            }
            if (Manager.friendManager.isShield(player.getId(), pp.getId())) {
                iterator.remove();
            }
        }
        sendMsgToPlayers(player, msg, plist, forbidType, fixContent, content);
    }

    private void sendMsgToPlayers(Player player, ChatResSC.Builder msg, List<Player> plist, int forbidType, String fixContent, String content) {
        for (Player p : plist) {
            sendMsgToPlayer(player, msg, forbidType, fixContent, content, p);
        }
    }

    private void sendMsgToPlayer(Player player, ChatResSC.Builder msg, int forbidType, String fixContent, String content, Player p) {
        if ((forbidType == ForbidChatType.Replace_All || forbidType == ForbidChatType.Replace_Word) && player.getId() != p.getId() && !inChatBlackList(p.getUserId())) {
            msg.getInfoBuilder().setCondition(fixContent);
        } else {
            msg.getInfoBuilder().setCondition(content);
        }
        MessageUtils.send_to_player(p, ChatResSC.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private boolean isForbidden(Player sendPlayer, Player toPlayer, int forbidType) {
        if (forbidType == ForbidChatType.Studio) {//发送人被禁
            //只发给自己和被标记用户
            if (sendPlayer.getId() == toPlayer.getId()) {
                return false;
            } else {
                if (!inChatBlackList(toPlayer.getUserId())) {
                    return true;
                }
            }
        }
        //检查接收人
        ForbidChatBean bean = Manager.chatManager.getForbids().get(toPlayer.getUserId());
        if (bean == null) {
            return false;
        }
        if (bean.getForbidType() == ForbidChatType.Studio) {//工作室禁言用户无法接受正常用户信息
            if (inForbidTime(bean)) {
                return true;
            }
        }

        return false;
    }

    private boolean isWorldForbidden(Player sendPlayer, Player toPlayer, int forbidType) {
        if (forbidType == ForbidChatType.Studio) {//发送人被禁
            //只发给自己和被标记用户
            if (sendPlayer.getId() != toPlayer.getId()) {
                if (!inChatBlackList(toPlayer.getUserId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isForbidden(long sendId, long recvId, int forbidType, boolean isWorld) {
        PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(sendId);
        if (pwi == null) {
            return false;
        }
        PlayerWorldInfo rpwi = Manager.playerManager.getPlayerWorldInfo(recvId);
        if (rpwi == null) {
            return false;
        }
        if (forbidType == ForbidChatType.Studio) {//只能发给自己和被标记用户
            if (pwi.getRoleid() == rpwi.getRoleid()) {
                return false;
            }
            if (!inChatBlackList(rpwi.getUserId())) {
                return true;
            }
        }

        if (isWorld) {
            return false;
        }

        //检查接收人
        ForbidChatBean bean = Manager.chatManager.getForbids().get(rpwi.getUserId());
        if (bean == null) {
            return false;
        }
        if (bean.getForbidType() == ForbidChatType.Studio && inForbidTime(bean)) {//工作室禁言用户无法接受正常用户信息
            return true;
        }

        return false;
    }

    @Override
    public void sendLevelMsg(Player player) {
        HashMap<Long, List<LeaveMsg>> levelMsgMap = Manager.chatManager.getLeaveMsgList().get(player.getId());
        if (levelMsgMap == null || levelMsgMap.isEmpty()) {
            return;
        }
        boolean isBlack = Manager.chatManager.getChatBlackList().containsKey(player.getUserId());
        ChatResLeaveMessageSC.Builder msg = ChatResLeaveMessageSC.newBuilder();
        for (Map.Entry<Long, List<LeaveMsg>> entry : levelMsgMap.entrySet()) {
            for (LeaveMsg s : entry.getValue()) {
                if (player.getId() != s.getSendId()) {
                    PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(s.getSendId());
                    if (pwi != null) {
                        ForbidChatBean bean = Manager.chatManager.getForbids().get(pwi.getUserId());
                        if (bean != null) {
                            if (isForbidden(s.getSendId(), player.getId(), bean.getForbidType(), false)) {
                                continue;
                            }

                            if ((bean.getForbidType() == ForbidChatType.Replace_All || bean.getForbidType() == ForbidChatType.Replace_Word) && inForbidTime(bean)) {
                                if (!isBlack) {//正常用户发送替换内容
                                    String tmp = s.getCondition();
                                    s.setCondition(replaceContent(tmp, bean.getForbidType() == ForbidChatType.Replace_All ? 1 : 0));
                                    msg.addMsgList(s.toBuildInfo());
                                    s.setCondition(tmp);
                                    continue;
                                }
                            }

                        }
                    }
                }
                msg.addMsgList(s.toBuildInfo());
            }
        }
        MessageUtils.send_to_player(player, ChatResLeaveMessageSC.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void sendWorldLevelMsg(Player player) {
        boolean isBlack = Manager.chatManager.getChatBlackList().containsKey(player.getUserId());
        ChatMessage.ResWorldHistoryChatInfo.Builder builder = ChatMessage.ResWorldHistoryChatInfo.newBuilder();
        List<LeaveMsg> list = new ArrayList<>();
        for (List<LeaveMsg> cacheList : Manager.chatManager.getWorldHistoryMsgCacheList().values()) {
            list.addAll(cacheList);
        }
        if (player.isHaveGuild()) {
            List<LeaveMsg> leaveMsgs = Manager.chatManager.getGuildHistoryMsgCacheList().get(player.getGuildId());
            if (leaveMsgs != null && !leaveMsgs.isEmpty()) {
                list.addAll(leaveMsgs);
            }
        }
        for (LeaveMsg s : list) {
            if (player.getId() != s.getSendId()) {
                PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(s.getSendId());
                if (pwi != null) {
                    ForbidChatBean bean = Manager.chatManager.getForbids().get(pwi.getUserId());
                    if (bean != null) {
                        if (isForbidden(s.getSendId(), player.getId(), bean.getForbidType(), true)) {
                            continue;
                        }

                        if ((bean.getForbidType() == ForbidChatType.Replace_All || bean.getForbidType() == ForbidChatType.Replace_Word) && inForbidTime(bean)) {
                            if (!isBlack) {//正常用户发送替换内容
                                String tmp = s.getCondition();
                                s.setCondition(replaceContent(tmp, bean.getForbidType() == ForbidChatType.Replace_All ? 1 : 0));
                                builder.addMsgList(s.toBuildInfo());
                                s.setCondition(tmp);
                                continue;
                            }
                        }
                    }
                }
            }
            builder.addMsgList(s.toBuildInfo());
        }
        MessageUtils.send_to_player(player, ChatMessage.ResWorldHistoryChatInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 通知
     *
     * @param messInfo
     */
    @Override
    public void F2GameServerNotice(F2GameServerNotice messInfo) {

        ChatMessage.PersonalNotice.Builder msg = ChatMessage.PersonalNotice.newBuilder();
        msg.setType(messInfo.getType());
        msg.setContent(messInfo.getContent());
        msg.addAllChatChannelList(messInfo.getChatChannelListList());
        msg.addAllValue(messInfo.getValueList());

        for (Player player : Manager.playerManager.getOnLines()) {
            MessageUtils.send_to_player(player, PersonalNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public void P2GameServerNotice(P2GameServerNotice messInfo) {
        ChatMessage.PersonalNotice.Builder msg = ChatMessage.PersonalNotice.newBuilder();
        msg.setType(messInfo.getType());
        msg.setContent(messInfo.getContent());
        msg.addAllChatChannelList(messInfo.getChatChannelListList());
        msg.addAllValue(messInfo.getValueList());

        for (Player player : Manager.playerManager.getOnLines()) {
            MessageUtils.send_to_player(player, PersonalNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public void onRedAddChatRoom(Player player, ReqAddChatRooM mess) {
        int type = mess.getType();
        long typeId = mess.getTypeId();
        switch (type) {
            case 1:
                typeId = player.getTeamId();
                break;
            case 2:
                typeId = player.getGuildId();
                break;
        }
        synchronized (ChatManager.mediaObj) {
            MediaChatData mcd;
            if (Manager.chatManager.getMediaChatList().containsKey(typeId)) {
                mcd = Manager.chatManager.getMediaChatList().get(typeId);
            } else {
                mcd = new MediaChatData();
                mcd.setChatId(typeId);
                mcd.setRoomId(mess.getRoomId());
                mcd.setType(type);
                Manager.chatManager.getMediaChatList().put(typeId, mcd);
            }
            mcd.getRoleIds().add(player.getId());
        }
        ResAddChatRooM.Builder msg = ResAddChatRooM.newBuilder();
        msg.setState(0);
        msg.setType(type);
        MessageUtils.send_to_player(player, ResAddChatRooM.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onReqChatRoom(Player player, ReqChatRoom mess) {
        int type = mess.getType();
        long typeId = 0;
        switch (type) {
            case 1:
                typeId = player.getTeamId();
                break;
            case 2:
                typeId = player.getGuildId();
                break;
        }

        if (typeId < 1) {
            return;
        }
        String roomId = "";
        synchronized (ChatManager.mediaObj) {
            if (Manager.chatManager.getMediaChatList().containsKey(typeId)) {
                roomId = Manager.chatManager.getMediaChatList().get(typeId).getRoomId();
            }
        }
        ResChatRoom.Builder msg = ResChatRoom.newBuilder();
        msg.setRoomId(roomId);
        msg.setType(type);
        msg.setTypeId(typeId);
        MessageUtils.send_to_player(player, ResChatRoom.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onReqExitChatRoom(Player player, ReqExitChatRoom mess) {
        int type = mess.getType();
        long typeId = 0;
        switch (type) {
            case 1:
                typeId = player.getTeamId();
                break;
            case 2:
                typeId = player.getGuildId();
                break;
        }
        boolean issucess = outChatRoom(typeId, player.getId());
        ResExitChatRoom.Builder msg = ResExitChatRoom.newBuilder();
        msg.setType(type);
        msg.setState(issucess ? 0 : 1);
        MessageUtils.send_to_player(player, ResExitChatRoom.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void playerLogout(Player player) {
        long typeId = player.getTeamId();
        outChatRoom(typeId, player.getId());//退出组队房间
        outChatRoom(player.getGuildId(), player.getId());//退出帮会房间
    }

    private boolean outChatRoom(long typeId, long roleId) {
        if (typeId < 1) {
            return false;
        }
        if (roleId < 1) {
            return false;
        }
        synchronized (ChatManager.mediaObj) {
            MediaChatData mcd;
            if (Manager.chatManager.getMediaChatList().containsKey(typeId)) {
                mcd = Manager.chatManager.getMediaChatList().get(typeId);
                mcd.getRoleIds().remove(roleId);
                log.error("语音房间" + mcd.getRoomId() + "的玩家ID：" + roleId + "退出房间！");
                if (mcd.getRoleIds().size() < 1) {
                    Manager.chatManager.getMediaChatList().remove(typeId);
                    log.error("语音房间" + mcd.getRoomId() + "的玩家少而解散");
                }
            }
        }
        return false;
    }

    @Override
    public boolean inChatRoom(long id, long roleId) {
        if (Manager.chatManager.getMediaChatList().containsKey(id)) {
            MediaChatData mcd = Manager.chatManager.getMediaChatList().get(id);
            return mcd.getRoleIds().contains(roleId);
        }
        return false;
    }

    @Override
    public void onP2GResChatMess(CrossServerMessage.P2GResChatMess mess) {
        //保存跨服聊天消息
        try {
            ChatResSC chatResSC = ChatResSC.parseFrom(mess.getChatData());

            LeaveMsg leaveMsg = LeaveMsg.makeLeaveMsg(chatResSC.getInfo().getChater(), chatResSC.getInfo().getChatername(), chatResSC.getInfo().getOcc(),
                    chatResSC.getInfo().getChaterlevel(), 0, chatResSC.getInfo().getCondition(), chatResSC.getInfo().getReceiver(), ChatChannel.CHATCHANNEL_CROSS, chatResSC.getInfo().getChaterSid(), chatResSC.getInfo().getChatBgId());
            if (chatResSC.getInfo().getHead() != null) {
                leaveMsg.setHeadId(chatResSC.getInfo().getHead().getFashionHead());
                leaveMsg.setUseCustomHead(chatResSC.getInfo().getHead().getUseCustomHead());
                leaveMsg.setHeadRoundId(chatResSC.getInfo().getHead().getFashionFrame());
                leaveMsg.setCustomHeadPath(chatResSC.getInfo().getHead().getCustomHeadPath());
            }
            saveWorldMsgCache(ChatChannel.CHATCHANNEL_CROSS, leaveMsg, 0);

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        for (Player player : Manager.playerManager.getOnLines()) {
            if (Manager.controlManager.deal().isOpenFunction(player, FunctionStart.CrossChat)) {
                MessageUtils.send_to_player(player, ChatMessage.ChatResSC.MsgID.eMsgID_VALUE, mess.getChatData().toByteArray());
            }
        }
    }

    /**
     * 留言信息处理
     *
     * @param recvPlayerId
     * @param msg
     */
    @Override
    public void leaveMsg(long recvPlayerId, LeaveMsg msg) {
        HashMap<Long, List<LeaveMsg>> levelMsgMap;
        List<LeaveMsg> msgList;
        long friendId = recvPlayerId == msg.getSendId() ? msg.getReceiverId() : msg.getSendId();
        if (Manager.chatManager.getLeaveMsgList().containsKey(recvPlayerId)) {
            levelMsgMap = Manager.chatManager.getLeaveMsgList().get(recvPlayerId);
            if (!levelMsgMap.containsKey(friendId)) {
                levelMsgMap.put(friendId, new ArrayList<>());
            }
            msgList = levelMsgMap.get(friendId);
            if (msgList.size() >= LeaveMsg_Count) {
                msgList.remove(0);
                msgList.add(msg);
            } else {
                msgList.add(msg);
            }
        } else {
            levelMsgMap = new HashMap<>();
            msgList = new ArrayList<>();
            msgList.add(msg);
            levelMsgMap.put(friendId, msgList);
        }
        Manager.chatManager.getLeaveMsgList().put(recvPlayerId, levelMsgMap);
    }

    /**
     * 检测多媒体
     */
    @Override
    public void tickMultiMedia() {
        int now = (int) (TimeUtils.Time() / 1000);
        Iterator it = Manager.chatManager.getMultiMediaList().values().iterator();
        while (it.hasNext()) {
            MultiMedia m = (MultiMedia) it.next();
            if (now - m.getTime() >= MultiMediaTime) {
                it.remove();
            }
        }
    }

    /**
     * 发送消息到系统频道
     * @param player
     * @param target
     * @param channel
     * @param type
     * @param str
     * @param messageStr
     */
    @Override
    public void sendChatMessage(Player player, PlayerWorldInfo target, ChatChannel channel, int type, String str, int messageStr) {

        ChatResInfo.Builder cb = MakeChatResInfoBuilder(player, target, channel, type, str, messageStr);

        ChatMessage.ChatResSC.Builder msg = ChatMessage.ChatResSC.newBuilder();
        msg.setInfo(cb);
        MessageUtils.send_to_player(player, ChatMessage.ChatResSC.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 发送屏蔽字
     *
     * @param player
     */
    @Override
    public void sendForbidWord(Player player) {
        ChatMessage.ResForbidWord.Builder builder = ChatMessage.ResForbidWord.newBuilder();
        builder.setType(0);
        for (ForbidWordBean bean : Manager.chatManager.getForbidWords().values()) {
            builder.addWordList(bean.getWord());
        }
        MessageUtils.send_to_player(player, ChatMessage.ResForbidWord.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 组装聊天消息
     *
     * @param sender
     * @param recv
     * @param channel
     * @param type
     * @param str
     * @param messageStr
     * @return
     */
    @Override
    public ChatResInfo.Builder MakeChatResInfoBuilder(Player sender, PlayerWorldInfo recv, ChatChannel channel, int type, String str, int messageStr) {
        ChatMessage.ChatResInfo.Builder msg = ChatMessage.ChatResInfo.newBuilder();
        msg.setChatchannel(channel.getChannel());
        msg.setChattype(type);
        msg.setCondition(str);
        msg.setSystemId(messageStr);
        if (sender != null) {
            msg.setChater(sender.getId());
            msg.setChatername(sender.getName());
            msg.setVipLv(0);
            msg.setOcc(sender.getCareer());
            msg.setChaterlevel(sender.getLevel());
            msg.setHead(MapUtils.getHead(sender));
            msg.setChatBgId(Manager.newFashionManager.deal().getQiPao(sender));
        } else {
            msg.setChater(0);
            msg.setChatername("");
            msg.setVipLv(0);
            msg.setOcc(0);
            msg.setChaterlevel(0);
            msg.setHead(MapUtils.getHead(0, 0, "", false));
            msg.setChatBgId(0);
        }
        if (recv != null) {
            msg.setReceiver(recv.getRoleid());
            msg.setReceivername(recv.getRolename());
            msg.setReceiverLevel(recv.getLevel());
            msg.setReceiverVipLv(recv.getPlayerVip());
            msg.setChatBgId(0);
        } else {
            msg.setReceiver(0);
            msg.setReceiverVipLv(0);
            msg.setReceivername("");
            msg.setReceiverLevel(0);
        }
        return msg;
    }

    private void writeLog(Player player, ChatReqCS messInfo) {
        try {
            if (messInfo.getChattype() != CHATTYPE_WORD) {
                return;
            }
            ChatLog chatLog = new ChatLog();
            chatLog.setChannel(messInfo.getChatchannel());
            chatLog.setContent(messInfo.getCondition().replace("'", "‘"));
            chatLog.setReceRoleId(messInfo.getRecRoleId());
            chatLog.setPlayer(player);
            chatLog.setLevel(player.getLevel());
            chatLog.setIp(player.getLoginIP());
            LogService.getInstance().execute(chatLog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

}
