package common.soulbeast;

import com.data.*;
import com.data.bean.*;
import com.data.container.Cfg_SoulBeastsEquip_Container;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemTypeConst;
import com.game.bi.manager.BIDefine;
import com.game.chat.structs.Notify;
import com.game.common.dblog.DbLog;
import com.game.common.dblog.DbLogEnum;
import com.game.log.LogDataManager;
import com.game.log.db.RoleGrowLog;
import com.game.log.grow.ActType;
import com.game.log.grow.GrowType;
import com.game.manager.Manager;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.Skill;
import com.game.soulbeast.log.SoulBeastItemLog;
import com.game.soulbeast.script.ISoulBeast;
import com.game.soulbeast.structs.EquipColorEnum;
import com.game.soulbeast.structs.SoulBeast;
import com.game.soulbeast.structs.SoulBeastEquip;
import com.game.soulbeast.structs.SoulBeastItem;
import com.game.task.structs.TaskHelp;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import game.core.dblog.LogService;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.message.EquipMessage;
import game.message.SoulBeastMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * @author admin
 */
public class SoulBeastScript implements ISoulBeast, IScript {

    private static final Logger logger = LogManager.getLogger(SoulBeastScript.class);

    /**
     * 获取scriptId
     */
    @Override
    public int getId() {
        return ScriptEnum.SoulBeastBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 玩家上线处理
     *
     * @param player
     */
    @Override
    public void online(Player player) {
        if (Manager.controlManager.deal().isOpenFunction(player, FunctionStart.MonsterAF)) {
            if (player.getSoulBeastInfo().getSoulBeasts().isEmpty()) {
                functionOpen(player);
            }
        }
        sendSoulBeastBagInfo(player);
        sendSoulBeastInfo(player);
        sendSoulBeastGrid(player);
    }

    /**
     * 获取神兽技能
     *
     * @param player
     * @return
     */
    public List<Skill> sumAllChildSkill(Player player) {
        List<Skill> skills = new ArrayList<>();
        List<SoulBeast> soulBeasts = Utils.find(player.getSoulBeastInfo().getSoulBeasts().values(), s -> s.isWork());
        for (SoulBeast beast : soulBeasts) {
            Cfg_SoulBeasts_Bean beastsBean = CfgManager.getCfg_SoulBeasts_Container().getValueByKey(beast.getBeastId());
            for (int skillId : beastsBean.getSkill().getValue()) {
                Skill skill = new Skill();
                skill.setSkillId(skillId);
                skills.add(skill);
            }
        }
        return skills;
    }

    /**
     * 请求魂兽出战 或脱战
     *
     * @param player
     * @param soulId 出战魂兽配置Id
     */
    @Override
    public void reqSoulBeastFight(Player player, int soulId) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.MonsterAF)) {
            return;
        }
        SoulBeast beast = player.getSoulBeastInfo().getSoulBeasts().get(soulId);
        if (beast == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.This_Soul_Beast_Not_Exist);
            return;
        }
        if (beast.isWork()) {
            beast.setWork(false);
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.SOUL_BEAST, PlayerAttributeType.Skill);
            sendSoulBeast(player, beast);
            //BI
            Manager.biManager.getScript().biGrow(player, GrowType.soulBeast_goBack.getType(), 0, GrowType.soulBeast_goBack.getAct_type(), soulId, 0, soulId);
            return;
        }

        List<SoulBeast> soulBeasts = Utils.find(player.getSoulBeastInfo().getSoulBeasts().values(), o -> o.isWork());
        if (soulBeasts.size() >= player.getSoulBeastInfo().getSoulGridNum() + Global.BossOld2_PossessionNum) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Max_Fight_Soul_Beast_Num);
            return;
        }
        Cfg_SoulBeasts_Bean beastsBean = CfgManager.getCfg_SoulBeasts_Container().getValueByKey(soulId);
        if (beast.getEquip().size() != beastsBean.getNeedEquip().size()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Soul_Beast_Must_Five_Equip_On_Can_Fight);
            return;
        }
        beast.setWork(true);
        //TODO 不知道为啥要刷新商店
        Manager.shopManager.limitShop().refresh(player);

        sendSoulBeast(player, beast);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.SOUL_BEAST, PlayerAttributeType.Skill);
        Manager.controlManager.operate(player, FunctionVariable.SoulBeastsID, 0);
        Manager.controlManager.operate(player, FunctionVariable.SoulBeastsNum, 0);

        Manager.biManager.getScript().biGrow(player, GrowType.soulBeast_active.getType(), 0, BIDefine.GrowActive, 0, soulId, soulId);

        //发送公告
        if (beastsBean.getNotice() != 0 || beastsBean.getChatchannel() != null) {
            MessageUtils.notify_allOnlinePlayer(beastsBean.getNotice(), beastsBean.getChatchannel(), MessageString.SOULBEASTS_NOTICE1,
                    player.getId() + "", player.getName(), beastsBean.getName(),
                    Utils.makeUrlStr(MessageString.SOULBEASTS_NOTICE1));
        }
    }

    /**
     * 玩家请求穿装备
     *
     * @param player
     * @param soulBeastId
     * @param equipIds
     */
    @Override
    public void reqSoulBeastEquipWear(Player player, int soulBeastId, List<Long> equipIds) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.MonsterAF)) {
            return;
        }
        SoulBeast soulBeast = player.getSoulBeastInfo().getSoulBeasts().get(soulBeastId);
        if (soulBeast == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.This_Soul_Beast_Not_Exist);
            return;
        }
        Cfg_SoulBeasts_Bean bean = CfgManager.getCfg_SoulBeasts_Container().getValueByKey(soulBeastId);
        List<Long> useEquips = new ArrayList<>();
        List<SoulBeastEquip> replaceEquips = new ArrayList<>();
        for (long equipId : equipIds) {
            Item item = player.getSoulBeastInfo().getSoulBeastEquipMap().get(equipId);
            if (!(item instanceof SoulBeastEquip)) {
                continue;
            }
            SoulBeastEquip equip = (SoulBeastEquip) item;
            Cfg_SoulBeastsEquip_Bean equipBean = CfgManager.getCfg_SoulBeastsEquip_Container().getValueByKey(item.getItemModelId());
            ReadArray<Integer> array = bean.getNeedEquip().get(equipBean.getPart() - 1);
            if (equipBean.getQuality() < array.get(1)) {
                String colorDesc = EquipColorEnum.getByColorNum(array.get(1)).getColorDesc();
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Need_More_High_Soul_Beast_Equip, "2&_" + colorDesc);
                continue;
            }
            if (equipBean.getDiamond_Number() < array.get(2)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_EQUIP_WEAR_FIALD);
                continue;
            }
            player.getSoulBeastInfo().getSoulBeastEquipMap().remove(equip.getId());

            SoulBeastEquip replaceEquip = soulBeast.getEquip().put(equipBean.getPart(), equip);
            if (replaceEquip != null) {
                replaceEquips.add(replaceEquip);
                player.getSoulBeastInfo().getSoulBeastEquipMap().put(replaceEquip.getId(), replaceEquip);
            }
            useEquips.add(equip.getId());
            Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
            Manager.biManager.getScript().biEquip(player, ActType.equip_wear.type, GrowType.soulBeast_equip_wear.getType(), equipBean.getPart(), equip.getItemModelId(), itemBean.getName(), equipBean.getDiamond_Number(), 0, equipBean.getQuality(), 0, 1, 0, 0, 0, 0, 0, 0);
            logger.info("穿戴装备 soulId={}, equip={}, player={}", soulBeast.getBeastId(), equip.getItemModelId(), player);
        }
        sendEquipDelete(player, useEquips.toArray(new Long[0]));
        sendSoulBeastEquipAdd(player, ItemChangeReason.UnWearEquipGet, replaceEquips.toArray(new SoulBeastEquip[0]));
        sendSoulBeast(player, soulBeast);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.SOUL_BEAST);
    }

    /**
     * 玩家请求脱装备
     *
     * @param player
     * @param equipIds
     */
    @Override
    public void reqSoulBeastEquipDown(Player player, int soulBeastId, List<Long> equipIds) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.MonsterAF)) {
            return;
        }
        if (Manager.soulBeastManager.getEmptyGridNum(player) <= 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Soul_Beast_Bag_Not_Enough);
            return;
        }
        SoulBeast beast = player.getSoulBeastInfo().getSoulBeasts().get(soulBeastId);
        if (beast == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.This_Soul_Beast_Equip_Not_Exist);
            return;
        }
        List<SoulBeastEquip> wearEquips = new ArrayList<>();
        for (long equipId : equipIds) {
            SoulBeastEquip one = Utils.findOne(beast.getEquip().values(), e -> e.getId() == equipId);
            if (one == null) {
                continue;
            }
            //TODO 脱装备
            Cfg_SoulBeastsEquip_Bean bean = CfgManager.getCfg_SoulBeastsEquip_Container().getValueByKey(one.getItemModelId());
            SoulBeastEquip equip = beast.getEquip().remove(bean.getPart());
            //TODO 装备回背包
            player.getSoulBeastInfo().getSoulBeastEquipMap().put(equip.getId(), equip);
            wearEquips.add(equip);
            logger.info("脱掉装备 soulId={}, equip={}, player={}", beast.getBeastId(), equip.getItemModelId(), player);
        }

        sendSoulBeastEquipAdd(player, ItemChangeReason.UnWearEquipGet, wearEquips.toArray(new SoulBeastEquip[0]));
        if (beast.isWork()) {
            beast.setWork(false);
        }
        sendSoulBeast(player, beast);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.SOUL_BEAST, PlayerAttributeType.Skill);
    }

    /**
     * 请求扩充格子
     *
     * @param player
     */
    @Override
    public void reqAddGrid(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.MonsterAF)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ThisFunctionIsUnlock);
            return;
        }
        if (player.getSoulBeastInfo().getSoulGridNum() >= Global.BossOld2_PossessionOtherItem.size()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Max_Fight_Soul_Beast_Num);
            return;
        }
        int next = player.getSoulBeastInfo().getSoulGridNum();
        ReadArray<Integer> array = Global.BossOld2_PossessionOtherItem.get(next);
        int level = array.get(0);
        if (player.getLevel() < level) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_CAMP_TASK_LEVELERROR);
            return;
        }
        int model = array.get(1);
        int num = array.get(2);
        if (!Manager.backpackManager.manager().onRemoveItem(player, model, num, ItemChangeReason.SoulBeastAddExtendGridDec, IDConfigUtil.getId())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
            return;
        }
        int oldNum = player.getSoulBeastInfo().getSoulGridNum();
        player.getSoulBeastInfo().setSoulGridNum(oldNum + 1);
        sendSoulBeastGrid(player);

        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.SoulBeastAddGridSuccess);

    }

    /**
     * 创建一个魂兽装备
     *
     * @param id
     * @return
     */
    @Override
    public Item createSoulBeastEquip(int id) {
        SoulBeastEquip equip = new SoulBeastEquip();
        if (CfgManager.getCfg_SoulBeastsEquip_Container().getValueByKey(id) == null) {
            logger.error("在创建魂兽装备时，在item中存在，但是在soulbeastEquip表中不存在" + id);
            return null;
        }
        equip.setLevel(0);
        return equip;
    }

    /**
     * 创建一个魂兽道具
     *
     * @param modeid
     * @return
     */
    @Override
    public Item createSoulBeastItem(int modeid) {
        SoulBeastItem item = new SoulBeastItem();
        item.setItemModelId(modeid);
        return item;
    }

    /**
     * 玩家增加魂兽装备
     *
     * @param player
     * @param item
     * @return
     */
    @Override
    public boolean addSoulBeastEquip(Player player, Item item, int reason, long actionId) {
        if (item == null) {
            logger.error("传入的道具为空！");
            return false;
        }
        if (item instanceof SoulBeastEquip) {
            player.getSoulBeastInfo().getSoulBeastEquipMap().put(item.getId(), item);
//            writeSoulBeastEquiqAddLog(player, (SoulBeastEquip) item, reason);
            writeSoulBeastLogAndBI(player, item, 0, 1, reason, actionId);
            sendSoulBeastEquipAdd(player, reason, (SoulBeastEquip) item);
            return true;
        } else if (item instanceof SoulBeastItem) {
            Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
            List<Item> items = Utils.find(player.getSoulBeastInfo().getSoulBeastEquipMap().values(), i -> i instanceof SoulBeastItem);
            int total = 0;
            for (Item sItem : items) {
                if (sItem.getItemModelId() != item.getItemModelId()) {
                    continue;
                }
                if (sItem.getNum() >= itemBean.getMax()) {
                    continue;
                }
                int limit = (int) (itemBean.getMax() - sItem.getNum());
                int add = Math.min(item.getNum(), limit);
                sItem.setNum(sItem.getNum() + add);
                sendSoulBeastItemUpdate(player, reason, sItem);
                item.setNum(item.getNum() - add);
                total += add;
                if (item.getNum() <= 0) {
                    break;
                }
            }
            if (item.getNum() > 0) {
                player.getSoulBeastInfo().getSoulBeastEquipMap().put(item.getId(), item);
                sendSoulBeastItemAdd(player, (SoulBeastItem) item, reason);
            }
            int change = total + item.getNum();
            int count = getItemCount(player, item.getItemModelId());
//            writeSoulBeastItemLog(player, (SoulBeastItem) item, change, reason);
            writeSoulBeastLogAndBI(player, item, count - change, count, reason, actionId);
            return true;
        } else {
            logger.error("传入的道具不是 SoulBeastEquip：" + item.getItemModelId());
            return false;
        }
    }

    /**
     * 获取道具数量
     *
     * @param player      玩家
     * @param itemModelId 道具id
     * @return
     */
    private int getItemCount(Player player, int itemModelId) {
        int count = 0;
        for (Item item : player.getSoulBeastInfo().getSoulBeastEquipMap().values()) {
            if (item.getItemModelId() != itemModelId) {
                continue;
            }
            count += item.getNum();
        }
        return count;
    }

    @Override
    public void sellSoulBeastEquipOrItem(Player player, List<Long> ids) {
        if (ids.size() <= 0) {
            logger.error("出售道具为空!!!");
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        //删除成功道具
        List<Long> succesIds = new ArrayList<>();
        List<Long> fialIds = new ArrayList<>();
        for (long id : ids) {
            Item item = player.getSoulBeastInfo().getSoulBeastEquipMap().get(id);
            if (item == null) {
                fialIds.add(id);
                continue;
            }
            if (item.getNum() <= 0) {
                fialIds.add(id);
                continue;
            }
            Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
            if (bean == null) {
                fialIds.add(id);
                logger.error("魂兽出售道具失败item表中无对应道具" + item.getItemModelId());
                continue;
            }
            Cfg_SoulBeastsEquip_Bean equip_bean = Cfg_SoulBeastsEquip_Container.GetInstance().getValueByKey(item.getItemModelId());
            if (equip_bean == null) {
                logger.error("售卖建魂兽装备，在SoulBeastsEquip无对应数据" + id);
                fialIds.add(id);
                continue;
            }
            ReadIntegerArray seal_num = equip_bean.getSeal_num();
            Integer currencyType = seal_num.get(0);
            Integer num = seal_num.get(1) * item.getNum();
            if (num == 0) {
                logger.info("售卖魂兽道具价格不能为0!!!");
                fialIds.add(id);
                continue;
            }
            if (!Manager.currencyManager.manager().canAddCurrency(player, currencyType, num)) {
                fialIds.add(id);
                continue;
            }
            player.getSoulBeastInfo().getSoulBeastEquipMap().remove(id);
            succesIds.add(id);
            Manager.currencyManager.manager().onAddItemCoin(player, currencyType, num, ItemChangeReason.SoulbestSellItemGet, actionId);
            int change = item.getNum();
            int after = getItemCount(player, item.getItemModelId());
            writeSoulBeastLogAndBI(player, item, after + change, after, ItemChangeReason.SoulbestSellItemDec, actionId);
            Manager.backpackManager.manager().writeItemLogAndBI(player, after + change, after, item, ItemChangeReason.SoulbestSellItemDec, actionId);
        }
        if (fialIds.size() != 0) {
            logger.info(TaskHelp.getPlayerInfo(player) + "出售兽魂道具失败列表:" + ids);
        }
        sendEquipDelete(player, succesIds.toArray(new Long[0]));
    }

    /**
     * 强化 神兽
     *
     * @param player
     * @param fixEquipId      强化装备ID
     * @param swallowEquipIds
     * @param needDouble
     */
    @Override
    public void reqSoulBeastEquipUp(Player player, int soulId, long fixEquipId, List<SoulBeastMessage.SoulCostItem> swallowEquipIds, boolean needDouble) {
        long actionId = IDConfigUtil.getLogId();
        if (swallowEquipIds.isEmpty()) {
            return;
        }
        //TODO 这个升级的装备只能是魂兽穿在身上，并且此魂兽必须出战
        SoulBeast beast = player.getSoulBeastInfo().getSoulBeasts().get(soulId);
        if (beast == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.This_Soul_Beast_Equip_Not_Exist);
            return;
        }
        SoulBeastEquip equip = Utils.findOne(beast.getEquip().values(), o -> o.getId() == fixEquipId);
        if (equip == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.This_Soul_Beast_Equip_Not_Exist);
            return;
        }

        Cfg_SoulBeastsEquip_Bean equipBean = CfgManager.getCfg_SoulBeastsEquip_Container().getValueByKey(equip.getItemModelId());
        int level = equipBean.getPart() * 10000 + equip.getLevel();

        Cfg_SoulBeastsEquipLevel_Bean nextBean = CfgManager.getCfg_SoulBeastsEquipLevel_Container().getValueByKey(level + 1);
        if (nextBean == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.This_Soul_Beast_Equip_Max_Level);
            return;
        }

        if (needDouble) {
            int exp = 0;
            for (SoulBeastMessage.SoulCostItem costItem : swallowEquipIds) {
                Item costEquip = player.getSoulBeastInfo().getSoulBeastEquipMap().get(costItem.getId());
                if (costEquip == null) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.UnKown);
                    continue;
                }
                Cfg_SoulBeastsEquip_Bean costEquipBean = CfgManager.getCfg_SoulBeastsEquip_Container().getValueByKey(costEquip.getItemModelId());
                if (costEquipBean != null) {
                    exp += costEquipBean.getSeal_exp();
                } else {
                    Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(costEquip.getItemModelId());
                    exp += (itemBean.getEffect_num().get(0).get(1) * costItem.getCount());
                }
            }
            //TODO 双倍强化时：每100点经验=1金元宝消耗，不足100按100计算
            int gold = (exp + 99) / 100;
            if (!Manager.currencyManager.manager().canDecBindGoldOrGold(player, gold)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Not_Enough_Gold);
                return;
            }
        }
        List<Long> costIds = new ArrayList<>();
        List<Item> updateCostItems = new ArrayList<>();
        int calcGoldExp = 0;
        for (SoulBeastMessage.SoulCostItem costItem : swallowEquipIds) {
            Item item = player.getSoulBeastInfo().getSoulBeastEquipMap().get(costItem.getId());
            if (item == null) {
                continue;
            }
            level = equipBean.getPart() * 10000 + equip.getLevel();
            nextBean = CfgManager.getCfg_SoulBeastsEquipLevel_Container().getValueByKey(level + 1);
            if (nextBean == null) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.This_Soul_Beast_Equip_Max_Level);
                break;
            }
            if (item instanceof SoulBeastEquip) {
                Cfg_SoulBeastsEquip_Bean bean = CfgManager.getCfg_SoulBeastsEquip_Container().getValueByKey(item.getItemModelId());
                //TODO 加上装备基础经验
                int baseExp = (needDouble ? 2 * bean.getSeal_exp() : bean.getSeal_exp());
                equip.setCurExp(equip.getCurExp() + baseExp);
                //TODO 加上装备强化经验
                int historyExp = calcHistoryExp((SoulBeastEquip) item);
                equip.setCurExp(equip.getCurExp() + historyExp);

                //TODO 清除
                player.getSoulBeastInfo().getSoulBeastEquipMap().remove(costItem.getId());
                costIds.add(costItem.getId());
                calcGoldExp = calcGoldExp + bean.getSeal_exp();
                //日志记录
//                addSoulBeastItemLog(player,item,-1,0,ItemChangeReason.SoulBeastStrengthenDec);
                Manager.backpackManager.manager().writeItemLogAndBI(player, item.getNum(), 0, item, ItemChangeReason.SoulBeastStrengthenDec, actionId);
            } else {
                Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
                //TODO 加上道具基础经验
                int exp = itemBean.getEffect_num().get(0).get(1) * costItem.getCount();
                equip.setCurExp(equip.getCurExp() + (needDouble ? 2 * exp : exp));
                //TODO 清除
                if (costItem.getCount() >= item.getNum()) {
                    costIds.add(costItem.getId());
                    player.getSoulBeastInfo().getSoulBeastEquipMap().remove(costItem.getId());
                    //日志记录
//                    addSoulBeastItemLog(player,item,-item.getNum(),0,ItemChangeReason.SoulBeastStrengthenDec);
                    Manager.backpackManager.manager().writeItemLogAndBI(player, item.getNum(), 0, item, ItemChangeReason.SoulBeastStrengthenDec, actionId);
                } else {
                    item.setNum(item.getNum() - costItem.getCount());
                    updateCostItems.add(item);
                    //日志记录
//                    addSoulBeastItemLog(player,item,- costItem.getCount(),item.getNum(),ItemChangeReason.SoulBeastStrengthenDec);
                    Manager.backpackManager.manager().writeItemLogAndBI(player, item.getNum() + costItem.getCount(), item.getNum(), item, ItemChangeReason.SoulBeastStrengthenDec, actionId);
                }
                calcGoldExp = calcGoldExp + exp;
            }
            //TODO 检测升级
            doEquipLevelUp(player, equip);
        }
        if (needDouble) {
            int gold = (calcGoldExp + 99) / 100;
            if (!Manager.currencyManager.manager().decGold(player, gold, ItemChangeReason.EquipPartStrengthenDec, actionId)) {
                logger.error("神兽强化元宝计算错误  player={}", player);
            }
        }
        sendSoulBeastItemUpdate(player, ItemChangeReason.EquipPartStrengthenDec, updateCostItems.toArray(new SoulBeastItem[0]));
        sendEquipDelete(player, costIds.toArray(new Long[0]));

        SoulBeastMessage.ResSoulBeastEquipInfo.Builder builder = SoulBeastMessage.ResSoulBeastEquipInfo.newBuilder();
        builder.setEquip(buildSoulBeastEquip(equip));
        builder.setSoulId(soulId);
        MessageUtils.send_to_player(player, SoulBeastMessage.ResSoulBeastEquipInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.SOUL_BEAST);
    }


    /**
     * 魂兽道具改变日志
     * @param player
     * @param item
     * @param changeNum  大于0增加，小于0减少
     * @param afterNum 改变后的数量
     * @param reason 改变原因
     */
