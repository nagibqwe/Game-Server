package common.unrealEquip;


import com.data.*;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.holyEquip.struct.HolyEquipDefine;
import com.game.holyEquip.struct.HolyEquipItem;
import com.game.holyEquip.struct.HolyEquipPart;
import com.game.log.LogDataManager;
import com.game.log.db.RoleGrowLog;
import com.game.log.grow.GrowType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.unrealEquip.script.IUnrealEquip;
import com.game.unrealEquip.struct.UnrealEquipItem;
import com.game.unrealEquip.struct.UnrealEquipPart;
import com.game.utils.MessageUtils;
import game.core.util.IDConfigUtil;
import game.message.HolyEquipMessage;
import game.message.UnrealEquipMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * 幻装 CXL
 */
public class UnrealEquipScript implements IUnrealEquip {


    private final static Logger logger = LogManager.getLogger(UnrealEquipScript.class);

    @Override
    public int getId() {
        return ScriptEnum.UnrealEquipScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }


    private void initPart(Player player) {
        if (player.getUnrealEquipBaseInfo().getUnrealEquipPartList().size() <= 0) {
            for (int i = Manager.unrealEquipManager.UnrealPartMin; i <= Manager.unrealEquipManager.UnrealPartMax; i++) {
                UnrealEquipPart part = new UnrealEquipPart();
                part.setPart(i);
                part.setEquipItem(null);
                player.getUnrealEquipBaseInfo().getUnrealEquipPartList().put(i, part);
            }
        }
        if (player.getUnrealEquipBaseInfo().getUnrealSoulInfoList().size() <= 0) {
            for (ReadArray<Integer> holySoul : Global.Equip_Magic_att_item.getValuees()) {
                player.getUnrealEquipBaseInfo().getUnrealSoulInfoList().put(holySoul.get(0), 0);
            }
        }
    }

    @Override
    public void onLine(Player player) {

        initPart(player);
        UnrealEquipMessage.ResOnlineInit.Builder msg = UnrealEquipMessage.ResOnlineInit.newBuilder();
        UnrealEquipMessage.UnrealEquipItem.Builder messageItem;
        //背包信息
        for (UnrealEquipItem holyEquipItem : player.getUnrealEquipBaseInfo().getUnrealEquipItemList().values()) {
            messageItem = UnrealEquipMessage.UnrealEquipItem.newBuilder();
            messageItem.setItemModelId(holyEquipItem.getItemModelId());
            messageItem.setItemId(holyEquipItem.getId());
            messageItem.setIsbind(holyEquipItem.isBind());
            msg.addUnrealBagItemList(messageItem.build());
        }

        //部位信息
        for (UnrealEquipPart unrealEquipPart : player.getUnrealEquipBaseInfo().getUnrealEquipPartList().values()) {
            UnrealEquipMessage.UnrealEquipPart.Builder messagePart = UnrealEquipMessage.UnrealEquipPart.newBuilder();
            messagePart.setPart(unrealEquipPart.getPart());
            if (unrealEquipPart.getEquipItem() != null) {
                messageItem = UnrealEquipMessage.UnrealEquipItem.newBuilder();
                messageItem.setItemId(unrealEquipPart.getEquipItem().getId());
                messageItem.setItemModelId(unrealEquipPart.getEquipItem().getItemModelId());
                messageItem.setIsbind(unrealEquipPart.getEquipItem().isBind());
                messagePart.setUnrealEquipItem(messageItem.build());
            }
            msg.addUnrealEquipPartList(messagePart.build());
        }

        //圣魂信息
        for (int key : player.getUnrealEquipBaseInfo().getUnrealSoulInfoList().keySet()) {
            UnrealEquipMessage.UnrealSoulInfo.Builder messageSoul = UnrealEquipMessage.UnrealSoulInfo.newBuilder();
            int value = player.getUnrealEquipBaseInfo().getUnrealSoulInfoList().get(key);
            messageSoul.setItemId(key);
            messageSoul.setUseNum(value);
            msg.addUnrealSoulInfoList(messageSoul.build());
        }
        MessageUtils.send_to_player(player, UnrealEquipMessage.ResOnlineInit.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public Item createUnrealEquip(int id) {
        Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(id);
        if (bean == null) {
            logger.error("Cfg_Equip_Bean  为空  + " + id);
            return null;
        }
        UnrealEquipItem unrealEquipItem = new UnrealEquipItem();
        unrealEquipItem.setItemModelId(id);
        unrealEquipItem.setPart(bean.getPart());
        unrealEquipItem.setGrade(bean.getGrade());
        unrealEquipItem.setColor(bean.getQuality());
        //TODO 职业改成多种，老玩家数据需要上线时做兼容处理
        // holyEquipItem.setGenders(getGenders(bean.getGender()));
        int suitID = bean.getGrade() + 10000;

        unrealEquipItem.setSuit(suitID);
        return unrealEquipItem;
    }

    @Override
    public boolean addUnrealEquip(Player player, Item item, int reason) {
        if (item == null) {
            logger.error("传入的道具为空！");
            return false;
        }
        if (item instanceof UnrealEquipItem) {
            UnrealEquipItem holyEquipItem = (UnrealEquipItem) item;

            if (player.getUnrealEquipBaseInfo().getUnrealEquipItemList().size() >= Global.Equip_Holy_bag) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.HolyBagIsfull);
                logger.info("圣装背包已达到上线！ 超出的圣装将分解");
                return false;
            }
            player.getUnrealEquipBaseInfo().getUnrealEquipItemList().put(holyEquipItem.getId(), holyEquipItem);
            List<UnrealEquipItem> holyEquipItems = new ArrayList<>();
            holyEquipItems.add(holyEquipItem);
            sendAddItemToClient(player, holyEquipItems, reason);
            //writeHolyEquipItemLogAndBI(player, holyEquipItem, 0, 1, reason);
            return true;
        }
        return false;
    }


