package common.store;

import com.data.CfgManager;
import com.data.bean.Cfg_Item_Bean;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import  com.data.MessageString;
import com.game.player.structs.Player;
import com.game.player.structs.SavePlayerLevel;
import com.game.script.structs.ScriptEnum;
import com.game.store.script.IStoreScript;
import com.game.structs.ItemChangeAction;
import com.data.ItemChangeReason;
import com.game.utils.MessageUtils;
import com.game.vip.manager.VipManager;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.message.storeMessage;
import game.message.storeMessage.ReqStoreMoveItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author admin
 */
public class StoreHandlerScript implements IScript, IStoreScript {

    private static final Logger log = LogManager.getLogger("StoreHandlerScript");

    @Override
    public int getId() {
        return ScriptEnum.StoreBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void OnReqBagToStore(Player player, storeMessage.ReqBagToStore messInfo) {
        if(!player.getVipPearl().canFree()){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_Vip_Power_No_Ues_Notice);
            return;
        }
        //VIP等级检查
        if(!Manager.vipManager.power().isCanFreeStore(player)){
            return;
        }
        int cellId = messInfo.getCellId();
        if (!checkAble(player)) {
            return;
        }
        Item item = player.getBackpackItems().get(cellId);
        if (item == null) {
            return;
        }

        int modelId = item.getItemModelId();
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(modelId);

        /**
         * 1、判断同类物品是否存在，不存在，再新增
         * 2、存在的话，判断同类物品是否达到堆叠上限，达到则新增
         * 3、如果未达到上限，但是两个物品数量之和大于上限，则新增
         * */
        boolean flag = false;
        int gridId = 0;
        List<Item> storeItems = getStoreItemsByModelId(player, modelId, item.isBind());
        if(null == storeItems || 0 == storeItems.size()) {
            gridId = Manager.storeManager.manager().getStoreFirstEmptGridId(player);
            if (gridId == -1) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoStoreCell);
                return;
            }
        } else {
            int addNum = item.getNum();
            for (Item storeItem : storeItems) {
                int num = storeItem.getNum();
                if (num >= model.getMax()) {
                    continue;
                }

                int totalNum = num + addNum;
                if (totalNum > model.getMax()) {
                    continue;
                }

                storeItem.setNum(totalNum);
                item = storeItem;
                flag = true;
                break;
            }

            if(!flag) {
                gridId = Manager.storeManager.manager().getStoreFirstEmptGridId(player);
                if (gridId == -1) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoStoreCell);
                    return;
                }
            }
        }

        long action = IDConfigUtil.getLogId();
        if (Manager.backpackManager.manager().removeItemByCellId(player, cellId, ItemChangeReason.BagToStore, action)) {
            if(!flag) {
                addStoreItem(player, gridId, item);
            }
            Manager.storeManager.sendStoreItemAdd(player, ItemChangeReason.BagToStore, item);
            Manager.backpackManager.manager().writeItemLog(player, item.getId(), item.getItemModelId(), 0,
                    item.getNum(), ItemChangeReason.BagToStore, ItemChangeAction.MOVE, action, 0, 0, item.getGridId());
        }
    }

    @Override
    public void OnReqStoreClearUp(Player player, storeMessage.ReqStoreClearUp messInfo) {
        if(!player.getVipPearl().canFree()){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_Vip_Power_No_Ues_Notice);
            return;
        }
        //VIP等级检查
        if(!Manager.vipManager.power().isCanFreeStore(player)){
            return;
        }
        Manager.storeManager.manager().storeClearUp(player, false);
    }

    @Override
    public void OnReqStoreMoveItem(Player player, ReqStoreMoveItem messInfo) {
        if(!player.getVipPearl().canFree()){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_Vip_Power_No_Ues_Notice);
            return;
        }
        //VIP等级检查
        if(!Manager.vipManager.power().isCanFreeStore(player)){
            return;
        }
        long itemId = messInfo.getItemId();
        int toGridId = messInfo.getToGridId();
        if (!checkAble(player)) {
            return;
        }
        Item item = getStoreItemById(player, itemId);
        if (item != null) {
            Manager.storeManager.manager().storeMoveItem(player, item, toGridId);
        }
    }

    @Override
    public void OnReqStoreToBag(Player player, storeMessage.ReqStoreToBag messInfo) {
        if(!player.getVipPearl().canFree()){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_Vip_Power_No_Ues_Notice);
            return;
        }
        //VIP等级检查
        if(!Manager.vipManager.power().isCanFreeStore(player)){
            return;
        }
        int cellId = messInfo.getCellId();
        if (!checkAble(player)) {
            return;
        }
        Item item = player.getStoreItems().get(cellId);
        if (item == null) {
            return;
        }
        Cfg_Item_Bean model =CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        if (model == null) {
            log.error("物品不存在 " + item.getItemModelId());
            return;
        }

        if (!Manager.backpackManager.manager().onHasAddSpace(player, item)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            return;
        }
        if (removeStoreItem(player, cellId)) {
            long action = IDConfigUtil.getLogId();
            Manager.storeManager.sendStoreItemDelete(player, ItemChangeReason.StoreToBag, item.getId());
//            if (model.getLog() == 1) {
            Manager.backpackManager.manager().writeItemLog(player, item.getId(), item.getItemModelId(), item.getNum(),
                    0, ItemChangeReason.StoreToBag, ItemChangeAction.MOVE, action, 0, 0, item.getGridId());
//            }
            item.setGridId(-1);
            Manager.backpackManager.manager().addItemHaveMergeFlag(player, item, ItemChangeReason.StoreToBag, action, 0, 0F, false);
        }
    }

    private boolean checkAble(Player player) {
        if (player == null) {
            return false;
        }
        return player.getStoreCellsNum() > 0;
    }

    private void addStoreItem(Player player, int gridId, Item item) {
        item.setGridId(gridId);
        player.getStoreItems().put(item.getGridId(), item);
        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
    }

    /**
     * 通过model id，获取在仓库中的同类物品列表
     * */
    private List<Item> getStoreItemsByModelId(Player player, int itemModelId, boolean isBind) {
        return player.getStoreItems().values().stream()
                .filter(item-> item.getItemModelId() == itemModelId && item.isBind() == isBind)
                .collect(Collectors.toList());
    }

    private Item getStoreItemById(Player player, long itemId) {
        Iterator<Item> iter = player.getStoreItems().values().iterator();
        while (iter.hasNext()) {
            Item item = iter.next();
            if (item.getId() == itemId) {
                return item;
            }
        }
        return null;
    }

    private boolean removeStoreItem(Player player, int cellId) {
        if (player == null) {
            return false;
        }
        player.getStoreItems().remove(cellId);
        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
        return true;
    }
}
