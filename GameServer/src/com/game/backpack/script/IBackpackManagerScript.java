package com.game.backpack.script;

import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Equip;
import com.game.backpack.structs.Gift;
import com.game.backpack.structs.Item;
import com.game.player.structs.Player;
import com.game.structs.ItemChangeAction;
import game.core.script.IScript;
import game.message.backpackMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 背包公共接口管理
 */
public interface IBackpackManagerScript extends IScript {
    /**
     * 移除指定格子中的物品
     * @param player 玩家
     * @param cellId 格子id
     * @param reasons 改变原因
     * @param action 关联id
     * @return boolean
     */
    boolean removeItemByCellId(Player player, int cellId, int reasons, long action);
    /**
     * 判断能否扣除物品数量,包括货币
     * @param player 玩家
     * @param items 物品
     * @param actionId 日志id
     * @param reason 原因
     * @return 结果
     */
    boolean removeItemOrCurrencies(Player player, ReadIntegerArrayEs items, long actionId, int reason);
    /**
     * 扣除物品数量,包括货币
     * @param player 玩家
     * @param itemModelId 物品id
     * @param  num 物品数量
     * @param actionId 日志id
     * @param reason 原因
     * @return 结果
     */
    boolean removeItemOrCurrency(Player player, int itemModelId, int num, long actionId, int reason);

    boolean onRemoveItem(Player player, int itemModelId, int num, int reasons, long action);

    boolean onRemoveItem(Player player, int itemModelId, int num, boolean bind, int reasons, long action);

    boolean onRemoveItem(Player player, Item item, int num, int reasons, long action);
    /**
     * 移除物品 不通知前端 最后统一处理
     * @param deleteList 删除列表
     * @param changeList 该表列表
     * @return
     */
    boolean onRemoveItemNotNoticeClinet(Player player, Item item, int num, int reasons, long action, Set<Long> deleteList, Set<Long> changeList);
    /**
     * 移除物品 不通知前端
     */
    boolean onRemoveItemByItemNotNotice(Player player, Item item, int reasons, long action);

    /**
     * 是否有增加一个物品的格子
     */
    boolean onHasAddSpace(Player player, Item item);
    /**
     * 是否有增加多个物品的格子
     */
    int onHasAddSpaces(Player player, Collection<Item> items);

    boolean addItem(Player player, int itemID, int num, boolean isBind, long lostTime, int reason, long action);

    /**
     * 增加物品 非货币消耗
     * @param player 玩家
     * @param item 物品
     * @param reason 原因
     * @param action 日志id
     * @return 结果
     */
    boolean addItem(Player player, Item item, int reason, long action);

    boolean addItemHaveMergeFlag(Player player, Item item, int reason, long action, int costType, float costNum, boolean needMergeFlag);

    /**
     * 增加物品
     */
    boolean onAddItem(Player player, Item item, int reason, long action, int costType, float costNum, boolean needMergeFlag);

    /**
     * 批量添加 货币消耗
     * @param player   玩家
     * @param items    物品
     * @param reasons  原因
     * @param action   日志id
     * @param costType 消耗货币类型
     * @param costNum  消耗数量
     * @return 结果
     */
    boolean addItems(Player player, List<Item> items, int reasons, long action, int costType, float costNum);

    boolean addItems(Player player, List<Item> items, int reasons, long action);

    /**
     * 添加或邮件发送道具
     * @param player
     * @param items
     * @param reasons
     * @param action
     * @return
     */
    void addOrSendItems(Player player, List<Item> items, int reasons, long action);

    /**
     * 使用物品
     */
    boolean useItem(Player player, long itemId, int num, int index);
    /**
     * 物品的作用效果列表，返回null表示作用效果失败不能，不能使用物品
     */
    void doEffects(Player player, Item item, int num, long actionId);
    /**
     * 计算世界等级倍率
     */
    int calWorldLvRate(Player player);
    /**
     * 计算战力放大倍率
     */
    @Deprecated
    int calFightPointRate(Player player);
    /**
     * 计算组队经验倍率
     */
    int calTeamRate(Player player);
    /**
     * 结婚组队经验加成
     * @param player
     * @return
     */
    int calMarriageRate(Player player);

    /**
     * 清理背包
     */
    void bagClearUp(Player player, boolean isgm);
    /**
     * 礼包卡使用
     */
    boolean useGift(Player player, int useNum, int index, long actionId, Gift model);
    /**
     * 验证两物品合并用
     */
    boolean isMergeAble(Item item1, Item item2);

    /**
     * 验证物品是否可以添加
     */
    boolean canUse(Player player, Item item, int usenum);

