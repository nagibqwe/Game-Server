package common.platformevaluate;

import com.data.*;
import com.data.bean.Cfg_Npc_friend_Bean;
import com.game.backpack.structs.Item;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.BooleanForever;
import com.game.count.structs.Count;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.platformevaluate.log.PlatformEvaluateLog;
import com.game.platformevaluate.script.IPlatformEvaluateScript;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import game.core.dblog.LogService;
import game.core.script.IScript;
import game.core.util.DateFormatUtils;
import game.core.util.IDConfigUtil;
import game.message.PlatformEvaluateMessage;
import game.message.PlatformEvaluateMessage.ResEvaluateResult;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 点赞分享评论脚本
 */
public class PlatformEvaluateScript implements IScript, IPlatformEvaluateScript {

    private final static Logger log = LogManager.getLogger(PlatformEvaluateScript.class);

    @Override
    public int getId() {
        return ScriptEnum.PlatformEvaluateBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onReqEvaluate(Player player, PlatformEvaluateMessage.ReqEvaluate messInfo) {
        int type = messInfo.getType();
        int actType = messInfo.getActType();
        if (type == 1) {//点赞
            if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ThaiLike)) {
                return;
            }
            if (!ServerParamUtil.serverEvaluateData.getOrDefault(1, true)) {
                return;
            }
            if (actType == 1) {
                if (player.getLike() != 0) {
                    return;
                }
                player.setLike(1);
                sendEvaluateResultMessage(player);
                writePlatformEvaluateLog(player, type, actType);
            } else if (actType == 2) {
                if (player.getLike() != 1) {
                    return;
                }
                //发道具
                List<Item> items = Item.createItems(Global.Thai_LikeRewards);
                if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
                    Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.PlatformEvaluateLike, IDConfigUtil.getLogId());
                } else {
                    Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.THAI_SHARE_EMAIL_TITLE, MessageString.THAI_SHARE_EMAIL_NOT_ENOUGH, items, ItemChangeReason.PlatformEvaluateLike);
                }
                player.setLike(2);

                sendEvaluateResultMessage(player);

                writePlatformEvaluateLog(player, type, actType);
            }
        } else if (type == 2) {//分享
            if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ThaiShare)) {
                return;
            }
            if (!ServerParamUtil.serverEvaluateData.getOrDefault(2, true)) {
                return;
            }
            if (actType == 1) {
                if (player.getShare() != 0) {
                    return;
                }
                player.setShare(1);
                sendEvaluateResultMessage(player);
                writePlatformEvaluateLog(player, type, actType);
            } else if (actType == 2) {
                if (player.getShare() != 1) {
                    return;
                }
                //发道具
                List<Item> items = Item.createItems(Global.Thai_ShareRewards);
                if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
                    Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.PlatformEvaluateShare, IDConfigUtil.getLogId());
                } else {
                    Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.THAI_SHARE_EMAIL_TITLE, MessageString.THAI_SHARE_EMAIL_NOT_ENOUGH, items, ItemChangeReason.PlatformEvaluateShare);
                }
                player.setShare(2);

                sendEvaluateResultMessage(player);

                writePlatformEvaluateLog(player, type, actType);
            }
        } else if (type == 3) {//评价
            if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ThaiScore)) {
                return;
            }
            player.setEvaluate(actType);
            sendEvaluateResultMessage(player);
            writePlatformEvaluateLog(player, type, actType);
        } else if (type == 4) { //每日分享
            if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.DayShare)) {
                return;
            }
            if (!ServerParamUtil.serverEvaluateData.getOrDefault(4, true)) {
                return;
            }
            if (actType == 1) {
                //

                long platformEvaluateEveryDayShare = player.getPlatformEvaluateEveryDayShare(); // Manager.countManager.getCount(player,BaseCountType.PlatformEvaluateEveryDayShare,0);
                if (platformEvaluateEveryDayShare != 0) {
                    return;
                }
                player.setPlatformEvaluateEveryDayShare(1);
                String day = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
                player.setPlatformEvaluateEveryDayShareDay(day);
                //  Manager.countManager.setCount(player, BaseCountType.PlatformEvaluateEveryDayShare,0, Count.RefreshType.CountType_Day,1);
                sendEvaluateResultMessage(player);

                writePlatformEvaluateLog(player, type, actType);
            } else if (actType == 2) {
                long platformEvaluateEveryDayShare = player.getPlatformEvaluateEveryDayShare(); // Manager.countManager.getCount(player,BaseCountType.PlatformEvaluateEveryDayShare,0);
                if (platformEvaluateEveryDayShare != 1) {
                    return;
                }
                //发道具
                List<Item> items = Item.createItems(Global.TW_DailyShareLinkRewards);
                if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
                    Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.PlatformEvaluateEveryDayShare, IDConfigUtil.getLogId());
                } else {
                    Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.THAI_SHARE_EMAIL_TITLE, MessageString.THAI_SHARE_EMAIL_NOT_ENOUGH, items, ItemChangeReason.PlatformEvaluateShare);
                }
                //  Manager.countManager.setCount(player, BaseCountType.PlatformEvaluateEveryDayShare,0, Count.RefreshType.CountType_Day,2);
                player.setPlatformEvaluateEveryDayShare(2);
                sendEvaluateResultMessage(player);

                writePlatformEvaluateLog(player, type, actType);
            }
        }else if (type == 5) {//商店评价

            if (!ServerParamUtil.serverEvaluateData.getOrDefault(5, true)) {
                return;
            }

            //已经评价了
            if (actType == 1) {
                long shopEvaluate = player.getShopEvaluate(); // Manager.countManager.getCount(player,BaseCountType.PlatformEvaluateEveryDayShare,0);
                if (shopEvaluate != 0) {
                    return;
                }
    //            if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
    //                Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.ShopCommentRewardsGet, IDConfigUtil.getLogId());
    //            } else {
                // }

                player.setShopEvaluate(actType);
                sendEvaluateResultMessage(player);
                writePlatformEvaluateLog(player, type, actType);
            } else if (actType == 2) {
                long shopEvaluate = player.getShopEvaluate(); // Manager.countManager.getCount(player,BaseCountType.PlatformEvaluateEveryDayShare,0);
                if (shopEvaluate != 1) {
                    return;
                }
                List<Item> items = Item.createItems(Global.TW_ShopCommentRewards);
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.Mail_ShopCommentOpen_Tittle, MessageString.Mail_ShopCommentOpen_Rewards, items, ItemChangeReason.ShopCommentRewardsGet);
                player.setShopEvaluate(2);
                sendEvaluateResultMessage(player);

                writePlatformEvaluateLog(player, type, actType);
            }
        }
    }

    private void writePlatformEvaluateLog(Player player, int type, int actType) {
        PlatformEvaluateLog platformEvaluateLog = new PlatformEvaluateLog();
        platformEvaluateLog.setPlayerInfo(player.getPlatformName(), player.getCreateServerId(), player.getUserId(), player.getId(), player.getName());
        platformEvaluateLog.setType(type);
        platformEvaluateLog.setActType(actType);
        LogService.getInstance().execute(platformEvaluateLog);
    }

    @Override
    public void playerOnline(Player player) {
        boolean openLike = ServerParamUtil.serverEvaluateData.getOrDefault(1, true);
        if (!openLike) {//上线检查活动已经关闭还未领玩家，补发邮件
            if (player.getLike() == 1) {
                List<Item> items = Item.createItems(Global.Thai_LikeRewards);
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.THAI_SHARE_EMAIL_TITLE, MessageString.THAI_SHARE_EMAIL_FORGET, items, ItemChangeReason.PlatformEvaluateLike);
            }
            player.setLike(0);
        }
        boolean openShare = ServerParamUtil.serverEvaluateData.getOrDefault(2, true);
        if (!openShare) {
            if (player.getShare() == 1) {
                List<Item> items = Item.createItems(Global.Thai_ShareRewards);
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.THAI_SHARE_EMAIL_TITLE, MessageString.THAI_SHARE_EMAIL_FORGET, items, ItemChangeReason.PlatformEvaluateShare);
            }
            player.setShare(0);
        }
        //每日分享领奖
        boolean openEveryDayShareShare = ServerParamUtil.serverEvaluateData.getOrDefault(4, true);
        if (!openEveryDayShareShare) {
            if (player.getPlatformEvaluateEveryDayShare() == 1) {
                List<Item> items = Item.createItems(Global.TW_DailyShareLinkRewards);
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.THAI_SHARE_EMAIL_TITLE, MessageString.THAI_SHARE_EMAIL_FORGET, items, ItemChangeReason.PlatformEvaluateEveryDayShare);
            }
            player.setPlatformEvaluateEveryDayShare(0);
        }

        //判断跨天
        String day = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        if(!day.equals(player.getPlatformEvaluateEveryDayShareDay())){
            if (player.getPlatformEvaluateEveryDayShare() == 1) {//关闭时未领补发
                List<Item> items = Item.createItems(Global.TW_DailyShareLinkRewards);
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.THAI_SHARE_EMAIL_TITLE, MessageString.THAI_SHARE_EMAIL_FORGET, items, ItemChangeReason.PlatformEvaluateEveryDayShare);
            }
            player.setPlatformEvaluateEveryDayShareDay(day);
            player.setPlatformEvaluateEveryDayShare(0);
        }

        sendEvaluateInfoMessage(player);
    }

    @Override
    public void updateEvaluate(Player player, int type, boolean newState) {
        if (type == 1) {
            if (!newState) {
                if (player.getLike() == 1) {//关闭时未领补发
                    List<Item> items = Item.createItems(Global.Thai_LikeRewards);
                    Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.THAI_SHARE_EMAIL_TITLE, MessageString.THAI_SHARE_EMAIL_FORGET, items, ItemChangeReason.PlatformEvaluateLike);
                }
                player.setLike(0);
            }
            sendEvaluateInfoMessage(player);
        } else if (type == 2) {
            if (!newState) {
                if (player.getShare() == 1) {//关闭时未领补发
                    List<Item> items = Item.createItems(Global.Thai_ShareRewards);
                    Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.THAI_SHARE_EMAIL_TITLE, MessageString.THAI_SHARE_EMAIL_FORGET, items, ItemChangeReason.PlatformEvaluateEveryDayShare);
                }
                player.setShare(0);
            }
            sendEvaluateInfoMessage(player);
        } else if (type == 4) {
            if (!newState) {
                if (player.getPlatformEvaluateEveryDayShare() == 1) {//关闭时未领补发
                    List<Item> items = Item.createItems(Global.TW_DailyShareLinkRewards);
                    Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.THAI_SHARE_EMAIL_TITLE, MessageString.THAI_SHARE_EMAIL_FORGET, items, ItemChangeReason.PlatformEvaluateEveryDayShare);
                }
                player.setPlatformEvaluateEveryDayShare(0);
            }
            sendEvaluateInfoMessage(player);
        }else if (type == 5) {
            if (!newState) {
                if (player.getShopEvaluate() == 1) {//关闭时未领补发
                    List<Item> items = Item.createItems(Global.TW_ShopCommentRewards);
                    Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.Mail_ShopCommentOpen_Tittle, MessageString.Mail_ShopCommentOpen_Rewards, items, ItemChangeReason.ShopCommentRewardsGet);
                }
                player.setShopEvaluate(0);
            }
            sendEvaluateInfoMessage(player);
        }
    }


    /**
     * 零点刷新
     */
    public void zeroClockDeal(Player player) {
        if (player.getPlatformEvaluateEveryDayShare() == 1) {//关闭时未领补发
            List<Item> items = Item.createItems(Global.TW_DailyShareLinkRewards);
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.THAI_SHARE_EMAIL_TITLE, MessageString.THAI_SHARE_EMAIL_FORGET, items, ItemChangeReason.PlatformEvaluateEveryDayShare);
        }
        player.setPlatformEvaluateEveryDayShare(0);
        sendEvaluateInfoMessage(player);
    }
    public void levelUp(Player player){
      //  levelUpPlatformEvaluateEveryDayShare(player);
    }

