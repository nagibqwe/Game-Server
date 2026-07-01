package common.cangbaoge;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Recharge_daily_cangzhenge_Bean;
import com.data.bean.Cfg_Recharge_daily_duibaodian_Bean;
import com.data.bean.Cfg_Recharge_daily_superreward_Bean;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.cangbaoge.manager.CangbaogeManager;
import com.game.cangbaoge.scripts.ICangbaogeScript;
import com.game.cangbaoge.struct.CangbaogeRecord;
import com.game.cangbaoge.struct.SuperrewardData;
import com.game.cangbaoge.timer.CangbaogeTimer;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.structs.GlobalType;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.core.util.WeightCalc;
import game.message.CangbaogeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cxl on 2020/9/2.
 */
public class CangbaogeScript implements ICangbaogeScript {


    private static final Logger logger = LogManager.getLogger(CangbaogeScript.class);

    private int max_record = 30;

    @Override
    public int getId() {
        return ScriptEnum.CangbaogeScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }


    public void tick()
    {
        long nowTime  =  TimeUtils.Time();
        long waitTime = Manager.cangbaogeManager.getNextRoundTime();
        if (nowTime < waitTime){
            return;
        }
        int maxRound = Global.CangZhenGe_Refresh_time.get(1);
        int curRound =  Manager.cangbaogeManager.getRound();
        int nextRound = (curRound+1)> maxRound ? maxRound:(curRound+1);
        waitTime = getWaitTime();
        Manager.cangbaogeManager.setRound(nextRound);
        Manager.cangbaogeManager.setNextRoundTime(waitTime);
        ServerParamUtil.cangbaogeRoundData.clear();
        ServerParamUtil.cangbaogeRoundData.put(nextRound,waitTime);
        ServerParamUtil.savaCangbaogeRoundData();

        ServerParamUtil.cangbaogeExchangeData.clear();
        ServerParamUtil.savaCangbaogeRoundData();

    }

