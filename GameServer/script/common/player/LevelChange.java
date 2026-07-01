package common.player;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Characters_Bean;
import com.data.bean.Cfg_TimingMail_Bean;
import com.game.activity.struct.ActivityConfig;
import com.game.backpack.structs.Item;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.hook.manager.PlayerHookManager;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.player.script.ImPlayerScript;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ResLevelChange;
import game.message.WelfareMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author lw
 */
public class LevelChange extends ImPlayerScript implements IScript {

    private static final Logger logger = LogManager.getLogger(LevelChange.class);

    @Override
    public int getId() {
        return ScriptEnum.PlayerLevelChangeBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void levelUp(Player player, int oldLevel) {
        if (player.getLevel() == oldLevel) {
            //转职可能等级未变化
            return;
        }
        //Manager.playerAttAttributeManager.deal().calPlayerAttributs(player, PlayerAttributeType.BASE);
        //玩家等级升级之任务系统判定
        Manager.taskManager.deal().addTaskLevelUp(player);

        for (int i = 1; i <= player.getLevel() - oldLevel; i++) {
            Cfg_Characters_Bean config = CfgManager.getCfg_Characters_Container().getValueByKey(oldLevel + i);
            if (config.getOccSkill() !=null){
                Manager.skillManager.deal().addActivateNewSkill(player,config.getOccSkill());

                //TODO 升级有可能获得经脉点
                if (config.getLevel_up_money() != null &&  config.getLevel_up_money().size()>0){
                    Manager.currencyManager.manager().onAddItemCoin(player,config.getLevel_up_money().get(0),config.getLevel_up_money().get(1), ItemChangeReason.LevelChangeGet, IDConfigUtil.getLogId());
                }
            }
        }

        logger.info(player.nameIdString() + " 从" + oldLevel + " 升级到" + player.getLevel());
        sendLevelChange(player);
        //更新登录服数据
        Manager.playerManager.manager().changeLoginLevel(player.getId(), player.getLevel());
        Manager.playerManager.manager().changeLoginFight(player.getId(), player.getFightPoint());
        Manager.controlManager.operate(player, FunctionVariable.PlayerLevel,  player.getLevel());
        Manager.playerHookManager.deal().checkPlayerRateChange(player, PlayerHookManager.WorldLvRate);
        if (player.getTeamId() > 0) {
            Manager.teamManager.deal().updateTeamInfoToLeader(player.getTeamId());
        }
        Manager.registerManager.roleLvUp(player);
        //更新充值排行活动排行榜上等级最大角色
//        for (ActivityConfig act : Manager.activityManager.getRechargeRankActivity().values()) {
//            rechargeTopLvUp(act, player);
//        }
        //通知客户端分享
//        Manager.shareManager.deal().sendShareNotice(player, ShareType.SHARE_UP_LEVEL, oldLevel + "");
        //玩家升级自动关注世界boss
        Manager.bossManager.manager().autoFollowBoss(player);
        Manager.guildActivityManager.deal().autoFollowBoss(player);
        Manager.crossFudManager.deal().autoFollowBoss(player);
        //level change
        Manager.welfareManager.getScript(WelfareMessage.WelfareType.LevelGift).freshDataNtf(player);
        Manager.shopManager.limitShop().refresh(player);
        Manager.shopManager.mysteryShop().refresh(player);
        sendLevelChangeMail(player);
        Manager.devilSeriesManager.getScript().followDefaultDeviCopy(player);


        //升级同步功能开关是否开启
        Manager.platformevaluateManager.deal().levelUp(player);

        //BI
        Manager.biManager.getScript().biUpdateBase(player);
        Manager.biManager.get4399Script().updatePlayer(player);

    }

    private void sendLevelChangeMail(Player player){
        for (Cfg_TimingMail_Bean bean:CfgManager.getCfg_TimingMail_Container().getValuees()) {
            if(Manager.controlManager.deal().checkFuncProgress(player, bean.getCondition())){
                if(Manager.countManager.getCount(player, BaseCountType.PlayerLevelMail, bean.getId())>0){
                    continue;
                }

                Manager.countManager.addCount(player, BaseCountType.PlayerLevelMail, bean.getId(), Count.RefreshType.CountType_Forever, 1);

                List<Item> items = Item.createItems(bean.getReward());

                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, bean.getMailTitle(), bean.getMail(), items, ItemChangeReason.LevelChangeGet);
            }
        }
    }

    //更新充值排行活动排行榜上等级最大角色
    private void rechargeTopLvUp(ActivityConfig act, Player player) {
//        ActivityDataRank activity = (ActivityDataRank) act;
//        boolean isUpdate = false;
//        for (ActivityRankTopTen top : activity.getTopTen()) {
//            if (top.getUserId() != player.getUserId()) {
//                continue;
//            }
//            long hRoleId = Manager.registerManager.getHighestLvRole(player.getUserId());
//            if (top.getRoleId() != hRoleId) {
//                top.setRoleId(hRoleId);
//                top.setName(Manager.registerManager.getRoleName(hRoleId));
//                isUpdate = true;
//            }
//        }
//        if (isUpdate) {
//            List<ActivetyRankInfo> tmp = new ArrayList<>();
//            tmp.addAll(activity.getRankInfoList());
//            activity.getRankInfoList().clear();
//            for (ActivetyRankInfo a : tmp) {
//                ActivetyRankInfo.Builder info = ActivetyRankInfo.newBuilder();
//                info.setTop(a.getTop());
//                info.setLimit(a.getLimit());
//                info.setRewardList(a.getRewardList());
//                info.setUserId(a.getUserId());
//                if (a.getUserId() == player.getUserId()) {
//                    long hRoleId = Manager.registerManager.getHighestLvRole(player.getUserId());
//                    info.setRoleId(hRoleId);
//                    info.setName(Manager.registerManager.getRoleName(hRoleId));
//                } else {
//                    info.setRoleId(a.getRoleId());
//                    info.setName(a.getName());
//                }
//                activity.getRankInfoList().add(info.build());
//            }
//            tmp.clear();
//        }
    }

    //发送玩家等级变化消息
    private void sendLevelChange(Player player) {
        ResLevelChange.Builder msg = ResLevelChange.newBuilder();
        msg.setLevel(player.getLevel());
        msg.setPlayerId(player.getId());
        msg.setTime((int) (TimeUtils.Time() / 1000));
        msg.setGrade(player.getXsGrade());
        byte[] mess = msg.build().toByteArray();
        MessageUtils.send_to_roundPlayer(player, ResLevelChange.MsgID.eMsgID_VALUE, mess);
    }

}
