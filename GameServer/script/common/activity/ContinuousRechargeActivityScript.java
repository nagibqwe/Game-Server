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
 * 连续充值 300015
 */
public class ContinuousRechargeActivityScript implements IActivityScript {


    public static final String ContinuousRechargeStr = "continuousRecharge";
    public static final String CurRechargeDay = "curRechargeDay";
    public static final String IsGetReward = "isGetReward";//是否领奖
    public static final String IsGetTotalReward = "IsGetTotalReward";//累计奖励
    public static final String RechargeNum = "rechargeNum"; //充值数量
    public static final String RechargeKey = "rechargeKey"; //充值目标Key
    public static final Logger LOGGER = LogManager.getLogger(ContinuousRechargeActivityScript.class);

    @Override
    public int getId() {
        return ScriptEnum.ContinuousRechargeScript;
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
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.ContinuousRecharge);
        for (ActivityConfig activityConfig:actCfgList) {

            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(ContinuousRechargeStr);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            ContinuousRechargeData newData = JsonUtils.toJavaObject(customStr, ContinuousRechargeData.class);
            activityConfig.getCustomCfgMap().put(ContinuousRechargeStr, newData);
        }
    }

    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {

        HashMap<String, Object>  dailyMap =  JsonUtils.parseObject(dataStr, new TypeReference<HashMap<String, Object>>(){});
        if (actCfg == null) {
            return;
        }
        int actType = actCfg.getType();
        ContinuousRechargeData dailyRechargeData = (ContinuousRechargeData) actCfg.getCustomCfgMap().get(ContinuousRechargeStr);
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
        int rechargeKey = Integer.parseInt(dailyMap.get(RechargeKey).toString());
        String getReward =  (String) dailyMap.get("getReward");


        HashMap<String,Object> isGetRewardMap = (HashMap<String,Object>) roleActDataMap.get(IsGetReward);
        long rechargeNum = Long.parseLong(roleActDataMap.get(RechargeNum).toString());
        HashMap<String,Object> isGetTotalRewardMap = (HashMap<String,Object>) roleActDataMap.get(IsGetTotalReward);
        HashMap<String,Object> curRechargeDayMap = (HashMap<String,Object>) roleActDataMap.get(CurRechargeDay);

        HashMap<Integer, ContinuousRechargeTarget> targetDataMap = dailyRechargeData.getTargetDataMap().get(rechargeKey);
        if (targetDataMap == null){
            LOGGER.error("rechargeKey in not find {}", rechargeKey);
            return;
        }
        int curRechargeDay =  (int)curRechargeDayMap.get(rechargeKey + "");
        if (!targetDataMap.containsKey(curRechargeDay)) {
            return;
        }
        if (rechargeNum < rechargeKey) {
            return;
        }
        ContinuousRechargeTarget  targetData =  targetDataMap.get(curRechargeDay);
        if (getReward.contains("GetNormalReward")) {
            int isGetReward  = (int)isGetRewardMap.get(rechargeKey+"");
            if (isGetReward > 0) {
                LOGGER.info("已领取奖励 " + curRechargeDay);
                return;
            }
            sendReward(player,targetData.getRewardDatas(),true, actCfg);
            isGetRewardMap.put(rechargeKey+"", 1);
//            Manager.biManager.getScript().biActivity(player, ItemChangeReason.ContinuousRechargeGet,actCfg.getType(), actCfg.getId());
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.ContinuouRecharge, ItemChangeReason.ContinuousRechargeGet);
        } else if (getReward.contains("GetTotalReward")) {
           int isGetTotalReward =  (int)isGetTotalRewardMap.get(rechargeKey+"");
            if (isGetTotalReward > 0) {
                LOGGER.info("累计奖励已经领取 ");
                return;
            }
            sendReward(player,targetData.getTotalRewardDatas(),true, actCfg);
            isGetTotalRewardMap.put(rechargeKey+"",1);
//            Manager.biManager.getScript().biActivity(player, ItemChangeReason.ContinuousRechargeGet, actCfg.getType(), actCfg.getId());
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.ContinuouRecharge, ItemChangeReason.ContinuousRechargeGet);
        }
        Manager.activityManager.deal().onReqActivity(player, actType);
        //保存角色活动数据
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        ContinuousRechargeData data = JsonUtils.parseObject(customStr, ContinuousRechargeData.class);
        actCfg.getCustomCfgMap().put(ContinuousRechargeStr, data);
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

        ContinuousRechargeData dailyRechargeData = (ContinuousRechargeData) actCfg.getCustomCfgMap().get(ContinuousRechargeStr);
        if (dailyRechargeData == null){
            LOGGER.error("ContinuousRechargeData is null " + actType);
            return "";
        }

        roleActDataMap.putIfAbsent(CurRechargeDay, new HashMap<String,Object>());
        roleActDataMap.putIfAbsent(RechargeNum,0);
        roleActDataMap.putIfAbsent(IsGetReward, new HashMap<String,Object>());
        roleActDataMap.putIfAbsent(IsGetTotalReward, new HashMap<String,Object>());

        HashMap<String,Object> rechargeTargetKey = (HashMap<String,Object>) roleActDataMap.get(CurRechargeDay);
        HashMap<String,Object> isGetReward = (HashMap<String,Object>) roleActDataMap.get(IsGetReward);
        HashMap<String,Object> isGetTotalReward = (HashMap<String,Object>) roleActDataMap.get(IsGetTotalReward);
        long rechargeNum = Long.parseLong(roleActDataMap.get(RechargeNum).toString()) + Manager.activityManager.getRoleTimeRecharge(roleId,actCfg);
        for (int rechargeKey : dailyRechargeData.getTargetDataMap().keySet()){
            rechargeTargetKey.putIfAbsent(rechargeKey + "",1);
            isGetReward.putIfAbsent(rechargeKey + "",0);
            isGetTotalReward.putIfAbsent(rechargeKey + "",0);
        }


        HashMap<String, Object> data = new HashMap<>();
        data.put(CurRechargeDay, rechargeTargetKey);
        data.put(RechargeNum, rechargeNum);
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
        ContinuousRechargeData dailyRechargeData = (ContinuousRechargeData) actCfg.getCustomCfgMap().get(ContinuousRechargeStr);
        if (dailyRechargeData == null) {
            LOGGER.error("dailyRechargeData is null " + actType);
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

        long oldRechargeNum = Long.parseLong(roleActDataMap.get(RechargeNum).toString()) +  Manager.activityManager.getRoleTimeRecharge(player.getId(),actCfg);
        roleActDataMap.put(RechargeNum, oldRechargeNum + rechargeNum);

        Manager.activityManager.deal().onReqActivity(player, actType);
        //保存角色活动数据
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

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

        if (actCfg == null) {
            return;
        }
        int actType = actCfg.getType();
        if (!actCfg.getCustomCfgMap().containsKey(ContinuousRechargeStr)) {
            return;
        }
        ContinuousRechargeData dailyRechargeData = (ContinuousRechargeData) actCfg.getCustomCfgMap().get(ContinuousRechargeStr);
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
            long rechargeNum = Long.parseLong(roleActDataMap.putIfAbsent(RechargeNum,  0).toString()) + Manager.activityManager.getRoleTimeRecharge(entry.getKey(),actCfg);
            HashMap<String,Object> getTotalRewardMap = ( HashMap<String,Object>) roleActDataMap.getOrDefault(IsGetTotalReward, new HashMap<String,Object>());

            for (Integer rechargeKey : dailyRechargeData.getTargetDataMap().keySet()){
                if (!getRewardMap.containsKey(rechargeKey + "")){
                    continue;
                }
                if (!curRechargeDayMap.containsKey(rechargeKey + "")){
                    continue;
                }
                if (!getTotalRewardMap.containsKey(rechargeKey + "")){
                    continue;
                }
                int isGetReward = (int)getRewardMap.get(rechargeKey + "");
                int curRechargeDay =  (int)curRechargeDayMap.get(rechargeKey + "");
                int isGetTotalReward = (int)getTotalRewardMap.get(rechargeKey + "");

                HashMap<Integer, ContinuousRechargeTarget> targetDataMap =  dailyRechargeData.getTargetDataMap().get(rechargeKey);
                if (!targetDataMap.containsKey(curRechargeDay)) {
                    LOGGER.error("连续充值 没有配置 该天的 数据 {}" , curRechargeDay);
                    continue;
                }
                ContinuousRechargeTarget targetData = targetDataMap.get(curRechargeDay);
                if (isGetReward <= 0) {
                    if (rechargeNum >= rechargeKey) {
                        sendReward(player,targetData.getRewardDatas(),false,actCfg);
                        curRechargeDayMap.put(rechargeKey + "" ,curRechargeDay+1);
//                        Manager.biManager.getScript().biActivity(player, ItemChangeReason.ContinuousRechargeGet, actCfg.getType(), actCfg.getId());
                        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.ContinuouRecharge, ItemChangeReason.ContinuousRechargeGet);
                    }
                }else {
                    curRechargeDayMap.put(rechargeKey + "" ,curRechargeDay+1);
                }
                if (isGetTotalReward <= 0) {
                    if (rechargeNum >= rechargeKey) {
                        sendReward(player,targetData.getTotalRewardDatas(),false,actCfg);
//                        Manager.biManager.getScript().biActivity(player, ItemChangeReason.ContinuousRechargeGet, actCfg.getType(), actCfg.getId());
                        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.ContinuouRecharge, ItemChangeReason.ContinuousRechargeGet);
                    }
                }
                getRewardMap.put(rechargeKey + "",0);
                getTotalRewardMap.put(rechargeKey + "",0);
            }
            roleActDataMap.put(RechargeNum, 0L);
            if (player.isOnline()) {
                Manager.activityManager.deal().onReqActivity(player, actType);
            }
            //保存角色活动数据
            Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        }
    }

    private void sendReward(Player player, List<RewardData> rewardDatas,boolean isBagPack, ActivityConfig actCfg){
        List<Item> itemList = new ArrayList<>();
        for (RewardData rewardData :rewardDatas) {
            if (!Manager.activityManager.checkCareer(player, rewardData.getC())){
                continue;
            }
            itemList.addAll(Item.createItems(rewardData.getI(), rewardData.getN(), rewardData.getB() == 1));
        }
        //活动模块原因码 动态计算 55 原因码模块
        int itemChangeReason = 55 * 100000 + actCfg.getId();
        if (isBagPack){
            if (!Manager.backpackManager.manager().addItems(player, itemList, itemChangeReason, IDConfigUtil.getLogId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, itemList, itemChangeReason);
            }
        }else {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.Festival_Recharge_Continuity_Mail_Title, MessageString.Festival_Recharge_Continuity_Mail, itemList, itemChangeReason);
        }
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

    }

    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum, ActivityConfig actCfg) {

    }
    static class ContinuousRechargeData {

        private String client;

        //key 充值金额
        private HashMap<Integer,  HashMap<Integer, ContinuousRechargeTarget>> targetDataMap = new HashMap<>();

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public HashMap<Integer, HashMap<Integer, ContinuousRechargeTarget>> getTargetDataMap() {
            return targetDataMap;
        }

        public void setTargetDataMap(HashMap<Integer, HashMap<Integer, ContinuousRechargeTarget>> targetDataMap) {
            this.targetDataMap = targetDataMap;
        }
    }

    static class ContinuousRechargeTarget{

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