    public boolean canAddUnrealEquipBag(Player player, List<Item> itemList) {
        if (itemList == null || itemList.size() <= 0) {
            logger.error("传入的道具为空！");
            return false;
        }
        int nowSize = player.getUnrealEquipBaseInfo().getUnrealEquipItemList().size() + itemList.size();
        if (nowSize > Global.Equip_Magic_bag) {
            return false;
        } else
            return true;
    }

    private void sendAddItemToClient(Player player, List<UnrealEquipItem> itemList, int reason) {
        UnrealEquipMessage.ResAddUnreal.Builder msg = UnrealEquipMessage.ResAddUnreal.newBuilder();
        for (UnrealEquipItem item : itemList) {
            UnrealEquipMessage.UnrealEquipItem.Builder holyItem = UnrealEquipMessage.UnrealEquipItem.newBuilder();
            holyItem.setItemModelId(item.getItemModelId());
            holyItem.setItemId(item.getId());
            holyItem.setIsbind(item.isBind());
            msg.addAddUnrealitem(holyItem.build());
        }
        msg.setReason(reason);
        MessageUtils.send_to_player(player, UnrealEquipMessage.ResAddUnreal.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void writeHolyEquipItemLogAndBI(Player player, UnrealEquipItem item, int oldNum, int afterNum, int reason) {
        try {
            int change = afterNum - oldNum;
            //writeHolyEquipItemLog( player,  item, change,  reason);
            Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
            int changeType = 0;
            if (change > 0) {
                changeType = 1;
            }
            long actionId = IDConfigUtil.getLogId();
            Manager.backpackManager.manager().writeItemLogAndBI(player, oldNum, afterNum, item, reason, actionId);
            LogDataManager.instance.onItemChange(player.getCreateServerId(), model.getId(), model.getType(), model.getName(), oldNum, afterNum);
            Manager.biManager.getScript().biItem(player, item.getId(), model.getType(), model.getColor(), model.getId(), model.getName(), Math.abs(change), afterNum, reason, 0, changeType, player.getCurGps().getModelId());
            Manager.biManager.get4399Script().itemBiTo4399(player, reason, model.getId(), oldNum, afterNum);
        } catch (Exception e) {
            logger.error("writeHolyEquipItemLogAndBI", e);
        }
    }


    public boolean delHolyEquip(Player player, Item item, int reason) {
        if (item == null) {
            logger.error("传入的道具为空！");
            return false;
        }
        if (item instanceof UnrealEquipItem) {
            UnrealEquipItem holyEquipItem = (UnrealEquipItem) item;
            if (player.getUnrealEquipBaseInfo().getUnrealEquipItemList().containsKey(item.getId())) {
                player.getUnrealEquipBaseInfo().getUnrealEquipItemList().remove(item.getId());
                List<Long> holyEquipItems = new ArrayList<>();
                holyEquipItems.add(holyEquipItem.getId());
                sendDeleteItemToClient(player, holyEquipItems, reason);
                return true;
            }
        }
        return false;
    }

    private void sendDeleteItemToClient(Player player, List<Long> uids, int reason) {
        UnrealEquipMessage.ResDeleteUnreal.Builder msg = UnrealEquipMessage.ResDeleteUnreal.newBuilder();
        msg.setReason(reason);
        msg.addAllDeleteUID(uids);
        MessageUtils.send_to_player(player, UnrealEquipMessage.ResDeleteUnreal.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    @Override
    public void resolveUnrealEquip(Player player, long uid) {
        if (!player.getUnrealEquipBaseInfo().getUnrealEquipItemList().containsKey(uid)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulResolveFail, uid + "");
            return;
        }
        UnrealEquipItem resoulveItem = player.getUnrealEquipBaseInfo().getUnrealEquipItemList().get(uid);
        resolve(player, resoulveItem);
        player.getUnrealEquipBaseInfo().getUnrealEquipItemList().remove(uid);
        //writeHolyEquipItemLogAndBI(player,resoulveItem,1,0,ItemChangeReason.HolyEquipResolveDec);
        List<Long> uids = new ArrayList<>();
        uids.add(uid);
        sendDeleteItemToClient(player, uids, ItemChangeReason.UnrealEquipResolveDec);
    }

    private void resolve(Player player, UnrealEquipItem resoulveItem) {
        Cfg_Equip_Magic_resolve_Bean bean = CfgManager.getCfg_Equip_Magic_resolve_Container().
                getValueByKey(resoulveItem.getItemModelId());
        if (bean != null) {
            if (bean.getResolveItem() != null && bean.getResolveItem().size() > 0) {
                List<Item> items = Item.createItems(bean.getResolveItem());
                if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.UnrealEquipResolveGet, IDConfigUtil.getLogId())) {
                    Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.BAG_FULL_MAIL_TITLE,
                            MessageString.BAG_FULL_MAIL_CONTENT, MessageString.NoBagCell, items, ItemChangeReason.UnrealEquipResolveGet);

                }
            }
            // int messageId =  bean.getType() == 1? MessageString.HolyResolveSucc:MessageString.HolyDisassembleSucc;
            MessageUtils.notify_player(player, Notify.SUCCESS, MessageString.HolyDisassembleSucc);
        }
    }

