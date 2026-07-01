package common.recharge;

import com.data.*;
import com.data.bean.Cfg_Limit_direct_shop_Bean;
import com.data.bean.Cfg_Limit_gold_shop_Bean;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.player.structs.Player;
import com.game.recharge.script.IDiscountRechargeScript;
import com.game.recharge.script.IRechargeReward;
import com.game.recharge.structs.RechargeDiscount;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.net.Config.ServerConfig;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.RechargeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO
 * @Date 2020/8/7 16:24
 * @Auth ZUncle
 */
public class DiscountRechargeScript implements IDiscountRechargeScript, IRechargeReward {

    private final Logger log = LogManager.getLogger("RechargeManager");

    int ActiveCommon = 0;             //普通
    int ActiveFirst = 1;              //激活
    int ActiveDelay = 2;              //延期

    int DelayTime = 5 * 60 * 1000;  //延期5分钟

    final int RechargeType = 0;
    final int GoldType = 1;

    /**
     * 能否给奖励
     *
     * @param player
     * @param goodId
     * @return
     */
    @Override
    public boolean canReward(Player player, int goodId) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.LimitDicretShop)) {
            return false;
        }
        RechargeDiscount rd = player.getRechargeDiscounts().get(goodId);
        if (rd == null) {
            return false;
        }
        Cfg_Limit_direct_shop_Bean config = CfgManager.getCfg_Limit_direct_shop_Container().getValueByKey(rd.getId());
        if (config == null) {
            return false;
        }
        return rd.getCount() < config.getBuyNum();
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

        RechargeDiscount rd = player.getRechargeDiscounts().get(goodId);
        if (rd == null) {
            return;
        }
        rd.setCount(rd.getCount() + 1);
        sendDiscountMessage(player, RechargeType, ActiveCommon, false);
        Manager.countManager.addCount(player, BaseCountType.Direct_shop, rd.getId(), Count.RefreshType.CountType_Forever, rd.getId());
        Manager.controlManager.operate(player, FunctionVariable.Limit_direct_shop_condition, 1);
        log.info("超值折扣充值完成 id={} player={}", goodId, player);
    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.DiscountRechargeScript;
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
     * 请求超值折扣数据
     *
     * @param player
     * @param mess
     */
    @Override
    public void reqDiscountRecharge(Player player, RechargeMessage.ReqDiscountRecharge mess) {
        if (mess.getType() == RechargeType) {
            if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.LimitDicretShop)) {
                return;
            }
        } else {
            if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.LimitDicretShop2)) {
                return;
            }
        }
        sendDiscountMessage(player, mess.getType(), ActiveCommon, false);
    }

    /**
     * 超值折扣充值检测
     *
     * @param player
     * @param mess
     */
    @Override
    public void checkDiscountRecharge(Player player, RechargeMessage.ReqCheckDiscRechargeGoods mess) {

        ConcurrentHashMap<Integer, RechargeDiscount> stop = mess.getType() == RechargeType ? player.getRechargeDiscounts() : player.getGoldDiscounts();

        RechargeMessage.ResCheckDiscRechargeGoods.Builder message = RechargeMessage.ResCheckDiscRechargeGoods.newBuilder();
        for (int goodsId : mess.getGoodsIdList()) {

            RechargeDiscount discount = stop.get(goodsId);
            //0=可购买 1=条件不满足 2=已过期
            RechargeMessage.CheckDiscState.Builder goods = RechargeMessage.CheckDiscState.newBuilder();
            goods.setGoodsId(goodsId);

            if (mess.getType() == RechargeType) {
                Cfg_Limit_direct_shop_Bean config = CfgManager.getCfg_Limit_direct_shop_Container().getValueByKey(goodsId);
                if (discount == null || config == null) {
                    goods.setState(1);
                    message.addCheck(goods);
                    continue;
                }
                if (discount.getTimeout() < TimeUtils.Time() || discount.getCount() >= config.getBuyNum()) {
                    goods.setState(2);
                    message.addCheck(goods);
                    continue;
                }
            } else {
                Cfg_Limit_gold_shop_Bean config = CfgManager.getCfg_Limit_gold_shop_Container().getValueByKey(goodsId);
                if (discount == null || config == null) {
                    goods.setState(1);
                    message.addCheck(goods);
                    continue;
                }
                if (discount.getTimeout() < TimeUtils.Time() || discount.getCount() >= config.getBuyNum()) {
                    goods.setState(2);
                    message.addCheck(goods);
                    continue;
                }
            }

            goods.setState(0);
            message.setType(mess.getType());
            message.addCheck(goods);
        }
        MessageUtils.send_to_player(player, RechargeMessage.ResCheckDiscRechargeGoods.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    RechargeMessage.DiscountRechargeItem.Builder pack(RechargeDiscount rd) {
        RechargeMessage.DiscountRechargeItem.Builder builder = RechargeMessage.DiscountRechargeItem.newBuilder();
        builder.setId(rd.getId());
        builder.setCount(rd.getCount());
        if (rd.getTimeout() < 0) {
            builder.setTimeout(-1);
        } else {
            Long remainTime = rd.getTimeout() - TimeUtils.Time();
            builder.setTimeout((int) (remainTime / 1000));
        }
        return builder;
    }

    /**
     * 超值折扣充值 开始倒计时
     *
     * @param player
     */
    @Override
    public void checkDiscountRecharge(Player player) {

        //TODO 元宝超值折扣
        checkGoldDiscount(player);

        //TODO 充值超值折扣
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.LimitDicretShop)) {
            return;
        }
        RechargeMessage.ResDiscountRechargeData.Builder builder = RechargeMessage.ResDiscountRechargeData.newBuilder();
        builder.setFirst(ActiveCommon);
        builder.setType(RechargeType);

        long limit = Global.Limit_direct_shop_time_limit;
        long last = player.getLastDiscFreeGiftTime();
        long remain = last + limit * 60 * 1000L - TimeUtils.Time();
        builder.setFreeGoodsRemainTime(Math.max(remain, 0));

        for (Cfg_Limit_direct_shop_Bean cfg : CfgManager.getCfg_Limit_direct_shop_Container().getValuees()) {
            if (player.getRechargeDiscounts().containsKey(cfg.getId())) {
                continue;
            }
            if (ServerConfig.getIsShenHe() > 0 || (Manager.controlManager.deal().checkFuncProgress(player, cfg.getCondition()) && Manager.controlManager.deal().checkFuncProgressSomeone(player, cfg.getCondition2()))) {
                RechargeDiscount rd = new RechargeDiscount();
                builder.setFirst(ActiveFirst);
                rd.setId(cfg.getId());
                if (cfg.getTime() <= 0) {
                    rd.setTimeout(-1);
                } else {

                    rd.setTimeout(MapUtils.getEndTime(cfg.getTime()));
                }
                player.getRechargeDiscounts().put(rd.getId(), rd);
                builder.addItems(pack(rd));

                log.info("生成超值折扣数据id={} timeout={} player={}", rd.getId(), rd.getTimeout(), player);
            }
        }
        MessageUtils.send_to_player(player, RechargeMessage.ResDiscountRechargeData.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    void checkGoldDiscount(Player player) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.LimitDicretShop2)) {
            return;
        }

        RechargeMessage.ResDiscountRechargeData.Builder builder = RechargeMessage.ResDiscountRechargeData.newBuilder();
        builder.setFirst(ActiveCommon);
        builder.setType(GoldType);

        long limit = Global.Limit_gold_shop_time_limit;
        long last = player.getLastGoldDiscFreeGiftTime();
        long remain = last + limit * 60 * 1000L - TimeUtils.Time();
        builder.setFreeGoodsRemainTime(Math.max(remain, 0));

        for (Cfg_Limit_gold_shop_Bean cfg : CfgManager.getCfg_Limit_gold_shop_Container().getValuees()) {
            if (player.getGoldDiscounts().containsKey(cfg.getId())) {
                continue;
            }

            if (ServerConfig.getIsShenHe() > 0 || (Manager.controlManager.deal().checkFuncProgress(player, cfg.getCondition()) && Manager.controlManager.deal().checkFuncProgressSomeone(player, cfg.getCondition2()))) {
                RechargeDiscount rd = new RechargeDiscount();
                builder.setFirst(ActiveFirst);
                rd.setId(cfg.getId());
                if (cfg.getTime() <= 0) {
                    rd.setTimeout(-1);
                } else {

                    rd.setTimeout(MapUtils.getEndTime(cfg.getTime()));
                }
                player.getGoldDiscounts().put(rd.getId(), rd);
                builder.addItems(pack(rd));

                log.info("元宝生成超值折扣数据id={} timeout={} player={}", rd.getId(), rd.getTimeout(), player);
            }
        }
        MessageUtils.send_to_player(player, RechargeMessage.ResDiscountRechargeData.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 玩家上线
     *
     * @param player
     */
    @Override
    public void online(Player player) {
        sendDiscountMessage(player, RechargeType, ActiveCommon, true);
        sendDiscountMessage(player, GoldType, ActiveCommon, true);
    }

    int checkCount(int coinType, RechargeDiscount rd) {
        if (coinType == RechargeType) {
            Cfg_Limit_direct_shop_Bean config = CfgManager.getCfg_Limit_direct_shop_Container().getValueByKey(rd.getId());
            if (config == null) {
                return -1;
            }
            if (rd.getCount() > config.getBuyNum()) {
                return -2;
            }
        } else {
            Cfg_Limit_gold_shop_Bean config = CfgManager.getCfg_Limit_gold_shop_Container().getValueByKey(rd.getId());
            if (config == null) {
                return -1;
            }
            if (rd.getCount() > config.getBuyNum()) {
                return -2;
            }
        }
        return 0;
    }

    void sendDiscountMessage(Player player, int coinType, int active, boolean login) {
        RechargeMessage.ResDiscountRechargeData.Builder builder = RechargeMessage.ResDiscountRechargeData.newBuilder();
        builder.setFirst(active);
        builder.setType(coinType);

        long limit = coinType == RechargeType ? Global.Limit_direct_shop_time_limit : Global.Limit_gold_shop_time_limit;
        long last = coinType == RechargeType ? player.getLastDiscFreeGiftTime() : player.getLastGoldDiscFreeGiftTime();
        long remain = last + limit * 60 * 1000L - TimeUtils.Time();
        builder.setFreeGoodsRemainTime(Math.max(remain, 0));

        ConcurrentHashMap<Integer, RechargeDiscount> stop = coinType == RechargeType ? player.getRechargeDiscounts() : player.getGoldDiscounts();

        for (RechargeDiscount rd : stop.values()) {
            if (checkCount(coinType, rd) < 0) {
                continue;
            }
            if (rd.getTimeout() > 0 && TimeUtils.Time() > rd.getTimeout()) {
                if (rd.isDelay()) {
                    continue;
                }
                //TODO 已过期，登陆可以延期一次
                if (login) {
                    rd.setDelay(true);
                    rd.setTimeout(TimeUtils.Time() + DelayTime);
                    builder.setFirst(ActiveDelay);
                    log.info("超值折扣延期 id={} player={}", rd.getId(), player);
                }
            }
            builder.addItems(pack(rd));
        }
        MessageUtils.send_to_player(player, RechargeMessage.ResDiscountRechargeData.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }


    /**
     * 玩家下线检测 超值充值
     *
     * @param player
     */
    @Override
    public void offline(Player player) {

        for (RechargeDiscount rd : player.getRechargeDiscounts().values()) {
            if (rd.getTimeout() < 0) {
                continue;
            }
            if (rd.isDelay()) {
                continue;
            }
            //TODO 在线过期的，不用延期
            if (rd.getTimeout() < TimeUtils.Time()) {
                rd.setDelay(true);
            }
        }

        for (RechargeDiscount rd : player.getGoldDiscounts().values()) {
            if (rd.getTimeout() < 0) {
                continue;
            }
            if (rd.isDelay()) {
                continue;
            }
            //TODO 在线过期的，不用延期
            if (rd.getTimeout() < TimeUtils.Time()) {
                rd.setDelay(true);
            }
        }
    }

    @Override
    public long getRemainTime(Player player, int cfgGoodId) {
        long remainTime = 0;
        RechargeDiscount rd = player.getRechargeDiscounts().get(cfgGoodId);
        if (rd == null || rd.getTimeout() == -1) {
            return 0;
        }
        remainTime = rd.getTimeout() - TimeUtils.Time();
        return remainTime;
    }

    /**
     * 购买商品
     *
     * @param player
     * @param messInfo
     */
    @Override
    public void reqDiscRechargeBuyGoods(Player player, RechargeMessage.ReqDiscRechargeBuyGoods messInfo) {

        RechargeDiscount rd = player.getGoldDiscounts().get(messInfo.getGoodsId());
        if (rd == null) {
            return;
        }
        Cfg_Limit_gold_shop_Bean config = CfgManager.getCfg_Limit_gold_shop_Container().getValueByKey(rd.getId());

        if (rd.getCount() >= config.getBuyNum() || messInfo.getCount() < 0 || messInfo.getCount() > config.getBuyNum() - rd.getCount()) {
            return;
        }
        int coin = config.getPrice().get(0);
        int price = config.getPrice().get(1);

        //TODO 开始购买
        long action = IDConfigUtil.getLogId();
        if (Manager.currencyManager.manager().onDecItemCoin(player, price, ItemChangeReason.GoldDiscountCost, action, coin * messInfo.getCount())) {
            List<Item> items = Item.createItems(player.getCareer(), config.getReward(), messInfo.getCount());

            if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.GoldDiscountReward, action)) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.GoldDiscountReward);
            }
            rd.setCount(rd.getCount() + 1);
            sendDiscountMessage(player, GoldType, ActiveCommon, false);
            Manager.countManager.addCount(player, BaseCountType.Direct_shop, rd.getId(), Count.RefreshType.CountType_Forever, rd.getId());
            Manager.controlManager.operate(player, FunctionVariable.Limit_direct_shop_condition, 1);
            log.info("元宝超值折扣购买完成 id={} player={}", config.getId(), player);
        }
    }

    /**
     * 领取免费超值折扣
     *
     * @param player
     * @param messInfo
     */
    @Override
    public void reqGetFreeDiscGoods(Player player, RechargeMessage.ReqGetFreeDiscGoods messInfo) {
        
        long curTime = TimeUtils.Time();
        long action = IDConfigUtil.getLogId();
        if (messInfo.getType() == RechargeType) {
            if (player.getLastDiscFreeGiftTime() + Global.Limit_direct_shop_time_limit * 60 * 1000L > curTime) {
                return;
            }
            player.setLastDiscFreeGiftTime(curTime);

            List<Item> items = Item.createItems(player.getCareer(), Global.Limit_direct_shop_time_reward, 1);

            if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.FreeDiscountReward, action)) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.FreeDiscountReward);
            }
            checkDiscountRecharge(player);
        } else {
            if (player.getLastDiscFreeGiftTime() + Global.Limit_gold_shop_time_limit * 60 * 1000L > curTime) {
                return;
            }
            player.setLastGoldDiscFreeGiftTime(curTime);

            List<Item> items = Item.createItems(player.getCareer(), Global.Limit_gold_shop_time_reward, 1);

            if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.FreeGoldDiscountReward, action)) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.FreeGoldDiscountReward);
            }
            checkGoldDiscount(player);
        }
    }
}
