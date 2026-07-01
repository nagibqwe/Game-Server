package common.welfare;

import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.game.backpack.structs.Item;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.welfare.script.IWelfareFreeGiftScript;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;

import java.util.List;

/**
 * 福利免费礼包
 */
public class WelfareFreeGiftScript implements IWelfareFreeGiftScript {
    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.WelfareFreeGiftScript;
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

    @Override
    public void ReqWelfareFreeGift(Player player) {
        boolean isCanGetGift = false;
        if(player.getLastWelfareFreeGiftTime() != 0){
            long nextTime = player.getLastWelfareFreeGiftTime() +  Global.Recharge_total_function_time_limit * 60 * 1000;
            //表示可以领取奖励
            if(TimeUtils.Time() > nextTime){
                isCanGetGift = true;
            }
        }else {
            isCanGetGift = true;
        }
        //不能领取奖励
        if(!isCanGetGift){
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        List<Item> items = Item.createItems(player.getCareer(), Global.Recharge_total_function_time_reward,1);
        if ( !Manager.backpackManager.manager().addItems(player,items, ItemChangeReason.WelfareFreeGiftGet, actionId)){
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.WelfareFreeGiftGet, actionId);
        }
        //记录当前领取时间
        player.setLastWelfareFreeGiftTime(TimeUtils.Time());
        this.sendResWelfareFreeGiftInfo(player);
    }

    @Override
    public void playerOnline(Player player) {
        this.sendResWelfareFreeGiftInfo(player);
    }

    public void sendResWelfareFreeGiftInfo(Player player){
        WelfareMessage.ResWelfareFreeGiftInfo.Builder builder = WelfareMessage.ResWelfareFreeGiftInfo.newBuilder();
        builder.setGetTime(player.getLastWelfareFreeGiftTime());
        MessageUtils.send_to_player(player, WelfareMessage.ResWelfareFreeGiftInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void freshDataNtf(Player player) {

    }
}
