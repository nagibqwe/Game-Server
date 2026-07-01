package common.huaxinflysword;

import com.data.*;
import com.data.bean.Cfg_FlySword_Grave_Bean;
import com.data.bean.Cfg_Sword_soul_copy_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.count.structs.VariantType;
import com.game.huaxinflysword.script.ISwordSoulScript;
import com.game.huaxinflysword.structs.FlyswordAllInfo;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.GlobalType;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.vip.manager.VipManager;
import com.game.vip.structs.VipPower;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage;
import game.message.HuaxinFlySwordMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chauncy
 * @create 2020/7/15 20:01
 */
public class SwordSoulScript implements ISwordSoulScript {

    private static final Logger logger = LogManager.getLogger(SwordSoulScript.class);

    @Override
    public int getId() {
        return ScriptEnum.SwordSoulTowerScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void initHookInfo(Player player) {
        FlyswordAllInfo info = player.getFlyswordAllInfo();
        info.setHookStartTime((int) (TimeUtils.Time() / 1000));
        info.setCurHookStartTime(info.getHookStartTime());
        info.getHookTimeInfo().clear();
    }

    @Override
    public void onReqSwordSoulPanel(Player player) {
        FlyswordAllInfo info = player.getFlyswordAllInfo();
        int count = (int) Manager.countManager.getVariant(player, VariantType.HookQuickEarn);
        int maxCount = Manager.vipManager.power().getVipPurNum(player, VipPower.POWER_36);
        int remainCount = Math.max(maxCount - count, 0);
        int hookTime = Math.min(GlobalType.SECOND_PER_DAY, (int)(TimeUtils.Time() / 1000 - info.getHookStartTime()));

        HuaxinFlySwordMessage.ResSwordSoulPannel.Builder builder = HuaxinFlySwordMessage.ResSwordSoulPannel.newBuilder();
        builder.setLayer(info.getSwordSoulLayer());
        builder.setHookTime(hookTime);
        builder.setRemainCount(remainCount);
        builder.setMaxCount(maxCount);
        MessageUtils.send_to_player(player, HuaxinFlySwordMessage.ResSwordSoulPannel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onReqGetHookReward(Player player) {
        FlyswordAllInfo info = player.getFlyswordAllInfo();
        int now = (int) (TimeUtils.Time() / 1000);
        int endTime = Math.min(now, info.getHookStartTime() + GlobalType.SECOND_PER_DAY);
        int earningTime = endTime - info.getCurHookStartTime();
        int count = earningTime / Global.Sword_Soul_SingleRewordTime;
        int remainTime = earningTime % Global.Sword_Soul_SingleRewordTime;
        if (count > 0) {
            Cfg_Sword_soul_copy_Bean bean = CfgManager.getCfg_Sword_soul_copy_Container().getValueByKey(info.getSwordSoulLayer());
            if (bean == null) {
                logger.error("Cfg_Sword_soul_copy_Bean配置表不存在：" + info.getSwordSoulLayer());
                return;
            }
            for (int i = 0; i < bean.getMandate_single_reward().size(); i++) {
                ReadArray<Integer> array = bean.getMandate_single_reward().get(i);
                int lastCount = info.getHookTimeInfo().getOrDefault(array.get(0), 0);
                info.getHookTimeInfo().put(array.get(0), array.get(1) * count + lastCount);
            }
        }
        if (info.getHookTimeInfo().size() <= 0) {
            return;
        }

        Manager.countManager.addVariant(player, VariantType.Daily_SwordSoul_Times, 1);
        Manager.controlManager.operate(player, FunctionVariable.Daily_SwordSoul_Times, 1);

        List<Item> itemList = new ArrayList<>();
        HuaxinFlySwordMessage.ResGetHookReward.Builder builder = HuaxinFlySwordMessage.ResGetHookReward.newBuilder();
        builder.setHookTime(earningTime - remainTime);
        for (Map.Entry<Integer, Integer> entry : info.getHookTimeInfo().entrySet()) {
            Item item = Item.createItem(entry.getKey(), entry.getValue(), true);
            itemList.add(item);
            CopyMapMessage.cardItemInfo.Builder itemInfo = CopyMapMessage.cardItemInfo.newBuilder();
            itemInfo.setItemId(item.getItemModelId());
            itemInfo.setNum(item.getNum());
            itemInfo.setBind(true);
            builder.addItemList(itemInfo);
        }
        if (!info.isFrist_reward()){
             List<Item> first_Rewards =  Item.createItems( Global.Sword_soul_copy_frist_reward);
            for (Item item : first_Rewards) {
                CopyMapMessage.cardItemInfo.Builder itemInfo = CopyMapMessage.cardItemInfo.newBuilder();
                itemInfo.setItemId(item.getItemModelId());
                itemInfo.setNum(item.getNum());
                itemInfo.setBind(true);
                builder.addItemList(itemInfo);
            }
            itemList.addAll(first_Rewards);
            info.setFrist_reward(true);
        }
        info.setHookStartTime(now - remainTime);
        info.setCurHookStartTime(info.getHookStartTime());
        info.getHookTimeInfo().clear();

        long actionId = IDConfigUtil.getLogId();
        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.SwordSoulTowerGet, actionId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System, MessageString.System
                    , MessageString.BAG_FULL_MAIL_CONTENT, itemList, ItemChangeReason.SwordSoulTowerGet, actionId);
        }
        MessageUtils.send_to_player(player, HuaxinFlySwordMessage.ResGetHookReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        onReqSwordSoulPanel(player);
    }

    @Override
    public void onReqQuickEarn(Player player) {
        //增加VIP宝珠状态检查
        if(!player.getVipPearl().canFree()){
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.C_Vip_Power_No_Ues_Notice);
            return;
        }
        int maxCount = Manager.vipManager.power().getVipPurNum(player, VipPower.POWER_36);
        int count = (int) Manager.countManager.getVariant(player, VariantType.HookQuickEarn);
        if (count >= maxCount) {
            logger.error("次数不足");
            return;
        }

        FlyswordAllInfo info = player.getFlyswordAllInfo();
        Cfg_Sword_soul_copy_Bean bean = CfgManager.getCfg_Sword_soul_copy_Container().getValueByKey(info.getSwordSoulLayer());
        if (bean == null) {
            logger.error("Cfg_Sword_soul_copy_Bean配置表不存在：" + info.getSwordSoulLayer());
            return;
        }

        long actionId = IDConfigUtil.getLogId();
        int costNum = Manager.vipManager.power().getVipAddNumPrice(count + 1, VipPower.POWER_36);
        if (costNum > 0) {
            if (!Manager.currencyManager.manager().decGold(player, costNum, ItemChangeReason.SwordSoulQuickRewardGet, actionId)) {
                logger.error("元宝不足");
                return;
            }
        }
        Manager.countManager.addVariant(player, VariantType.HookQuickEarn, 1);
        List<Item> itemList = Item.createItems(bean.getMandate_single_reward(), Global.Sword_Soul_quicktimes);
        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.SwordSoulQuickRewardGet, actionId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System, MessageString.System
                    , MessageString.BAG_FULL_MAIL_CONTENT, itemList, ItemChangeReason.SwordSoulQuickRewardGet, actionId);
        }

