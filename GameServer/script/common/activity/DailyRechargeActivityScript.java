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
 * Ежедневное пополнение 300002
 */
public class DailyRechargeActivityScript implements IActivityScript {


    public static final String DailyRechargeDataStr = "dailyRechargeData";
    public static final String CurRechargeDay = "curRechargeDay";
    public static final String IsGetReward = "isGetReward";//Флаг получения награды
    public static final String IsGetTotalReward = "IsGetTotalReward";//Накопительная награда
    public static final String RechargeNum = "rechargeNum"; //Сумма пополнения
    public static final Logger LOGGER = LogManager.getLogger(DailyRechargeActivityScript.class);

    @Override
    public int getId() {
        return ScriptEnum.DailyRechargeActivityScript;
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
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.DailyRecharge);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(DailyRechargeDataStr);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            DailyRechargeData newData = JsonUtils.toJavaObject(customStr, DailyRechargeData.class);
            activityConfig.getCustomCfgMap().put(DailyRechargeDataStr, newData);
        }
    }

    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {

        if (actCfg == null) {
            return;
        }
        int actType = actCfg.getType();

        int startDay = TimeUtils.getCurDayByTime(actCfg.getBeginTime());
        int curDay = TimeUtils.getCurDayByTime(TimeUtils.Time());
        int overDay = TimeUtils.getCurDayByTime(actCfg.getEndTime());

        if (curDay < startDay || curDay > overDay) {
            LOGGER.error("Время активности истекло" + actType);
            return;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actType);
        roleActDataMap.putIfAbsent(CurRechargeDay, 1);
        roleActDataMap.putIfAbsent(IsGetReward, 0);
        roleActDataMap.putIfAbsent(RechargeNum, 0L);
        roleActDataMap.putIfAbsent(IsGetTotalReward, 0);


        DailyRechargeData dailyRechargeData = (DailyRechargeData) actCfg.getCustomCfgMap().get(DailyRechargeDataStr);
        int isGetReward = (int) roleActDataMap.get(IsGetReward);
        int curRechargeDay = (int) roleActDataMap.get(CurRechargeDay);
        long rechargeNum = Long.parseLong(roleActDataMap.get(RechargeNum).toString());
        int isGetTotalReward = (int) roleActDataMap.get(IsGetTotalReward);


        if (!dailyRechargeData.getTargetDataMap().containsKey(curRechargeDay)) {
            return;
        }
        DailyTargetData targetData = dailyRechargeData.getTargetDataMap().get(curRechargeDay);
        if (rechargeNum < targetData.getRechargeTarget()) {
            return;
        }

        List<Item> itemList = new ArrayList<>();
        int reason = ItemChangeReason.DailyRechargeAcitvityGet;
        if (dataStr.contains("GetNormalReward")) {
            if (isGetReward > 0) {
                LOGGER.info("Награда уже получена " + curRechargeDay);
                return;
            }
            for (RewardData rewardData : targetData.getRewardDatas()) {
                if (Manager.activityManager.checkCareer(player, rewardData.getC())){
                    itemList.addAll(Item.createItems(rewardData.getI(), rewardData.getN(), rewardData.getB() == 0 ? false : true));
                }
            }
            roleActDataMap.put(IsGetReward, 1);
        } else if (dataStr.contains("GetTotalReward")) {
            reason = ItemChangeReason.DailyRechargeAcitvityTotalGet;
            if (isGetTotalReward > 0) {
                LOGGER.info("Накопительная награда уже получена ");
                return;
            }
            for (RewardData rewardData : targetData.getTotalRewardDatas()) {
                if (Manager.activityManager.checkCareer(player, rewardData.getC())){
                    itemList.addAll(Item.createItems(rewardData.getI(), rewardData.getN(), rewardData.getB() == 0 ? false : true));
                }
            }
            roleActDataMap.put(IsGetTotalReward, 1);
        }
        if (itemList.size() > 0) {
            long actionId = IDConfigUtil.getLogId();
            if (!Manager.backpackManager.manager().addItems(player, itemList, reason, actionId)) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, itemList, reason, actionId);
            }
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.DailyRecharge, reason);
        }


        Manager.activityManager.deal().onReqActivity(player, actType);
        //Сохранение данных активности игрока
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {

        DailyRechargeData data = JsonUtils.parseObject(customStr, DailyRechargeData.class);
        actCfg.getCustomCfgMap().put(DailyRechargeDataStr, data);
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
        roleActDataMap.putIfAbsent(CurRechargeDay, 1);
        roleActDataMap.putIfAbsent(RechargeNum, 0L);
        roleActDataMap.putIfAbsent(IsGetReward, 0);
        roleActDataMap.putIfAbsent(IsGetTotalReward, 0);


        int curday = (int) roleActDataMap.get(CurRechargeDay);
        long rechargeNum = Long.parseLong(roleActDataMap.get(RechargeNum).toString());
        int isGetReward = (int) roleActDataMap.get(IsGetReward);
        int isGetTotalReward = (int) roleActDataMap.get(IsGetTotalReward);

        HashMap<String, Object> data = new HashMap<>();
        data.put(CurRechargeDay, curday);
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
        DailyRechargeData dailyRechargeData = (DailyRechargeData) actCfg.getCustomCfgMap().get(DailyRechargeDataStr);
        if (dailyRechargeData == null) {
            LOGGER.error("dailyRechargeData is null " + actType);
            return;
        }

        int startDay = TimeUtils.getCurDayByTime(actCfg.getBeginTime());
        int curDay = TimeUtils.getCurDayByTime(TimeUtils.Time());
        int overDay = TimeUtils.getCurDayByTime(actCfg.getEndTime());

        if (curDay < startDay || curDay > overDay) {
            LOGGER.error("Время активности истекло" + actType);
            return;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actType);
        int curRechargeDay = (int) roleActDataMap.get(CurRechargeDay);
        if (!dailyRechargeData.getTargetDataMap().containsKey(curRechargeDay)) {
            return;
        }
        long oldRechargeNum = Long.parseLong(roleActDataMap.get(RechargeNum).toString());
        roleActDataMap.put(RechargeNum, oldRechargeNum + rechargeNum);

        Manager.activityManager.deal().onReqActivity(player, actType);
        //Сохранение данных активности игрока
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
        if (!actCfg.getCustomCfgMap().containsKey(DailyRechargeDataStr)) {
            return;
        }
        int curDay = TimeUtils.getCurDayByTime(TimeUtils.Time());
        int overDay = TimeUtils.getCurDayByTime(actCfg.getEndTime());
        if (curDay > overDay + 1) {
            LOGGER.error("Время активности истекло" + actType);
            return;
        }

        ConcurrentHashMap<Long, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>>> roleActDatas = Manager.activityManager.getRoleActDatas();
        DailyRechargeData dailyRechargeData = (DailyRechargeData) actCfg.getCustomCfgMap().get(DailyRechargeDataStr);
        for (Map.Entry<Long, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>>> entry : roleActDatas.entrySet()) {
            ConcurrentHashMap<String, Object> roleActDataMap = entry.getValue().get(actType);
            if (roleActDataMap == null) {
                continue;
            }
            Player player = Manager.playerManager.getPlayer(entry.getKey());
            if (player == null) {
                LOGGER.error("player == null" + entry.getKey());
                continue;
            }
            int isGetReward = (int) roleActDataMap.getOrDefault(IsGetReward, 0);
            int curRechargeDay = (int) roleActDataMap.getOrDefault(CurRechargeDay, 0);
            long rechargeNum = Long.parseLong(roleActDataMap.getOrDefault(RechargeNum, 0L).toString());
            int isGetTotalReward = (int) roleActDataMap.getOrDefault(IsGetTotalReward, 0);
            if (isGetReward <= 0) {
                if (!dailyRechargeData.getTargetDataMap().containsKey(curRechargeDay)) {
                    continue;
                }
                DailyTargetData targetData = dailyRechargeData.getTargetDataMap().get(curRechargeDay);
                if (rechargeNum >= targetData.getRechargeTarget()) {
                    List<Item> itemList = new ArrayList<>();
                    for (RewardData rewardData : targetData.getRewardDatas()) {
                        if (Manager.activityManager.checkCareer(player, rewardData.getC())) {
                            itemList.addAll(Item.createItems(rewardData.getI(), rewardData.getN(), rewardData.getB() == 1));
                        }
                    }
                    Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                            MessageString.Daily_Recharge_Mail_Title, MessageString.Daily_Recharge_Mail, itemList, ItemChangeReason.DailyRechargeAcitvityGet);
                    roleActDataMap.put(CurRechargeDay, curRechargeDay + 1);
                    Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.DailyRecharge, ItemChangeReason.DailyRechargeAcitvityGet);
                }
            } else {
                roleActDataMap.put(CurRechargeDay, curRechargeDay + 1);
            }

            if (isGetTotalReward <= 0) {
                if (!dailyRechargeData.getTargetDataMap().containsKey(curRechargeDay)) {
                    continue;
                }
                DailyTargetData targetData = dailyRechargeData.getTargetDataMap().get(curRechargeDay);
                if (rechargeNum >= targetData.getRechargeTarget()) {
                    List<Item> itemList = new ArrayList<>();
                    for (RewardData rewardData : targetData.getTotalRewardDatas()) {
                        if (Manager.activityManager.checkCareer(player, rewardData.getC())) {
                            itemList.addAll(Item.createItems(rewardData.getI(), rewardData.getN(), rewardData.getB() == 0 ? false : true));
                        }
                    }
                    if (itemList.size() > 0) {
                        Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                                MessageString.Daily_Recharge_Mail_Title, MessageString.Daily_Recharge_Mail, itemList, ItemChangeReason.DailyRechargeAcitvityTotalGet);
                        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.DailyRecharge, ItemChangeReason.DailyRechargeAcitvityTotalGet);
                    }
                }
            }
            roleActDataMap.put(IsGetTotalReward, 0);
            roleActDataMap.put(RechargeNum, 0L);
            roleActDataMap.put(IsGetReward, 0);
            if (player.isOnline()) {
                Manager.activityManager.deal().onReqActivity(player, actType);
            }
            //Сохранение данных активности игрока
            Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        }
    }

    @Override
    public void fiveClockDeal(ActivityConfig actCfg) {

    }

    @Override
    public void everyHourDeal(ActivityConfig actCfg) {

    }

    /**
     * Выпадение с босса
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
     * Выпадение из сундука
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
     * Выпадение в подземелье
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
    static class DailyRechargeData {

        private String client;

        //key день (номер дня) -> соответствующая награда
        private HashMap<Integer, DailyTargetData> targetDataMap = new HashMap<>();

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public HashMap<Integer, DailyTargetData> getTargetDataMap() {
            return targetDataMap;
        }

        public void setTargetDataMap(HashMap<Integer, DailyTargetData> targetDataMap) {
            this.targetDataMap = targetDataMap;
        }
    }

    static class DailyTargetData {

        private int day;//Текущий день активности

        private int rechargeTarget;//Целевая сумма пополнения

        private List<RewardData> rewardDatas = new ArrayList<>();//Фиксированная награда

        private List<RewardData> totalRewardDatas = new ArrayList<>();//Накопительная награда

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getRechargeTarget() {
            return rechargeTarget;
        }

        public void setRechargeTarget(int rechargeTarget) {
            this.rechargeTarget = rechargeTarget;
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