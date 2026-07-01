package common.activity;

import com.data.ItemChangeReason;
import com.data.MessageString;

import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityType;
import com.game.activity.struct.RewardData;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
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
 * Created by CXL on 2020/8/31.
 * 连续充值2 300021
 */
public class ContinuousRecharge2ActivityScript implements IActivityScript {


    public static final String ContinuousRecharge2Str = "continuousRecharge2";
    public static final String CurRechargeDay = "curRechargeDay";
    public static final String IsGetReward = "isGetReward";//是否领奖
    public static final String IsGetTotalReward = "IsGetTotalReward";//累计奖励
    public static final String RechargeID = "rechargeID"; //充值ID
    public static final String RechargeKey = "rechargeKey"; //充值目标Key
    public static final Logger LOGGER = LogManager.getLogger(ContinuousRecharge2ActivityScript.class);

    @Override
    public int getId() {
        return ScriptEnum.ContinuousRecharge2Script;
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
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.ContinuousRecharge2);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(ContinuousRecharge2Str);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            ContinuousRecharge2Data newData = JsonUtils.toJavaObject(customStr, ContinuousRecharge2Data.class);
            activityConfig.getCustomCfgMap().put(ContinuousRecharge2Str, newData);
        }
    }

    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {

        HashMap<String, Object>  dailyMap =  JsonUtils.parseObject(dataStr, new TypeReference<HashMap<String, Object>>(){});
        if (actCfg == null) {
            return;
        }
        int actType = actCfg.getType();
        ContinuousRecharge2Data dailyRechargeData = (ContinuousRecharge2Data) actCfg.getCustomCfgMap().get(ContinuousRecharge2Str);
        if (dailyRechargeData == null){
            LOGGER.error("ContinuousRechargeData is null " + actType);
            return;
        }
        int startDay = TimeUtils.getCurDayByTime(actCfg.getBeginTime());
        int curDay = TimeUtils.getCurDayByTime(TimeUtils.Time());
        int overDay = TimeUtils.getCurDayByTime(actCfg.getEndTime());

        if (curDay < startDay || curDay > overDay) {
            LOGGER.error("活动时间已结束" + actType);
            return;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actType);
        int rechargeKey = (int)dailyMap.get(RechargeKey);

        HashMap<String,Object> isGetTotalRewardMap = (HashMap<String,Object>) roleActDataMap.get(IsGetTotalReward);
        HashMap<String,Object> curRechargeDayMap = (HashMap<String,Object>) roleActDataMap.get(CurRechargeDay);

        HashMap<Integer, ContinuousRecharge2Target> targetDataMap = dailyRechargeData.getTargetDataMap().get(rechargeKey);
        if (targetDataMap == null){
            LOGGER.error("rechargeKey in not find {}", rechargeKey);
            return;
        }
        int curRechargeDay =  (int)curRechargeDayMap.get(rechargeKey + "");
        if (!targetDataMap.containsKey(curRechargeDay)) {
            return;
        }

        int isGetTotalReward =  (int)isGetTotalRewardMap.get(rechargeKey+"");
        if (isGetTotalReward > 0) {
            LOGGER.info("累计奖励已经领取 ");
            return;
        }
        ContinuousRecharge2Target  targetData =  targetDataMap.get(curRechargeDay);
        sendReward(player,targetData.getTotalRewardDatas(),true,actCfg);
        isGetTotalRewardMap.put(rechargeKey+"",1);
        Manager.activityManager.deal().onReqActivity(player, actType);
        //保存角色活动数据
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.ContinuousRechargeGet2, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.ContinuouRecharge2, ItemChangeReason.ContinuousRechargeDaysGet2, rechargeKey);
    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        ContinuousRecharge2Data data = JsonUtils.parseObject(customStr, ContinuousRecharge2Data.class);
        actCfg.getCustomCfgMap().put(ContinuousRecharge2Str, data);
        actCfg.getCustomCfgMap().put("client", data.getClient());
        return true;
    }

    @Override
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr) {

        return true;
    }

    @Override
    public String getActivityDataStr(ActivityConfig actCfg, long roleId) {
        int actType = actCfg.getType();
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId, actType);
        ContinuousRecharge2Data dailyRechargeData = (ContinuousRecharge2Data) actCfg.getCustomCfgMap().get(ContinuousRecharge2Str);
        if (dailyRechargeData == null){
            LOGGER.error("ContinuousRechargeData is null " + actType);
            return "";
        }

        roleActDataMap.putIfAbsent(CurRechargeDay, new HashMap<String,Object>());
        roleActDataMap.putIfAbsent(RechargeID, new ArrayList<Integer>());
        roleActDataMap.putIfAbsent(IsGetReward, new HashMap<String,Object>());
        roleActDataMap.putIfAbsent(IsGetTotalReward, new HashMap<String,Object>());

        HashMap<String,Object> rechargeTargetKey = (HashMap<String,Object>) roleActDataMap.get(CurRechargeDay);
        HashMap<String,Object> isGetReward = (HashMap<String,Object>) roleActDataMap.get(IsGetReward);
        HashMap<String,Object> isGetTotalReward = (HashMap<String,Object>) roleActDataMap.get(IsGetTotalReward);
        List<Integer> rechargeList =  (ArrayList<Integer>)roleActDataMap.get(RechargeID);

        for (int rechargeKey : dailyRechargeData.getTargetDataMap().keySet()){
            rechargeTargetKey.putIfAbsent(rechargeKey + "",1);
            isGetReward.putIfAbsent(rechargeKey + "",0);
            isGetTotalReward.putIfAbsent(rechargeKey + "",0);
        }


        HashMap<String, Object> data = new HashMap<>();
        data.put(CurRechargeDay, rechargeTargetKey);
        data.put(RechargeID, rechargeList);
        data.put(IsGetReward, isGetReward);
        data.put(IsGetTotalReward, isGetTotalReward);
        return JsonUtils.toJSONString(data);
    }

    @Override
    public void rechargeDeal(Player player,int getGoodsCfgId, int rechargeNum, ActivityConfig actCfg) {

        if (actCfg == null) {
            return;
        }
        int actType = actCfg.getType();
        ContinuousRecharge2Data dailyRechargeData = (ContinuousRecharge2Data) actCfg.getCustomCfgMap().get(ContinuousRecharge2Str);
        if (dailyRechargeData == null) {
            LOGGER.error("dailyRechargeData is null " + actType);
            return;
        }
        if (!dailyRechargeData.getTargetDataMap().containsKey(getGoodsCfgId)){
            return;
        }
        int startDay = TimeUtils.getCurDayByTime(actCfg.getBeginTime());
        int curDay = TimeUtils.getCurDayByTime(TimeUtils.Time());
        int overDay = TimeUtils.getCurDayByTime(actCfg.getEndTime());

        if (curDay < startDay || curDay > overDay) {
            LOGGER.error("活动时间已结束" + actType);
            return;
        }
        HashMap<Integer, ContinuousRecharge2Target> rechargeTargetHashMap = dailyRechargeData.getTargetDataMap().get(getGoodsCfgId);
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actType);
        HashMap<String,Object> curRechargeDayMap = (HashMap<String,Object>) roleActDataMap.getOrDefault(CurRechargeDay, new HashMap<String,Object>());
        HashMap<String,Object> isGetRewardMap = (HashMap<String,Object>) roleActDataMap.get(IsGetReward);
        List<Integer> rechargelist = (ArrayList<Integer>) roleActDataMap.get(RechargeID);
        if ( !curRechargeDayMap.containsKey(getGoodsCfgId + "" )){
             return;
        }
        if (rechargelist.contains(getGoodsCfgId)){
            return;
        }
        int curRechargeDay = (int)curRechargeDayMap.get(getGoodsCfgId+"");
        if (!rechargeTargetHashMap.containsKey(curRechargeDay)) {
            return;
        }

        ContinuousRecharge2Target  targetData =  rechargeTargetHashMap.get(curRechargeDay);
        sendReward(player,targetData.getRewardDatas(),true,actCfg);

        isGetRewardMap.put(getGoodsCfgId + "",1);
        rechargelist.add(getGoodsCfgId);
        roleActDataMap.put(RechargeID, rechargelist);
        roleActDataMap.put(IsGetReward,isGetRewardMap);

        Manager.activityManager.deal().onReqActivity(player, actType);
        //保存角色活动数据
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.ContinuousRechargeGet2, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.ContinuouRecharge2, ItemChangeReason.ContinuousRechargeGet2);
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
    public void zeroClockDeal( ActivityConfig actCfg) {

        if (actCfg == null) {
            return;
        }
        int actType = actCfg.getType();
        if (!actCfg.getCustomCfgMap().containsKey(ContinuousRecharge2Str)) {
            return;
        }
        ContinuousRecharge2Data dailyRechargeData = (ContinuousRecharge2Data) actCfg.getCustomCfgMap().get(ContinuousRecharge2Str);
        if (dailyRechargeData == null){
            return;
        }
        int curDay = TimeUtils.getCurDayByTime(TimeUtils.Time());
        int overDay = TimeUtils.getCurDayByTime(actCfg.getEndTime());
        if (curDay > overDay + 1) {
            LOGGER.error("活动时间已结束" + actType);
            return;
        }

        ConcurrentHashMap<Long, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>>> roleActDatas = Manager.activityManager.getRoleActDatas();
        for (Map.Entry<Long, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>>> entry : roleActDatas.entrySet()) {
            ConcurrentHashMap<String, Object> roleActDataMap = entry.getValue().get(actType);
            if (roleActDataMap == null) {
                continue;
            }
            Player player = Manager.playerManager.getPlayer(entry.getKey());
            if (player == null) {
                LOGGER.error("player  == null" + entry.getKey());
                continue;
            }
            HashMap<String,Object> getRewardMap = (HashMap<String,Object>) roleActDataMap.getOrDefault(IsGetReward, new HashMap<String,Object>());
            HashMap<String,Object> curRechargeDayMap = (HashMap<String,Object>) roleActDataMap.getOrDefault(CurRechargeDay, new HashMap<String,Object>());
            List<Integer> rechargelist = (ArrayList<Integer>) roleActDataMap.getOrDefault(RechargeID, new ArrayList<Integer>());
            HashMap<String,Object> getTotalRewardMap = ( HashMap<String,Object>) roleActDataMap.getOrDefault(IsGetTotalReward, new HashMap<String,Object>());

            for (Integer rechargeKey : dailyRechargeData.getTargetDataMap().keySet()){

                if (!curRechargeDayMap.containsKey(rechargeKey + "")){
                    continue;
                }
                if (!getTotalRewardMap.containsKey(rechargeKey + "")){
                    continue;
                }
                int curRechargeDay =  (int)curRechargeDayMap.get(rechargeKey + "");
                int isGetTotalReward = (int)getTotalRewardMap.get(rechargeKey + "");

                HashMap<Integer, ContinuousRecharge2Target> targetDataMap =  dailyRechargeData.getTargetDataMap().get(rechargeKey);
                if (!targetDataMap.containsKey(curRechargeDay)) {
                    LOGGER.error("连续充值 没有配置 该天的 数据 {}" , curRechargeDay);
                    continue;
                }
                ContinuousRecharge2Target targetData = targetDataMap.get(curRechargeDay);

                if (rechargelist.contains(rechargeKey)){
                    curRechargeDayMap.put(rechargeKey + "" ,curRechargeDay+1);
                    if (isGetTotalReward <= 0) {
                        sendReward(player,targetData.getTotalRewardDatas(),false,actCfg);
//                        Manager.biManager.getScript().biActivity(player, ItemChangeReason.ContinuousRechargeGet2, actCfg.getType(), actCfg.getId());
                        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.ContinuouRecharge2, ItemChangeReason.ContinuousRechargeDaysGet2, rechargeKey);
                    }
                }
                getRewardMap.put(rechargeKey + "",0);
                getTotalRewardMap.put(rechargeKey + "",0);
            }
            roleActDataMap.put(RechargeID, new ArrayList<Integer>());
            if (player.isOnline()) {
                Manager.activityManager.deal().onReqActivity(player, actType);
            }
            //保存角色活动数据
            Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        }

    }

    private void sendReward(Player player, List<RewardData> rewardDatas,boolean isBagPack, ActivityConfig actCfg){
        if (rewardDatas.size() <= 0){
            LOGGER.error("连续充值累计奖励 == null 运营没有配置 :");
            return;
        }
        List<Item> itemList = new ArrayList<>();
        for (RewardData rewardData :rewardDatas) {
            if (!Manager.activityManager.checkCareer(player, rewardData.getC())){
                continue;
            }
            itemList.addAll(Item.createItems(rewardData.getI(), rewardData.getN(), rewardData.getB() == 1));
        }
        if (itemList.size() <= 0){
            LOGGER.error("运营没有配置本职业的奖励 == null {} :",player.getCareer());
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        if (isBagPack){
            if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.ContinuousRechargeGet2, actionId)) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, itemList, ItemChangeReason.ContinuousRechargeGet2, actionId);
            }
        }else {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString. Festival_Recharge_Continuity_Mail_Title, MessageString. Festival_Recharge_Continuity_Mail
                    , itemList, ItemChangeReason.ContinuousRechargeGet2, actionId);
        }
    }

    @Override
    public void fiveClockDeal( ActivityConfig actCfg) {

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

    }

    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum, ActivityConfig actCfg) {

    }
    static class ContinuousRecharge2Data {

        private String client;

        //key 充值ID --rechargeitem表
        private HashMap<Integer,  HashMap<Integer, ContinuousRecharge2Target>> targetDataMap = new HashMap<>();

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public HashMap<Integer, HashMap<Integer, ContinuousRecharge2Target>> getTargetDataMap() {
            return targetDataMap;
        }

        public void setTargetDataMap(HashMap<Integer, HashMap<Integer, ContinuousRecharge2Target>> targetDataMap) {
            this.targetDataMap = targetDataMap;
        }
    }

    static class ContinuousRecharge2Target{

        private int day;//当前活动天

        private List<RewardData> rewardDatas = new ArrayList<>();//固定奖励

        private List<RewardData> totalRewardDatas = new ArrayList<>();//累计奖励

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public List<RewardData> getRewardDatas() {
            return rewardDatas;
        }

        public void setRewardDatas(List<RewardData> rewardDatas) {
            this.rewardDatas = rewardDatas;
        }

        public List<RewardData> getTotalRewardDatas() {
            return totalRewardDatas;
        }

        public void setTotalRewardDatas(List<RewardData> totalRewardDatas) {
            this.totalRewardDatas = totalRewardDatas;
        }
    }

}
