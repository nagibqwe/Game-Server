package common.holyEquip;

import com.data.*;
import com.data.bean.*;
import com.data.container.Cfg_Equip_holy_type_Container;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.equip.struct.EquipDefine;
import com.game.equip.struct.EquipPart;
import com.game.holyEquip.log.HolyEquipLog;
import com.game.holyEquip.script.IHolyEquipScript;
import com.game.holyEquip.struct.HolyEquipItem;
import com.game.holyEquip.struct.HolyEquipPart;
import com.game.holyEquip.struct.HolyEquipDefine;
import com.game.log.LogDataManager;
import com.game.log.db.RoleGrowLog;
import com.game.log.grow.GrowType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.HolyEquipMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by 542 on 2019/10/25.
 */
public class HolyEquipScript implements IHolyEquipScript, IScript {

    private final static Logger logger = LogManager.getLogger(HolyEquipScript.class);

    @Override
    public int getId() {
        return ScriptEnum.HolyEquipScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    private void initPart(Player player) {
        if (player.getHolyEquipBaseInfo().getHolyEquipPartList().size() <= 0) {
            for (int i = HolyEquipDefine.HolyPartMin; i <= HolyEquipDefine.HolyPartMax; i++) {
                HolyEquipPart part = new HolyEquipPart();
                part.setPart(i);
                part.setPartLevel(0);
                part.setEquipItem(null);
                player.getHolyEquipBaseInfo().getHolyEquipPartList().put(i, part);
            }
        }
        if (player.getHolyEquipBaseInfo().getHolySoulInfoList().size() <= 0) {
            for (ReadArray<Integer> holySoul : Global.Equip_Holy_att_item.getValuees()) {
                player.getHolyEquipBaseInfo().getHolySoulInfoList().put(holySoul.get(0), 0);
            }
        }
    }

    /**
     * 上线初始化
     */
    public void onLine(Player player) {
        initPart(player);
        HolyEquipMessage.ResOnlineInit.Builder msg = HolyEquipMessage.ResOnlineInit.newBuilder();
        HolyEquipMessage.HolyEquipItem.Builder messageItem;
        //背包信息
        for (HolyEquipItem holyEquipItem : player.getHolyEquipBaseInfo().getHolyEquipItemList().values()) {
            messageItem = HolyEquipMessage.HolyEquipItem.newBuilder();
            messageItem.setItemId(holyEquipItem.getItemModelId());
            messageItem.setUid(holyEquipItem.getId());
            messageItem.setIsBind(holyEquipItem.isBind());
            msg.addHolyBagItemList(messageItem.build());
        }

        //部位信息
        for (HolyEquipPart holyEquipPart : player.getHolyEquipBaseInfo().getHolyEquipPartList().values()) {
            HolyEquipMessage.HolyEquipPart.Builder messagePart = HolyEquipMessage.HolyEquipPart.newBuilder();
            messagePart.setPart(holyEquipPart.getPart());
            messagePart.setLevel(holyEquipPart.getPartLevel());
            if (holyEquipPart.getEquipItem() != null) {
                messageItem = HolyEquipMessage.HolyEquipItem.newBuilder();
                messageItem.setUid(holyEquipPart.getEquipItem().getId());
                messageItem.setItemId(holyEquipPart.getEquipItem().getItemModelId());
                messageItem.setIsBind(holyEquipPart.getEquipItem().isBind());
                messagePart.setHolyEquipItem(messageItem.build());
            }
            msg.addHolyEquipPartList(messagePart.build());
        }
        //圣魂信息
        for (int key : player.getHolyEquipBaseInfo().getHolySoulInfoList().keySet()) {
            HolyEquipMessage.HolySoulInfo.Builder messageSoul = HolyEquipMessage.HolySoulInfo.newBuilder();
            int value = player.getHolyEquipBaseInfo().getHolySoulInfoList().get(key);
            messageSoul.setItemId(key);
            messageSoul.setUseNum(value);
            msg.addHolySoulInfoList(messageSoul.build());
        }
        MessageUtils.send_to_player(player, HolyEquipMessage.ResOnlineInit.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 创建
     */
    public Item createHolyEquip(int id) {
        Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(id);
        if (bean == null) {
            logger.error("Cfg_Equip_Bean  为空  + " + id);
            return null;
        }
        HolyEquipItem holyEquipItem = new HolyEquipItem();
        holyEquipItem.setItemModelId(id);
        holyEquipItem.setPart(bean.getPart());
        holyEquipItem.setGrade(bean.getGrade());
        holyEquipItem.setColor(bean.getQuality());
        holyEquipItem.setHolySuitType(bean.getHolySuitType());
        //TODO 职业改成多种，老玩家数据需要上线时做兼容处理
        // holyEquipItem.setGenders(getGenders(bean.getGender()));
        int suitID = bean.getGrade() * 10000 + bean.getQuality() * 100 + bean.getHolySuitType();
        holyEquipItem.setSuit(suitID);
        return holyEquipItem;
    }


    public boolean canAddHolyEquipBag(Player player, List<Item> itemList) {

        if (itemList == null || itemList.size() <= 0) {
            logger.error("传入的道具为空！");
            return false;
        }
        int needResolve = itemList.size();
        int nowSize = player.getHolyEquipBaseInfo().getHolyEquipItemList().size() + itemList.size();
        if (nowSize > Global.Equip_Holy_bag) {
            int quality = player.getHolyEquipBaseInfo().getQuilty();
            int grade = player.getHolyEquipBaseInfo().getGrade();
            int canResoleCount = 0;
            for (HolyEquipItem holyEquipItem : player.getHolyEquipBaseInfo().getHolyEquipItemList().values()) {
                if (quality >= holyEquipItem.getColor() && grade >= holyEquipItem.getGrade()) {
                    canResoleCount++;
                    if (canResoleCount >= needResolve) {
                        return true;
                    }
                }
            }
            return false;
        } else
            return true;
    }

    @Override
    public int getSpiritNum(Player player, int grade, int quality, int count) {
        int num = 0;
        for (HolyEquipPart holyEquipPart : player.getHolyEquipBaseInfo().getHolyEquipPartList().values()) {
            if (holyEquipPart.getEquipItem() == null)
                continue;
            Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(holyEquipPart.getEquipItem().getItemModelId());
            if (bean == null) {
                continue;
            }
            if (bean.getGrade() >= grade && bean.getQuality() >= quality) {
                num++;
            }
        }
        return num;
    }

    /**
     * 添加
     */
    public boolean addHolyEquip(Player player, Item item, int reason) {
        if (item == null) {
            logger.error("传入的道具为空！");
            return false;
        }
        if (item instanceof HolyEquipItem) {
            HolyEquipItem holyEquipItem = (HolyEquipItem) item;
            autoResolve(player);
            if (player.getHolyEquipBaseInfo().getHolyEquipItemList().size() >= Global.Equip_Holy_bag) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.HolyBagIsfull);
                logger.info("圣装背包已达到上线！ 超出的圣装将分解");
                return false;
            }
            player.getHolyEquipBaseInfo().getHolyEquipItemList().put(holyEquipItem.getId(), holyEquipItem);
            List<HolyEquipItem> holyEquipItems = new ArrayList<>();
            holyEquipItems.add(holyEquipItem);
            sendAddItemToClient(player, holyEquipItems, reason);
            writeHolyEquipItemLogAndBI(player, holyEquipItem, 0, 1, reason);
            return true;
        }
        return false;
    }

    private void writeHolyEquipItemLogAndBI(Player player, HolyEquipItem item, int oldNum, int afterNum, int reason) {
        try {
            int change = afterNum - oldNum;
            writeHolyEquipItemLog(player, item, change, reason);
            Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
            int changeType = 0;
            if (change > 0) {
                changeType = 1;
            }
            LogDataManager.instance.onItemChange(player.getCreateServerId(), model.getId(), model.getType(), model.getName(), oldNum, afterNum);
            Manager.biManager.getScript().biItem(player, item.getId(), model.getType(), model.getColor(), model.getId(), model.getName(), Math.abs(change), afterNum, reason, 0, changeType, player.getCurGps().getModelId());
            Manager.biManager.get4399Script().itemBiTo4399(player, reason, model.getId(), oldNum, afterNum);
        } catch (Exception e) {
            logger.error("writeHolyEquipItemLogAndBI", e);
        }
    }

    private void writeHolyEquipItemLog(Player player, HolyEquipItem item, int change, int reason) {
        HolyEquipLog log = new HolyEquipLog();
        log.setPlayerInfo(player.getPlatformName()
                , player.getCreateServerId()
                , player.getUserId()
                , player.getId()
                , player.getName());
        log.setColor(item.getColor());
        log.setModelItemId(item.getItemModelId());
        log.setPart(item.getPart());
        log.setReason(reason);
        log.setGrade(item.getGrade());
        log.setChangeNum(change);
        LogService.getInstance().execute(log);
    }

    public boolean delHolyEquip(Player player, Item item, int reason) {
        if (item == null) {
            logger.error("传入的道具为空！");
            return false;
        }
        if (item instanceof HolyEquipItem) {
            HolyEquipItem holyEquipItem = (HolyEquipItem) item;
            if (player.getHolyEquipBaseInfo().getHolyEquipItemList().containsKey(item.getId())) {
                player.getHolyEquipBaseInfo().getHolyEquipItemList().remove(item.getId());
                List<Long> holyEquipItems = new ArrayList<>();
                holyEquipItems.add(holyEquipItem.getId());
                sendDeleteItemToClient(player, holyEquipItems, reason);
                return true;
            }
        }
        return false;
    }

    /**
     * 分解
     */
    public void resolveHolyEquip(Player player, List<Long> uids) {
        boolean isAllInPack = true;
        for (Long uid : uids) {
            if (!player.getHolyEquipBaseInfo().getHolyEquipItemList().containsKey(uid)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulResolveFail, uid + "");
                isAllInPack = false;
                break;
            }
        }
        if (isAllInPack) {
            long logId = IDConfigUtil.getLogId();
            int holyEquipIntegral = 0;
            for (long uid : uids) {
                HolyEquipItem resoulveItem = player.getHolyEquipBaseInfo().getHolyEquipItemList().get(uid);
                holyEquipIntegral += resolve(player, resoulveItem);
                player.getHolyEquipBaseInfo().getHolyEquipItemList().remove(uid);
                writeHolyEquipItemLogAndBI(player, resoulveItem, 1, 0, ItemChangeReason.HolyEquipResolveDec);
                Manager.backpackManager.manager().writeItemLogAndBI(player, 1, 0, resoulveItem, ItemChangeReason.HolyEquipResolveDec, logId);
            }
            if (holyEquipIntegral > 0) {
                Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.HolyEquipIntegral, holyEquipIntegral,
                        ItemChangeReason.HolyEquipResolveGet, logId);
            }
            sendDeleteItemToClient(player, uids, ItemChangeReason.HolyEquipResolveDec);
        }
    }

    /**
     * 自动分解设置
     */
    public void ReqSetAutoResolve(Player player, HolyEquipMessage.ReqSetAutoResolve message) {
        player.getHolyEquipBaseInfo().setAutoResolve(message.getIsAuto());
        player.getHolyEquipBaseInfo().setQuilty(message.getQuality());
        player.getHolyEquipBaseInfo().setGrade(message.getGrade());
    }

    /**
     * 自动分解
     */
    private void autoResolve(Player player) {
        if (player.getHolyEquipBaseInfo().getHolyEquipItemList().size() < Global.Equip_Holy_bag)
            return;
        boolean isAuto = player.getHolyEquipBaseInfo().getIsAutoResolve();
        if (!isAuto) return;
        int quality = player.getHolyEquipBaseInfo().getQuilty();
        int grade = player.getHolyEquipBaseInfo().getGrade();
        List<Long> uids = new ArrayList<>();
        int holyEquipIntegral = 0;
        for (HolyEquipItem holyEquipItem : player.getHolyEquipBaseInfo().getHolyEquipItemList().values()) {
            if (quality >= holyEquipItem.getColor() && grade >= holyEquipItem.getGrade()) {
                uids.add(holyEquipItem.getId());
                holyEquipIntegral += resolve(player, holyEquipItem);
                player.getHolyEquipBaseInfo().getHolyEquipItemList().remove(holyEquipItem.getId());
            }
        }
        if (holyEquipIntegral > 0) {
            Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.HolyEquipIntegral, holyEquipIntegral,
                    ItemChangeReason.HolyEquipResolveGet, IDConfigUtil.getLogId());
        }
        if (uids.size() > 0)
            sendDeleteItemToClient(player, uids, ItemChangeReason.HolyEquipResolveDec);
    }

    private int resolve(Player player, HolyEquipItem resoulveItem) {
        int holyEquipIntegral = 0;
        Cfg_Equip_holy_resolve_Bean bean = CfgManager.getCfg_Equip_holy_resolve_Container().
                getValueByKey(resoulveItem.getItemModelId());
        if (bean != null) {
            if (bean.getItem() != null && bean.getItem().size() > 0) {
                if (bean.getItem().get(0) == ItemCoinType.HolyEquipIntegral) {
                    holyEquipIntegral = bean.getItem().get(1);
                }
            }
            if (bean.getHolyEquip() != null && bean.getHolyEquip().size() > 0) {
                for (ReadArray<Integer> itemBack : bean.getHolyEquip().getValuees()) {
                    Item item = Item.createItem(itemBack.get(0), itemBack.get(1), false);
                    if (!Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.
                            HolyEquipResolveGet, item.getItemModelId())) {
                        Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                                MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT
                                , Collections.singletonList(item), ItemChangeReason.HolyEquipResolveGet, item.getItemModelId());
                    }
                }
            }
        }
        return holyEquipIntegral;
    }


    /**
     * 强化部位
     */
    public void intensifyHolyPart(Player player, int part) {
        if (part > HolyEquipDefine.HolyPartMax || part < HolyEquipDefine.HolyPartMin) {
            logger.info("强化部位不正确！ " + part);
            return;
        }
        HolyEquipPart equipPart = player.getHolyEquipBaseInfo().getHolyEquipPartList().get(part);
        if (equipPart.getEquipItem() == null) {
            logger.info("部位未穿戴装备 " + part);
            return;
        }

        int intenssfyID = (part - 101) % 11 * 10000 + equipPart.getPartLevel();
        Cfg_Equip_holy_levelup_Bean bean = CfgManager.getCfg_Equip_holy_levelup_Container().getValueByKey(intenssfyID);
        if (bean == null) {
            logger.info("Cfg_Equip_holy_levelup_Bean 未找到   " + intenssfyID);
            return;
        }
        if (Manager.currencyManager.manager().canDecItemCoin(player, bean.getCost(), ItemCoinType.HolyEquipIntegral)) {
            Manager.currencyManager.manager().onDecItemCoin(player, bean.getCost(),
                    ItemChangeReason.ImmortalLevelUpDec, bean.getId(), ItemCoinType.HolyEquipIntegral);

            int newLevel = equipPart.getPartLevel() + 1;
            equipPart.setPartLevel(newLevel);
            sendIntensifyHolyResult(player, equipPart);
//            writeHolyEquipItemLog( player,  equipPart.getEquipItem(), 0,  ItemCoinType.HolyEquipIntegral);
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.HolyEquip);

            Manager.controlManager.deal().operate(player, FunctionVariable.WornHolyEquip_strengthen_Grade, 1);
            //
            Cfg_Equip_Bean equip_bean = CfgManager.getCfg_Equip_Container().getValueByKey(equipPart.getEquipItem().getItemModelId());
            RoleGrowLog.create(player, GrowType.holyEquip_intensify, equip_bean, newLevel, 0);
        } else {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.HolyMoneyNotEnough);
            logger.info("圣装精碎不足   " + bean.getCost());
            return;
        }
    }

    private void sendIntensifyHolyResult(Player player, HolyEquipPart equipPart) {
        HolyEquipMessage.ResIntensifyHolyPartResult.Builder msg = HolyEquipMessage.ResIntensifyHolyPartResult.newBuilder();
        HolyEquipMessage.HolyEquipPart.Builder messagePart = HolyEquipMessage.HolyEquipPart.newBuilder();
        messagePart.setPart(equipPart.getPart());
        messagePart.setLevel(equipPart.getPartLevel());
        if (equipPart.getEquipItem() != null) {
            HolyEquipMessage.HolyEquipItem.Builder messageItem = HolyEquipMessage.HolyEquipItem.newBuilder();
            messageItem.setUid(equipPart.getEquipItem().getId());
            messageItem.setItemId(equipPart.getEquipItem().getItemModelId());
            messageItem.setIsBind(equipPart.getEquipItem().isBind());
            messagePart.setHolyEquipItem(messageItem.build());
        }
        msg.setHolyPart(messagePart.build());
        MessageUtils.send_to_player(player, HolyEquipMessage.ResIntensifyHolyPartResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    /**
     * //使用圣魂
     */
    public void useHolySoul(Player player, int itemID, int num) {
        int canUseNum = 0;
        if (player.getHolyEquipBaseInfo().getHolySoulInfoList().containsKey(itemID)) {
            int curNum = player.getHolyEquipBaseInfo().getHolySoulInfoList().get(itemID);
            for (ReadArray<Integer> holySoulMax : Global.Equip_Holy_att_item.getValuees()) {
                if (itemID == holySoulMax.get(0)) {
                    if (curNum >= holySoulMax.get(1)) {
                        MessageUtils.notify_player(player, Notify.ERROR, MessageString.HolyUseMax);
                        logger.info("使用圣魂数量已达到上线   " + holySoulMax.get(1));
                        return;
                    } else {
                        canUseNum = (holySoulMax.get(1) - curNum) > num ? num : (holySoulMax.get(1) - curNum);
                    }
                }
            }

            if (!Manager.backpackManager.manager().canDeleteItemNum(player, itemID, canUseNum)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.HolySoulNotEnough);
                logger.info("背包圣魂不足   " + canUseNum);
                return;
            }

            int nowUseNum = curNum + canUseNum;
            player.getHolyEquipBaseInfo().getHolySoulInfoList().put(itemID, nowUseNum);
            //从新计算属性----
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.HolyEquip);
            //发消息给客户端
            Manager.backpackManager.manager().onRemoveItem(player, itemID, canUseNum, ItemChangeReason.UseHolySoulDec, itemID);
            HolyEquipMessage.ResUseHolySoulResult.Builder msg = HolyEquipMessage.ResUseHolySoulResult.newBuilder();
            HolyEquipMessage.HolySoulInfo.Builder soulInfo = HolyEquipMessage.HolySoulInfo.newBuilder();
            soulInfo.setItemId(itemID);
            soulInfo.setUseNum(nowUseNum);
            msg.setHolySoulData(soulInfo.build());
            MessageUtils.send_to_player(player, HolyEquipMessage.ResUseHolySoulResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            RoleGrowLog.create(player, GrowType.holyEquip_useSoul, 0, itemID, curNum, nowUseNum, null);
        }
    }

    /**
     * 合成
     */
    public void compoundSoul(Player player, HolyEquipMessage.ReqCompoundHoly message) {
        List<Long> eqs = message.getEquipIdsList();
        if (eqs.size() != 2) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MaterialNotEnough);
            logger.info("合成装备材料 数量 不匹配   " + eqs.size());
            return;
        }
        int id = message.getId();
        Cfg_Equip_holy_synthesis_Bean equipSyn = CfgManager.getCfg_Equip_holy_synthesis_Container().getValueByKey(id);
        if (equipSyn == null) {
            logger.info("Cfg_Equip_holy_synthesis_Bean  未找到  " + id);
            return;
        }
        Cfg_Equip_Bean targetBean = CfgManager.getCfg_Equip_Container().getValueByKey(equipSyn.getEquip_ID());
        if (targetBean == null) {
            logger.info("getCfg_Equip_Container  未找到  " + equipSyn.getEquip_ID());
            return;
        }
        List<HolyEquipItem> equips = new ArrayList<>();
        List<Long> needDec = new ArrayList<>();
        int part = 0;
        for (Long eid : eqs) {
            if (!player.getHolyEquipBaseInfo().getHolyEquipItemList().containsKey(eid)) {
                for (HolyEquipPart holyEquipPart : player.getHolyEquipBaseInfo().getHolyEquipPartList().values()) {
                    if (holyEquipPart.getEquipItem() != null) {
                        if (holyEquipPart.getEquipItem().getId() == eid) {
                            if (holyEquipPart.getEquipItem().getItemModelId() != id) {
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
                HolyEquipItem holyEquipItem = player.getHolyEquipBaseInfo().getHolyEquipItemList().get(eid);
                if (holyEquipItem.getItemModelId() != id) {
                    logger.error("合成目标ID  和 材料装备ID 不一致   "
                            + holyEquipItem.getItemModelId() + " id " + id);
                    return;
                }
                needDec.add(eid);
                equips.add(holyEquipItem);
            }
        }
        if (equips.size() <= 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MaterialNotEnough);
            return;
        }
        //计算概率
        long actionId = IDConfigUtil.getLogId();
        int itemModeId = 0;
        int itemNumber = 0;
        if (equipSyn.getJoin_item() != null && equipSyn.getJoin_item().size() > 0) {
            itemModeId = equipSyn.getJoin_item().get(0);
            itemNumber = equipSyn.getJoin_item().get(1);
            if (!Manager.backpackManager.manager().canDeleteItemNum(player, itemModeId, itemNumber)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.MaterialNotEnough);
                return;
            }
        }
        //移除道具
        boolean isBind = false;
        for (HolyEquipItem e : equips) {
            player.getHolyEquipBaseInfo().getHolyEquipItemList().remove(e.getId());
            writeHolyEquipItemLogAndBI(player, e, 1, 0, ItemChangeReason.HolyEquipCompoundDec);
            Manager.backpackManager.manager().writeItemLogAndBI(player, 1, 0, e, ItemChangeReason.HolyEquipCompoundDec, actionId);
        }
        if (equipSyn.getJoin_item() != null && equipSyn.getJoin_item().size() == 2) {
            if (!isBind) {
                int bindNum = Manager.backpackManager.manager().getItemNum(player, itemModeId, true);
                isBind = bindNum > 0 ? true : false;
            }
            Manager.backpackManager.manager().onRemoveItem(player, itemModeId, itemNumber, ItemChangeReason.HolyEquipCompoundDec, actionId);
        }
        int equiId = equipSyn.getEquip_ID();
        HolyEquipItem item = (HolyEquipItem) Item.createItem(equiId, 1, isBind);
        if (part > 0) {
            HolyEquipPart equipPart = player.getHolyEquipBaseInfo().getHolyEquipPartList().get(part);
            if (equipPart == null) {
                logger.error("没有该部位 " + part);
                return;
            }
            //道具删除
            Manager.backpackManager.manager().writeItemLogAndBI(player, 1, 0, equipPart.getEquipItem(), ItemChangeReason.HolyEquipCompoundDec, actionId);
            writeHolyEquipItemLogAndBI(player, equipPart.getEquipItem(), 1, 0, ItemChangeReason.HolyEquipCompoundDec);
            equipPart.setEquipItem(item);
            //新增道具
            Manager.backpackManager.manager().writeItemLogAndBI(player, 0, 1, equipPart.getEquipItem(), ItemChangeReason.HolyEquipCompoundGet, actionId);
            writeHolyEquipItemLogAndBI(player, equipPart.getEquipItem(), 0, 1, ItemChangeReason.HolyEquipCompoundGet);
            refreshPart(player, equipPart);
            sendDeleteItemToClient(player, needDec, ItemChangeReason.HolyEquipCompoundDec);
            setFashionForPartStage(player, part);
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.HolyEquip);
            if (part == EquipDefine.HolyEquip_DouXin) {//如果是穿戴斗心
                Manager.controlManager.operate(player, FunctionVariable.WornHolyEquip_activation_level_cool, item.getGrade(), item.getColor(), 1);
            }
            RoleGrowLog.create(player, GrowType.holyEquip_synthesis, targetBean, 0, 0);
            RoleGrowLog.create(player, GrowType.holyEquipInlay, targetBean, equipPart.getPartLevel(), 0);
        } else {
            Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.HolyEquipCompoundGet, actionId);
            sendDeleteItemToClient(player, needDec, ItemChangeReason.HolyEquipCompoundGet);
            RoleGrowLog.create(player, GrowType.holyEquip_synthesis, targetBean, 0, 0);
        }

        Manager.controlManager.operate(player, FunctionVariable.Synthesis_level_Holydress, item.getGrade(), 1);
    }


    /**
     * 镶嵌
     */
    public void holyEquipInlay(Player player, long uid) {
        //圣装穿戴
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.HolyEquip)) {
            return;
        }
        if (!player.getHolyEquipBaseInfo().getHolyEquipItemList().containsKey(uid)) {
            logger.info("背包中没有该圣装  + " + uid);
            return;
        }
        HolyEquipItem holyEquipItem = player.getHolyEquipBaseInfo().getHolyEquipItemList().get(uid);
        Cfg_Equip_Bean equip_bean = CfgManager.getCfg_Equip_Container().getValueByKey(holyEquipItem.getItemModelId());
        int part = equip_bean.getPart();
        if (!player.getHolyEquipBaseInfo().getHolyEquipPartList().containsKey(equip_bean.getPart())) {
            logger.error("没有该部位   + " + equip_bean.getPart());
            return;
        }
        if (player.getLevel() < equip_bean.getLevel()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.EquipWearLevelNot);
            return;
        }
        Cfg_Equip_holy_type_Bean bean = getHolyEquipTypeBean(part);
        if (bean == null)
            return;

        int openDay = TimeUtils.getOpenServerDay();
        if (openDay < bean.getOpenFunction()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.EquipHolyNotice);
            return;
        }
        // int globalIndex = part - HolyEquipDefine.HolyPartMin;
        // if (globalIndex  < 0 || globalIndex >= Global.Equip_Holy_Open_Level.size()){
        //     logger.error("索引位置不对 "   +   globalIndex);
        //     return;
        // }
        // int neePartLevel =  Global.Equip_Holy_Open_Level.get(globalIndex);
        // if (player.getLevel() < neePartLevel){
        //     MessageUtils.notify_player(player, Notify.ERROR, MessageString.EquipHolyNotice);
        //     return;
        // }
        HolyEquipPart holyEquipPart = player.getHolyEquipBaseInfo().getHolyEquipPartList().get(part);

        if (!equip_bean.getGender().contains((int) player.getCareer())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.UseItemCareerLimit);
            return;
        }

        if (holyEquipItem.getPart() != part) {
            logger.error("替换部位不对 +  " + part);
            return;
        }
        sendInlayResult(player, holyEquipPart, holyEquipItem, uid);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.HolyEquip);
        Manager.controlManager.operate(player, FunctionVariable.WornHolyEquip, 1);
        Manager.controlManager.operate(player, FunctionVariable.EquipHolyTypeWorn, 0);
        if (part == EquipDefine.HolyEquip_DouXin) {//如果是穿戴斗心
            Manager.controlManager.operate(player, FunctionVariable.WornHolyEquip_activation_level_cool, holyEquipItem.getGrade(), holyEquipItem.getColor(), 1);
        }
        Manager.controlManager.operate(player, FunctionVariable.Holydress_sum, 0);
        setFashionForPartStage(player, part);
        //装备BI
        Manager.biManager.getScript().biEquip(player, 1, GrowType.holyEquipInlay.getType(), equip_bean.getPart(), equip_bean.getId(), equip_bean.getName(), equip_bean.getDiamond_Number(), equip_bean.getGrade(), equip_bean.getQuality(), holyEquipPart.getPartLevel(), equip_bean.getBind(), 0, 0, 0, 0, 0, 0);
    }

    private void sendInlayResult(Player player, HolyEquipPart holyEquipPart, HolyEquipItem holyEquipItem, long uid) {
        HolyEquipItem partEquip = holyEquipPart.getEquipItem();
        if (partEquip != null) {
            player.getHolyEquipBaseInfo().getHolyEquipItemList().put(partEquip.getId(), partEquip);
            List<HolyEquipItem> holyEquipItems = new ArrayList<>();
            holyEquipItems.add(partEquip);
            sendAddItemToClient(player, holyEquipItems, ItemChangeReason.HolyInlayReasonGet);
        }
        player.getHolyEquipBaseInfo().getHolyEquipItemList().remove(uid);
        List<Long> uids = new ArrayList<>();
        uids.add(uid);

        holyEquipPart.setEquipItem(holyEquipItem);
        refreshPart(player, holyEquipPart);

        sendDeleteItemToClient(player, uids, ItemChangeReason.HolyInlayReasonGet);
    }

    private void refreshPart(Player player, HolyEquipPart holyEquipPart) {
        HolyEquipMessage.ResInlayHolyReuslt.Builder msg = HolyEquipMessage.ResInlayHolyReuslt.newBuilder();
        HolyEquipMessage.HolyEquipPart.Builder messagePart = HolyEquipMessage.HolyEquipPart.newBuilder();
        messagePart.setPart(holyEquipPart.getPart());
        messagePart.setLevel(holyEquipPart.getPartLevel());
        HolyEquipMessage.HolyEquipItem.Builder messageItem = HolyEquipMessage.HolyEquipItem.newBuilder();
        messageItem.setUid(holyEquipPart.getEquipItem().getId());
        messageItem.setItemId(holyEquipPart.getEquipItem().getItemModelId());
        messageItem.setIsBind(holyEquipPart.getEquipItem().isBind());
        messagePart.setHolyEquipItem(messageItem.build());
        msg.setHolyPart(messagePart.build());
        MessageUtils.send_to_player(player, HolyEquipMessage.ResInlayHolyReuslt.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    private void sendAddItemToClient(Player player, List<HolyEquipItem> itemList, int reason) {
        HolyEquipMessage.ResAddHoly.Builder msg = HolyEquipMessage.ResAddHoly.newBuilder();
        for (HolyEquipItem item : itemList) {
            HolyEquipMessage.HolyEquipItem.Builder holyItem = HolyEquipMessage.HolyEquipItem.newBuilder();
            holyItem.setItemId(item.getItemModelId());
            holyItem.setUid(item.getId());
            holyItem.setIsBind(item.isBind());
            msg.addAddholyitem(holyItem.build());
        }
        msg.setReason(reason);
        MessageUtils.send_to_player(player, HolyEquipMessage.ResAddHoly.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void sendDeleteItemToClient(Player player, List<Long> uids, int reason) {
        HolyEquipMessage.ResDeleteHoly.Builder msg = HolyEquipMessage.ResDeleteHoly.newBuilder();
        msg.setReason(reason);
        msg.addAllDeleteUID(uids);
        MessageUtils.send_to_player(player, HolyEquipMessage.ResDeleteHoly.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    private Cfg_Equip_holy_type_Bean getHolyEquipTypeBean(int part) {
        for (Cfg_Equip_holy_type_Bean bean : Cfg_Equip_holy_type_Container.GetInstance().getValuees()) {
            for (int pos : bean.getParts_list().getValue()) {
                if (pos == part) {
                    return bean;
                }
            }
        }
        return null;
    }


    private void setFashionForPartStage(Player player, int part) {
        Cfg_Equip_holy_type_Bean bean = getHolyEquipTypeBean(part);
        if (bean == null)
            return;
        int index = bean.getId();
        if (part >= HolyEquipDefine.HolySuit_1_Hun_Start && part <= HolyEquipDefine.HolySuit_1_Hun_End) {
            calActiveFashion(player, HolyEquipDefine.HolySuit_1_Hun_Start, HolyEquipDefine.HolySuit_1_Hun_End, 1, index);
            calActiveFashion(player, HolyEquipDefine.HolySuit_1_Hun_Start, HolyEquipDefine.HolySuit_1_Hun_End, 3, index);
        } else if (part >= HolyEquipDefine.HolySuit_1_Po_Start && part <= HolyEquipDefine.HolySuit_1_Po_End) {
            calActiveFashion(player, HolyEquipDefine.HolySuit_1_Po_Start, HolyEquipDefine.HolySuit_1_Po_End, 2, index);
            calActiveFashion(player, HolyEquipDefine.HolySuit_1_Po_Start, HolyEquipDefine.HolySuit_1_Po_End, 4, index);
        } else if (part >= HolyEquipDefine.HolySuit_2_Hun_Start && part <= HolyEquipDefine.HolySuit_2_Hun_End) {
            calActiveFashion(player, HolyEquipDefine.HolySuit_2_Hun_Start, HolyEquipDefine.HolySuit_2_Hun_End, 1, index);
            calActiveFashion(player, HolyEquipDefine.HolySuit_2_Hun_Start, HolyEquipDefine.HolySuit_2_Hun_End, 3, index);
        } else if (part >= HolyEquipDefine.HolySuit_2_Po_Start && part <= HolyEquipDefine.HolySuit_2_Po_End) {
            calActiveFashion(player, HolyEquipDefine.HolySuit_2_Po_Start, HolyEquipDefine.HolySuit_2_Po_End, 2, index);
            calActiveFashion(player, HolyEquipDefine.HolySuit_2_Po_Start, HolyEquipDefine.HolySuit_2_Po_End, 4, index);
        } else if (part >= HolyEquipDefine.HolySuit_3_Hun_Start && part <= HolyEquipDefine.HolySuit_3_Hun_End) {
            calActiveFashion(player, HolyEquipDefine.HolySuit_3_Hun_Start, HolyEquipDefine.HolySuit_3_Hun_End, 1, index);
            calActiveFashion(player, HolyEquipDefine.HolySuit_3_Hun_Start, HolyEquipDefine.HolySuit_3_Hun_End, 3, index);
        } else if (part >= HolyEquipDefine.HolySuit_3_Po_Start && part <= HolyEquipDefine.HolySuit_3_Po_End) {
            calActiveFashion(player, HolyEquipDefine.HolySuit_3_Po_Start, HolyEquipDefine.HolySuit_3_Po_End, 2, index);
            calActiveFashion(player, HolyEquipDefine.HolySuit_3_Po_Start, HolyEquipDefine.HolySuit_3_Po_End, 4, index);
        }
    }


    private void calActiveFashion(Player player, int start, int end, int type, int index) {
        Cfg_Equip_holy_type_Bean type_bean = CfgManager.getCfg_Equip_holy_type_Container().getValueByKey(index);
        if (type_bean == null) {
            return;
        }
        ConcurrentHashMap<Integer, HolyEquipPart> holyEquipPartList = player.getHolyEquipBaseInfo().getHolyEquipPartList();
        boolean isAcitve = true;
        int fashionID = 0;
        for (int i = start; i <= end; i++) {
            HolyEquipPart holyEquipPart = holyEquipPartList.get(i);
            if (holyEquipPart.getEquipItem() == null)
                return;

            if (type == 1) {
                if (holyEquipPart.getEquipItem().getGrade() < type_bean.getHun_need_degree()) {
                    isAcitve = false;
                    return;
                }
                fashionID = type_bean.getHun_fashion_id();
            } else if (type == 2) {
                if (holyEquipPart.getEquipItem().getGrade() < type_bean.getPo_need_degree()) {
                    isAcitve = false;
                    return;
                }
                fashionID = type_bean.getPo_fashion_id();
            } else if (type == 3 || type == 4) {
                if (holyEquipPart.getEquipItem().getGrade() < type_bean.getAwaken_degree() ||
                        holyEquipPart.getEquipItem().getColor() < type_bean.getAwaken_quality()) {
                    isAcitve = false;
                    return;
                }
                fashionID = type == 3 ? type_bean.getAwaken_hun_fashion_id() : type_bean.getAwaken_po_fashion_id();
            }
        }
        if (isAcitve && fashionID > 0) {
            Manager.newFashionManager.deal().addFashionID(player, fashionID);
        }
    }

    //GM清空穿戴圣装
    public void gmclearHolyEuiqp(Player player) {
        for (HolyEquipPart holyEquipPart : player.getHolyEquipBaseInfo().getHolyEquipPartList().values()) {
            if (holyEquipPart.getEquipItem() == null)
                continue;
            Manager.backpackManager.manager().addItem(player, holyEquipPart.getEquipItem().getItemModelId(), 1, false, 0, ItemChangeReason.HolyInlayReasonGet, IDConfigUtil.getId());
            holyEquipPart.setEquipItem(null);
        }

        onLine(player);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.HolyEquip);
    }


    public int getHolyEquipNumForType(Player player, int type) {
        int count = 0;
        for (HolyEquipPart holyEquipPart : player.getHolyEquipBaseInfo().getHolyEquipPartList().values()) {
            if (holyEquipPart.getEquipItem() != null) {
                Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(holyEquipPart.getEquipItem().getItemModelId());
                int suitID = bean.getGrade() * 10000 + bean.getQuality() * 100 + bean.getHolySuitType();
                Cfg_Equip_holy_suit_Bean bean1 = CfgManager.getCfg_Equip_holy_suit_Container().getValueByKey(suitID);
                if (bean == null) {
                    logger.info("配置表不存在 Cfg_Equip_holy_suit_Bean  " + suitID);
                    continue;
                }
                if (bean1.getType() == type) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public int getHolyEquipStrengthenLevel(Player player) {
        int count = 0;
        for (HolyEquipPart holyEquipPart : player.getHolyEquipBaseInfo().getHolyEquipPartList().values()) {
            count += holyEquipPart.getPartLevel();
        }
        return count;
    }

    @Override
    public int getDouxin(Player player, Integer grade, Integer quality) {
        for (HolyEquipPart holyEquipPart : player.getHolyEquipBaseInfo().getHolyEquipPartList().values()) {
            if (holyEquipPart.getPart() == EquipDefine.HolyEquip_DouXin && holyEquipPart.getEquipItem() != null) {//斗心
                HolyEquipItem item = holyEquipPart.getEquipItem();
                if (item.getGrade() >= grade && item.getColor() >= quality) {
                    return 1;
                }
                break;
            }
        }
        return 0;
    }

    @Override
    public int getHolyEquipNum(Player player) {
        int count = 0;
        for (HolyEquipPart holyEquipPart : player.getHolyEquipBaseInfo().getHolyEquipPartList().values()) {
            if (holyEquipPart.getEquipItem() != null) {
                count++;
            }
        }
        return count;
    }
}