//    private void addSoulBeastItemLog(Player player, Item item, int changeNum, int afterNum, int reason) {
//        if(changeNum == 0){
//            return;
//        }
//        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
//        long action = IDConfigUtil.getLogId();
//        if(model != null){
//            //变化日志
//            Manager.backpackManager.manager().writeItemLog(player,item.getId(),model.getId(),afterNum - changeNum, afterNum, reason,ItemChangeAction.CHANGE,action,0,0,0, model.getType());
//            //BI
//            Manager.biManager.getScript().biItem(player, item.getId(), model.getType(), model.getColor(), model.getId(), model.getName()
//                    , changeNum, afterNum, reason, action, changeNum > 0 ? 1 : 0, player.getCurGps().getModelId());
//        }
//    }

    /**
     * 神兽装备合成
     *
     * @param player
     * @param part
     * @param equips
     */
    @Override
    public void reqSoulBeastEquipSynthetic(Player player, int part, List<Long> equips) {
        List<Item> items = Utils.find(player.getSoulBeastInfo().getSoulBeastEquipMap().values(), e -> equips.contains(e.getId()) && e instanceof SoulBeastEquip);
        //TODO 不足三合一
        if (items.size() < 3) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
            return;
        }
        Item first = items.get(0);
        Cfg_SoulBeastsEquip_Bean firstBean = CfgManager.getCfg_SoulBeastsEquip_Container().getValueByKey(first.getItemModelId());
        if (firstBean.getIf_ban() == 1) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
            return;
        }
        //TODO 验证是否是同一种道具
        for (Item item : items) {
            Cfg_SoulBeastsEquip_Bean nextBean = CfgManager.getCfg_SoulBeastsEquip_Container().getValueByKey(item.getItemModelId());
            if (firstBean.getQuality() != nextBean.getQuality() || firstBean.getDiamond_Number() != nextBean.getDiamond_Number()) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
                return;
            }
        }
        long actionId = IDConfigUtil.getId();
        //TODO 计算大成功概率
        ReadIntegerArray equipCfg = RandomUtils.random(1, 10000) <= firstBean.getBigsuccess() ? firstBean.getBigsuccess_target_equip() : firstBean.getTarget_equip();
        Cfg_SoulBeastsEquip_Bean equip_bean = CfgManager.getCfg_SoulBeastsEquip_Container().getValueByKey(equipCfg.get(part - 1));
        if (equip_bean == null) {
            logger.error(TaskHelp.getPlayerInfo(player) + "在魂兽装备合成的时候，合成物：" + firstBean + " 生成：" + equipCfg + "在表 SoulBeastsEquip_Bean 无对应数据,可能已经到达满级");
            return;
        }
        //TODO 扣除必须扣除的道具
        ReadArray<Integer> mustitem = firstBean.getDemand_item();
        if (mustitem.size() >= 2) {
            if (!Manager.backpackManager.manager().onRemoveItem(player, mustitem.get(0), mustitem.get(1), ItemChangeReason.SoulBeastMergeDec, actionId)) {
                MessageUtils.notify_player(player, Notify.NORMAL, MessageString.SoulBeastEquipSyntheticItemNotEnough);
                return;
            }
        }
        SoulBeastEquip target = (SoulBeastEquip) Item.createItem(equip_bean.getId(), 1, false);
        //TODO 旧装备经验 全部加到新装备上
        for (Item item : items) {
            SoulBeastEquip equip = (SoulBeastEquip) item;
            Cfg_SoulBeastsEquip_Bean equipBean = CfgManager.getCfg_SoulBeastsEquip_Container().getValueByKey(equip.getItemModelId());
            int level = equipBean.getPart() * 10000 + equip.getLevel();
            Cfg_SoulBeastsEquipLevel_Bean levelBean = CfgManager.getCfg_SoulBeastsEquipLevel_Container().getValueByKey(level);
            target.setCurExp(target.getCurExp() + equip.getCurExp() + levelBean.getNeedExp());

            player.getSoulBeastInfo().getSoulBeastEquipMap().remove(item.getId());
            //日志记录
//            addSoulBeastItemLog(player,item,-item.getNum(),0,ItemChangeReason.SoulBeastStrengthenDec);
            Manager.backpackManager.manager().writeItemLogAndBI(player, item.getNum(), 0, item, ItemChangeReason.SoulBeastMergeDec, actionId);
        }
        sendEquipDelete(player, equips.toArray(new Long[0]));

        doEquipLevelUp(player, target);

        Manager.soulBeastManager.deal().addSoulBeastEquip(player, target, ItemChangeReason.SoulBeastMergeGet, actionId);
        EquipMessage.ResSoulBeastEquipSyn.Builder builder = EquipMessage.ResSoulBeastEquipSyn.newBuilder();
        builder.setState(0);
        MessageUtils.send_to_player(player, EquipMessage.ResSoulBeastEquipSyn.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        if (equip_bean.getNotice() != 0 || equip_bean.getChatchannel() != null) {
            MessageUtils.notify_allOnlinePlayer(equip_bean.getNotice(), equip_bean.getChatchannel(), MessageString.EquipSynthetic,
                    player.getId() + "",
                    player.getName(),
                    Manager.backpackManager.manager().getChatInfo(target),
                    Utils.makeUrlStr(MessageString.EquipSynthetic));
        }

        //TODO 写日志
        StringBuilder useEquipInfo = new StringBuilder();
        for (Item item : items) {
            useEquipInfo.append(item).append(";");
        }
        DbLog.save(DbLogEnum.EQUIP_SYNTHETIC_LOG, player, String.valueOf(equip_bean.getId()), useEquipInfo.toString(), String.valueOf(equip_bean.getId()), "1", "1");

        Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(equip_bean.getId());
        Manager.biManager.getScript().biEquip(player, ActType.equip_synthesis.type, GrowType.soulBeast_equip_synthesis.getType(), equip_bean.getPart(), equip_bean.getId(), itemBean.getName(), equip_bean.getDiamond_Number(), 0, equip_bean.getQuality(), target.getLevel(), 1, 0, 0, 0, 0, 0, 0);

    }


    /**
     * 计算历史强化经验
     *
     * @param equip
     * @return
     */
    int calcHistoryExp(SoulBeastEquip equip) {
        int exp = equip.getCurExp();
        Cfg_SoulBeastsEquip_Bean equipBean = CfgManager.getCfg_SoulBeastsEquip_Container().getValueByKey(equip.getItemModelId());
        int level = equipBean.getPart() * 10000 + equip.getLevel();
        Cfg_SoulBeastsEquipLevel_Bean lastBean = CfgManager.getCfg_SoulBeastsEquipLevel_Container().getValueByKey(level - 1);
        if (lastBean == null) {
            return exp;
        }
        exp += lastBean.getNeedExp();
        return exp;
    }

    /**
     * 升级
     *
     * @param player
     * @param equip
     */
    void doEquipLevelUp(Player player, SoulBeastEquip equip) {
        Cfg_SoulBeastsEquip_Bean equipBean = CfgManager.getCfg_SoulBeastsEquip_Container().getValueByKey(equip.getItemModelId());
        int level = equipBean.getPart() * 10000 + equip.getLevel();
        Cfg_SoulBeastsEquipLevel_Bean nextBean = CfgManager.getCfg_SoulBeastsEquipLevel_Container().getValueByKey(level + 1);
        if (nextBean == null) {
            equip.setCurExp(0);
            return;
        }
        Cfg_SoulBeastsEquipLevel_Bean levelBean = CfgManager.getCfg_SoulBeastsEquipLevel_Container().getValueByKey(level);
        int exp = nextBean.getNeedExp() - levelBean.getNeedExp();
        if (equip.getCurExp() >= exp) {
            equip.setLevel(equip.getLevel() + 1);
            equip.setCurExp(equip.getCurExp() - exp);
            logger.info("魂兽装备升级level={}, exp ={} player={}", equip.getLevel(), equip.getCurExp(), player);
//            RoleGrowLog.create(player,GrowType.soulBeast_equip_intensify, );
            Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(equip.getItemModelId());
            Manager.biManager.getScript().biEquip(player, ActType.equip_intensify.type, GrowType.soulBeast_equip_intensify.getType(), equipBean.getPart(), equip.getItemModelId(), itemBean.getName(), equipBean.getDiamond_Number(), 0, equipBean.getQuality(), equip.getLevel(), 1, 0, 0, 0, 0, 0, 0);
            doEquipLevelUp(player, equip);
        }
    }

    //TODO 发送装备更新
    void sendSoulBeastItemUpdate(Player player, int reason, Item... items) {
        SoulBeastMessage.ResSoulBeastItemUpdate.Builder builder = SoulBeastMessage.ResSoulBeastItemUpdate.newBuilder();
        builder.setReason(reason);
        for (Item item : items) {
            builder.addItems(pack(item));
        }
        MessageUtils.send_to_player(player, SoulBeastMessage.ResSoulBeastItemUpdate.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    //TODO 发送装备添加
    void sendSoulBeastItemAdd(Player player, SoulBeastItem item, int reason) {
        SoulBeastMessage.ResSoulBeastItemAdd.Builder builder = SoulBeastMessage.ResSoulBeastItemAdd.newBuilder();
        builder.addItems(pack(item));
        builder.setReason(reason);
        MessageUtils.send_to_player(player, SoulBeastMessage.ResSoulBeastItemAdd.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }


    /**
     * 更新神兽装备
     *
     * @param player
     * @param equips
     */
    void sendSoulBeastEquipAdd(Player player, int reason, SoulBeastEquip... equips) {
        SoulBeastMessage.ResSoulBeastEquipAdd.Builder builder = SoulBeastMessage.ResSoulBeastEquipAdd.newBuilder();
        for (SoulBeastEquip equip : equips) {
            builder.addEquips(buildSoulBeastEquip(equip));
        }
        builder.setReason(reason);
        MessageUtils.send_to_player(player, SoulBeastMessage.ResSoulBeastEquipAdd.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 判断此道具是不是魂兽装备
     *
     * @param item
     * @return
     */
    @Override
    public boolean isSoulBeastEquipOrItem(Item item) {
        Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        return itemBean.getType() == ItemTypeConst.SoulBeastEquip || itemBean.getType() == ItemTypeConst.SoulBeastItem;
    }

    /**
     * 功能开放
     *
     * @param player
     */
    @Override
    public void functionOpen(Player player) {
        player.getSoulBeastInfo().getSoulBeasts().clear();
        for (Cfg_SoulBeasts_Bean soulBeastsBean : CfgManager.getCfg_SoulBeasts_Container().getValuees()) {
            if (soulBeastsBean.getCanShow() != 1) {
                continue;
            }
            SoulBeast soulBeast = createSoulBeast(soulBeastsBean);
            player.getSoulBeastInfo().getSoulBeasts().put(soulBeast.getBeastId(), soulBeast);
        }
        sendSoulBeastBagInfo(player);
        sendSoulBeastInfo(player);
        sendSoulBeastGrid(player);
    }


    /**
     * 写魂兽装备增加日志
     *
     * @param player
     * @param item
     * @param reason
     */

    private void writeSoulBeastLogAndBI(Player player, Item item, int oldNum, int afterNum, int reason, long actionId) {
        try {
            SoulBeastItemLog log = new SoulBeastItemLog();
            log.setPlayerInfo(player.getPlatformName()
                    , player.getCreateServerId()
                    , player.getUserId()
                    , player.getId()
                    , player.getName());
            if (item instanceof SoulBeastEquip) {
                Cfg_SoulBeastsEquip_Bean bean = CfgManager.getCfg_SoulBeastsEquip_Container().getValueByKey(item.getItemModelId());
                log.setColor(bean.getQuality());
                log.setPart(bean.getPart());
                log.setStar(bean.getDiamond_Number());
                log.setType(1);
            } else {
                log.setColor(0);
                log.setPart(0);
                log.setStar(0);
                log.setType(2);
            }
            log.setReason(reason);
            log.setModelItemId(item.getItemModelId());
            int change = afterNum - oldNum;
            log.setChangeNum(change);
            log.setExtraAttribute("");
            LogService.getInstance().execute(log);

            Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
            int changeType = 0;
            if (change > 0) {
                changeType = 1;
            }
            change = Math.abs(change);

            LogDataManager.instance.onItemChange(player.getCreateServerId(), model.getId(), model.getType(), model.getName(), oldNum, afterNum);
            Manager.biManager.getScript().biItem(player, item.getId(), model.getType(), model.getColor(), model.getId(), model.getName()
                    , change, afterNum, reason, actionId, changeType, player.getCurGps().getModelId());
            Manager.biManager.get4399Script().itemBiTo4399(player, reason, model.getId(), oldNum, afterNum);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    /**
     * 写魂兽道具增加日志
     *
     * @param player
     * @param item
     * @param reason
     */
//    private void writeSoulBeastItemLog(Player player, SoulBeastItem item, int change, int reason) {
//        SoulBeastItemLog log = new SoulBeastItemLog();
//        log.setPlayerInfo(player.getPlatformName()
//                , player.getCreateServerId()
//                , player.getUserId()
//                , player.getId()
//                , player.getName());
//        log.setColor(0);
//        log.setModelItemId(item.getItemModelId());
//        log.setPart(0);
//        log.setReason(reason);
//        log.setStar(0);
//        log.setExtraAttribute("");
//        log.setType(2);
//        log.setChange(change);
//        LogService.getInstance().execute(log);
//    }

    /**
     * 创建一个魂兽
     *
     * @param beastsBean
     * @return
     */
    private SoulBeast createSoulBeast(Cfg_SoulBeasts_Bean beastsBean) {
        SoulBeast beast = new SoulBeast();
        beast.setBeastId(beastsBean.getId());
        return beast;
    }


    /**
     * 发送背包数据
     *
     * @param player
     */
    private void sendSoulBeastBagInfo(Player player) {

        SoulBeastMessage.ResSoulBeastBag.Builder builder = SoulBeastMessage.ResSoulBeastBag.newBuilder();
        for (Item item : player.getSoulBeastInfo().getSoulBeastEquipMap().values()) {
            if (item instanceof SoulBeastEquip) {
                builder.addEquips(buildSoulBeastEquip((SoulBeastEquip) item));
            } else if (item instanceof SoulBeastItem) {
                builder.addItems(pack(item));
            }
        }
        MessageUtils.send_to_player(player, SoulBeastMessage.ResSoulBeastBag.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 发送神兽数据
     *
     * @param player
     */
    private void sendSoulBeastInfo(Player player) {
        SoulBeastMessage.ResSoulBeastList.Builder builder = SoulBeastMessage.ResSoulBeastList.newBuilder();
        for (SoulBeast soulBeast : player.getSoulBeastInfo().getSoulBeasts().values()) {
            builder.addBeasts(pack(soulBeast));
        }
        MessageUtils.send_to_player(player, SoulBeastMessage.ResSoulBeastList.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 发送玩家最新的格子数量
     *
     * @param player
     */
    private void sendSoulBeastGrid(Player player) {
        SoulBeastMessage.ResSoulBeastGridNum.Builder builder = SoulBeastMessage.ResSoulBeastGridNum.newBuilder();
        builder.setNum(player.getSoulBeastInfo().getSoulGridNum() + Global.BossOld2_PossessionNum);
        MessageUtils.send_to_player(player, SoulBeastMessage.ResSoulBeastGridNum.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    //TODO 通知bag删除装备
    void sendEquipDelete(Player player, Long... ids) {
        SoulBeastMessage.ResDeleteSoulBeast.Builder builder = SoulBeastMessage.ResDeleteSoulBeast.newBuilder();
        for (long id : ids) {
            builder.addDeleteEquipIds(id);
        }
        MessageUtils.send_to_player(player, SoulBeastMessage.ResDeleteSoulBeast.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    //TODO 同步神兽数据
    void sendSoulBeast(Player player, SoulBeast beast) {
        SoulBeastMessage.ResSoulBeastInfo.Builder builder = SoulBeastMessage.ResSoulBeastInfo.newBuilder();
        builder.setBeast(pack(beast));
        MessageUtils.send_to_player(player, SoulBeastMessage.ResSoulBeastInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    SoulBeastMessage.SoulBeastEquip buildSoulBeastEquip(SoulBeastEquip item) {
        SoulBeastMessage.SoulBeastEquip.Builder builder = SoulBeastMessage.SoulBeastEquip.newBuilder();
        builder.setItemId(item.getId());
        builder.setItemModelId(item.getItemModelId());
        builder.setLevel(item.getLevel());
        builder.setCurExp(item.getCurExp());
        return builder.build();
    }

    private SoulBeastMessage.SoulBeast pack(SoulBeast beast) {
        SoulBeastMessage.SoulBeast.Builder builder = SoulBeastMessage.SoulBeast.newBuilder();
        builder.setSoulId(beast.getBeastId());
        builder.setFight(beast.isWork());
        for (SoulBeastEquip equip : beast.getEquip().values()) {
            SoulBeastMessage.SoulBeastEquip soulBeastEquip = buildSoulBeastEquip(equip);
            builder.addEquips(soulBeastEquip);
        }
        return builder.build();
    }

    /**
     * 组装协议
     *
     * @param item
     * @return
     */
    SoulBeastMessage.itemModel.Builder pack(Item item) {
        SoulBeastMessage.itemModel.Builder builder = SoulBeastMessage.itemModel.newBuilder();
        builder.setItemModelId(item.getItemModelId());
        builder.setItemId(item.getId());
        builder.setNum(item.getNum());
        return builder;
    }

}
