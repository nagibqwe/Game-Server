package common.commercialize;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Recharge_daily_Bean;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.commercialize.inter.IDailyRechargeTotal;
import com.game.commercialize.struct.DailyRechargeData;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CommercializeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by cxl on 2020/9/7.
 */
public class DailyRechargeTotalScript  implements IDailyRechargeTotal {

    private static final Logger logger = LogManager.getLogger(DailyRechargeTotalScript.class);
    @Override
    public int getId() {
        return ScriptEnum.DailyRechargeTotalScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void playerOnline(Player player) {
        DailyRechargeData rechargeData =  player.getDailyRechargeData();
        int curDay   =  TimeUtils.getCurDayByTime(TimeUtils.Time());
        int lastDay = 0;
        if (rechargeData.getLastLoginTime() > 0 ){
            lastDay = TimeUtils.getCurDayByTime(rechargeData.getLastLoginTime());
            if (curDay > lastDay){
                long rechargeTotal =  rechargeData.getRechargeTotal();
                int consumeTotal  =  rechargeData.getConsumeTotal();

                for (Cfg_Recharge_daily_Bean bean: CfgManager.getCfg_Recharge_daily_Container().getValuees()){
                    if (bean.getType() == 1){
                        sendRewarItem(player,bean,rechargeData.getAlreadyGetRechargeList(),rechargeTotal,ItemChangeReason.DailyRechargeget);
                    }else {
                        sendRewarItem(player,bean,rechargeData.getAlreadyGetConsumeList(),consumeTotal,ItemChangeReason.DailyConsumeget);
                    }
                }
                rechargeData.getAlreadyGetRechargeList().clear();
                rechargeData.getAlreadyGetConsumeList().clear();
                rechargeData.setRechargeTotal(0L);
                rechargeData.setConsumeTotal(0);
                rechargeData.setBoxRewardCount(0);
            }
        }
        rechargeData.setLastLoginTime(TimeUtils.Time());
    }


    private boolean sendRewarItem(Player player, Cfg_Recharge_daily_Bean bean , List<Integer> alreadyGetList,long total,int reason)
    {
        if (alreadyGetList.contains(bean.getID())){
           return false;
        }
        if (total < bean.getMoney()){
            return false;
        }
        List<Item> items = Item.createItems(player.getCareer(), bean.getAward(), 1);
        long actionId = IDConfigUtil.getLogId();
        if ( !Manager.backpackManager.manager().addItems(player,items, reason, actionId)){
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, reason, actionId);
        }
        return true;
    }

    @Override
    public void onReqCommercialize(Player player) {
        DailyRechargeData rechargeData =  player.getDailyRechargeData();
        CommercializeMessage.ResDailyRechargeInfo.Builder msg =  CommercializeMessage.ResDailyRechargeInfo.newBuilder();
        msg.setConsumeTotal(rechargeData.getConsumeTotal());
        msg.setRechargeTotal(rechargeData.getRechargeTotal());
        msg.addAllConsumeIdList(rechargeData.getAlreadyGetConsumeList());
        msg.addAllRechargeIdList(rechargeData.getAlreadyGetRechargeList());
        msg.setBoxRewardCount(rechargeData.getBoxRewardCount());
        MessageUtils.send_to_player(player,CommercializeMessage.ResDailyRechargeInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void recharge(Player player, int commercializeID, int gold, int totalFee) {

       DailyRechargeData rechargeData =  player.getDailyRechargeData();
       rechargeData.setRechargeTotal(rechargeData.getRechargeTotal() + totalFee);
       onReqCommercialize( player);
    }

    @Override
    public void onReqGetRechargeReward(Player player, int id) {

        Cfg_Recharge_daily_Bean bean  = CfgManager.getCfg_Recharge_daily_Container().getValueByKey(id);
        if (bean == null){
            logger.error("Cfg_Recharge_daily_Bean is null "   +id);
            return;
        }
        DailyRechargeData rechargeData =  player.getDailyRechargeData();

        if (bean.getType()  == 1){
            if (!sendRewarItem(player,bean,rechargeData.getAlreadyGetRechargeList(),rechargeData.getRechargeTotal(),ItemChangeReason.DailyRechargeget)){
                return;
            }
            rechargeData.getAlreadyGetRechargeList().add(id);
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.EVERYDAY_CHARGE, ItemChangeReason.DailyRechargeget, bean.getPosition());
        }else {
            if (!sendRewarItem(player,bean,rechargeData.getAlreadyGetConsumeList(),rechargeData.getConsumeTotal(),ItemChangeReason.DailyConsumeget)){
              return;
            }
            rechargeData.getAlreadyGetConsumeList().add(id);
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.EVERYDAY_CONSUME, ItemChangeReason.DailyConsumeget, bean.getPosition());
        }
        onReqCommercialize( player);
    }

    @Override
    public void totalConsume(Player player, int gold) {
        DailyRechargeData rechargeData =  player.getDailyRechargeData();
        rechargeData.setConsumeTotal(rechargeData.getConsumeTotal() + gold);
        onReqCommercialize( player);
    }

    @Override
    public  void onReqGetBoxReward(Player player){
        DailyRechargeData rechargeData =  player.getDailyRechargeData();
        if (rechargeData.getBoxRewardCount() > 0){
            logger.info("今日宝箱奖励已经");
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        List<Item> items = Item.createItems( Global.Daily_Recharge_Reward);
        if ( !Manager.backpackManager.manager().addItems(player,items, ItemChangeReason.DailyRechargeget, actionId)){
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.DailyRechargeget, actionId);
        }
        rechargeData.setBoxRewardCount(1);
        CommercializeMessage.ResGetBoxRewardResult.Builder msg =  CommercializeMessage.ResGetBoxRewardResult.newBuilder();
        msg.setBoxRewardCount(1);
        MessageUtils.send_to_player(player,CommercializeMessage.ResGetBoxRewardResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
}
