package common.vip;

import com.data.*;
import com.data.bean.Cfg_VIPTrueRecharge_Bean;
import com.data.bean.Cfg_Vip_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.count.structs.BooleanDay;
import com.game.count.structs.VariantType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.vip.manager.VipManager;
import com.game.vip.script.IVipScript;
import com.game.vip.structs.VipPower;
import game.core.util.IDConfigUtil;
import game.message.VipMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class VipScript implements IVipScript {
    private final Logger logger = LogManager.getLogger(VipScript.class);

    @Override
    public int getId() {
        return ScriptEnum.VipBaseScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void online(Player player) {
        VipMessage.ResVipRed.Builder builder = VipMessage.ResVipRed.newBuilder();
        if (!Manager.countManager.getBooleanCountValue(player, BooleanDay.VipDailyReward) && player.getVipLv() > 0) {
            builder.setIsRed(true);
        } else {
            builder.setIsRed(false);
        }
        builder.setIsGetFreeAward(player.isGetVipFreeAward());
        MessageUtils.send_to_player(player, VipMessage.ResVipRed.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        int buffId1 = Manager.vipManager.power().getVipFreeNum(player, VipPower.POWER_8);
        int buffId2 = Manager.vipManager.power().getVipFreeNum(player, VipPower.POWER_12);
        Manager.buffManager.deal().onAddBuff(player, player, buffId1);
        Manager.buffManager.deal().onAddBuff(player, player, buffId2);

        if (!Manager.countManager.getBooleanCountValue(player, BooleanDay.VipDailyExp)) {
            addVipExp(player, Manager.vipManager.power().getVipFreeNum(player, VipPower.POWER_26), ItemChangeReason.VipRechargeReward, IDConfigUtil.getLogId());
            Manager.countManager.setBooleanCountValue(player, BooleanDay.VipDailyExp, true);
        }

        //检测 高级VIP和高级至尊VIP
        Manager.vipManager.deal().checkSpecialVip(player);
        //发送特有vip信息
        this.sendResSpecialVipStateInfo(player);
    }

    @Override
    public void addVipExp(Player player, int exp, int reason, long actionId) {
        Manager.currencyManager.manager().onAddItemCoin(player,
                ItemCoinType.VipExp, exp, reason, actionId);
    }

    @Override
    public void getVipInfo(Player player) {
        VipMessage.ResVip.Builder builder = VipMessage.ResVip.newBuilder();
        builder.setHasGet(player.getPurVipGift());
        builder.setIsGet(Manager.countManager.getBooleanCountValue(player, BooleanDay.VipDailyReward));
        MessageUtils.send_to_player(player, VipMessage.ResVip.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public int getVipLvByExp(long vipExp) {
        if (vipExp <= 0) {
            return 0;
        }
        int vipLv = 0;
        for (Cfg_Vip_Bean bean : CfgManager.getCfg_Vip_Container().getValuees()) {
            if (bean.getVipLevelUp() > vipExp) {
                continue;
            }
            if (bean.getVipLevel() < vipLv) {
                continue;
            }
            vipLv = bean.getVipLevel();
        }
        return vipLv;
    }

    @Override
    public void getVipFreeReward(Player player){

        //1.判断是否领取
        if(player.isGetVipFreeAward()){
            logger.error("VIP免费送的奖励已经领取了. playerID:"+player.getId());
            return ;
        }

        //2.判断在线时间是否足够
        if (player.getAccunonlinetime() < Global.Free_VIP_Level_Up_Time * 60) {
            logger.error("玩家在线时长不足,不能领取VIP免费送的奖励. playerID:"+player.getId() );
            return;
        }

        //3.设置标记
        player.setGetVipFreeAward(true);

        //4.发送奖励
        List<Item> items = Item.createItems(player.getCareer(), Global.Free_VIP_Level_Up_Reward, 1);
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.OnlineVipExpGet, IDConfigUtil.getLogId())){
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.OnlineVipExpGet);
        }

        //5.发送是否领取的消息(这里使用红点处理)
        VipMessage.ResVipRed.Builder builder = VipMessage.ResVipRed.newBuilder();
        if (!Manager.countManager.getBooleanCountValue(player, BooleanDay.VipDailyReward) && player.getVipLv() > 0) {
            builder.setIsRed(true);
        } else {
            builder.setIsRed(false);
        }
        builder.setIsGetFreeAward(player.isGetVipFreeAward());
        MessageUtils.send_to_player(player, VipMessage.ResVipRed.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void getVipReward(Player player) {
        //增加VIP宝珠状态检查
        if(!player.getVipPearl().canFree()){
            return;
        }

        int vip = player.getVipLv();
        int vipRewardLv;
        //2021.9.6欧阳帆说VIP每日奖励都一样，默认取vip为1的奖励
        if (vip <= 0) {
            vipRewardLv = 1;
        }else{
            vipRewardLv = vip;
        }

        if (Manager.countManager.getBooleanCountValue(player, BooleanDay.VipDailyReward)) {
            return;
        }

        Cfg_Vip_Bean bean = CfgManager.getCfg_Vip_Container().getValueByKey(vipRewardLv);
        if (bean == null) {
            return;
        }

        List<Item> items = Item.createItems(player.getCareer(),bean.getVipRewardPer(), 1);

        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) != 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            return;
        }

        Manager.countManager.setBooleanCountValue(player, BooleanDay.VipDailyReward, true);

        Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.VipDailyGift, IDConfigUtil.getLogId());

        VipMessage.ResVipReward.Builder builder = VipMessage.ResVipReward.newBuilder();
        MessageUtils.send_to_player(player, VipMessage.ResVipReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        Manager.controlManager.operate(player, FunctionVariable.Get_Vip_Daily_Reward, 1);

        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.VIP_DailyGift, ItemChangeReason.VipDailyGift, vip);
    }

    @Override
    public void purVipReward(Player player, int lv) {
        Cfg_Vip_Bean bean = CfgManager.getCfg_Vip_Container().getValueByKey(lv);
        if (bean == null) {
            return;
        }

        if (hasGet(player.getPurVipGift(), lv)) {
            return;
        }

        List<Item> items = Item.createItems(player.getCareer(),bean.getVipReward(), 1);
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) != 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            return;
        }

        if (!Manager.currencyManager.manager().onDecItemCoin(player, bean.getVipRewardPriceNow().get(1),
                ItemChangeReason.VipPurGiftDec, IDConfigUtil.getLogId(), bean.getVipRewardPriceNow().get(0))) {
            return;
        }

        int flag = player.getPurVipGift();
        flag |= (1 << (lv - 1));
        player.setPurVipGift(flag);
        Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.VipPurGiftGet, IDConfigUtil.getLogId());

        VipMessage.ResVipPurGift.Builder builder = VipMessage.ResVipPurGift.newBuilder();
        builder.setLv(lv);
        MessageUtils.send_to_player(player, VipMessage.ResVipPurGift.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        //记录bi数据
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.VIP_PurGift, ItemChangeReason.VipPurGift, lv);
    }


    @Override
    public void onlineVipRechage(Player player) {
        VipMessage.ResVipRechageMoney.Builder builder = VipMessage.ResVipRechageMoney.newBuilder();
        builder.setMoney(Manager.countManager.getVariant(player, VariantType.RechargeMoney));
        MessageUtils.send_to_player(player, VipMessage.ResVipRechageMoney.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onlineVipRechageRewardList(Player player) {
        VipMessage.ResVipRechageRewardList.Builder builder = VipMessage.ResVipRechageRewardList.newBuilder();
        builder.setHasGet(player.getVipRechargeReward());
        MessageUtils.send_to_player(player, VipMessage.ResVipRechageRewardList.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void getVipRechageReward(Player player, int id) {
        Cfg_VIPTrueRecharge_Bean bean = CfgManager.getCfg_VIPTrueRecharge_Container().getValueByKey(id);
        if (bean == null) {
            return;
        }

        long total = Manager.countManager.getVariant(player, VariantType.RechargeMoney);

        int flag = player.getVipRechargeReward();
        if (bean.getRechargeLimit() > total) {
            return;
        }

        if (hasGet(flag, id)) {
            return;
        }

        //需求判断前面一档是否领取了
        if (bean.getType() == 2) {
            Cfg_VIPTrueRecharge_Bean b = CfgManager.getCfg_VIPTrueRecharge_Container().getValueByKey(id - 1);
            if (b.getType() == 2) {
                if (!hasGet(flag, id - 1)) {
                    return;
                }
            }
        }

        List<Item> items = getItems(player, bean.getRechargeReward());
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) != 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            return;
        }

        flag |= (1 << (id - 1));
        player.setVipRechargeReward(flag);
        Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.VipRechargeGiftGet, IDConfigUtil.getLogId());

        VipMessage.ResVipRechargeReward.Builder builder = VipMessage.ResVipRechargeReward.newBuilder();
        builder.setId(id);
        MessageUtils.send_to_player(player, VipMessage.ResVipRechargeReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        addVipExp(player, Manager.vipManager.power().getVipFreeNum(player, VipPower.POWER_26), ItemChangeReason.VipRechargeReward, IDConfigUtil.getLogId());
        Manager.countManager.setBooleanCountValue(player, BooleanDay.VipDailyExp, true);

        //记录bi数据
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.VipRechargeGiftGet, BIActiityTypeEnum.CHARGE_WELFARE.getId(), id);
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.CHARGE_WELFARE, ItemChangeReason.VipRechargeGiftGet, id);
    }

    private boolean hasGet(int hasget, int id) {
        return (hasget & (1 << (id - 1))) != 0;
    }

