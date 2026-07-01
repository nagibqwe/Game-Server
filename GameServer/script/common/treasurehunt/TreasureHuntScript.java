package common.treasurehunt;

import com.data.*;
import com.data.bean.*;
import com.data.container.Cfg_Treasure_Hunt_Container;
import com.data.container.Cfg_Treasure_Pop_Container;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.GlobalType;
import com.game.treasurehunt.log.BuyTreasureHuntCountLog;
import com.game.treasurehunt.log.TreasureHuntLog;
import com.game.treasurehunt.script.ITreasureHuntScript;
import com.game.treasurehunt.struct.HuntReward;
import com.game.treasurehunt.struct.TreasureHuntRecord;
import com.game.treasurehuntwuyou.manager.TreasureHuntWuyouManager;
import com.game.treasurehuntxianjia.manager.TreasureHuntXianjiaManager;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.ServerParamUtil;
import com.game.utils.Utils;
import com.mysql.cj.util.TimeUtil;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.core.util.WeightCalc;
import game.message.TreasureHuntMessage;
import game.message.TreasureHuntXianjiaMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 瞿冰冰
 * 2019/7/9
 */
public class TreasureHuntScript implements ITreasureHuntScript {


    private final static long freeTime = Global.Hunt_Free_Time * 60 * 60 *1000 ;


    private static final Logger logger = LogManager.getLogger(TreasureHuntScript.class);

    @Override
    public int getId() {
        return ScriptEnum.TreasureHuntBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void treasureHuntBaseInfo(Player player) {
        TreasureHuntMessage.ResAllWarehouseInfo.Builder msg = TreasureHuntMessage.ResAllWarehouseInfo.newBuilder();
        ConcurrentHashMap<Integer, List<TreasureHuntRecord>> serverMap = ServerParamUtil.treasureHuntRecordMap;
        ArrayList<TreasureHuntMessage.treasureRecordInfo> allServerRecords = recordDataToMsg(serverMap,player);
        msg.addAllAllRecordInfo(allServerRecords);
        Map<Integer, List<TreasureHuntRecord>> records = player.getTreasuryHuntData().getRecords();
        ArrayList<TreasureHuntMessage.treasureRecordInfo> playerRecords = recordDataToMsg(records,player);
        msg.addAllSelfRecordInfo(playerRecords);
        Map<Integer, Map<Long, Item>> storeHouse = player.getTreasuryHuntData().getStoreHouse();
        Cfg_Treasure_Pop_Bean[] beans = Cfg_Treasure_Pop_Container.GetInstance().getValuees();
        for (Cfg_Treasure_Pop_Bean bean : beans) {
            TreasureHuntMessage.warehouseInfo.Builder warehouseBuild = TreasureHuntMessage.warehouseInfo.newBuilder();
            int type = bean.getRewardType();
            warehouseBuild.setType(type);
            warehouseBuild.setFreetimes(getLeftFreeTimes(player, type, bean.getFreeTimes()));
            warehouseBuild.setMustTypeleftTimes(getLeftMustTypeTimes(player, type, bean));
            warehouseBuild.setTodayLeftTimes(getTodayUseTimes(player, type));
            Map<Long, Item> items = storeHouse.get(type);
            if (items != null && items.size() != 0) {
                for (Item item : items.values()) {
                    TreasureHuntMessage.simpleItemInfo.Builder simpleItemInfo = TreasureHuntMessage.simpleItemInfo.newBuilder();
                    simpleItemInfo.setBind(item.isBind() ? 1 : 0);
                    simpleItemInfo.setItemId(item.getItemModelId());
                    simpleItemInfo.setItemNum(item.getNum());
                    simpleItemInfo.setUid(item.getId());
                    warehouseBuild.addSimpleItems(simpleItemInfo.build());
                }
            }
            msg.addInfo(warehouseBuild);
        }
        MessageUtils.send_to_player(player, TreasureHuntMessage.ResAllWarehouseInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        //初始化免费寻宝时间
        for (int i = 1 ; i < TreasureHuntXianjiaManager.XianjiaHuntType;i++){
            onSendFreeTreasureTime(player,i);
        }
    }

    private void onSendFreeTreasureTime(Player player,int type){
        player.getTreasuryHuntData().getLastHuntTime().putIfAbsent(type,0l);
        long lastTime = player.getTreasuryHuntData().getLastHuntTime().get(type);
        int tickTime = lastTime <=0 ? 0:(int) (lastTime / 1000)  + Global.Hunt_Free_Time * 60 * 60;
        TreasureHuntMessage.ResFreeTreasureTime.Builder msg = TreasureHuntMessage.ResFreeTreasureTime.newBuilder();
        msg.setType(type);
        msg.setTick(tickTime);
        MessageUtils.send_to_player(player, TreasureHuntMessage.ResFreeTreasureTime.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    private ArrayList<TreasureHuntMessage.treasureRecordInfo> recordDataToMsg(Map<Integer, List<TreasureHuntRecord>> records,Player player) {
        Iterator<Map.Entry<Integer, List<TreasureHuntRecord>>> recordsIterator = records.entrySet().iterator();
        ArrayList<TreasureHuntMessage.treasureRecordInfo> treasureRecordInfos = new ArrayList<>();
        while (recordsIterator.hasNext()) {
            TreasureHuntMessage.treasureRecordInfo.Builder tBuilder = TreasureHuntMessage.treasureRecordInfo.newBuilder();
            Map.Entry<Integer, List<TreasureHuntRecord>> next = recordsIterator.next();
            Integer type = next.getKey();
            tBuilder.setType(type);
            List<TreasureHuntRecord> list = next.getValue();
            for (TreasureHuntRecord huntRecord : list) {
                TreasureHuntMessage.itemRecordInfo.Builder builder = TreasureHuntMessage.itemRecordInfo.newBuilder();
                builder.setPlayername(huntRecord.getPlayerName());
                builder.setItemId(huntRecord.getItemId());
                builder.setBind(huntRecord.getBind());
                builder.setItemNum(huntRecord.getItemNum());
                builder.setType(huntRecord.getType());
                tBuilder.addInfo(builder);
            }
            if (type < TreasureHuntXianjiaManager.XianjiaHuntType){
                int count =  ServerParamUtil.treasureHuntCount.getOrDefault(type,0);
                tBuilder.setServerLuckCount(count);
            }
            treasureRecordInfos.add(tBuilder.build());
        }
        return treasureRecordInfos;
    }

    /**
     * 购买
     *
     * @param type          类型 1、寻宝奖池 2、铭文奖池 3、装备奖池 4、巅峰奖池 .....
     * @param times         寻宝次数
     * @param messInfoTimes 购买次数
     */
    @Override
    public void buy(Player player, int type, int times, int messInfoTimes) {
        Cfg_Treasure_Pop_Bean pop_bean = Cfg_Treasure_Pop_Container.GetInstance().getValueByKey(type);
        if (pop_bean == null) {
            logger.error(String.format("寻宝{%s}购买失败,表Treasure_Pop无对应数据!!!", type));
            return;
        }
        int todayLeftTimes = getTodayLeftTimes(player, type);
        if (times > todayLeftTimes) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.SeekNumNotEnough);
            logger.error(String.format("今日剩余次数不足!!! type: %s", type));
            return;
        }
        if (Manager.backpackManager.manager().getEmptyGridNum(player) < 1) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            return;
        }
        Integer[] moneyCost = pop_bean.getMoneyCost().getValue();
        Integer curType = moneyCost[0];
        int needTotalNum = moneyCost[1] * times;
        long actionId = IDConfigUtil.getLogId();
        if (!Manager.currencyManager.manager().onDecItemCoin(player, needTotalNum, ItemChangeReason.TreasureHuntBuyDec, actionId, curType)) {
            return;
        }
        Integer[] item = pop_bean.getItem().getValue();
        Integer modelId = item[0];
        int itemNum = item[1] * times;
        int beforeTimes = Manager.backpackManager.manager().getItemNum(player, modelId);

        //赠送道具和货币
        int num = 0;
        Integer[] gold = pop_bean.getGold().getValue();
        if (gold != null && gold.length >= 2) {
            Integer id = gold[0];
            num = gold[1] * times;
            Manager.backpackManager.manager().addItem(player, id, num, false, 0, ItemChangeReason.TreasureHuntBuyGet, actionId);
        }

        List<Item> itemList = new ArrayList<>();
        itemList.addAll(Item.createItems(modelId,itemNum, false));
        if ( !Manager.backpackManager.manager().addItems(player,itemList,ItemChangeReason.TreasureHuntBuyGet, actionId,curType,needTotalNum)){
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,MessageString.System, MessageString.NoBagCell, itemList,ItemChangeReason.TreasureHuntBuyGet);
        }
        if (type == TreasureHuntXianjiaManager.XianjiaHuntType) {
            TreasureHuntXianjiaMessage.ResBuyCountResult.Builder builder = TreasureHuntXianjiaMessage.ResBuyCountResult.newBuilder();
            builder.setType(type);
            MessageUtils.send_to_player(player, TreasureHuntXianjiaMessage.ResBuyCountResult.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        } else {
            TreasureHuntMessage.ResBuyResult.Builder builder = TreasureHuntMessage.ResBuyResult.newBuilder();
            builder.setType(type);
            MessageUtils.send_to_player(player, TreasureHuntMessage.ResBuyResult.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
        int afterTimes = Manager.backpackManager.manager().getItemNum(player, modelId);
        writeBuyTimesLog(player, type, times, beforeTimes, afterTimes, modelId);

        if (type < TreasureHuntXianjiaManager.XianjiaHuntType){
            //购买后直接抽奖
            treasureHunt(player, messInfoTimes, type);
        }else {
            Manager.treasureHuntXianjiaManager.deal().onReqTreasureHuntXijia(player,type,messInfoTimes);
        }
    }

    /**
     * 寻宝逻辑
     *
     * @param type  寻宝类型 1、寻宝奖池 2、铭文奖池 3、装备奖池 4、巅峰奖池 .....
     * @param times 寻宝次数
     */
    @Override
    public void treasureHunt(Player player, int times, int type) {

        //灵魄寻宝走自己逻辑
        if (type == 2) {
            treasureSoulHunt(player, times, false);
            return;
        }
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TreasureHunt)) {
            logger.info("寻宝系统未达到开启条件!!!");
            return;
        }
        int todayLeftTimes = getTodayLeftTimes(player, type);
        if (times > todayLeftTimes) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.SeekNumNotEnough);
            logger.info(String.format("寻宝次数不足！！！寻宝次数{%s}---当前剩余次数{%s}", times, todayLeftTimes));
            return;
        }
        Cfg_Treasure_Pop_Bean pop_bean = Cfg_Treasure_Pop_Container.GetInstance().getValueByKey(type);
        if (pop_bean == null) {
            logger.error(String.format("玩家寻宝失败{%s}类型在表Treasure_Pop不存在！！！", type));
            return;
        }
        //是否免费
        long now =  TimeUtils.Time();
        boolean isfree = false;
        if (times == 1 && type < TreasureHuntXianjiaManager.XianjiaHuntType){
            if (player.getTreasuryHuntData().getLastHuntTime().get(type) <=0 ){
                isfree = true;
            }else {
                long waitTime = now  -  player.getTreasuryHuntData().getLastHuntTime().get(type);
                if (waitTime > freeTime  ){
                    isfree  = true;
                }
            }
        }

