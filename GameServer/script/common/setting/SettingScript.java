package common.setting;

import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.game.chat.structs.Notify;
import com.game.db.bean.ServerNameBean;
import com.game.db.dao.ServerNameDao;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.setting.log.FeedbackLog;
import com.game.setting.script.ISettingScript;
import com.game.setting.struct.FeedBackInfo;
import com.game.setting.struct.SettingType;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.utils.Utils;
import game.core.dblog.LogService;
import game.core.net.Config.ServerConfig;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import game.message.SettingMessage;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SettingScript implements ISettingScript {

    @Override
    public int getId() {
        return ScriptEnum.SettingBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void playerOnline(Player player) {
        //发送设置信息
        sendSettingInfo(player);

        ConcurrentHashMap<Long, List<FeedBackInfo>> feedbackMap = Manager.settingManager.getFeedbackMap();
        SettingMessage.ResGMFeedback.Builder builder = SettingMessage.ResGMFeedback.newBuilder();
        if (!feedbackMap.containsKey(player.getId()) || feedbackMap.get(player.getId()).isEmpty()) {
            return;
        }
        for (FeedBackInfo info : feedbackMap.get(player.getId())) {
            SettingMessage.feedback.Builder feedback = SettingMessage.feedback.newBuilder();
            feedback.setType(info.getType());
            feedback.setContent(info.getContent());
            feedback.setTime(info.getTime());
            builder.addList(feedback);
        }
        MessageUtils.send_to_player(player, SettingMessage.ResGMFeedback.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        //从缓存中清除
        feedbackMap.remove(player.getId());
    }
    /**
     * 发送设置信息
     */
    private void sendSettingInfo(Player player) {
        ConcurrentHashMap<Integer, Boolean> settings = player.getSettings();
        if (settings.isEmpty()) {
            settings.put(SettingType.Auto_Sitting, false);
            settings.put(SettingType.Auto_FightBack, false);
            settings.put(SettingType.Auto_Rebrith, false);
            settings.put(SettingType.Auto_EatEquip, false);
            settings.put(SettingType.AutoJoinTeam, false);
            settings.put(SettingType.Auto_AddHookTime, false);
//            settings.put(SettingType.Auto_ChumInviteRefuse, false);
        }
        SettingMessage.ResSettingInfo.Builder builder = SettingMessage.ResSettingInfo.newBuilder();
        for (Map.Entry<Integer, Boolean> entry : settings.entrySet()) {
            SettingMessage.setting.Builder info = SettingMessage.setting.newBuilder();
            info.setType(entry.getKey());
            info.setValue(entry.getValue());
            builder.addList(info);
        }
        MessageUtils.send_to_player(player, SettingMessage.ResSettingInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onReqSendSetting(Player player, SettingMessage.ReqSendSetting mess) {
        ConcurrentHashMap<Integer, Boolean> settings = player.getSettings();
        for (SettingMessage.setting setting: mess.getListList()) {
            if (!settings.containsKey(setting.getType())) {
                continue;
            }
            settings.put(setting.getType(), setting.getValue());
        }
        sendSettingInfo(player);
    }

    @Override
    public void onReqCommitFeedback(Player player, SettingMessage.ReqCommitFeedback mess) {
        //反馈类型 1:游戏bug 2:优化建议 3:问题咨询 4:其他
        if (mess.getType() < 1 || mess.getType() > 4) {
            return;
        }
        //屏蔽字检查
        if (Utils.isForbiddenStr(mess.getContent())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ForbiddenString);
            return;
        }

        FeedBackInfo info = new FeedBackInfo();
        info.setType(mess.getType());
        info.setContent(mess.getContent());
        info.setTime((int) (TimeUtils.Time() / 1000));
        writeFeedBacklog(player, info, 0);

        SettingMessage.ResCommitFeedback.Builder builder = SettingMessage.ResCommitFeedback.newBuilder();
        builder.setSuccess(true);
        MessageUtils.send_to_player(player, SettingMessage.ResCommitFeedback.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void sendGMFeedback(long playerId, FeedBackInfo info) {
        if (!Manager.playerManager.getAllPlayerWorldInfo().containsKey(playerId)) {
            return;
        }
        Player player = Manager.playerManager.getPlayerOnline(playerId);
        ConcurrentHashMap<Long, List<FeedBackInfo>> feedbackMap = Manager.settingManager.getFeedbackMap();

        //写日志
        writeFeedBacklog(player, info, 1);

        //不在线，添加到缓存中
        if (player == null) {
            List<FeedBackInfo> list = feedbackMap.getOrDefault(playerId, new ArrayList<>());
            list.add(info);
            feedbackMap.put(playerId, list);
            return;
        }

        //在线直接发送
        SettingMessage.ResGMFeedback.Builder builder = SettingMessage.ResGMFeedback.newBuilder();
        SettingMessage.feedback.Builder feedback = SettingMessage.feedback.newBuilder();
        feedback.setType(info.getType());
        feedback.setContent(info.getContent());
        feedback.setTime(info.getTime());
        builder.addList(feedback);
        MessageUtils.send_to_player(player, SettingMessage.ResGMFeedback.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private void writeFeedBacklog(Player player, FeedBackInfo info, int sender) {
        FeedbackLog feedbackLog = new FeedbackLog();
        feedbackLog.setPlayer(player);
        feedbackLog.setSender(sender);
        feedbackLog.setType(info.getType());
        feedbackLog.setContent(info.getContent());
        feedbackLog.setSendTime(info.getTime());
        LogService.getInstance().execute(feedbackLog);
    }

    @Override
    public void changeServerName(Player player, SettingMessage.ReqChangeServerName messInfo) {
        String name = messInfo.getName();
        if (name == null || StringUtils.isBlank(name)) {
            return;
        }
        //检查名字长度(字符长度)
        int nameLength = length(name);
        int min = Global.ChangeServerNameLong.get(0);
        int max = Global.ChangeServerNameLong.get(1);
        if (nameLength < min || nameLength > max) {
            //名字长度不符合规定，请重新输入
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ChangedServerName, min + "", max + "");
            return;
        }
        //屏蔽标点符号检查
        if (Utils.isContainsShielding_symbol(name)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ChangedServerName1);
            return;
        }
        //屏蔽字检查
        if (Utils.isForbiddenStr(name)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.SensitiveWords);
            return;
        }
        //消耗物品
        if(!Manager.backpackManager.manager().onRemoveItem(player, Global.ChangeServerNameCost, 1, ItemChangeReason.ChangeServerNameDec, IDConfigUtil.getLogId())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
            return;
        }
        //发邮件
        String ServerName = ServerParamUtil.serverName.isEmpty() ? ServerConfig.getServerName() : ServerParamUtil.serverName;
        String content = MessageString.ChangedServerNameMailText + "@_@" + player.getName() + "@_@" + ServerName + "@_@" + name;
        for (PlayerWorldInfo other : Manager.playerManager.getAllPlayerWorldInfo().values()) {
            Manager.mailManager.sendMailToPlayer(other.getRoleid(), MailType.SysCommonRewardMail, MessageString.System, MessageString.ChangedServerNameMailTitle, content);
        }
        //更新数据
        ServerParamUtil.serverName = name;
        ServerParamUtil.saveServerName();

        ServerNameBean bean = new ServerNameBean();
        bean.setRoleId(player.getId());
        bean.setChangeName(name);
        bean.setChangeTime((int)(System.currentTimeMillis() / 1000));
        bean.setServerId(ServerConfig.getServerId());

        ServerNameDao dao = new ServerNameDao();
        if (dao.update(bean) < 1) {
            dao.insert(bean);
        }
        //向客户端同步消息
        SettingMessage.ResChangeServerNameSuccess.Builder clientMsg = SettingMessage.ResChangeServerNameSuccess.newBuilder();
        clientMsg.setServerId(ServerConfig.getServerId());
        clientMsg.setChangeName(name);
        MessageUtils.send_to_player(player, SettingMessage.ResChangeServerNameSuccess.MsgID.eMsgID_VALUE, clientMsg.build().toByteArray());
        //向公共服发消息
        CrossServerMessage.G2PServerNameChange.Builder msg = CrossServerMessage.G2PServerNameChange.newBuilder();
        msg.setServerId(ServerConfig.getServerId());
        msg.setPlat(ServerConfig.getServerPlatform());
        msg.setServerName(name);
        msg.addAllServerIdsList(ServerConfig.getServerIdList());
        MessageUtils.send_to_public(CrossServerMessage.G2PServerNameChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 得到一个字符串的长度,一个汉字或日韩文长度为2,英文字符长度为1
     */
    private int length(String s) {
        String newString;
        try {
            newString = new String(s.getBytes("gbk"), "ISO-8859-1");
            return newString.length();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return s.length();
    }
}