        HuaxinFlySwordMessage.ResQuickEarn.Builder builder = HuaxinFlySwordMessage.ResQuickEarn.newBuilder();
        builder.setRemainCount(maxCount - count - 1);
        for (Item item : itemList) {
            CopyMapMessage.cardItemInfo.Builder itemInfo = CopyMapMessage.cardItemInfo.newBuilder();
            itemInfo.setItemId(item.getItemModelId());
            itemInfo.setNum(item.getNum());
            itemInfo.setBind(true);
            builder.addItemList(itemInfo);
        }
        MessageUtils.send_to_player(player, HuaxinFlySwordMessage.ResQuickEarn.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        Manager.biManager.getScript().biRoomResult(player, bean.getNum());
    }

    @Override
    public void finishChallengeTower(Player player, int layer) {
        FlyswordAllInfo info = player.getFlyswordAllInfo();
        if (info.getHookStartTime() == 0) {
            logger.error("剑灵挂机数据错误！");
            info.setHookStartTime((int) (TimeUtils.Time() / 1000));
        }
        if (layer != info.getSwordSoulLayer()) {
            logger.error("剑灵阁层数参数错误");
            return;
        }
        //第一次通过 -300秒  策划特殊要求
        if (layer == 1){
            info.setHookStartTime(info.getHookStartTime() - Global.Sword_Soul_SingleRewordTime);
            info.setCurHookStartTime(info.getCurHookStartTime()-Global.Sword_Soul_SingleRewordTime);
        }
        info.setSwordSoulLayer(layer + 1);
        Manager.controlManager.operate(player, FunctionVariable.Soul_copy_Num, 0);

        //每次通关刷新挂机收益数据
        int now = (int) (TimeUtils.Time() / 1000);
        if (now - info.getHookStartTime() > GlobalType.SECOND_PER_DAY) {
            logger.info("收益时间已满");
            return;
        }
        int intervalTime = now - info.getCurHookStartTime();
        int count = intervalTime / Global.Sword_Soul_SingleRewordTime;
        if (count == 0) {
            return;
        }
        Cfg_Sword_soul_copy_Bean bean = CfgManager.getCfg_Sword_soul_copy_Container().getValueByKey(layer - 1);
        if (bean == null) {
            return;
        }
        int remainTime = intervalTime % Global.Sword_Soul_SingleRewordTime;
        info.setCurHookStartTime(now - remainTime);
        for (int i = 0; i < bean.getMandate_single_reward().size(); i++) {
            ReadArray<Integer> array = bean.getMandate_single_reward().get(i);
            int lastCount = info.getHookTimeInfo().getOrDefault(array.get(0), 0);
            info.getHookTimeInfo().put(array.get(0), array.get(1) * count + lastCount);
        }
    }

