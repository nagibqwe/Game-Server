package common.welfare;

import com.data.FunctionVariable;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.recharge.script.IRechargeReward;
import com.game.recharge.structs.RechargeDefine;
import com.game.recharge.structs.RechargeItemInfo;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.welfare.script.IDayGiftScript;
import game.message.WelfareMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DayGiftScript implements IDayGiftScript, IRechargeReward {
    private final Logger log = LogManager.getLogger(DayGiftScript.class);

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.DayGiftBaseScript;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }

    /**
     * 玩家上线
     *
     * @param player
     */
    @Override
    public void playerOnline(Player player) {

    }

    /**
     * 请求某福利子项数据
     *
     * @param player
     */
    @Override
    public void freshDataNtf(Player player) {
        if (player == null)
            return;

        List<Integer> list = new ArrayList<>();
        for (RechargeItemInfo bean : Manager.rechargeManager.getRechargeItemInfoMap().values()) {
            if (bean.getGoods_type() != 2) continue;
            if (Manager.countManager.getCount(player, BaseCountType.DayGift, bean.getGoods_system_cfg_id()) != 0)
                list.add(bean.getGoods_system_cfg_id());
        }

        WelfareMessage.ResDayGiftData.Builder builder = WelfareMessage.ResDayGiftData.newBuilder();
        builder.addAllBuyIDs(list);
        MessageUtils.send_to_player(player, WelfareMessage.ResDayGiftData.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 能否给奖励
     *
     * @param player
     * @param goodId
     * @return
     */
    @Override
    public boolean canReward(Player player, int goodId) {
        if (!Manager.welfareManager.isOpen(player, WelfareMessage.WelfareType.DayGift)) {
            return false;
        }
//        if(TimeUtils.getOpenServerDay()<Global.Daily_Gift_Open_Time){
//            return false;
//        }

        return Manager.countManager.getCount(player, BaseCountType.DayGift, goodId) == 0;
    }

    /**
     * 给奖励之后的逻辑处理
     *
     * @param player
     * @param orderId
     * @param goodId
     */
    @Override
    public void afterReward(Player player, String orderId, int goodId) {
        if (!Manager.welfareManager.isOpen(player, WelfareMessage.WelfareType.DayGift)) {
            return;
        }
//        if(TimeUtils.getOpenServerDay()<Global.Daily_Gift_Open_Time){
//            return;
//        }


        //添加一键购买，当判断是一键购买时候，把类型维二的切不是免费的全部记录为购买状态
       RechargeItemInfo rechargeItemInfo =   Manager.rechargeManager.getRechargeItemInfoMap().get(goodId);
        if (rechargeItemInfo.getGoods_type() == RechargeDefine.RECHARGE_TYPE_DAY_ONEKEY_GIFT){
            for (RechargeItemInfo bean : Manager.rechargeManager.getRechargeItemInfoMap().values()) {
                if (bean.getGoods_type() != 2) continue;
                int totalfen =  Manager.rechargeManager.deal().getMoney(player, bean, bean.getGoods_show_price());
                if (totalfen <=0) continue;
                Manager.countManager.addCount(player, BaseCountType.DayGift, bean.getGoods_system_cfg_id(), Count.RefreshType.CountType_Day, 1);
                Manager.countManager.addCount(player, BaseCountType.WelfareDailyGiftNum, goodId, Count.RefreshType.CountType_Forever, 1);
                Manager.controlManager.operate(player, FunctionVariable.WelfareDailyGiftNum, 1);
            }
        }

        Manager.countManager.addCount(player, BaseCountType.DayGift, goodId, Count.RefreshType.CountType_Day, 1);
        Manager.countManager.addCount(player, BaseCountType.WelfareDailyGiftNum, goodId, Count.RefreshType.CountType_Forever, 1);
        Manager.controlManager.operate(player, FunctionVariable.WelfareDailyGiftNum, 1);

        //特殊档次处理,用functionVaribale的坑
        if (goodId == 21) {
            Manager.controlManager.operate(player, FunctionVariable.WelfareDaily0GiftNum, 1);
        }

        if (goodId == 22) {
            Manager.controlManager.operate(player, FunctionVariable.WelfareDaily1GiftNum, 1);
        }

        if (goodId == 23) {
            Manager.controlManager.operate(player, FunctionVariable.WelfareDaily6GiftNum, 1);
        }

        if (goodId == 24) {
            Manager.controlManager.operate(player, FunctionVariable.WelfareDaily30GiftNum, 1);
        }
        freshDataNtf(player);
    }

    /**
     * 临时每日礼包充值
     *
     * @param player
     * @param id
     */
    @Override
    public void onReqDayGiftRecharge(Player player, int id) {
        log.error("不再支持每日礼包临时充值协议:" + player.getInfo());
    }
}