//    private void sendItems(Player player, ReadIntegerArrayEs itemBean) {
//        List<Item> items = getItems(player, itemBean);
//        Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.VIPWeekReward_MailTitle, MessageString.VIPWeekReward_MailContent, items);
//    }

    private List<Item> getItems(Player player, ReadIntegerArrayEs itemBean) {
        List<Item> items = new ArrayList<>();
        for (ReadArray<Integer> l : itemBean.getValuees()) {
            if (l.get(3) != PlayerDefine.CAREER_All && l.get(3) != player.getCareer()) {
                continue;
            }
            items.addAll(Item.createItems(l.get(0), l.get(1), l.get(2) == 1));
        }
        return items;
    }


    public void checkSpecialVip(Player player){
        long rechargeGold =  Manager.countManager.getVariant(player, VariantType.RechargeGold);
        if (Manager.controlManager.deal().isOpenFunction(player, FunctionStart.VipInvationNormal)) {
           if(!player.getSpecialVipStateBean().isHighVipActivate())
           {
               if(!player.getSpecialVipStateBean().isNormalVipActivate()){
                   if(rechargeGold>= Global.Special_vip_recharge.get(0)){
                       //集合高级vip
                       player.getSpecialVipStateBean().setNormalVipActivate(true);

                       Manager.mailManager.sendMailToPlayer(player.getId(),
                               MessageString.System, MessageString.System, MessageString.special_normal_vip_mail, MessageString.special_normal_vip_tex);

                       sendResSpecialVipStateInfo(player);
                       //发送邮件
                       player.getSpecialVipStateBean().setNormalVipNotifyClient(true);
                   }
               }
           }

        }

        if (Manager.controlManager.deal().isOpenFunction(player, FunctionStart.VipInvationZunGui)) {
            if(!player.getSpecialVipStateBean().isHighVipActivate()){
                if(rechargeGold>= Global.Special_vip_recharge.get(1)){
                    //集合高级至尊vip
                    player.getSpecialVipStateBean().setNormalVipActivate(false);
                    player.getSpecialVipStateBean().setHighVipActivate(true);
                    Manager.mailManager.sendMailToPlayer(player.getId(),
                            MessageString.System, MessageString.System, MessageString.special_high_vip_mail, MessageString.special_high_vip_tex);

                    sendResSpecialVipStateInfo(player);
                    //发送邮件
                    player.getSpecialVipStateBean().setHighVipNotifyClient(true);


                }
            }
        }

    }

    public void sendResSpecialVipStateInfo(Player player){
        VipMessage.ResSpecialVipStateInfo.Builder resBuilder = VipMessage.ResSpecialVipStateInfo.newBuilder();
        VipMessage.SpecialVipStateInfo.Builder normalVipBuilder = VipMessage.SpecialVipStateInfo.newBuilder();
        normalVipBuilder.setIsActivate(player.getSpecialVipStateBean().isNormalVipActivate());
        normalVipBuilder.setIsNotifyClient(player.getSpecialVipStateBean().isNormalVipNotifyClient());
        resBuilder.setNormalVip(normalVipBuilder);

        VipMessage.SpecialVipStateInfo.Builder highVipBuilder = VipMessage.SpecialVipStateInfo.newBuilder();
        highVipBuilder.setIsActivate(player.getSpecialVipStateBean().isHighVipActivate());
        highVipBuilder.setIsNotifyClient(player.getSpecialVipStateBean().isHighVipNotifyClient());
        resBuilder.setHighVip(highVipBuilder);
        MessageUtils.send_to_player(player, VipMessage.ResSpecialVipStateInfo.MsgID.eMsgID_VALUE, resBuilder.build().toByteArray());
    }
}
