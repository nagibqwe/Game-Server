package common.backpack;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Bag_grid_Bean;
import com.data.bean.Cfg_Buff_Bean;
import com.data.bean.Cfg_Characters_Bean;
import com.data.bean.Cfg_Item_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.script.IBackpackScript;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.backpack.structs.ItemEffectType;
import com.game.backpack.structs.ItemTypeConst;
import com.game.buff.structs.Buff;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.SavePlayerLevel;
import com.game.script.structs.ScriptEnum;
import com.game.structs.ItemChangeAction;
import com.game.structs.ServerStr;
import com.game.task.structs.TaskHelp;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.message.backpackMessage;
import game.message.backpackMessage.ReqBagClearUp;
import game.message.backpackMessage.ReqCompound;
import game.message.backpackMessage.ReqMoveItem;
import game.message.backpackMessage.ResUseItemMakeBuff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * @author Administrator
 */
public class BackpackHandlerScript implements IScript, IBackpackScript {

    private static final Logger log = LogManager.getLogger("BackpackHandlerScript");

    @Override
    public int getId() {
        return ScriptEnum.BackpackBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void OnReqBagClearUp(Player player, ReqBagClearUp messInfo) {
        Manager.backpackManager.manager().bagClearUp(player, false);
    }

    /**
     * 物品合成规则
     * 1、先合成未绑定的物品
     * 2、未绑定不够且同时也有绑定的，加入绑定的
     * 3、合成绑定的(材料中有绑定，则合成出的道具为绑定)
     */
    @Override
    public void OnReqCompound(Player player, ReqCompound messInfo) {
        final int type = messInfo.getType();
        List<Long> nonBindIdList = messInfo.getNonBindIdList();
        List<Long> bindIdList = messInfo.getBindIdList();
        if (null == nonBindIdList && null == bindIdList) {
            return;
        }

        //优先使用非绑定
        int num = compound(nonBindIdList, player, type, false, false, bindIdList.size() != 0);
        //合成一次就成功则不再使用绑定合成
        if (type == 0 && num == 1) {
            sendCompoundResult(player);
            return;
        }

        //使用绑定合成
        compound(bindIdList, player, type, true, nonBindIdList.size() != 0, false);

        sendCompoundResult(player);
    }

    private void sendCompoundResult(Player player) {
        backpackMessage.ResCompoundResult.Builder builder = backpackMessage.ResCompoundResult.newBuilder();
        MessageUtils.send_to_player(player, backpackMessage.ResCompoundResult.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void OnReqMoveItem(Player player, ReqMoveItem messInfo) {
        int num = messInfo.getNum();
        long itemId = messInfo.getItemId();
        // 数据检查
        if (num < 0) {
            return;
        }
        // 获得要移动物品
        Item item = Manager.backpackManager.manager().getItemById(player, itemId);
        if (item == null) {
            return;
        }
        if (num >= item.getNum()) {
            return;
        }
        int modleId = item.getItemModelId();
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(modleId);
        if (model == null) {
            return;
        }
        long actionId = IDConfigUtil.getLogId();//日志事件
        int oldNum = item.getNum();

        try {
            int toGridId = getBackpackClosestEmptGridId(player, item.getGridId());
            if (toGridId == -1) {
                log.error("包裹空间不足,拆分失败 roleId:" + player.getId());
                return;
            }
            Item togrid = Manager.backpackManager.manager().getItemByCellId(player, toGridId);
            if (togrid != null) {
                return;
            }

            // 新增拆分物品
            Item clone = item.clone();
            clone.setId(IDConfigUtil.getId());
            clone.setNum(num);
            clone.setGridId(toGridId);
            player.getBackpackItems().put(clone.getGridId(), clone);
            item.setNum(item.getNum() - num);

            Manager.backpackManager.manager().sendItemChange(player, ItemChangeReason.Slip, item);
            Manager.backpackManager.manager().sendItemAdd(player, ItemChangeReason.Slip, clone);
            // 拆分增加日志
            Manager.backpackManager.manager().writeItemLog(player, clone.getId(), modleId, 0, clone.getNum(),
                    ItemChangeReason.Slip, ItemChangeAction.ADD, actionId, 0, 0, clone.getGridId());
            // 拆分减少日志
            Manager.backpackManager.manager().writeItemLog(player, item.getId(), modleId, oldNum, item.getNum(),
                    ItemChangeReason.Slip, ItemChangeAction.CHANGE, actionId, 0, 0, item.getGridId());
        } catch (Exception e) {
            log.error(e, e);
        }
        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
    }

    @Override
    public void OnReqOpenBagCell(Player player, backpackMessage.ReqOpenBagCell messInfo) {
        int openCellId = messInfo.getCellId();
        if (openCellId <= 0 || openCellId > Global.Born_Bag_Num.get(1) || openCellId <= player.getBagCellsNum()) {
            log.error("背包 非法请求开格号" + openCellId + "roleid" + player.getId() + "当前格子ID" + player.getBagCellsNum());
            return;
        }
        //收到消息时已判断传入格子参数为正确参数
        int needgold = 0;
        for (int i = player.getBagCellsNum() + 1; i <= openCellId; i++) {
            int bagGridId = 1 * 1000 + i;
            Cfg_Bag_grid_Bean config = CfgManager.getCfg_Bag_grid_Container().getValueByKey(bagGridId);
            if (i == player.getBagCellsNum() + 1) {
                if (player.getBagCellTimeCount() < config.getTime()) {
                    needgold += config.getCost();
                }
            } else {
                needgold += config.getCost();
            }
        }
        long actionId = IDConfigUtil.getLogId();
        if (Manager.currencyManager.manager().decGold(player, needgold, ItemChangeReason.OpenBagCellDec, actionId)) {
            Manager.backpackManager.manager().openBagCellSuccess(player, openCellId, (byte) 1, needgold, actionId);
        } else {
            backpackMessage.ResOpenBagCellFailed.Builder msg = backpackMessage.ResOpenBagCellFailed.newBuilder();
            msg.setReason(-1);
            MessageUtils.send_to_player(player, backpackMessage.ResOpenBagCellFailed.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public void OnReqSellItems(Player player, backpackMessage.ReqSellItems messInfo) {
        Long itemId = messInfo.getItemId();
        int num = messInfo.getNum();
        dealItemSell(player, itemId, num);
    }

    private void dealItemSell(Player player, Long itemId, int num) {
        long actionId = IDConfigUtil.getLogId();
        int sellNums = 0;
        Item item = Manager.backpackManager.manager().getItemById(player, itemId);
        if (item == null) {
            return;
        }
        if (num <= 0) {
            log.error("物品不够选择的数量，售卖失败,参数错误" + num);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_ITEM_NUM_NOTZERO);
            return;
        }
        if (item.getNum() < num) {
            log.error("物品不够选择的数量，售卖失败");
            MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, MessageString.BAGSELL_ITEM_NUMERROR);
            return;
        }
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        if (model == null) {
            return;
        }

        if (model.getType() == ItemTypeConst.EQUIP) {
            //装备不能出售
            log.error("物品不够选择的数量，售卖失败");

            MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, MessageString.BAGSELL_ITEM_EQUIPERROR);
            return;
        }

        boolean overdue = item.haveLost();
        if (Manager.backpackManager.manager().onRemoveItem(player, item, num, ItemChangeReason.SellItemDec, actionId)) {
            if (overdue) {//过期道具出售直接销毁
                return;
            }
            //所有可出售物品的单价均为1
            sellNums = 1 * num;
            //新版本出售的货币统一为3
            Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.BindMoney, sellNums, ItemChangeReason.SellItemDec, actionId);
        }
    }

    @Override
    public void OnReqUseItem(Player player, backpackMessage.ReqUseItem messInfo) {
        long itemId = messInfo.getItemId();
        int num = messInfo.getNum();
        Manager.backpackManager.manager().useItem(player, itemId, num, messInfo.getIndex());
    }

    /**
     * 获取离当前格子最近的一个空格子
     *
     * @param gridId
     * @return
     */
    private int getBackpackClosestEmptGridId(Player player, int gridId) {
        int result = -1;
        if (gridId <= 0) {
            //非法的格子
            return result;
        }
        int bagCellsNum = player.getBagCellsNum();
        for (int i = gridId + 1; i <= bagCellsNum; i++) {
            Item item = player.getBackpackItems().get(i);
            if (item == null || item.getNum() == 0) {
                result = i;
                break;
            }
        }
        if (gridId <= 1) {
            return result;
        }
        int result2 = -1;
        for (int i = gridId - 1; i >= 1; i--) {
            Item item = player.getBackpackItems().get(i);
            if (item == null || item.getNum() == 0) {
                result2 = i;
                break;
            }
        }
        if (result == -1 && result2 == -1) {
            return -1;
        }
        if (result != -1 && result2 == -1) {
            return result;
        }
        if (result == -1 && result2 != -1) {
            return result2;
        }
        int distance = result - gridId;
        int distance2 = gridId - result2;
        if (distance <= distance2) {
            return result;
        } else {
            return result2;
        }
    }

    @Override
    public void onReqUseItemMakeBuff(Player player, backpackMessage.ReqUseItemMakeBuff messInfo) {
        int modelId = messInfo.getItemModelId();
        Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(modelId);
        if (itemBean == null) {
            log.error(player.nameIdString() + "物品ID" + modelId + "不存在");
            return;
        }
        if (itemBean.getEffect_num().size() < 1) {
            log.error(player.nameIdString() + "物品ID" + modelId + " 的特效没有哦");
            return;
        }
        ReadArray<Integer> ali = itemBean.getEffect_num().get(0);
        int buffId = (int) (ali.get(1));
        Cfg_Buff_Bean buffBean = CfgManager.getCfg_Buff_Container().getValueByKey(buffId);
        if (buffBean == null) {
            log.error(player.nameIdString() + "物品ID" + modelId + " 的产生的BUFF" + buffId + " 配置不存在");
            return;
        }
        int state = 0;
        Buff buff = Manager.buffManager.deal().isHaveBuff(player, buffBean.getType(), buffBean.getGroup());
        if (buff == null) {
            state = 0;
        } else {
            //两个BUFF相同
            if (buff.getBuffId() == buffId) {
                state = 1;
                if (buffBean.getOverlap() > 0 && buff.getOverlap() >= buffBean.getOverlap()) {
                    state = 2;
                }
            } else {
                Cfg_Buff_Bean havebuffBean = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
                if (havebuffBean.getGroup() == buffBean.getGroup()) {
                    state = 3;//替换
                    if (havebuffBean.getLevel() < buffBean.getLevel()) {
                        state = -1;
                    }
                }
            }
        }
        log.debug(player.nameIdString() + "物品ID" + modelId + " 的产生的BUFF" + buffId + " 配置 state =" + state);
        sendMakeBuffResult(player, buffId, modelId, state);
    }

    @Override
    public void onReqAutoUseItem(Player player) {
        int totalBindGold = 0;
        long totalExp = 0L;
        int bindGoldAmount = 0;
        Cfg_Item_Bean bean;
        Iterator<Map.Entry<Integer, Item>> iteras = player.getBackpackItems().entrySet().iterator();
        while (iteras.hasNext()) {
            Map.Entry<Integer, Item> entry = iteras.next();
            Item item = entry.getValue();
            bean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
            if (bean == null) {
                continue;
            }
            //使用银元宝袋
            if (item.getItemModelId() == 1017) {
                if (Manager.backpackManager.manager().useItem(player, item.getId(), item.getNum(), 0)) {
                    if (bindGoldAmount <= 0) {
                        List<List<Integer>> drops = Manager.dropManager.deal().getItemDrops(player, bean.getUes_gift());
                        bindGoldAmount = drops.get(0).get(1);
                    }
                    totalBindGold += item.getNum() * bindGoldAmount;
                }
                continue;
            }
            //使用经验丹
            if (bean.getType() != ItemTypeConst.EFFECT_ITEM || bean.getEffect_num() == null || bean.getEffect_num().size() <= 0) {
                continue;
            }
            int effectType = bean.getEffect_num().get(0).get(0);
            if (effectType != ItemEffectType.AddExp) {
                continue;
            }
            //过期的经验丹出售掉
            if (item.haveLost()) {
                dealItemSell(player, item.getId(), item.getNum());
                continue;
            }

            if(Global.No_auto_use_item.contains(item.getItemModelId())){
                continue;
            }

            if (Manager.backpackManager.manager().useItem(player, item.getId(), item.getNum(), 0)) {
                totalExp += getAddExp(player, bean.getEffect_num().get(0).get(2), bean.getEffect_num().get(0).get(3), item.getNum());
            }
        }

        backpackMessage.ResAutoUseItem.Builder msg = backpackMessage.ResAutoUseItem.newBuilder();
        msg.setBindGold(totalBindGold);
        msg.setExp(totalExp);
        MessageUtils.send_to_player(player, backpackMessage.ResAutoUseItem.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private long getAddExp(Player player, int maxLevel, int prop, int num) {
        int key = player.getLevel();
        if (player.getLevel() >= maxLevel) {
            key = maxLevel;
        }

        Cfg_Characters_Bean bean = CfgManager.getCfg_Characters_Container().getValueByKey(key);
        if (bean == null) {
            return 0L;
        }

        return (bean.getExp() * prop / 10000) * num;
    }

    private void sendMakeBuffResult(Player player, int buffId, int modelId, int state) {
        ResUseItemMakeBuff.Builder msg = ResUseItemMakeBuff.newBuilder();
        msg.setBuffId(buffId);
        msg.setItemModelId(modelId);
        msg.setState(state);
        MessageUtils.send_to_player(player, ResUseItemMakeBuff.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 检查是否满足合成的条件
     */
    private ReadArray<Integer> checkCompound(Cfg_Item_Bean bean) {
        //判断物品是否可以合成其他物品，主要是看button type是否有数据，有数据的话是否配置了合成
        final ReadArray<Integer> buttonTypeArray = bean.getButton_type();
        if (null == buttonTypeArray || 0 == buttonTypeArray.size()) {
            return null;
        }

        boolean compoundFlag = false;
        for (int i = 0; i < buttonTypeArray.size(); i++) {
            if (7 == buttonTypeArray.get(i)) {
                compoundFlag = true;
                break;
            }
        }

        if (!compoundFlag) {
            return null;
        }

        //检查合成的目标配置
        final ReadArray<Integer> array = bean.getHechen_target();
        if (null == array || 2 != array.size()) {
            return null;
        }

        return array;
    }

    /**
     * 合成的时候选了绑定和非绑定混合合成，在合成绑的时候优先消耗非绑定的物品 (合成材料为同一种道具)
     *
     * @param itemIds          合成材料ID列表
     * @param type             0：只合成1次，1：全部合成或一键合成
     * @param firstCostNotBand 优先扣非绑
     * @param hasNotBind       是否选中的绑定的物品
     */
    private int compound(List<Long> itemIds, Player player, int type, boolean isBind, boolean firstCostNotBand, boolean hasNotBind) {
        if (itemIds.isEmpty()) {
            return 0;
        }

        int itemModelId = 0;
        String itemName = "";
        int totalCompoundNum = 0;
        int totalNeedNum = -1;
        int totalNum = 0;
        int compoundId = 0;
        int currencyType = 0;
        int currencyMoney = 0;
        int totalCurrencyNum = 0;
        //处理绑定材料时，是否计算非邦材料
        boolean isBindItem = false;
        int notBindNum = 0;
        int needNotBindNum = 0;
        int needNum = 0;
        //<道具ID,道具对象>
        Map<Long, Item> itemMaps = new HashMap<>();
        //<道具ID,要扣除数量>
        Map<Long, Integer> numMaps = new HashMap<>();
        for (long itemId : itemIds) {
            Item item = Manager.backpackManager.manager().getItemById(player, itemId);
            if (item == null) {
                log.info(String.format("玩家{%s}使用道具{%s}不存在，合成失败！", TaskHelp.getPlayerInfo(player), itemId));
                return 0;
            }
            if (item.isBind() != isBind) {
                log.info(String.format("警告!!!玩家{%s}使用道具{%s}绑定类型不一致！", TaskHelp.getPlayerInfo(player), itemId));
            }
            itemModelId = item.getItemModelId();

            //先取出每个item需要扣除的数量
            Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(itemModelId);
            if (null == bean) {
                log.error(String.format("合成道具{%s}失败，在配置表Item中找不到对应的道具!!!", itemModelId));
                return 0;
            }
            ReadArray<Integer> compoundTargetArray = checkCompound(bean);
            if (null == compoundTargetArray) {
                return 0;
            }

            if ((null != bean.getHechen_money() && bean.getHechen_money().size() >= 2 && bean.getHechen_money().get(0) > 0 && bean.getHechen_money().get(1) > 0)) {
                currencyType = bean.getHechen_money().get(0);
                currencyMoney = bean.getHechen_money().get(1);
            }
            compoundId = compoundTargetArray.get(0);
            needNum = compoundTargetArray.get(1);
            itemName = bean.getName();

            itemMaps.put(item.getId(), item);
            numMaps.put(item.getId(), item.getNum());
            totalNum += item.getNum();
        }
        //处理绑定材料时，则把背包中剩余非绑定材料先拿来消耗
        if (firstCostNotBand && !hasNotBind) {
            notBindNum = Manager.backpackManager.manager().getItemNum(player, itemModelId, false);
        }
        //检查最少合成数量
        if ((totalNum + notBindNum < needNum) && (
                (!firstCostNotBand && !hasNotBind) ||//处理非邦时，没有绑定材料 或者 处理绑定时,没有非邦材料
                        (firstCostNotBand && !hasNotBind)//处理绑定时,有非绑材料
        )) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_UI_EQUIPSYN_METRILLESS, ServerStr.getChatTableName(itemName), String.valueOf(needNum));
            return 0;
        } else if ((totalNum + notBindNum < needNum) && (!firstCostNotBand && hasNotBind)) {//处理非邦时，有绑定材料
            return 0;
        }

        for (Item item : itemMaps.values()) {
            int itemNum = item.getNum();

            int decNum;
            isBindItem = item.isBind();
            switch (type) {
                case 0:
                    if (totalNeedNum == -1) {
                        totalCompoundNum = 1;
                        totalNeedNum = needNum;
                        if (currencyType > 0) {
                            //检查货币费用是否足够
                            totalCurrencyNum += currencyMoney * totalCompoundNum;
                            if (!Manager.currencyManager.manager().canRemoveCurrency(player, currencyType, totalCurrencyNum)) {
                                MessageUtils.notify_player(player, Notify.NORMAL, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(currencyType));
                                return 0;
                            }
                        }
                    }
                    if (totalNeedNum >= itemNum + notBindNum) {
                        decNum = itemNum;
                        needNotBindNum = notBindNum;
                    } else {//材料足够
                        decNum = totalNeedNum - notBindNum;
                        needNotBindNum += notBindNum;
                    }
                    numMaps.put(item.getId(), decNum);
                    totalNeedNum -= itemNum + notBindNum;

                    //使用非绑定时，如果非绑数量不够，并且选中了绑定的物品，继续走合成绑定的物品，不给道具不足的提示
                    if (totalCompoundNum <= 0) {
                        if (!hasNotBind) {
                            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_UI_EQUIPSYN_METRILLESS, ServerStr.getChatTableName(itemName), String.valueOf(needNum));
                        }
                        return 0;
                    }
                    break;
                case 1:
                    if (totalNeedNum == -1) {
                        totalCompoundNum = (totalNum + notBindNum) / needNum;
                        totalNeedNum = totalCompoundNum * needNum;

                        if (currencyType > 0) {
                            //检查货币费用是否足够
                            totalCurrencyNum += currencyMoney * totalCompoundNum;
                            if (!Manager.currencyManager.manager().canRemoveCurrency(player, currencyType, totalCurrencyNum)) {
                                //全部的钱不够则检查剩下的钱可以合成多少个
                                int remainCurrency = Manager.currencyManager.manager().getCurrencyIntNum(player, currencyType);
                                if (remainCurrency < currencyMoney) {
                                    MessageUtils.notify_player(player, Notify.NORMAL, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(currencyType));
                                    return 0;
                                }
                                int remainNum = remainCurrency / currencyMoney;
                                totalCompoundNum = remainNum;
                                totalNeedNum = totalCompoundNum * needNum;
                                totalCurrencyNum = totalCompoundNum * currencyMoney;
                            }
                        }
                    }
                    if (totalNeedNum >= itemNum + notBindNum) {
                        decNum = itemNum;
                        needNotBindNum = notBindNum;
                    } else {//材料足够
                        decNum = totalNeedNum - notBindNum;
                        needNotBindNum += notBindNum;
                    }
                    numMaps.put(item.getId(), decNum);
                    totalNeedNum -= itemNum + notBindNum;

                    //使用非绑定时，如果非绑数量不够，并且选中了绑定的物品，继续走合成绑定的物品，不给道具不足的提示
                    if (totalCompoundNum <= 0) {
                        if (!hasNotBind) {
                            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_UI_EQUIPSYN_METRILLESS, ServerStr.getChatTableName(itemName), String.valueOf(needNum));
                        }
                        return 0;
                    }
                    break;
                default:
                    return 0;
            }
        }

        //生产合成道具
        Item targetItem = Item.createItem(compoundId, totalCompoundNum, isBindItem);
        if (targetItem == null) {
            log.error(String.format("合成道具失败!!!道具id{%s}在配置表中不存在!!!", compoundId));
            return 0;
        }

        long logId = IDConfigUtil.getLogId();

        //处理道具扣除
        for (Item item : itemMaps.values()) {
            int useItemNum = numMaps.get(item.getId());
            if (useItemNum > 0) {
                boolean removeItem = Manager.backpackManager.manager().onRemoveItem(player, item, useItemNum, ItemChangeReason.CompoundDec, logId);
                if (!removeItem) {
                    removeItem = Manager.backpackManager.manager().onRemoveItem(player, item.getItemModelId(), useItemNum, ItemChangeReason.CompoundDec, logId);
                    if (!removeItem) {
                        log.info("合成道具失败!!!扣除道具" + item.getItemModelId() + "异常!!!");
                        return 0;
                    }
                }
            }
        }

        //处理绑定材料时，扣除非绑定道具
        if (firstCostNotBand && notBindNum != 0) {
            boolean removeItem = Manager.backpackManager.manager().onRemoveItem(player, itemModelId, needNotBindNum, false, ItemChangeReason.CompoundDec, logId);
            if (!removeItem) {
                log.info("合成道具失败!!!扣除道具" + itemModelId + "异常!!!");
                return 0;
            }
        }

        //扣除消耗货币
        if (currencyType > 0 && totalCurrencyNum > 0 && !Manager.currencyManager.manager().onDecItemCoin(player, totalCurrencyNum, ItemChangeReason.CompoundDec, logId, currencyType)) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(currencyType));
            return 0;
        }

        //增加合成道具
        if (!Manager.backpackManager.manager().addItem(player, targetItem, ItemChangeReason.CompoundAdd, logId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.System, String.valueOf(MessageString.CompoundTitle), Collections.singletonList(targetItem), ItemChangeReason.CompoundAdd, logId);
        }
        return totalCompoundNum;
    }
}
