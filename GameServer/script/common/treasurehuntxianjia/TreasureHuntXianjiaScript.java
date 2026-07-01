package common.treasurehuntxianjia;

import com.data.*;
import com.data.bean.Cfg_Item_Bean;
import com.data.bean.Cfg_Treasure_Pop_Bean;
import com.data.bean.Cfg_Treasure_xianjiaSecret_Bean;
import com.data.container.Cfg_Treasure_Pop_Container;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemTypeConst;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.count.structs.VariantType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.treasurehunt.struct.HuntReward;
import com.game.treasurehunt.struct.TreasureHuntRecord;
import com.game.treasurehuntxianjia.manager.TreasureHuntXianjiaManager;
import com.game.treasurehuntxianjia.script.ITreasureHuntXianjia;
import com.game.treasurehuntxianjia.timer.TreasureHuntTimer;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.ServerParamUtil;
import common.treasurehunt.TreasureHuntScript;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.core.util.WeightCalc;
import game.message.TreasureHuntMessage;
import game.message.TreasureHuntXianjiaMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 542 on 2020/2/25.
 */
public class TreasureHuntXianjiaScript implements ITreasureHuntXianjia {


    private static final Logger logger = LogManager.getLogger(TreasureHuntScript.class);

    @Override
    public int getId() {
        return ScriptEnum.TreasureHuntXianjiaScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


    public void onLineInit(Player player) {
        TreasureHuntXianjiaMessage.ResAllWarehouseXianjiaInfo.Builder msg = TreasureHuntXianjiaMessage.ResAllWarehouseXianjiaInfo.newBuilder();
        ConcurrentHashMap<Integer, List<TreasureHuntRecord>> serverMap = ServerParamUtil.treasureHuntRecordMap;
        ArrayList<TreasureHuntMessage.treasureRecordInfo> allServerRecords = recordDataToMsg(serverMap);
        msg.addAllAllRecordInfo(allServerRecords);
        Map<Integer, List<TreasureHuntRecord>> records = player.getTreasuryHuntData().getRecords();
        ArrayList<TreasureHuntMessage.treasureRecordInfo> playerRecords = recordDataToMsg(records);
        msg.addAllSelfRecordInfo(playerRecords);
        Map<Integer, Map<Long, Item>> storeHouse = player.getTreasureHuntXianjiaData().getXianjiaStoreHouse();
        Cfg_Treasure_Pop_Bean[] beans = Cfg_Treasure_Pop_Container.GetInstance().getValuees();
        for (Cfg_Treasure_Pop_Bean bean : beans) {
            TreasureHuntMessage.warehouseInfo.Builder warehouseBuild = TreasureHuntMessage.warehouseInfo.newBuilder();
            int type = bean.getRewardType();
            if (type != TreasureHuntXianjiaManager.MibaoHuntType && type != TreasureHuntXianjiaManager.XianjiaHuntType)
                continue;
            warehouseBuild.setType(type);
            warehouseBuild.setFreetimes(getLeftFreeTimes(player, type, bean.getFreeTimes()));
            warehouseBuild.setMustTypeleftTimes(Manager.treasureHuntManager.deal().getLeftMustTypeTimes(player, type, bean));
            warehouseBuild.setTodayLeftTimes(getTodayLeftTimes(player, type));
            Map<Long, Item> items = storeHouse.get(type);
            if (items != null && items.size() != 0) {
                for (Item item : items.values()) {
                    TreasureHuntMessage.simpleItemInfo.Builder simpleItemInfo = TreasureHuntMessage.simpleItemInfo.newBuilder();
                    simpleItemInfo.setBind(item.isBind() ? 1 : 0);
                    simpleItemInfo.setItemId(item.getItemModelId());
                    simpleItemInfo.setUid(item.getId());
                    simpleItemInfo.setItemNum(item.getNum());
                    warehouseBuild.addSimpleItems(simpleItemInfo.build());
                }
            }
            msg.addInfo(warehouseBuild);
        }
        msg.setXijiaHuntCount(player.getTreasureHuntXianjiaData().getHuntXianjiaCount());
        msg.setMibaoHuntCount(player.getTreasureHuntXianjiaData().getMibaoCount());
        MessageUtils.send_to_player(player, TreasureHuntXianjiaMessage.ResAllWarehouseXianjiaInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());


        if (isNeeedReviseXianjiaData(player)) {
            int curRound = Manager.treasureHuntXianjiaManager.getRound();
            initXianjiaData(player, curRound);
        }
        onReqOpenXianjiaHuntPanel(player, TreasureHuntXianjiaManager.MibaoHuntType);
    }


    public void treasureTick() {
        long nowTime = TimeUtils.Time();
        long waitTime = Manager.treasureHuntXianjiaManager.getNextRoundTime();
        if (nowTime < waitTime) {
            return;
        }
        int maxRound = Global.Xinajia_Hunt_pra.get(2);
        int curRound = Manager.treasureHuntXianjiaManager.getRound();
        int nextRound = (curRound + 1) > maxRound ? 1 : (curRound + 1);
        waitTime = getWaitTime();
        Manager.treasureHuntXianjiaManager.setRound(nextRound);
        Manager.treasureHuntXianjiaManager.setNextRoundTime(waitTime);
        ServerParamUtil.huntXianjiaData.clear();
        ServerParamUtil.huntXianjiaData.put(nextRound, waitTime);
        ServerParamUtil.savaXianjiaHuntData();
    }

    public void onReqOpenXianjiaHuntPanel(Player player, int type) {
        int curRound = Manager.treasureHuntXianjiaManager.getRound();
        if (player.getTreasureHuntXianjiaData().getRound() != curRound) {
            initXianjiaData(player, curRound);
        }
        TreasureHuntXianjiaMessage.ResOpenXianjiaHuntPanel.Builder msg =
                TreasureHuntXianjiaMessage.ResOpenXianjiaHuntPanel.newBuilder();
        msg.setType(type);
        msg.setRound(curRound);
        msg.setHuntTime(player.getTreasureHuntXianjiaData().getHuntXianjiaCount());
        long lastTime = Manager.treasureHuntXianjiaManager.getNextRoundTime() - TimeUtils.Time();
        lastTime = lastTime < 0 ? 0 : lastTime;
        msg.setLastTime(lastTime);
        msg.setDayHuntCount(getTodayUseTime(player, TreasureHuntXianjiaManager.XianjiaHuntType));
        msg.setMibaoHuntCount(player.getTreasureHuntXianjiaData().getMibaoCount());
        if (type == TreasureHuntXianjiaManager.MibaoHuntType) {
            msg.addAllReward1(getMibaoData(player.getTreasureHuntXianjiaData().getMibaoreward_1()));
            msg.addAllReward2(getMibaoData(player.getTreasureHuntXianjiaData().getMibaoreward_2()));
        }
        MessageUtils.send_to_player(player, TreasureHuntXianjiaMessage.ResOpenXianjiaHuntPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 寻宝
     *
     * @param player
     * @param type
     * @param times
     */
    public void onReqTreasureHuntXijia(Player player, int type, int times) {
        if (type != TreasureHuntXianjiaManager.XianjiaHuntType  && type != TreasureHuntXianjiaManager.QingyiHuntType) {
            logger.error(String.format("类型不是仙甲寻宝 {%s}!!!", type));
            return;
        }
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.XJXunbao)) {
            logger.info("仙甲寻宝系统未达到开启条件!!!");
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
        int leftFreeTimes = getLeftFreeTimes(player, type, pop_bean.getFreeTimes());
        int needCostItemTimes = Math.max(0, price - leftFreeTimes);
        long actionId = IDConfigUtil.getLogId();
        if (needCostItemTimes > 0) {
            boolean costSuccess = Manager.backpackManager.manager().onRemoveItem(player, itemId, needCostItemTimes, ItemChangeReason.TreasureXianjiaHuntDec, actionId);
            if (!costSuccess) {
                int hasNum = Manager.backpackManager.manager().getItemNum(player, itemId);
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
                logger.error(String.format("寻宝--扣除道具失败{%s}   hasNum {%s} needCostItemnum {%s}  ", itemId, hasNum, needCostItemTimes));
                return;
            }
        }
        Manager.countManager.addVariant(player, VariantType.Daily_XianJiaXunBao_Times, needCostItemTimes);
        //记录免费次数
        int recordNum = price - needCostItemTimes;
        if (recordNum > 0) {
            Manager.countManager.addCount(player, BaseCountType.PTreasureHuntFreeUseTimes, type, Count.RefreshType.CountType_Day, recordNum);
        }
        List<HuntReward> finalItems = new ArrayList<>();
        //有几率会出
        int index = 0;
        int extractCount = 0;
        for (ReadArray<Integer> guarantees_rewardType : pop_bean.getGuarantees_reward().getValuees()) {
            int alreadIndex = player.getTreasureHuntXianjiaData().getAlreadyIndex();
            index++;
            if (alreadIndex >= index)
                continue;
            if (extractCount >= times)
                break;
            Integer[] value = guarantees_rewardType.getValue();
            Integer rewardType = value[0];
            Integer needMinNum = value[1];
            Integer needMaxNum = value[2];
            int curAllCount = player.getTreasureHuntXianjiaData().getHuntXianjiaCount() + times;
            boolean isGet = false;
            if (curAllCount >= needMaxNum) {
                isGet = true;
            } else {
                if (curAllCount >= needMinNum && curAllCount <= needMaxNum) {
                    int weight = RandomUtils.random(needMinNum, needMaxNum);
                    if (curAllCount >= weight) {
                        isGet = true;
                    }
                }
            }
            if (isGet) {
                HuntReward huntReward = Manager.treasureHuntManager.deal().createHuntReward(player, rewardType, type,false);
                if (huntReward == null) {
                    logger.error(String.format("寻宝生成必出类型{%s}奖励异常", rewardType));
                    return;
                }
                player.getTreasureHuntXianjiaData().setAlreadyIndex(index);
                addHuntReward(finalItems, huntReward);
                extractCount++;
            }
        }
        int finalItemsSize = finalItems.size();
        //生成寻宝 奖励
        for (int i = 0; i < times - finalItemsSize; i++) {
            HuntReward huntReward = Manager.treasureHuntManager.deal().createHuntReward(player, 1, type,false);
            if (huntReward == null) {
                logger.info("寻宝生成奖励异常");
                return;
            }
            addHuntReward(finalItems, huntReward);
        }
        int pointNum = 0;
        Integer[] integral = pop_bean.getIntegral().getValue();
        if (integral != null && integral.length >= 2) {
            Integer currType = integral[0];
            pointNum = integral[1] * times;
            Manager.backpackManager.manager().addItem(player, currType, pointNum, false, 0, ItemChangeReason.TreasureHuntBuyGet, actionId);
        }

        //统计道具
        Map<Integer, Map<Long, Item>> storeHouse = player.getTreasureHuntXianjiaData().getXianjiaStoreHouse();
        Map<Long, TreasureHuntRecord> recordMap = new HashMap<>();
        List<TreasureHuntRecord> recordList = new ArrayList<>();
        for (HuntReward finalItem : finalItems) {
            long key = finalItem.getUid();
            TreasureHuntRecord record = new TreasureHuntRecord();
            record.setItemId(finalItem.getId());
            record.setPlayerName(player.getName());
            record.setItemNum(finalItem.getNum());
            record.setBind(finalItem.isBind() ? 1 : 0);
            record.setType(finalItem.getType());
            recordList.add(record);
            recordMap.put(key, record);
        }

        //寻宝奖励面板消息
        TreasureHuntXianjiaMessage.ResRewardResultXianjiaPanle.Builder builder = TreasureHuntXianjiaMessage.ResRewardResultXianjiaPanle.newBuilder();
        builder.setType(type);
        builder.setExt1(pointNum);
        builder.setExt2(0);
        TreasureHuntMessage.warehouseInfo.Builder wareHouse = TreasureHuntMessage.warehouseInfo.newBuilder();

        //不是仙魄寻宝
        Manager.treasureHuntManager.deal().treasureHuntRecord(player, recordList, type);
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
            simpleInfo.setUid(reward.getUid());
            simpleInfo.setItemNum(item.getNum());
            builder.addSimpleItems(simpleInfo);
            wareHouse.addSimpleItems(simpleInfo);
            integerItemMap.put(reward.getUid(), item);
        }
        MessageUtils.send_to_player(player, TreasureHuntXianjiaMessage.ResRewardResultXianjiaPanle.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        //个人记录抽奖次数
        int curCount = player.getTreasureHuntXianjiaData().getHuntXianjiaCount();
        player.getTreasureHuntXianjiaData().setHuntXianjiaCount(curCount + times);
        //寻宝结果消息
        TreasureHuntXianjiaMessage.ResTreasureXijiaResult.Builder msg = TreasureHuntXianjiaMessage.ResTreasureXijiaResult.newBuilder();
        wareHouse.setType(type);
        wareHouse.setFreetimes(getLeftFreeTimes(player, type, pop_bean.getFreeTimes()));
        wareHouse.setMustTypeleftTimes(Manager.treasureHuntManager.deal().getLeftMustTypeTimes(player, type, pop_bean));
        wareHouse.setTodayLeftTimes(getTodayLeftTimes(player, type));
        msg.setInfo(wareHouse);
        msg.setDayHuntCount(getTodayUseTime(player, TreasureHuntXianjiaManager.XianjiaHuntType));
        msg.setXijiaHuntCount(player.getTreasureHuntXianjiaData().getHuntXianjiaCount());
        MessageUtils.send_to_player(player, TreasureHuntXianjiaMessage.ResTreasureXijiaResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        Manager.shopManager.limitShop().refresh(player);
        Manager.controlManager.operate(player, FunctionVariable.TreasurePopNum, times);
        Manager.controlManager.operate(player, FunctionVariable.Treasure_XianJia_Num, times);

        //寻宝日志
        Manager.treasureHuntManager.deal().writeTreasureHurtLog(player, recordMap.values(), times, type);
        //BI
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.TreasureXianjia, ItemChangeReason.TreasureHuntXJGet, times);
    }

    private void addHuntReward(List<HuntReward> finalItems, HuntReward huntReward) {
        boolean isAdd = false;
        for (HuntReward reward : finalItems) {
            if (reward.getId() != huntReward.getId())
                continue;
            Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(huntReward.getId());
            if (itemBean != null) {
                if ((reward.getNum() + huntReward.getNum()) > itemBean.getMax()) {
                    finalItems.add(huntReward);
                } else {
                    reward.setNum(reward.getNum() + huntReward.getNum());
                }
                isAdd = true;
            }
            break;
        }
        if (!isAdd)
            finalItems.add(huntReward);
    }

    /**
     * 秘宝
     *
     * @param player
     * @param type
     * @param id
     */
    public void onReqTreasureHuntMibao(Player player, int type, int id) {

        List<Item> items = new ArrayList<>();
        if (type == 1) {
            getMibaoReward_1(player, id, items);
        } else {
            id = getMibaoReward_2(player, items);
        }
        logger.info("寻找bug日志记录 onReqTreasureHuntMibao  roleId  {} type {}  id {} ",player.getId(),type,id);
        if (items.size() <= 0) {
            logger.error("抽奖失败: " + id);
            return;
        }

        //寻宝奖励面板消息
        TreasureHuntXianjiaMessage.ResRewardResultXianjiaPanle.Builder builder = TreasureHuntXianjiaMessage.ResRewardResultXianjiaPanle.newBuilder();
        builder.setType(TreasureHuntXianjiaManager.MibaoHuntType);
        builder.setExt1(type);
        builder.setExt2(0);
        TreasureHuntMessage.warehouseInfo.Builder wareHouse = TreasureHuntMessage.warehouseInfo.newBuilder();


        //统计道具
        Map<Integer, Map<Long, Item>> storeHouse = player.getTreasureHuntXianjiaData().getXianjiaStoreHouse();
        Map<Long, TreasureHuntRecord> recordMap = new HashMap<>();
        List<TreasureHuntRecord> recordList = new ArrayList<>();

        Map<Long, Item> integerItemMap = storeHouse.get(TreasureHuntXianjiaManager.MibaoHuntType);
        if (integerItemMap == null) {
            integerItemMap = new HashMap<>();
            storeHouse.put(TreasureHuntXianjiaManager.MibaoHuntType, integerItemMap);
        }

        String itemStr = "";
        for (Item item : items) {
            long key = item.getId();
            TreasureHuntRecord record = new TreasureHuntRecord();
            record.setItemId(item.getItemModelId());
            record.setPlayerName(player.getName());
            record.setItemNum(item.getNum());
            record.setBind(item.isBind() ? 1 : 0);
            record.setType(TreasureHuntXianjiaManager.MibaoHuntType);
            recordList.add(record);
            recordMap.put(key, record);

            TreasureHuntMessage.simpleItemInfo.Builder simpleInfo = TreasureHuntMessage.simpleItemInfo.newBuilder();
            simpleInfo.setBind(item.isBind() ? 1 : 0);
            simpleInfo.setItemId(item.getItemModelId());
            simpleInfo.setUid(item.getId());
            simpleInfo.setItemNum(item.getNum());
            builder.addSimpleItems(simpleInfo);
            wareHouse.addSimpleItems(simpleInfo);
            integerItemMap.put(key, item);
            itemStr +=item.getItemModelId() + "--";
        }
        logger.info("寻找bug日志记录  秘宝 奖品 itemStr {} roleId {} ", itemStr,player.getId());
        //不是仙魄寻宝
        Manager.treasureHuntManager.deal().treasureHuntRecord(player, recordList, TreasureHuntXianjiaManager.MibaoHuntType);

        MessageUtils.send_to_player(player, TreasureHuntXianjiaMessage.ResRewardResultXianjiaPanle.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        //寻宝结果消息
        TreasureHuntXianjiaMessage.MibaoData.Builder mibaodata = TreasureHuntXianjiaMessage.MibaoData.newBuilder();
        mibaodata.setId(id);
        mibaodata.setIsGet(false);

        TreasureHuntXianjiaMessage.ResTreasureHuntMibaoResult.Builder msg = TreasureHuntXianjiaMessage.ResTreasureHuntMibaoResult.newBuilder();
        wareHouse.setType(TreasureHuntXianjiaManager.MibaoHuntType);
        wareHouse.setFreetimes(getLeftFreeTimes(player, TreasureHuntXianjiaManager.MibaoHuntType, 0));
        wareHouse.setMustTypeleftTimes(0);
        wareHouse.setTodayLeftTimes(getTodayLeftTimes(player, TreasureHuntXianjiaManager.MibaoHuntType));
        msg.setType(type);
        msg.setInfo(wareHouse);
        msg.addReward1(mibaodata.build());
        msg.setMibaoHuntCount(player.getTreasureHuntXianjiaData().getMibaoCount());
        MessageUtils.send_to_player(player, TreasureHuntXianjiaMessage.ResTreasureHuntMibaoResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        //寻宝日志
        Manager.treasureHuntManager.deal().writeTreasureHurtLog(player, recordMap.values(), TreasureHuntXianjiaManager.MibaoHuntType, 1);

        //BI
        if(type == 1){
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.TreasureXianjia, ItemChangeReason.TreasureHuntXJAwardGet, items.get(0).getItemModelId());
        }else{
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.TreasureXianjia, ItemChangeReason.TreasureHuntMibaoGet, items.get(0).getItemModelId());
        }
    }


    private void getMibaoReward_1(Player player, int id, List<Item> items) {
        if (!player.getTreasureHuntXianjiaData().getMibaoreward_1().containsKey(id)) {
            logger.error("秘宝领取道具不在奖池列表 : " + id);
            return;
        }
        if (!player.getTreasureHuntXianjiaData().getMibaoreward_1().get(id)) {
            logger.error("该奖品已经被领取 : " + id);
            return;
        }

        Cfg_Treasure_xianjiaSecret_Bean bean = CfgManager.getCfg_Treasure_xianjiaSecret_Container().getValueByKey(id);
        if (bean == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConfigError_GetByKey, id + "");
            return;
        }
        if (player.getTreasureHuntXianjiaData().getHuntXianjiaCount() < bean.getTime()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.JUSTDOIT_FREECOUNT_OVER);
            logger.error("抽奖次数不足 : " + bean.getTime() + " self " + player.getTreasureHuntXianjiaData().getHuntXianjiaCount());
            return;
        }
        getRewardListItem(player, bean, items);
        if (items.size() > 0)
            player.getTreasureHuntXianjiaData().getMibaoreward_1().put(id, false);
    }


