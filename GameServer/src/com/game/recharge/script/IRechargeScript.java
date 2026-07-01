package com.game.recharge.script;


import com.game.db.bean.RechargeBean;
import com.game.player.structs.Player;
import com.game.recharge.structs.Recharge;
import com.game.recharge.structs.RechargeItemInfo;

/**
 * @explain: 充值接口，大于对外接口，小于对内接口
 * @time Created on 2019/11/21 15:32.
 * @author: tc
 */
public interface IRechargeScript {
    /**
     * 充值来了
     * @param recharge
     * @param data
     * @param src
     * @return
     */
    int Recharge(Recharge recharge, String data, Byte src);

    /**
     * 能否发奖励
     * @param player
     * @param bean
     * @return
     */
    boolean canReward(Player player, RechargeItemInfo bean);

    /**
     * 发货
     * @param order_id
     */
    void Reward(String order_id);


    /**
     * add order
     * @param bean
     */
    void addOrder(RechargeBean bean);

    /**
     * 上线充值处理
     *
     * @param player 玩家
     */
    void onPlayerOnline(Player player);

    /**
     * 内部充值
     * @param player
     * @param goodId
     */
    void onReqRecharge(Player player, int goodId);

    /**
     * 请求充值数据
     * @param player
     */
    void onReqRechargeData(Player player);

    /**
     * 充值的总数量
     * @param player
     * @return
     */
    long rechargeAll(Player player);

    /**
     * 今日充值的数量
     * @param player
     * @return
     */
    long rechargeDay(Player player);

    /**
     * 设置充值新手期
     * @param player
     */
    void setPayNewbieStart(Player player);


    /**
     * 获取对应礼包充值次数
     * @param player
     * @param goodsId
     * @return
     */
    int getRechargeNum(Player player,int goodsId);


    /**
     *充值XX充值礼包YY次（XX对应rechargeItem表的rechargeSubType字段
     * @param player
     * @param actType
     * @return
     */
    int getRechargeNumForType(Player player,int actType);


    /**
     * 充值商品MD5效验
     * @param player
     * @param md5
     */
    void onReqCheckRechargeMd5(Player player,String md5);


    /**
     * 检测商品是否能购买
     * @param player
     * @param id
     */
    void onReqCheckGoodsIsCanbuy(Player player,int id,String moneyType);


    /**
     * 服务器下单
     * @param player
     * @param rechargeItemInfo
     */
    void onResRechargePlaceOrder(Player player,RechargeItemInfo rechargeItemInfo,String moneyType);


    /**
     * 发送所有商品
     * @param player
     */
    void sendRechargeItems(Player player);


    /**
     * 根据机型获取货币
     * @param player
     * @param bean
     * @return
     */
    int getMoney(Player player,RechargeItemInfo bean,String moneyType);


    /**
     * 开服充值异常检测
     */
     void rchargeCheckException();


    /**
     * 获取第三方充值的信息
     */
    String getThirdPayItemInfo(Player player);

}