    /**
     * 成功开启格子
     * @param player     角色
     * @param cellId     开启的格子
     * @param actionType 1 元宝开启 0时间开启
     * @param costGold   扣除的元宝数量
     * @param actionId   日志id
     */
    void openBagCellSuccess(Player player, int cellId, byte actionType, int costGold, long actionId);
    /**
     * 获取物品发到聊天框的信息
     */
    String getChatInfo(Item item);
    /**
     * 获取指定绑定属性的数量
     * @param player      玩家
     * @param itemModelId 物品配置表id
     * @param bind        绑定状态
     * @return 结果
     */
    int getItemNum(Player player, int itemModelId, boolean bind);
    /**
     * 物品排序
     */
    void sortItems(ConcurrentHashMap<Integer, Item> container, List<Equip> equips, List<Item> items, int gender);
    /**
     * 获取物品或者装备(聊天环境,带类型分割符号)
     */
    String getName(int model);
    /**
     * 获取物品或装备名（非聊天环境）
     * @param model 模型id
     * @return 名字
     */
    String getItemName(int model);

    /**
     * 获得剩余格子数量
     * @param player 玩家
     * @return 剩余格子数量
     */
    int getEmptyGridNum(Player player);
    /**
     * 获得背包中物品
     * @param player 玩家
     * @param cellId 格子id
     * @return item
     */
    Item getItemByCellId(Player player, int cellId);
    /**
     * 获得背包中物品
     * @param player 玩家
     * @param itemId 物品id
     * @return item
     */
    Item getItemById(Player player, long itemId);
    /**
     * 通过modelId获得背包中物品 注意：只适用于背包中只能存在唯一一个的物品，多个相同物品返回的id不确定
     * @param player 玩家
     * @param modelId 配置表id
     * @return item
     */
    Item getItemByModelId(Player player, int modelId);
    /**
     * 获取物品 版doing
     * @param player 玩家
     * @param modelId 配置表id
     * @return item
     */
    Item getItemByModelIdIsBind(Player player, int modelId, boolean bind);
    /**
     * 获得物品数量
     * @param player 玩家
     * @param itemModelId 物品模板id
     * @return 物品数量
     */
    int getItemNum(Player player, int itemModelId);
    /**
     * 判断能否扣除物品数量,包括货币
     * @param player 玩家
     * @param arrays 物品
     * @param rate 倍率
     * @return 结果
     */
    boolean canDeleteItemNum(Player player, ReadIntegerArrayEs arrays, int rate);
    /**
     * 判断能否扣除多个物品数量,包括货币
     * @param player 玩家
     * @param itemlist 物品配置表id integers[0]为物品配置表id,integers[1]为扣除数量
     * @return 结果
     */
    boolean canDeleteItemNumList(Player player, ArrayList<Integer[]> itemlist);
    /**
     * 判断能否扣除物品数量,包括货币
     * @param player 玩家
     * @param itemModelId 物品配置表id
     * @param num 扣除物品数量
     * @return 结果
     */
    boolean canDeleteItemNum(Player player, int itemModelId, int num);

    /**
     * 生成客户端物品信息
     * @param item 物品
     * @return 协议
     */
    backpackMessage.ItemInfo.Builder buildItemInfo(Item item);

    List<backpackMessage.ItemInfo> buildItemInfo(List<Item> items);

    backpackMessage.Key_Value.Builder buildKeyValue(int key, int value);

    /**
     * 上线或者整理背包时，对重新排序后的物品的处理
     * 1、有需要的话，调整物品在背包的位置
     */
    backpackMessage.ItemInfo.Builder addItemInfoToList(Integer index, Item item, ConcurrentHashMap<Integer, Item> backpack, Player player);

    /**
     * 返回物品合并后的列表
     */
    List<List<Integer>> getAfterMergeItemList(List<List<Integer>> itemList);

    /**
     * 发送背包物品消息
     * @param player 玩家
     */
    void sendItemInfos(Player player);
    /**
     * 发送物品增加消息
     * @param player 玩家
     * @param reason 原因
     * @param item 物品
     */
    void sendItemAdd(Player player, int reason, Item item);
    /**
     * 发送物品改变消息
     * @param player 玩家
     * @param reason 原因码
     * @param item 物品
     */
    void sendItemChange(Player player, int reason, Item item);
    /**
     * 发送物品删除消息
     * @param player 玩家
     * @param reason 原因码
     * @param itemId 物品id
     */
    void sendItemDelete(Player player, int reason, long itemId);
    /**
     * 发送物品删除消息
     * @param player 玩家
     * @param reason 原因码
     * @param itemIds 物品id
     */
    void sendItemDeleteList(Player player, int reason, Set<Long> itemIds);
    /**
     * 发送物品不足消息
     */
    void sendItemNotEnough(Player player, int itemModelId);

    /**
     * 写道具日志
     * @param player 玩家数量
     * @param itemId 物品实例id
     * @param modelId 模型id
     * @param oldNum 变化之前的物品数量
     * @param afterNum 变化后的数量
     * @param reason 变更原因
     * @param actionId 事件ID
     * @param changeAction 变更类型
     * @param coinType 消耗货币类型
     * @param costNum 消耗货币数量
     * @param cellId 当前格子位置
     */
    void writeItemLog(Player player, long itemId, int modelId, int oldNum, int afterNum, int reason, ItemChangeAction changeAction, long actionId, int coinType, float costNum, int cellId);

    void pushCollectLog(Player player, int modelId, long num);

    public void writeItemLogAndBI(Player player, int oldNum, int afterNum, Item item, int reason, long actionId);
}