    private int getMibaoReward_2(Player player, List<Item> items) {
        if (getMibaoFreeTimes(player) <= 0) {
            logger.error("免费次数不足");
            return 0;
        }
        ConcurrentHashMap<Integer, Boolean> rewardList = player.getTreasureHuntXianjiaData().getMibaoreward_2();
        if (rewardList.size() <= 0) {
            logger.error("奖励列表为空");
            return 0;
        }
        WeightCalc<Integer> weightCalc = new WeightCalc<>();
        boolean hasReward = false;
        for (int key : rewardList.keySet()) {
            if (rewardList.get(key) == false)
                continue;
            Cfg_Treasure_xianjiaSecret_Bean bean = CfgManager.getCfg_Treasure_xianjiaSecret_Container().getValueByKey(key);
            if (bean == null) {
                logger.error("配置表为空  " + key);
                continue;
            }
            weightCalc.addObject(key, bean.getProbability());
            hasReward = true;
        }
        if (!hasReward) {
            logger.error("奖励已全被领取");
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.JUSTDOIT_FREECOUNT_OVER);
            return 0;
        }
        int id = weightCalc.getObject();
        Cfg_Treasure_xianjiaSecret_Bean bean = CfgManager.getCfg_Treasure_xianjiaSecret_Container().getValueByKey(id);
        if (bean == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConfigError_GetByKey, id + "");
            return 0;
        }
        getRewardListItem(player, bean, items);
        if (items.size() > 0) {
            player.getTreasureHuntXianjiaData().getMibaoreward_2().put(id, false);
            int count = player.getTreasureHuntXianjiaData().getMibaoCount() + 1;
            player.getTreasureHuntXianjiaData().setMibaoCount(count);
            for (Item itm : items) {
                String name = Manager.backpackManager.manager().getName(itm.getItemModelId());
                MessageUtils.notify_allOnlinePlayer(Notify.MARQUEE, MessageString.TreasureHuntRadioMiBao, player.getName(), name);
            }
        }
        return id;
    }

    private void getRewardListItem(Player player, Cfg_Treasure_xianjiaSecret_Bean bean, List<Item> items) {
        int modelID = 0;
        int num = 0;
        int isbind = 0;
        int career = 0;
        for (ReadArray integerArray : bean.getReward().getValuees()) {
            modelID = (int) integerArray.get(0);
            num = (int) integerArray.get(1);
            isbind = (int) integerArray.get(2);
            career = (int) integerArray.get(3);
            Item item = null;
            if (career == 9) { // 9是通用
                item = Item.createItem(modelID, num, isbind == 1);
            } else if (career == player.getCareer()) {
                item = Item.createItem(modelID, num, isbind == 1);
            }
            if (item != null) {
                items.add(item);
            }
        }
    }


    public void onReqBuyCount(Player player, int type, int num, int times) {
        if (type != TreasureHuntXianjiaManager.XianjiaHuntType) {
            logger.error(String.format("仙甲寻宝类型不匹配 {%s}!!!", type));
            return;
        }
        Manager.treasureHuntManager.deal().buy(player, type, num, times);
    }

    public void onReqExtract(Player player, int type, long uid) {
        boolean isSucc = false;
        List<Item> list = new ArrayList<>();
        long actionId = IDConfigUtil.getLogId();
        if (uid > 0) {
            Map<Integer, Map<Long, Item>> storeHouse = player.getTreasureHuntXianjiaData().getXianjiaStoreHouse();
            if (!storeHouse.containsKey(type)) {
                logger.error(String.format("该类型仓库为空 {%s}!!!", type));
                return;
            }
            Map<Long, Item> itemMap = storeHouse.get(type);
            if (!itemMap.containsKey(uid)) {
                logger.error(String.format("道具不存在 {%s}!!!", uid));
                return;
            }
            Item item = itemMap.get(uid);
            int reason = type == TreasureHuntXianjiaManager.XianjiaHuntType ? ItemChangeReason.TreasureHuntXJGet:ItemChangeReason.TreasureHuntMibaoGet;
            boolean success = Manager.backpackManager.manager().addItem(player, item, reason, actionId);
            if (!success) {
                logger.info(String.format("{%s}玩家{%s}寻宝提取类型{%s}失败道具{%s}!!!", player.getName(), player.getId(), type, item.getItemModelId()));
                list.add(item);
            }
            itemMap.remove(uid);
            isSucc = true;

        } else {
            Map<Integer, Map<Long, Item>> storeHouse = player.getTreasureHuntXianjiaData().getXianjiaStoreHouse();
            Iterator<Map.Entry<Integer, Map<Long, Item>>> storeHouseiterator = storeHouse.entrySet().iterator();
            while (storeHouseiterator.hasNext()) {
                Map.Entry<Integer, Map<Long, Item>> housenext = storeHouseiterator.next();
                int reason = housenext.getKey() == TreasureHuntXianjiaManager.XianjiaHuntType ? ItemChangeReason.TreasureHuntXJGet:ItemChangeReason.TreasureHuntMibaoGet;
                Iterator<Map.Entry<Long, Item>> iterator = housenext.getValue().entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Long, Item> next = iterator.next();
                    Item item = next.getValue();
                    boolean success = Manager.backpackManager.manager().addItem(player, item, reason, actionId);
                    if (!success) {
                        logger.info(String.format("{%s}玩家{%s}寻宝提取类型{%s}失败道具{%s}!!!", player.getName(), player.getId(), type, item.getItemModelId()));
                        list.add(item);
                    }
                    //获得仙甲触发 时装条件检测
                    if (item.getItemModelId() >= 5000000 && item.getItemModelId() < 6000000) {
                        Manager.controlManager.operate(player, FunctionVariable.GetEquipId, item.getItemModelId());
                    }
                    iterator.remove();
                    isSucc = true;
                }
            }
        }

        if (list.size() > 0)
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.System, String.valueOf(MessageString.TreasureStoreOnekeyExtract), list,ItemChangeReason.TreasureStoreOnekeyExtractGet);
        if (isSucc) {
            TreasureHuntXianjiaMessage.ResExtractResult.Builder msg = TreasureHuntXianjiaMessage.ResExtractResult.newBuilder();
            msg.setType(type);
            msg.setUid(uid);
            MessageUtils.send_to_player(player, TreasureHuntXianjiaMessage.ResExtractResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    private List<TreasureHuntXianjiaMessage.MibaoData> getMibaoData(ConcurrentHashMap<Integer, Boolean> mibaoreward) {

        List<TreasureHuntXianjiaMessage.MibaoData> mibaoDatas = new ArrayList<>();
        for (int key : mibaoreward.keySet()) {
            boolean value = mibaoreward.get(key);
            TreasureHuntXianjiaMessage.MibaoData.Builder mibaodata = TreasureHuntXianjiaMessage.MibaoData.newBuilder();
            mibaodata.setId(key);
            mibaodata.setIsGet(value);
            mibaoDatas.add(mibaodata.build());
        }
        return mibaoDatas;
    }

    private ArrayList<TreasureHuntMessage.treasureRecordInfo> recordDataToMsg(Map<Integer, List<TreasureHuntRecord>> records) {
        Iterator<Map.Entry<Integer, List<TreasureHuntRecord>>> recordsIterator = records.entrySet().iterator();
        ArrayList<TreasureHuntMessage.treasureRecordInfo> treasureRecordInfos = new ArrayList<>();
        while (recordsIterator.hasNext()) {
            Map.Entry<Integer, List<TreasureHuntRecord>> next = recordsIterator.next();
            Integer type = next.getKey();
            if (type == TreasureHuntXianjiaManager.XianjiaHuntType || type == TreasureHuntXianjiaManager.MibaoHuntType) {
                TreasureHuntMessage.treasureRecordInfo.Builder tBuilder = TreasureHuntMessage.treasureRecordInfo.newBuilder();
                tBuilder.setType(type);
                List<TreasureHuntRecord> list = next.getValue();
                for (TreasureHuntRecord huntRecord : list) {
                    TreasureHuntMessage.itemRecordInfo.Builder builder = TreasureHuntMessage.itemRecordInfo.newBuilder();
                    builder.setItemNum(huntRecord.getItemNum());
                    builder.setType(huntRecord.getType());
                    builder.setPlayername(huntRecord.getPlayerName());
                    builder.setItemId(huntRecord.getItemId());
                    builder.setBind(huntRecord.getBind());
                    tBuilder.addInfo(builder);
                }
                treasureRecordInfos.add(tBuilder.build());
            }
        }
        return treasureRecordInfos;
    }

    public int getTodayLeftTimes(Player player, int type) {
        if (type == TreasureHuntXianjiaManager.MibaoHuntType) {
            return getMibaoFreeTimes(player);
        }
        long count = Manager.countManager.getCount(player, BaseCountType.PTodayTreasureTypeHuntTimes, type);
        return (int) Math.max(0, Global.Xinajia_Hunt_pra.get(0) - count);
    }

    public int getLeftFreeTimes(Player player, int type, int allCount) {
        if (type == TreasureHuntXianjiaManager.MibaoHuntType) {
            return getMibaoFreeTimes(player);
        }
        int playerCount = (int) Manager.countManager.getCount(player, BaseCountType.PTreasureHuntFreeUseTimes, type);
        int leftTimes = allCount - playerCount;
        return leftTimes <= 0 ? 0 : leftTimes;
    }

    public int getTodayUseTime(Player player, int type) {
        long count = Manager.countManager.getCount(player, BaseCountType.PTodayTreasureTypeHuntTimes, type);
        return (int) count;
    }


    private int getMibaoFreeTimes(Player player) {
        int alreadycount = player.getTreasureHuntXianjiaData().getMibaoCount();
        int freeTimes = player.getTreasureHuntXianjiaData().getHuntXianjiaCount() / Global.HuntNumAddSecret;
        freeTimes = freeTimes > Global.SecretHuntLimit ? Global.SecretHuntLimit : freeTimes;
        freeTimes = freeTimes - alreadycount;
        return freeTimes;
    }

    private void initXianjiaData(Player player, int round) {
        //密宝奖池1
        ConcurrentHashMap<Integer, Boolean> mibaoreward_1 = new ConcurrentHashMap<>();
        //密宝奖池2
        ConcurrentHashMap<Integer, Boolean> mibaoreward_2 = new ConcurrentHashMap<>();
        for (Cfg_Treasure_xianjiaSecret_Bean bean : CfgManager.getCfg_Treasure_xianjiaSecret_Container().getValuees()) {
            if (bean.getRound() != round)
                continue;
            if (bean.getType() == 1) {
                mibaoreward_1.put(bean.getId(), true);
            }
            if (bean.getType() == 2) {
                mibaoreward_2.put(bean.getId(), true);
            }
        }
        Manager.countManager.setCount(player, BaseCountType.PTodayTreasureTypeHuntTimes, TreasureHuntXianjiaManager.XianjiaHuntType, Count.RefreshType.CountType_Day, 0);
        Manager.countManager.setCount(player, BaseCountType.PTodayTreasureTypeHuntTimes, TreasureHuntXianjiaManager.MibaoHuntType, Count.RefreshType.CountType_Day, 0);
        player.getTreasureHuntXianjiaData().init(round, mibaoreward_1, mibaoreward_2);
    }

    private long getWaitTime() {
        long periodTime = Global.Xinajia_Hunt_pra.get(1) * 3600 * 1000;
        long openTime = TimeUtils.getBeginTime(TimeUtils.getOpenServerTime());
        long waitTime = 0;
        long nowTime = TimeUtils.Time();
        long intervalTime = nowTime - openTime;
        int intervalDay = (int) (intervalTime / periodTime);
        if (intervalDay > 0) {
            int beyondTime = (int) (intervalTime % periodTime);
            waitTime = (nowTime - beyondTime) + periodTime;
        } else {
            waitTime = openTime + periodTime;
        }
        return waitTime;
    }

    public void loadData() {
        int round = 0;
        int maxRound = Global.Xinajia_Hunt_pra.get(2);
        long nowTime = TimeUtils.Time();
        if (ServerParamUtil.huntXianjiaData.size() == 0) {
            round = 1;
        } else {
            for (Integer roundKey : ServerParamUtil.huntXianjiaData.keySet()) {
                long overTime = ServerParamUtil.huntXianjiaData.get(roundKey);
                if (nowTime > overTime) {
                    round = (roundKey + 1) > maxRound ? 1 : (roundKey + 1);
                } else {
                    round = roundKey;
                }
                break;
            }
        }
        long waitTime = getWaitTime();
        TreasureHuntXianjiaManager.getInstance().setRound(round);
        TreasureHuntXianjiaManager.getInstance().setNextRoundTime(waitTime);
        ServerParamUtil.huntXianjiaData.clear();
        ServerParamUtil.huntXianjiaData.put(round, waitTime);
        ServerParamUtil.savaXianjiaHuntData();
        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);
        int millis = calendar.get(Calendar.MILLISECOND);
        long delay = 60 * 1000 - second * 1000 - millis;
        GameServer.getInstance().getAssistThread().addTimerEvent(new TreasureHuntTimer(delay));
    }


    public void gmClearXianjiaData(Player player) {
        player.getTreasureHuntXianjiaData().setRound(0);
        player.getTreasureHuntXianjiaData().setAlreadyIndex(0);
        player.getTreasureHuntXianjiaData().setMibaoCount(0);
        player.getTreasureHuntXianjiaData().setHuntXianjiaCount(0);
        for (int key : player.getTreasureHuntXianjiaData().getMibaoreward_2().keySet()) {
            player.getTreasureHuntXianjiaData().getMibaoreward_2().put(key, true);
        }
        for (int key : player.getTreasureHuntXianjiaData().getMibaoreward_1().keySet()) {
            player.getTreasureHuntXianjiaData().getMibaoreward_1().put(key, true);
        }
        Manager.countManager.setCount(player, BaseCountType.PTodayTreasureTypeHuntTimes, TreasureHuntXianjiaManager.XianjiaHuntType, Count.RefreshType.CountType_Day, 0);
    }

    public void gmSetMibao(Player player) {
        player.getTreasureHuntXianjiaData().setMibaoCount(0);
        player.getTreasureHuntXianjiaData().setHuntXianjiaCount(1600);

        for (int key : player.getTreasureHuntXianjiaData().getMibaoreward_2().keySet()) {
            player.getTreasureHuntXianjiaData().getMibaoreward_2().put(key, true);
        }
        for (int key : player.getTreasureHuntXianjiaData().getMibaoreward_1().keySet()) {
            player.getTreasureHuntXianjiaData().getMibaoreward_1().put(key, true);
        }
    }


    //修补配置表错误的数据
    private boolean isNeeedReviseXianjiaData(Player player) {
        ConcurrentHashMap<Integer, Boolean> mibaoList = player.getTreasureHuntXianjiaData().getMibaoreward_2();
        for (int key : mibaoList.keySet()) {
            Cfg_Treasure_xianjiaSecret_Bean bean = CfgManager.getCfg_Treasure_xianjiaSecret_Container().getValueByKey(key);
            if (bean == null) {
                return true;
            }
        }
        return false;
    }
}
