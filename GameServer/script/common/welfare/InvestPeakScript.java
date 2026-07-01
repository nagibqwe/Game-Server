package common.welfare;

import com.data.*;
import com.data.bean.Cfg_InvestPeak_Bean;
import com.data.bean.Cfg_InvestPeak_Global_Bean;
import com.data.bean.Cfg_InvestPeak_Level_Bean;
import com.data.container.Cfg_InvestPeak_Level_Container;
import com.game.backpack.structs.BindStatus;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.recharge.script.IRechargeReward;
import com.game.redpacket.structs.RedPacketEnum;
import com.game.script.structs.ScriptEnum;
import com.game.task.structs.TaskHelp;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.welfare.script.IInvestPeakScript;
import com.game.welfare.struct.GrowthFund;
import game.core.util.IDConfigUtil;
import game.message.WelfareMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class InvestPeakScript implements IInvestPeakScript, IRechargeReward {

    /**购买人次*/
    private static final int id_count = 0;
    /**购买等级*/
    private static final int id_level = 1;

    private static final Logger log = LogManager.getLogger(InvestPeakScript.class);
    /**
     * 请求购买巅峰基金
     *
     * @param player
     * @param gear
     */
    @Override
    public void onReqInvestPeakBuy(Player player, int gear) {
        Cfg_InvestPeak_Level_Bean bean = Cfg_InvestPeak_Level_Container.GetInstance().getValueByKey(gear);
        if(bean != null){
            boolean success = Manager.backpackManager.manager().removeItemOrCurrency(player, bean.getMoneyType(), bean.getDiamond(), IDConfigUtil.getLogId(), ItemChangeReason.InvestPeakCost);
            if(success){
                afterReward(player, "0", gear);
            }else{

            }
        }
    }

    /**
     * 请求领取巅峰基金奖励
     *
     * @param player
     * @param cfgID
     */
    @Override
    public void onReqInvestPeakGetAward(Player player, int cfgID) {
        GrowthFund fund = checkAndGet(player);
        if (fund == null)
            return;
        if (!fund.isBuy())
            return;

        Cfg_InvestPeak_Bean bean = CfgManager.getCfg_InvestPeak_Container().getValueByKey(cfgID);
        if (bean.getGear() != fund.getGear())
            return;
        if (fund.getRewardCfgID().contains(cfgID))
            return;

        // 发奖
        if (bean.getMoney().size() == 2)
            Manager.currencyManager.manager().onAddItemCoin(player, bean.getMoney().get(1), bean.getMoney().get(0),
                    ItemChangeReason.InvestPeakGet, cfgID);
        fund.getRewardCfgID().add(cfgID);
        freshDataNtf(player);

        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WELFARE_InvestmentPeak, ItemChangeReason.InvestPeakGet, bean.getLevel());
    }

    /**
     * 请求领取巅峰基金全服奖励
     *
     * @param player
     * @param cfgID
     */
    @Override
    public void onReqInvestPeakServer(Player player, int cfgID) {
        GrowthFund fund = checkAndGet(player);
        if (fund == null)
            return;

        Cfg_InvestPeak_Global_Bean bean = CfgManager.getCfg_InvestPeak_Global_Container().getValueByKey(cfgID);
        if (bean == null || bean.getReward().size() != 3)
            return;
        if (fund.getGear() != bean.getGear())
            return;
        if (player.getLevel() < bean.getLevel())
            return;
        if (getNumber() < bean.getTimes())
            return;
        if (fund.getRewardCfgIDServer().contains(cfgID))
            return;

        List<Item> items = Item.createItems(bean.getReward().get(0), bean.getReward().get(1), BindStatus.getBind(bean.getReward().get(2)));
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) != 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            return;
        }

        Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.InvestPeakGet, cfgID);
        fund.getRewardCfgIDServer().add(cfgID);
        freshDataNtf(player);
    }

    private GrowthFund checkAndGet(Player player) {
        if (player == null)
            return null;
        if (!Manager.welfareManager.isOpen(player, WelfareMessage.WelfareType.InvestPeak))
            return null;

        checkSrvAndPlayerReset(player);
        return player.getInvestPeak();
    }

    /**
     * 玩家上线
     *
     * @param player
     */
    @Override
    public void playerOnline(Player player) {
        if (player == null)
            return;
        GrowthFund fund = player.getInvestPeak();
        if (fund == null) {
            fund = GrowthFund.newGrowthFund();
            player.setInvestPeak(fund);
        }
        checkSrvAndPlayerReset(player);
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

        checkAndGet(player);

        GrowthFund fund = player.getInvestPeak();
        if (fund == null)
            return;

        WelfareMessage.ResInvestPeakData.Builder builder = WelfareMessage.ResInvestPeakData.newBuilder();
        builder.setIsBuy(fund.isBuy());
        builder.addAllRewardCfgID(fund.getRewardCfgID());
        builder.addAllRewardCfgIDServer(fund.getRewardCfgIDServer());
        builder.setGear(fund.getGear());
        builder.setBuyNum(getNumber());
        MessageUtils.send_to_player(player, WelfareMessage.ResInvestPeakData.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.InvestPeak;
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

    private void checkSrvAndPlayerReset(Player player) {
        // 检查服务器是否重置
        checkReset();

        // 把玩家之前满足条件的发奖，并且重置玩家个人数据
        if (player == null)
            return;
        GrowthFund fund = player.getInvestPeak();
        if (fund == null)
            return;

        // 都没有买巅峰基金，你来凑个啥？
        if (!fund.isBuy())
            return;

        int gear = getGear();
        if (fund.getGear() == gear)
            return;

        log.info("重置玩家个人巅峰基金数据：" + TaskHelp.getPlayerInfo(player));

        // 玩家历史发奖，并重置数据（只检查个人的奖励）
        for (Cfg_InvestPeak_Bean bean : CfgManager.getCfg_InvestPeak_Container().getValuees()) {
            if (bean.getGear() != fund.getGear())
                continue;
//            if (bean.getLevel() > player.getLevel())
//                continue;
            // 判断有没有领奖
            if (fund.getRewardCfgID().contains(bean.getID()))
                continue;

            // 发奖
            if (bean.getMoney().size() == 2) {
                Item item = Item.createItemCoin(bean.getMoney().get(1), bean.getMoney().get(0));
                List<Item> itemList = new ArrayList<>();
                itemList.add(item);
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                        MessageString.System, MessageString.Welfare_InvestPeak_MailTitle, MessageString.Welfare_InvestPeak_MailText, itemList, ItemChangeReason.InvestPeakGet);
            }

            fund.getRewardCfgID().add(bean.getID());
        }

        // 重置玩家数据
        fund.setGear(0);
        fund.setBuy(false);
        fund.getRewardCfgID().clear();
        fund.getRewardCfgIDServer().clear();
    }

    private synchronized void checkReset() {
        Cfg_InvestPeak_Level_Bean bean = getInvestLvlCfg();
        if (bean == null)
            return;

        if (bean.getInvestLevel() == getGear())
            return;

        // 需要重置了
        int gear = bean.getInvestLevel();
        log.info("重置全服巅峰基金 -> " + gear);
        Manager.countManager.setServerCount(BaseCountType.InvestPeak, Count.RefreshType.CountType_Forever, id_level, gear);
    }

    /**
     * 获取当前档位的配置
     *
     * @return
     */
    private Cfg_InvestPeak_Level_Bean getInvestLvlCfg() {
        Cfg_InvestPeak_Level_Bean ret = null;
        for (Cfg_InvestPeak_Level_Bean bean : CfgManager.getCfg_InvestPeak_Level_Container().getValuees()) {
            if (bean.getIfOpen() != 1)
                continue;

            if (ret != null)
                return null;

            ret = bean;
        }
        return ret;
    }

    /**
     * 获取巅峰基金档位
     *
     * @return
     */
    private int getGear() {
        return Manager.countManager.getServerCount(BaseCountType.InvestPeak,id_level);
    }

    /**
     * 获取巅峰基金全服购买次数
     *
     * @return
     */
    private int getNumber() {
        return Manager.countManager.getServerCount(BaseCountType.InvestPeak,id_count);
    }

    /**
     * 添加巅峰基金购买次数
     */
    @Override
    public synchronized void add() {
        int addNum = 1;
        if (Global.Welfare_Pray_Times.size() == 2)
            addNum += RandomUtils.random(Global.Welfare_Pray_Times.get(0), Global.Welfare_Pray_Times.get(1));
        Manager.countManager.addServerCount(BaseCountType.InvestPeak, Count.RefreshType.CountType_Forever, id_count, addNum);
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
        GrowthFund fund = checkAndGet(player);
        if (fund == null)
            return false;
        if (fund.isBuy())
            return false;

        Cfg_InvestPeak_Level_Bean bean = CfgManager.getCfg_InvestPeak_Level_Container().getValueByKey(goodId);
        if (bean == null)
            return false;

        return bean.getIfOpen() == 1;
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
        GrowthFund fund = checkAndGet(player);
        if (fund == null)
            return;

        log.info(TaskHelp.getPlayerInfo(player) + " 购买巅峰基金成功：" + goodId);

        fund.setBuy(true);
        fund.setGear(goodId);
        Manager.countManager.addCount(player, BaseCountType.InvestPeak, fund.getGear(), Count.RefreshType.CountType_Forever, 1);
//        Manager.controlManager.operate(player, FunctionVariable.BuyInvest, 1);
        // 添加全服购买人数
        add();
        freshDataNtf(player);

        //发红包
        Manager.redPacketManager.createRedpacket(player, RedPacketEnum.investPeak);
    }
}
