package common.equip;

import com.data.*;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.log.db.RoleGrowLog;
import com.game.log.grow.GrowType;
import com.game.manager.Manager;
import com.game.newfashion.manager.NewFashionManager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.Skill;
import com.game.soulArmor.script.ISoulArmor;
import com.game.soulArmor.struct.SoulArmor;
import com.game.soulArmor.struct.SoulArmorSlot;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import game.core.util.IDConfigUtil;
import game.message.MapMessage;
import game.message.SoulArmorMessage;
import game.message.backpackMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * @Desc TODO
 * @Date 2020/12/23 18:29
 * @Auth ZUncle
 */
public class SoulArmorScript implements ISoulArmor {

    final Logger logger = LogManager.getLogger(SoulArmorScript.class);

    /**
     * 获取魂甲抽奖等级
     *
     * @param player
     */
    @Override
    public void reqOpenSoulArmorLottery(Player player) {
        sendLotteryInfo(player);
    }

    void sendLotteryInfo(Player player) {
        SoulArmorMessage.ResSoulArmorLottery.Builder message = SoulArmorMessage.ResSoulArmorLottery.newBuilder();
        message.setLevel(player.getSoulArmor().getLotteryLevel());
        message.setExp(player.getSoulArmor().getLotteryExp());
        MessageUtils.send_to_player(player, SoulArmorMessage.ResSoulArmorLottery.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 获取魂甲背包数据
     *
     * @param player
     */
    @Override
    public void reqSoulArmorBag(Player player) {

        SoulArmorMessage.ResSoulArmorBag.Builder mBag = SoulArmorMessage.ResSoulArmorBag.newBuilder();

        for (Item ball : player.getSoulArmor().getBag().values()) {
            mBag.addBalls(pack(ball));
        }

        MessageUtils.send_to_player(player, SoulArmorMessage.ResSoulArmorBag.MsgID.eMsgID_VALUE, mBag.build().toByteArray());
    }

    /**
     * 魂甲抽奖
     *
     * @param player
     * @param type
     * @param isGoldDouble
     */
    @Override
    public void reqSoulArmorLottery(Player player, int type, int count, boolean isGoldDouble) {
        if (count < 1) {
            return;
        }
        Cfg_SoulArmor_signet_lottery_object_Bean bean = CfgManager.getCfg_SoulArmor_signet_lottery_object_Container().getValueByKey(type);
        if (bean == null) {
            return;
        }
        if (isGoldDouble) {
            if (!Manager.backpackManager.manager().canDeleteItemNum(player, bean.getConsumeMoney(), count)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Not_Enough_Gold);
                return;
            }
        }
        if (!Manager.backpackManager.manager().canDeleteItemNum(player, bean.getConsumeItem(), count)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Item_Not_Enough, Manager.backpackManager.manager().getName(bean.getConsumeItem()));
            return;
        }
        long logId = IDConfigUtil.getLogId();

        if (isGoldDouble) {
            for (ReadArray<Integer> cost : bean.getConsumeMoney().getValuees()) {
                Manager.backpackManager.manager().onRemoveItem(player, cost.get(0), cost.get(1) * count, ItemChangeReason.SoulArmorLotteryDec, logId);
            }
        }
        Manager.backpackManager.manager().onRemoveItem(player, bean.getConsumeItem(), count, ItemChangeReason.SoulArmorLotteryDec, logId);
        List<List<Integer>> drops = new ArrayList<>();
        int index = type * 1000 + player.getSoulArmor().getLotteryLevel();
        Cfg_SoulArmor_signet_lottery_Bean pool = CfgManager.getCfg_SoulArmor_signet_lottery_Container().getValueByKey(index);
        for (int i = 0; i < count; i++) {
            ReadArray<Integer> random = random(pool.getProbability());
            List<List<Integer>> itemDrops = Manager.dropManager.deal().getItemDrops(player, random.get(2));
            drops.addAll(itemDrops);
        }
        List<List<Integer>> mergeItems = Manager.backpackManager.manager().getAfterMergeItemList(drops);
        List<Item> items = Item.createItems(mergeItems, isGoldDouble ? 2 : 1);

        int reason = isGoldDouble ? ItemChangeReason.SoulArmorGoldLotteryGet : ItemChangeReason.SoulArmorLotteryGet;

        if (!Manager.backpackManager.manager().addItems(player, items, reason, logId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, reason, logId);
        }

        player.getSoulArmor().setLotteryExp(player.getSoulArmor().getLotteryExp() + bean.getExp() * count * (isGoldDouble ? 2 : 1));

        doLotteryLevelUp(player);

        sendLotteryInfo(player);
        logger.info("魂甲抽奖 type={} count={} useGold={} player={}", type, count, isGoldDouble, player);

        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.SoulArmorLottery, bean.getId(), bean.getName(), reason, 0, player.getSoulArmor().getLotteryLevel());
    }

    void doLotteryLevelUp(Player player) {

        int index = 2 * 1000 + player.getSoulArmor().getLotteryLevel();
        Cfg_SoulArmor_signet_lottery_Bean next = CfgManager.getCfg_SoulArmor_signet_lottery_Container().getValueByKey(index + 1);
        if (next == null) {
            return;
        }
        Cfg_SoulArmor_signet_lottery_Bean bean = CfgManager.getCfg_SoulArmor_signet_lottery_Container().getValueByKey(index);
        if (player.getSoulArmor().getLotteryExp() >= bean.getLevelUPExp()) {
            player.getSoulArmor().setLotteryLevel(player.getSoulArmor().getLotteryLevel() + 1);
            player.getSoulArmor().setLotteryExp(player.getSoulArmor().getLotteryExp() - bean.getLevelUPExp());
            doLotteryLevelUp(player);
        }
    }

    ReadArray<Integer> random(ReadIntegerArrayEs params) {
        TreeMap<Float, ReadArray<Integer>> weightMap = new TreeMap<>();
        for (ReadArray<Integer> param : params.getValuees()) {
            Float weight = weightMap.size() == 0 ? 0f : weightMap.lastKey();
            weight += param.get(1);
            weightMap.put(weight, param);
        }
        float randomWeight = RandomUtils.randomFloatValue(0f, weightMap.lastKey());
        SortedMap<Float, ReadArray<Integer>> sort = weightMap.tailMap(randomWeight, false);
        return weightMap.get(sort.firstKey());
    }

    /**
     * 魂印分解
     *
     * @param player
     * @param ids
     */
    @Override
    public void reqSplitSoulArmorBall(Player player, List<Long> ids) {
        List<Long> decList = new ArrayList<>();
        HashMap<String, Integer[]> merge = new HashMap<>();
        long actionId = IDConfigUtil.getLogId();
        for (long id : ids) {
            Item item = player.getSoulArmor().getBag().get(id);
            if (item == null) {
                continue;
            }
            Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(item.getItemModelId());
            if (bean == null) {
                logger.error("魂印没有分解配置 id={}", id);
                continue;
            }
            player.getSoulArmor().getBag().remove(id);
            Manager.backpackManager.manager().writeItemLogAndBI(player, item.getNum(), 0, item, ItemChangeReason.SoulArmorBallSplitDec, actionId);
            decList.add(id);
            for (ReadArray<Integer> p : bean.getPrice1().getValuees()) {
                int itemId = p.get(0);
                int num = p.get(1);
                int bind = p.get(2, 1);
                String key = itemId + "_" + bind;
                Integer[] arg = {0, 0, 0};
                if (merge.containsKey(key)) {
                    arg = merge.get(key);
                } else {
                    merge.put(key, arg);
                }
                arg[0] = itemId;
                arg[1] = (num + arg[1]);
                arg[2] = bind;
            }
        }
        List<Item> items = new ArrayList<>();
        for (Integer[] a : merge.values()) {
            items.addAll(Item.createItems(a[0], a[1], a[2] == 1));
        }
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.SoulArmorBallSplitGet, actionId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.SoulArmorBallSplitGet, actionId);
        }

        sendSoulBallDec(player, ItemChangeReason.SoulArmorBallSplitDec, decList.toArray(new Long[decList.size()]));

        logger.info("魂印分解 ids={} player={}", ids, player);
    }

    void sendSoulBallDec(Player player, int reasion, Long... ids) {

        SoulArmorMessage.ResDelSoulArmorBall.Builder message = SoulArmorMessage.ResDelSoulArmorBall.newBuilder();
        message.setReason(reasion);
        for (long id : ids) {
            message.addIds(id);
        }
        MessageUtils.send_to_player(player, SoulArmorMessage.ResDelSoulArmorBall.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 脱下
     *
     * @param player
     * @param slotId
     */
    @Override
    public void reqUnWearSoulArmorBall(Player player, int slotId) {

        SoulArmorSlot slot = player.getSoulArmor().getSlots().get(slotId);
        if (slot == null) {
            return;
        }
        Item ball = slot.getBall();
        if (ball == null) {
            return;
        }
        slot.setBall(null);
        long actionId = IDConfigUtil.getLogId();
        if (!Manager.backpackManager.manager().addItem(player, ball, ItemChangeReason.SoulArmorUnWearGet, actionId)) {
            ArrayList<Item> items = new ArrayList<>();
            items.add(ball);
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.SoulArmorUnWearGet, actionId);
        }

        sendUpdateSoulArmorBallSlot(player, slot);

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.SoulEquip);

        logger.info("魂印卸下 slotId={} player={}", slotId, player);
    }


    void sendUpdateSoulArmorBallSlot(Player player, SoulArmorSlot slot) {

        SoulArmorMessage.SoulArmorBallSlot.Builder mSlot = SoulArmorMessage.SoulArmorBallSlot.newBuilder();
        mSlot.setIsOpen(true);
        mSlot.setSlot(slot.getSlotId());
        mSlot.setLevel(slot.getLevel());
        if (slot.getBall() != null) {
            mSlot.setBall(pack(slot.getBall()));
        }

        SoulArmorMessage.ResUpdateSoulArmorBallSlot.Builder message = SoulArmorMessage.ResUpdateSoulArmorBallSlot.newBuilder();
        message.setSlot(mSlot);

        MessageUtils.send_to_player(player, SoulArmorMessage.ResUpdateSoulArmorBallSlot.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 淬炼魂甲
     *
     * @param player
     */
    @Override
    public void reqUpSoulArmor(Player player) {
        SoulArmor soulArmor = player.getSoulArmor();
        Cfg_SoulArmor_level_up_Bean next = CfgManager.getCfg_SoulArmor_level_up_Container().getValueByKey(soulArmor.getLevel() + 1);
        if (next == null) {
            return;
        }
        Cfg_SoulArmor_level_up_Bean bean = CfgManager.getCfg_SoulArmor_level_up_Container().getValueByKey(soulArmor.getLevel());
        if (!Manager.backpackManager.manager().canDeleteItemNum(player, bean.getConsume(), 1)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MaterialNotEnough);
            return;
        }
        long logId = IDConfigUtil.getLogId();
        for (ReadArray<Integer> cost : bean.getConsume().getValuees()) {
            Manager.backpackManager.manager().onRemoveItem(player, cost.get(0), cost.get(1), ItemChangeReason.SoulArmorUpDec, logId);
        }
        soulArmor.setLevel(soulArmor.getLevel() + 1);

        SoulArmorMessage.ResUpdateSoulArmorLevel.Builder message = SoulArmorMessage.ResUpdateSoulArmorLevel.newBuilder();
        message.setLevel(player.getSoulArmor().getLevel());
        MessageUtils.send_to_player(player, SoulArmorMessage.ResUpdateSoulArmorLevel.MsgID.eMsgID_VALUE, message.build().toByteArray());

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.SoulEquip);

        logger.info("魂甲淬炼 level={} player={}", soulArmor.getLevel(), player);

        RoleGrowLog.create(player, GrowType.soulArmor_level_up, 0, 0, soulArmor.getLevel() - 1, soulArmor.getLevel(), null);
    }

    /**
     * 突破
     *
     * @param player
     */
    @Override
    public void reqUpSoulArmorQuality(Player player) {

        SoulArmor soulArmor = player.getSoulArmor();
        Cfg_SoulArmor_breach_Bean next = CfgManager.getCfg_SoulArmor_breach_Container().getValueByKey(soulArmor.getQualityLevel() + 1);
        if (next == null) {
            return;
        }
        Cfg_SoulArmor_breach_Bean bean = CfgManager.getCfg_SoulArmor_breach_Container().getValueByKey(soulArmor.getQualityLevel());
        if (!Manager.backpackManager.manager().canDeleteItemNum(player, bean.getConsume(), 1)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MaterialNotEnough);
            return;
        }
        long logId = IDConfigUtil.getLogId();
        for (ReadArray<Integer> cost : bean.getConsume().getValuees()) {
            Manager.backpackManager.manager().onRemoveItem(player, cost.get(0), cost.get(1), ItemChangeReason.SoulArmorUpQualityDec, logId);
        }
        soulArmor.setQualityLevel(soulArmor.getQualityLevel() + 1);

        Manager.newFashionManager.deal().huaxingActivateFashion(player, NewFashionManager.SOUL_TYPE, player.getSoulArmor().getQualityLevel());
        SoulArmorMessage.ResSoulArmorQualityLevel.Builder message = SoulArmorMessage.ResSoulArmorQualityLevel.newBuilder();
        message.setQualityLevel(soulArmor.getQualityLevel());
        MessageUtils.send_to_player(player, SoulArmorMessage.ResSoulArmorQualityLevel.MsgID.eMsgID_VALUE, message.build().toByteArray());

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.SoulEquip);

        MessageUtils.notify_allOnlinePlayer(next.getNotice() , next.getChatchannel(), MessageString.SOULARMOR_BREACH_NOTICE1,
                player.getId()+"",
                player.getName(),
                ServerStr.getChatTableName(next.getName()),
                Utils.makeUrlStr(MessageString.SOULARMOR_BREACH_NOTICE1));

        logger.info("魂甲淬炼 level={} player={}", soulArmor.getLevel(), player);

        RoleGrowLog.create(player, GrowType.soulArmor_evolution, 0, 0, soulArmor.getQualityLevel() - 1, soulArmor.getQualityLevel(), null);
    }

    /**
     * 觉醒
     *
     * @param player
     */
    @Override
    public void reqUpSoulArmorSkill(Player player) {

        Cfg_SoulArmor_awaken_Bean next = CfgManager.getCfg_SoulArmor_awaken_Container().getValueByKey(player.getSoulArmor().getSkillLevel() + 1);
        if (next == null) {
            return;
        }
        Cfg_SoulArmor_awaken_Bean bean = CfgManager.getCfg_SoulArmor_awaken_Container().getValueByKey(player.getSoulArmor().getSkillLevel());
        if (!Manager.backpackManager.manager().canDeleteItemNum(player, bean.getConsume(), 1)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MaterialNotEnough);
            return;
        }
        long logId = IDConfigUtil.getLogId();
        for (ReadArray<Integer> cost : bean.getConsume().getValuees()) {
            Manager.backpackManager.manager().onRemoveItem(player, cost.get(0), cost.get(1), ItemChangeReason.SoulArmorUpSkillLevelDec, logId);
        }
        int oldLevel = player.getSoulArmor().getSkillLevel();
        player.getSoulArmor().setSkillLevel(next.getLevel());


        SoulArmorMessage.ResUpSoulArmorSkillLevel.Builder message = SoulArmorMessage.ResUpSoulArmorSkillLevel.newBuilder();
        message.setSkillLevel(next.getLevel());
        if (next.getJudgeOpenSkill() > 0) {
            Skill skill = new Skill();
            skill.setSkillId(next.getSkill());
            player.getSoulArmor().getSkills().put(skill.getSkillId(), skill);
            Manager.skillManager.addSkill(player, skill.getSkillId());
            message.setSkillId(skill.getSkillId());
            logger.info("魂甲觉醒获得技能 skill={}  player={}", skill.getSkillId(), player);
        }
        MessageUtils.send_to_player(player, SoulArmorMessage.ResUpSoulArmorSkillLevel.MsgID.eMsgID_VALUE, message.build().toByteArray());

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.SoulEquip);

        logger.info("魂甲觉醒等级 level={}  player={}", next.getLevel(), player);

        RoleGrowLog.create(player, GrowType.soulArmor_wake, 0, 0, oldLevel, next.getLevel(), null);
    }

    /**
     * 觉醒技能升级
     *
     * @param player
     * @param skillId
     */
    @Override
    public void reqChangeArmorSkill(Player player, int skillId) {

        Skill skill = player.getSoulArmor().getSkills().get(skillId);
        if (skill == null) {
            return;
        }
        Cfg_SoulArmor_awaken_skill_Bean bean = CfgManager.getCfg_SoulArmor_awaken_skill_Container().getValueByKey(skill.getSkillId());
        if (bean == null || bean.getNextSkill() <= 0) {
            return;
        }
        if (!Manager.backpackManager.manager().canDeleteItemNum(player, bean.getConsumeSkill(), 1)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MaterialNotEnough);
            return;
        }
        long logId = IDConfigUtil.getLogId();
        for (ReadArray<Integer> cost : bean.getConsumeSkill().getValuees()) {
            Manager.backpackManager.manager().onRemoveItem(player, cost.get(0), cost.get(1), ItemChangeReason.SoulArmorUpSkillDec, logId);
        }
        player.getSoulArmor().getSkills().remove(skillId);
        Manager.skillManager.removeSkill(player, skill.getSkillId());

        skill.setSkillId(bean.getNextSkill());
        Manager.skillManager.addSkill(player, skill.getSkillId());
        player.getSoulArmor().getSkills().put(skill.getSkillId(), skill);

        SoulArmorMessage.ResChangeSoulArmorSkill.Builder message = SoulArmorMessage.ResChangeSoulArmorSkill.newBuilder();
        message.setOldId(skillId);
        message.setSkillId(skill.getSkillId());
        MessageUtils.send_to_player(player, SoulArmorMessage.ResChangeSoulArmorSkill.MsgID.eMsgID_VALUE, message.build().toByteArray());

        logger.info("魂甲觉醒技能升级 old={} new={}  player={}", skillId, skill.getSkillId(), player);
    }

    /**
     * 强化魂印孔位
     *
     * @param player
     * @param slotId
     */
    @Override
    public void reqUpSoulArmorSlotLevel(Player player, int slotId) {
        SoulArmorSlot slot = player.getSoulArmor().getSlots().get(slotId);
        if (slot == null) {
            return;
        }
        int id = slotId * 10000 + slot.getLevel();
        Cfg_SoulArmor_signet_intensify_Bean next = CfgManager.getCfg_SoulArmor_signet_intensify_Container().getValueByKey(id + 1);
        if (next == null) {
            return;
        }
        Cfg_SoulArmor_signet_intensify_Bean bean = CfgManager.getCfg_SoulArmor_signet_intensify_Container().getValueByKey(id);
        if (!Manager.backpackManager.manager().canDeleteItemNum(player, bean.getConsume(), 1)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MaterialNotEnough);
            return;
        }
        long logId = IDConfigUtil.getLogId();
        for (ReadArray<Integer> cost : bean.getConsume().getValuees()) {
            Manager.backpackManager.manager().onRemoveItem(player, cost.get(0), cost.get(1), ItemChangeReason.SoulArmorUpSlotDec, logId);
        }
        slot.setLevel(slot.getLevel() + 1);

        sendUpdateSoulArmorBallSlot(player, slot);

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.SoulEquip);

        logger.info("魂印孔强化 slot={}  player={}", slot, player);

        Cfg_Equip_Bean config = CfgManager.getCfg_Equip_Container().getValueByKey(slot.getBall().getItemModelId());
        RoleGrowLog.create(player, GrowType.soulArmor_equip_intensify, config, slot.getLevel(), 0);
    }

    /**
     * 穿戴魂印
     *
     * @param player
     * @param slotId
     * @param id
     */
    @Override
    public void reqWearSoulArmorBall(Player player, int slotId, long id) {

        SoulArmorSlot slot = player.getSoulArmor().getSlots().get(slotId);
        if (slot == null) {
            return;
        }
        Cfg_SoulArmor_signet_hole_Bean bean = CfgManager.getCfg_SoulArmor_signet_hole_Container().getValueByKey(slot.getSlotId());
        if (slot.getBall() != null) {
            reqUnWearSoulArmorBall(player, slotId);
        }
        Item ball = player.getSoulArmor().getBag().get(id);
        if (ball == null) {
            return;
        }
        Cfg_Equip_Bean config = CfgManager.getCfg_Equip_Container().getValueByKey(ball.getItemModelId());
        if (config == null || config.getPart() != bean.getEquipType()) {
            return;
        }
        slot.setBall(ball);
        player.getSoulArmor().getBag().remove(id);

        sendSoulBallDec(player, ItemChangeReason.SoulArmorWearDec, id);

        sendUpdateSoulArmorBallSlot(player, slot);

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.SoulEquip);

        logger.info("魂印穿戴 slotId={} ball={} player={}", slotId, ball, player);

        RoleGrowLog.create(player, GrowType.soulArmor_equip_wear, config, slot.getLevel(), 0);
    }

    /**
     * 魂印合成
     *
     * @param player
     * @param slotId
     * @param equipsList
     */
    @Override
    public void reqSoulArmorMerge(Player player, int slotId, List<Long> equipsList) {
        SoulArmorSlot slot = player.getSoulArmor().getSlots().get(slotId);
        if (slot == null || equipsList.isEmpty()) {
            return;
        }
        Item ball = slot.getBall();
        if (ball == null) {
            return;
        }
        Cfg_SoulArmor_equip_synthesis_Bean ballBean = CfgManager.getCfg_SoulArmor_equip_synthesis_Container().getValueByKey(ball.getItemModelId());
        Cfg_Equip_Bean target = CfgManager.getCfg_Equip_Container().getValueByKey(ballBean.getEquip_ID());
        if (target == null) {
            logger.warn("魂印合成错误的配置 ball={}", ball.getItemModelId());
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        if (ballBean.getJoin_item().size() >= 2) {
            //特殊道具是否满足
            int itemId = ballBean.getJoin_item().get(0);
            int count = ballBean.getJoin_item().get(1);
            if (!Manager.backpackManager.manager().onRemoveItem(player, itemId, count, ItemChangeReason.SoulArmorBallMergeDec, actionId)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Item_Not_Enough, Manager.backpackManager.manager().getName(itemId));
                return;
            }
        }
        List<Long> decBall = new ArrayList<>();
        long totalRate = 0;
        for (long id : equipsList) {
            Item item = player.getSoulArmor().getBag().get(id);
            if (item == null) {
                continue;
            }
            Cfg_Equip_Bean costBean = CfgManager.getCfg_Equip_Container().getValueByKey(item.getItemModelId());
            //部位是否满足
            if (!ballBean.getJoin_part().contains(costBean.getPart())) {
                continue;
            }
            //品质是否满足
            if (!ballBean.getQuality().contains(costBean.getQuality())) {
                continue;
            }
            //星级是否满足
            if (!ballBean.getDiamond().contains(costBean.getDiamond_Number())) {
                continue;
            }
            //职业是否满足
            if (!costBean.getGender().contains(ballBean.getProfessional())) {
                continue;
            }
            totalRate += (long) gainMergeRate(ballBean.getQuality(), ballBean.getQuality_Number(), costBean.getQuality())
                    * gainMergeRate(ballBean.getDiamond(), ballBean.getDiamond_Number(), costBean.getDiamond_Number())
                    * ballBean.getJoin_num_probability();
            decBall.add(id);
            player.getSoulArmor().getBag().remove(id);
            Manager.backpackManager.manager().writeItemLogAndBI(player, item.getNum(), 0, item, ItemChangeReason.SoulArmorBallMergeDec, actionId);
        }

        boolean success = RandomUtils.defaultIsGenerate((int) (totalRate / 10000_0000));
        if (success) {
            ball.setItemModelId(target.getId());
            Manager.backpackManager.manager().writeItemLogAndBI(player, 1, 0, ball, ItemChangeReason.SoulArmorBallMergeGet, actionId);
        }
        //公告
        if (success && ballBean.getNotice() == 1) {
            MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, MessageString.EquipSynthetic,
                    player.getName(), Manager.backpackManager.manager().getChatInfo(ball),
                    Utils.makeUrlStr(MessageString.EquipSynthetic));
        }

        sendSoulBallDec(player, ItemChangeReason.SoulArmorWearDec, decBall.toArray(new Long[decBall.size()]));

        sendUpdateSoulArmorBallSlot(player, slot);

        SoulArmorMessage.ResSoulArmorMerge.Builder messge = SoulArmorMessage.ResSoulArmorMerge.newBuilder();
        messge.setResult(success);
        MessageUtils.send_to_player(player, SoulArmorMessage.ResSoulArmorMerge.MsgID.eMsgID_VALUE, messge.build().toByteArray());
        logger.info("魂印合成success={}{}->{} player={}", success, ballBean.getId(), target.getId(), player);
    }

    /**
     * 魂甲ID
     *
     * @param player
     * @param soulArmorId
     */
    @Override
    public void wearSoulArmor(Player player, int soulArmorId) {
        MapMessage.ResSoulEquipChange.Builder message = MapMessage.ResSoulEquipChange.newBuilder();
        message.setPlayerId(player.getId());
        if (soulArmorId > 0 && soulArmorId <= player.getSoulArmor().gainQualityLevel()) {
            player.getSoulArmor().setWearId(soulArmorId);
        }
        message.setSoulArmorId(player.getSoulArmor().getWearId());
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResSoulEquipChange.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    //获取类型对应的概率
    int gainMergeRate(ReadArray<Integer> iArr, ReadArray<Integer> rArr, int type) {
        for (int i = 0; i < iArr.size(); i++) {
            if (iArr.get(i) == type) {
                return rArr.get(i);
            }
        }
        return 0;
    }

    /**
     * 同步魂甲信息
     *
     * @param player
     */
    @Override
    public void sendSoulArmorInfo(Player player) {
        List<SoulArmorMessage.SoulArmorBallSlot> slots = new ArrayList<>();
        for (Cfg_SoulArmor_signet_hole_Bean bean : CfgManager.getCfg_SoulArmor_signet_hole_Container().getValuees()) {
            SoulArmorMessage.SoulArmorBallSlot.Builder mSlot = SoulArmorMessage.SoulArmorBallSlot.newBuilder();
            SoulArmorSlot slot = player.getSoulArmor().getSlots().get(bean.getHole());
            if (slot != null) {
                mSlot.setIsOpen(true);
                mSlot.setSlot(slot.getSlotId());
                mSlot.setLevel(slot.getLevel());
                if (slot.getBall() != null)
                    mSlot.setBall(pack(slot.getBall()));
                slots.add(mSlot.build());
                continue;
            }
            if (Manager.controlManager.deal().checkFuncProgress(player, bean.getOpen())) {
                slot = new SoulArmorSlot();
                slot.setSlotId(bean.getHole());
                player.getSoulArmor().getSlots().put(slot.getSlotId(), slot);
                mSlot.setIsOpen(true);
                mSlot.setSlot(slot.getSlotId());
                mSlot.setLevel(slot.getLevel());
                logger.info("激活魂甲.魂印孔位 slot={} player={}", slot, player);
            } else {
                mSlot.setIsOpen(false);
                mSlot.setSlot(bean.getHole());
                mSlot.setLevel(0);
            }
            slots.add(mSlot.build());
        }

        SoulArmorMessage.ResSoulArmor.Builder message = SoulArmorMessage.ResSoulArmor.newBuilder();
        message.setLevel(player.getSoulArmor().getLevel());
        message.setQualityLevel(player.getSoulArmor().getQualityLevel());
        message.setSkillLevel(player.getSoulArmor().getSkillLevel());
        message.addAllSlots(slots);
        player.getSoulArmor().getSkills().values().forEach(skill -> message.addSkillList(skill.getSkillId()));
        MessageUtils.send_to_player(player, SoulArmorMessage.ResSoulArmor.MsgID.eMsgID_VALUE, message.build().toByteArray());

        logger.info("发送魂甲信息 player={} ", player);
    }

    backpackMessage.ItemInfo.Builder pack(Item item) {
        backpackMessage.ItemInfo.Builder ball = backpackMessage.ItemInfo.newBuilder();
        ball.setItemId(item.getId());
        ball.setItemModelId(item.getItemModelId());
        ball.setNum(item.getNum());
        return ball;
    }

    /**
     * 魂甲背包能否添加道具
     *
     * @param player
     * @param items
     */
    @Override
    public boolean canAdd(Player player, List<Item> items) {
        return player.getSoulArmor().getBag().size() + items.size() <= Global.Born_Bag_Num.get(1);
    }

    /**
     * 添加魂印
     *
     * @param player
     * @param item
     * @param reason
     * @return
     */
    @Override
    public boolean addSoulArmorEquip(Player player, Item item, int reason) {
        player.getSoulArmor().getBag().put(item.getId(), item);

        SoulArmorMessage.ResAddSoulArmorBall.Builder message = SoulArmorMessage.ResAddSoulArmorBall.newBuilder();
        message.setReason(reason);
        message.addBalls(pack(item));
        MessageUtils.send_to_player(player, SoulArmorMessage.ResAddSoulArmorBall.MsgID.eMsgID_VALUE, message.build().toByteArray());

        Manager.backpackManager.manager().writeItemLogAndBI(player, 0, item.getNum(), item, reason, IDConfigUtil.getLogId());
        return true;
    }

    /**
     * 检测孔位激活
     *
     * @param player
     */
    @Override
    public void checkActiveSlot(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.SoulEquip)) {
            return;
        }
        if (!player.getSoulArmor().isOpen()) {
            //功能开放
            player.getSoulArmor().setOpen(true);

            Manager.newFashionManager.deal().huaxingActivateFashion(player, NewFashionManager.SOUL_TYPE, player.getSoulArmor().getQualityLevel());
            wearSoulArmor(player, player.getSoulArmor().getQualityLevel());
            logger.info("魂甲系统激活 player={}", player);
        }
        for (Cfg_SoulArmor_signet_hole_Bean bean : CfgManager.getCfg_SoulArmor_signet_hole_Container().getValuees()) {
            SoulArmorSlot slot = player.getSoulArmor().getSlots().get(bean.getHole());
            if (slot != null) {
                continue;
            }
            if (Manager.controlManager.deal().checkFuncProgress(player, bean.getOpen())) {
                slot = new SoulArmorSlot();
                slot.setSlotId(bean.getHole());
                player.getSoulArmor().getSlots().put(slot.getSlotId(), slot);
                logger.info("激活魂甲.魂印孔位 slot={} player={}", slot, player);

                sendUpdateSoulArmorBallSlot(player, slot);
            }
        }
    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.SoulArmorScript;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }
}
