package common.activity;

import com.data.ItemChangeReason;
import com.data.MessageString;

import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityType;
import com.game.activity.struct.RewardData;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * 累计消耗 300007
 * Created by cxl on 2020/8/20.
 */
public class TotalConsumeActivityScript  implements IActivityScript {

    private static final Logger LOGGER = LogManager.getLogger(TotalConsumeActivityScript.class);

    static class TotalConsumeData{

        private String client ;

        //消耗类型
        private int coinType;

        //充值目标金额为KEY，
        private HashMap<Integer,TotalConsumeTarget> totalConsumeTargetMap = new HashMap<>();

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public HashMap<Integer, TotalConsumeTarget> getTotalConsumeTargetMap() {
            return totalConsumeTargetMap;
        }

        public void setTotalConsumeTargetMap(HashMap<Integer, TotalConsumeTarget> totalConsumeTargetMap) {
            this.totalConsumeTargetMap = totalConsumeTargetMap;
        }

        public int getCoinType() {
            return coinType;
        }

        public void setCoinType(int coinType) {
            this.coinType = coinType;
        }
    }

    /**
     * 每项充值的达成目标
     */
    static class TotalConsumeTarget{

        private int targetConsume;//充值达成条件

        private int customlen = 0;

        //ItemId
        private List<RewardData> fixedRewardMap = new ArrayList<>();

        private List<RewardData> customRewardMap = new ArrayList<>();


        public int getCustomlen() {
            return customlen;
        }

        public void setCustomlen(int customlen) {
            this.customlen = customlen;
        }


        public int getTargetConsume() {
            return targetConsume;
        }

        public void setTargetConsume(int targetConsume) {
            this.targetConsume = targetConsume;
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

    private final static String TotalConsumelData = "totalConsumelData";
    private final static String ConsumeNum        = "consumeNum";
    private final static String AlreadyGet        = "alreadyGet";
    private final static String CustomReward      = "customReward";


    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {

        HashMap<String, Object>  rechargeMap =  JsonUtils.parseObject(dataStr, new TypeReference<HashMap<String, Object>>(){});
        if (actCfg == null){
            return;
        }
        int actType = actCfg.getType();
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actType);
        roleActDataMap.putIfAbsent(ConsumeNum,0);
        roleActDataMap.putIfAbsent(CustomReward,new  HashMap<Integer,HashMap<Integer,Integer>>());
        HashMap<Integer,HashMap<Integer,Integer>> customRewardMap = (HashMap<Integer,HashMap<Integer,Integer>>) roleActDataMap.get(CustomReward);
        TotalConsumeData totalConsumeData =  (TotalConsumeData)actCfg.getCustomCfgMap().get(TotalConsumelData);
        //领奖处理
        if (checkIsGetReward(rechargeMap)){
            getActivityReward( player,  rechargeMap, roleActDataMap, totalConsumeData, actType,actCfg);
            //设置自定义奖励
        }else if (checkIsSetCustomReward(rechargeMap)){
            setCustomReward( player,  rechargeMap, totalConsumeData, customRewardMap, actType);
        }
    }

