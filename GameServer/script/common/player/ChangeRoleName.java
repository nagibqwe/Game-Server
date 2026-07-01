package common.player;

import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.game.activity.struct.ActivityConfig;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.Manager.ChatManager;
import com.game.chat.structs.Notify;
import com.game.guild.structs.Guild;
import com.game.manager.Manager;
import com.game.player.log.ChangeRoleNameLog;
import com.game.player.script.ImPlayerScript;
import com.game.player.structs.Player;
import com.game.player.structs.SavePlayerLevel;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.utils.Utils;
import game.core.dblog.LogService;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.message.PlayerMessage.ResChangeRoleNameResult;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 *
 * @author lw
 */
public class ChangeRoleName extends ImPlayerScript implements IScript {

    private static final Logger log = LogManager.getLogger(ChangeRoleName.class);

    @Override
    public int getId() {
        return ScriptEnum.ChangeRoleNameBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    //进行更改角色名
    @Override
    public void changeName(Player player, String newName) {
        //检查新名字
        if (!isNameOK(player, newName)) {
            return;
        }

        //检查下新名字能否存入数据库中(有些表情符可能存在无法存入数据库的情况)
        ServerParamUtil.roleNameTest = newName;
        int state = ServerParamUtil.saveRoleNameTest();
        if (state == 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ShieldName);
            log.error("角色名字插入数据库失败，表示此角色名字中含有特殊字符(笑脸、闪电这种)，角色名newName=" + newName);
            return;
        }
        int itemId = Global.ChangeNameItem.get(0);
        int costNum = Global.ChangeNameItem.get(1);
        long actionId = IDConfigUtil.getLogId();
        //移除改名卡
        if (!Manager.backpackManager.manager().onRemoveItem(player, itemId, 1, ItemChangeReason.ChangeNameDec, actionId)) {
            if (!Manager.currencyManager.manager().onDecItemCoin(player, costNum, ItemChangeReason.ChangeNameDec, actionId, ItemCoinType.GemCoin)) {
                log.error("玩家" + player.getName() + "改名时角色改名卡或者元宝扣除失败！");
                return;
            }
        }

        //改名
        String oldName = player.getName();
        player.setName(newName);

        Manager.registerManager.addRoleName(player.getId(), newName); //改名成功加入新角色名
        Manager.playerManager.savePlayer(player, SavePlayerLevel.HalfMinLater); //新角色名需同步到是世界服的，要先addRoleName，再savePlayer
        //Todo 修改改名刷新
        Manager.playerManager.manager().changeLoginName(player.getId(), player.getName());


        //更新排行活动上的名字(如果有的话)
//        for (ActivityConfig act : Manager.activityManager.getRechargeRankActivity().values()) {
//            updateActRankInfo(act, oldName, newName);
//        }
//        for (ActivityConfig act : Manager.activityManager.getConsumptionRankActivity().values()) {
//            updateActRankInfo(act, oldName, newName);
//        }
//        for (ActivityConfig act : Manager.activityManager.getDonateRankActivity().values()) {
//            updateActRankInfo(act, oldName, newName);
//        }
//        for (ActivityConfig act : Manager.activityManager.getGatherRankActivity().values()) {
//            updateActRankInfo(act, oldName, newName);
//        }

        //通知客户端改名成功
        ResChangeRoleNameResult.Builder resMsg = ResChangeRoleNameResult.newBuilder();
        resMsg.setIsSuccess(true);
        resMsg.setNewName(newName);
        resMsg.setRoleId(player.getId());
        //广播给周围玩家
        MessageUtils.send_to_roundPlayer(player, ResChangeRoleNameResult.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
//        MessageUtils.send_to_player(player, ResChangeRoleNameResult.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());

        //全服公告被当作bug了(MMO1-Bug/MMOB-5414),修改改名不进行全服公告  --gzg 2020/07/08
        MessageUtils.notify_player(player,Notify.CHAT, MessageString.ChangeNameSuccess, oldName, newName);
       // MessageUtils.notify_allOnlinePlayer(Notify.CHAT, MessageString.ChangeNameSuccess, oldName, newName); //全服公告：“XXX玩家名字更变为XXX，如存在显示问题，请重新登录游戏！”

        if (player.getTeamId() > 0) {
            Manager.teamManager.deal().updateTeamInfoToLeader(player.getTeamId());
        }

        //同步给本地区域的人
//        manager.mapManager.manager().SynMeToOther(player);
        //向公共服发送玩家已经改名
        log.error("玩家" + oldName + "使用角色改名卡改名成功，当前新角色名为：" + player.getName());

        ChangeRoleNameLog cLog = new ChangeRoleNameLog();
        cLog.setPlayerId(player.getId());
        cLog.setUserId(player.getUserId());
        cLog.setSid(player.getCreateServerId());
        cLog.setOldName(oldName);
        cLog.setNewName(newName);
        cLog.setModelId(itemId);
        cLog.setPlatformName(player.getPlatformName());
        LogService.getInstance().execute(cLog);

        Manager.biManager.getScript().biUpdateBase(player);
        Manager.biManager.get4399Script().chatInfoTo4399(player, ChatManager.ROLE_NAME, "", newName, new ArrayList<>());

        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        if (guild != null && guild.getChairMan().getId() == player.getId()) {
            Manager.biManager.get4399Script().updateGuild(guild);
        }
    }

    //检查新角色名是否正确合法
    private boolean isNameOK(Player player, String name) {
        if (name == null || StringUtils.isBlank(name)) { //检查是否为空(客户端判断给予提示)
            //MessageUtils.notify_player(player, Notify.ERROR, 0); //“请输入您想要修改的角色名！”
            log.error("新角色名为空！");
            return false;
        }

        int nameLength = length(name);
        if (nameLength < Global.PlayerNameLimit.get(1) || nameLength > Global.PlayerNameLimit.get(0)) { //检查名字长度(客户端判断给予提示)
//            MessageUtils.notify_player(p, Notify.NORMAL, MessageString.SetRank_Succeed);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NameLength_error, Global.PlayerNameLimit.get(1) + "", Global.PlayerNameLimit.get(0) + ""); //“对不起，您的名字不符合取名规则，请重新输入。”
            log.error("新角色名长度错误，nameLength=" + nameLength);
            return false;
        }

        if (Utils.isContainsShielding_symbol(name)) { //屏蔽标点符号检查
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.SensitiveNewName); //“对不起，您的名字中包含敏感字，改名失败！”
            log.error("新角色名包含有屏蔽标点符号!");
            return false;
        }

        if (Utils.isForbiddenStr(name)) { //屏蔽字检查
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.SensitiveNewName); //“对不起，您的名字中包含敏感字，改名失败！”
            log.error("新角色名包含有屏蔽字!");
            return false;
        }

