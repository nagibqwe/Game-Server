package com.game.backpack.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

/**
 * 货币公共接口管理
 */
public interface ICurrencyManagerScript extends IScript {

    /**
     * 是否是货币
     * @param itemModelId 配置表id
     * @return
     */
     boolean isCoinItem(int itemModelId);

    /**
     * 获取货币的上线
     * @param currencyType
     * @return
     */
    long getMaxCurrencyLimit(int currencyType);

    /**
     * 判断元宝和绑定元宝是否能扣除
     * @param player
     * @param num 扣除数量
     * @return
     */
    boolean canDecBindGoldOrGold(Player player, long num);

    /**
     * 是否可以减少元宝
     *
     * @param player
     * @param gold 正数负数都行 都是扣
     * @return
     */
    boolean canRemoveGold(Player player, long gold);

    /**
     * 是否可以减少货币(非元宝，经验)
     *
     * @param player
     * @param currencyType 货币类型
     * @param num 正数负数都行 都是扣
     * @return
     */
    boolean canRemoveCurrency(Player player, int currencyType, long num);

    /**
     * 判断是否可以扣除货币
     *
     * @param player
     * @param num 改变数量>0
     * @return
     */
    boolean canDecItemCoin(Player player, long num, int itemModleId);

    /**
     * 是否可以增加元宝
     *
     * @param player
     * @param gold
     * @return
     */
    boolean canAddGold(Player player, long gold);

    boolean canAddCurrency(Player player, int currencyType, long num);

    /**
     * 增加玩家经验,直接增加最终经验值，不计算加成
     *
     * @param player    玩家
     * @param num       经验值
     * @param reasons   原因码
     * @param action    行为id
     */
    boolean addEXP(Player player, long num, int reasons, long action);

    /**
     * 玩家增加最终经验,直接增加到玩家身上，不计算加成
     * @param num      经验值
     * @param reasons  原因吗
     * @param actionId 行为id
     */
    boolean onChangeFinalExp(Player player, long num, int reasons, long actionId);

    /**
     * 元宝的添加与删除
     */
    boolean changeGold(Player player, long before, long num, long after, int reasons, long action);

    /**
     * 货币增加完后的另外事务处理
     */
    void coinChangeEvent(Player player, int type, long before, long num, long after, int reasons, long action);

    /**
     * 元宝增加的接口函数
     */
    boolean onAddGold(Player player, int gold, int itemChangeReason, long actionId);

    /**
     * 物品的添加
     */
    boolean onAddItemCoin(Player player, int itemModelId, long num, int reson, long action);

    /**
     * 统一规则 扣取规则是绑定优先，绑定不足非绑定补足
     * @param player
     * @param num 改变数量>0
     * @param reasons 改变原因
     * @param action 关联id
     * @return
     */
    boolean decBindGoldOrGold(Player player, int num, int reasons, long action);

    /**
     * 扣除元宝的操作
     */
    boolean decGold(Player player, int gold, int reasons, long action);

    /**
     * 扣除货币操作
     * @param player
     * @param num
     * @param reason
     * @param action
     * @param itemModelId
     * @return
     */
    boolean onDecItemCoin(Player player, long num, int reason, long action, int itemModelId);

    /**
     * 获取玩家指定货币的数量
     * @return 返回货币的数量值
     */
    long getCurrencyNum(Player player, int currencyType);

    /**
     * 获取玩家指定货币的数量(不超过整型上限)
     * @return 返回货币的数量值
     */
    int getCurrencyIntNum(Player player, int currencyType);

    /**
     * 指定发送某个货币的变化值到客户端
     */
    void sendItemCoinChange(Player player, int reason, int type, long v);

    /**
     * 发送玩家货币消息
     */
    void sendItemCoinInfos(Player player);

    /**
     * 发送玩家经验变化消息
     * @param player 玩家
     * @param reason 理由
     * @param changeValue 变化量
     * @param value 最后值
     */
    void sendExpChange(Player player, int reason, long value, long changeValue);

    void writeLog(Player player, long beforeNum, long afterNum, long changeNum, long otherNum, int value, long action, int currencyType);
}
