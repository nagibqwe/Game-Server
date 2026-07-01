package common.gem;

import com.data.*;
import com.data.bean.Cfg_GemRefining_Bean;
import com.data.bean.Cfg_GemstoneInlay_Bean;
import com.data.bean.Cfg_Item_Bean;
import com.data.container.Cfg_GemRefining_Container;
import com.data.container.Cfg_GemstoneInlay_Container;
import com.data.container.Cfg_Item_Container;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.backpack.structs.ItemEffectType;
import com.game.backpack.structs.ItemTypeConst;
import com.game.db.bean.RankPlayer;
import com.game.equip.log.GemInlayLog;
import com.game.equip.log.GemRefineLog;
import com.game.equip.manager.GemManager;
import com.game.equip.script.IGemScript;
import com.game.equip.struct.EquipPart;
import com.game.equip.struct.GemInfo;
import com.game.log.grow.GrowType;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.ranklist.manager.RankListManager;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.message.EquipMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class GemScript implements IGemScript {

    private static final Logger logger = LogManager.getLogger(GemScript.class);

    @Override
    public int getId() {
        return ScriptEnum.GemBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void initGemInfo(Player player) {
        int partNo;
        GemInfo gemInfo;
        List<EquipPart> equipParts = player.getEquipParts();
        for (EquipPart part : equipParts) {
            partNo = part.getType();

            //初始化宝石孔位
            gemInfo = part.getGemInfo();
            if (gemInfo.getGemIds() == null || gemInfo.getGemIds().isEmpty()) {
                Cfg_GemstoneInlay_Bean bean = Cfg_GemstoneInlay_Container.GetInstance().getValueByKey(1000 + partNo);
                if (bean == null) {
                    logger.error("Cfg_GemstoneInlayBean配置表不存在：" + (1000 + partNo));
                    continue;
                }
                gemInfo.setColor(bean.getColor_type());
                gemInfo.setGemIds(new ArrayList<>(bean.getLimit_Number()));
                ReadIntegerArrayEs condition = bean.getLocation_condition();
                for (int i = 0; i < bean.getLimit_Number(); i++) {
                    boolean open = true;
                    if (condition.size() > i) {
                        //检查开启条件
                        open = Manager.controlManager.deal().checkFuncProgress(player, condition.get(i));
                    }
                    gemInfo.getGemIds().add(open ? 0 : -1);
                    if (open) {
                        //宝石孔位开启日志
                        writeGemInlayLog(player, partNo, GemManager.Gem_Type, i, 0, -1, 0);
                    }
                }
            }

            //初始化仙玉孔位
            if (gemInfo.getJadeIds() == null || gemInfo.getJadeIds().isEmpty()) {
                Cfg_GemstoneInlay_Bean bean = Cfg_GemstoneInlay_Container.GetInstance().getValueByKey(2000 + partNo);
                if (bean == null) {
                    logger.error("Cfg_GemstoneInlayBean配置表不存在：" + (2000 + partNo));
                    continue;
                }
                gemInfo.setJadeIds(new ArrayList<>(bean.getLimit_Number()));
                ReadIntegerArrayEs condition = bean.getLocation_condition();

                for (int i = 0; i < bean.getLimit_Number(); i++) {
                    boolean open = true;
                    if (condition.size() > i) {
                        //检查开启条件
                        open = Manager.controlManager.deal().checkFuncProgress(player, condition.get(i));
                    }
                    gemInfo.getJadeIds().add(open ? 0 : -1);
                    if (open) {
                        //仙玉孔位开启日志
                        writeGemInlayLog(player, partNo, GemManager.Jade_Type, i, 0, -1, 0);
                    }
                }
            }
        }

        Manager.equipManager.sendEquipPartInfoToClient(player);
    }

    /**
     * 检查孔位开启情况
     */
    @Override
    public void checkOpenState(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.LianQiGem)) {
            return;
        }

        List<EquipPart> equipParts = player.getEquipParts();
        Cfg_GemstoneInlay_Bean[] list = Cfg_GemstoneInlay_Container.GetInstance().getValuees();
        for (Cfg_GemstoneInlay_Bean bean : list) {
            int partNo = bean.getId() % 1000;
            int type = bean.getId() / 1000;
            EquipPart part = equipParts.get(partNo);
            if (part == null) {
                logger.error("宝石孔位检查，玩家不存在此部位：" + partNo);
                continue;
            }
            List<Integer> gemList;
            switch (type) {
                case GemManager.Gem_Type:
                    gemList = part.getGemInfo().getGemIds();
                    break;
                case GemManager.Jade_Type:
                    gemList = part.getGemInfo().getJadeIds();
                    break;
                default:
                    logger.error("错误的类型：" + type);
                    continue;
            }
            ReadIntegerArrayEs condition = bean.getLocation_condition();
            //检查每个孔位的开启情况

            for (int i = 0; i < condition.size(); i++) {
                boolean open = Manager.controlManager.deal().checkFuncProgress(player, condition.get(i));
                Integer gemId = gemList.get(i);
                //达到条件解锁
                if (open && gemId == -1) {
                    gemList.set(i, 0);
                    sendGemUpdateMsg(player, type, partNo, i, 0);
                    //宝石孔位开启日志
                    writeGemInlayLog(player, partNo, type, i, 0, -1, 0);
                }
                //已镶嵌又不满足条件
                if (!open && gemId >= 0) {
                    gemList.set(i, -1);
                    sendGemUpdateMsg(player, type, partNo, i, -1);
                    if (gemId > 0) {
                        Item item = Item.createItem(gemId, 1, false);
                        int reason = type == GemManager.Gem_Type ? ItemChangeReason.GemInlayDown : ItemChangeReason.JadeInlayDown;
                        long actionId = IDConfigUtil.getLogId();
                        //卸下
                        if (!Manager.backpackManager.manager().addItem(player, item, reason, actionId)) {
                            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail
                                    , MessageString.System, MessageString.BAGISSPACETOMAIL
                                    , MessageString.GetAwardNotEnoughSpaceContent, Collections.singletonList(item), reason, actionId);
                        }
                        //宝石卸下日志
                        writeGemInlayLog(player, partNo, type, i, 4, gemId, -1);
                    }
                }
            }
        }
    }

    @Override
    public void onReqInlay(Player player, EquipMessage.ReqInlay mess) {
        List<EquipPart> equipParts = player.getEquipParts();
        EquipPart equipPart = equipParts.get(mess.getPart());
        if (equipPart == null || equipPart.getEquip() == null) {
            logger.error("不存在此部位或未穿戴装备：" + mess.getPart());
            return;
        }
        List<Integer> inlayInfo;
        switch (mess.getType()) {
            case GemManager.Gem_Type:
                inlayInfo = equipPart.getGemInfo().getGemIds();
                break;
            case GemManager.Jade_Type:
                inlayInfo = equipPart.getGemInfo().getJadeIds();
                break;
            default:
                logger.error("镶嵌宝石传递错误的类型：" + mess.getType());
                return;
        }
        if (inlayInfo == null) {
            logger.error("宝石或仙玉信息未初始化！！！");
            return;
        }
        if (inlayInfo.get(mess.getGemIndex()) == null) {
            logger.error("该位置未解锁： type-" + mess.getType() + "  index-" + mess.getGemIndex());
            return;
        }
        if (!Manager.backpackManager.manager().canDeleteItemNum(player, mess.getGemId(), 1)) {
            logger.info("玩家未拥有该宝石：" + mess.getGemId());
            return;
        }

        Cfg_GemstoneInlay_Bean bean = Cfg_GemstoneInlay_Container.GetInstance().getValueByKey(mess.getType() * 1000 + mess.getPart());
        if (bean == null) {
            logger.error("Cfg_GemstoneInlayBean配置表不存在：" + mess.getType() * 1000 + mess.getPart());
            return;
        }
        boolean isRightId = false;
        for (int i = 0; i < bean.getGemstone_id().size(); i++) {
            if (bean.getGemstone_id().get(i) == mess.getGemId()) {
                isRightId = true;
                break;
            }
        }
        if (!isRightId) {
            logger.info("该部位不支持该宝石id" + mess.getPart());
            return;
        }

        long actionId = IDConfigUtil.getLogId();
        int reason = mess.getType() == GemManager.Gem_Type ? ItemChangeReason.GemInlayDown : ItemChangeReason.JadeInlayDown;
        //已有宝石先卸下
        int lastGemId = inlayInfo.get(mess.getGemIndex());
        if (lastGemId > 0) {
            Item item = Item.createItem(inlayInfo.get(mess.getGemIndex()), 1, false);
            if (!Manager.backpackManager.manager().addItem(player, item, reason, actionId)) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail
                        , MessageString.System, MessageString.BAGISSPACETOMAIL
                        , MessageString.GetAwardNotEnoughSpaceContent, Collections.singletonList(item), reason, actionId);
            }
        }

        //扣物品
        if (!Manager.backpackManager.manager().onRemoveItem(player, mess.getGemId(), 1, reason, actionId)) {
            logger.error("宝石镶嵌物品不足！");
            return;
        }
        inlayInfo.set(mess.getGemIndex(), mess.getGemId());
        Manager.rankListManager.deal().changeGemLv(player);
        //重新计算属性
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.GEM);
        Manager.controlManager.operate(player, FunctionVariable.GemLevelMax, 0);
        sendGemUpdateMsg(player, mess.getType(), mess.getPart(), mess.getGemIndex(), mess.getGemId());

        //宝石镶嵌或替换日志
        writeGemInlayLog(player, mess.getPart(), mess.getType(), mess.getGemIndex(), lastGemId == 0 ? 1 : 2, lastGemId, mess.getGemId());

        if(mess.getType() == GemManager.Gem_Type){
            getBIGemInfo(player, GrowType.gem_inlay.getAct_type(), equipPart, mess.getGemIndex(), mess.getGemId());
        }
    }

    @Override
    public void onReqQuickRefineGem(Player player, EquipMessage.ReqQuickRefineGem mess) {
        EquipPart equipPart = player.getEquipParts().get(mess.getPart());
        if (equipPart == null || equipPart.getEquip() == null) {
            logger.error("不存在该部位或未穿戴装备：" + mess.getPart());
            return;
        }
        boolean hasGem = equipPart.getGemInfo().getGemIds().stream().anyMatch(n -> n > 0);
        if (!hasGem) {
            logger.error("未镶嵌宝石");
            return;
        }

        int nextBeanId = equipPart.getType() * 1000 + equipPart.getGemInfo().getLevel() + 1;

        Cfg_GemRefining_Bean nextBean = Cfg_GemRefining_Container.GetInstance().getValueByKey(nextBeanId);
        if (nextBean == null) {
            logger.error("Cfg_GemRefiningBean不存在或已超过最大等级：" + nextBeanId);
            return;
        }
        Cfg_GemRefining_Bean bean = Cfg_GemRefining_Container.GetInstance().getValueByKey(mess.getPart() * 1000 + 1);
        boolean isRightItem = false;
        for (int i = 0; i < bean.getItemID().size(); i++) {
            if (bean.getItemID().get(i) == mess.getItemId()) {
                isRightItem = true;
                break;
            }
        }
        if (!isRightItem) {
            logger.error("错误的物品id:" + mess.getItemId());
            return;
        }
        int itemNum = Manager.backpackManager.manager().getItemNum(player, mess.getItemId());
        if (itemNum <= 0) {
            logger.error("物品不足！");
            return;
        }

        //获取实际的物品数量和经验
        Cfg_Item_Bean itemBean = Cfg_Item_Container.GetInstance().getValueByKey(mess.getItemId());
        if (itemBean == null || itemBean.getEffect_num().size() == 0
                || itemBean.getEffect_num().get(0).get(0) != ItemEffectType.GemExpItem) {
            logger.info("Cfg_ItemBean配置表找不到或使用类型配置错误：" + mess.getItemId());
            return;
        }

        //确保快速精炼当达到最大级经验溢出时，至多 多消耗一个材料
        //升到最高级所需的经验
        int maxNeedExp = 0;
        Cfg_GemRefining_Bean gemBean;
        for (int i = equipPart.getLevel()+1; i < 1000; i++) {
            gemBean = Cfg_GemRefining_Container.GetInstance().getValueByKey(equipPart.getType() * 1000 + i);
            if (gemBean == null) {
                break;
            }
            maxNeedExp += gemBean.getExp();
        }
        maxNeedExp -= equipPart.getGemInfo().getExp();
        logger.info("===============maxNeedExp:"+maxNeedExp);

        long expAdd = itemBean.getEffect_num().get(0).get(1);
        long exp = expAdd * itemNum;
        if (exp > maxNeedExp) {
            itemNum = (int) (maxNeedExp / expAdd + 1);
            exp = maxNeedExp;
            logger.info("===============itemNum:"+itemNum+",exp:"+exp);
        }
        long actionId = IDConfigUtil.getId();
        if (!Manager.backpackManager.manager().onRemoveItem(player, mess.getItemId(), itemNum, ItemChangeReason.GemRefineDec, actionId)) {
            logger.error("物品不足！");
            return;
        }
        int lastLevel = equipPart.getGemInfo().getLevel();
        int lastExp = equipPart.getGemInfo().getExp();
        addGemPartExp(equipPart, exp);
        int nowLevel = equipPart.getGemInfo().getLevel();
        int nowExp = equipPart.getGemInfo().getExp();
        //重新计算属性
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.GEM);

        //返回消息
        EquipMessage.ResQuickRefineGem.Builder builder = EquipMessage.ResQuickRefineGem.newBuilder();
        EquipMessage.gemRefine.Builder gemRefine = EquipMessage.gemRefine.newBuilder();
        gemRefine.setPart(equipPart.getType());
        gemRefine.setLevel(equipPart.getGemInfo().getLevel());
        gemRefine.setExp(equipPart.getGemInfo().getExp());
        builder.setResult(gemRefine);
        MessageUtils.send_to_player(player, EquipMessage.ResQuickRefineGem.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        //快速精炼日志
        writeGemRefineLog(player, mess.getPart(), 0, lastLevel, nowLevel, lastExp, nowExp, exp, actionId);

        getBIGemInfo(player, GrowType.gem_refine.getAct_type(), equipPart, 0, 0);
    }

    /**
     * 增加宝石部位经验
     *
     * @param part 部位
     * @param exp  经验值
     */
    private void addGemPartExp(EquipPart part, long exp) {
        int cfgId = part.getType() * 1000 + part.getGemInfo().getLevel() + 1;
        Cfg_GemRefining_Bean bean = Cfg_GemRefining_Container.GetInstance().getValueByKey(cfgId);
        int level = part.getGemInfo().getLevel();
        exp += part.getGemInfo().getExp();
        Cfg_GemRefining_Bean nextBean;
        while (bean.getExp() <= exp) {
            nextBean = Cfg_GemRefining_Container.GetInstance().getValueByKey(part.getType() * 1000 + level + 1);
            if (nextBean == null) {
                logger.error("Cfg_GemRefiningBean配置表不存在" + level);
                break;
            }
            level = bean.getLevel();
            exp -= bean.getExp();
            bean = nextBean;
        }
        part.getGemInfo().setLevel(level);
        part.getGemInfo().setExp((int) exp);
    }

    @Override
    public void onReqAutoRefineGem(Player player, EquipMessage.ReqAutoRefineGem mess) {
        EquipPart equipPart = player.getEquipParts().get(mess.getPart());
        if (equipPart == null) {
            logger.error("不存在该部位:" + mess.getPart());
            return;
        }
        EquipPart part = player.getEquipParts().stream()
                .filter(n -> n.getEquip() != null
                        && n.getGemInfo().getGemIds().stream().anyMatch(m -> m > 0)
                        && n.getGemInfo().getColor() == equipPart.getGemInfo().getColor())
                .min(Comparator.comparingInt(n -> n.getGemInfo().getLevel())).orElse(null);
        if (part == null) {
            return;
        }

        int beanId = part.getType() * 1000 + part.getGemInfo().getLevel() + 1;
        Cfg_GemRefining_Bean bean = Cfg_GemRefining_Container.GetInstance().getValueByKey(beanId);
        if (bean == null) {
            logger.error("Cfg_GemRefiningBean不存在或已超过最大等级：" + beanId);
            return;
        }

        GemInfo gemInfo = part.getGemInfo();
        int needExp = bean.getExp() - gemInfo.getExp();
        if (needExp <= 0) {
            logger.error("经验异常！！");
            return;
        }

        //拿到玩家精炼材料数量和经验值
        Cfg_Item_Bean itemBean;
        int[] useNum = new int[bean.getItemID().size()];
        int[] exp = new int[bean.getItemID().size()];
        for (int i = 0; i < bean.getItemID().size(); i++) {
            int itemNum = Manager.backpackManager.manager().getItemNum(player, bean.getItemID().get(i));
            itemBean = Cfg_Item_Container.GetInstance().getValueByKey(bean.getItemID().get(i));
            if (itemBean == null || itemBean.getEffect_num().size() == 0
                    || itemBean.getEffect_num().get(0).get(0) != ItemEffectType.GemExpItem) {
                logger.info("Cfg_ItemBean配置表找不到或使用类型配置错误：" + bean.getItemID().get(i));
                continue;
            }
            exp[i] = itemBean.getEffect_num().get(0).get(1);
            if (exp[i] * itemNum < needExp) {
                useNum[i] = itemNum;
            } else {
                useNum[i] = needExp % exp[i] == 0 ? needExp / exp[i] : needExp / exp[i] + 1;
                break;
            }
            needExp -= useNum[i] * exp[i];
        }

        //扣物品，加经验
        long actionId = IDConfigUtil.getLogId();
        for (int i = 0; i < useNum.length; i++) {
            Manager.backpackManager.manager().onRemoveItem(player, bean.getItemID().get(i), useNum[i], ItemChangeReason.GemRefineDec, actionId);
        }
        int lastLevel = part.getGemInfo().getLevel();
        int lastExp = part.getGemInfo().getExp();
        int addExp = exp[0] * useNum[0] + exp[1] * useNum[1] + exp[2] * useNum[2];
        addGemPartExp(part, addExp);
        int nowLevel = part.getGemInfo().getLevel();
        int nowExp = part.getGemInfo().getExp();
        //重新计算属性
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.GEM);

        //返回消息
        EquipMessage.ResAutoRefineGem.Builder builder = EquipMessage.ResAutoRefineGem.newBuilder();
        EquipMessage.gemRefine.Builder gemRefine = EquipMessage.gemRefine.newBuilder();
        gemRefine.setPart(part.getType());
        gemRefine.setLevel(gemInfo.getLevel());
        gemRefine.setExp(gemInfo.getExp());
        builder.addResult(gemRefine);
        MessageUtils.send_to_player(player, EquipMessage.ResAutoRefineGem.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        //智能精炼日志
        writeGemRefineLog(player, mess.getPart(), 1, lastLevel, nowLevel, lastExp, nowExp, addExp, actionId);

        getBIGemInfo(player, GrowType.gem_refine.getAct_type(), equipPart, 0, 0);
    }

    @Override
    public void onReqUpGradeGem(Player player, EquipMessage.ReqUpGradeGem mess) {

        EquipPart part = player.getEquipParts().get(mess.getPart());
        if (part == null || part.getEquip() == null) {
            return;
        }
        List<Integer> gemList;
        switch (mess.getType()) {
            case GemManager.Gem_Type:
                gemList = part.getGemInfo().getGemIds();
                break;
            case GemManager.Jade_Type:
                gemList = part.getGemInfo().getJadeIds();
                break;
            default:
                logger.error("错误的类型：" + mess.getType());
                return;
        }

        Integer gemId = gemList.get(mess.getIndex());
        if (gemId == null || gemId <= 0) {
            logger.error("该位置未镶嵌：" + mess.getIndex());
            return;
        }
        Cfg_Item_Bean bean = Cfg_Item_Container.GetInstance().getValueByKey(gemId);
        if (bean == null) {
            logger.error("Cfg_ItemBean不存在配置表：" + gemId);
            return;
        }
        ReadIntegerArray target = bean.getHechen_target();
        if (target == null || target.size() < 2 || target.get(1) < 1) {
            return;
        }

        Cfg_Item_Bean targetBean = Cfg_Item_Container.GetInstance().getValueByKey(target.get(0));
        if (targetBean == null || targetBean.getType() != ItemTypeConst.GEM) {
            logger.error("没有合成目标或目标不是宝石类型！ itemId：" + target.get(0));
            return;
        }

        //计算需扣除的元宝和物品
        int curLv = bean.getId() % 100;
        int needNum = target.get(1) - 1;
        Map<Integer, Integer> needItemMap = new HashMap<>();
        while (curLv >= 1) {
            int beanId = (bean.getId() / 100) * 100 + curLv;
            int itemNum = Manager.backpackManager.manager().getItemNum(player, beanId);
            if (itemNum > 0) {
                needItemMap.put(beanId, Math.min(needNum, itemNum));
            }
            needNum = Math.max(0, needNum - itemNum);
            if (needNum == 0) {
                break;
            }

            Cfg_Item_Bean tempBean = CfgManager.getCfg_Item_Container().getValueByKey(beanId - 1);
            if (tempBean == null) {
                break;
            }
            needNum *= tempBean.getHechen_target().get(1);
            curLv -= 1;
        }

        long actionId = IDConfigUtil.getLogId();
        if (needNum > 0) {
            Cfg_Item_Bean tempBean = CfgManager.getCfg_Item_Container().getValueByKey((bean.getId() / 100) * 100 + 1);
            if (tempBean.getItem_Price() == 0) {
                return;
            }
            int needGold = tempBean.getItem_Price() * needNum;
            if (!Manager.currencyManager.manager().canDecItemCoin(player, needGold, ItemCoinType.GemCoin)) {
                logger.info("元宝不足！");
                return;
            }
            Manager.currencyManager.manager().onDecItemCoin(player, needGold, ItemChangeReason.CompoundDec, actionId, ItemCoinType.GemCoin);
        }

        for (Map.Entry<Integer, Integer> entry : needItemMap.entrySet()) {
            Manager.backpackManager.manager().onRemoveItem(player, entry.getKey(), entry.getValue(), ItemChangeReason.CompoundDec, actionId);
        }
        Item item = Item.createItem(bean.getId(), 1, false);
        Item targetItem = Item.createItem(targetBean.getId(), 1, false);
        //替换原宝石id
        gemList.set(mess.getIndex(), targetItem.getItemModelId());
        //宝石消耗日志
        int sourceAfterNum = getGemNum(player, bean.getId(), mess.getType());
        Manager.backpackManager.manager().writeItemLogAndBI(player,sourceAfterNum + 1,sourceAfterNum, item, ItemChangeReason.CompoundDec, actionId);
        //宝石产生日志
        int targetAfterNum = getGemNum(player, targetBean.getId(), mess.getType());
        Manager.backpackManager.manager().writeItemLogAndBI(player,targetAfterNum - 1,targetAfterNum, targetItem, ItemChangeReason.CompoundGet, actionId);
        //重新计算属性
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.GEM);
        Manager.rankListManager.deal().changeGemLv(player);
        Manager.controlManager.operate(player, FunctionVariable.GemLevelMax, 0);
        sendGemUpdateMsg(player, mess.getType(), mess.getPart(), mess.getIndex(), targetBean.getId());

        //宝石升级日志
        writeGemInlayLog(player, mess.getPart(), GemManager.Gem_Type, mess.getIndex(), 3, gemId, targetBean.getId());
        if(mess.getType() == GemManager.Gem_Type)getBIGemInfo(player, GrowType.gem_level_up.getAct_type(), part, mess.getIndex(), targetBean.getId());
    }

    /**
     * 获取玩家宝石总数
     * @param player
     * @param itemModelId
     * @param type 宝石/翡翠
     * @return
     */
    private int getGemNum(Player player, int itemModelId, int type) {
        int count = 0;
        //镶嵌的数量
        for(EquipPart part : player.getEquipParts()){
            List<Integer> gs;
            if(type == GemManager.Gem_Type){
                gs = part.getGemInfo().getGemIds();
            }else {
                gs = part.getGemInfo().getJadeIds();
            }
            for(int id : gs){
                if(id == itemModelId){
                    count++;
                }
            }
        }
        //背包的数量
        int backNum = Manager.backpackManager.manager().getItemNum(player,itemModelId);
        return count + backNum;
    }

    private void sendGemUpdateMsg(Player player, int type, int part, int index, int gemId) {
        EquipMessage.ResUpdateGemDatas.Builder builder = EquipMessage.ResUpdateGemDatas.newBuilder();
        builder.setType(type);
        builder.setPart(part);
        builder.setGemIndex(index);
        builder.setGemId(gemId);
        MessageUtils.send_to_player(player, EquipMessage.ResUpdateGemDatas.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private void writeGemInlayLog(Player player, int part, int type, int index, int operateType,
                                  int lastId, int nowId) {
        GemInlayLog gemInlayLog = new GemInlayLog();
        gemInlayLog.setRoleId(player.getId());
        gemInlayLog.setPart(part);
        gemInlayLog.setType(type);
        gemInlayLog.setGemIndex(index);
        gemInlayLog.setOperateType(operateType);
        gemInlayLog.setLastId(lastId);
        gemInlayLog.setNowId(nowId);
        LogService.getInstance().execute(gemInlayLog);
    }

    private void writeGemRefineLog(Player player, int part, int operateType, int lastLevel, int nowLevel,
                                   long lastExp, long nowExp, long addExp, long actionId) {
        GemRefineLog gemRefineLog = new GemRefineLog();
        gemRefineLog.setPlayer(player);
        gemRefineLog.setPart(part);
        gemRefineLog.setOperateType(operateType);
        gemRefineLog.setLastLevel(lastLevel);
        gemRefineLog.setNowLevel(nowLevel);
        gemRefineLog.setLastExp(lastExp);
        gemRefineLog.setNowExp(nowExp);
        gemRefineLog.setAddExp(addExp);
        gemRefineLog.setActionId(actionId);
        LogService.getInstance().execute(gemRefineLog);
    }

    private void getBIGemInfo(Player player, int actType, EquipPart part, int slotPos, int gemId) {
        GemInfo gemInfo = part.getGemInfo();
        if(gemInfo == null||gemInfo.getGemIds() == null||gemInfo.getGemIds().isEmpty()){
            return;
        }
        int slotNum = 0;
        int gemNum = 0;
        for (int i = 0; i < gemInfo.getGemIds().size(); i++) {
            if (gemInfo.getGemIds().get(i) < 0) {//未开启
                continue;
            }
            if (gemInfo.getGemIds().get(i) > 0) {
                gemNum++;
            }
            slotNum++;
        }

        int gemScore = 0;
        RankPlayer rp = RankListManager.getRankPlayerMap().get(player.getId());
        if (rp != null) {
            gemScore = rp.getGemFightPower();
        }
        int level = part.getLevel();
        if(actType == GrowType.gem_refine.getAct_type()){
            level = gemInfo.getLevel();
        }else {
            level = 0;
        }
        gemScore = gemId % 100;
        Manager.biManager.getScript().biEquip(player, actType, 1, part.getType(), 0, "", 0, 0, 0, level, 0, 0, slotNum, slotPos, gemNum, gemScore, gemId,
                null, null, null, null, null, level);
    }
}