//    private  void levelUpPlatformEvaluateEveryDayShare(Player player){
//        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.DayShare)) {
//            return;
//        }
//        if (Manager.countManager.getBooleanCountValue(player, BooleanForever.PlatformEvaluateEveryDayShare)) {
//            return;
//        }
//        Manager.countManager.setBooleanCountValue(player, BooleanForever.PlatformEvaluateEveryDayShare, true);
//        sendEvaluateInfoMessage(player);
//    }



    private void sendEvaluateInfoMessage(Player player) {
        PlatformEvaluateMessage.ResEvaluateInfo.Builder msg = PlatformEvaluateMessage.ResEvaluateInfo.newBuilder();
        msg.setLike(player.getLike());
        msg.setShare(player.getShare());
        msg.setEvaluate(player.getEvaluate());
        msg.setOpenLike(ServerParamUtil.serverEvaluateData.getOrDefault(1, true));
        msg.setOpenShare(ServerParamUtil.serverEvaluateData.getOrDefault(2, true));
        msg.setOpenEveryDayShare(ServerParamUtil.serverEvaluateData.getOrDefault(4, true));
        //每日分享记录
        msg.setEveryDayShare(player.getPlatformEvaluateEveryDayShare());
        //商店评价
        msg.setShopEvaluate(player.getShopEvaluate());
        msg.setOpenShopEvaluate(ServerParamUtil.serverEvaluateData.getOrDefault(5, true));
        MessageUtils.send_to_player(player, PlatformEvaluateMessage.ResEvaluateInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void sendEvaluateResultMessage(Player player) {
        ResEvaluateResult.Builder msg = ResEvaluateResult.newBuilder();
        msg.setLike(player.getLike());
        msg.setShare(player.getShare());
        msg.setEvaluate(player.getEvaluate());
        msg.setEveryDayShare(player.getPlatformEvaluateEveryDayShare());
        msg.setShopEvaluate(player.getShopEvaluate());

        MessageUtils.send_to_player(player, ResEvaluateResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
}
