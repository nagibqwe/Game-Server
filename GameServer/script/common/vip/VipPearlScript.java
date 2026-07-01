package common.vip;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Vip_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.count.structs.BooleanDay;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.vip.logs.VipPearlChangeLog;
import com.game.vip.script.IVipPearlScript;
import com.game.vip.structs.VipPearl;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.VipMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class VipPearlScript implements IVipPearlScript {
    private final Logger logger = LogManager.getLogger(VipPearlScript.class);

    @Override
    public int getId() {
        return ScriptEnum.VipPearlScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void activeVipPearl(Player player, int id) {
        int useItemId = 0;
        //策划说限时宝珠和永久宝珠先写死
        if(id == 0){
            useItemId = 1372;
        }else if(id == 1){
            useItemId = 1373;
        }

        //使用宝珠
        Item item = Manager.backpackManager.manager().getItemByModelId(player, useItemId);
        Manager.backpackManager.manager().useItem(player, item.getId(), 1, 0);
    }

    @Override
    public void useVipPearlItem(Player player, int itemId, ReadArray<Integer> tab, int reason) {
        int pearltype = tab.get(1);
        int deadline = 0;
        boolean isActive = false;
        VipPearl data = player.getVipPearl();
        if(data.getState() == 0){//当前没有宝珠，直接佩戴
            if(pearltype==0){//限时珠宝
                int deadTime = tab.get(2);
                deadline = deadTime == 0 ? 0 : (int) (TimeUtils.Time() / 1000) + deadTime;
                data.setState(1);
                data.setDeadline(deadline);
                writeVIPPearlChangeLog(player, 1, itemId, deadline);
                isActive = true;
            }else if(pearltype==1){//永久宝珠
                data.setState(2);
                data.setDeadline(0);
                //重新计算属性
                Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Vip);
                writeVIPPearlChangeLog(player, 1, itemId, deadline);
                isActive = true;
            }
        }else if(data.getState() == 1 && pearltype == 1){//当前为限时宝珠时,只能用永久宝珠覆盖
            data.setState(2);
            data.setDeadline(0);
            //重新计算属性
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Vip);
            writeVIPPearlChangeLog(player, 2, itemId, deadline);
        }else if(data.getState() == 2 && pearltype == 1){//当前为永久宝珠时,再次使用永久宝珠则增加货币
            int addItemId = tab.get(2);
            int addItemCount = tab.get(3);
            data.setState(2);
            data.setDeadline(0);

            //重新计算属性
//            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Vip);
            writeVIPPearlChangeLog(player, 3, itemId, deadline);

            //增加货币
            Item item = Item.createItem(addItemId, addItemCount, true);
            if (!Manager.backpackManager.manager().addItems(player, Arrays.asList(item), ItemChangeReason.VipPearlGet, IDConfigUtil.getLogId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, Arrays.asList(item), ItemChangeReason.VipPearlGet);
            }
        }

        //返回佩戴宝珠消息
        sendVipPearlInfo(player, data.getState(), data.getDeadline(), isActive);

//        Manager.controlManager.operate(player, FunctionVariable.VipPearl, 0);

    }

    @Override
    public void vipPearlTimeOutCheck(Player player) {
        VipPearl data = player.getVipPearl();
        if(data.getState()!=1){//只检查限时宝珠
            return;
        }
        if (data.getDeadline() <= TimeUtils.Time() / 1000) {
            //穿戴的珠宝过期了，直接卸下
            data.setState(0);
            data.setDeadline(0);
            //重新计算属性
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Vip);
//            logger.info("=======vipPearlTimeOutCheck  state:"+data.getState()+",time:"+data.getDeadline());
            sendVipPearlInfo(player, data.getState(), data.getDeadline(), false);

            //如果最后一天的vip每日奖励没领则通过邮件补发
            if(!Manager.countManager.getBooleanCountValue(player, BooleanDay.VipDailyReward)){
                Cfg_Vip_Bean bean = CfgManager.getCfg_Vip_Container().getValueByKey(1);
                if (bean != null) {
                    List<Item> items = Item.createItems(bean.getVipRewardPer());
                    Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                            MessageString.C_Vip_Power_Reward_Mail_Title, MessageString.C_Vip_Power_Reward_Mail_Tex, items, ItemChangeReason.VipDailyGift);
                    //设置领取状态
                    Manager.countManager.setBooleanCountValue(player, BooleanDay.VipDailyReward, true);
                }
            }

            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.VIP_Powe_Overdue_Mail_Title, MessageString.VIP_Powe_Overdue_Mail_Tex);
            writeVIPPearlChangeLog(player, 0, 0, 0);
        }
    }

    @Override
    public void onlineVipPearlInfo(Player player) {
        VipPearl vipPearl = player.getVipPearl();
        sendVipPearlInfo(player, vipPearl.getState(), vipPearl.getDeadline(), false);
    }

    private void sendVipPearlInfo(Player player, int state, int deadLine, boolean isActive) {
        VipMessage.ResVipPearlInfo.Builder builder = VipMessage.ResVipPearlInfo.newBuilder();
        builder.setState(state);
        builder.setDeadLine(deadLine);
        builder.setIsActive(isActive);
        MessageUtils.send_to_player(player, VipMessage.ResVipPearlInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * VIP宝珠状态改变日志
     */
    private void writeVIPPearlChangeLog(Player player, int opType, int itemId, int deadLine) {
        VipPearlChangeLog changeLog = new VipPearlChangeLog();
        changeLog.setPlayer(player);
        changeLog.setOpType(opType);
        changeLog.setItemId(itemId);
        changeLog.setDeadLine(deadLine);
        LogService.getInstance().execute(changeLog);
    }
}