        if (name.contains("?")) { //屏蔽字检查
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.SensitiveNewName); //“对不起，您的名字中包含敏感字，改名失败！”
            log.error("新角色名包含有屏蔽字!");
            return false;
        }

        if (Manager.registerManager.isUsedName(name)) { //重名检查
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.DupNewName); //“对不起，该名字已被其他玩家占用，改名失败！”
            log.error("新角色名有重复！");
            return false;
        }

        return true;
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

    //更新排行活动上的名字(如果有的话)
    private void updateActRankInfo(ActivityConfig act, String oldName, String newName) {
//        boolean isExist = false;
//        ActivityDataRank activity = (ActivityDataRank) act;
//        for (ActivetyRankInfo info : activity.getRankInfoList()) {
//            if (info.getName().equals(oldName)) {
//                //info.toBuilder().setName(newName); //这样改不了。。
//                isExist = true;
//            }
//        }
//
//        if (isExist) {
//            List<ActivetyRankInfo> tmp = new ArrayList<>();
//            tmp.addAll(activity.getRankInfoList());
//            activity.getRankInfoList().clear();
//            for (ActivetyRankInfo a : tmp) {
//                ActivetyRankInfo.Builder info = ActivetyRankInfo.newBuilder();
//                info.setTop(a.getTop());
//                info.setLimit(a.getLimit());
//                info.setRewardList(a.getRewardList());
//                if (a.getName().equals(oldName)) {
//                    info.setName(newName);
//                } else {
//                    info.setName(a.getName());
//                }
//                info.setRoleId(a.getRoleId());
//                info.setUserId(a.getUserId());
//                activity.getRankInfoList().add(info.build());
//            }
//            tmp.clear();
//        }

    }
}
