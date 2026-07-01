package common.recharge;

import com.data.*;
import com.data.bean.Cfg_Sdkplatform_Bean;
import com.data.struct.QQConfig;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.count.structs.VariantType;
import com.game.db.bean.RechargeBean;
import com.game.db.bean.RechargeTotalMoneyBean;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.player.structs.Player;
import com.game.recharge.handler.RechargePlaceOrderHandler;
import com.game.recharge.handler.RechargeRewardHandler;
import com.game.recharge.log.RechargeLog;
import com.game.recharge.script.IRechargeReward;
import com.game.recharge.script.IRechargeScript;
import com.game.recharge.structs.Recharge;
import com.game.recharge.structs.RechargeDefine;
import com.game.recharge.structs.RechargeItemInfo;
import com.game.recharge.structs.ServerOrderData;
import com.game.register.structs.UserInfo;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.welfare.script.IExclusiveCardScript;
import game.core.dblog.LogService;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.core.util.*;
import game.message.RechargeMessage;
import game.message.VipMessage;
import game.message.WelfareMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @explain: 充值相关逻辑脚本
 * @time Created on 2019/11/21 15:32.
 * @author: tc
 */
public class RechargeScript implements IScript, IRechargeScript, IRechargeReward {
    private final Logger log = LogManager.getLogger("RechargeManager");