    @Override
    public void onReqSwordTombPanel(Player player) {
        FlyswordAllInfo info = player.getFlyswordAllInfo();
        HuaxinFlySwordMessage.ResSwordTomb.Builder builder = HuaxinFlySwordMessage.ResSwordTomb.newBuilder();
        for (Cfg_FlySword_Grave_Bean bean : CfgManager.getCfg_FlySword_Grave_Container().getValuees()) {
            builder.addId(bean.getId());
            builder.addState(info.getSwaordTombInfo().getOrDefault(bean.getId(), 0));
        }
        MessageUtils.send_to_player(player, HuaxinFlySwordMessage.ResSwordTomb.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onReqSwordTombWakeUp(Player player, int id) {
        FlyswordAllInfo info = player.getFlyswordAllInfo();
        Integer state = info.getSwaordTombInfo().getOrDefault(id, 0);
        if (state != 1) {
            logger.info("当前层未完成或已觉醒：" + id + "," + state);
            return;
        }
        Cfg_FlySword_Grave_Bean bean = CfgManager.getCfg_FlySword_Grave_Container().getValueByKey(id);
        if (bean == null) {
            logger.error("Cfg_FlySword_Grave_Bean配置表不存在：" + id);
            return;
        }
        info.getSwaordTombInfo().put(id, 2);
        Manager.huaxinFlySwordManager.deal().onReqUseHuxin(player, bean.getFlySword_id(), 1, false);

        HuaxinFlySwordMessage.ResSwordTombChange.Builder builder = HuaxinFlySwordMessage.ResSwordTombChange.newBuilder();
        builder.setId(id);
        builder.setState(2);
        MessageUtils.send_to_player(player, HuaxinFlySwordMessage.ResSwordTombChange.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }


    @Override
    public void onReqSkipSoulCopy(Player player) {

        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map.getZoneModelId() == 500001) {
            logger.error("关卡中不能跳过");
            return;
        }

        FlyswordAllInfo info = player.getFlyswordAllInfo();
        List<Item> itemList = new ArrayList<>();
        int count = 0;
        for (int i = info.getSwordSoulLayer() ;i < CfgManager.getCfg_Sword_soul_copy_Container().size();i++){
            Cfg_Sword_soul_copy_Bean bean = CfgManager.getCfg_Sword_soul_copy_Container().getValueByKey(i);
            if (bean == null){
                logger.info("Cfg_Sword_soul_copy_Bean配置表不存在：" + i);
                break;
            }
            if (bean.getPass_power() <= 0){
                logger.info("该关卡不能跳过：" + i);
                break;
            }
            if (player.getFightPoint() < bean.getPass_power()){
                logger.info("跳关战力不足 : "+bean.getPass_power());
                break;
            }
            itemList.addAll(Item.createItems(bean.getReward()));
            itemList.addAll(getLayerReward(i));
            finishChallengeTower(player,i);
            count++;
        }
        if (count<=0){
            return;
        }
        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.CopyMapGet, 500001)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System, MessageString.System, MessageString.BAG_FULL_MAIL_CONTENT, itemList, ItemChangeReason.CopyMapGet);
        }

        HuaxinFlySwordMessage.ResSkipSoulCopyResult.Builder msg = HuaxinFlySwordMessage.ResSkipSoulCopyResult.newBuilder();
        msg.setLayer(info.getSwordSoulLayer());
        for (Item item : itemList) {
            CopyMapMessage.cardItemInfo.Builder itemInfo = CopyMapMessage.cardItemInfo.newBuilder();
            itemInfo.setItemId(item.getItemModelId());
            itemInfo.setNum(item.getNum());
            itemInfo.setBind(true);
            msg.addItemList(itemInfo);
        }
        MessageUtils.send_to_player(player, HuaxinFlySwordMessage.ResSkipSoulCopyResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private List<Item> getLayerReward(int layer) {
        List<Item> immortalRewardList = new ArrayList<>();
        Cfg_Sword_soul_copy_Bean bean = CfgManager.getCfg_Sword_soul_copy_Container().getValueByKey(layer);
        if (bean == null) {
            logger.error("发送奖励，Cfg_Sword_soul_copy_Bean配置表存在：" + layer);
            return immortalRewardList;
        }
        for (int i = 0; i < bean.getImmortal_soul_reward().size(); i++) {
            List<Integer> subTypeList = new ArrayList<>();
            ReadArray<Integer> exArray = bean.getImmortal_soul_reward_exclusive().get(i);
            for (int j = 0; j < exArray.size(); j++) {
                int type = exArray.get(j);
                if (type <= bean.getReward_type()) {
                    subTypeList.add(type);
                }
            }
            ReadArray<Integer> array = bean.getImmortal_soul_reward().get(i);
            int num = array.get(0);
            int type = array.get(1);
            int quality = array.get(2);
            int subType = 0;
            for (int j = 0; j < num; j++) {
                if (type != 2) {
                    int random = RandomUtils.random(subTypeList.size());
                    subType = subTypeList.get(random);
                }
                int immortalCfgId = buildImmortalId(type, quality, subType);
                immortalRewardList.add(Item.createItem(immortalCfgId, 1, false));
            }
        }
        return immortalRewardList;
    }
    private int buildImmortalId(int type, int quality, int subType) {
        return 6000000 + type * 100000 + quality * 1000 + subType;
    }



}
