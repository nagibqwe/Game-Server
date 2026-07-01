package common.store;

import com.data.CfgManager;
import com.data.bean.Cfg_Item_Bean;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.cooldown.structs.CooldownTypes;
import com.game.manager.Manager;
import  com.data.MessageString;
import com.game.player.structs.Player;
import com.game.player.structs.SavePlayerLevel;
import com.game.script.structs.ScriptEnum;
import com.game.store.script.IStoreManagerScript;
import com.game.structs.GlobalType;
import com.game.structs.ItemChangeAction;
import com.data.ItemChangeReason;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author admin
 */
public class StoreManagerScript implements IScript, IStoreManagerScript {

    @Override
    public int getId() {
        return ScriptEnum.StoreManagerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void storeClearUp(Player player, boolean isgm) {
        if (!checkAble(player)) {
            return;
        }
        if (!isgm) {
            if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.STORE_CLEAR, null)) {
                long cooldownTime = Manager.cooldownManager.getCooldownTime(player, CooldownTypes.STORE_CLEAR, null);
                if (cooldownTime / 1000L != 0) {//不提示0
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.BagClearUpCooling, String.valueOf(cooldownTime / 1000L));
                }
                return;
            }
            Manager.cooldownManager.addCooldown(player, CooldownTypes.STORE_CLEAR, null, GlobalType.ClearUp_Ttime_Interval);
        }
        List<Map.Entry<Integer, Item>> list = new ArrayList<>();
        ConcurrentHashMap<Integer, Item> container = player.getStoreItems();
        list.addAll(container.entrySet());
        //LOGGER.debug("整理开始前"+list.size());
        long actionId = IDConfigUtil.getLogId();
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<Integer, Item> entry = list.get(i);
            Item value = entry.getValue();
            Cfg_Item_Bean valuemodel = CfgManager.getCfg_Item_Container().getValueByKey(value.getItemModelId());
            // 如果第一个物品是满的那么直接略过
            if (valuemodel.getMax() > 1 && valuemodel.getMax() > value.getNum()) {
                for (int j = i + 1; j < list.size(); j++) {
                    Map.Entry<Integer, Item> entry2 = list.get(j);
                    Item value2 = entry2.getValue();
                    if (Manager.backpackManager.manager().isMergeAble(value, value2)) {
                        // 合并
                        if (value.getNum() + value2.getNum() <= valuemodel.getMax()) {
                            int beforeNum1 = value.getNum();
                            int beforeNum2 = value2.getNum();
                            value.setNum(value.getNum() + value2.getNum());
                            // 移除原来物品
                            // 这里直接移除的话会倒致下标溢出
                            value2.setNum(0);
//                            if (valuemodel.getLog() == 1) {
                            Manager.backpackManager.manager().writeItemLog(player, value.getId(), value.getItemModelId(), beforeNum1,
                                    value.getNum(), ItemChangeReason.StoreClearUp,
                                    ItemChangeAction.CHANGE, actionId, 0, 0, value.getGridId());
                            Manager.backpackManager.manager().writeItemLog(player, value2.getId(), value2.getItemModelId(), beforeNum2,
                                    0, ItemChangeReason.StoreClearUp, ItemChangeAction.MOVE, actionId, 0, 0, value2.getGridId());
//                            }

                        } else {
                            int beforeNum1 = value.getNum();
                            int beforeNum2 = value2.getNum();
                            int all = value.getNum() + value2.getNum();
                            value.setNum((int) valuemodel.getMax());
                            value2.setNum(all - (int) valuemodel.getMax());
//                            if (valuemodel.getLog() == 1) {
                            Manager.backpackManager.manager().writeItemLog(player, value.getId(), value.getItemModelId(), beforeNum1,
                                    value.getNum(), ItemChangeReason.StoreClearUp,
                                    ItemChangeAction.CHANGE, actionId, 0, 0, value.getGridId());
                            Manager.backpackManager.manager().writeItemLog(player, value2.getId(), value2.getItemModelId(), beforeNum2,
                                    value2.getNum(), ItemChangeReason.StoreClearUp,
                                    ItemChangeAction.CHANGE, actionId, 0, 0, value2.getGridId());
//                            }

                        }
                        if (value.getNum() >= valuemodel.getMax()) {
                            break;
                        }
                    }
                }
            }
        }
        //清除数量为零的
        for (Map.Entry<Integer, Item> entry : list) {
            if (entry.getValue().getNum() == 0) {
                container.remove(entry.getKey());
            }
        }

        Manager.storeManager.sendStoreItemInfos(player);
        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
    }

    @Override
    public void storeMoveItem(Player player, Item item, int toCellId) {
        if (item.getNum() <= 0) {
            return;
        }
        // 数据检查
        if (toCellId > player.getStoreCellsNum() || toCellId <= 0) {
            return;
        }

        if (toCellId == item.getGridId()) {
            return;
        }
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        if (model == null) {
            return;
        }
//        boolean isNeedLog = (model.getLog() == 1);
        boolean isNeedLog = true;
        if (player.getStoreItems().get(toCellId) == null) {
            // 完全移动
            player.getStoreItems().remove(item.getGridId());
            item.setGridId(toCellId);
            player.getStoreItems().put(item.getGridId(), item);
            Manager.storeManager.sendStoreItemChange(player, ItemChangeReason.StoreMove, item);
            if (isNeedLog) {
                Manager.backpackManager.manager().writeItemLog(player, item.getId(), item.getItemModelId(), item.getNum(),
                        item.getNum(), ItemChangeReason.StoreMove, ItemChangeAction.MOVE, IDConfigUtil.getLogId(), 0, 0, item.getGridId());
            }

        } else {
            Item toGrid = player.getStoreItems().get(toCellId);
            if (toGrid.equals(item)) {
                return;
            }
            if (Manager.backpackManager.manager().isMergeAble(item, toGrid)) {
                if (item.getNum() + player.getStoreItems().get(toCellId).getNum() <= model.getMax()) {
                    int beforeNum = toGrid.getNum();
                    // 完全合并
                    // 合并
                    toGrid.setNum(item.getNum() + toGrid.getNum());
                    // 移除原来物品
                    player.getStoreItems().remove(item.getGridId());
                    //发送给客户端
                    Manager.storeManager.sendStoreItemChange(player, ItemChangeReason.StoreMove, toGrid);
                    Manager.storeManager.sendStoreItemDelete(player, ItemChangeReason.StoreMove, item.getGridId());
                    if (isNeedLog) {
                        long actionId = IDConfigUtil.getLogId();
                        Manager.backpackManager.manager().writeItemLog(player, item.getId(), item.getItemModelId(), item.getNum(),
                                0, ItemChangeReason.StoreMove, ItemChangeAction.REMOVE, actionId, 0, 0, item.getGridId());

                        Manager.backpackManager.manager().writeItemLog(player, toGrid.getId(), toGrid.getItemModelId(), beforeNum,
                                toGrid.getNum(), ItemChangeReason.StoreMove, ItemChangeAction.CHANGE, actionId, 0, 0, toGrid.getGridId());

                    }

                } else {
                    // 部分合并
                    // 可以合并 的数量
                    int sub =(int) model.getMax() - toGrid.getNum();
                    int beforeNum1 = item.getNum();
                    int beforeNum2 = toGrid.getNum();
                    toGrid.setNum((int)model.getMax());
                    item.setNum(item.getNum() - sub);
                    Manager.storeManager.sendStoreItemChange(player, ItemChangeReason.StoreMove, toGrid);
                    Manager.storeManager.sendStoreItemChange(player, ItemChangeReason.StoreMove, item);
                    if (isNeedLog) {
                        long actionId = IDConfigUtil.getLogId();
                        Manager.backpackManager.manager().writeItemLog(player, item.getId(), item.getItemModelId(), beforeNum1,
                                item.getNum(), ItemChangeReason.StoreMove, ItemChangeAction.CHANGE, actionId, 0, 0, item.getGridId());
                        Manager.backpackManager.manager().writeItemLog(player, toGrid.getId(), toGrid.getItemModelId(), beforeNum2,
                                toGrid.getNum(), ItemChangeReason.StoreMove, ItemChangeAction.CHANGE, actionId, 0, 0, toGrid.getGridId());
                    }

                }
            } else {
                // 交换位置
                int tmpgrid = item.getGridId();
                player.getStoreItems().remove(tmpgrid);
                player.getStoreItems().remove(toGrid.getGridId());
                item.setGridId(toGrid.getGridId());
                toGrid.setGridId(tmpgrid);
                player.getStoreItems().put(item.getGridId(), item);
                player.getStoreItems().put(toGrid.getGridId(), toGrid);
                Manager.storeManager.sendStoreItemChange(player, ItemChangeReason.StoreMove, toGrid);
                Manager.storeManager.sendStoreItemChange(player, ItemChangeReason.StoreMove, item);
                if (isNeedLog) {
                    long actionId = IDConfigUtil.getLogId();
                    Manager.backpackManager.manager().writeItemLog(player, item.getId(), item.getItemModelId(), item.getNum(),
                            item.getNum(), ItemChangeReason.StoreMove, ItemChangeAction.MOVE, actionId, 0, 0, item.getGridId());
                    Cfg_Item_Bean toGridModel = CfgManager.getCfg_Item_Container().getValueByKey(toGrid.getItemModelId());
                    Manager.backpackManager.manager().writeItemLog(player, toGrid.getId(), toGrid.getItemModelId(), item.getNum(),
                            toGrid.getNum(), ItemChangeReason.StoreMove, ItemChangeAction.MOVE, actionId, 0, 0, toGrid.getGridId());

                }

            }
        }
        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
    }

    @Override
    public int getStoreFirstEmptGridId(Player player) {
        for (int i = 1; i <= player.getStoreCellsNum(); i++) {
            Item item = player.getStoreItems().get(i);
            if (item == null || item.getNum() == 0) {
                return i;
            }
        }
        return -1;
    }

    private boolean checkAble(Player player) {
        if (player == null) {
            return false;
        }
        return player.getStoreCellsNum() > 0;
    }

}