    @Override
    public int getId() {
        return ScriptEnum.RechargeManagerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


    /**
     * 服务器下单订单 修补
     * @param recharge
     * @param serverOrderData
     * @return
     */
    private Recharge repairRecharge(Recharge recharge,ServerOrderData serverOrderData){

        if (!Manager.rechargeManager.getRechargeItemInfoMap().containsKey(serverOrderData.getGoodsId())){
            log.error("ServerOrderData  Goods_id:  {} not found   " ,serverOrderData.getGoodsId() );
            return null;
        }
        RechargeItemInfo rechargeItemInfo =  Manager.rechargeManager.getRechargeItemInfoMap().get(serverOrderData.getGoodsId());
        recharge.setGoods_id(serverOrderData.getGoodsId());
        recharge.setRole_id(serverOrderData.getRoleId());
        recharge.setGoods_type(rechargeItemInfo.getGoods_type()+"");
        recharge.setGame_money(gold(rechargeItemInfo));
        recharge.setGoods_name(rechargeItemInfo.getGoods_name());
        recharge.setGoods_code(rechargeItemInfo.getGoods_system_cfg_id()+"");
        recharge.setMoney_type(serverOrderData.getMoneyType());
        return recharge;
    }



    /**
     * 充值来了
     *
     * @param recharge
     * @param data
     * @param src
     * @return
     */
    @Override
    public int Recharge(Recharge recharge, String data, Byte src) {
        log.info("order: " + recharge.getOrder_no() + " src:" + src + " recharge data:" + data + " struct:" + recharge.toString());

        if (!src.equals(RechargeDefine.SRC_BACKEND) && !src.equals(RechargeDefine.SRC_GM)){
            if (Manager.rechargeManager.getServerOrderMap().containsKey(recharge.getOrder_no())){
                ServerOrderData orderData =   Manager.rechargeManager.getServerOrderMap().get(recharge.getOrder_no());
                recharge =  repairRecharge(recharge,orderData);
                if (recharge == null){
                    return 0;
                }
            }
            if (!Manager.rechargeManager.getRechargeItemInfoMap().containsKey(recharge.getGoods_id())){
                log.error("Goods_id:  {} not found   order: {} " ,recharge.getGoods_id(), recharge.getOrder_no() );
                return 0;
            }
        }
        Player player = Manager.playerManager.getPlayer(recharge.getRole_id());
        if (player == null) {
            saveLog(recharge, data, src, RechargeDefine.STA_REASON_PlayerNotExist);
            log.error("order: " + recharge.getOrder_no() + " not found player");
            return 0;
        }

        if (Manager.rechargeManager.getRechargeMap().containsKey(recharge.getOrder_no())) {
            saveLog(recharge, data, src, RechargeDefine.STA_REASON_Repeated);
            log.error("order: " + recharge.getOrder_no() + " player:" + player.getInfo() + " already exist");
            return 0;
        }

        RechargeBean bean = change(recharge, data, player, src);
        if (Manager.rechargeManager.getDao().insert("", bean) != 1) {
            saveLog(bean, (byte) -3);
            log.error("order: " + recharge.getOrder_no() + " player:" + player.getInfo() + " insert db failed");
            return 0;
        }

        // add to ram
        addOrder(bean);


        if (recharge.getTotal_fee() > 0) {
            player.addRechargeTime(TimeUtils.Time());
        }
        saveLog(bean, RechargeDefine.STA_REASON_None);
//        Manager.biManager.getScript().biPay(player, bean.getGoodsId(), bean.getOrderNo(),
//                bean.getGoodsName(), bean.getTotalFee(), 3, bean.getGameMoney(), bean.getSrc(), player.getCurGps().getModelId(), player.getStateVip().getLv());
        Manager.biManager.get4399Script().payBiTo4399(player, bean.getSrc().byteValue() == RechargeDefine.SRC_NORMAL.byteValue() ? 1 : 0, bean.getOrderNo(), bean.getTotalFee(), bean.getGameMoney());

        // 等待发奖
        if (Manager.playerManager.isOnline(player.getId()))
            toReward(recharge.getOrder_no());
        log.info("order: " + recharge.getOrder_no() + " player:" + player.getInfo() + " add success");
        return 1;
    }


    /**
     * 发货
     *
     * @param order_id
     */
    @Override
    public void Reward(String order_id) {
        RechargeBean bean = Manager.rechargeManager.getRechargeMap().get(order_id);
        if (bean == null)
            return;

        byte sta = bean.getStatus();
        if (sta != RechargeDefine.STA_NEW)
            return;

        Player player = Manager.playerManager.getPlayer(bean.getRoleId());
        if (player == null)
            return;
        RechargeItemInfo cfg = Manager.rechargeManager.getRechargeItemInfoMap().get(bean.getGoodsId());
        boolean isCanReward = false;
        if (cfg != null){
            isCanReward = canReward(player,cfg);
        }
        bean.setStatus(RechargeDefine.STA_COMPLETE);
        if (Manager.rechargeManager.getDao().update("", bean) == 0) {
            // 回滚状态
            log.info(player.getInfo() + " 更新充值数据失败：" + order_id);
            bean.setStatus(sta);
            saveLog(bean, RechargeDefine.STA_REASON_Rollback);
            return;
        }
//		long actionId = IDConfigUtil.getLogId();
        List<Item> items = new ArrayList<>();
        int goldCount = bean.getGameMoney();
        if (cfg != null) {
            if (isCanReward) {
                // FIXME 以配置表为准
                int career = player.getCareer();
                int count = getCount(player, cfg);

                // 普通奖励
                int normalBer = 1;
                if (cfg.getGoods_multiple().size() == 2 && (cfg.getGoods_multiple().get(1) == -1 || count <= cfg.getGoods_multiple().get(1)))
                    normalBer *= cfg.getGoods_multiple().get(0);

                goldCount *= normalBer;
                items = Item.createItems(player.getCareer(), cfg.getGoods_reward(), normalBer);
                log.error("player rolid {} 充值 奖励 getGoods_reward  {}",player.getId(), cfg.getGoods_reward());
                // 额外奖励
                if (cfg.getGoods_extra_reward_limit() == -1 || count <= cfg.getGoods_extra_reward_limit()) {
                    List<Item> items1 = Item.createItems(career, cfg.getGoods_extra_reward(), 1);
                    items.addAll(items1);
                }
                afterReward(player, bean, cfg);
            } else {
                log.error(player.getInfo() + "" +
                        " 充值成功，但不发奖，设为异常订单order:  {}  Goods_type : {} Goods_subtype: {}  Goods_id:  {}  Goods_limit:{} getCount:{}",
                        order_id, cfg.getGoods_type(),cfg.getGoods_subtype(),cfg.getGoods_id(),cfg.getGoods_limit(), getCount(player, cfg) );
                bean.setStatus(RechargeDefine.STA_ERROR);
                saveLog(bean, RechargeDefine.STA_REASON_NoReward);
                if (Manager.rechargeManager.getDao().update("", bean) == 0) {
                    log.error(player.getInfo() + " 严重错误，设为异常订单保存数据库失败 " + order_id);
                }
//                Manager.biManager.getScript().biPay(player, bean.getGoodsId(), bean.getOrderNo(),
//                        bean.getGoodsName(), bean.getTotalFee(), 4, bean.getGameMoney(), bean.getSrc(), player.getCurGps().getModelId(), player.getStateVip().getLv());
                return;
            }
            saveLog(bean, RechargeDefine.STA_REASON_RewardNormal);
        } else {
            // FIXME 配置表不存在数据，以后台为准
            log.info(player.getInfo() + " 不知名的订单，后台有，配置表中没有：" + order_id);
            if (bean.getGameMoney() > 0) {
                Item item = Item.createItem(ItemCoinType.GemCoin, bean.getGameMoney(), false);
                items.add(item);
            }
            if (bean.getItemId() > 0) {
                Item item = Item.createItem(bean.getItemId(), 1, false);
                items.add(item);
            }
            saveLog(bean, RechargeDefine.STA_REASON_RewardNoConfig);
        }

        if (goldCount > 0)
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.RechargeRewardTitle,
                    MessageString.RechargeRewardContext + "@_@" + goldCount);

        //这里记录充值都改为真实货币,因为有些配置是没有游戏货币的
        accumulativeDayRecharge(player, cfg == null ? goldCount : cfg.getIsTotalRecharge());
        accumulativeRecharge(player, cfg == null ? goldCount : cfg.getIsTotalRecharge());

        long actionId = IDConfigUtil.getLogId();
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.RechargeSourceCode, actionId))
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.RechargeRewardMailTitle,
                    MessageString.RechargeRewardMailContext + "@_@" + goldCount + "@_@" + order_id, items, ItemChangeReason.RechargeSourceCode, actionId);

        callBack(player, bean);
        log.info("order: " + bean.getOrderNo() + " player:" + player.getInfo() + " reward success");
        //记录BI数据
        RechargeItemInfo cfg_rechargeItem_bean = Manager.rechargeManager.getRechargeItemInfoMap().get(bean.getGoodsId());
        if (cfg_rechargeItem_bean == null){
            log.error("充值配置找不到" + bean.getGoodsCfg());
            return;
        }
        //功能任务充值
        if(cfg_rechargeItem_bean.getGoods_type() == RechargeDefine.RECHARGE_TYPE_FUNCTION_TASK){
            Manager.functionTaskManager.getScript().onRecharge(player, bean.getGoodsId());
        }
        if(cfg_rechargeItem_bean.getGoods_type() == 1){//正常充值
            //新手礼包
            if (cfg_rechargeItem_bean.getGoods_subtype() == 2) {
                Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.Pay_Newbie, ItemChangeReason.ChargeNewPlayer, cfg_rechargeItem_bean.getGoods_id());
            }
            //周礼包
            else if (cfg_rechargeItem_bean.getGoods_subtype() == 3) {
                Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.Pay_Week, ItemChangeReason.ChargeEveryWeekFGift, cfg_rechargeItem_bean.getGoods_id());
            }
            //每日礼包
            else if (cfg_rechargeItem_bean.getGoods_subtype() == 4) {
                Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.Pay_Day, ItemChangeReason.ChargeEverydayGift, cfg_rechargeItem_bean.getGoods_id());
            }
            //其他一般充值
            else if (cfg_rechargeItem_bean.getGoods_subtype() == 1) {
                Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.Pay_Base, ItemChangeReason.Charge, cfg_rechargeItem_bean.getGoods_id());
            }
        }
        //狂欢周(周六狂欢)
        else if (cfg_rechargeItem_bean.getGoods_type() == 10) {
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.CRAZY_WEEKEND, ItemChangeReason.CrazyWeekend, cfg_rechargeItem_bean.getGoods_id());
        }
        //超值折扣
        else if (cfg_rechargeItem_bean.getGoods_type() == 9) {
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.DISCOUNT, ItemChangeReason.Chaozhizhekou, cfg_rechargeItem_bean.getGoods_id());
        }
        //月卡购买
        else if (cfg_rechargeItem_bean.getGoods_type() == 4) {
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WELFARE_Card, ItemChangeReason.WelfareExclusiveCard, cfg_rechargeItem_bean.getGoods_id());
        }
        //周卡购买
        else if (cfg_rechargeItem_bean.getGoods_type() == 3) {
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WELFARE_Card, ItemChangeReason.WelfareCardWeek, cfg_rechargeItem_bean.getGoods_id());
        }
        //成长基金
        else if (cfg_rechargeItem_bean.getGoods_type() == 6) {
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WELFARE_Investment, ItemChangeReason.WelfareGrowthFund, cfg_rechargeItem_bean.getGoods_id());
        }
        //神秘商店
        else if (cfg_rechargeItem_bean.getGoods_type() == 7) {
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.MYSTERY_SHOP, ItemChangeReason.MyShopRewardGet, cfg_rechargeItem_bean.getGoods_id());
        }
        //天禁令高级令牌
        else if (cfg_rechargeItem_bean.getGoods_type() == 12) {
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.FallingSky_Buy, ItemChangeReason.FallingSkyGet, cfg_rechargeItem_bean.getGoods_id());
        }
    }

    /**
     * 内部充值
     *
     * @param player
     * @param goodId
     */
    @Override
    public void onReqRecharge(Player player, int goodId) {
        RechargeItemInfo bean   = Manager.rechargeManager.getRechargeItemInfoMap().get(goodId);
        if (bean == null)
            return;
        int money =  getMoney(player,bean,"CNY");
        if (money > 0 && !ServerConfig.isTestServer())
            return;

        Recharge recharge = new Recharge();
        recharge.setOrder_no("INTERNAL_" + String.valueOf(TimeUtils.Time()));
        recharge.setGoods_id(goodId);
        recharge.setGoods_type("1");
        recharge.setGoods_ext("");
        recharge.setTotal_fee(money * 100);
        recharge.setGoods_code(String.valueOf(goodId));
        recharge.setGoods_name(bean.getGoods_name());
        recharge.setItem_id(0);
        recharge.setGame_money(0);
        recharge.setRole_id(player.getId());
        recharge.setExt_param("internal");
        recharge.setSign_type("1");
        recharge.setSign("tiancheng==");
        recharge.setMoney_type("CNY");
        Manager.rechargeManager.AddRecharge(null, recharge, JsonUtils.toJSONString(recharge), RechargeDefine.SRC_INTERNAL);
    }

    /**
     * 请求充值数据
     *
     * @param player
     */
    @Override
    public void onReqRechargeData(Player player) {
        syncRechargeData(player);
    }

    private void syncRechargeData(Player player) {
        RechargeMessage.ResRechargeData.Builder msg = RechargeMessage.ResRechargeData.newBuilder();
        for (RechargeItemInfo bean :Manager.rechargeManager.getRechargeItemInfoMap().values()) {
            if ((bean.getGoods_multiple().size() == 2 && bean.getGoods_multiple().get(1) != -1) ||
                    bean.getGoods_extra_reward_limit() != -1) {
                RechargeMessage.RechargeItem.Builder ri = RechargeMessage.RechargeItem.newBuilder();
                ri.setId(bean.getGoods_system_cfg_id());
                ri.setCount(getCount(player, bean));
                msg.addItems(ri);
            }
        }
        //log.info("NewBie player:"+ player.getNewbieRechargeTime()+";;name:"+player.getName());
        if(player.getNewbieRechargeTime() > 0) {
            long endtime = Global.Recharge_New_Bie_Gift_Limit * 3600 + player.getNewbieRechargeTime()/1000;
            //log.info("NewBie endtime:"+endtime+"player:"+ player.getNewbieRechargeTime());
            //新手礼包
            //判断实收审核服
            endtime = ServerConfig.getIsShenHe() >0 ? MapUtils.getEndTime( Global.Recharge_New_Bie_Gift_Limit * 3600):endtime;
            RechargeMessage.SubTypeTime.Builder st = RechargeMessage.SubTypeTime.newBuilder();
            st.setSubtype(RechargeDefine.RECHARGE_TYPE_NORMAL * 10000 + RechargeDefine.RECHARGE_REFRESH_TYPE_NEWBIE);
            st.setRemiantime(endtime);
            msg.addTypeTime(st);
        }

        MessageUtils.send_to_player(player, RechargeMessage.ResRechargeData.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 获取对应礼包充值次数
     * @param player
     * @param goodsId
     * @return
     */
    public int getRechargeNum(Player player,int goodsId){
        List<RechargeBean> list = Manager.rechargeManager.getRechargeList(player.getId());
        int count = 0;
        int OneKeyId = Global.DailyRechargeGift_Num.get(1).get(0) ;
        int onKeyBuyCount = 0;
        for (RechargeBean rechargeBean : list){
            if (rechargeBean.getGoodsCfgId() == goodsId){
                count++;
            }
            if (rechargeBean.getGoodsId() == OneKeyId){
                onKeyBuyCount++;
            }
        }
        for (Integer daliyId:Global.DailyRechargeGift_Num.get(0).getValue()){
            if (daliyId == goodsId){
                count += onKeyBuyCount;
            }
        }
        return count;
    }

    public int getRechargeNumForType(Player player,int actType){
        List<RechargeBean> list = Manager.rechargeManager.getRechargeList(player.getId());
        int count = 0;
        for (RechargeBean rechargeBean : list){
            RechargeItemInfo bean = Manager.rechargeManager.getRechargeItemInfoMap().get(rechargeBean.getGoodsId());
            if (bean == null){
                log.error("RechargeIteminfo  is nul" + rechargeBean.getGoodsId());
               continue;
            }
            if (bean.getGoods_subtype() == actType){
                count++;
            }
        }
        return count;
    }

    /**
     * 获取玩家某商品的充值次数
     *
     * @param player
     * @param goodBean
     * @return
     */
    private int getCount(Player player, RechargeItemInfo goodBean) {
        List<RechargeBean> list = Manager.rechargeManager.getRechargeList(player.getId());
        int count = 0;
        int goodID = goodBean.getGoods_system_cfg_id();
        int refreshType = getRechargeRefreshType(goodBean);
        //记录当前时间
        long curTime = TimeUtils.Time();
        for (RechargeBean bean : list) {
            if (bean.getStatus()!= RechargeDefine.STA_COMPLETE)
                continue;

            if (bean.getGoodsCfgId() == goodID) {
                if(refreshType == RechargeDefine.RECHARGE_REFRESH_TYPE_WEEK){
                    //检测本周充值
                    if(TimeUtils.isSameWeek(curTime,bean.getAddTime())){
                        count++;
                    }
                }else if(refreshType == RechargeDefine.RECHARGE_REFRESH_TYPE_DAY){
                    //检测今日充值
                    if(TimeUtils.isSameDay(curTime,bean.getAddTime())){
                        count++;
                    }
                }
                else
                {
                    //新手期类型和基础类型一样,都是终生制,不需要进行特别处理
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 获取重置次数的刷新类型
     * @param goodBean
     * @return
     */
    private int getRechargeRefreshType(RechargeItemInfo goodBean){
        int rechargeType = goodBean.getGoods_type();
        int type = RechargeDefine.RECHARGE_REFRESH_TYPE_NORMAL;
        //重置类型 1:普通,2:新手,3:周,4:日
        if(rechargeType == RechargeDefine.RECHARGE_TYPE_NORMAL){
            //普通类型的,通过定义的重置类型来处理
            type = goodBean.getGoods_subtype();
        } else if(rechargeType == RechargeDefine.RECHARGE_TYPE_CRAZYSAT){
            //狂欢周每天进行次数重置
            type = RechargeDefine.RECHARGE_REFRESH_TYPE_DAY;
        }
        return type;
    }

    /**
     * 充值成功回调
     *
     * @param player
     * @param bean
     */
    private void callBack(Player player, RechargeBean bean) {

        RechargeItemInfo rechargeItem_bean = Manager.rechargeManager.getRechargeItemInfoMap().get( bean.getGoodsId());

        try {
            // 充值了一笔钱
            int totalRecharge = rechargeItem_bean == null?bean.getTotalRecharge():rechargeItem_bean.getIsTotalRecharge();
            int totalVipPower = rechargeItem_bean == null ? bean.getTotalVipPower():rechargeItem_bean.getTotalVipPower();
            //Cfg_RechargeItem_Bean rechargeItem_bean = CfgManager.getCfg_RechargeItem_Container().getValueByKey( bean.getGoodsCfgId());

            Manager.commercializeManager.recharge(player, bean.getGoodsCfgId(), bean.getGameMoney(),totalRecharge);

            // 红包
//            Manager.redPacketManager.manager().rechargeSendRedpacket(player, bean.getGoodsId(), bean.getGameMoney());

            // 通知境界礼包更新
//            Manager.stateVipManager.deal().updateStateVipGifts(player);

            // 玩家充值以后保存玩家数据
            Manager.playerManager.manager().SavePlayer(player);

            //开服狂翻排名
            Manager.openServerAcManager.deal().onOperateRechargeGold(player, totalRecharge);
            //增加vip
            int vipExp =totalVipPower;
            Manager.vipManager.deal().addVipExp(player, vipExp, ItemChangeReason.RechargeVipExpGet, bean.getGoodsCfgId());
           //检测 高级VIP和高级至尊VIP
            Manager.vipManager.deal().checkSpecialVip(player);
            //运营活动
            Manager.activityManager.deal().rechargeDeal(player, bean.getGoodsCfgId(), totalRecharge);

            //充值触发任务检测
            Manager.controlManager.operate(player, FunctionVariable.Recharge_Money_Limit, 0);

            //充值触发任务检测
            Manager.controlManager.operate(player, FunctionVariable.Recharge_Gift_Limit, 0);

            //充值周福利抽奖检测
            Manager.controlManager.operate(player, FunctionVariable.Recharge_Money_Day, totalRecharge);

            //天禁令
            Manager.fallingSkyManager.deal().onResRefreshRechargeState(player,bean.getGoodsCfgId());
        } catch (Exception ex) {
            log.error(player.getInfo() + " except:" + bean.getOrderNo());
            log.error(ex, ex);
            saveLog(player, bean, RechargeDefine.STA_REASON_RewardExcept);
        }

        if (player.isOnline()) {
            sendRechargeAllData(player);
            syncRechargeData(player);
        }

        Manager.biManager.getScript().biPay(player, bean.getGoodsCfgId(), bean.getOrderNo(),
                bean.getGoodsName(), bean.getTotalFee(), 5, bean.getGameMoney(), bean.getSrc(), bean.getMoneyType());

        saveLog(player, bean, RechargeDefine.STA_REASON_Complete);


        //记录充值返利
        ServerOrderData serverOrderData = Manager.rechargeManager.getServerOrderMap().get(bean.getOrderNo());
        String moneyType = serverOrderData == null ?  "CNY": serverOrderData.getMoneyType();
        Manager.rechargeRebateManager.deal().addTotalRecharge(player,getMoney(player, rechargeItem_bean, moneyType));

        Manager.rechargeManager.getServerOrderMap().remove(bean.getOrderNo());
    }



    private RechargeBean change(Recharge recharge, String data, Player player, Byte src) {
        RechargeItemInfo itemInfo = Manager.rechargeManager.getRechargeItemInfoMap().get(recharge.getGoods_id());
        String goods_system_cfgid = itemInfo == null ? "0":itemInfo.getGoods_system_cfg_id()+"";
        RechargeBean bean = new RechargeBean();
        bean.setOrderNo(recharge.getOrder_no());
        bean.setUserId(player.getUserId());
        bean.setRoleId(player.getId());
        bean.setSrvId(ServerConfig.getServerId());
        bean.setGoodsId(recharge.getGoods_id());
        bean.setGoodsType(recharge.getGoods_type());
        bean.setGoodsExt(recharge.getGoods_ext());
        bean.setGoodsName(recharge.getGoods_name());
        bean.setGoodsCfg(goods_system_cfgid);
        bean.setTotalFee(recharge.getTotal_fee());
        bean.setItemId(recharge.getItem_id());
        bean.setExtParam(JsonUtils.toJSONString(recharge.getExt_param()));
        bean.setSignType(recharge.getSign_type());
        bean.setSign(recharge.getSign());
        bean.setAddTime(TimeUtils.Time());
        bean.setStatus(RechargeDefine.STA_NEW);
        bean.setSrc(src);
        bean.setData(data);
        bean.setMoneyType(recharge.getMoney_type());
        bean.setTrade_no(recharge.getTrade_no());
        bean.setTrade_status(recharge.getTrade_status());
        bean.setNotify_id(recharge.getNotify_id());
        bean.setNotify_time(recharge.getNotify_time());
        // FIXME 如果配置中能找到，则不根据运营平台数据发货
        // FIXME 这里不加倍
        if (itemInfo == null) {
            bean.setGameMoney(recharge.getGame_money());
            bean.setTotalRecharge(recharge.getTotalRecharge());
            bean.setTotalVipPower(recharge.getTotalVipPower());
        } else {
            bean.setGameMoney(itemInfo.getIsTotalRecharge());
            bean.setTotalRecharge(itemInfo.getIsTotalRecharge());
            bean.setTotalVipPower(itemInfo.getTotalVipPower());
        }
        return bean;
    }

    private int gold(RechargeItemInfo cfg) {
        if (cfg == null)
            return 0;

        int goldCount = 0;
        for (ReadArray<Integer> array : cfg.getGoods_reward().getValuees()) {
            if (array.size() != 4) continue;
            if (ItemCoinType.GemCoin == array.get(0)) goldCount += array.get(1);
        }
        return goldCount;
    }

    /**
     * add order
     *
     * @param bean
     */
    @Override
    public void addOrder(RechargeBean bean) {
        Manager.rechargeManager.getRechargeMap().put(bean.getOrderNo(), bean);

        List<String> list = null;
        if (Manager.rechargeManager.getPlayerRechargeMap().get(bean.getRoleId()) == null) {
            list = new ArrayList<>();
            list.add(bean.getOrderNo());
            Manager.rechargeManager.getPlayerRechargeMap().put(bean.getRoleId(), list);
        } else {
            list = Manager.rechargeManager.getPlayerRechargeMap().get(bean.getRoleId());
            if (!list.contains(bean.getOrderNo()))
                list.add(bean.getOrderNo());
        }
    }

    /**
     * 充值的总数量
     *
     * @param player
     * @return
     */
    @Override
    public long rechargeAll(Player player) {
        return Manager.countManager.getVariant(player, VariantType.RechargeGold);
    }

    /**
     * 今日充值的数量
     *
     * @param player
     * @return
     */
    @Override
    public long rechargeDay(Player player) {
        return Manager.countManager.getCount(player, BaseCountType.Recharge, 0);
    }

    private void accumulativeDayRecharge(Player player, int value) {
        Manager.countManager.addVariant(player, VariantType.RechargeGold, value);
        Manager.countManager.addCount(player, BaseCountType.Recharge, 0, Count.RefreshType.CountType_Day, value);
    }

    private void accumulativeRecharge(Player player, int value) {
        Manager.countManager.addVariant(player, VariantType.RechargeMoney, value);
        VipMessage.ResVipRechageMoney.Builder builder = VipMessage.ResVipRechageMoney.newBuilder();
        builder.setMoney(Manager.countManager.getVariant(player, VariantType.RechargeMoney));
        MessageUtils.send_to_player(player, VipMessage.ResVipRechageMoney.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * send recharge all gold to client
     *
     * @param player
     */
    private void sendRechargeAllData(Player player) {
        //发送玩家的最新的总充值数值
        RechargeMessage.ResRechargeTotalValue.Builder msg = RechargeMessage.ResRechargeTotalValue.newBuilder();
        msg.setGoldTotal((int) rechargeAll(player));
        MessageUtils.send_to_player(player, RechargeMessage.ResRechargeTotalValue.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 上线充值处理
     *
     * @param player 玩家
     */
    @Override
    public void onPlayerOnline(Player player) {
        // check recharge
        List<RechargeBean> list = Manager.rechargeManager.getRechargeList(player.getId());
        for (RechargeBean bean : list) {
            if (!bean.getStatus().equals(RechargeDefine.STA_NEW))
                continue;

            toReward(bean.getOrderNo());
        }


        //todo  玩家上线不在发送充值商品
        //sendRechargeItems(player);

        // 发送总充值金额
        sendRechargeAllData(player);
        syncRechargeData(player);
    }

    /**
     * 获取第三方充值的信息
     */
    public String getThirdPayItemInfo(Player player) {
        int  count    = 0;
        long remainTime = 0;
        HashMap<Integer,HashMap<Integer,Long>> itemMap = new HashMap<>();
        HashMap<Integer,Long> itemInfos;
        for (RechargeItemInfo bean :Manager.rechargeManager.getRechargeItemInfoMap().values()) {
            if (canReward(player,bean)){
                if (bean.getGoods_type() == RechargeDefine.FALLSKY_TYPE_ACTIVITY ||
                        bean.getGoods_type() == RechargeDefine.RECHARGE_TYPE_MYSTERY_SHOP ||
                        bean.getGoods_type() == RechargeDefine.RECHARGE_TYPE_FREE_SHOP||
                        bean.getGoods_type() == RechargeDefine.RECHARGE_TYPE_ACTIVITY){
                    continue;
                }
                count =  getCount(player,bean);
               switch (bean.getGoods_type()){
                   case RechargeDefine.RECHARGE_TYPE_NORMAL:
                   case RechargeDefine.RECHARGE_TYPE_CRAZYSAT:
                   case RechargeDefine.RECHARGE_TYPE_DAY_GIFT:
                   case RechargeDefine.RECHARGE_TYPE_GROWTH_FUND:
                   case RechargeDefine.RECHARGE_TYPE_THIRDPAY:
                   case RechargeDefine.RECHARGE_TYPE_INVESTPEAK:
                   case RechargeDefine.RECHARGE_TYPE_DAY_ONEKEY_GIFT:
                   case RechargeDefine.RECHARGE_TYPE_FUNCTION_TASK:
                       remainTime = 0;
                       break;
                   case RechargeDefine.RECHARGE_TYPE_WEEK_CARD:
                   case RechargeDefine.RECHARGE_TYPE_MONTH_CARD:
                   case RechargeDefine.RECHARGE_TYPE_EXCLUSIVE_CARD:
                       remainTime = ((IExclusiveCardScript) Manager.welfareManager.
                               getScript(WelfareMessage.WelfareType.ExclusiveCard)).getEndTime(player,bean.getGoods_id());
                       break;
                   case RechargeDefine.RECHARGE_TYPE_DISCOUNT:
                       remainTime =  Manager.rechargeManager.discountScript().getRemainTime(player,bean.getGoods_system_cfg_id());
                       break;
               }
                itemInfos = new HashMap<>();
                if (remainTime < 0){
                    remainTime = 0;
                }
                itemInfos.put(count,remainTime);
                itemMap.put(bean.getGoods_id(),itemInfos);
            }
        }
        return  JsonUtils.toJSONString(itemMap);
    }

    /**
     * to reward
     *
     * @param order_id
     */
    private void toReward(String order_id) {
        RechargeRewardHandler handle = new RechargeRewardHandler(order_id);
        Manager.rechargeManager.addCommand(handle);
    }

    /**
     * 能否发奖励
     * @param player
     * @param bean
     * @return
     */
    @Override
    public boolean canReward(Player player, RechargeItemInfo bean) {
        IRechargeReward reward = null;
        switch (bean.getGoods_type()) {
            case RechargeDefine.RECHARGE_TYPE_NORMAL:
            case RechargeDefine.RECHARGE_TYPE_CRAZYSAT:
                return canReward(player, bean.getGoods_id());
            case RechargeDefine.RECHARGE_TYPE_DAY_GIFT:
            case RechargeDefine.RECHARGE_TYPE_DAY_ONEKEY_GIFT:
                reward = Manager.rechargeManager.recharge(ScriptEnum.DayGiftBaseScript);
                if (reward == null) return false;
                return reward.canReward(player, bean.getGoods_system_cfg_id());
            case RechargeDefine.RECHARGE_TYPE_WEEK_CARD:
            case RechargeDefine.RECHARGE_TYPE_MONTH_CARD:
            case RechargeDefine.RECHARGE_TYPE_EXCLUSIVE_CARD:
                //紧急热更新，添加两个周卡 策划喊写死
            case 91:
            case 92:
                reward = Manager.rechargeManager.recharge(ScriptEnum.ExclusiveCardBaseScript);
                if (reward == null) return false;
                return reward.canReward(player, bean.getGoods_id());
            case RechargeDefine.RECHARGE_TYPE_GROWTH_FUND:
                reward = Manager.rechargeManager.recharge(ScriptEnum.GrowthFundBaseScript);
                if (reward == null) return false;
                return reward.canReward(player, bean.getGoods_system_cfg_id());
            case RechargeDefine.RECHARGE_TYPE_MYSTERY_SHOP:
                reward = Manager.rechargeManager.recharge(ScriptEnum.MysteryShopScript);
                if (reward == null) return false;
                return reward.canReward(player, bean.getGoods_system_cfg_id());
            case RechargeDefine.RECHARGE_TYPE_FREE_SHOP:
                reward = Manager.rechargeManager.recharge(ScriptEnum.FreeShopScript);
                if (reward == null) return false;
                return reward.canReward(player, bean.getGoods_system_cfg_id());
            case RechargeDefine.RECHARGE_TYPE_DISCOUNT:
                reward = Manager.rechargeManager.recharge(ScriptEnum.DiscountRechargeScript);
                if (reward == null) return false;
                return reward.canReward(player, bean.getGoods_system_cfg_id());
            case RechargeDefine.RECHARGE_TYPE_ACTIVITY:
                return true;
            case RechargeDefine.RECHARGE_TYPE_THIRDPAY:
            case RechargeDefine.FALLSKY_TYPE_ACTIVITY:
                return true;
            case RechargeDefine.RECHARGE_TYPE_INVESTPEAK:
                reward = Manager.rechargeManager.recharge(ScriptEnum.InvestPeak);
                if (reward == null) return false;
                return reward.canReward(player, bean.getGoods_system_cfg_id());
            case RechargeDefine.RECHARGE_TYPE_FUNCTION_TASK:
                return Manager.functionTaskManager.getScript().canReward(player, bean.getGoods_id());


        }
        return false;
    }

    private void afterReward(Player player, RechargeBean bean, RechargeItemInfo cfg) {
        IRechargeReward reward = null;
        switch (cfg.getGoods_type()) {
            case RechargeDefine.RECHARGE_TYPE_NORMAL:
            case RechargeDefine.RECHARGE_TYPE_CRAZYSAT:
            case RechargeDefine.RECHARGE_TYPE_ACTIVITY:
            case RechargeDefine.RECHARGE_TYPE_THIRDPAY:
                afterReward(player, bean.getOrderNo(), cfg.getGoods_id());
                break;
            case RechargeDefine.RECHARGE_TYPE_DAY_GIFT:
            case RechargeDefine.RECHARGE_TYPE_DAY_ONEKEY_GIFT:
                reward = Manager.rechargeManager.recharge(ScriptEnum.DayGiftBaseScript);
                if (reward == null) return;
                reward.afterReward(player, bean.getOrderNo(), cfg.getGoods_system_cfg_id());
                break;
            case RechargeDefine.RECHARGE_TYPE_WEEK_CARD:
            case RechargeDefine.RECHARGE_TYPE_MONTH_CARD:
            case RechargeDefine.RECHARGE_TYPE_EXCLUSIVE_CARD:
                //紧急热更新，添加两个周卡 策划喊写死
            case 91:
            case 92:
                reward = Manager.rechargeManager.recharge(ScriptEnum.ExclusiveCardBaseScript);
                if (reward == null) return;
                reward.afterReward(player, bean.getOrderNo(), cfg.getGoods_id());
                break;
            case RechargeDefine.RECHARGE_TYPE_GROWTH_FUND:
                reward = Manager.rechargeManager.recharge(ScriptEnum.GrowthFundBaseScript);
                if (reward == null) return;
                reward.afterReward(player, bean.getOrderNo(), cfg.getGoods_system_cfg_id());
                break;
            case RechargeDefine.RECHARGE_TYPE_MYSTERY_SHOP:
                reward = Manager.rechargeManager.recharge(ScriptEnum.MysteryShopScript);
                if (reward == null) return;
                reward.afterReward(player, bean.getOrderNo(), cfg.getGoods_system_cfg_id());
                break;
            case RechargeDefine.RECHARGE_TYPE_FREE_SHOP:
                reward = Manager.rechargeManager.recharge(ScriptEnum.FreeShopScript);
                if (reward == null) return;
                reward.afterReward(player, bean.getOrderNo(), cfg.getGoods_system_cfg_id());
                break;
            case RechargeDefine.RECHARGE_TYPE_DISCOUNT:
                reward = Manager.rechargeManager.recharge(ScriptEnum.DiscountRechargeScript);
                if (reward == null) return;
                reward.afterReward(player, bean.getOrderNo(), cfg.getGoods_system_cfg_id());
                break;
            case RechargeDefine.RECHARGE_TYPE_INVESTPEAK:
                reward = Manager.rechargeManager.recharge(ScriptEnum.InvestPeak);
                if (reward == null) return;
                reward.afterReward(player, bean.getOrderNo(), cfg.getGoods_system_cfg_id());
                break;
            case RechargeDefine.RECHARGE_TYPE_FUNCTION_TASK:
                break;
        }
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

        RechargeItemInfo bean = Manager.rechargeManager.getRechargeItemInfoMap().get(goodId);
        //重置类型 1:不重置,2:新手期,3:周,4:日 ,默认不重置
        int type = getRechargeRefreshType(bean);
        if(type != RechargeDefine.RECHARGE_REFRESH_TYPE_NORMAL ){
            //子类型除了普通,其他的都是在有限的时间内只刷新一次. 充值次数还是根据配置表来控制
            if(bean.getGoods_limit() > 0) {
                return getCount(player, bean) < bean.getGoods_limit();
            }
        }
        return true;
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

    }

    /**
     * 设置充值新手期
     * @param player
     */
    public void setPayNewbieStart(Player player){
        player.setNewbieRechargeTime(TimeUtils.Time());
    }

    /**
     * 充值商品MD5效验
     * @param player
     * @param md5
     */
    @Override
    public void onReqCheckRechargeMd5(Player player, String md5) {
        if (Manager.rechargeManager.getMd5().equalsIgnoreCase(md5)){
            RechargeMessage.ResRechargeItems.Builder msg =  RechargeMessage.ResRechargeItems.newBuilder();
            msg.setRechargeItemJson("");
            msg.setMd5(Manager.rechargeManager.getMd5());
            MessageUtils.send_to_player(player, RechargeMessage.ResRechargeItems.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }
        log.info("从新刷新 充值商品列表  old md5 {}  new {}",  md5,Manager.rechargeManager.getMd5());
        sendRechargeItems(player);
    }
    /**
     * 检测商品是否能购买
     * @param player
     * @param id
     */
    @Override
    public void onReqCheckGoodsIsCanbuy(Player player, int id,String moneyType) {
        RechargeItemInfo rechargeItemInfo = null;
        for (RechargeItemInfo bean : Manager.rechargeManager.getRechargeItemInfoMap().values()){
            if (bean.getGoods_system_cfg_id() == id){
                rechargeItemInfo = bean;
                break;
            }
        }
        if (rechargeItemInfo == null){
            log.error("没有找到该充值商品 id {} ", id);
            sendResCheckGoodsResult(player,id,0,"");
            return;
        }
        int result = canReward(player,rechargeItemInfo) == true?1:0;
        if ( result <= 0){
            log.error("商品不能购买或者购买次数已满 id {} ", id);
            sendResCheckGoodsResult(player,rechargeItemInfo.getGoods_system_cfg_id(),0,"");
            return;
        }
        RechargePlaceOrderHandler orderHandler = new RechargePlaceOrderHandler(player,rechargeItemInfo,moneyType);
        Manager.rechargeManager.addCommand(orderHandler);
    }

   public  void onResRechargePlaceOrder(Player player,RechargeItemInfo rechargeItemInfo,String moneyType){

       HashMap<String, String> param =buildOrderInfo(player,rechargeItemInfo, moneyType);
       StringBuilder resultJson = new StringBuilder();
       String parstring = buildMd5String(param);
       log.info("服务器下单信息 send to X8 info {} ", parstring);
       int code  = HttpUtils.sendPost(ServerConfig.getServerOrderUrl(),parstring,resultJson);
       String resultStr = resultJson.toString();
       log.info("服务器接受到X8返回信息 code {} result {} ", code,resultStr);
       ServerOrderData orderData  = JsonUtils.parseObject(resultStr,ServerOrderData.class);
       if (orderData == null){
           log.error("下单失败  : {}",resultStr);
           sendResCheckGoodsResult(player,rechargeItemInfo.getGoods_system_cfg_id(),0,"");
           return;
       }
       if (orderData.getState() != 1){
           log.error("下单失败  : {}",resultStr);
           sendResCheckGoodsResult(player,rechargeItemInfo.getGoods_system_cfg_id(),0,"");
           return;
       }
       orderData.setGoodsId(rechargeItemInfo.getGoods_id());
       orderData.setRoleId(player.getId());
       orderData.setMoneyType(moneyType);
       String order_no = orderData.getData().get("order_no");
       if (Manager.rechargeManager.getServerOrderMap().containsKey(order_no)){
           log.error("订单重复  : {}",order_no);
           sendResCheckGoodsResult(player,rechargeItemInfo.getGoods_system_cfg_id(),0,"");
           return;
       }
       Manager.rechargeManager.getServerOrderMap().put(order_no,orderData);
       sendResCheckGoodsResult(player,rechargeItemInfo.getGoods_system_cfg_id(),orderData.getState(),resultStr);

   }
    private String buildMd5String(HashMap<String,String> paramer){
        List<String> keyList = new ArrayList<>();
        for(String key : paramer.keySet()){
            keyList.add(key);
        }
        keyList.sort(null);
        StringBuilder sb = new StringBuilder();
        //排序
        for(int i = 0;i<keyList.size();i++){
            if(i == 0){
                sb.append(keyList.get(i)+"=").append(paramer.get(keyList.get(i)));
            }else {
                sb.append("&"+keyList.get(i)+"=").append(paramer.get(keyList.get(i)));
            }
        }
//        sb.append("&secretkey=").append(RechargeItemManager.getInstance().getSecretkey());
       // sb.append(GameInfoManager.getInstance().getGameInfo().getRechargeSecretkey());
        return sb.toString();
    }
   private void sendResCheckGoodsResult(Player player,int id,int result,String resultJson){
       RechargeMessage.ResCheckGoodsResult.Builder msg = RechargeMessage.ResCheckGoodsResult.newBuilder();
       msg.setId(id);
       msg.setResult(result);
       msg.setOrderInfo(resultJson);
       MessageUtils.send_to_player(player, RechargeMessage.ResCheckGoodsResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());

   }

    /**
     * 发送所有商品
     * @param player
     */
    public void sendRechargeItems(Player player){
        RechargeMessage.ResRechargeItems.Builder msg =  RechargeMessage.ResRechargeItems.newBuilder();
        msg.setRechargeItemJson(Manager.rechargeManager.getRechargeItemJson());
        msg.setMd5(Manager.rechargeManager.getMd5());
        MessageUtils.send_to_player(player, RechargeMessage.ResRechargeItems.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        log.error("玩家登录上限发送充值数据  成功 playerid {} Md5 {} ",player.getId(),Manager.rechargeManager.getMd5());
    }


    /**
     * 开服充值异常检测
     */
    public void rchargeCheckException() {
        if (ServerParamUtil.isRechargeCheck <= 0) {
            long curTime = TimeUtils.Time();
            long openServerTime = TimeUtils.getOpenServerTime();
            long rechargeCheckTime =openServerTime +  ServerConfig.getCheckRechargeTime() * 60 * 1000;
            if (curTime >= rechargeCheckTime) {
                if (Manager.rechargeManager.getRechargeMap().size() <= 0) {
                    //此处报警
                    log.error(BeanUtil.customThrowException("RchargeCheckException"));
                } else {
                    boolean iskException = true;
                    for (RechargeBean rechargeBean : Manager.rechargeManager.getRechargeMap().values()){
                        if (rechargeBean.getAddTime() > openServerTime && rechargeBean.getAddTime() < rechargeCheckTime){
                            iskException = false;
                            break;
                        }
                    }
                    if (iskException){
                        //此处报警
                        log.error(BeanUtil.customThrowException("RchargeCheckException"));
                    }
                }
                ServerParamUtil.saveRchargeCheck();
            }
        }
    }

    private void saveLog(Recharge recharge, String data, Byte src, int statusReason) {
        RechargeLog log = new RechargeLog();
        log.setOrderNo(recharge.getOrder_no());
        log.setUserId(Manager.playerManager.getPlayerWorldInfo(recharge.getRole_id()).getUserId());
        log.setRoleId(recharge.getRole_id());
        log.setRoleName(Manager.playerManager.getPlayerWorldInfo(recharge.getRole_id()).getRolename());
        log.setPlatform(Manager.playerManager.getPlatformName(recharge.getRole_id()));
        log.setCreateSid(ServerConfig.getServerId());
        log.setGoodsId(recharge.getGoods_id());
        log.setGoodsType(recharge.getGoods_type());
        log.setGoodsExt(recharge.getGoods_ext());
        log.setGoodsName(recharge.getGoods_name());
        RechargeItemInfo rechargeItemInfo = Manager.rechargeManager.getRechargeItemInfoMap().get(recharge.getGoods_id());
        log.setGoodsCfg(rechargeItemInfo.getGoods_system_cfg_id()+"");
        log.setTotalFee(recharge.getTotal_fee());
        log.setItemId(recharge.getItem_id());
        log.setGameMoney(recharge.getGame_money());
        log.setExtParam(recharge.getExt_param());
        log.setSignType(recharge.getSign_type());
        log.setSign(recharge.getSign());
        log.setAddTime(TimeUtils.Time());
        log.setStatus(RechargeDefine.STA_ERROR);
        log.setSrc(src);
        log.setData(data);
        log.setStatusReason(statusReason);
        log.setMoneyType(recharge.getMoney_type());
        log.setTrade_no(recharge.getTrade_no());
        log.setTrade_status(recharge.getTrade_status());
        log.setNotify_id(recharge.getNotify_id());
        log.setNotify_time(recharge.getNotify_time());
        LogService.getInstance().execute(log);
    }

    private void saveLog(Player player, RechargeBean bean, int statusReason) {
        RechargeLog log = new RechargeLog();
        log.setPlayer(player);
        log.setOrderNo(bean.getOrderNo());
        log.setCreateSid(bean.getSrvId());
        log.setGoodsId(bean.getGoodsCfgId());
        log.setGoodsType(bean.getGoodsType());
        log.setGoodsExt(bean.getGoodsExt());
        log.setGoodsName(bean.getGoodsName());
        log.setGoodsCfg(bean.getGoodsCfg());
        log.setTotalFee(bean.getTotalFee());
        log.setItemId(bean.getItemId());
        log.setGameMoney(bean.getGameMoney());
        log.setExtParam(bean.getExtParam());
        log.setSignType(bean.getSignType());
        log.setSign(bean.getSign());
        log.setAddTime(bean.getAddTime());
        log.setStatus(bean.getStatus());
        log.setSrc(bean.getSrc());
        log.setData(bean.getData());
        log.setStatusReason(statusReason);
        log.setMoneyType(bean.getMoneyType());
        log.setTrade_no(bean.getTrade_no());
        log.setTrade_status(bean.getTrade_status());
        log.setNotify_id(bean.getNotify_id());
        log.setNotify_time(bean.getNotify_time());
        LogService.getInstance().execute(log);
    }

    private void saveLog(RechargeBean bean, int statusReason) {
        RechargeLog log = new RechargeLog();
        log.setUserId(bean.getUserId());
        log.setRoleId(bean.getRoleId());
        log.setRoleName(Manager.playerManager.getPlayerWorldInfo(bean.getRoleId()).getRolename());
        log.setPlatform(Manager.playerManager.getPlatformName(bean.getRoleId()));
//        log.setCreateSid(ServerConfig.getServerId());
        log.setOrderNo(bean.getOrderNo());
        log.setCreateSid(bean.getSrvId());
        log.setGoodsId(bean.getGoodsCfgId());
        log.setGoodsType(bean.getGoodsType());
        log.setGoodsExt(bean.getGoodsExt());
        log.setGoodsName(bean.getGoodsName());
        log.setGoodsCfg(bean.getGoodsCfg());
        log.setTotalFee(bean.getTotalFee());
        log.setItemId(bean.getItemId());
        log.setGameMoney(bean.getGameMoney());
        log.setExtParam(bean.getExtParam());
        log.setSignType(bean.getSignType());
        log.setSign(bean.getSign());
        log.setAddTime(bean.getAddTime());
        log.setStatus(bean.getStatus());
        log.setSrc(bean.getSrc());
        log.setData(bean.getData());
        log.setStatusReason(statusReason);
        log.setMoneyType(bean.getMoneyType());
        log.setTrade_no(bean.getTrade_no());
        log.setTrade_status(bean.getTrade_status());
        log.setNotify_id(bean.getNotify_id());
        log.setNotify_time(bean.getNotify_time());
        LogService.getInstance().execute(log);
    }

    //TODO 不同机型获取对应的充值货币
    public int getMoney(Player player,RechargeItemInfo bean,String moneyType){
        if (player.getOs().contains("ios") ){
            return  Integer.parseInt(bean.getGoods_price().get("ios").get(moneyType));
        }
        log.error(bean.getGoods_price()+"::::"+moneyType);
        return  Integer.parseInt( bean.getGoods_price().get("android").get(moneyType));
    }
    public String getPrice_point(Player player,RechargeItemInfo rechargeItemInfo){
        if (player.getOs().contains("ios") ){
            return  rechargeItemInfo.getGoods_price_point().get("ios");
        }
        return  rechargeItemInfo.getGoods_price_point().get("android");
    }

    private HashMap<String,String> buildOrderInfo(Player player,RechargeItemInfo rechargeItemInfo,String moneyType){
        HashMap<String, String> param = new HashMap<>();
        UserInfo userInfo = Manager.registerManager.getUserInfo(player.getUserId());
        userInfo.getExtension().get(QQConfig.Fgi.getKey());
        param.put("user_id",player.getUuid());
        param.put("goods_id",rechargeItemInfo.getGoods_id()+"");
        param.put("goods_code",rechargeItemInfo.getGoods_system_cfg_id()+"");
        param.put("goods_name",rechargeItemInfo.getGoods_name());
        param.put("goods_desc",rechargeItemInfo.getGoods_name());
        int total_fee = getMoney(player,rechargeItemInfo,moneyType);
        param.put("total_fee",total_fee+"");
        param.put("role_id",player.getId()+"");
        param.put("role_name",player.getName());
        param.put("server_id",ServerConfig.getServerId()+"");
        param.put("server_name",ServerConfig.getServerName());
        param.put("vip_level",player.getVipLv()+"");
        String extension =  makeQQExtension(player,rechargeItemInfo,total_fee);
        log.info("buildOrderInfo Extension :{}" , extension);
        param.put("extension",extension);
        param.put("notify_url",ServerConfig.getRechargeNotifyUrl());
        param.put("client_ip",player.getLoginIP());
        param.put("goods_price_point",getPrice_point(player,rechargeItemInfo));
        param.put("fee_type",moneyType);
        param.put("sign_type","md5");
        String sign = Recharge.sign(param);
        param.put("sign",sign);
        return param;
    }

    private String makeQQExtension(Player player,RechargeItemInfo rechargeItemInfo,int total_fee){
        UserInfo userInfo = Manager.registerManager.getUserInfo(player.getUserId());
        String value =  userInfo.getExtension().get(QQConfig.Fgi.getKey());
        log.info("Fgi  value :{}" ,value);
        Cfg_Sdkplatform_Bean bean = CfgManager.getCfg_Sdkplatform_Container().getValueByKey(Integer.parseInt(value));
        if (bean == null){
            log.info("Cfg_Sdkplatform_Bean  is null :{}",value);
            return "";
        }
        log.info("Chanel  :{}",bean.getChanel());
        if (!bean.getChanel().equalsIgnoreCase("QQ")){
            return "";
        }
        HashMap<String,String> param = new HashMap<>();
        String payitem = rechargeItemInfo.getGoods_id() +"*"+(total_fee/10)+"*"+1;

        param.put("openid",userInfo.getExtension().get(QQConfig.OpenId.getKey()));
        param.put("openkey",userInfo.getExtension().get(QQConfig.OpenKey.getKey()));
        param.put("pf",userInfo.getExtension().get(QQConfig.Pf.getKey()));
        param.put("pfkey",userInfo.getExtension().get(QQConfig.PfKey.getKey()));
        param.put("ts", TimeUtils.TimeSec()+"");
        param.put("payitem", payitem);
        param.put("goodsmeta", rechargeItemInfo.getGoods_name()+"*"+"商品描述");
        param.put("goodsurl", rechargeItemInfo.getGoodsurl());
        param.put("zoneid", userInfo.getExtension().get(QQConfig.Zoneid.getKey()));
        return  JsonUtils.toJSONString(param);
    }
}