    //领奖
    private void getActivityReward(Player player,HashMap<String, Object>  rechargeMap,
                                  ConcurrentHashMap<String, Object> roleActDataMap,
                                  TotalConsumeData totalConsumeData,
                                  int actType,ActivityConfig actCfg){

        HashMap<Integer,HashMap<Integer,Integer>> customRewardMap = (HashMap<Integer,HashMap<Integer,Integer>>) roleActDataMap.get(CustomReward);
        long  roleTimeConsumption  =  Manager.activityManager.getRoleTimeConsumption(player.getId(),actCfg);
        long allConsumeNum = Long.parseLong(roleActDataMap.get(ConsumeNum).toString()) + roleTimeConsumption;
        List<Integer> alreadyGetList = (List<Integer>) roleActDataMap.get(AlreadyGet);
        int level = (int)rechargeMap.get("getReward");
        if (!totalConsumeData.getTotalConsumeTargetMap().containsKey(level)){
            LOGGER.error("not find this level recharge "  + level);
            return;
        }
        TotalConsumeTarget totalConsumeTarget = totalConsumeData.getTotalConsumeTargetMap().get(level);
        if (alreadyGetList.contains(level)){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GUILD_ACTIVITY_BOSS_HASREWARD);
            return;
        }
        if (allConsumeNum < totalConsumeTarget.getTargetConsume()){
            LOGGER.info("消耗金额不足  "  + allConsumeNum);
            return;
        }
        HashMap<Integer,Integer> coustomList = new HashMap<>();
        if (totalConsumeTarget.getCustomlen() > 0){
            if (!customRewardMap.containsKey(level)){
                LOGGER.info("自定义奖励没有设置   "  + level);
                return;
            }
            coustomList =  customRewardMap.get(level);
            if (coustomList.size() < totalConsumeTarget.getCustomlen()){
                LOGGER.info("自定义奖励没有设置完成还差   "  + (totalConsumeTarget.getCustomlen() - coustomList.size()));
                return;
            }
            for (Integer id : coustomList.values()){
                if (!isContainsKey(totalConsumeTarget.getCustomRewardMap(),id)){
                    LOGGER.info("领取的自定义奖励和配置不匹配    "  + id);
                    return;
                }
            }
        }
        List<Item> itemList = createRewardList( player, totalConsumeTarget.getFixedRewardMap());
        for (Integer itemID:coustomList.values()){
            RewardData rewardData = getRewardData(totalConsumeTarget.getCustomRewardMap(),itemID) ;
            if (Manager.activityManager.checkCareer(player, rewardData.getC())){
                Item item =  Item.createItem(rewardData.getI(),rewardData.getN(),rewardData.getB() > 0);
                if (item == null){
                    continue;
                }
                itemList.add(item);
            }
        }
        long actionId = IDConfigUtil.getLogId();
        if ( !Manager.backpackManager.manager().addItems(player,itemList, ItemChangeReason.TotalConsumeActivityGet, actionId)){
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, itemList, ItemChangeReason.TotalConsumeActivityGet, actionId);
        }
        alreadyGetList.add(level);
        roleActDataMap.put(AlreadyGet,alreadyGetList);
        Manager.activityManager.deal().onReqActivity(player,actType);
        //保存角色活动数据
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        //记录BI
//            Manager.biManager.getScript().biActivity(player, ItemChangeReason.TotalConsumeActivityGet, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.TotalConsume, ItemChangeReason.TotalConsumeActivityGet, level);
    }

    //设置自定义奖励
    private void setCustomReward(Player player, HashMap<String, Object>  rechargeMap,
                                 TotalConsumeData totalConsumeData,
                                 HashMap<Integer,HashMap<Integer,Integer>> customRewardMap,
                                 int actType) {

        int  level = JsonUtils.parseObject(rechargeMap.get(CustomReward).toString() , Integer.class);
        int index =  (Integer)rechargeMap.get("index");
        int value =  (Integer)rechargeMap.get("value");
        if (!totalConsumeData.getTotalConsumeTargetMap().containsKey(level)){
            LOGGER.info("not find this level recharge "  + level);
            return;
        }
        TotalConsumeTarget totalConsumeTarget = totalConsumeData.getTotalConsumeTargetMap().get(level);
        if (index+1>totalConsumeTarget.getCustomlen()){
            LOGGER.info("设置自定义索引超标   index "  + index  + " Customlen "  +totalConsumeTarget.getCustomlen());
            return;
        }

        if (!isContainsKey(totalConsumeTarget.getCustomRewardMap(),value)){
            LOGGER.info("设置的自定义奖励不存在    "  + value);
            return;
        }
        if (!Manager.activityManager.checkCareer(player, getRewardData(totalConsumeTarget.getCustomRewardMap(),value).getC())){
            LOGGER.info("设置自定义奖励与职业不匹配   id  "  + value  +" player.getCareer() " + player.getCareer());
            return;
        }
        if (!customRewardMap.containsKey(level)){
            HashMap<Integer,Integer> itemlist   = new HashMap<>();
            itemlist.put(index,value);
            customRewardMap.put(level,itemlist);
        }else {
            customRewardMap.get(level).put(index,value);
        }
        Manager.activityManager.deal().onReqActivity(player,actType);
        //保存角色活动数据
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
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

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        TotalConsumeActivityScript.TotalConsumeData data = JsonUtils.toJavaObject(customStr, TotalConsumeActivityScript.TotalConsumeData.class);
        actCfg.getCustomCfgMap().put(TotalConsumelData,data);
        actCfg.getCustomCfgMap().put("client",data.getClient());
        return true;
    }

    @Override
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr) {
        parseCustomConfig( actCfg,  customStr);
        return false;
    }

    @Override
    public String getActivityDataStr(ActivityConfig actCfg, long roleId) {

        int actType = actCfg.getType();
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId, actType);
        long  roleTimeConsumption =  Manager.activityManager.getRoleTimeConsumption(roleId,actCfg);
        roleActDataMap.putIfAbsent(ConsumeNum,  0);
        roleActDataMap.putIfAbsent(AlreadyGet,new ArrayList<Integer>());
        roleActDataMap.putIfAbsent(CustomReward,new HashMap<Integer,HashMap<Integer,Integer>>());

        List<Integer> alreadyGetList = (List<Integer>)roleActDataMap.get(AlreadyGet);
        long allRecharNum = Long.parseLong(roleActDataMap.get(ConsumeNum).toString())  + roleTimeConsumption;
        HashMap<Integer,HashMap<Integer,Integer>> customRewardTemp =  (HashMap<Integer,HashMap<Integer,Integer>>) roleActDataMap.get(CustomReward);
        HashMap<String,Object> rechargeData = new HashMap<>();
        rechargeData.put(ConsumeNum,allRecharNum);
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

    @Override
    public void rechargeDeal(Player player,int getGoodsCfgId, int rechargeNum, ActivityConfig actCfg) {

    }

    public void consumeDeal(Player player,int coinType, int consumeNum, ActivityConfig actCfg){
        if (actCfg == null){
            return;
        }
        //只统计金元宝消耗
        if (ItemCoinType.GemCoin != coinType){
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
        roleActDataMap.putIfAbsent(ConsumeNum,0);
        long allRecharNum = Long.parseLong(roleActDataMap.get(ConsumeNum).toString());
        allRecharNum +=consumeNum;
        roleActDataMap.put(ConsumeNum , allRecharNum);


        //保存角色活动数据
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        Manager.activityManager.deal().onReqActivity(player,actType);


    }

    @Override
    public void playerOnline(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void zeroClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void fiveClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void zeroClockDeal(ActivityConfig actCfg) {

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

    @Override
    public void activityEndDeal(ActivityConfig actCfg) {
        if (actCfg == null){
            return;
        }
        int actType = actCfg.getType();
        if (!actCfg.getCustomCfgMap().containsKey(TotalConsumelData)){
            return;
        }
        TotalConsumeActivityScript.TotalConsumeData totalConsumeData =  (TotalConsumeActivityScript.TotalConsumeData)actCfg.getCustomCfgMap().get(TotalConsumelData);
        if (totalConsumeData == null){
            LOGGER.error("rechargeTotalData is null ");
            return;
        }
        for (Map.Entry<Long, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>>> entry: Manager.activityManager.getRoleActDatas().entrySet()) {
            ConcurrentHashMap<String, Object> roleActDataMap = entry.getValue().get(actType);
            if (roleActDataMap == null){
                continue;
            }
            if (!roleActDataMap.containsKey(AlreadyGet)){
                continue;
            }
            if(!roleActDataMap.containsKey(ConsumeNum)){
                continue;
            }
            if (!roleActDataMap.containsKey(CustomReward)){
                continue;
            }
            List<Integer> alreadyGetList = (List<Integer>) roleActDataMap.get(AlreadyGet);
            long  roleTimeConsumption  =  Manager.activityManager.getRoleTimeConsumption(entry.getKey(),actCfg);
            long allRecharNum = Long.parseLong(roleActDataMap.get(ConsumeNum).toString())  + roleTimeConsumption;
            HashMap<Integer, HashMap<Integer, Integer>> customReward = (HashMap<Integer, HashMap<Integer, Integer>>) roleActDataMap.get(CustomReward);

            Player player = Manager.playerManager.getPlayer(entry.getKey());
            if (player == null){
                LOGGER.error("player  == null"  + entry.getKey());
                continue;
            }
            for (Integer rechargeLevel : totalConsumeData.getTotalConsumeTargetMap().keySet()) {
                if (alreadyGetList.contains(rechargeLevel)) {
                    continue;
                }
                if (allRecharNum < rechargeLevel) {
                    continue;
                }
                TotalConsumeActivityScript.TotalConsumeTarget totalConsumeTarget = totalConsumeData.getTotalConsumeTargetMap().get(rechargeLevel);
                List<Integer> customList = new ArrayList<>();
                if (customReward.containsKey(rechargeLevel)) {
                    customList.addAll(new ArrayList<>(customReward.get(rechargeLevel).values()));
                }

                if (customList.size() < totalConsumeTarget.getCustomlen()) {
                    int count = 0;
                    int max = totalConsumeTarget.getCustomlen() - customList.size();
                    for (RewardData rewardData : totalConsumeTarget.getCustomRewardMap()) {
                        if (customList.contains(rewardData.getI())) {
                            continue;
                        }
                        if (count >= max) {
                            break;
                        }
                        if (!Manager.activityManager.checkCareer(player, rewardData.getC())) {
                            continue;
                        }
                        customList.add(rewardData.getI());
                        count++;
                    }
                }

                List<Item> itemList = createRewardList( player, totalConsumeTarget.getFixedRewardMap());
                for (Integer itemID:customList){
                    RewardData rewardData = getRewardData(totalConsumeTarget.getCustomRewardMap(),itemID);
                    if (Manager.activityManager.checkCareer(player, rewardData.getC())){
                        Item item =  Item.createItem(rewardData.getI(),rewardData.getN(),rewardData.getB() > 0 ?true:false);
                        if (item == null){
                            continue;
                        }
                        itemList.add(item);
                    }
                }
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.Use_Add_Reward_Mail_Title, MessageString.Use_Add_Reward_Mail, itemList, ItemChangeReason.TotalConsumeActivityGet);
            }
            //活动结束清理玩家数据
            roleActDataMap.clear();
            // 保存角色活动数据
            Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        }
    }

    private List<Item> createRewardList(Player player, List<RewardData> fixedRewardMap){
        List<Item> itemList = new ArrayList<>();
        for (RewardData rewardData: fixedRewardMap){
            if (Manager.activityManager.checkCareer(player, rewardData.getC())){
                 itemList .addAll(  Item.createItems(rewardData.getI(),rewardData.getN(),rewardData.getB() >0,0,0,0));
            }
        }
        return itemList;
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

    @Override
    public int getId() {
        return ScriptEnum.LimitedTotalConsumeActivityScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void reload() {
        if(Manager.activityManager == null){
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.LimitedTotalConsume);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(TotalConsumelData);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            TotalConsumeData newData = JsonUtils.toJavaObject(customStr, TotalConsumeData.class);
            activityConfig.getCustomCfgMap().put(TotalConsumelData, newData);
        }
    }
}
