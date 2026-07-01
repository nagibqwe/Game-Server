package common.welfare;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Month_card_Bean;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.mail.manager.MailManager;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.recharge.script.IRechargeReward;
import com.game.recharge.structs.RechargeDefine;
import com.game.recharge.structs.RechargeItemInfo;
import com.game.redpacket.structs.RedPacketEnum;
import com.game.script.structs.ScriptEnum;
import com.game.structs.GlobalType;
import com.game.task.structs.TaskHelp;
import com.game.utils.MessageUtils;
import com.game.welfare.script.IExclusiveCardScript;
import com.game.welfare.struct.ExclusiveCard;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ExclusiveCardScript implements IExclusiveCardScript, IRechargeReward {
    private static final Logger log = LogManager.getLogger(ExclusiveCardScript.class);
    // 周卡
    public static final int WEEK_CARD = 1;
    // 月卡
    public static final int MONTH_CARD = 2;
    // 尊享卡
    public static final int EXCLUSIVE_CARD = 3;
    // 永久有效
    private final int FOREVER = -1;

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.ExclusiveCardBaseScript;
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
        if (player.getExclusiveCards() == null)
            player.setExclusiveCards(new ArrayList<>());


        //TODO  临时解决 因为添加了两个周卡导致玩家之前买了老月卡 周卡的奖励领取不到了，上线进行判断发放
        fixExclusiveCardReward(player,WEEK_CARD);
        fixExclusiveCardReward(player,MONTH_CARD);

        //发放未领取的奖励
        sendOldAward(player);
    }


    public void sendOldAward(Player player) {
        try{
            long now = TimeUtils.Time();
            for(ExclusiveCard card : player.getExclusiveCards()){
                if(card.isValid()){
                    Cfg_Month_card_Bean bean = CfgManager.getCfg_Month_card_Container().getValueByKey(card.getId());
                    if (bean == null){
                        return;
                    }
                    //当前剩余天数
                    int nowRemainDay = (int) ((card.getEndTime() - now) / GlobalType.MILLIS_PER_DAY);
                    //最后领取时的剩余天数
                    int lastRemainDay = 0;
                    long lastRewardTime = card.getLastRewardTime();
                    if(lastRewardTime == 0){
                        lastRemainDay = bean.getDay();
                    }else if(lastRewardTime > 0){
                        lastRemainDay = (int) ((card.getEndTime() - lastRewardTime) / GlobalType.MILLIS_PER_DAY);
                    }

                    int reason = card.getId() == 1 ? ItemChangeReason.WelfareCardWeekGet : ItemChangeReason.WelfareExclusiveCardGet;

                    for(; lastRemainDay >(nowRemainDay + 1); lastRemainDay--){
                        if(lastRewardTime == 0){
                            lastRewardTime = card.getEndTime() - bean.getDay() * GlobalType.MILLIS_PER_DAY + 3600000;
                        }else{
                            lastRewardTime = card.getLastRewardTime() + GlobalType.MILLIS_PER_DAY;
                        }
                        card.setLastRewardTime(lastRewardTime);
                        sendMail(player, bean.getDayAddItem(), lastRemainDay-1, reason);
                    }
                }
            }
        }catch (Exception e){
            log.error("自动发送特权卡奖励异常",e);
        }
    }

    /**
     *
     * @param player
     * @param es
     * @param day
     * @param reason
     */
    private void sendMail(Player player, ReadIntegerArrayEs es, int day, int reason) {
        List<Item> items = Item.createItems(es);
        String content = MailManager.linkContext(MessageString.Month_Card_Reward_Miss_mail_tex, day);
        Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                MessageString.System, MessageString.Month_Card_Reward_Miss_mail, content, items, reason);
    }

    //TODO 临时解决 因为添加了两个周卡导致玩家之前买了老月卡 周卡的奖励领取不到了，上线进行判断发放
    private void fixExclusiveCardReward(Player player,int cardId){

        if (player == null)
            return;
        if (!Manager.welfareManager.isOpen(player, WelfareMessage.WelfareType.ExclusiveCard))
            return;
        ExclusiveCard card = card(player, cardId);
        if (card == null)
            return;

        long now = TimeUtils.Time();
        if (!card.isValid() || (card.getLastRewardTime() > 0 && TimeUtils.isSameDay(card.getLastRewardTime(), now)))
            return;

        Cfg_Month_card_Bean bean = CfgManager.getCfg_Month_card_Container().getValueByKey(cardId);
        if (bean == null)
            return;

        card.setLastRewardTime(now);

        long actionId = IDConfigUtil.getLogId();
        List<Item> items = Item.createItems(bean.getDayAddItem());

        int reason = cardId == 1 ? ItemChangeReason.WelfareExclusiveCardGet : ItemChangeReason.WelfareCardWeekGet;

        mail(player, cardId, items, actionId);

        freshDataNtf(player);

        //记录BI数据
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WELFARE_Card, reason, cardId);

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

        WelfareMessage.ResExclusiveCardData.Builder builder = WelfareMessage.ResExclusiveCardData.newBuilder();
        long nowTime = TimeUtils.Time();
        for (ExclusiveCard card : player.getExclusiveCards()) {
            if (!card.isValid())
                continue;
            int rd = getRemainDay(card, nowTime);
            if (rd == 0)
                continue;

            WelfareMessage.CardInfo.Builder cb = WelfareMessage.CardInfo.newBuilder();
            cb.setId(card.getId());
            cb.setRemain(rd);
            cb.setReceive(TimeUtils.isSameDay(card.getLastRewardTime(), nowTime));
            builder.addOwnedCards(cb);
        }
        MessageUtils.send_to_player(player, WelfareMessage.ResExclusiveCardData.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }


    /**
     * 购买
     *
     * @param player
     * @param cardId
     */
    @Override
    public void onReqExclusiveCard(Player player, int cardId) {
        if (player == null)
            return;
        if (!Manager.welfareManager.isOpen(player, WelfareMessage.WelfareType.ExclusiveCard))
            return;

        ExclusiveCard card = card(player, cardId);
        if (card == null) {
            card = new ExclusiveCard(cardId);
            player.getExclusiveCards().add(card);
        }

        if (card.isValid())
            return;

        if (cardId != WEEK_CARD && cardId != MONTH_CARD && cardId != EXCLUSIVE_CARD && cardId!= 4 && cardId !=5)
            return;

        Cfg_Month_card_Bean bean = CfgManager.getCfg_Month_card_Container().getValueByKey(cardId);
        if (bean == null || bean.getCost().size() != 2)
            return;

        if (!Manager.currencyManager.manager().canDecItemCoin(player, bean.getCost().get(1), bean.getCost().get(0)))
            return;

        long actionId = IDConfigUtil.getLogId();

        // 扣钱
        Manager.currencyManager.manager().onDecItemCoin(player, bean.getCost().get(1), ItemChangeReason.WelfareExclusiveCardDec, cardId, bean.getCost().get(0));

        if (bean.getDay() > 0) {
            long nowTime = TimeUtils.Time();
            long time = TimeUtils.getBeginTime(nowTime) + bean.getDay() * (long)GlobalType.MILLIS_PER_DAY;
            card.setEndTime(time);
        } else {
            card.setEndTime(FOREVER);
        }

        log.info(TaskHelp.getPlayerInfo(player) + " 购买卡成功, ID:" + cardId);

        Manager.countManager.addCount(player, BaseCountType.WelfareCard, cardId, Count.RefreshType.CountType_Forever, 1);
        Manager.controlManager.operate(player, FunctionVariable.WelfareCard, 1);

        // 发购买奖
        List<Item> items = Item.createItems(bean.getNowitem());
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.WelfareExclusiveCard, actionId);
        }else {
            mail(player, cardId, items, actionId);
        }
        freshDataNtf(player);

        //发红包
        if(cardId == 4){
            Manager.redPacketManager.createRedpacket(player, RedPacketEnum.monthCard);
        }else if(cardId == 5){
            Manager.redPacketManager.createRedpacket(player, RedPacketEnum.superMonthCard);
        }
    }

    /**
     * 领取奖励
     *
     * @param player
     * @param cardId
     */
    @Override
    public void onReqExclusiveCardReward(Player player, int cardId) {
        if (player == null)
            return;
        if (!Manager.welfareManager.isOpen(player, WelfareMessage.WelfareType.ExclusiveCard))
            return;
        ExclusiveCard card = card(player, cardId);
        if (card == null)
            return;

        long now = TimeUtils.Time();
        if (!card.isValid() || (card.getLastRewardTime() > 0 && TimeUtils.isSameDay(card.getLastRewardTime(), now)))
            return;

        Cfg_Month_card_Bean bean = CfgManager.getCfg_Month_card_Container().getValueByKey(cardId);
        if (bean == null)
            return;

        card.setLastRewardTime(now);

        long actionId = IDConfigUtil.getLogId();
        List<Item> items = Item.createItems(bean.getDayAddItem());

        int reason = cardId == 1 ? ItemChangeReason.WelfareExclusiveCardGet : ItemChangeReason.WelfareCardWeekGet;

        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0)
            Manager.backpackManager.manager().addItems(player, items, reason, actionId);
        else
            mail(player, cardId, items, actionId);

        freshDataNtf(player);

        //记录BI数据
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WELFARE_Card, reason, cardId);
    }

    /**
     * 是否有周卡
     *
     * @param player
     * @return
     */
    @Override
    public boolean haveWeekCard(Player player) {
        return haveCard(player, WEEK_CARD);
    }

    /**
     * 是否有月卡
     *
     * @param player
     * @return
     */
    @Override
    public boolean haveMonthCard(Player player) {
        return haveCard(player, MONTH_CARD);
    }

    /**
     * 是否有尊享卡
     *
     * @param player
     * @return
     */
    @Override
    public boolean haveExclusiveCard(Player player) {
        return haveCard(player, EXCLUSIVE_CARD);
    }

    private boolean haveCard(Player player, int cardId) {
        ExclusiveCard card = card(player, cardId);
        if (card == null)
            return false;
        return card.isValid();
    }

    @Override
    public int getWeekCardRate(Player player) {
        return getCardRate(player, WEEK_CARD);
    }

    @Override
    public int getMonthCardRate(Player player) {
        return getCardRate(player, MONTH_CARD);
    }

    private int getCardRate(Player player, int cardId) {
        if (haveCard(player, cardId)) {
            return 0;
        }
        Cfg_Month_card_Bean bean = CfgManager.getCfg_Month_card_Container().getValueByKey(cardId);
        return bean.getMagicBowl();
    }

    private void mail(Player player, int cardId, List<Item> items, long actionId) {
        int title = 0;
        int content = 0;
        switch (cardId) {
            case WEEK_CARD:
                title = MessageString.Welfare_Card_MailTitle2;
                content = MessageString.Welfare_Card_MailText2;
                break;
            case MONTH_CARD:
                title = MessageString.Welfare_Card_MailTitle;
                content = MessageString.Welfare_Card_MailText;
                break;
            case EXCLUSIVE_CARD:
                title = MessageString.Welfare_Card_MailTitle1;
                content = MessageString.Welfare_Card_MailText1;
                break;
        }

        Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                MessageString.System, title, content, items, ItemChangeReason.WelfareExclusiveCard, actionId);
    }

    private ExclusiveCard card(Player player, int cardId) {
        List<ExclusiveCard> cardList = player.getExclusiveCards();
        if (cardList == null || cardList.size() == 0)
            return null;

        for (ExclusiveCard card : cardList) {
            if (card.getId() == cardId)
                return card;
        }
        return null;
    }

    private int getRemainDay(ExclusiveCard card, long nowTime) {
        if (card == null)
            return 0;

        if (card.getEndTime() == FOREVER)
            return FOREVER;

        int diffDay = TimeUtils.getBetweenDays(card.getEndTime(), nowTime);
        if (diffDay == 0 && card.getEndTime() > nowTime)
            diffDay = 1;
        return diffDay;
    }


    /**
     * 获取剩余时间
     * @param player
     * @param cfgGoodId
     * @return
     */
    public long getEndTime(Player player,int cfgGoodId){
        long lastTime = 0;
        int cardId = type(cfgGoodId);
        ExclusiveCard card = card(player, cardId);
        if (card.getEndTime() == -1 ){
            return 0;
        }
        lastTime = card.getEndTime() - TimeUtils.Time();
        return lastTime;
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
        if (player == null)
            return false;
        if (!Manager.welfareManager.isOpen(player, WelfareMessage.WelfareType.ExclusiveCard))
            return false;

        int cardId = type(goodId);
        if(cardId == 0) return false;
        ExclusiveCard card = card(player, cardId);
        if (card == null) {
            card = new ExclusiveCard(cardId);
            player.getExclusiveCards().add(card);
        }
        return !card.isValid();
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
        if (player == null)
            return;
        if (!Manager.welfareManager.isOpen(player, WelfareMessage.WelfareType.ExclusiveCard))
            return;

        int cardId = type(goodId);
        if(cardId == 0) return;

        ExclusiveCard card = card(player, cardId);
        if (card == null) {
            card = new ExclusiveCard(cardId);
            player.getExclusiveCards().add(card);
        }

        if (card.isValid())
            return;

        if (cardId != WEEK_CARD && cardId != MONTH_CARD && cardId != EXCLUSIVE_CARD && cardId!= 4 && cardId !=5)
            return;

        Cfg_Month_card_Bean bean = CfgManager.getCfg_Month_card_Container().getValueByKey(cardId);
        if (bean == null)
            return;

        if (bean.getDay() > 0) {
            long nowTime = TimeUtils.Time();
            long time = TimeUtils.getBeginTime(nowTime) + bean.getDay() * GlobalType.MILLIS_PER_DAY;
            card.setEndTime(time);
        } else {
            card.setEndTime(FOREVER);
        }
        log.info(TaskHelp.getPlayerInfo(player) + " 购买卡成功, ID:" + cardId);

        Manager.countManager.addCount(player, BaseCountType.WelfareCard, cardId, Count.RefreshType.CountType_Forever, 1);
        Manager.controlManager.operate(player, FunctionVariable.WelfareCard, 1);

        long actionId = IDConfigUtil.getLogId();
        // 发购买奖
        List<Item> items = Item.createItems(bean.getNowitem());
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0)
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.WelfareExclusiveCard, actionId);
        else
            mail(player, cardId, items, actionId);

        freshDataNtf(player);
    }

    private int type(int goodId) {
        RechargeItemInfo cfg = Manager.rechargeManager.getRechargeItemInfoMap().get(goodId);
        if (cfg == null)
            return 0;

        switch (cfg.getGoods_type()) {
            case RechargeDefine.RECHARGE_TYPE_WEEK_CARD:
                return WEEK_CARD;
            case RechargeDefine.RECHARGE_TYPE_MONTH_CARD:
                return MONTH_CARD;
            case RechargeDefine.RECHARGE_TYPE_EXCLUSIVE_CARD:
                return EXCLUSIVE_CARD;
            //紧急热更新，添加两个周卡 策划喊写死
            case 91:
                return 4;
            case 92:
                return 5;

        }
        return 0;
    }
}
