package common.activity;

import com.data.ItemChangeReason;
import com.data.MessageString;

import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityType;
import com.game.activity.struct.RewardData;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.json.TypeReference;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * 累计充值 300006
 * Created by cxl on 2020/8/18.
 */
public class RechargeTotalActivityScript implements IActivityScript {
    private static final Logger LOGGER = LogManager.getLogger(RechargeTotalActivityScript.class);
    private final static String RechargeTotalData = "RechargeTotalData";
    private final static String RechargeNum  = "rechargeNum";
    private final static String AlreadyGet   = "alreadyGet";
    private final static String CustomReward = "customReward";

    public void reload(){
        if(Manager.activityManager == null){
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.LimitedTotalRecharge);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(RechargeTotalData);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            RechargeTotalData newData = JsonUtils.toJavaObject(customStr, RechargeTotalData.class);
            activityConfig.getCustomCfgMap().put(RechargeTotalData, newData);
        }
    }

    @Override
    public int getId() {
        return ScriptEnum.RechargeTotalActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }
    /**
     * 解析活动自己的自定义配置
     */
   public boolean parseCustomConfig(ActivityConfig actCfg, String customStr){

       RechargeTotalData data = JsonUtils.toJavaObject(customStr, RechargeTotalData.class);
       actCfg.getCustomCfgMap().put(RechargeTotalData,data);
       actCfg.getCustomCfgMap().put("client",data.getClient());
       return true;
   }

    /**
     * 活动配置更新处理
     */
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr){
        parseCustomConfig( actCfg,  customStr);
        return true;
    }

    /**
     * 生成活动数据字符串
     */
    @Override
    public String getActivityDataStr(ActivityConfig actCfg, long roleId){

        int actType = actCfg.getType();
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId, actType);
        roleActDataMap.putIfAbsent(RechargeNum,0);
        roleActDataMap.putIfAbsent(AlreadyGet,new ArrayList<Integer>());
        roleActDataMap.putIfAbsent(CustomReward,new HashMap<Integer,HashMap<Integer,Integer>>());
        List<Integer> alreadyGetList = (List<Integer>)roleActDataMap.get(AlreadyGet);
        long allRecharNum = Long.parseLong(roleActDataMap.get(RechargeNum).toString()) + Manager.activityManager.getRoleTimeRecharge(roleId,actCfg);
        HashMap<Integer,HashMap<Integer,Integer>> customRewardTemp =  (HashMap<Integer,HashMap<Integer,Integer>>) roleActDataMap.get(CustomReward);
        HashMap<String,Object> rechargeData = new HashMap<>();
        rechargeData.put(RechargeNum,allRecharNum);
        rechargeData.put(AlreadyGet,alreadyGetList);
        HashMap<String,HashMap<String,Integer>> customReward = new HashMap<>();
        for (Map.Entry<Integer,HashMap<Integer,Integer>> entry: customRewardTemp.entrySet()){
            HashMap<String,Integer> itemList = new HashMap<>();
            customReward.put(entry.getKey()+"",itemList);
            for (Map.Entry<Integer,Integer> entry1: entry.getValue().entrySet()){
                        itemList.put(entry1.getKey()+"",entry1.getValue());
            }
        }
        rechargeData.put(CustomReward,customReward);
        return JsonUtils.toJSONString(rechargeData);
    }

    /**
     * 充值后的处理
     * @param player
     */
    public void rechargeDeal(Player player, int getGoodsCfgId,int rechargeNum, ActivityConfig actCfg){


        if (actCfg == null){
            return;
        }
        int actType = actCfg.getType();
        int startDay =  TimeUtils.getCurDayByTime(actCfg.getBeginTime());
        int curDay   =  TimeUtils.getCurDayByTime(TimeUtils.Time());
        int overDay  =  TimeUtils.getCurDayByTime(actCfg.getEndTime());

        if (curDay < startDay || curDay > overDay){
            LOGGER.error("活动时间已结束"  + actType);
            return;
        }

        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actType);
        roleActDataMap.putIfAbsent(RechargeNum,0);
        long allRecharNum = Long.parseLong(roleActDataMap.get(RechargeNum).toString());
        allRecharNum +=rechargeNum;
        roleActDataMap.put(RechargeNum , allRecharNum);


        //保存角色活动数据
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        Manager.activityManager.deal().onReqActivity(player,actType);

    }
    /**
     * 玩家上线处理
     */
    public void playerOnline(Player player, ActivityConfig actCfg){
        testConfig();
    }

    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg){
        HashMap<String, Object>  rechargeMap =  JsonUtils.parseObject(dataStr, new TypeReference<HashMap<String, Object>>(){});

        if (actCfg == null){
            return;
        }
        int actType = actCfg.getType();
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actType);
        roleActDataMap.putIfAbsent(RechargeNum,0);
        roleActDataMap.putIfAbsent(CustomReward,new  HashMap<Integer,HashMap<Integer,Integer>>());
        HashMap<Integer,HashMap<Integer,Integer>> customRewardMap = (HashMap<Integer,HashMap<Integer,Integer>>) roleActDataMap.get(CustomReward);


        RechargeTotalData rechargeTotalData =  (RechargeTotalData)actCfg.getCustomCfgMap().get(RechargeTotalData);
        //领奖处理
        if (checkIsGetReward(rechargeMap)){
            getRechargeReward( player,  rechargeMap,roleActDataMap, rechargeTotalData, actType,actCfg);
            //设置自定义奖励
        }else if (checkIsSetCustomReward(rechargeMap)){
            setCustomReward( player,  rechargeMap, rechargeTotalData, customRewardMap, actType);
        }
    }

    private void setCustomReward(Player player,HashMap<String, Object>  rechargeMap,
                                    RechargeTotalData rechargeTotalData,
                                    HashMap<Integer,HashMap<Integer,Integer>> customRewardMap,
                                    int actType){
        int  rechargeLevel =JsonUtils.parseObject(rechargeMap.get(CustomReward).toString() , Integer.class);

        //HashMap<Integer,Integer> itemlist =  (HashMap<Integer,Integer>)rechargeMap.get("itemList");
        int index =  (Integer)rechargeMap.get("index");
        int value =  (Integer)rechargeMap.get("value");
        if (!rechargeTotalData.getRechargeTargetMap().containsKey(rechargeLevel)){
            LOGGER.info("not find this level recharge "  + rechargeLevel);
            return;
        }
        RechargeTarget rechargeTarget = rechargeTotalData.getRechargeTargetMap().get(rechargeLevel);
        if (index+1>rechargeTarget.getCustomlen()){
            LOGGER.info("设置自定义索引超标   index "  + index  + " Customlen "  +rechargeTarget.getCustomlen());
            return;
        }
        if (!isContainsKey(rechargeTarget.getCustomRewardMap(),value)){
            LOGGER.info("设置的自定义奖励和配置不匹配    "  + value);
            return;
        }
        if (!Manager.activityManager.checkCareer(player, getRewardData(rechargeTarget.getCustomRewardMap(),value).getC())){
            LOGGER.info("设置自定义奖励与职业不匹配   id  "  + value  +" player.getCareer() " + player.getCareer());
            return;
        }
        if (!customRewardMap.containsKey(rechargeLevel)){
            HashMap<Integer,Integer> itemlist   = new HashMap<>();
            itemlist.put(index,value);
            customRewardMap.put(rechargeLevel,itemlist);
        }else {
            customRewardMap.get(rechargeLevel).put(index,value);
        }
        Manager.activityManager.deal().onReqActivity(player,actType);
        //保存角色活动数据
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

    }

    private void getRechargeReward(Player player, HashMap<String, Object>  rechargeMap,
                                  ConcurrentHashMap<String, Object> roleActDataMap,
                                   RechargeTotalData rechargeTotalData,
                                   int actType,ActivityConfig actCfg){

        long allRecharNum = Long.parseLong(roleActDataMap.get(RechargeNum).toString()) + Manager.activityManager.getRoleTimeRecharge(player.getId(),actCfg);
        List<Integer> alreadyGetList = (List<Integer>) roleActDataMap.get(AlreadyGet);
        HashMap<Integer,HashMap<Integer,Integer>> customRewardMap =
                (HashMap<Integer,HashMap<Integer,Integer>>) roleActDataMap.get(CustomReward);
        int rechargeLevel = (int)rechargeMap.get("getReward");
        if (!rechargeTotalData.getRechargeTargetMap().containsKey(rechargeLevel)){
            LOGGER.error("not find this level recharge "  + rechargeLevel);
            return;
        }
        RechargeTarget rechargeTarget = rechargeTotalData.getRechargeTargetMap().get(rechargeLevel);
        if (alreadyGetList.contains(rechargeLevel)){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GUILD_ACTIVITY_BOSS_HASREWARD);
            return;
        }
        if (allRecharNum < rechargeTarget.getTargetRcharge()){
            LOGGER.info("充值金额不足  "  + allRecharNum);
            return;
        }
        HashMap<Integer,Integer>coustomList = new HashMap<>();
        if (rechargeTarget.getCustomlen() >0){
            if (!customRewardMap.containsKey(rechargeLevel)){
                LOGGER.info("自定义奖励没有设置   "  + rechargeLevel);
                return;
            }
            coustomList =  customRewardMap.get(rechargeLevel);
            if (coustomList.size() < rechargeTarget.getCustomlen()){
                LOGGER.info("自定义奖励没有设置完成还差   "  + (rechargeTarget.getCustomlen() - coustomList.size()));
                return;
            }
            for (Integer id : coustomList.values()){
                if (!isContainsKey(rechargeTarget.getCustomRewardMap(),id)){
                    LOGGER.info("领取的自定义奖励和配置不匹配    "  + id);
                    return;
                }
            }
        }
        List<Item> itemList = getRechageReward( player, rechargeTarget.getFixedRewardMap());
        for (Integer itemID:coustomList.values()){
            RewardData rewardData = getRewardData(rechargeTarget.getCustomRewardMap(),itemID) ;
            if (Manager.activityManager.checkCareer(player, rewardData.getC())){
                Item item =  Item.createItem(rewardData.getI(),rewardData.getN(),rewardData.getB() > 0);
                if (item == null){
                    continue;
                }
                itemList.add(item);
            }
        }
        //累积充值 原因码



        if ( !Manager.backpackManager.manager().addItems(player,itemList, ItemChangeReason.RechargeTotalActivityGet, IDConfigUtil.getLogId())){
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, itemList, ItemChangeReason.RechargeTotalActivityGet);
        }
        alreadyGetList.add(rechargeLevel);
        roleActDataMap.put(AlreadyGet,alreadyGetList);
        Manager.activityManager.deal().onReqActivity(player,actType);

        //保存角色活动数据
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        //记录BI
//            Manager.biManager.getScript().biActivity(player, ItemChangeReason.RechargeTotalActivityGet, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.RechargeTotal, ItemChangeReason.RechargeTotalActivityGet, rechargeLevel);
    }

    private boolean checkIsGetReward( HashMap<String, Object> rechargeMap){
        if (rechargeMap.get("getReward") == null){
            return false;
        }
        return true;
    }

    private boolean checkIsSetCustomReward(HashMap<String, Object> rechargeMap){
        if (rechargeMap.get(CustomReward) == null){
            return false;
        }
        if (rechargeMap.get("index") == null){
            return false;
        }
        if (rechargeMap.get("value") == null){
            return false;
        }
        return true;
    }



    public void zeroClockPlayerDeal(Player player, ActivityConfig actCfg){



    }

    @Override
    public void fiveClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    public void activityEndDeal(ActivityConfig actCfg){
        if (actCfg == null){
            return;
        }
        int actType =  actCfg.getType();
        if (!actCfg.getCustomCfgMap().containsKey(RechargeTotalData)){
            return;
        }
        RechargeTotalData rechargeTotalData =  (RechargeTotalData)actCfg.getCustomCfgMap().get(RechargeTotalData);
        if (rechargeTotalData == null){
            LOGGER.error("rechargeTotalData is null ");
            return;
        }
        for (Map.Entry<Long, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>>> entry: Manager.activityManager.getRoleActDatas().entrySet()) {
            ConcurrentHashMap<String, Object> roleActDataMap = entry.getValue().get(actType);
            if(roleActDataMap == null){
                continue;
            }
            roleActDataMap.putIfAbsent(AlreadyGet,new ArrayList<Integer>());
            List<Integer> alreadyGetList = (List<Integer>) roleActDataMap.get(AlreadyGet);
            roleActDataMap.putIfAbsent(RechargeNum,0);
            long allRecharNum = Long.parseLong(roleActDataMap.get(RechargeNum).toString()) + Manager.activityManager.getRoleTimeRecharge(entry.getKey(),actCfg);
            roleActDataMap.put(CustomReward,new HashMap<Integer, HashMap<Integer, Integer>>());
            HashMap<Integer, HashMap<Integer, Integer>> customReward = (HashMap<Integer, HashMap<Integer, Integer>>) roleActDataMap.get(CustomReward);

            Player player = Manager.playerManager.getPlayer(entry.getKey());
            if (player == null){
                LOGGER.error("player  == null"  + entry.getKey());
                continue;
            }
            for (Integer rechargeLevel : rechargeTotalData.getRechargeTargetMap().keySet()) {
                if (alreadyGetList.contains(rechargeLevel)) {
                    continue;
                }
                if (allRecharNum < rechargeLevel) {
                    continue;
                }
                RechargeTarget rechargeTarget = rechargeTotalData.getRechargeTargetMap().get(rechargeLevel);
                List<Integer> customList = new ArrayList<>();
                if (customReward.containsKey(rechargeLevel)) {
                    customList.addAll(new ArrayList<>(customReward.get(rechargeLevel).values()));
                }

                if (customList.size() < rechargeTarget.getCustomlen()) {
                    int count = 0;
                    int max = rechargeTarget.getCustomlen() - customList.size();
                    for (RewardData rewardData :rechargeTarget.getCustomRewardMap()){
                        if (count >= max) {
                            break;
                        }
                        if (customList.contains(rewardData.getI())) {
                            continue;
                        }
                        if (!Manager.activityManager.checkCareer(player, rewardData.getC())) {
                            continue;
                        }
                        customList.add(rewardData.getI());
                        count++;
                    }
                }

                List<Item> itemList = getRechageReward( player, rechargeTarget.getFixedRewardMap());
                for (Integer itemID:customList){
                    RewardData rewardData = getRewardData(rechargeTarget.getCustomRewardMap(),itemID);
                    if (Manager.activityManager.checkCareer(player, rewardData.getC())){
                        Item item =  Item.createItem(rewardData.getI(),rewardData.getN(),rewardData.getB() > 0);
                        if (item == null){
                            continue;
                        }
                        itemList.add(item);
                    }
                }
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.Recharge_Totle_Mail_Title, MessageString.Recharge_Totle_Mail, itemList, ItemChangeReason.RechargeTotalActivityGet);
            }
            //活动结束清理玩家数据
            roleActDataMap.clear();
           // 保存角色活动数据
            Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        }
    }

    private List<Item> getRechageReward(Player player, List< RewardData> fixedRewardMap){
        List<Item> itemList = new ArrayList<>();
        for (RewardData rewardData: fixedRewardMap){
            if (Manager.activityManager.checkCareer(player, rewardData.getC())){
                itemList.addAll(Item.createItems(rewardData.getI(),rewardData.getN(),rewardData.getB() > 0,0,0,0));

            }
        }
        return itemList;
    }

    public void zeroClockDeal( ActivityConfig actCfg) {

    }

    @Override
    public void fiveClockDeal(ActivityConfig actCfg) {

    }

    @Override
    public void everyHourDeal(ActivityConfig actCfg) {

    }

    /**
     * 活动掉落
     *
     * @param player
     * @param bossId
     * @return
     */
    @Override
    public boolean bossDrop(Player player, int bossId, ActivityConfig actCfg) {
        return false;
    }

    /**
     * 活动掉落
     *
     * @param player
     * @param boxId
     * @return
     */
    @Override
    public boolean boxDrop(Player player, int boxId, ActivityConfig actCfg) {
        return false;
    }

    /**
     * 副本掉落
     *
     * @param player
     * @param cloneId
     * @return
     */
    @Override
    public boolean cloneDrop(Player player, int cloneId, ActivityConfig actCfg) {
        return false;
    }

    public void consumeDeal(Player player,int coinType, int consumeNum, ActivityConfig actCfg){

    }

    private boolean isContainsKey(List<RewardData> rewardDataList,int id){
        for (RewardData rewardData : rewardDataList){
            if (rewardData.getI() == id) {
                return true;
            }
        }
        return false;
    }

    private RewardData getRewardData(List<RewardData> rewardDataList,int id){
        for (RewardData rewardData : rewardDataList){
            if (rewardData.getI() == id) {
                return rewardData;
            }
        }
        return null;
    }

    public void testConfig(){

    }
    static class RechargeTotalData{

        private String client ;

        //充值目标金额为KEY，
        private HashMap<Integer,RechargeTarget> rechargeTargetMap = new HashMap<>();

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public HashMap<Integer, RechargeTarget> getRechargeTargetMap() {
            return rechargeTargetMap;
        }

        public void setRechargeTargetMap(HashMap<Integer, RechargeTarget> rechargeTargetMap) {
            this.rechargeTargetMap = rechargeTargetMap;
        }
    }

    /**
     * 每项充值的达成目标
     */
    static class RechargeTarget{

        private int targetRcharge;//充值达成条件

        private int customlen = 0;

        //ItemId
        private List<RewardData> fixedRewardMap  = new ArrayList<>();

        private List<RewardData> customRewardMap = new ArrayList<>();

        public int getCustomlen() {
            return customlen;
        }

        public void setCustomlen(int customlen) {
            this.customlen = customlen;
        }

        public int getTargetRcharge() {
            return targetRcharge;
        }

        public void setTargetRcharge(int targetRcharge) {
            this.targetRcharge = targetRcharge;
        }

        public List<RewardData> getFixedRewardMap() {
            return fixedRewardMap;
        }

        public void setFixedRewardMap(List<RewardData> fixedRewardMap) {
            this.fixedRewardMap = fixedRewardMap;
        }

        public List<RewardData> getCustomRewardMap() {
            return customRewardMap;
        }

        public void setCustomRewardMap(List<RewardData> customRewardMap) {
            this.customRewardMap = customRewardMap;
        }
    }
}
