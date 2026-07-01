package common.equip;

import com.data.*;
import com.data.bean.*;
import com.data.container.Cfg_Equip_shenpin_synthesis_Container;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.attribute.BaseIntAttribute;
import com.game.backpack.structs.Equip;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.Manager.ChatManager;
import com.game.chat.structs.ChatChannel;
import com.game.chat.structs.Notify;
import com.game.common.dblog.DbLog;
import com.game.common.dblog.DbLogEnum;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.equip.log.EquipSellLog;
import com.game.equip.log.EquipWashLog;
import com.game.equip.script.IEquipScript;
import com.game.equip.struct.*;
import com.game.log.db.RoleGrowLog;
import com.game.log.grow.GrowType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.PlayerDefine;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import com.game.structs.ServerStr;
import com.game.task.structs.TaskHelp;
import com.game.title.manager.TitleManager;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import com.game.vip.manager.VipManager;
import com.game.vip.structs.VipPower;
import game.core.dblog.LogService;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.WeightCalc;
import game.message.EquipMessage;
import game.message.EquipMessage.*;
import game.message.MapMessage;
import game.message.SpiritMessage;
import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author admin
 */
public class EquipManagerScript implements IScript, IEquipScript {
    private static final Logger LOGGER = LogManager.getLogger(EquipManagerScript.class);