    public void loadData(){
        int round = 0;
        int  maxRound = Global.CangZhenGe_Refresh_time.get(1);
        long nowTime =  TimeUtils.Time();
        if (ServerParamUtil.cangbaogeRoundData.size() == 0 ) {
            round = 1;
        }else {
            for (Integer roundKey : ServerParamUtil.cangbaogeRoundData.keySet()){
                long overTime =   ServerParamUtil.cangbaogeRoundData.get(roundKey);
                if (nowTime > overTime){
                    round =  roundKey == maxRound ? maxRound:roundKey+1;
                    ServerParamUtil.cangbaogeExchangeData.clear();
                    ServerParamUtil.savaCangbaogeRoundData();
                }
                else {
                    round = roundKey;
                }
                break;
            }
        }

        long waitTime  = getWaitTime();
        CangbaogeManager.getInstance().setRound(round);
        CangbaogeManager.getInstance().setNextRoundTime(waitTime);
        ServerParamUtil.cangbaogeRoundData.clear();
        ServerParamUtil.cangbaogeRoundData.put(round,waitTime);
        ServerParamUtil.savaCangbaogeRoundData();
        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);
        int millis = calendar.get(Calendar.MILLISECOND);
        long delay = 60 * 1000 - second * 1000 - millis;
        GameServer.getInstance().getAssistThread().addTimerEvent(new CangbaogeTimer(delay));
    }

    private long getWaitTime(){

        long day = Global.CangZhenGe_Refresh_time.get(0);
        long periodTime = day * 24 * 3600 * 1000;
        long openTime = TimeUtils.getBeginTime(TimeUtils.getOpenServerTime());
        long waitTime = 0;
        long nowTime =  TimeUtils.Time();
        long  intervalTime =  nowTime  - openTime;
        int intervalDay = (int) (intervalTime / periodTime);
        if (intervalDay >0){
            int beyondTime = (int) (intervalTime % periodTime);
            waitTime =  (nowTime - beyondTime) + periodTime;
        }else {
            waitTime = openTime + periodTime;
        }
        return waitTime;
    }


    public void playerOnline(Player player){
        sendOpenPanelInfo(player);
    }


    @Override
    public void ReqOpenCangbaogePanel(Player player) {
        sendOpenPanelInfo(player);
    }


    private void sendOpenPanelInfo(Player player){
        CangbaogeMessage.ResOpenCangbaogePanel.Builder msg = CangbaogeMessage.ResOpenCangbaogePanel.newBuilder();
        msg.setLotteryTimes(player.getSuperrewardData().getLotteryTimes());
        msg.setCurSuperRound(player.getSuperrewardData().getRound());
        msg.addAllAlreadyGetID( player.getSuperrewardData().getAlreadyGetID());
        MessageUtils.send_to_player(player, CangbaogeMessage.ResOpenCangbaogePanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    public void gmSetLotteryTimes(Player player){
        player.getSuperrewardData().setLotteryTimes(250);
        sendOpenPanelInfo(player);
    }


    @Override
    public void ReqOpenRecordPanel(Player player) {
        List<CangbaogeRecord> records =  ServerParamUtil.cangbaogeRecords;
        CangbaogeMessage.ResOpenRecordPanel.Builder msg = CangbaogeMessage.ResOpenRecordPanel.newBuilder();
        for (CangbaogeRecord record :records){
            msg.addRecordList(buildRecord(record));
        }
        MessageUtils.send_to_player(player,CangbaogeMessage.ResOpenRecordPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private  CangbaogeMessage.CangBaogeRecord buildRecord( CangbaogeRecord record){
        CangbaogeMessage.CangBaogeRecord.Builder builder = CangbaogeMessage.CangBaogeRecord.newBuilder();
        builder.setItemId(record.getItemId());
        builder.setNum(record.getNum());
        builder.setPlayerName(record.getPlayerName());
        builder.setTime(record.getTime());
        return builder.build();
    }



    @Override
    public void ReqCangbaogeLottery(Player player) {

        int needItemID =   Global.CangZhenGe_need_item.get(0);
        int needNum =  Global.CangZhenGe_need_item.get(1);

        long actionId = IDConfigUtil.getLogId();
        boolean costSuccess = Manager.backpackManager.manager().onRemoveItem(player, needItemID, needNum, ItemChangeReason.CangbaogeLotteryDel, actionId);
        if (!costSuccess){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
            return;
        }
        WeightCalc<Integer> weightCalc = new WeightCalc<>();
        int curRound =  Manager.cangbaogeManager.getRound();
        boolean isIn = false;
        for (Cfg_Recharge_daily_cangzhenge_Bean bean :  CfgManager.getCfg_Recharge_daily_cangzhenge_Container().getValuees()){
            if (bean.getTimes() == curRound ){
                weightCalc.addObject(bean.getID(),bean.getProbability());
                isIn = true;
            }
        }
        if (!isIn){
            logger.error("round is not find"   + curRound);
            return;
        }

        int exId =  weightCalc.getObjectAndRemove();

        Cfg_Recharge_daily_cangzhenge_Bean rewardBean =  CfgManager.getCfg_Recharge_daily_cangzhenge_Container().getValueByKey(exId);
        if (rewardBean == null){
            logger.error("Cfg_Recharge_daily_cangzhenge_Bean null"   + exId);
            return;
        }
        List<Item> items = Item.createItems(player.getCareer(), rewardBean.getReward(), 1);
        if (! Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CangbaogeLotteryGet, actionId)){
            Manager.mailManager.sendMailToPlayer(player.getId(),
                    MessageString.System, MessageString.System, MessageString.System, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.CangbaogeLotteryGet);
        }


        //记录
        List<CangbaogeRecord> records =  ServerParamUtil.cangbaogeRecords;
        if (records.size()>= max_record){
            records.remove(0);
        }
        CangbaogeRecord cangbaogeRecord = new CangbaogeRecord();
        cangbaogeRecord.setTime(TimeUtils.Time());
        cangbaogeRecord.setPlayerName(player.getName());
        cangbaogeRecord.setItemId(items.get(0).getItemModelId());
        cangbaogeRecord.setNum(items.get(0).getNum());
        records.add(cangbaogeRecord);

        int lotteryTimes =   player.getSuperrewardData().getLotteryTimes();
        player.getSuperrewardData().setLotteryTimes(lotteryTimes + 1);

        CangbaogeMessage.ResCangbaogeLottery.Builder msg =  CangbaogeMessage.ResCangbaogeLottery.newBuilder();
        msg.setLotteryTimes(lotteryTimes + 1);
        msg.setLotteryID(exId);
        MessageUtils.send_to_player(player, CangbaogeMessage.ResCangbaogeLottery.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        ServerParamUtil.saveCangbaogeRecord();
        //记录bi数据
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.EVERYDAY_CANGBAOGE, ItemChangeReason.CangbaogeLotteryGet, rewardBean.getID());
    }

    @Override
    public void ReqCangbaogeReward(Player player, int id) {
        SuperrewardData superrewardData =  player.getSuperrewardData();

        if (superrewardData.getAlreadyGetID().contains(id)){
            logger.error("已领取"   + id);
            return;
        }

        Cfg_Recharge_daily_superreward_Bean bean = CfgManager.getCfg_Recharge_daily_superreward_Container().getValueByKey(id);
        if (bean == null){
            logger.error("Cfg_Recharge_daily_superreward_Bean null "   + id);
            return;
        }
        if (bean.getTimes()  != superrewardData.getRound()){
            logger.error("轮次不匹配 Round  "  + superrewardData.getRound() + " bean.getTimes()  " + bean.getTimes());
            return;
        }
        if (bean.getNeed() > superrewardData.getLotteryTimes()){
            logger.error("抽奖次数不够  "   + bean.getTimes());
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        List<Item> items = Item.createItems(player.getCareer(), bean.getReward(), 1);
        if (! Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CangbaogeRewardGet, actionId)){
            Manager.mailManager.sendMailToPlayer(player.getId(),
                    MessageString.System, MessageString.System, MessageString.System, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.CangbaogeRewardGet);
        }

        superrewardData.getAlreadyGetID().add(id);
        int maxRound = Global.Superreward_Refresh_time.get(1);
        int curRound = superrewardData.getRound();
        if (bean.getNeed() ==  Global.Superreward_Refresh_time.get(0)){
            sendRoundReward( player, superrewardData.getAlreadyGetID(),curRound);
            superrewardData.setLotteryTimes(superrewardData.getLotteryTimes()-bean.getNeed());
            curRound =   curRound>= maxRound?maxRound : curRound+1;
            superrewardData.setRound(curRound);
            superrewardData.getAlreadyGetID().clear();
        }
        CangbaogeMessage.ResCangbaogeReward.Builder msg =  CangbaogeMessage.ResCangbaogeReward.newBuilder();
        msg.setCurSuperRound(curRound);
        msg.setLotteryTimes(superrewardData.getLotteryTimes());
        msg.addAllAlreadyGetID(superrewardData.getAlreadyGetID());
        MessageUtils.send_to_player(player, CangbaogeMessage.ResCangbaogeReward.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        //记录bi数据
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.EVERYDAY_CANGBAOGE, ItemChangeReason.CangbaogeRewardGet, bean.getID());
    }

    private void sendRoundReward(Player player, List<Integer> alreadyGetID,int round){

        long actionId = IDConfigUtil.getLogId();
        List<Item> items = new ArrayList<>();
        for (Cfg_Recharge_daily_superreward_Bean bean :
                CfgManager.getCfg_Recharge_daily_superreward_Container().getValuees()){
            if (bean.getTimes() == round){
                if (!alreadyGetID.contains(bean.getID())){
                     items.addAll(Item.createItems(player.getCareer(), bean.getReward(), 1));
                }
            }
        }
        if (items.size()>0) {
            if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CangbaogeRewardGet, actionId)) {
                Manager.mailManager.sendMailToPlayer(player.getId(),
                        MessageString.System, MessageString.System, MessageString.System, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.CangbaogeRewardGet);
            }
        }
    }


    public  void ReqOpenCangbaogeExchange(Player player){

        HashMap<Integer,Integer> exchangeDataMap = new HashMap<>();
        ConcurrentHashMap<Long, HashMap<Integer,Integer>> cangbaogeExchangeData  =  ServerParamUtil.cangbaogeExchangeData;
        if (cangbaogeExchangeData.containsKey(player.getId())){
            exchangeDataMap = cangbaogeExchangeData.get(player.getId());
        }
        CangbaogeMessage.ResOpenCangbaogeExchange.Builder msg =  CangbaogeMessage.ResOpenCangbaogeExchange.newBuilder();
        CangbaogeMessage.CangbaogeExchangeData.Builder builder;
        for (Map.Entry<Integer,Integer> entry:exchangeDataMap.entrySet()){
            builder =  CangbaogeMessage.CangbaogeExchangeData.newBuilder();
            builder.setExchangeID(entry.getKey());
            builder.setExchangeNum(entry.getValue());
            msg.addExchangeMaps(builder.build());
        }
        MessageUtils.send_to_player(player,  CangbaogeMessage.ResOpenCangbaogeExchange.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }


    @Override
    public void ReqCangbaogeExchange(Player player, int exchangeId) {

        Cfg_Recharge_daily_duibaodian_Bean bean = CfgManager.getCfg_Recharge_daily_duibaodian_Container().getValueByKey(exchangeId);
        if (bean == null){
            logger.error("Cfg_Recharge_daily_duibaodian_Bean is null  "  + exchangeId);
            return;
        }
        int round =  Manager.cangbaogeManager.getRound();
        if (bean.getTimes() != round){
            logger.error("兑换的物品轮数不匹配 round  "  + round +
                    " bean.round " +bean.getTimes() + " exchangeId " +exchangeId);
            return;
        }
        ConcurrentHashMap<Long, HashMap<Integer,Integer>> cangbaogeExchangeData  =  ServerParamUtil.cangbaogeExchangeData;
        HashMap<Integer,Integer> exchangeDataMap  = cangbaogeExchangeData.get(player.getId());
        if (exchangeDataMap == null){
            exchangeDataMap = new HashMap<>();
            cangbaogeExchangeData.put(player.getId(),exchangeDataMap);
        }
        exchangeDataMap.putIfAbsent(exchangeId,0);
        int exchangeNum = exchangeDataMap.get(exchangeId);
        if (exchangeNum >= bean.getExchange_limit()){
            logger.error("兑换次数已达到上限 "  +  exchangeNum);
            return;
        }

        long actionId = IDConfigUtil.getLogId();
        boolean costSuccess = Manager.backpackManager.manager().onRemoveItem(player,  bean.getNeed().get(0),
                bean.getNeed().get(1), ItemChangeReason. CangbaogeExchangeDel, actionId);
        if (!costSuccess){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
        }
        List<Item> items = Item.createItems(player.getCareer(), bean.getReward(), 1);
        if (! Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CangbaogeExchangeGet, actionId)){
            Manager.mailManager.sendMailToPlayer(player.getId(),
                    MessageString.System, MessageString.System, MessageString.System, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.CangbaogeExchangeGet);
        }

        exchangeNum =exchangeNum+1;
        exchangeDataMap.put(exchangeId,exchangeNum);

        CangbaogeMessage.ResCangbaogeExchange.Builder msg =  CangbaogeMessage.ResCangbaogeExchange.newBuilder();
        CangbaogeMessage.CangbaogeExchangeData.Builder builder =  CangbaogeMessage.CangbaogeExchangeData.newBuilder();
        builder.setExchangeNum(exchangeNum);
        builder.setExchangeID(exchangeId);
        msg.setExchangeData(builder.build());
        MessageUtils.send_to_player(player, CangbaogeMessage.ResCangbaogeExchange.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        cangbaogeExchangeData.put(player.getId(),exchangeDataMap);
        ServerParamUtil.saveCangbaogeExchangeData();
        //记录bi数据
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.EVERYDAY_DUIBAODIAN, ItemChangeReason.CangbaogeExchangeGet, bean.getID());
    }
}
