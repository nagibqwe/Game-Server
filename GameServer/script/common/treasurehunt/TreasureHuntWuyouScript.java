package common.treasurehunt;

import com.data.*;
import com.data.bean.Cfg_Item_Bean;
import com.data.bean.Cfg_Treasure_Pop_Bean;
import com.data.container.Cfg_Treasure_Pop_Container;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.treasurehunt.struct.HuntReward;
import com.game.treasurehunt.struct.TreasureHuntRecord;
import com.game.treasurehuntwuyou.manager.TreasureHuntWuyouManager;
import com.game.treasurehuntwuyou.script.ITreasureHuntWuyou;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.ServerParamUtil;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.TreasureHuntMessage;
import game.message.TreasureHuntWuyouMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/8/9 14:16
 */
public class TreasureHuntWuyouScript implements ITreasureHuntWuyou {

    private static final Logger log = LogManager.getLogger(TreasureHuntWuyouScript.class);

    @Override
    public int getId() {
        return ScriptEnum.TreasureHuntWuyouScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void online(Player player) {
        try{
            if(!isOpen()){
                return;
            }
            TreasureHuntWuyouMessage.ResAllInfo.Builder msg = TreasureHuntWuyouMessage.ResAllInfo.newBuilder();
            //服务器抽奖记录
            ConcurrentHashMap<Integer, List<TreasureHuntRecord>> serverMap = ServerParamUtil.treasureHuntRecordMap;
            TreasureHuntMessage.treasureRecordInfo allServerRecords = recordDataToMsg(serverMap);
            msg.addAllRecordInfo(allServerRecords);
            //玩家抽奖记录
            Map<Integer, List<TreasureHuntRecord>> records = player.getTreasuryHuntData().getRecords();
            TreasureHuntMessage.treasureRecordInfo playerRecords = recordDataToMsg(records);
            msg.addSelfRecordInfo(playerRecords);
            //仓库信息
            Map<Long, Item> storeHouse = getStore(player);

            TreasureHuntMessage.warehouseInfo.Builder warehouseBuild = TreasureHuntMessage.warehouseInfo.newBuilder();
            warehouseBuild.setFreetimes(0);
            warehouseBuild.setMustTypeleftTimes(0);
            warehouseBuild.setType(TreasureHuntWuyouManager.REWARD_TYPE);
            warehouseBuild.setTodayLeftTimes(0);
            for(Item item : storeHouse.values()){
                TreasureHuntMessage.simpleItemInfo.Builder simpleItemInfo = TreasureHuntMessage.simpleItemInfo.newBuilder();
                simpleItemInfo.setBind(item.isBind() ? 1 : 0);
                simpleItemInfo.setItemId(item.getItemModelId());
                simpleItemInfo.setUid(item.getId());
                simpleItemInfo.setItemNum(item.getNum());
                warehouseBuild.addSimpleItems(simpleItemInfo.build());
            }
            msg.setInfo(warehouseBuild);
            MessageUtils.send_to_player(player, TreasureHuntWuyouMessage.ResAllInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            reqOpenPanel(player);
        }catch (Exception e){
            log.error(e);
        }
    }

    /**
     * 获取仓库信息
     * @param player
     * @return
     */
    private Map<Long, Item> getStore(Player player) {
        Map<Long, Item> store = player.getTreasuryHuntData().getStoreHouse().get(TreasureHuntWuyouManager.HUNT_TYPE);
        if(store == null){
            store = new HashMap<>();
            player.getTreasuryHuntData().getStoreHouse().put(TreasureHuntWuyouManager.HUNT_TYPE,store);
        }
        return store;
    }

    @Override
    public void reqOpenPanel(Player player) {
        if(!isOpen()){
            log.info("无忧宝库 活动已关闭 player:{}", player.getId());
            return;
        }
        TreasureHuntWuyouMessage.ResOpenPanel.Builder res = TreasureHuntWuyouMessage.ResOpenPanel.newBuilder();
        res.setGetItem(checkGetItem(player));
        res.setLastTime(getEndTime() - System.currentTimeMillis());
        if(!res.getGetItem()){
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            res.setGetItemTime(c.getTimeInMillis() - System.currentTimeMillis());
        }
        MessageUtils.send_to_player(player, TreasureHuntWuyouMessage.ResOpenPanel.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    /**
     * 检测是否可领取
     * @param player
     * @return
     */
    private boolean checkGetItem(Player player) {
        String getAwardDate = player.getTreasureHuntWuyouData().getAwardDate();
        if(getAwardDate == null){
            return true;
        }else if(getAwardDate.equals(TreasureHuntWuyouManager.dateFormat.format(new Date()))){
            return false;
        }
        return true;
    }

    private TreasureHuntMessage.treasureRecordInfo recordDataToMsg(Map<Integer, List<TreasureHuntRecord>> records) {
        List<TreasureHuntRecord> rs = records.get(TreasureHuntWuyouManager.HUNT_TYPE);
        TreasureHuntMessage.treasureRecordInfo.Builder tBuilder = TreasureHuntMessage.treasureRecordInfo.newBuilder();
        tBuilder.setType(TreasureHuntWuyouManager.REWARD_TYPE);
        if(rs != null){
            for(TreasureHuntRecord r : rs){
                TreasureHuntMessage.itemRecordInfo.Builder builder = TreasureHuntMessage.itemRecordInfo.newBuilder();
                builder.setItemNum(r.getItemNum());
                builder.setType(r.getType());
                builder.setPlayername(r.getPlayerName());
                builder.setItemId(r.getItemId());
                builder.setBind(r.getBind());
                tBuilder.addInfo(builder);
            }
        }
        return tBuilder.build();
    }

    @Override
    public void reqGetItem(Player player) {
        if(!isOpen()){
            log.info("无忧宝库 活动已关闭 player:{}", player.getId());
            return;
        }
        TreasureHuntWuyouMessage.ResGetItem.Builder res = TreasureHuntWuyouMessage.ResGetItem.newBuilder();
        if(checkGetItem(player)){

            ReadIntegerArray arr = Global.Worry_free_treasure_item;
            Item item = Item.createItem(arr.get(0), arr.get(1), arr.get(2) == 1 ? true : false);
            if(!Manager.backpackManager.manager().onHasAddSpace(player, item)){
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
                return;
            }
            player.getTreasureHuntWuyouData().setAwardDate(TreasureHuntWuyouManager.dateFormat.format(new Date()));
            Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.WuyouHuntFreeGet, IDConfigUtil.getLogId());
            res.setSuccess(true);
            TreasureHuntMessage.simpleItemInfo.Builder simpleItemInfo = TreasureHuntMessage.simpleItemInfo.newBuilder();
            simpleItemInfo.setItemId(item.getItemModelId());
            simpleItemInfo.setBind(item.isBind() ? 1 : 0);
            simpleItemInfo.setItemNum(item.getNum());
            simpleItemInfo.setUid(item.getId());
            res.setSimpleItems(simpleItemInfo);
        }else{
            res.setSuccess(false);
            log.warn("无忧宝库 今日已领取 player:{}", player.getId());
        }
        MessageUtils.send_to_player(player, TreasureHuntWuyouMessage.ResGetItem.MsgID.eMsgID_VALUE, res.build().toByteArray());

        reqOpenPanel(player);
    }

    private boolean isOpen(){
        if(System.currentTimeMillis() < getEndTime()){
            return true;
        }
        return false;
    }

    /**
     * 获取活动结束时间
     * @return
     */
    private long getEndTime(){
        long openServerTime = TimeUtils.getOpenServerTime();
        long activeTime = Global.Worry_free_treasure_time * 24l * 60 * 60 * 1000;
        long activeEndTime = openServerTime + activeTime;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(activeEndTime);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    @Override
    public void reqHunt(Player player, int times) {
        if (!isOpen()) {
            log.info("未达到开启条件!!!");
            return;
        }
        int type = TreasureHuntWuyouManager.HUNT_TYPE;

        Cfg_Treasure_Pop_Bean pop_bean = Cfg_Treasure_Pop_Container.GetInstance().getValueByKey(TreasureHuntWuyouManager.HUNT_TYPE);
        if (pop_bean == null) {
            log.error(String.format("玩家寻宝失败{%s}类型在表Treasure_Pop不存在！！！", TreasureHuntWuyouManager.HUNT_TYPE));
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
            log.error(String.format("玩家寻宝失败{%s}类型在表Treasure_Pop价格异常{%s}不存在！！！", type, times));
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        if (price > 0) {
            boolean costSuccess = Manager.backpackManager.manager().onRemoveItem(player, itemId, price, ItemChangeReason.WuyouHuntCost, actionId);
            if (!costSuccess) {
                int hasNum = Manager.backpackManager.manager().getItemNum(player, itemId);
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
                log.error("寻宝--扣除道具失败{},hasNum {}, needCostItemnum {}  ", itemId, hasNum, price);
                return;
            }
        }

        List<HuntReward> finalItems = new ArrayList<>();
        //有几率会出
        int index = 0;
        int extractCount = 0;
        for (ReadArray<Integer> guarantees_rewardType : pop_bean.getGuarantees_reward().getValuees()) {
            int alreadIndex = player.getTreasureHuntWuyouData().getAlreadyIndex();
            index++;
            if (alreadIndex >= index)
                continue;
            if (extractCount >= times)
                break;
            Integer[] value = guarantees_rewardType.getValue();
            Integer rewardType = value[0];
            Integer needMinNum = value[1];
            Integer needMaxNum = value[2];
            int curAllCount = player.getTreasureHuntWuyouData().getAwardCount() + times;
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
                    log.error(String.format("寻宝生成必出类型{%s}奖励异常", rewardType));
                    return;
                }
                player.getTreasureHuntWuyouData().setAlreadyIndex(index);
                addHuntReward(finalItems, huntReward);
                extractCount++;
            }
        }
        int finalItemsSize = finalItems.size();
        //生成寻宝 奖励
        for (int i = 0; i < times - finalItemsSize; i++) {
            HuntReward huntReward = Manager.treasureHuntManager.deal().createHuntReward(player, 1, type,false);
            if (huntReward == null) {
                log.info("寻宝生成奖励异常");
                return;
            }
            addHuntReward(finalItems, huntReward);
        }

        //保存抽奖记录
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
        Manager.treasureHuntManager.deal().treasureHuntRecord(player, recordList, TreasureHuntWuyouManager.REWARD_TYPE);

        //寻宝结果消息
        TreasureHuntWuyouMessage.ResHuntResult.Builder msg = TreasureHuntWuyouMessage.ResHuntResult.newBuilder();
        //保存到仓库
        Map<Long, Item> stores = getStore(player);
        for (HuntReward reward : finalItems) {
            Item item = Item.createItem(reward.getId(), reward.getNum(), reward.isBind());
            item.setId(reward.getUid());
            TreasureHuntMessage.simpleItemInfo.Builder simpleInfo = TreasureHuntMessage.simpleItemInfo.newBuilder();
            simpleInfo.setBind(item.isBind() ? 1 : 0);
            simpleInfo.setItemId(item.getItemModelId());
            simpleInfo.setUid(reward.getUid());
            simpleInfo.setItemNum(item.getNum());
            msg.addSimpleItems(simpleInfo);
            stores.put(reward.getUid(), item);
        }

        //个人记录抽奖次数
        int curCount = player.getTreasureHuntWuyouData().getAwardCount();
        player.getTreasureHuntWuyouData().setAwardCount(curCount + times);

        MessageUtils.send_to_player(player, TreasureHuntWuyouMessage.ResHuntResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        //寻宝日志
        Manager.treasureHuntManager.deal().writeTreasureHurtLog(player, recordMap.values(), times, type);
        //BI
//        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.TreasureXianjia, ItemChangeReason.TreasureHuntXJGet, times);
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

    @Override
    public void reqExtract(Player player, long uid) {
        boolean isSucc = false;
        List<Item> list = new ArrayList<>();
        long actionId = IDConfigUtil.getLogId();
        Map<Long, Item> items = getStore(player);
        if (uid > 0) {
            if (!items.containsKey(uid)) {
                log.error(String.format("道具不存在 {%s}!!!", uid));
                return;
            }
            Item item = items.get(uid);
            boolean success = Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.WuyouHuntExtractGet, actionId);
            if (!success) {
                log.info(String.format("{%s}玩家{%s}寻宝提取类型{%s}失败道具{%s}!!!", player.getName(), player.getId(), TreasureHuntWuyouManager.HUNT_TYPE, item.getItemModelId()));
                list.add(item);
            }
            items.remove(uid);
            isSucc = true;

        } else {
            Iterator<Map.Entry<Long, Item>> iterator =  items.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<Long, Item> entry = iterator.next();
                Item item = entry.getValue();
                boolean success = Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.WuyouHuntExtractGet, actionId);
                if (!success) {
                    log.info(String.format("{%s}玩家{%s}寻宝提取类型{%s}失败道具{%s}!!!", player.getName(), player.getId(), TreasureHuntWuyouManager.HUNT_TYPE, item.getItemModelId()));
                    list.add(item);
                }
                iterator.remove();
                isSucc = true;
            }
        }

        if (list.size() > 0)
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.System, String.valueOf(MessageString.WUYOU_STORE_ONEKEY_EXTRACT), list,ItemChangeReason.WuyouHuntExtractGet);
        if (isSucc) {
            TreasureHuntWuyouMessage.ResExtractResult.Builder msg = TreasureHuntWuyouMessage.ResExtractResult.newBuilder();
            msg.setUid(uid);
            MessageUtils.send_to_player(player, TreasureHuntWuyouMessage.ResExtractResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }
}