        int price = 0;
        int itemId = 0;
        ReadArray<Integer>[] timesCostArr = pop_bean.getTimes().getValuees();
        for (ReadArray<Integer> timesCost : timesCostArr) {
            if (times == timesCost.get(0)) {
                itemId = timesCost.get(1);
                price = timesCost.get(2);
                break;
            }
        }
        if (price == 0 || itemId == 0) {
            logger.error(String.format("玩家寻宝失败{%s}类型在表Treasure_Pop价格异常{%s}不存在！！！", type, times));
            return;
        }
        int needCostItemTimes = 0;
        long actionId = IDConfigUtil.getLogId();
        if (!isfree){
            int leftFreeTimes = getLeftFreeTimes(player, type, pop_bean.getFreeTimes());
            needCostItemTimes = Math.max(0, price - leftFreeTimes);
            if (needCostItemTimes > 0) {
                boolean costSuccess = Manager.backpackManager.manager().onRemoveItem(player, itemId, needCostItemTimes, ItemChangeReason.TreasureHuntDec, actionId);
                if (!costSuccess) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
                    int hasNum = Manager.backpackManager.manager().getItemNum(player, itemId);
                    logger.error(String.format("寻宝--扣除道具失败{%s}   hasNum {%s} needCostItemnum {%s}  ", itemId, hasNum, needCostItemTimes));
                    return;
                }
            }
        }else {
            needCostItemTimes = times;
        }


        //记录免费次数
        int recordNum = price - needCostItemTimes;
        if (recordNum > 0) {
            Manager.countManager.addCount(player, BaseCountType.PTreasureHuntFreeUseTimes, type, Count.RefreshType.CountType_Day, recordNum);
        }

        List<HuntReward> finalItems = new ArrayList<>();
        //检查是否需要生成特殊的宝箱 如果有优先处理
        List<Integer> mustTypeList = new ArrayList<>();
        for (ReadArray<Integer> mustOutRewardsType : pop_bean.getFrequency().getValuees()) {
            Integer[] value = mustOutRewardsType.getValue();
            Integer rewardType = value[0];
            Integer needDoNum = value[1];
            long count = Manager.countManager.getCount(player, BaseCountType.PTreasureTypeHuntTimes, type * 1000 + rewardType);
            long total = count + times;
            if (total >= needDoNum && times > mustTypeList.size()) {
                mustTypeList.add(rewardType);
            }
        }
        //必出类型
        for (Integer mustType : mustTypeList) {
            HuntReward huntReward = createHuntReward(player, mustType, type,isfree);
            if (huntReward == null) {
                logger.error(String.format("寻宝生成必出类型{%s}奖励异常", mustType));
                return;
            }
            addHuntReward(finalItems, huntReward);
            Manager.countManager.setCount(player, BaseCountType.PTreasureTypeHuntTimes, pop_bean.getRewardType() * 1000 + mustType, Count.RefreshType.CountType_Forever, 0);
        }
        //生成寻宝 奖励
        for (int i = 0; i < times - mustTypeList.size(); i++) {
            HuntReward huntReward = createHuntReward(player, 1, type,isfree);
            if (huntReward == null) {
                logger.info("寻宝生成奖励异常");
                return;
            }
            addHuntReward(finalItems, huntReward);
        }
        if (isfree){
            player.getTreasuryHuntData().getLastHuntTime().put(type,now);
        }

        int pointNum = 0;
        Integer[] integral = pop_bean.getIntegral().getValue();
        if (integral != null && integral.length >= 2) {
            Integer currType = integral[0];
            pointNum = integral[1] * times;
            Manager.backpackManager.manager().addItem(player, currType, pointNum, false, 0, ItemChangeReason.TreasureHuntBuyGet, actionId);
        }

        //统计道具
        Map<Integer, Map<Long, Item>> storeHouse = player.getTreasuryHuntData().getStoreHouse();
        Map<Long, TreasureHuntRecord> recordMap = new HashMap<>();
        List<TreasureHuntRecord> recordList = new ArrayList<>();
        for (HuntReward finalItem : finalItems) {
            long key = finalItem.getUid();
            TreasureHuntRecord record = new TreasureHuntRecord();
            record.setItemId(finalItem.getId());
            record.setItemNum(finalItem.getNum());
            record.setPlayerName(player.getName());
            record.setBind(finalItem.isBind() ? 1 : 0);
            record.setType(finalItem.getType());
            recordList.add(record);
            recordMap.put(key, record);
        }


        //寻宝奖励面板消息
        TreasureHuntMessage.ResRewardResultPanle.Builder builder = TreasureHuntMessage.ResRewardResultPanle.newBuilder();
        builder.setType(type);
        builder.setExt1(pointNum);
        builder.setExt2(0);
        TreasureHuntMessage.warehouseInfo.Builder wareHouse = TreasureHuntMessage.warehouseInfo.newBuilder();
        if (type == 2) {
            //仙魄寻宝
            for (HuntReward finalItem : finalItems) {
                TreasureHuntMessage.simpleItemInfo.Builder simpleInfo = TreasureHuntMessage.simpleItemInfo.newBuilder();
                simpleInfo.setBind(finalItem.isBind() ? 1 : 0);
                simpleInfo.setItemId(finalItem.getId());
                simpleInfo.setItemNum(finalItem.getNum());
                simpleInfo.setUid(finalItem.getUid());
                builder.addSimpleItems(simpleInfo.build());
                Manager.immortalSoulManager.gmAddSoul(player, finalItem.getId(), false,ItemChangeReason.TreasureHuntGet);
            }
        } else {
            //不是仙魄寻宝
            treasureHuntRecord(player, recordList, type);
            Map<Long, Item> integerItemMap = storeHouse.get(type);
            if (integerItemMap == null) {
                integerItemMap = new HashMap<>();
                storeHouse.put(type, integerItemMap);
            }
            for (HuntReward reward : finalItems) {
                Item item = Item.createItem(reward.getId(), reward.getNum(), reward.isBind());
                item.setId(reward.getUid());
                TreasureHuntMessage.simpleItemInfo.Builder simpleInfo = TreasureHuntMessage.simpleItemInfo.newBuilder();
                simpleInfo.setBind(item.isBind() ? 1 : 0);
                simpleInfo.setItemId(item.getItemModelId());
                simpleInfo.setItemNum(item.getNum());
                simpleInfo.setUid(reward.getUid());
                builder.addSimpleItems(simpleInfo);
                wareHouse.addSimpleItems(simpleInfo);
                integerItemMap.put(reward.getUid(), item);
                //Manager.backpackManager.manager().sendItemChange(player, ItemChangeReason.TreasureHuntAddItem, item);
            }
        }
        MessageUtils.send_to_player(player, TreasureHuntMessage.ResRewardResultPanle.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        //清空次数
        for (Integer mustType : mustTypeList) {
            Manager.countManager.setCount(player, BaseCountType.PTreasureTypeHuntTimes, pop_bean.getRewardType() * 1000 + mustType, Count.RefreshType.CountType_Forever, 0);
        }

        //寻宝结果消息
        TreasureHuntMessage.ResTreasureResult.Builder msg = TreasureHuntMessage.ResTreasureResult.newBuilder();
        wareHouse.setType(type);
        wareHouse.setFreetimes(getLeftFreeTimes(player, type, pop_bean.getFreeTimes()));
        wareHouse.setMustTypeleftTimes(getLeftMustTypeTimes(player, type, pop_bean));
        wareHouse.setTodayLeftTimes(getTodayUseTimes(player, type));
        msg.setInfo(wareHouse);
        MessageUtils.send_to_player(player, TreasureHuntMessage.ResTreasureResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        //发送下次免费寻宝时间
        if (isfree){
            onSendFreeTreasureTime(player,type);
        }

        Manager.shopManager.limitShop().refresh(player);
        Manager.controlManager.operate(player, FunctionVariable.TreasurePopNum, times);
        //寻宝日志
        writeTreasureHurtLog(player, recordMap.values(), type, times);

        int biType = times > 1 ? (times == 10 ? 2 : 3) : 1;
        Manager.biManager.getScript().biDraw(player, type, biType);
        Manager.controlManager.operate(player, FunctionVariable.Hunt_Treasure_Num, times);
        //记录bi数据
        if(type == 3){//造化
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.TreasureZaoHua, ItemChangeReason.TreasureHuntGet, times);
        }else if(type == 1){//机缘
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.TreasureFind, ItemChangeReason.TreasureHuntGet, times);
        }
    }

    private void addHuntReward(List<HuntReward> finalItems, HuntReward huntReward) {
        boolean isAdd = false;
        for (HuntReward reward : finalItems) {
            if (reward.getId() != huntReward.getId())
                continue;
            Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(huntReward.getId());
            if (itemBean != null) {
                if ((reward.getNum() + huntReward.getNum()) < itemBean.getMax()) {
                    reward.setNum(reward.getNum() + huntReward.getNum());
                } else {
                    finalItems.add(huntReward);
                }
                isAdd = true;
            }
            break;
        }
        if (!isAdd)
            finalItems.add(huntReward);
    }

    /**
     * 创建寻宝奖励
     *
     * @param mustType 必中类型
     * @param type     寻宝类型 1、 机缘寻宝 2 仙魄寻宝
     * @return 奖励物品
     */
    public HuntReward createHuntReward(Player player, int mustType, int type,boolean isFree) {
        //创建唯一id
        HuntReward huntReward = null;
        Cfg_Treasure_Pop_Bean pop_bean = Cfg_Treasure_Pop_Container.GetInstance().getValueByKey(type);
        Integer[] sectionArr = pop_bean.getSection().getValue();
        Integer start = sectionArr[0];
        Integer end = sectionArr[1];
        if (end - start <= 0) {
            logger.info(String.format("寻宝生成奖励失败,表Treasure_Pop奖励区间{%s--%s}异常", start, end));
            return null;
        }
        int addNum = 0;
        WeightCalc<Integer> weightCalc = new WeightCalc<>();
        int worldlevel = GlobalType.getWorldLevel();

        //判断仙甲寻宝幸运值是否达到上限
        boolean luckyvalueIsFull = false;
        boolean normalLuck =false;
        boolean mustBeGetIt = false;//必中
        if (type == TreasureHuntXianjiaManager.XianjiaHuntType || type == TreasureHuntXianjiaManager.QingyiHuntType) {
             int count = (int) Manager.countManager.getCount(player, BaseCountType.PTreasureHuntLuckyvalue, type);
            if (count + Global.Treasure_xianjia_Limit.get(1) >= Global.Treasure_xianjia_Limit.get(0)){
                luckyvalueIsFull = true;
            }
        }

        if(type < TreasureHuntXianjiaManager.XianjiaHuntType){
            int count = ServerParamUtil.treasureHuntCount.getOrDefault(type,0);
            int selfcount = (int) Manager.countManager.getCount(player, BaseCountType.PTreasureHuntLuckyvalue, type);

            if (count + 1 >= pop_bean.getLuck_limit()){
                normalLuck = true;
            }
            if(selfcount + 1 >= pop_bean.getLuck_limit_times()){
                mustBeGetIt = true;
            }
        }

        for (int i = start; i <= end; i++) {
            Cfg_Treasure_Hunt_Bean bean = Cfg_Treasure_Hunt_Container.GetInstance().getValueByKey(i);
            if (bean == null) {
                logger.info(String.format("寻宝生成奖励警告！！！表Treasure_Hunt无对应的奖励{%s},跳过", i));
                continue;
            }
            if (mustType != 1 && mustType != bean.getType()) {
                continue;
            }
            if (type == TreasureHuntXianjiaManager.XianjiaHuntType || type == TreasureHuntXianjiaManager.QingyiHuntType) {
                int curRound = Manager.treasureHuntXianjiaManager.getRound();
                if (worldlevel < bean.getWorldLevel().get(0) ||
                        worldlevel > bean.getWorldLevel().get(1)) {
                    continue;
                }
                if (curRound != bean.getRound())
                    continue;
                if (luckyvalueIsFull){
                    //幸运值达到最高后必出最好奖励 特殊处理
                    if (bean.getIsShow() == 2){
                        weightCalc.addObject(i,100000);
                        addNum++;
                        logger.error("仙甲寻宝幸运值 已达到必出 最好奖励 playerid {}  bean.id {}" , player.getId(),bean.getId());
                        break;
                    }
                }else {
                    if (mustType != 1) {
                        weightCalc.addObject(i, bean.getProbabilityFreq());
                    } else {
                        weightCalc.addObject(i, bean.getProbability());
                    }
                }
            } else {
                //必中普通大奖
                if (mustBeGetIt  && normalLuck){
                    if (bean.getType() == 2){
                        weightCalc.addObject(i,100000);
                        addNum++;
                        logger.error("必中普通大奖  player {}  bean.id {}" , player.getId(),bean.getId());
                        break;
                    }
                }
                else if (normalLuck){
                    //检验
                    if (mustType != 1) {
                        weightCalc.addObject(i, bean.getProbabilityFreq());
                    } else {
                        if (bean.getType() == 2){
                            float luck_limit_mult = pop_bean.getLuck_limit_mult()/10000f;
                            weightCalc.addObject(i, (int)(bean.getProbability() * luck_limit_mult));
                        }else {
                            weightCalc.addObject(i, bean.getProbability());
                        }
                    }
                }else {
                    //检验
                    if (mustType != 1) {
                        weightCalc.addObject(i, bean.getProbabilityFreq());
                    } else {
                        if (isFree && bean.getType() == 2){
                            weightCalc.addObject(i, 0);
                        }else {
                            weightCalc.addObject(i, bean.getProbability());
                        }
                    }
                }
            }
            addNum++;
        }
        //生成出来 再去看
        for (int i = 1; i <= addNum; i++) {
            Integer id = weightCalc.getObjectAndRemove();
            //判断这个是不是被限制了
            Cfg_Treasure_Hunt_Bean bean = Cfg_Treasure_Hunt_Container.GetInstance().getValueByKey(id);
            int serverControl = bean.getServerControl();
            int dailyControl = bean.getDailyControl();
            int serverCount = Manager.countManager.getServerCount(BaseCountType.STreasureHuntItem, bean.getId());
            long playerCount = Manager.countManager.getCount(player, BaseCountType.PTreasureHuntItem, bean.getId());
            if ((serverControl <= serverCount && i != addNum && serverControl != 0) || (dailyControl <= playerCount && i != addNum && dailyControl != 0)) {
                continue;
            }
            Integer[] rewardArr = null;
            for (ReadArray<Integer> readArray : bean.getReward().getValuees()) {
                int career = readArray.get(3);
                if (career == player.getCareer()) {
                    rewardArr = readArray.getValue();
                    break;
                } else if (career == 9) { //通用
                    rewardArr = readArray.getValue();
                    break;
                }
            }

           // int ifRadio = bean.getIfRadio();
            countKey(player, bean.getId());
            huntReward = HuntReward.createHuntReward(rewardArr[0], rewardArr[1], rewardArr[2] != 0, type);

            if (type == TreasureHuntXianjiaManager.XianjiaHuntType|| type == TreasureHuntXianjiaManager.QingyiHuntType){
                if (bean.getIsShow() == 2){//策划说2代表最好奖励要清空幸运值
                    Manager.countManager.setCount(player, BaseCountType.PTreasureHuntLuckyvalue, type,Count.RefreshType.CountType_Forever,0);
                    logger.info("抽到最好大奖 清空幸运值  playerID {}",player.getId());
                }else {
                    Manager.countManager.addCount(player, BaseCountType.PTreasureHuntLuckyvalue,type,Count.RefreshType.CountType_Forever,Global.Treasure_xianjia_Limit.get(1));
                }
            }
            if (type < TreasureHuntXianjiaManager.XianjiaHuntType ){
                if (bean.getType() == 2 && normalLuck ) {//策划说2代表最好奖励要清空幸运值
                    ServerParamUtil.treasureHuntCount.put(type,0);
                    Manager.countManager.setCount(player, BaseCountType.PTreasureHuntLuckyvalue, type,Count.RefreshType.CountType_Forever,0);
                    logger.info("机缘寻宝抽到最好大奖 清空幸运值  playerID {}",player.getId());
                } else  {
                    int count = ServerParamUtil.treasureHuntCount.getOrDefault(type,0);
                    ServerParamUtil.treasureHuntCount.put(type,count+1);
                    Manager.countManager.addCount(player, BaseCountType.PTreasureHuntLuckyvalue,type,Count.RefreshType.CountType_Forever,1);
                }
            }
            //@todo 公告
            if (bean.getIfRadio() != 0 || bean.getChatchannel() != null) {
                String name = Manager.backpackManager.manager().getName(huntReward.getId());
                if (type == 2) {
                    name = Manager.immortalSoulManager.getImmortalSoulName(huntReward.getId());
                }
                //@todo 公告需要物品链接 组装链接
                Item item = Item.createItem(huntReward.getId(), huntReward.getNum(), huntReward.isBind());
                List<Item> itemList = new ArrayList<>();
                itemList.add(item);
                String rewardStr = Utils.makeItemsStr(itemList);
                if (type == 1) {
                    MessageUtils.notify_allOnlinePlayer(bean.getIfRadio() , bean.getChatchannel(), MessageString.TreasureHuntRadio, player.getId()+"", player.getName(), rewardStr);
                } else if (type == 2) {
                    MessageUtils.notify_allOnlinePlayer(bean.getIfRadio() , bean.getChatchannel(), MessageString.TreasureHuntRadioXianPo,player.getId()+"",  player.getName(), rewardStr);
                } else if (type == 3) {
                    MessageUtils.notify_allOnlinePlayer(bean.getIfRadio() , bean.getChatchannel(), MessageString.TreasureHuntRadioZaoHua, player.getId()+"", player.getName(), rewardStr);
                } else if (type == 4) {
                    MessageUtils.notify_allOnlinePlayer(bean.getIfRadio() , bean.getChatchannel(), MessageString.TreasureHuntRadioHongMeng,player.getId()+"",  player.getName(), rewardStr);
                } else if (type == 5) {
                    MessageUtils.notify_allOnlinePlayer(bean.getIfRadio() , bean.getChatchannel(), MessageString.TreasureHuntRadioShangGu,player.getId()+"",  player.getName(), rewardStr);
                } else if (type == 6) {
                    MessageUtils.notify_allOnlinePlayer(bean.getIfRadio() , bean.getChatchannel(), MessageString.TreasureHuntRadioXianJia,player.getId()+"",  player.getName(), rewardStr);
                }
            }
            break;
        }
        return huntReward;
    }

    /**
     * 计数器
     *
     * @param rewardId 奖励的id
     */
    private void countKey(Player player, int rewardId) {
        Cfg_Treasure_Hunt_Bean bean = Cfg_Treasure_Hunt_Container.GetInstance().getValueByKey(rewardId);

        if (bean == null) {
            logger.info(String.format("寻宝计数器跟新失败,{%s}在表Treasure_Hunt中不存在!!!", rewardId));
            return;
        }
        if (bean.getDailyControl() > 0) {
            Manager.countManager.addCount(player, BaseCountType.PTreasureHuntItem, rewardId, Count.RefreshType.CountType_Day, 1);
        }

        if (bean.getServerControl() > 0) {
            Manager.countManager.addServerCount(BaseCountType.STreasureHuntItem, Count.RefreshType.CountType_Day, rewardId, 1);
        }
        //需要清理才计数
        Cfg_Treasure_Pop_Bean pop_bean = Cfg_Treasure_Pop_Container.GetInstance().getValueByKey(bean.getRewardType());
        if (pop_bean == null) {
            logger.info(String.format("寻宝奖励类型计数器跟新失败,{%s}在表Treasure_Hunt中不存在!!!", rewardId));
            return;
        }
        ReadArray<Integer>[] rewardTypeArr = pop_bean.getFrequency().getValuees();
        Manager.countManager.addCount(player, BaseCountType.PTodayTreasureTypeHuntTimes, pop_bean.getRewardType(), Count.RefreshType.CountType_Day, 1);
        Manager.countManager.addCount(player, BaseCountType.PTotalTreasureTypeHuntTimes, pop_bean.getRewardType(), Count.RefreshType.CountType_Forever, 1);
        for (ReadArray<Integer> rewardType : rewardTypeArr) {
            Integer type = rewardType.getValue()[0];
            Manager.countManager.addCount(player, BaseCountType.PTreasureTypeHuntTimes, pop_bean.getRewardType() * 1000 + type, Count.RefreshType.CountType_Forever, 1);
        }
    }

    public int getTodayLeftTimes(Player player, int type) {
        long count = Manager.countManager.getCount(player, BaseCountType.PTodayTreasureTypeHuntTimes, type);
        int leftTimes = 0;
        if (type == TreasureHuntXianjiaManager.XianjiaHuntType) {
            leftTimes = (int) Math.max(0, Global.Xinajia_Hunt_pra.get(0) - count);
        } else {
            leftTimes = (int) Math.max(0, Global.TreasureTimes - count);
        }
        return leftTimes;
    }

    public int getTodayUseTimes(Player player, int type) {
        int count = (int) Manager.countManager.getCount(player, BaseCountType.PTodayTreasureTypeHuntTimes, type);
        return count;
    }

    /**
     * 暂时只设计一种类型 需要的时候这里需要跟着修改
     */
    public int getLeftMustTypeTimes(Player player, int type, Cfg_Treasure_Pop_Bean pop_bean) {
        ReadArray<Integer>[] values = pop_bean.getFrequency().getValuees();
        if (values == null || values.length <= 0)
            return 0;
        Integer rewardType = values[0].get(0);
        Integer times = values[0].get(1);
        long count = Manager.countManager.getCount(player, BaseCountType.PTreasureTypeHuntTimes, type * 1000 + rewardType);
        int needTimes = (int) (times - count);
        if (needTimes < 0) {
            needTimes = 1;
        }
        return needTimes;
    }

    /**
     * 全服记录 和单人 记录
     *
     * @param records 寻宝记录
     * @param type    寻宝类型
     */
    public void treasureHuntRecord(Player player, List<TreasureHuntRecord> records, int type) {
        //仙魄寻宝个人全服无记录
        if (type == 2) {
            return;
        }
        int serverMax = Global.TreasureServer;
        int singleMax = Global.TreasurePepole;
        Map<Integer, List<TreasureHuntRecord>> recordMap = player.getTreasuryHuntData().getRecords();
        List<TreasureHuntRecord> treasureHuntRecords = recordMap.get(type);
        List<TreasureHuntRecord> serverRecords = ServerParamUtil.treasureHuntRecordMap.get(type);
        for (TreasureHuntRecord re : records) {
            //添加个人记录
            if (treasureHuntRecords == null) {
                treasureHuntRecords = new ArrayList<>();
                recordMap.put(type, treasureHuntRecords);
            }
            if (treasureHuntRecords.size() >= singleMax) {
                treasureHuntRecords.remove(0);
            }
            treasureHuntRecords.add(re);

            //添加全服记录
            if (serverRecords == null) {
                serverRecords = new ArrayList<>();
                ServerParamUtil.treasureHuntRecordMap.put(type, serverRecords);
            }
            if (serverRecords.size() >= serverMax) {
                serverRecords.remove(0);
            }
            serverRecords.add(re);
        }
        //发送前端消息 更新 全服记录和
        updatePlayerRecord(player, treasureHuntRecords, type, 0);
        updatePlayerRecord(player, serverRecords, type, 1);
        ServerParamUtil.saveTreasureHunt();
        ServerParamUtil.saveTreasureHuntCount();
    }

    /**
     * 更新寻宝记录
     *
     * @param records 寻宝记录
     * @param type    寻宝类型
     */
    private void updatePlayerRecord(Player player, Collection<TreasureHuntRecord> records, int type, int updateType) {
        TreasureHuntMessage.ResUpdateRecord.Builder msg = TreasureHuntMessage.ResUpdateRecord.newBuilder();
        TreasureHuntMessage.treasureRecordInfo.Builder builder = TreasureHuntMessage.treasureRecordInfo.newBuilder();
        for (TreasureHuntRecord record : records) {
            TreasureHuntMessage.itemRecordInfo.Builder itemInfo = TreasureHuntMessage.itemRecordInfo.newBuilder();
            itemInfo.setItemId(record.getItemId());
            itemInfo.setItemNum(record.getItemNum());
            itemInfo.setPlayername(record.getPlayerName());
            itemInfo.setBind(record.getBind());
            itemInfo.setType(record.getType());
            builder.addInfo(itemInfo.build());
        }
        builder.setType(type);
        msg.setType(updateType);
        if (type < TreasureHuntXianjiaManager.XianjiaHuntType){
            int count = ServerParamUtil.treasureHuntCount.getOrDefault(type,0);
            builder.setServerLuckCount(count);
        }
        msg.setRecord(builder);
        if (updateType == 0) {
            MessageUtils.send_to_player(player, TreasureHuntMessage.ResUpdateRecord.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        } else {
            MessageUtils.send_to_all_player(TreasureHuntMessage.ResUpdateRecord.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }


    /**
     * 获取玩家的免费次数
     *
     * @param type     寻宝类型
     * @param allCount 总的次数
     */
    public int getLeftFreeTimes(Player player, int type, int allCount) {
        int playerCount = (int) Manager.countManager.getCount(player, BaseCountType.PTreasureHuntFreeUseTimes, type);
        int leftTimes = allCount - playerCount;
        return leftTimes <= 0 ? 0 : leftTimes;
    }

    @Override
    public void onekeyExtract(Player player, int type) {
        Map<Integer, Map<Long, Item>> storeHouse = player.getTreasuryHuntData().getStoreHouse();
        Iterator<Map.Entry<Integer, Map<Long, Item>>> storeHouseiterator = storeHouse.entrySet().iterator();
        List<Item> list = new ArrayList<>();
        while (storeHouseiterator.hasNext()) {
            Map.Entry<Integer, Map<Long, Item>> housenext = storeHouseiterator.next();
            Iterator<Map.Entry<Long, Item>> iterator = housenext.getValue().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, Item> next = iterator.next();
                Item item = next.getValue();
                boolean success = Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.TreasureHuntExtractGet, IDConfigUtil.getLogId());
                if (!success) {
                    logger.info(String.format("{%s}玩家{%s}寻宝提取类型{%s}失败道具{%s}!!!", player.getName(), player.getId(), type, item.getItemModelId()));
                    list.add(item);
                }
                iterator.remove();
            }
        }
        if (list.size() > 0){
            if(type == TreasureHuntWuyouManager.HUNT_TYPE){
                Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.System, String.valueOf(MessageString.WUYOU_STORE_ONEKEY_EXTRACT), list,ItemChangeReason.WuyouHuntExtractGet);
            }else{
                Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.System, String.valueOf(MessageString.TreasureStoreOnekeyExtract), list,ItemChangeReason.TreasureStoreOnekeyExtractGet);
            }
        }
        TreasureHuntMessage.ResOnekeyExtractResult.Builder msg = TreasureHuntMessage.ResOnekeyExtractResult.newBuilder();
        msg.setType(type);
        MessageUtils.send_to_player(player, TreasureHuntMessage.ResOnekeyExtractResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public void writeTreasureHurtLog(Player player, Collection<TreasureHuntRecord> rewards, int times, int type) {
        TreasureHuntLog log = new TreasureHuntLog();
        log.setRewards(JsonUtils.toJSONString(rewards));
        log.setActionId(IDConfigUtil.getId());
        log.setPlatform(player.getPlatformName());
        log.setTreasureHutTimes(times);
        log.setCreateSid(player.getCreateServerId());
        log.setRoleId(player.getId());
        log.setRoleName(player.getName());
        log.setUserId(player.getUserId());
        log.setType(type);
        log.setPlayer(player);
        LogService.getInstance().execute(log);
    }

    public void writeBuyTimesLog(Player player, int type, int times, int beforeTimes, int afterTimes, int modelId) {
        BuyTreasureHuntCountLog log = new BuyTreasureHuntCountLog();
        log.setAfterTimes(afterTimes);
        log.setActionId(IDConfigUtil.getId());
        log.setBuyTimes(times);
        log.setType(type);
        log.setBeforeTimes(beforeTimes);
        log.setModelId(modelId);
        log.setPlayer(player);
        LogService.getInstance().execute(log);
    }


    //--------灵魄寻宝---------------//


    public void treasureSoulHunt(Player player, int times, boolean isCopyMap) {
        if (isCopyMap) {
            Cfg_Immortal_soul_hunt_Bean bean = getHuntBean(1, -1);
            createSoulHunt(player, bean, times,0,true);
        } else {
            int allHuntTime = getSoulHuntCount(player, times);
            Cfg_Immortal_soul_hunt_Bean bean = null;
            int setTime = 0;
            if (times == Manager.treasureHuntManager.ONE_TIMES) {
                bean = getHuntBean(times, -1);
                setTime = getNewSoulTimes(times, allHuntTime, Global.Sword_Soul_chouqu_times.get(0));
            } else if (times == Manager.treasureHuntManager.TEN_TIMES) {
                bean = getHuntBean(times, -1);
                setTime = getNewSoulTimes(times, allHuntTime, Global.Sword_Soul_chouqu_times.get(1));
            }
            if (bean == null) {
                logger.info(" Cfg_Immortal_soul_hunt_Bean  null  times " + times);
                return;
            }

            int coinType = bean.getBasic_attributes().get(0);
            long needNum = bean.getBasic_attributes().get(1);
            long coinNum = Manager.currencyManager.manager().getCurrencyNum(player, coinType);
            float needBuy = 0;
            if (coinNum < needNum) {
                needBuy = needNum - coinNum;
                int needBuyTime = (int) Math.ceil(needBuy / bean.getType().get(1));
                int needGold = needBuyTime * bean.getExchange_ranking();
                long sendSoulCoin = needBuyTime * bean.getType().get(1);
                long sendMoney = needBuyTime * bean.getExp().get(1);
                // 扣钱 Reason
                long actionId = IDConfigUtil.getLogId();
                if (!Manager.currencyManager.manager().onDecItemCoin(player, needGold, ItemChangeReason.SoulBuyDec, actionId, ItemCoinType.GemCoin)) {
                    logger.error("玩家" + player.getName() + " " + player.getId() + "购买灵魄抽奖货币 扣除绑定元宝失败！");
                    return;
                }
                Manager.currencyManager.manager().onAddItemCoin(player, bean.getType().get(0), sendSoulCoin, ItemChangeReason.SoulBuySendGet, 0);
                Manager.currencyManager.manager().onAddItemCoin(player, bean.getExp().get(0), sendMoney, ItemChangeReason.SoulBuySendGet, 0);
            }
            if (!Manager.currencyManager.manager().onDecItemCoin(player, needNum, ItemChangeReason.SoulHuntDec, 0, coinType)) {
                logger.error("玩家" + player.getName() + " " + player.getId() + "扣除灵魄抽奖货币失败 ！type " + coinType);
                return;
            }
            createSoulHunt(player, bean, times,allHuntTime,false);
            setSoulHuntCount(player, setTime, times);
            Manager.controlManager.operate(player, FunctionVariable.LingPoLottery_Any, times);
            //记录bi数据
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.TreasureXianPo, ItemChangeReason.TreasureHuntGet,times);
        }
    }

    /**
     * 一键回收
     * @param player
     */
    public void onReqOnekeyRecovery(Player player){
        HashMap<Integer,Integer> returnMoneyMaps = new HashMap<>();
        long logid =  IDConfigUtil.getLogId();
        for (Cfg_Treasure_Recovery_Bean bean :CfgManager.getCfg_Treasure_Recovery_Container().getValuees()){
            if (bean.getCondition() >0 ){
                if (!Manager.controlManager.deal().isOpenFunction(player, bean.getCondition())) {
                    logger.error("功能未开启 {}", bean.getCondition());
                    continue;
                }
            }

            for (ReadArray<Integer> readArray: bean.getItem().getValuees()){
                if (readArray.get(1) == 9 || readArray.get(1) == player.getCareer()){//通用
                    int num =  Manager.backpackManager.manager().getItemNum(player ,readArray.get(0));
                    if ( Manager.backpackManager.manager().onRemoveItem(player,readArray.get(0),num,ItemChangeReason.TreasureRecoveryCost,logid)){
                        int allReturnNum = num * bean.getReward().get(1);
                        int coinId = bean.getReward().get(0);
                        int hasNum =  returnMoneyMaps.getOrDefault(coinId,0);
                        returnMoneyMaps.put(coinId,hasNum + allReturnNum);
                    }
                }
            }
        }
        for (Map.Entry<Integer,Integer> entry :returnMoneyMaps.entrySet()){
            Manager.currencyManager.manager().onAddItemCoin(player,entry.getKey(),entry.getValue(),ItemChangeReason.TreasureRecoveryGet,logid);
        }
        sendRecoveryResult(player,returnMoneyMaps.size()>0 ?1:0);
    }


    /**
     * 选择部分回收
     * @param player
     * @param itemId
     * @param num
     */
    public void onReqChooseRecovery(Player player,int itemId,int num){

        long logid =  IDConfigUtil.getLogId();
        for (Cfg_Treasure_Recovery_Bean bean :CfgManager.getCfg_Treasure_Recovery_Container().getValuees()){
            for (ReadArray<Integer> readArray: bean.getItem().getValuees()){
                if (readArray.get(0) != itemId){
                    continue;
                }
                  if (bean.getCondition() >0 ) {
                      if (!Manager.controlManager.deal().isOpenFunction(player, bean.getCondition())) {
                          logger.error("功能未开启 {}", bean.getCondition());
                          sendRecoveryResult(player, 0);
                          return;
                      }
                  }
                if (readArray.get(1) == 9 || readArray.get(1) == player.getCareer()) {//通用
                    if (!Manager.backpackManager.manager().canDeleteItemNum(player ,readArray.get(0),num)){
                        logger.error("回收失败 材料不足 {}",num);
                        sendRecoveryResult(player,0);
                        return;
                    }
                    if ( !Manager.backpackManager.manager().onRemoveItem(player,readArray.get(0),num,ItemChangeReason.TreasureRecoveryCost,logid)){
                        sendRecoveryResult(player,0);
                        logger.error("回收失败 材料不足 {}  {} ",itemId,num);
                        sendRecoveryResult(player,0);
                        return;
                    }
                    Manager.currencyManager.manager().onAddItemCoin(player,bean.getReward().get(0),
                            (long) bean.getReward().get(1) *num,ItemChangeReason.TreasureRecoveryGet,logid);
                    sendRecoveryResult(player,1);
                }
            }
        }
    }

    private void sendRecoveryResult(Player player,int result){
        TreasureHuntMessage.ResRecoveryResult.Builder msg = TreasureHuntMessage.ResRecoveryResult.newBuilder();
        msg.setResult(result);
        MessageUtils.send_to_player(player, TreasureHuntMessage.ResRecoveryResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    private Cfg_Immortal_soul_hunt_Bean getNewSoulBean(int times, int allHuntTime, int restTimes) {
        Cfg_Immortal_soul_hunt_Bean bean = null;
        if (times + allHuntTime >= restTimes) {
            bean = getHuntBean(times, restTimes);
        } else {
            bean = getHuntBean(times, -1);
        }
        return bean;
    }

    private int getNewSoulTimes(int times, int allHuntTime, int restTimes) {
        int setTime = 0;
        if (times + allHuntTime >= restTimes) {
            setTime = 0;
        } else {
            setTime = times + allHuntTime;
        }
        return setTime;
    }

    private void setSoulHuntCount(Player player, int times, int addtime) {
        if (addtime == 1) {
            Manager.countManager.setCount(player, BaseCountType.TreasureSoulOneTimeCount, 0, Count.RefreshType.CountType_Forever, times);
        } else if (addtime == 10) {
            Manager.countManager.setCount(player, BaseCountType.TreasureSoulTenTimesCount, 0, Count.RefreshType.CountType_Forever, times);
        }
        Manager.countManager.addCount(player, BaseCountType.TreasureSoulTotalCount, 0, Count.RefreshType.CountType_Forever, addtime);
    }

    private int getSoulHuntCount(Player player, int times) {
        if (times == 1) {
            return (int) Manager.countManager.getCount(player, BaseCountType.TreasureSoulOneTimeCount, 0);
        } else if (times == 10) {
            return (int) Manager.countManager.getCount(player, BaseCountType.TreasureSoulTenTimesCount, 0);
        }
        return 0;
    }


    private Cfg_Immortal_soul_hunt_Bean getHuntBean(int condition_1, int condition_2) {
        for (Cfg_Immortal_soul_hunt_Bean bean : CfgManager.getCfg_Immortal_soul_hunt_Container().getValuees()) {
            if ((bean.getTimes() == condition_1) && bean.getSpecial_times() == condition_2) {
                return bean;
            }
        }
        return null;
    }

    private void createSoulHunt(Player player, Cfg_Immortal_soul_hunt_Bean hunt_bean, int times ,int nowTimes,boolean isCopy) {

        TreasureHuntMessage.ResRewardResultPanle.Builder builder = TreasureHuntMessage.ResRewardResultPanle.newBuilder();
        builder.setType(2);
        builder.setExt1(0);
        builder.setExt2(0);
        List<Item> items = new ArrayList<>();
        Item item = null;
        int allTimes = 0;
        int soulItemID = 0;
        for (int i = 0; i < times; i++) {
             allTimes = nowTimes + i + 1;
            if (isCopy){
                soulItemID = randomSoulId(player, hunt_bean,false);
            }else {
                if (allTimes >= hunt_bean.getSpecial_times1()){
                    soulItemID = randomSoulId(player, hunt_bean,true);
                }else {
                    soulItemID = randomSoulId(player, hunt_bean,false);
                }
            }
            item = Item.createItem(soulItemID, 1, true);
            if (item == null) {
                continue;
            }
            items.add(item);
            TreasureHuntMessage.simpleItemInfo.Builder simpleInfo = TreasureHuntMessage.simpleItemInfo.newBuilder();
            simpleInfo.setBind(item.isBind() ? 1 : 0);
            simpleInfo.setItemId(item.getItemModelId());
            simpleInfo.setItemNum(item.getNum());
            simpleInfo.setUid(item.getId());
            builder.addSimpleItems(simpleInfo.build());

        }
        MessageUtils.send_to_player(player, TreasureHuntMessage.ResRewardResultPanle.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.SoulHuntGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.System, String.valueOf(MessageString.XJ_XUNBAO), items, ItemChangeReason.SoulHuntGet);
        }
        items = Item.createItems(hunt_bean.getReward());
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.SoulHuntGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.System, String.valueOf(MessageString.XJ_XUNBAO), items, ItemChangeReason.SoulHuntGet);
        }
    }

    private int randomSoulId(Player player, Cfg_Immortal_soul_hunt_Bean hunt_bean,boolean isSpecial) {
        int soulLayer = player.getFlyswordAllInfo().getSwordSoulLayer() - 1;
        ReadIntegerArrayEs type_proArr =isSpecial ?
                hunt_bean.getSpecial_type_probability():hunt_bean.getType_probability() ;
        ReadIntegerArrayEs quality_proArr = isSpecial ?
                hunt_bean.getSpecial_quality_probability() : hunt_bean.getQuality_probability();
        ReadIntegerArray add_type_proArr = hunt_bean.getAdd_type_probability();


        int type_proID = getArrayEsWeightCalc(type_proArr);
        int qualityID = getArrayEsWeightCalc(quality_proArr);
        int star = (qualityID == 7?1:0) *100;  //qualityID==7,star == 1 其余一定为0
        int levelID = type_proID  == 2 ? 0:getSoulLevelID(add_type_proArr, soulLayer); //策划说type==2,level一定为0
        type_proID = type_proID * 100000;
        qualityID = qualityID * 1000;
        int finalID = 6000000 + type_proID + qualityID + star + levelID ;
        return finalID;
    }

    private int getArrayEsWeightCalc(ReadIntegerArrayEs arrayEs) {
        WeightCalc<Integer> weightCalc = new WeightCalc<>();
        for (ReadArray<Integer> array : arrayEs.getValuees()) {
            weightCalc.addObject(array.get(0), array.get(1));
        }
        return weightCalc.getObjectAndRemove();
    }

    private int getSoulLevelID(ReadIntegerArray proArr, int level) {
        List<Integer> arrlist = new ArrayList<>();
        level = level < 1 ? 1 : level;
        Cfg_Sword_soul_copy_Bean copy_bean = CfgManager.getCfg_Sword_soul_copy_Container().getValueByKey(level);
        if (copy_bean == null) {
            logger.error("Cfg_Sword_soul_copy_Bean  is null  " + level);
            return 0;
        }
        for (Integer value : proArr.getValue()) {
            if (value <= copy_bean.getReward_type()) {
                arrlist.add(value);
            }
        }
        int index = RandomUtils.random(0, arrlist.size() - 1);
        return arrlist.get(index);
    }
}