    @Override
    public int getId() {
        return ScriptEnum.EquipManagerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 获取装备
     *
     * @param equipId 装备id
     * @param tagType 0背包和装备栏。1装备栏。2背包
     * @param player  玩家
     * @return 装备
     */
    private Equip gainEquip(long equipId, byte tagType, Player player) {
        switch (tagType) {
            case 0:
                Equip equip = Manager.equipManager.getEquipById(player, equipId);
                return equip == null ? (Equip) Manager.backpackManager.manager().getItemById(player, equipId) : equip;
            case 1:
                return Manager.equipManager.getEquipById(player, equipId);
            case 2:
                return (Equip) Manager.backpackManager.manager().getItemById(player, equipId);
            default:
                LOGGER.info("gainEquip wrong tagType:" + tagType);
        }
        return null;
    }

    @Override
    public void equipSell(Player player, List<Long> id, boolean isAuto) {
        if (id.size() <= 0) {
            sendEquipSellInfoList(player, EquipDefine.Equip_Failed_Param);
            return;
        }
        boolean hasOverdue = false;
        //道具出售的价格
        List<List<Integer>> price = new ArrayList<>();
        List<Equip> equips = new ArrayList<>();
        long actionId = IDConfigUtil.getLogId();
        for (long i : id) {
            Equip equip = gainEquip(i, (byte) 2, player);
            if (equip == null) {
                continue;
            }
            if (equip.getNum() <= 0) {
                continue;
            }
            equips.add(equip);

            Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
            if (bean == null) {
                continue;
            }

            //没有配价格表示装备不可出售
            if ((bean.getPrice() == null || bean.getPrice().isEmpty()) && (bean.getPrice1() == null || bean.getPrice1().isEmpty())) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.CantSellEquip);
                return;
            }
            ReadIntegerArrayEs es = null;
            if (!bean.getPrice().isEmpty()) {
                es = bean.getPrice();
            } else if (!bean.getPrice1().isEmpty()) {
                es = bean.getPrice1();
            }
            if (es == null) {
                return;
            }

            boolean removeSuccess = removeEquip(player, equip, ItemChangeReason.EquipSellDec, actionId);
            if (!removeSuccess) {
                LOGGER.info(String.format("玩家{%s}售卖装备失败!移除装备{%s}错误!!!", TaskHelp.getPlayerInfo(player), equip.getItemModelId()));
                continue;
            }

            //装备过期则直接销毁
            if (equip.haveLost()) {
                hasOverdue = true;
                continue;
            }

            for (ReadArray<Integer> b : es.getValuees()) {
                if (b.get(0) <= 0 || b.get(1) <= 0) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.CantSellEquip);
                    return;
                }
                List<Integer> ss = new ArrayList<>();
                ss.add(b.get(0));
                ss.add(b.get(1));
                price.add(ss);
            }

            if (isAuto) {
                for (List<Integer> list : price) {
                    MessageUtils.notify_chat_player(player, ChatChannel.CHATCHANNEL_SYSTEM, MessageString.AutoResolveEquip, Manager.backpackManager.manager().getName(equip.getItemModelId()), Manager.backpackManager.manager().getName(list.get(0)), String.valueOf(list.get(1)));
                }
            }
        }
        if (price.size() <= 0) {
            if (hasOverdue) {
                sendEquipSellInfoList(player, 0);
                writeEquipSellLog(player, id, equips, actionId);
                return;
            }
            sendEquipSellInfoList(player, EquipDefine.Equip_Failed_Config);
            return;
        }

        //出售获取
        List<Item> items = Item.createItems(Utils.toReadIntegerArrayEsByList(price));
        List<Item> sendMail = new ArrayList<>();
        for (Item item : items) {
            if (Manager.backpackManager.manager().onHasAddSpace(player, item)) {
                Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.EquipSellDec, actionId);
            } else {
                sendMail.add(item);
            }
        }
        if (sendMail.size() > 0) {
            Manager.taskManager.deal().sendRewardByMail(player, sendMail, ItemChangeReason.EquipSellDec, actionId);
        }
        if (!isAuto) {
            sendEquipSellInfoList(player, 0);
        }

        writeEquipSellLog(player, id, equips, actionId);
    }

    @Override
    public void OnReqEquipSell(Player player, EquipMessage.ReqEquipSell messInfo) {
        List<Long> id = messInfo.getIdList();
        this.equipSell(player, id, false);
    }

    //发送装备出售返回
    private void sendEquipSellInfoList(Player player, int result) {
        ResEquipSell.Builder msg = ResEquipSell.newBuilder();
        msg.setResult(result);
        MessageUtils.send_to_player(player, ResEquipSell.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public boolean removeEquip(Player player, Equip equip, int reason, long actionId) {
        if (equip == null) {
            LOGGER.error("移除装备返还技能书时：装备已经不存在了：");
            return false;
        }
        return Manager.backpackManager.manager().onRemoveItem(player, equip, 1, reason, actionId);
    }

    @Override
    public boolean removeEquipNotNotic(Player player, Equip equip, int reason, long actionId, Set<Long> deleteList, Set<Long> changeList) {
        if (equip == null) {
            LOGGER.error("移除装备返还技能书时：装备已经不存在了：");
            return false;
        }
        return Manager.backpackManager.manager().onRemoveItemNotNoticeClinet(player, equip, 1, reason, actionId, deleteList, changeList);
    }

    //GM工具发送指定的装备
    @Override
    public boolean sendAppointEquip(Player player, int id, boolean bind) {
        try {
            Item item = Item.createItem(id, 1, bind);
            if (item instanceof Equip) {
                Equip equip = (Equip) item;
                if (!Manager.backpackManager.manager().onHasAddSpace(player, equip)) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
                    return false;
                }
                return Manager.backpackManager.manager().addItem(player, equip, ItemChangeReason.GM, IDConfigUtil.getLogId());
            }
            return false;
        } catch (Exception e) {
            LOGGER.error(e, e);
            return false;
        }
    }

    @Override
    public int isEquipQuality(Player player, int layer, int quality, int diamond) {
        Cfg_Equip_Bean model;
        int num = 0;

        for (EquipPart equipPart : player.getEquipParts()) {
            if (equipPart == null || null == equipPart.getEquip()) {
                continue;
            }
            model = CfgManager.getCfg_Equip_Container().getValueByKey(equipPart.getEquip().getItemModelId());
            if (model == null) {
                continue;
            }
            //首先阶数>=目标阶数，若品质相等，大于等于星数满足，如品质高，则不看星数直接满足
            if (model.getGrade() >= layer && ((model.getQuality() == quality && model.getDiamond_Number() >= diamond) || model.getQuality() > quality)) {
                num++;
            }
        }
        return num;
    }

    public  int getEquipNum(Player player ,int equipId){
        int num = 0;
        for (EquipPart equipPart : player.getEquipParts()) {
            if (equipPart == null || null == equipPart.getEquip()) {
                continue;
            }
            if (equipPart.getEquip().getItemModelId() == equipId){
                num++;
            }
        }
        return num;
    }

    public int getGrowup_wear_equip(Player player,int part,int grade,int quality){
        Cfg_Equip_Bean model;
        for (EquipPart equipPart : player.getEquipParts()) {
            if (equipPart == null || null == equipPart.getEquip()) {
                continue;
            }
            model = CfgManager.getCfg_Equip_Container().getValueByKey(equipPart.getEquip().getItemModelId());
            if (model == null) {
                continue;
            }

            if (model.getPart() != part){
                continue;
            }
            //首先阶数>=目标阶数，若品质相等，大于等于星数满足，如品质高，则不看星数直接满足
            if (model.getGrade() >= grade && model.getQuality() >= quality) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 合成X件X阶及以上的X色X星装备,条件id_阶数id_品质id_星数_件数:55
     * @param player
     * @param grade
     * @param quility
     * @return
     */
    public int getComposeEquip(Player player,int grade, int quility,int start){

        Cfg_Equip_Bean model;
        int num =0;
        for (EquipPart equipPart : player.getEquipParts()) {
            if (equipPart == null || null == equipPart.getEquip()) {
                continue;
            }
            model = CfgManager.getCfg_Equip_Container().getValueByKey(equipPart.getEquip().getItemModelId());
            if (model == null) {
                continue;
            }

            //首先阶数>=目标阶数，若品质相等，大于等于星数满足，如品质高，则不看星数直接满足
            if (model.getGrade() >= grade && model.getQuality() >= quility && model.getDiamond_Number() >= start) {
                num++;
            }
        }
        return num;
    }

    /**
     * 计算手镯经验加成
     * @param player
     * @return
     */
    public int calEquipRate(Player player,int partType){
        EquipPart part =  player.getEquipParts().get( partType);
        if (part.getEquip() == null){
            return 0;
        }

        Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(part.getEquip().getItemModelId());
        if (bean == null){
            LOGGER.error("Cfg_Equip_Bean is null  :{}",part.getEquip().getItemModelId());
            return 0;
        }

        if (bean.getPart() != partType){
            LOGGER.error("部位不正确 part  :{}",bean.getPart());
            return 0;
        }
        int addAll = 0;
        ReadArray[] readArrays = new ReadArray[]{};
        readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays, bean.getAttribute1().getValuees());
        readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays, bean.getAttribute2().getValuees());
        readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays, bean.getAttribute3().getValuees());
        for (ReadArray<Integer> readArray : readArrays){
            if (readArray.get(0) == 43) { //策划喊43类型写死
                addAll +=readArray.get(1);
            }
        }
        return addAll/100;
    }



    @Override
    public void OnReqAutoResolveSet(Player player, EquipMessage.ReqAutoResolveSet mess) {
        List<Integer> ids = mess.getQualitysList();
        player.getResolveSettings().clear();
        player.getResolveSettings().addAll(ids);
        sendResEquipResolveSet(player);
    }

    @Override
    public void OnReqEquipResolveSet(Player player, EquipMessage.ReqEquipResolveSet mess) {
        sendResEquipResolveSet(player);
    }

    @Override
    public void OnReqEquipSynthetic(Player player, Cfg_Equip_synthesis_Bean equipSyn, List<Long> eqs, boolean isHaveItem, int type) {
        Manager.countManager.addCount(player, BaseCountType.SyntheticEquipStrong, 0, Count.RefreshType.CountType_Forever, 1);
        Manager.controlManager.operate(player, FunctionVariable.ComposeEquiponce, 1);
        if (player.getLevel() < equipSyn.getSynthesis_level()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GetPetFaildLevel);
            return;
        }
        Cfg_Equip_Bean srcBean = CfgManager.getCfg_Equip_Container().getValueByKey(equipSyn.getId());
        Cfg_Equip_Bean targetBean = CfgManager.getCfg_Equip_Container().getValueByKey(equipSyn.getEquip_ID());
        if (srcBean == null || targetBean == null) {
            return;
        }
        //是否存在穿戴装备或灵体装备
        if (type == 0) {
            Equip srcEquip = Manager.equipManager.getEquipByType(player, srcBean.getPart());
            if (srcEquip == null) {
                return;
            }
        } else if (type == 1) {
            SpiritInfo info = player.getSpiritData().getSpiritInfoMap().get(srcBean.getGrade());
            if (info == null || !info.getEquipList().contains(equipSyn.getId())) {
                return;
            }
        } else {
            LOGGER.error("错误的类型");
            return;
        }

        //join_num_probability为空代表首饰，首饰只需要指定道具材料
        boolean isJewelry = equipSyn.getJoin_num_probability().size() == 0;
        //代表合成首饰的join_id1是否匹配通过
        boolean joinIdOneFlag = false;
        //代表合成首饰的join_id2是否匹配通过
        boolean joinIdTwoFlag = false;
        if (isJewelry && equipSyn.getJoin_EquipID1().size() == 0 && equipSyn.getJoin_EquipID2().size() == 0) {
            joinIdOneFlag = joinIdTwoFlag = true;
        }

        List<Equip> equips = new ArrayList<>();
        long totalProbability = 0L;
        int gradeIndex, qualityIndex, diamondIndex;
        for (Long eid : eqs) {
            Equip equip = gainEquip(eid, (byte) 2, player);
            if (equip == null) {
                LOGGER.error("背包不存在这个装备：name=" + player.getName() + " id=" + eid);
                return;
            }
            Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
            if (!bean.getGender().contains(equipSyn.getProfessional()) || !targetBean.getGender().contains(equipSyn.getProfessional())) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.UseItemCareerLimit);
                return;
            }

            //首饰合成
            if (isJewelry) {
                //首饰合成必须至少包含join_EquipID1和join_EquipID2中的一个装备
                if (!joinIdOneFlag) {
                    joinIdOneFlag = checkInJoinIdArray(equipSyn.getJoin_EquipID1(), equip.getItemModelId());
                }
                if (!joinIdTwoFlag) {
                    joinIdTwoFlag = checkInJoinIdArray(equipSyn.getJoin_EquipID2(), equip.getItemModelId());
                }
                equips.add(equip);
                continue;
            }

            //非首饰合成
            if (srcBean.getGrade() < bean.getGrade()) {
                gradeIndex = 1;
            } else if (srcBean.getGrade() == bean.getGrade()) {
                gradeIndex = 0;
            } else {
                gradeIndex = srcBean.getGrade() - bean.getGrade() == 1 ? 2 : 3;
            }
            if (equipSyn.getJoin_num_probability().size() <= gradeIndex) {
                LOGGER.error("Cfg_Equip_synthesis_Bean装备合成配置表配置错误：" + equipSyn.getId() + ", grade" + bean.getGrade());
                return;
            }

            qualityIndex = getArrayIndex(equipSyn.getQuality(), bean.getQuality());
            diamondIndex = getArrayIndex(equipSyn.getDiamond(), bean.getDiamond_Number());
            if (qualityIndex == -1 || diamondIndex == -1) {
                LOGGER.error(String.format("Cfg_Equip_synthesis_Bean装备合成配置表概率配置错误%s： 品质：%s，星数：%s",
                        equipSyn.getId(), bean.getQuality(), bean.getDiamond_Number()));
                return;
            }
            totalProbability += 1L * equipSyn.getJoin_num_probability().get(gradeIndex)
                    * equipSyn.getQuality_Number().get(qualityIndex)
                    * equipSyn.getDiamond_Number().get(diamondIndex);
            equips.add(equip);
        }

        if (isJewelry) {
            if (!joinIdOneFlag || !joinIdTwoFlag) {
                LOGGER.info("七彩装备合成：name=" + player.getName() + " 材料数量错误：");
                return;
            }
        }

        //计算扣除的道具和元宝
        int itemModeId = 0;
        int curItemNum = 0;
        int curNeedCoin = 0;
        if (equipSyn.getJoin_item().size() >= 2) {
            int itemNumber = equipSyn.getJoin_item().get(1);
            itemModeId = equipSyn.getJoin_item().get(0);
            curItemNum = Manager.backpackManager.manager().getItemNum(player, itemModeId);

            if (curItemNum < itemNumber) {
                if (!isHaveItem) {
                    return;
                }
                if (equipSyn.getItem_Price().size() < 2) {
                    LOGGER.error("装备合成：name=" + player.getName() + "Join_item材料不足");
                    return;
                }
                curNeedCoin = (itemNumber - curItemNum) * equipSyn.getItem_Price().get(1);
                if (!Manager.currencyManager.manager().canDecItemCoin(player, curNeedCoin, equipSyn.getItem_Price().get(0))) {
                    LOGGER.error("装备合成：name=" + player.getName() + "Join_item货币不足");
                    return;
                }
            } else {
                curItemNum = itemNumber;
            }
        }

        //移除道具
        long actionId = IDConfigUtil.getLogId();
        StringBuilder sb = new StringBuilder();
        for (Equip e : equips) {
            sb.append(e.toString()).append(";");
            boolean removeSuccess = removeEquip(player, e, ItemChangeReason.EquipSyntheticDec, actionId);
            if (!removeSuccess) {
                LOGGER.info(String.format("玩家{%s}合成装备错误!移除装备{%s}错误!!!", TaskHelp.getPlayerInfo(player), e.getItemModelId()));
            }
        }

        if (curItemNum != 0) {
            Manager.backpackManager.manager().onRemoveItem(player, itemModeId, curItemNum, ItemChangeReason.EquipSyntheticDec, actionId);
        }

        if (curNeedCoin != 0) {
            Manager.currencyManager.manager().onDecItemCoin(player, curNeedCoin, ItemChangeReason.EquipSyntheticDec, actionId, equipSyn.getItem_Price().get(0));
        }

        //概率计算成功与否
        boolean success = true;
        if (!isJewelry) {
            success = RandomUtils.defaultIsGenerate((int) (totalProbability / 10000_0000));
        }

        int state = 0;
        if (success) {
            Equip srcEquip = null;
            boolean isBind = true;
            if(type == 0){
                srcEquip = Manager.equipManager.getEquipByType(player, srcBean.getPart());
                isBind = srcEquip.isBind();
            }
            Equip tarEquip = (Equip) Item.createItem(equipSyn.getEquip_ID(), 1, isBind);
            Manager.backpackManager.manager().addItem(player, tarEquip, ItemChangeReason.EquipSyntheticDec, actionId);
            if (type == 0) {
                boolean useFlag = tarEquip.use(player, 1, 0, actionId);
                if (useFlag) {
                    boolean remove = removeEquip(player, srcEquip, ItemChangeReason.EquipSyntheticDec, actionId);
                    if (!remove) {
                        LOGGER.error("扣除装备失败！");
                    }
                }
            }
            //灵体合成 = 装备合成+自动收集
            if (type == 1) {
                collectEquip(player, srcBean.getGrade(), equipSyn.getEquip_ID(), 1, false);
            }
            //@todo 修改公告
            if ( equipSyn.getNotice() != 0 || equipSyn.getChatchannel() != null) {
                MessageUtils.notify_allOnlinePlayer(equipSyn.getNotice(),equipSyn.getChatchannel(), MessageString.EquipSynthetic,
                        player.getId()+"",   player.getName(), Manager.backpackManager.manager().getChatInfo(tarEquip),
                        Utils.makeUrlStr(MessageString.EquipSynthetic));
            }
            Manager.biManager.getScript().biEquip(player, GrowType.equip_synthesis.getAct_type(), GrowType.equip_synthesis.getType(), targetBean.getPart(), tarEquip.getItemModelId(), targetBean.getName(), targetBean.getDiamond_Number(), targetBean.getGrade(), targetBean.getQuality(), 0, targetBean.getBind(), tarEquip.getSuitId(), 0, 0, 0, 0, 0);

            long key = targetBean.getGrade() * 10000 + targetBean.getQuality() * 100 + targetBean.getDiamond_Number();

            Manager.countManager.addCount(player, BaseCountType.SyntheticEquipStrong, key, Count.RefreshType.CountType_Forever, 1);
            Manager.controlManager.operate(player, FunctionVariable.ComposeEquip, 1);

            Manager.controlManager.operate(player, FunctionVariable.Equip_Star_Level_Up, 1);


        } else {
            state = 1;
            if (equipSyn.getFailure_type().size() != 0) {
                List<Item> items = Item.createItems(equipSyn.getFailure_type().get(0), equipSyn.getFailure_type().get(1), true);
                Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.EquipSyntheticDec, actionId);
            }
        }

        sendResEquipSyn(player, state, type);
        DbLog.save(DbLogEnum.EQUIP_SYNTHETIC_LOG, player, equipSyn.getEquip_ID() + "", sb.toString(), itemModeId + "", curItemNum + "", state + "");
    }

    /**
     * 获取指定值在数组中的索引
     */
    private int getArrayIndex(ReadIntegerArray array, int target) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) == target) {
                return i;
            }
        }
        return -1;
    }

    //装备拆解
    @Override
    public void OnReqEquipSynSplit(Player player, ReqEquipSynSplit messinfo) {
        Equip equip = gainEquip(messinfo.getEId(), (byte) 2, player);
        if(equip == null){
            sendEquipSplit(player, 1);
            return;
        }

        Cfg_Equip_resolve_Bean bean = CfgManager.getCfg_Equip_resolve_Container().getValueByKey(equip.getItemModelId());
        if(bean == null){
            sendEquipSplit(player, 2);
            return;
        }

        //消耗装备
        long actionId = IDConfigUtil.getLogId();
        if (!Manager.equipManager.deal().removeEquip(player, equip, ItemChangeReason.EquipSplitDec, actionId)) {
            LOGGER.error(String.format("玩家{%s}从背包移除道具{%s}错误!!!", TaskHelp.getPlayerInfo(player), equip.getItemModelId()));
            sendEquipSplit(player, 3);
            return;
        }

        //增加拆解材料
        Integer[] equipVal = bean.getEquip().getValue();
        List<Item> equips = Item.createItems(equipVal[0], equipVal[1], equipVal[2]==1);
        List<Item> items = Item.createItems(bean.getItem());
        equips.addAll(items);

        if (!Manager.backpackManager.manager().addItems(player, equips, ItemChangeReason.EquipSplitGet, actionId)) {
            LOGGER.error("玩家" + player.getId() + "拆解装备的奖励发放错误");
            sendEquipSplit(player, 4);
            return;
        }

        sendEquipSplit(player, 0);
    }

    private void sendEquipSplit(Player player, int state) {
        ResEquipSynSplit.Builder msg = ResEquipSynSplit.newBuilder();
        msg.setState(state);//0成功
        MessageUtils.send_to_player(player, ResEquipSynSplit.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //装备套装
    @Override
    public void OnEquipSuit(Player player, int suitId, long equipId) {
        Cfg_Equip_suit_Bean suitbean = CfgManager.getCfg_Equip_suit_Container().getValueByKey(suitId);
        if (suitbean == null) {
            return;
        }
        Equip equip = gainEquip(equipId, (byte) 0, player);
        if (equip == null) {
            return;
        }
        Cfg_Equip_Bean eb = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());

        if (suitbean.getNeed_diamonds() > eb.getDiamond_Number()) {
            return;
        }
        if (suitbean.getNeed_quality() > eb.getQuality()) {
            return;
        }

        if (suitbean.getNeed_gender().get(0) != PlayerDefine.CAREER_All) {
            if (!ArrayUtils.contains(suitbean.getNeed_gender().getValue(), (int) player.getCareer())) {
                return;
            }
        }

        if (!ArrayUtils.contains(suitbean.getNeed_degree().getValue(), eb.getGrade())) {
            return;
        }
        if (!ArrayUtils.contains(suitbean.getNeed_parts().getValue(), eb.getPart())) {
            return;
        }
        List<ReadArray<Integer>> decItems = new ArrayList<>();
        for (ReadArray<Integer> item : suitbean.getNeed_items().getValuees()) {
            if (eb.getPart() == item.get(0)) {
                if (!Manager.backpackManager.manager().canDeleteItemNum(player, item.get(1), item.get(2))) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConsumeNotEnough, Manager.backpackManager.manager().getName(item.get(1)));
                    Manager.backpackManager.manager().sendItemNotEnough(player, item.get(1));
                    return;
                }
                decItems.add(item);
            }
        }
        //扣除材料
        for (ReadArray<Integer> item : decItems) {
            Manager.backpackManager.manager().onRemoveItem(player, item.get(1), item.get(2), ItemChangeReason.EquipSuitDec, equipId);
        }
        //处理数据
        equip.setSuitId(suitId);
        ResEquipSuit.Builder msg = ResEquipSuit.newBuilder();
        msg.setState(0);
        MessageUtils.send_to_player(player, ResEquipSuit.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        Manager.equipManager.updateOneEquipPartInfo(player, eb.getPart());
        MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, MessageString.Equip_Suit_Success,
                player.getName(),
                Manager.backpackManager.manager().getName(equip.getItemModelId()),
                ServerStr.getChatTableName(suitbean.getName()),
                Utils.makeUrlStr(MessageString.Equip_Suit_Success));
        Manager.controlManager.operate(player, FunctionVariable.EquipSuitNum, 0);
        Manager.controlManager.operate(player, FunctionVariable.EquipSuitLevel, 0);
        Manager.shopManager.limitShop().refresh(player);

        int suitNum = 0;
        for (EquipPart equipPart : player.getEquipParts()) {
            if (null == equipPart || null == equipPart.getEquip()) {
                continue;
            }

            if (equipPart.getEquip().getSuitId() == suitId) {
                suitNum++;
            }
        }

        if (suitNum % 2 == 0) {
            MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, MessageString.Equip_Suit_Num,
                    player.getName(),
                    String.valueOf(suitNum),
                    ServerStr.getChatTableName(suitbean.getName()),
                    Utils.makeUrlStr(MessageString.Equip_Suit_Num));
        }
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.EQUIP);

        Manager.biManager.getScript().biEquip(player, GrowType.equip_suit_active.getAct_type(), GrowType.equip_suit_active.getType(), eb.getPart(), equip.getItemModelId(), eb.getName(), eb.getDiamond_Number(), eb.getGrade(), eb.getQuality(), 0, eb.getBind(), equip.getSuitId(), 0, 0, 0, 0, 0);
    }

    @Override
    public void equipSuitInheritance(Player player, Equip source, Equip target) {
        if (source.getSuitId() <= 0) {
            return;
        }
        if (target.getSuitId() > 0) {
            return;
        }
        Cfg_Equip_suit_Bean suitbean = CfgManager.getCfg_Equip_suit_Container().getValueByKey(source.getSuitId());

        Cfg_Equip_Bean eb = CfgManager.getCfg_Equip_Container().getValueByKey(target.getItemModelId());

        List<Item> items = new ArrayList<>();
        for (ReadArray<Integer> item : suitbean.getNeed_items().getValuees()) {
            if (eb.getPart() == item.get(0)) {
                items.addAll(Item.createItems(item.get(1), item.get(2), true));
            }
        }

        Cfg_Equip_suit_Bean parentSuitbean = null;

        if (suitbean.getParent_ID() > 0) {
            parentSuitbean = CfgManager.getCfg_Equip_suit_Container().getValueByKey(suitbean.getParent_ID());
            if (parentSuitbean != null) {
                for (ReadArray<Integer> item : parentSuitbean.getNeed_items().getValuees()) {
                    if (eb.getPart() == item.get(0)) {
                        items.addAll(Item.createItems(item.get(1), item.get(2), true));
                    }
                }
            }
        }

        if (parentSuitbean != null && parentSuitbean.getParent_ID() > 0) {
            parentSuitbean = CfgManager.getCfg_Equip_suit_Container().getValueByKey(parentSuitbean.getParent_ID());
            if (parentSuitbean != null) {
                for (ReadArray<Integer> item : parentSuitbean.getNeed_items().getValuees()) {
                    if (eb.getPart() == item.get(0)) {
                        items.addAll(Item.createItems(item.get(1), item.get(2), true));
                    }
                }
            }
        }

        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.UnWearEquipGet, source.getId());
        } else {
            Manager.taskManager.deal().sendRewardByMail(player, items, ItemChangeReason.UnWearEquipGet, source.getId());
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.BAGISSPACETOMAIL);//"背包格子不足"
        }
        source.setSuitId(0);
    }

    /**
     * 处理装备强化
     */
    @Override
    public void onReqEquipStrengthUpLevel(int type, Player player) {
        EquipPart part = player.getEquipParts().get(type);
        if (null == part) {
            return;
        }

        Equip equip = part.getEquip();
        if (null == equip) {
            return;
        }

        Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if (part.getLevel() > bean.getLevel_max()) {
            return;
        }

        int strengthenId = getEquipPartStrengthenId(part, type);
        Cfg_Equip_inten_main_Bean strengthenBean = CfgManager.getCfg_Equip_inten_main_Container().getValueByKey(strengthenId);
        if (null == strengthenBean) {
            return;
        }

        if (!Manager.backpackManager.manager().canDeleteItemNum(player, strengthenBean.getConsume(), 1)) {
            return;
        }

        Manager.backpackManager.manager().removeItemOrCurrencies(player, strengthenBean.getConsume(), IDConfigUtil.getLogId(), ItemChangeReason.EquipPartStrengthenDec);

        int exp = RandomUtils.random(strengthenBean.getProficiency().get(0), strengthenBean.getProficiency().get(1));
        if (part.getCurrentExp() + exp >= strengthenBean.getProficiency_Max()) {
            part.setCurrentExp(strengthenBean.getProficiency_Max() - part.getCurrentExp() - exp);
            part.addLevel(1);
            Manager.rankListManager.deal().changeEquipStrengthen(player);
            Manager.controlManager.operate(player, FunctionVariable.EquipStrengthenLevelMax, 0);
            Manager.shopManager.limitShop().refresh(player);
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.EQUIP);
        } else {
            part.setCurrentExp(part.getCurrentExp() + exp);
        }

        ResEquipStrengthUpLevel.Builder builder = ResEquipStrengthUpLevel.newBuilder();
        EquipStrengthInfo.Builder infoBuilder = EquipStrengthInfo.newBuilder();
        infoBuilder.setType(type);
        infoBuilder.setLevel(part.getLevel());
        infoBuilder.setExp(part.getCurrentExp());
        builder.setInfo(infoBuilder);
        MessageUtils.send_to_player(player, ResEquipStrengthUpLevel.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        Manager.biManager.getScript().biEquip(player, GrowType.equip_intensify.getAct_type(), GrowType.equip_intensify.getType(), bean.getPart(), equip.getItemModelId(), bean.getName(), bean.getDiamond_Number(), bean.getGrade(), bean.getQuality(), part.getLevel(), bean.getBind(), equip.getSuitId(), 0, 0, 0, 0, 0);
    }

    @Override
    public int calculateStrengthenPower(Player player) {
        BaseIntAttribute att = new BaseIntAttribute(AttributeType.ATTR_MAX);
        att.clean();
        for (EquipPart equipPart : player.getEquipParts()) {
            if (equipPart != null && null != equipPart.getEquip()) {
                Equip equip = equipPart.getEquip();
                if (equip.haveLost()) {
                    continue;
                }
                if (!equip.canEquip(player)) {
                    continue;
                }
                //fixBug 穿戴了装备的才计算属性
                if (equip.getGridId() < 0 || equip.getGridId() > 7) {
                    continue;
                }
                /**
                 * 计算部位强化带来的属性
                 * */
                compEquipPartAttr(player, att, equipPart);
            }
        }
        return Manager.playerAttAttributeManager.deal().calcFightPower(att);
    }

    /**
     * 计算部位强化的属性
     */
    @Override
    public void compEquipPartAttr(Player player, BaseIntAttribute attribute, EquipPart part) {
        Cfg_Equip_inten_main_Bean bean = CfgManager.getCfg_Equip_inten_main_Container().getValueByKey(part.getStrengthenExcelId());
        if (null == bean) {
            LOGGER.error("错误的装备部位 excelId:" + part.getType() + " " + part.getLevel());
            return;
        }
        for (ReadArray<Integer> aii : bean.getValue().getValuees()) {
            attribute.addAttribute(aii.get(0), aii.get(1));
        }
    }


    @Override
    public void onReqEquipWash(Player player, int part, boolean type, List<Integer> indexs) {
        Equip equip = Manager.equipManager.getEquipByType(player, part);
        if (equip == null) {
            return;
        }

        EquipPart equipPart = player.getEquipParts().get(part);
        if (equipPart == null) {
            return;
        }
        Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if (bean == null) {
            return;
        }

        //检查洗练部位等级限制
        Cfg_Wash_best_Bean washBean = CfgManager.getCfg_Wash_best_Container().getValueByKey(part + 1000);
        if (washBean == null) {
            return;
        }

        if (player.getLevel() < washBean.getLevel_limit()) {
            return;
        }

        int unLocknum = 0;
//        int lastStateLv = 0;

//        int stateLv = player.getStateVip().getLv();
        int star = bean.getDiamond_Number();

        for (int i = 0; i < Global.Equip_washing_conditions_Num_4.size(); i++) {
            if (star >= Global.Equip_washing_conditions_Num_4.get(i).get(1)) {
                unLocknum = Global.Equip_washing_conditions_Num_4.get(i).get(0);
            } else {
                break;
            }
        }
//        for (ReadArray<Integer> l : Global.Equip_washing_conditions_Num_4.getValuees()) {
//            if (stateLv > l.get(1)) {
//                lastStateLv = l.get(1);
//                unLocknum = l.get(0);
//                continue;
//            }
//
//            if (stateLv <= l.get(1) && stateLv > lastStateLv) {
//                unLocknum = l.get(0);
//                break;
//            }
//        }

        List<Integer> unLockIndexs = new ArrayList<>();

        if (player.getVipLv() >= Global.Equip_washing_conditions_Num_5.get(1)) {
//            unLocknum = unLocknum + 1;
            unLockIndexs.add(5);
        }

        for (int i = 1; i <= unLocknum; i++) {
            unLockIndexs.add(i);
        }

        int length = unLockIndexs.size();
        if (length == 0) {
//            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.EquipWashStarLess, 0 + "");
            return;
        }

        for (Integer index : indexs) {
            if (unLockIndexs.contains(index)) {
                unLockIndexs.remove(index);
            }
//            if (index > length) {
//                return;
//            }
        }


        if (indexs.size() == 0 && type == false) {
            return;
        }

        if (type) {
            int itemId = 0;
            int itemNum = 0;
            for (ReadArray<Integer> l : Global.Clear_time.getValuees()) {
                if (l.get(1) == indexs.size()) {
                    itemId = l.get(0);
                    itemNum = l.get(2);
                    break;
                }
            }
            if (!Manager.backpackManager.manager().canDeleteItemNum(player, itemId, itemNum)) {
                return;
            }
            if (!Manager.backpackManager.manager().onRemoveItem(player, itemId, itemNum, ItemChangeReason.EquipWashDec, part)) {
                return;
            }
        } else {
            int itemId = 0;
            int itemNum = 0;
            for (ReadArray<Integer> l : Global.Clear_time.getValuees()) {
                if (l.get(1) == 0) {
                    itemId = l.get(0);
                    itemNum = l.get(2);
                    break;
                }
            }
            if (!Manager.backpackManager.manager().canDeleteItemNum(player, itemId, itemNum)) {
                return;
            }

            int num = 0;
           // int currencyId = 0;
            for (ReadArray<Integer> l : Global.Clear_cost.getValuees()) {
                if (l.get(0) == indexs.size()) {
                   // currencyId = l.get(1);
                    num = (int) (l.get(2) * Manager.vipManager.power().getVipDiscount(player, VipPower.POWER_29) * 1F / 10000);
                    break;
                }
            }
//            if (num > 0 && !Manager.currencyManager.manager().decGold(player, num, ItemChangeReason.EquipWash, IDConfigUtil.getLogId())) {
//                return;
//            }



            if (num > 0 && !Manager.currencyManager.manager().decBindGoldOrGold(player, num, ItemChangeReason.EquipWashDec, IDConfigUtil.getLogId())) {
                return;
            }

            if (!Manager.backpackManager.manager().onRemoveItem(player, itemId, itemNum, ItemChangeReason.EquipWashDec, part)) {
                return;
            }

        }
        equipPart.setWashNum(equipPart.getWashNum() + 1);
        int oldScore = equipPart.getWashScore();
        equipWash(player, part, unLockIndexs, equipPart);
        Manager.controlManager.operate(player, FunctionVariable.EquipWashingNum, 1);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.EQUIP);
        writeEquipWashLog(player, equipPart, indexs, equip.getId());
        int lockNum = indexs.size();
        int lockType = lockNum == 0 ? 0 : type ? 1 : 2;
        Manager.biManager.getScript().biEquip(player, GrowType.equip_wash.getAct_type(), GrowType.equip_wash.getType(), bean.getPart(), equip.getItemModelId(), bean.getName(), bean.getDiamond_Number(), bean.getGrade(), bean.getQuality(), equipPart.getLevel(), bean.getBind(), equip.getSuitId()
                , 0, 0, 0, 0, 0,
                lockNum, lockType, oldScore, equipPart.getWashScore() - oldScore, equipPart.getWashScore(), null);
    }

    /**
     * 装备洗练次数
     *
     * @param player
     */
    @Override
    public int equipWashNum(Player player) {
        int washNum = 0;
        List<EquipPart> equipParts = player.getEquipParts();
        for (EquipPart part : equipParts) {
            washNum += part.getWashNum();
        }
        return washNum;
    }

    @Override
    public int gainSuitNum(Player player, int layer) {
        Cfg_Equip_Bean bean;
        int suitNum = 0;
        for (EquipPart part : player.getEquipParts()) {
            if (part.getEquip() == null || part.getEquip().getSuitId() == 0) {
                continue;
            }
            bean = CfgManager.getCfg_Equip_Container().getValueByKey(part.getEquip().getItemModelId());
            if (bean != null && bean.getGrade() >= layer) {
                suitNum++;
            }
        }
        return suitNum;
    }

    @Override
    public int gainSuitNum(Player player, int layer, int level) {
        Cfg_Equip_Bean bean;
        int suitNum = 0;
        for (EquipPart part : player.getEquipParts()) {
            if (part.getEquip() == null || part.getEquip().getSuitId() == 0) {
                continue;
            }
            bean = CfgManager.getCfg_Equip_Container().getValueByKey(part.getEquip().getItemModelId());
            Cfg_Equip_suit_Bean b1 = CfgManager.getCfg_Equip_suit_Container().getValueByKey(part.getEquip().getSuitId());
            if (bean != null && bean.getGrade() >= layer && b1.getLevel() >= level) {
                suitNum++;
            }
        }
        return suitNum;
    }


    @Override
    public void sendSpiritInfo(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.LingTi) &&
                !Manager.controlManager.deal().isOpenFunction(player, FunctionStart.LingtiFanTai)) {
            return;
        }
        ConcurrentHashMap<Integer, SpiritInfo> spiritInfo = player.getSpiritData().getSpiritInfoMap();
        SpiritMessage.ResSpiritInfo.Builder builder = SpiritMessage.ResSpiritInfo.newBuilder();
        for (Cfg_Equip_Collection_Bean bean : CfgManager.getCfg_Equip_Collection_Container().getValuees()) {
            if (bean.getId() / 100 != player.getCareer()) {
                continue;
            }
            int spiritId = bean.getId() % 100;
            if (!spiritInfo.containsKey(spiritId) && player.getStateVip().getLv() >= bean.getDescribe()) {
                spiritInfo.put(spiritId, new SpiritInfo(spiritId));
            }
            if (!spiritInfo.containsKey(spiritId)) {
                continue;
            }
            SpiritMessage.spiritInfo.Builder info = SpiritMessage.spiritInfo.newBuilder();
            info.setId(spiritId);
            info.addAllEquipList(spiritInfo.get(spiritId).getEquipList());
            info.setIsActive(spiritInfo.get(spiritId).isActive());
            builder.addList(info);
        }
        builder.setCfgId(player.getSpiritData().getCfgId());
        builder.setStarNum(player.getSpiritData().getOpenStar());
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Spirit, PlayerAttributeType.EQUIP);
        MessageUtils.send_to_player(player, SpiritMessage.ResSpiritInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void upLevel(Player player, int cfgId) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.LingtiFanTai)) {
            return;
        }
        Cfg_Equip_Collection_start_Bean bean = CfgManager.getCfg_Equip_Collection_start_Container().getValueByKey(cfgId);
        if (bean == null) {
            LOGGER.error("Cfg_Equip_Collection_start_Bean配置表不存在：" + cfgId);
            return;
        }
        int nextCfgId = getNextCfgId(player);
        if (nextCfgId == 0 || cfgId != nextCfgId) {
            LOGGER.error("蕴养id错误：" + player.getSpiritData().getCfgId() + "|" + cfgId);
            return;
        }
        if (player.getLevel() < bean.getLevel()) {
            return;
        }
        if (bean.getNeeditem().size() >= 2) {
            if (!Manager.backpackManager.manager().onRemoveItem(player, bean.getNeeditem().get(0), bean.getNeeditem().get(1), ItemChangeReason.SpiritStartUseDec, 0)) {
                return;
            }
        }
        int oldId = player.getSpiritData().getCfgId();
        player.getSpiritData().setCfgId(cfgId);
        if ((cfgId % 100 == 99) && (cfgId / 100 > player.getSpiritData().getSpiritId())) {
            player.getSpiritData().setSpiritId(cfgId / 100);
            broadCaseSpirit(player);
        }
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.BASE, PlayerAttributeType.EQUIP);

        Manager.controlManager.operate(player, FunctionVariable.LingtiSealRelief, 1);

        SpiritMessage.ResUpLevel.Builder builder = SpiritMessage.ResUpLevel.newBuilder();
        builder.setCfgId(cfgId);
        MessageUtils.send_to_player(player, SpiritMessage.ResUpLevel.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        RoleGrowLog.create(player, GrowType.book_active, 0, cfgId, oldId,cfgId, null);
    }

    private int getNextCfgId(Player player) {
        int cfgId = player.getSpiritData().getCfgId();
        //x00表示x阶段解锁
        if (cfgId == 0) {
            return 101;
        }
        Cfg_Equip_Collection_start_Bean bean;
        //x99表示x阶激活
        if (cfgId % 100 == 99) {
            bean = CfgManager.getCfg_Equip_Collection_start_Container().getValueByKey(cfgId + 2);
            return bean == null ? 0 : bean.getId();
        }
        int nextCfgId = cfgId + 1;
        bean = CfgManager.getCfg_Equip_Collection_start_Container().getValueByKey(nextCfgId);
        if (bean == null) {
            //下一级为null表示当前阶可激活
            nextCfgId = (cfgId / 100) * 100 + 99;
            bean = CfgManager.getCfg_Equip_Collection_start_Container().getValueByKey(nextCfgId);
        }
        if (bean == null) {
            LOGGER.error("获取下一级配置表id，Cfg_Equip_Collection_start_Bean配置表不存在：" + cfgId + "|" + nextCfgId);
            return 0;
        }
        return bean.getId();
    }

    @Override
    public void upStar(Player player, int id) {
        int totalStar = getTotalStar(player);
        if (id == 0) {
            for (Cfg_Equip_Collection_star_Bean tempBean : CfgManager.getCfg_Equip_Collection_star_Container().getValuees()) {
                if (tempBean.getStarNum() > totalStar) {
                    break;
                }
                id = tempBean.getId();
            }
        }
        if (id <= player.getSpiritData().getOpenStar()) {
            return;
        }
        Cfg_Equip_Collection_star_Bean bean = CfgManager.getCfg_Equip_Collection_star_Container().getValueByKey(id);
        if (bean == null) {
            LOGGER.error("Cfg_Equip_Collection_star_Bean配置表不存在:" + id);
            return;
        }
        if (totalStar < bean.getStarNum()) {
            return;
        }
        int oldId = player.getSpiritData().getOpenStar();
        player.getSpiritData().setOpenStar(id);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Spirit, PlayerAttributeType.EQUIP);
        Manager.controlManager.operate(player, FunctionVariable.LingTiStar, id);

        SpiritMessage.ResUpStar.Builder builder = SpiritMessage.ResUpStar.newBuilder();
        builder.setStarNum(id);
        MessageUtils.send_to_player(player, SpiritMessage.ResUpStar.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        //成长日志
        Cfg_Equip_Collection_star_Bean oldBean = CfgManager.getCfg_Equip_Collection_star_Container().getValueByKey(oldId);
        RoleGrowLog.create(player, GrowType.spirit_starUp, 0, oldBean != null ? oldBean.getStarNum() : 0,bean.getStarNum());
    }

    /**
     * 获取灵体装备总星数
     */
    private int getTotalStar(Player player) {
        SpiritData spiritData = player.getSpiritData();

        int totalStart = 0;
        Cfg_Equip_Bean bean;
        for (Map.Entry<Integer, SpiritInfo> entry : spiritData.getSpiritInfoMap().entrySet()) {
            for (Integer equipId : entry.getValue().getEquipList()) {
                bean = CfgManager.getCfg_Equip_Container().getValueByKey(equipId);
                if (bean == null) {
                    continue;
                }
                totalStart += bean.getDiamond_Number();
            }
        }
        return totalStart;
    }

    @Override
    public void activeSpirit(Player player, int spiritId) {
        ConcurrentHashMap<Integer, SpiritInfo> spiritInfo = player.getSpiritData().getSpiritInfoMap();
        if (!spiritInfo.containsKey(spiritId)) {
            return;
        }
        SpiritInfo info = spiritInfo.get(spiritId);
        if (info.isActive()) {
            return;
        }
        for (Integer equipId : info.getEquipList()) {
            if (equipId == 0) {
                return;
            }
        }
        info.setActive(true);

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Spirit, PlayerAttributeType.EQUIP);

        SpiritMessage.ResActiveSpirit.Builder builder = SpiritMessage.ResActiveSpirit.newBuilder();
        builder.setId(spiritId);
        MessageUtils.send_to_player(player, SpiritMessage.ResActiveSpirit.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private void broadCaseSpirit(Player player) {
        MapMessage.ResSpiritIdBroadCast.Builder builder = MapMessage.ResSpiritIdBroadCast.newBuilder();
        builder.setPlayerId(player.getId());
        builder.setSpiritId(player.getSpiritData().getSpiritId());
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResSpiritIdBroadCast.MsgID.eMsgID_VALUE, builder.build().toByteArray(), true);
    }

    @Override
    public void collectEquip(Player player, int spiritId, int equipId, int type, boolean isInherit) {
        int configId = player.getCareer() * 100 + spiritId;
        Cfg_Equip_Collection_Bean bean = CfgManager.getCfg_Equip_Collection_Container().getValueByKey(configId);

        ConcurrentHashMap<Integer, SpiritInfo> spiritInfoMap = player.getSpiritData().getSpiritInfoMap();
        SpiritInfo info = spiritInfoMap.get(spiritId);
        if (info == null) {
            if (bean != null && player.getLevel() >= bean.getDescribe()) {
                info = new SpiritInfo(spiritId);
                spiritInfoMap.put(spiritId, info);
            } else {
                LOGGER.error("灵体未开启,id:" + spiritId);
                return;
            }
        }
        Cfg_Equip_Bean newEquipBean = CfgManager.getCfg_Equip_Container().getValueByKey(equipId);
        if (newEquipBean == null || (!newEquipBean.getGender().contains((int)player.getCareer()) && !newEquipBean.getGender().contains(9))) {
            LOGGER.error("配置表找不到，或装备职业配置错误");
            return;
        }
        int partIndex = newEquipBean.getPart();
        Integer lastValue = info.getEquipList().get(partIndex);
        int oldEquipId = lastValue == 0 ? bean.getEquip().get(partIndex) : lastValue;
        Cfg_Equip_Bean oldEquipBean = CfgManager.getCfg_Equip_Container().getValueByKey(oldEquipId);
        if (oldEquipBean == null) {
            return;
        }
        if (newEquipBean.getGrade() != spiritId) {
            return;
        }

        if (player.getXsGrade() < newEquipBean.getClasslevel()) {
            Cfg_Changejob_Bean jobean = CfgManager.getCfg_Changejob_Container().getValueByKey(newEquipBean.getClasslevel());
            if (jobean != null) {
                String jobName = ServerStr.getChatTableName(jobean.getChangejob_name());
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_EQUIP_WARFIALECLASEE, jobean.getChangejob_condition(), jobName);
            }
            return;
        }

        //放入装备
        if (lastValue == 0 && (newEquipBean.getQuality() <= oldEquipBean.getQuality() && newEquipBean.getDiamond_Number() < oldEquipBean.getDiamond_Number())) {
            return;
        }
        //替换装备，必须品质高于或品质相等但星级高于之前的装备
        if (lastValue != 0 && (newEquipBean.getQuality() <= oldEquipBean.getQuality() && newEquipBean.getDiamond_Number() <= oldEquipBean.getDiamond_Number())) {
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        if (!Manager.backpackManager.manager().onRemoveItem(player, equipId, 1, ItemChangeReason.SpiritDec, actionId)) {
            return;
        }
        int newEquipId = equipId;
        if (type == 0 && lastValue != 0) {
            if (newEquipBean.getQuality() == EquipDefine.EquipQuality_Red && newEquipBean.getDiamond_Number() < oldEquipBean.getDiamond_Number() &&
                    oldEquipBean.getInherit_equip_id() > 0 && isInherit) {
                newEquipId = oldEquipBean.getInherit_equip_id();
            } else {
                Item item = Item.createOwnedItem(lastValue, 1, true);
                if (!Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.SpiritGet, actionId)) {
                    LOGGER.error(String.format("灵体替换添加装备失败！！ %s : %s", player.getId(), lastValue));
                }
            }
        }
        info.getEquipList().set(partIndex, newEquipId);
        Manager.rankListManager.deal().updateEquipAllStar(player);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Spirit, PlayerAttributeType.EQUIP);
        Manager.controlManager.operate(player, FunctionVariable.LingTiEquip, 0);

        SpiritMessage.ResCollectEquip.Builder builder = SpiritMessage.ResCollectEquip.newBuilder();
        builder.setId(spiritId);
        builder.setEquipId(newEquipId);
        MessageUtils.send_to_player(player, SpiritMessage.ResCollectEquip.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        activeSpirit(player, spiritId);

        //激活称号
        if(info.isActiveExt() && bean.getActivit_title() > 0){
            TitleManager.getInstance().deal().useTitleItem(player, bean.getActivit_title(), 1,ItemChangeReason.SpiritGet);
        }

        //bi
        GrowType growType = null;
        if(type == 0){
            growType = GrowType.spirit_equip_wear;
        }else{
            growType = GrowType.spirit_equip_synthesis;
        }
        Manager.biManager.getScript().biEquip(player, type == 0 ? 1 : 2,
                growType.getType() + (spiritId - 3), partIndex, newEquipBean.getId(), newEquipBean.getName(), newEquipBean.getDiamond_Number(), newEquipBean.getGrade(), newEquipBean.getQuality(), 0, newEquipBean.getBind(), 0, 0, 0, 0, 0, 0);
    }

    @Override
    public int getSpiritNum(Player player, int grad, int quality, int star) {
        int num = 0;
        for (Map.Entry<Integer, SpiritInfo> entry : player.getSpiritData().getSpiritInfoMap().entrySet()) {
            if (entry.getKey() == grad) {
                for (int equipId : entry.getValue().getEquipList()) {
                    Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equipId);
                    if (bean == null) {
                        continue;
                    }

                    if (bean.getGrade() >= grad && ((bean.getQuality() == quality && bean.getDiamond_Number() >= star) || bean.getQuality() > quality)) {
                        num++;
                    }
                }
            }
        }
        return num;
    }

    /***********************************************private functions******************************************************/
    /**
     * 获取当前强化配置表id
     */
    private int getEquipPartStrengthenId(EquipPart part, int type) {
        return (EquipPartBaseType.PLAYER + type) * 1000 + part.getLevel();
    }


    /**
     * 判断合成七彩装备的原材料装备的id是否在指定的列表中
     */
    private boolean checkInJoinIdArray(ReadArray<Integer> array, int modelId) {
        if (array.size() == 0) {
            return true;
        }
        for (int i = 0; i < array.size(); i++) {
            if (modelId == array.get(i)) {
                return true;
            }
        }
        return false;
    }

    private void equipWash(Player player, int part, List<Integer> unLockIndexs, EquipPart equipPart) {
        EquipMessage.ResEquipWash.Builder msg = EquipMessage.ResEquipWash.newBuilder();
        msg.setId(part);
        for (Integer unLockIndex : unLockIndexs) {
            Cfg_Wash_Bean washBean = CfgManager.getCfg_Wash_Container().getValueByKey((part + 1) * 10 + unLockIndex);
            EquipMessage.EquipWashInfo.Builder msgWashInfo = EquipMessage.EquipWashInfo.newBuilder();
            EquipWash equipWash = equipPart.getEquipWashs().get(unLockIndex);
            if (equipWash == null) {
                equipWash = new EquipWash();
                equipWash.setId(unLockIndex);
                equipPart.getEquipWashs().put(unLockIndex, equipWash);
            }
            ReadArray<Integer> lastProb = washBean.getProbability().get(washBean.getProbability().size() - 1);
            if (equipWash.getValue() + 1 >= washBean.getBlessing()) {
                equipWash.setValue(0);
                int per = lastProb.get(1);
                equipWash.setPer(per);
            } else {
                equipWash.setValue(equipWash.getValue() + 1);
                WeightCalc<List<Integer>> wc = new WeightCalc<>();
                for (ReadArray<Integer> prob : washBean.getProbability().getValuees()) {
                    List<Integer> perList = new ArrayList<>();
                    perList.add(prob.get(0));
                    perList.add(prob.get(1));
                    wc.addObject(perList, prob.get(2));
                }
                List<Integer> resProb = wc.getObject();
                int per = RandomUtils.random(resProb.get(0), resProb.get(1));
                equipWash.setPer(per);
            }
            Manager.rankListManager.deal().changeEquipWashData(player);
            msgWashInfo.setValue(equipWash.getValue());
            msgWashInfo.setIndex(unLockIndex);
            msgWashInfo.setPer(equipWash.getPer());
            msg.addWashInfos(msgWashInfo);

            //发送传闻
            if(equipWash.getPer() >= lastProb.get(0)){
                MessageUtils.notify_allOnlinePlayer(washBean.getNotice(), washBean.getChatchannel(), MessageString.EquipWashNotice,
                        player.getId() + "",
                        player.getName(),
                        Utils.makeUrlStr(MessageString.EquipWashNotice));
            }
        }
        MessageUtils.send_to_player(player, EquipMessage.ResEquipWash.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        calcEquipWashAttr(equipPart);
    }

    private void calcEquipWashAttr(EquipPart equipPart) {
        BaseIntAttribute att = new BaseIntAttribute(AttributeType.ATTR_MAX);
        Equip equip = equipPart.getEquip();
        Cfg_Equip_Bean model = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        for (int i = 0; i < model.getDiamond_Number(); i++) {
            EquipWash equipWash = equipPart.getEquipWashs().get(i + 1);
            if (equipWash == null){
                continue;
            }
            if (model.getWash().size() <= i) {
                continue;
            }
            ReadArray<Integer> prop = model.getWash().get(i);
            att.addAttribute(prop.get(0), equipWash.getPer() * (prop.get(2) - prop.get(1)) / 10000 + prop.get(1));
        }

        Cfg_Wash_best_Bean bestBean = CfgManager.getCfg_Wash_best_Container().getValueByKey(equipPart.getType() + 1000);
        if (bestBean == null) {
            return;
        }
        Iterator<EquipWash> iter = equipPart.getEquipWashs().values().iterator();
        int allPer = 0;
        while (iter.hasNext()) {
            allPer += iter.next().getPer();
        }

        if (allPer >= bestBean.getCondition()) {
            for (ReadArray<Integer> aii : bestBean.getAttribute().getValuees()) {
                att.addAttribute(aii.get(0), aii.get(1));
            }
        }

        equipPart.setWashScore(allPer);
    }

    private void writeEquipWashLog(Player player, EquipPart part, List<Integer> lock, long equipId) {
        EquipWashLog log = new EquipWashLog();
        log.setRoleId(player.getId());
        log.setRoleName(player.getName());
        log.setActionId(IDConfigUtil.getLogId());
        log.setPart(part.getType());
        log.setEquipId(equipId);
        log.setPos(JsonUtils.toJSONString(lock));
        log.setWash(JsonUtils.toJSONString(part.getEquipWashs()));
        LogService.getInstance().execute(log);
    }

    private void writeEquipSellLog(Player player, List<Long> ids, List<Equip> equips, long actioinId) {
        try {
            EquipSellLog sellLog = new EquipSellLog();
            StringBuilder sb = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();
            if (ids != null) {
                for (Long id : ids) {
                    sb.append(id).append(",");
                }
            }
            for (Equip e : equips) {
                sb1.append(e.toString()).append(",");
            }
            sellLog.setPlayer(player);
            sellLog.setEquipInfo(sb1.toString());
            sellLog.setIds(sb.toString());
            sellLog.setActionId(actioinId);
            LogService.getInstance().execute(sellLog);
        } catch (Exception e) {
            LOGGER.error(e, e);
        }
    }

    private void sendResEquipResolveSet(Player player) {
        ResEquipResolveSet.Builder msg = ResEquipResolveSet.newBuilder();
        msg.addAllQualitys(player.getResolveSettings());
        MessageUtils.send_to_player(player, ResEquipResolveSet.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void sendResEquipSyn(Player player, int state, int type) {
        ResEquipSyn.Builder msg = ResEquipSyn.newBuilder();
        msg.setState(state);
        msg.setType(type);
        MessageUtils.send_to_player(player, ResEquipSyn.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onReqShenpinEquipUp(Player player, int type, int part) {
        if(type < 1 || type > 2){
            return;
        }
        EquipPart equipPart = player.getEquipParts().get(part);
        Equip srcEquip = equipPart.getEquip();
        if(srcEquip == null){
            return;
        }
        Cfg_Equip_shenpin_synthesis_Bean bean = Cfg_Equip_shenpin_synthesis_Container.GetInstance().getValueByKey(type * 10000000 + srcEquip.getItemModelId());
        ReadIntegerArray materials = bean.getJoin_item();
        if(materials == null || materials.size() == 0){
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        int reason_dec = 0;
        int reason_get = 0;
        if(type == 1){//升星
            reason_dec = ItemChangeReason.ShenpinEquipUpStarDec;
            reason_get = ItemChangeReason.ShenpinEquipUpStarGet;
        }else if(type == 2){//升阶
            reason_dec = ItemChangeReason.ShenpinEquipUpStageDec;
            reason_get = ItemChangeReason.ShenpinEquipUpStageGet;
        }
        //判断物品是否足够
        if(!Manager.backpackManager.manager().removeItemOrCurrency(player, materials.get(0), materials.get(1), actionId, reason_dec)){
            LOGGER.error("玩家{} 神品装备提升失败，道具不足", player.getId());
            return;
        }
        Equip tarEquip = (Equip)Item.createItem(bean.getTarget_Equip_ID(), 1, true);

        boolean useFlag = tarEquip.use(player, 1, 0, actionId);
        if (useFlag) {
            boolean remove = removeEquip(player, srcEquip, ItemChangeReason.EquipSyntheticDec, actionId);
            if (!remove) {
                LOGGER.error("扣除装备失败！");
            }
        }

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.EQUIP);

        EquipMessage.ResShenpinEquipUp.Builder res = EquipMessage.ResShenpinEquipUp.newBuilder();
        res.setEquip(Manager.backpackManager.manager().buildItemInfo(tarEquip).build());
        res.setType(type);
        res.setPart(part);
        MessageUtils.send_to_player(player, ResShenpinEquipUp.MsgID.eMsgID_VALUE, res.build().toByteArray());

    }

}
