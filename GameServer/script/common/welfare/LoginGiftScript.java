package common.welfare;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Sevenday_login_Bean;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.count.structs.VariantType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.welfare.script.ILoginGiftScript;
import com.game.welfare.struct.LoginGift;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class LoginGiftScript implements ILoginGiftScript {
    private final Logger log = LogManager.getLogger(LoginGiftScript.class);

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.LoginGiftBaseScript;
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
        checkLogin(player);
    }

    private void checkLogin(Player player) {
        if (player == null)
            return;
        if (!Manager.welfareManager.isOpen(player, WelfareMessage.WelfareType.LoginGift))
            return;

        if (player.getLoginGift() == null)
            player.setLoginGift(LoginGift.newLoginGift());

        LoginGift gift = player.getLoginGift();
        // 如果已经达到最大天数，则不再累积了
        if (gift.getMaxDay() != 0 && gift.getLoginNum() >= gift.getMaxDay())
            return;

        long nowTime = TimeUtils.Time();
        if (!TimeUtils.isSameDay(nowTime, gift.getLastCalcTime())) {
            // 累计新的登陆天数
            gift.setLastCalcTime(nowTime);
            gift.setLoginNum(gift.getLoginNum() + 1);
            Manager.shopManager.limitShop().refresh(player);
        }
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

        // 先刷新一下
        checkLogin(player);

        LoginGift gift = player.getLoginGift();
        if (gift == null)
            return;

        WelfareMessage.ResLoginGiftData.Builder builder = WelfareMessage.ResLoginGiftData.newBuilder();
        builder.setLoginNum(gift.getLoginNum());
        for (int recv : gift.getReceives()) {
            builder.addReceives(recv);
        }
        MessageUtils.send_to_player(player, WelfareMessage.ResLoginGiftData.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 请求领奖
     *
     * @param player
     * @param day
     */
    @Override
    public void onReqLoginGiftReward(Player player, int day) {
        if (player == null)
            return;
        LoginGift gift = player.getLoginGift();
        if (gift == null)
            return;

        if (!Manager.welfareManager.isOpen(player, WelfareMessage.WelfareType.LoginGift))
            return;

        if (day > gift.getLoginNum())
            return;

        if (gift.getReceives().contains(day))
            return;

        Cfg_Sevenday_login_Bean cfg = CfgManager.getCfg_Sevenday_login_Container().getValueByKey(day);
        if (cfg == null)
            return;

//        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.CityNoRoot);

        // 发奖
        List<Item> items = Item.createItems(player.getCareer(), cfg.getAward(), 1);
        int msId = Manager.backpackManager.manager().onHasAddSpaces(player, items);
        if (msId != 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            return;
        }

        // 记录
        gift.getReceives().add(day);
        List<Item> itemList = Item.clone(items);
        Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.WelfareLoginGiftGet, day);
        freshWelfareRewardNtf(player, WelfareMessage.WelfareType.LoginGift, itemList);
        Manager.countManager.addVariant(player, VariantType.LoginGift, 1);
        Manager.controlManager.operate(player, FunctionVariable.LandGiftReceive, 1);

        // 同步
        freshDataNtf(player);
        //记录BI数据
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.WelfareLoginGift, BIActiityTypeEnum.WELFARE.getId(), day);
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WELFARE_LoginGift, ItemChangeReason.WelfareLoginGift, day);
    }

    /**
     * 获得奖励
     *
     * @param player
     * @param type
     * @param item
     */
    @Override
    public void freshWelfareRewardNtf(Player player, WelfareMessage.WelfareType type, List<Item> item) {
        if (item == null || item.size() == 0)
            return;

        WelfareMessage.ResWelfareReward.Builder builder = WelfareMessage.ResWelfareReward.newBuilder();
        builder.setTyp(type.getNumber());
        for (int i = 0; i < item.size(); i++) {
            WelfareMessage.ItemInfo.Builder info = WelfareMessage.ItemInfo.newBuilder();
            info.setItemID(item.get(i).getItemModelId());
            info.setNum(item.get(i).getNum());
            info.setBind(item.get(i).isBind());
            builder.addItems(info);
        }
        MessageUtils.send_to_player(player, WelfareMessage.ResWelfareReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }
}
