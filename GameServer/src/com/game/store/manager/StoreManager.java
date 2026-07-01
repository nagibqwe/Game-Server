package com.game.store.manager;

import com.game.backpack.structs.Equip;
import com.game.backpack.structs.Item;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.store.script.IStoreManagerScript;
import com.game.store.script.IStoreScript;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.message.backpackMessage;
import game.message.storeMessage;
import game.message.storeMessage.ResStoreItemAdd;
import game.message.storeMessage.ResStoreItemChange;
import game.message.storeMessage.ResStoreItemDelete;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author Administrator
 */
public class StoreManager {
    private static final Logger log = LogManager.getLogger(StoreManager.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        StoreManager processor;

        Singleton() {
            this.processor = new StoreManager();
        }

        StoreManager getProcessor() {
            return processor;
        }
    }

    public static StoreManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 仓库整理
     *
     * @param player
     * @param isgm
     */
    public void storeClearUp(Player player, boolean isgm) {
        if (player.getStoreCellsNum() <= 0) {
            return;
        }
        Manager.storeManager.manager().storeClearUp(player, isgm);
    }

    /**
     * 发送仓库物品消息
     *
     * @param player
     */
    public void sendStoreItemInfos(Player player) {
        storeMessage.ResStoreItemInfos.Builder msg = storeMessage.ResStoreItemInfos.newBuilder();
        msg.setCellnum(player.getStoreCellsNum());
        msg.setCellTime(0);
        ConcurrentHashMap<Integer, Item> storeItems = player.getStoreItems();
        List<Equip> equips = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        Manager.backpackManager.manager().sortItems(storeItems, equips, items, player.getCareer());
        int i = 0;
        Integer index = 0;
        for(; i < equips.size(); i++) {
            Equip equip = equips.get(i);
            index = i + 1;
            backpackMessage.ItemInfo.Builder info = Manager.backpackManager.manager().addItemInfoToList(index, equip, storeItems, player);
            msg.addItemInfoList(info);
        }
        for(int j = 0; j < items.size(); j++) {
            Item item = items.get(j);
            index = ++i;
            backpackMessage.ItemInfo.Builder info = Manager.backpackManager.manager().addItemInfoToList(index, item, storeItems, player);
            msg.addItemInfoList(info);
        }
        MessageUtils.send_to_player(player, storeMessage.ResStoreItemInfos.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 发送仓库物品增加消息
     *
     * @param player
     * @param reason
     * @param item
     */
    public void sendStoreItemAdd(Player player, int reason, Item item) {
        ResStoreItemAdd.Builder msg = ResStoreItemAdd.newBuilder();
        msg.setReason(reason);
        msg.setItemInfo(Manager.backpackManager.manager().buildItemInfo(item));//);
        MessageUtils.send_to_player(player, ResStoreItemAdd.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 发送物品改变消息
     *
     * @param player
     * @param reason
     * @param item
     */
    public void sendStoreItemChange(Player player, int reason, Item item) {
        ResStoreItemChange.Builder msg = ResStoreItemChange.newBuilder();
        msg.setReason(reason);
        msg.setItemInfo(Manager.backpackManager.manager().buildItemInfo(item));//);
        MessageUtils.send_to_player(player, ResStoreItemChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 发送物品删除消息
     *
     * @param player
     * @param reason
     * @param itemid
     */
    public void sendStoreItemDelete(Player player, int reason, long itemid) {
        ResStoreItemDelete.Builder msg = ResStoreItemDelete.newBuilder();
        msg.setReason(reason);
        msg.setItemId(itemid);
        MessageUtils.send_to_player(player, ResStoreItemDelete.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public IStoreScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.StoreBaseScript);
        if (is instanceof IStoreScript) {
            return (IStoreScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public IStoreManagerScript manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.StoreManagerBaseScript);
        if (is instanceof IStoreManagerScript) {
            return (IStoreManagerScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }
}