    @Override
    public void intensifyUnrealPart(Player player, int part) {

    }

    @Override
    public void useUnrealSoul(Player player, int itemID, int num) {
        int canUseNum = 0;
        if (player.getUnrealEquipBaseInfo().getUnrealSoulInfoList().containsKey(itemID)) {
            int curNum = player.getUnrealEquipBaseInfo().getUnrealSoulInfoList().get(itemID);
            for (ReadArray<Integer> holySoulMax : Global.Equip_Magic_att_item.getValuees()) {
                if (itemID == holySoulMax.get(0)) {
                    if (curNum >= holySoulMax.get(1)) {
                        MessageUtils.notify_player(player, Notify.ERROR, MessageString.MagicUseMax);
                        logger.info("使用幻魂数量已达到上线   " + holySoulMax.get(1));
                        return;
                    } else {
                        canUseNum = (holySoulMax.get(1) - curNum) > num ? num : (holySoulMax.get(1) - curNum);
                    }
                }
            }

            if (!Manager.backpackManager.manager().canDeleteItemNum(player, itemID, canUseNum)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.MagicSoulNotEnough);
                logger.info("背幻魂不足   " + canUseNum);
                return;
            }

            int nowUseNum = curNum + canUseNum;
            player.getUnrealEquipBaseInfo().getUnrealSoulInfoList().put(itemID, nowUseNum);
            //从新计算属性----
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.UnrealEquip);
            //发消息给客户端
            Manager.backpackManager.manager().onRemoveItem(player, itemID, canUseNum, ItemChangeReason.UseUnrealSoulDec, itemID);
            UnrealEquipMessage.ResUseUnrealSoulResult.Builder msg = UnrealEquipMessage.ResUseUnrealSoulResult.newBuilder();
            UnrealEquipMessage.UnrealSoulInfo.Builder soulInfo = UnrealEquipMessage.UnrealSoulInfo.newBuilder();
            soulInfo.setItemId(itemID);
            soulInfo.setUseNum(nowUseNum);
            msg.setUnrealSoulData(soulInfo.build());
            MessageUtils.send_to_player(player, UnrealEquipMessage.ResUseUnrealSoulResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            RoleGrowLog.create(player, GrowType.holyEquip_useSoul, 0, itemID, curNum, nowUseNum, null);
        }
    }

    @Override
    public void compoundUnrealSoul(Player player, UnrealEquipMessage.ReqCompoundUnreal message) {
        int id = message.getId();
        Cfg_Equip_Magic_synthesis_Bean equipSyn = CfgManager.getCfg_Equip_Magic_synthesis_Container().getValueByKey(id);
        if (equipSyn == null) {
            logger.info("Cfg_Equip_Magic_synthesis_Bean  未找到  " + id);
            return;
        }
        Cfg_Equip_Bean targetBean = CfgManager.getCfg_Equip_Container().getValueByKey(equipSyn.getId());
        if (targetBean == null) {
            logger.info("Cfg_Equip_Bean  未找到  " + equipSyn.getId());
            return;
        }

        List<Long> eqs = message.getEquipIdsList();
        //判断装备材料
        List<UnrealEquipItem> equips = new ArrayList<>();
        List<Long> needDec = new ArrayList<>();
        int part = 0;
        if (equipSyn.getNeedEquip() != null && equipSyn.getNeedEquip().size() > 0) {
            int needEquipId = equipSyn.getNeedEquip().get(0);
            for (Long eid : eqs) {
                if (!player.getUnrealEquipBaseInfo().getUnrealEquipItemList().containsKey(eid)) {
                    for (UnrealEquipPart holyEquipPart : player.getUnrealEquipBaseInfo().getUnrealEquipPartList().values()) {
                        if (holyEquipPart.getEquipItem() != null) {
                            if (holyEquipPart.getEquipItem().getId() == eid) {
                                if (holyEquipPart.getEquipItem().getItemModelId() != needEquipId) {
                                    logger.error("合成目标ID  和 材料装备ID 不一致   "
                                            + holyEquipPart.getEquipItem().getItemModelId() + " id " + id);
                                    return;
                                }
                                part = holyEquipPart.getPart();
                                break;
                            }
                        }
                    }
                } else {
                    UnrealEquipItem holyEquipItem = player.getUnrealEquipBaseInfo().getUnrealEquipItemList().get(eid);
                    if (holyEquipItem.getItemModelId() != needEquipId) {
                        logger.error("合成目标ID  和 材料装备ID 不一致   "
                                + holyEquipItem.getItemModelId() + " id " + id);
                        return;
                    }
                    needDec.add(eid);
                    equips.add(holyEquipItem);
                }
            }
            int count = 0;
            if (part > 0) {
                count = 1;
            }
            count += equips.size();
            if (count < equipSyn.getNeedEquip().get(1)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.MaterialNotEnough);
                return;
            }
        }
        //判断其他材料
        int needItemId = 0;
        int num = 0;
        List<UnrealEquipMessage.UnrealSyncItem> items = message.getItemListList();
        HashMap<Item, Integer> costItems = new HashMap<>();
        if (equipSyn.getNeedItem() != null && equipSyn.getNeedItem().size() > 0) {
            needItemId = equipSyn.getNeedItem().get(0);
            int needNum = equipSyn.getNeedItem().get(1);
            if (items.size() <= 0) {
                logger.error("合成需要的材料 客户端没发下来  {} ", equipSyn.getNeedItem());
                return;
            }

            for (UnrealEquipMessage.UnrealSyncItem unrealSyncItem : items) {
                Item item = Manager.backpackManager.manager().getItemById(player, unrealSyncItem.getUid());
                if (item == null) {
                    logger.error("材料没背包没找到： {} ", unrealSyncItem.getUid());
                    return;
                }
                if (item.getItemModelId() != needItemId) {
                    logger.error("材料ID对不上needItemId： {} item.getItemModelId() {}", needItemId, item.getItemModelId());
                    return;
                }
                if (item.getNum() < unrealSyncItem.getNum()) {
                    logger.error("材料不足  ItemModelId  {} Uid {} ", item.getItemModelId(), unrealSyncItem.getUid());
                    return;
                }
                costItems.put(item, unrealSyncItem.getNum());
                num += unrealSyncItem.getNum();
            }
            if (num != needNum) {
                logger.error("材料数量对不上号 client num  {}  配置数量 {}  ", num, needNum);
                return;
            }
        }

        //扣除材料
        long actionId = IDConfigUtil.getLogId();
        for (UnrealEquipItem e : equips) {
            player.getUnrealEquipBaseInfo().getUnrealEquipItemList().remove(e.getId());
            writeHolyEquipItemLogAndBI(player, e, 1, 0, ItemChangeReason.UnrealEquipCompoundDec);
        }
        for (Map.Entry<Item, Integer> itemIntegerEntry : costItems.entrySet()) {
            Item item = itemIntegerEntry.getKey();
            Manager.backpackManager.manager().onRemoveItem(player, item, itemIntegerEntry.getValue(),
                    ItemChangeReason.HolyEquipCompoundDec, actionId);
        }

        int equiId = equipSyn.getId();
        UnrealEquipItem item = (UnrealEquipItem) Item.createItem(equiId, 1, false);

        if (part > 0) {
            UnrealEquipPart equipPart = player.getUnrealEquipBaseInfo().getUnrealEquipPartList().get(part);
            if (equipPart == null) {
                logger.error("没有该部位 " + part);
                return;
            }
            //道具删除
            writeHolyEquipItemLogAndBI(player, equipPart.getEquipItem(), 1, 0, ItemChangeReason.UnrealEquipCompoundDec);
            equipPart.setEquipItem(item);
            //新增道具
            writeHolyEquipItemLogAndBI(player, equipPart.getEquipItem(), 0, 1, ItemChangeReason.UnrealEquipCompoundGet);
            refreshPart(player, equipPart);
            sendDeleteItemToClient(player, needDec, ItemChangeReason.UnrealEquipCompoundDec);
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.UnrealEquip);
            RoleGrowLog.create(player, GrowType.holyEquip_synthesis, targetBean, 0, 0);
            RoleGrowLog.create(player, GrowType.holyEquipInlay, targetBean, 0, 0);
        } else {
            Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.UnrealEquipCompoundGet, actionId);
            sendDeleteItemToClient(player, needDec, ItemChangeReason.UnrealEquipCompoundDec);
            RoleGrowLog.create(player, GrowType.holyEquip_synthesis, targetBean, 0, 0);
        }
    }

    @Override
    public void soulEquipInlay(Player player, long uid) {

        //幻装穿戴
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.HolyEquip)) {
            return;
        }
        if (!player.getUnrealEquipBaseInfo().getUnrealEquipItemList().containsKey(uid)) {
            logger.info("背包中没有该幻  + " + uid);
            return;
        }
        UnrealEquipItem unrealEquipItem = player.getUnrealEquipBaseInfo().getUnrealEquipItemList().get(uid);
        Cfg_Equip_Bean equip_bean = CfgManager.getCfg_Equip_Container().getValueByKey(unrealEquipItem.getItemModelId());
        int part = equip_bean.getPart();
        if (!player.getUnrealEquipBaseInfo().getUnrealEquipPartList().containsKey(equip_bean.getPart())) {
            logger.error("没有该部位   + " + equip_bean.getPart());
            return;
        }

        if (player.getLevel() < equip_bean.getLevel()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.EquipWearLevelNot);
            return;
        }
        UnrealEquipPart unrealEquipPart = player.getUnrealEquipBaseInfo().getUnrealEquipPartList().get(part);
        if (!equip_bean.getGender().contains((int) player.getCareer()) && !equip_bean.getGender().contains(9)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.UseItemCareerLimit);
            return;
        }
        if (unrealEquipPart.getPart() != part) {
            logger.error("替换部位不对 +  " + part);
            return;
        }
        sendInlayResult(player, unrealEquipPart, unrealEquipItem, uid);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.UnrealEquip);
        // Manager.controlManager.operate(player, FunctionVariable.WornHolyEquip, 1);
        //Manager.controlManager.operate(player, FunctionVariable.EquipHolyTypeWorn, 0);

    }

    private void sendInlayResult(Player player, UnrealEquipPart unrealEquipPart, UnrealEquipItem unrealEquipItem, long uid) {
        UnrealEquipItem partEquip = unrealEquipPart.getEquipItem();
        if (partEquip != null) {
            player.getUnrealEquipBaseInfo().getUnrealEquipItemList().put(partEquip.getId(), partEquip);
            List<UnrealEquipItem> holyEquipItems = new ArrayList<>();
            holyEquipItems.add(partEquip);
            sendAddItemToClient(player, holyEquipItems, ItemChangeReason.UnrealInlayReasonGet);
        }
        player.getUnrealEquipBaseInfo().getUnrealEquipItemList().remove(uid);
        List<Long> uids = new ArrayList<>();
        uids.add(uid);

        unrealEquipPart.setEquipItem(unrealEquipItem);
        refreshPart(player, unrealEquipPart);

        sendDeleteItemToClient(player, uids, ItemChangeReason.UnrealInlayReasonGet);
    }

    private void refreshPart(Player player, UnrealEquipPart unrealEquipPart) {
        UnrealEquipMessage.ResInlayUnrealReuslt.Builder msg = UnrealEquipMessage.ResInlayUnrealReuslt.newBuilder();
        UnrealEquipMessage.UnrealEquipPart.Builder messagePart = UnrealEquipMessage.UnrealEquipPart.newBuilder();
        messagePart.setPart(unrealEquipPart.getPart());
        UnrealEquipMessage.UnrealEquipItem.Builder messageItem = UnrealEquipMessage.UnrealEquipItem.newBuilder();
        messageItem.setItemId(unrealEquipPart.getEquipItem().getId());
        messageItem.setItemModelId(unrealEquipPart.getEquipItem().getItemModelId());
        messageItem.setIsbind(unrealEquipPart.getEquipItem().isBind());
        messagePart.setUnrealEquipItem(messageItem.build());
        msg.setUnrealPart(messagePart.build());
        MessageUtils.send_to_player(player, UnrealEquipMessage.ResInlayUnrealReuslt.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

}
